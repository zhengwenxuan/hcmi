package com.hjw.webService.client.dashiqiao;

import java.awt.Robot;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.DTO.FileDTO;
import com.hjw.interfaces.FTPServer.PdfFtpHelper;
import com.hjw.interfaces.util.PacsPictureDecodeBase64Util;
import com.hjw.interfaces.util.PdfToJpg;
import com.hjw.service.ConfigService;
import com.hjw.util.Base64;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.Timeutils;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.dashiqiao.PacsResBean.PacsResPDF;
import com.hjw.webService.client.xhhk.bean.ResponseXHHK;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CommService;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PacsResMessageDSQ_GetFtp{
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private static CommService commService;
	private static String TPLJ;
    
	static {
    	init();
    }
	  
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		commService = (CommService) wac.getBean("commService");
		TPLJ = commService.getDatadis("TPLJ").getName();
	}

	public String getMessage(String strbody,String logname) {
		ResponseXHHK response = new ResponseXHHK();
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + strbody);
		PacsResPDF pacsPdf = new Gson().fromJson(strbody, PacsResPDF.class);
		String json="";
		
		ExamInfoUserDTO ei = getExaminfofindpatid(pacsPdf.getPATIENT_ID(),pacsPdf.getCLINIC_NO(),logname);
		if(ei.getId()<=0 || ei ==null){
			response.setCode(1);
			response.setMsg("通过PATIENT_ID或CLINIC_NO没有找到体检人信息");
		}else{
			try {
				PdfFtpHelper ftpHelper = new PdfFtpHelper();
				boolean login = ftpHelper.login(pacsPdf.getFTP_IP(), 21, pacsPdf.getFTP_USER(), pacsPdf.getFTP_PWD());
				TranLogTxt.liswriteEror_to_txt(logname, "访问ftp成功或失败===:" + login);
				
				if(login){
					//String url = pacsPdf.getFTP_URL().substring(1);  //永久目录
					String url = pacsPdf.getREPORT_TYPE();//临时目录
					TranLogTxt.liswriteEror_to_txt(logname, "访问ftp===url===:" + url);
					FileDTO fileDTO = ftpHelper.downloadFile(url,pacsPdf.getFILE_NAME(),false);
					if(fileDTO.isFlag()){
						String json2 = new Gson().toJson(fileDTO, FileDTO.class);
						
						
						byte[] bytes = fileDTO.getBytes();
						String base64Str = Base64.base64Encode(bytes);
						
						
						String picname = decodeBase64PDF(ei.getExam_num(),pacsPdf.getREPORT_TYPE(), DateTimeUtil.getDateTimes().substring(0, 8),base64Str);
						
						TranLogTxt.liswriteEror_to_txt(logname, "pdf转换为jpg路径===:" + picname);
						boolean flag = insertPdfVJpg(ei,pacsPdf,picname,logname);
						if(flag){
							response.setCode(1);
							response.setMsg("图片接收成功");
						}else{
							response.setCode(0);
							response.setMsg("图片接收失败");
						}
						TranLogTxt.liswriteEror_to_txt(logname, "pdf_v_jpg表插入数据成功或失败===:" + flag);
						
						
					
					}
				}else{
					response.setCode(1);
					response.setMsg("FTP拒绝连接!!");
					System.err.println("接口内FTP拒绝连接!!");
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
				response.setCode(1);
				response.setMsg("图片解析失败:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		}
		
		
		json = new Gson().toJson(response, ResponseXHHK.class);
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + json);
		return json;
	}

	//插入pdf和jgp图片关系表
	private boolean insertPdfVJpg(ExamInfoUserDTO ei, PacsResPDF pacsPdf, String jpgpath, String logname) {
		boolean flag=false;
		//pdf转jpg的图片 有可能会多张
		String[] splitjpgpath = jpgpath.split(";");
		for (int i = 0; i < splitjpgpath.length; i++) {
			//pdf路径
			String pdfpath = 	"ftp:/"+pacsPdf.getFTP_IP()+pacsPdf.getFTP_URL()+"/"+pacsPdf.getFILE_NAME();
			//jpg图片顺序码
			char jpgorder = splitjpgpath[i].charAt(jpgpath.indexOf(".jpg")-1);
			//jpg图片名称
			 String[] splitjpgname = splitjpgpath[i].split("/");
			 String jpgname = splitjpgname[splitjpgname.length-1];
			
			Connection tjtmpconnect = null;
			try {
				tjtmpconnect = this.jdbcQueryManager.getConnection();
				String delsql = " delete result_pdf_v_jpg where exam_num='"+ei.getExam_num()+"' and pdf_name='"+pacsPdf.getFILE_NAME()+"' and pdf_path='"+pdfpath+"' and result_type='"+pacsPdf.getREPORT_TYPE()+"' ";
				int rs = tjtmpconnect.createStatement().executeUpdate(delsql);
				
				TranLogTxt.liswriteEror_to_txt(logname, "res删除pdf与jpg关系表:" + delsql +"==执行成功数目="+rs +"\r\n");
				
				String insertsql = " insert into result_pdf_v_jpg values ('"+ei.getExam_num()+"','"+pacsPdf.getFILE_NAME()+"','"+pdfpath+"','"+pacsPdf.getREPORT_TYPE()+"',"
						+ " '"+jpgname+"','"+splitjpgpath[i]+"','"+jpgorder+"','"+pacsPdf.getNAME()+"','"+14+"','"+DateTimeUtil.getDateTime()+"') ";
				int count = tjtmpconnect.createStatement().executeUpdate(insertsql);
				TranLogTxt.liswriteEror_to_txt(logname, "res插入pdf与jpg关系表:" + delsql +"==执行成功数目="+count +"\r\n");
				if(count>0){
					flag=true;
				}else{
					flag=false;
				}
				
	        }catch(Exception ex){
	        	flag=false;
			}finally {
				try {
					if (tjtmpconnect != null) {
						tjtmpconnect.close();
					}
				} catch (SQLException sqle4) {
					sqle4.printStackTrace();
				}
			}
		}
		
		
		return flag;
	}

	private ExamInfoUserDTO getExaminfofindpatid(String pat_id, String arch_num, String logname) {
		String sql = " select * from exam_info e ,customer_info c where e.customer_id=c.id and e.patient_id='"+pat_id+"' and c.arch_num='"+arch_num+"'and e.is_Active='Y' and c.is_Active='Y' ";
		
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, ExamInfoUserDTO.class);
		TranLogTxt.liswriteEror_to_txt(logname, "通过患者 id档案号查询exam_info表:" + sql + "\r\n");
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	//通过pdf base64编码 转换为jpg图片
	public  String decodeBase64PDF(String exam_num, String res_type, String datetime, String base64Str) throws IOException {
		String picpath = TPLJ;
		File f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\pacs_img";
		String picname = "/" +"pacs_img";
		
		
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + res_type;
		picname = picname + "/" + res_type;
		
		
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		picpath = picpath + "\\" + datetime;
		picname = picname + "/" + datetime;
		
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + exam_num;
		picname = picname + "/" + exam_num;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		
		
		String filepath = exam_num + "-" + Timeutils.getFileData() + ".pdf";
		String jpgpath = exam_num;
		String pdfpath = picpath + "\\" + filepath;
		picpath = picpath + "\\" + jpgpath+"_"+Timeutils.getFileData();
		picname = picname + "/" + jpgpath+"_"+Timeutils.getFileData();
		FileOutputStream fos = null;
		try {
			f = new File(pdfpath);
			if (f.exists() && f.isFile())
				f.delete();
			fos = new FileOutputStream(pdfpath);
			// 用FileOutputStream 的write方法写入字节数组
			byte[] bmpfiledata64 = Base64.base64Decode(base64Str);
			fos.write(bmpfiledata64);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		PdfToJpg pjpg = new PdfToJpg();
		//支持多种转换方法  根据实际情况选定
		int picnum = pjpg.pdf2jpg1(pdfpath, picpath,100);
		String file_img = "";
		for (int j = 1; j <= picnum; j++) {
			file_img = file_img + ";" +picname + "_" + j + ".jpg";
		}
		if (file_img.length() > 1) {
			file_img = file_img.substring(1, file_img.length());
		}
		if (f.exists() && f.isFile())
			f.delete();
		return file_img;
	}
	
}
