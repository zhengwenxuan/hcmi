package com.hjw.webService.client.tj180;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.Base64;
import com.hjw.util.BmpReader;
import com.hjw.util.DeleteFileUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.util.ValidateTime;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.ExamPicBean;
import com.hjw.webService.client.tj180.Bean.ExamPicGetBean;
import com.hjw.webService.client.tj180.Bean.ExamPicGetResBean;
import com.hjw.wst.DTO.JobDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 获取第三方系统的体检者图片
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class ExamPicSendMessageTJ180 {
	private static JdbcQueryManager jdbcQueryManager;
	   static{
	   	init();
	   	}
		public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		}
	public ExamPicSendMessageTJ180(){
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getMessage(String url,String exam_num,String logname) {
		ResultHeader rb = new ResultHeader();
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + exam_num);
		    if(exam_num!=null&&exam_num.trim().length()>0){
		    	ExamPicBean epb= new ExamPicBean();
		    	epb=this.getExamInfoForId(exam_num, logname);
				if (epb.getExam_id()<=0) {
					rb.setTypeCode("AE");
					rb.setText("无效档案编号");
				}else if (epb.getPicture_resource()==1&&epb.getPicture_path()!=null&&epb.getPicture_path().trim().length()>0) {
					rb.setTypeCode("AE");
					rb.setText("已经获取照片，无需重新获取");
				} else {
					ExamPicGetBean hbrb =new ExamPicGetBean();
					hbrb.setPatientId(epb.getArch_num());
					JSONObject json = JSONObject.fromObject(hbrb);// 将java对象转换为json对象
					//String str = json.toString();// 将json对象转换为字符串
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + epb.getArch_num());
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
					
					String result = HttpUtil.doPost(url,hbrb,"utf-8");
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
		            if((result!=null)&&(result.trim().length()>0)){
						result = result.trim();
						JSONObject jsonobject = JSONObject.fromObject(result);
						ExamPicGetResBean resdah = new ExamPicGetResBean();
						resdah = (ExamPicGetResBean) JSONObject.toBean(jsonobject, ExamPicGetResBean.class);
						if ("200".equals(resdah.getStatus())) {
							String patch=saveExamInfoPic(resdah.getPhoto());
							if(patch.length()>10){
								setExamPic(epb.getExam_id(),patch,logname);
							}
							rb.setText("");	
							rb.setTypeCode("AA");
						}else{
							rb.setTypeCode("AE");
							rb.setText(resdah.getErrorinfo());	
						}
		            }else{
						rb.setTypeCode("AE");
						rb.setText("系统返回错误");
		            }
				}
		    }else{
		    	rb.setTypeCode("AE");
				rb.setText("无效体检编号");	
		    }
		} catch (Exception ex) {
			rb.setTypeCode("AE");
			rb.setText("处理错误");
		}
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + JSONObject.fromObject(rb).toString());
		return rb;
	}
	
	/**
	 * 
	 * @param url
	 * @param arch_num
	 * @param logname
	 * @return
	 */
	public ResultHeader getMessageArchNum(String url,String arch_num,String logname) {
		ResultHeader rb = new ResultHeader();
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + arch_num);
		    if(arch_num!=null&&arch_num.trim().length()>0){
 	
					ExamPicGetBean hbrb =new ExamPicGetBean();
					hbrb.setPatientId(arch_num);
					JSONObject json = JSONObject.fromObject(hbrb);// 将java对象转换为json对象
					//String str = json.toString();// 将json对象转换为字符串
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + arch_num);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
					
					String result = HttpUtil.doPost(url,hbrb,"utf-8");
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
		            if((result!=null)&&(result.trim().length()>0)){
						result = result.trim();
						JSONObject jsonobject = JSONObject.fromObject(result);
						ExamPicGetResBean resdah = new ExamPicGetResBean();
						resdah = (ExamPicGetResBean) JSONObject.toBean(jsonobject, ExamPicGetResBean.class);
						if ("200".equals(resdah.getStatus())) {
							String patch=saveExamInfoPic(resdah.getPhoto());
							if(patch.length()>10){
								rb.setText(patch);	
								rb.setTypeCode("AA");
							}else{
							    rb.setText("无效图片");	
							    rb.setTypeCode("AE");
							}
						}else{
							rb.setTypeCode("AE");
							rb.setText(resdah.getErrorinfo());	
						}
		            }else{
						rb.setTypeCode("AE");
						rb.setText("系统返回错误");
		            }
				}
		} catch (Exception ex) {
			rb.setTypeCode("AE");
			rb.setText("处理错误");
		}
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + JSONObject.fromObject(rb).toString());
		return rb;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamPicBean getExamInfoForId(String exam_num,String logname) throws ServiceException {
		ExamPicBean pb= new ExamPicBean();		
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();			
			StringBuffer sb = new StringBuffer();
			sb.append("select b.arch_num,a.id,a.exam_num,a.picture_path,a.picture_resource");
			sb.append(" from exam_info a ");			
			sb.append(",customer_info b where a.customer_id=b.id ");
			sb.append(" and a.exam_num='"+exam_num+"' ");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb.toString());
			if (rs1.next()) {
				pb.setArch_num(rs1.getString("arch_num"));
				pb.setExam_id(rs1.getLong("id"));
				pb.setExam_num(exam_num);
				pb.setPicture_path(rs1.getString("picture_path"));
				pb.setPicture_resource(rs1.getLong("picture_resource"));
			}
			rs1.close();
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
		return pb;
	}
	
	/**
	 * 
	 * @Title: saveExamInfoPic @Description: 保存图片 @param: @param
	 *         examid @param: @param picstring @param: @param
	 *         flags @param: @return @return: String @throws
	 */
	private String saveExamInfoPic(String picstring) {
		String filepath = "";
		// System.out.println("0000:="+picstring);
		if ((picstring != null) && (picstring.length() > 500)) {
			String bmpfiledata = picstring;
			try {
					// System.out.println("0000000="+bmpfiledata);
					byte[] bmpfiledata64 = Base64.base64Decode(bmpfiledata);
					String picpath = this.getDatadis("TPLJ").getName();
					String path = picpath + "\\";
					// System.out.println("111111="+bmpfiledata);
					File f = new File(path);
					if (!f.exists() && !f.isDirectory())
						f.mkdir();
					path = path + "\\customer-picture";
					f = new File(path + "\\customer-picture");
					if (!f.exists() && !f.isDirectory())
						f.mkdir();
					String picfilename = ValidateTime.getDayTimeAll();
					filepath = "customer-picture/" + picfilename + ".jpg";
					// System.out.println("22222="+filepath);
					path = path + "/" + picfilename + ".jpg";
					
					if("bmp".equals(com.hjw.interfaces.util.FileUtil.getPicType(bmpfiledata64))){
						File dmpf = new File(picpath + "\\deme-picture");
						if (!dmpf.exists() && !dmpf.isDirectory())
							dmpf.mkdir();
						String dmpfilepath = picpath + "\\deme-picture\\" + ValidateTime.getDayTimeAll() + ".bmp";
                    	FileOutputStream fos = new FileOutputStream(dmpfilepath);
						fos.write(bmpfiledata64);
						System.out.println("写入成功");
						fos.close();
						BmpReader.bmpTojpg(dmpfilepath,path);  
						DeleteFileUtil.deleteFile(dmpfilepath);
				} else {
					FileOutputStream fos = new FileOutputStream(path);
					// System.out.println("33333="+path);
					fos.write(bmpfiledata64);
					System.out.println("写入成功");
					fos.close();
				}
			} catch (Exception e) {
				filepath="";
				e.printStackTrace();
			}
		}
		return filepath;
	}
	
	public JobDTO getDatadis(String data_code) throws ServiceException {
		String sqltext = "select id,data_name from data_dictionary where data_code='" + data_code
				+ "' and isActive='Y' ";
		Connection connection = null;
		Statement statement = null;
		JobDTO jd = new JobDTO();
		try {
			// 读取记录数
			connection = this.jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sqltext);
			if (rs.next()) {
				jd.setId(rs.getString("id"));
				jd.setName(rs.getString("data_name"));
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return jd;
	}
	
	public void setExamPic(long exam_id,String picpath,String logname) throws ServiceException {
		String sqltext = "update exam_info set picture_path='"+picpath+"',picture_resource='1' where id='" + exam_id + "' ";
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + sqltext.toString());
		Connection connection = null;
		try {
			// 读取记录数
			connection = this.jdbcQueryManager.getConnection();
			connection.createStatement().executeUpdate(sqltext);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
}