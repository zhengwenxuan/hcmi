package com.hjw.webService.client.tj180;


import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.HttpServer.HttpDownloader;
import com.hjw.interfaces.util.PdfToJpg;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.HealthFileResBody;
import com.hjw.webService.client.tj180.client.CollectionInterface;
import com.hjw.webService.client.tj180.client.CollectionInterfaceLocator;
import com.hjw.webService.client.tj180.client.CollectionInterfaceSoap_PortType;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class HealthResMessageTj180{
   private static CommService commService;  
   private static JdbcQueryManager jdbcQueryManager;
   static{
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		commService = (CommService) wac.getBean("commService");
	}

	public HealthResMessageTj180(){
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public HealthFileResBody getMessage(String url, String exam_num, int stype, String logname) {
		HealthFileResBody hb = new HealthFileResBody();
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + exam_num);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		eu = this.getExamInfoForNum(exam_num, logname);
		if(stype==1){
			hb=getFile(url,eu.getId(),exam_num,logname);
		}else{
			hb = getHealthOne(eu.getId(),logname);
			if (!"AA".equals(hb.getRescode())){
				hb=getFile(url,eu.getId(),exam_num,logname);
			}
		}
		return hb;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	private ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times ");
		sb.append(" from exam_info c ");
		sb.append(" where c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
	
	private HealthFileResBody getFile(String url,long exam_id,String exam_num,String logname){
		
		HealthFileResBody hb=new HealthFileResBody();
		hb.setRescode("AE");
		hb.setRestext("获取图片失败");
		try {
			CollectionInterface cf=new CollectionInterfaceLocator(url);
			TranLogTxt.liswriteEror_to_txt(logname, "res:webservice 地址:" +url);
			CollectionInterfaceSoap_PortType cp=cf.getCollectionInterfaceSoap();
			String reportpath = cp.getAuditReportPath(exam_num);
			//String reportpath="http://localhost:81/201804250014.pdf";
			TranLogTxt.liswriteEror_to_txt(logname, "res:webservice "+exam_num+" 返回:" +reportpath);
			if(reportpath==null||reportpath.trim().length()<=0){
				hb.setRescode("AE");
				hb.setRestext("获取图片失败,服务返回为空！");
			}else if("1".equals(reportpath.trim())){
				hb.setRescode("AE");
				hb.setRestext("获取图片失败,服务返回为:没有报告");
			}else if("2".equals(reportpath.trim())){
				hb.setRescode("AE");
				hb.setRestext("获取图片失败,服务返回为:没有生成报告");
			}else if("3".equals(reportpath.trim())){
				hb.setRescode("AE");
				hb.setRestext("获取图片失败,服务返回为:报告未审核");
			}else if("4".equals(reportpath.trim())){
				hb.setRescode("AE");
				hb.setRestext("获取图片失败,服务返回为:报告审核未通过");
			}else if("5".equals(reportpath.trim())){
				hb.setRescode("AE");
				hb.setRestext("获取图片失败,服务返回为:文件缺失");
			}else{
				//hb=getFTPfile(exam_id,reportpath.trim(),logname);
				hb=getHTTPfile(exam_id,exam_num,reportpath.trim(),logname);
			}
		}catch(Exception ex){
			hb.setRescode("AA");
			hb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex)); 
		}
		//hb=getFTPfile(exam_id,"cf_201504151245_2015-04-20.pdf",logname);
		JSONObject json = JSONObject.fromObject(hb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "re1:" + str);
		return hb;
	}
	
	/**
	 * ftp获取
	 * @param exam_id
	 * @param reportpath
	 * @param logname
	 * @return
	 */
	private HealthFileResBody getHTTPfile(long exam_id,String exam_num,String pdfurl,String logname){		
		String picpath = this.commService.getDatadis("TPLJ").getName(); 
		HealthFileResBody hb=new HealthFileResBody();
		hb.setRescode("AE");
		hb.setRestext("获取图片失败");
		String savefile="";
		File f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		picpath = picpath + "\\health_file";
		String picurl = "/health_file";
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		picpath = picpath + "\\"+exam_num;
		picurl = picurl+"/"+exam_num;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		String filename=exam_num;
		picpath=picpath+"\\"+filename;
		picurl=picurl+"/"+filename;
		try {
			if (new HttpDownloader(pdfurl, picpath+".pdf", logname).download()) {
					PdfToJpg pjpg = new PdfToJpg();
					int picnum = pjpg.pdf2jpg1(picpath+".pdf", picpath,200);
					List<String> file_img = new ArrayList<String>();
					for (int j = 1; j <= picnum; j++) {
						file_img.add(picurl + "_" + j + ".jpg");						
					}
					try{
					    File file = new File(picpath+".pdf");
					    file.delete();
					}catch(Exception e){
						TranLogTxt.liswriteEror_to_txt(logname, "res:pdf删除失败"+com.hjw.interfaces.util.StringUtil.formatException(e));
					}
					hb.setImageID(updateFile(exam_id, file_img, logname));
					hb.setFilePath("");
					hb.setRestext("");
					hb.setRescode("AA");
			}else{
				hb.setRescode("AE");
				hb.setFilePath("");
				hb.setRestext("下载图片失败,保存文件失败");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		  return hb;
	}
	
	/**
	 * 	
	 * @param exam_id
	 * @param pathfile
	 * @param logname
	 * @return
	 */
	private long updateFile(long exam_id,List<String> pathfile,String logname){
		Connection tjtmpconnect = null;
		//long hf=0; 
		long exam_health_id=0;
		try {
			if(pathfile!=null&&pathfile.size()>0){	
				
				tjtmpconnect = this.jdbcQueryManager.getConnection();
				String sb1 = "select id from exam_health where exam_id='"+exam_id+"'";
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1);
				if (rs1.next()) {
					exam_health_id=rs1.getLong("id");					
				}else{
					String inserts="insert into exam_health (exam_id,print_status,creater,create_time,updater,update_time) "
							+ "values ('" + exam_id + "','N','14','"
			                + DateTimeUtil.getDateTime() + "','14','"
							+ DateTimeUtil.getDateTime() + "')";
							PreparedStatement preparedStatement = null;
							preparedStatement = tjtmpconnect.prepareStatement(inserts, Statement.RETURN_GENERATED_KEYS);
							preparedStatement.executeUpdate();
							ResultSet rs = null;
							rs = preparedStatement.getGeneratedKeys();
							if (rs.next())
								exam_health_id = rs.getInt(1);
							rs.close();
							preparedStatement.close();	
				}
				rs1.close();		
						
			sb1 = "delete from exam_health_file where exam_id='"+exam_id+"'";
			tjtmpconnect.createStatement().executeUpdate(sb1);
			for(String files:pathfile){
				String inserts="insert into exam_health_file (exam_id,exam_health_id,file_path,creater,create_time,updater,update_time) "
				+ "values ('" + exam_id + "','"+exam_health_id+"','" +files+ "','14','"
                + DateTimeUtil.getDateTime() + "','14','"
				+ DateTimeUtil.getDateTime() + "')";
				tjtmpconnect.createStatement().executeUpdate(inserts);
			}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return exam_health_id;
	}
	
	/**
	 * 
	 * @param exam_id
	 * @param logname
	 * @return
	 */
	private HealthFileResBody getHealthOne(long exam_id,String logname){
		Connection tjtmpconnect = null;
		HealthFileResBody hfb=new HealthFileResBody();		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select id,file_path from exam_health where exam_id='"+exam_id+"'";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1);
			if (rs1.next()) {
				hfb.setImageID(rs1.getLong("id"));
				hfb.setRescode("AA");				
				sb1 = "select id,file_path from exam_health_file where exam_health_id='"+exam_id+"'";
				ResultSet rs2 = tjtmpconnect.createStatement().executeQuery(sb1);
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1);
				if (rs2.next()) {
					hfb.setFilePath(hfb.getFilePath()+","+rs2.getString("file_path"));
				} 
				rs2.close();				
			} 
			rs1.close();
		} catch (SQLException ex) {
			hfb.setRescode(com.hjw.interfaces.util.StringUtil.formatException(ex));
			hfb.setRescode("AA");
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return hfb;
	}	
}
