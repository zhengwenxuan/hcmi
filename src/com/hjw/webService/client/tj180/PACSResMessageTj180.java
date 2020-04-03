package com.hjw.webService.client.tj180;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqPacsItemDTO;
import com.hjw.interfaces.HttpServer.HttpDownloader;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.PdfToJpg;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.tj180.Bean.PacsGetReqBean;
import com.hjw.webService.client.tj180.Bean.PacsGetResBean;
import com.hjw.webService.client.tj180.Bean.PacsGetResItemBean;
import com.hjw.webService.client.tj180.Bean.PacsReqBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcPacsResult;
import com.hjw.wst.service.CommService;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;
import com.synjones.framework.util.DateTimeUtil;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSResMessageTj180{
   private static CommService commService;  
   private static ConfigService configService;
   private static JdbcQueryManager jdbcQueryManager;
   static{
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		commService = (CommService) wac.getBean("commService");
		configService = (ConfigService) wac.getBean("configService");
	}
	@SuppressWarnings("resource")
	public PACSResMessageTj180(){
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logname,boolean bldeptnumflag,String bldept_num,boolean usdeptnumflag,String usdept_num,boolean handflag, String exam_num) {
		ResultPacsBody rb = new ResultPacsBody();
		Connection tjtmpconnect = null;
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + exam_num);
		try {
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			eu = this.getExamInfoForNum(exam_num);
			
			long oldexam_id =eu.getId();
			String oldexam_num=exam_num;
			String olddate = eu.getJoin_date();
			ExamInfoUserDTO oldeu = null;
			boolean isbjflag=false;
			
			if(oldexam_num.indexOf("B")==0){
				oldexam_num=oldexam_num.substring(1,oldexam_num.length());
				oldeu = this.getExamInfoForNum(oldexam_num);
				oldexam_id = oldeu.getId();
				olddate = oldeu.getJoin_date();
				isbjflag=true;
			}
			
			List<ZlReqPacsItemDTO> zris = new ArrayList<ZlReqPacsItemDTO>();
			zris = this.getzl_req_item(oldexam_id,oldexam_num,bldeptnumflag,bldept_num,usdeptnumflag,usdept_num,handflag,logname);			
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			Map<String,ProcPacsResult> hisresltMap =new HashMap<String,ProcPacsResult>();
			for (ZlReqPacsItemDTO zri : zris) {
				//tjtmpconnect.setAutoCommit(false);					
				try {
					
				    boolean sichecksh=true;
					if((isbjflag)&&!checkDeptnum(eu.getId(),zri.getRemark2(),logname)){	
						sichecksh=false;
					}
					if(sichecksh){		
					if(!"EC".equals(zri.getRemark2())){
					String kslx=zri.getRemark2();
//					if("XGNS".equals(kslx)){
//						kslx="US";
//					}else if("EA".equals(kslx)){
//						kslx="NKJ";
//					}
					PacsGetReqBean p = new PacsGetReqBean();					
					PacsReqBean lrclass = new PacsReqBean();
					lrclass = this.getExamClass(zri.getZl_pacs_id(), logname);					
					p.setExamClass(lrclass.getExamClass());
					p.setExamSubClass(lrclass.getExamSubClass());
					p.setExamineDate(olddate);
					p.setPatientId(zri.getZl_pat_id());
					p.setExamNo(zri.getReq_id());
					JSONObject json = JSONObject.fromObject(p);// 将java对象转换为json对象
					String str = json.toString();// 将json对象转换为字符串
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
					String result = HttpUtil.doPost(url,p,"utf-8");
					//String result="{\"status\":\"200\",\"errorinfo\":\"\",\"examResultInfo\":[{\"examClass\":\"US\",\"examSubClass\":\"US\",\"examNo\":\"16668758\",\"description\":\"1111\",\"impression\":\"444444\",\"abnormalIndicator\":\"1\",\"resultDateTime\":\"20180813095147\",\"operator\":\"李耀华\",\"checkPerson\":\"李耀华\",\"reportDateTime\":\"2018-08-13T09:51:47\",\"examItemName\":\"B超\",\"examPdfPath\":\"http://10.180.180.100:8015/PdfGenerateWebapi.ashx?exam_no=16668758\"}]}";
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + result);
		            if((result!=null)&&(result.trim().length()>0)){
							result = result.trim();
							JSONObject jsonobject = JSONObject.fromObject(result);
							Map classMap = new HashMap();
							classMap.put("examResultInfo", PacsGetResItemBean.class);
							PacsGetResBean resdah = new PacsGetResBean();
							resdah = (PacsGetResBean) JSONObject.toBean(jsonobject, PacsGetResBean.class, classMap);
							if ("200".equals(resdah.getStatus())) {
								for (PacsGetResItemBean lgri : resdah.getExamResultInfo()) {
									// N：正常，L：偏低，H：偏高
									// 火箭蛙 高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
									String mapkye = "";
									if ((lgri.getExamItemName() != null) && (lgri.getExamItemName().length() > 0)) {
										String name = lgri.getExamItemName();
										int xzc = name.indexOf("心脏彩");
										int jh = name.indexOf("+");
										int zh = name.indexOf("左");
										int cd = name.indexOf("测定");
										
										int dzzycj=name.indexOf("电子直乙结肠镜");
										int dzqjcj=name.indexOf("电子喉镜");
										int dzbyj=name.indexOf("电子鼻咽镜");
										if ((xzc >= 0) && (xzc < jh) && (jh < zh) && (zh < cd)) {
											mapkye = "XGNS&&" + lgri.getResultDateTime();
											kslx = "XGNS";
										}else if (dzzycj>=0) {
											mapkye = "EA&&" + lgri.getResultDateTime();
											kslx = "EA";
										}else if(dzqjcj>=0 || dzbyj>=0){
											mapkye = "DZHJ&&" + lgri.getResultDateTime();
											kslx = "DZHJ";
										}else if(lgri.getExamClass().equals("ES")){
											mapkye = "NKJ&&" + lgri.getResultDateTime();
											kslx = "NKJ";
										} else if(lgri.getExamClass().equals("US")){
											mapkye = "US&&" + lgri.getResultDateTime();
											kslx = "US";
										} else {
											mapkye = kslx + "&&" + lgri.getResultDateTime();
										}
									} else {
										mapkye = kslx + "&&" + lgri.getResultDateTime();
									}
									
									ProcPacsResult plr = hisresltMap.get(mapkye);
									if (plr == null) {
										if ((lgri.getImpression() != null) && (lgri.getImpression().trim().length() > 0)
												&& (!"null".equals(lgri.getImpression().trim()))) {
											plr = new ProcPacsResult();
											plr.setCheck_date(parse5(lgri.getResultDateTime()));
											plr.setCheck_doct(lgri.getCheckPerson());
											plr.setAudit_date(parse5(lgri.getResultDateTime()));
											plr.setAudit_doct(lgri.getOperator());
											plr.setExam_desc(lgri.getDescription());
											plr.setExam_result(lgri.getImpression());
											plr.setImg_file(lgri.getExamPdfPath());
											plr.setExam_num(lgri.getExamNo());
											plr.setOld_exam_num("B"+lgri.getExamNo());
											plr.setPacs_name(lgri.getExamItemName());
											hisresltMap.put(mapkye, plr);
										}
									}
								}
							}
						}
					}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
			 Map<String, List<ProcPacsResult>> map = new HashMap<String, List<ProcPacsResult>>();
			 map = mapCombine(hisresltMap);
			 if(isbjflag){
				 proc_pacs_report_dbgj(oldeu,map,handflag,logname);
			 }
			 proc_pacs_report_dbgj(eu,map,handflag,logname);
			 
			rb.getResultHeader().setTypeCode("AA");
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("pacs调用webservice错误");
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rb;
	}	
	
	 public static Map mapCombine(Map<String,ProcPacsResult> list) {  
	        Map<String, List<ProcPacsResult>> map = new HashMap<String, List<ProcPacsResult>>();  
	        for (String key : list.keySet()) {
	        	 String md[]=key.split("&&");
	        	 String keyone=md[0];
	                if (!map.containsKey(keyone)) { 
	                    List<ProcPacsResult> newList = new ArrayList<ProcPacsResult>();  
	                    newList.add(list.get(key));  
	                    map.put(keyone, newList);  
	                } else {  
	                    map.get(keyone).add(list.get(key));  
	                }  
	            }  
	        return map;  
	    }  
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	private ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
				+ ",c.register_date,convert(varchar(50),c.join_date,23) join_date,c.exam_times ");
		sb.append(" from exam_info c ");
		sb.append(" where c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
	
	private List<ZlReqPacsItemDTO> getzl_req_item(long examInfoId,String exam_num,boolean bldeptnumfalse,String bldept_num,boolean usdeptnumflag,String usdept_num,boolean handflag,String logname) throws Exception{
		Connection tjtmpconnect = null;
		List<ZlReqPacsItemDTO> req_ids= new ArrayList<ZlReqPacsItemDTO>();
		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select z.req_id,z.zl_pacs_id,z.pacs_req_code,z.zl_pat_id,z.remark1,z.remark2,z.remark3,z.charging_item_ids,p.id "
					+ "from zl_req_pacs_item z,charging_item c,pacs_summary p,examinfo_charging_item eci where c.item_code = z.charging_item_ids and p.examinfo_num = '"+exam_num+"' "
					+ " and p.pacs_req_code = z.pacs_req_code and eci.examinfo_id='" + examInfoId + "' and eci.charge_item_id=c.id "
					+ " and z.exam_info_id='" + examInfoId + "'";
			if(!handflag){
				sb1= sb1+" and z.flags='0' ";
			}
			
			if(!bldeptnumfalse){
				if(!"all".equals(bldeptnumfalse)){
			       sb1= sb1+" and  remark2!='"+bldept_num+"' ";
				}
			}else{
				if(!"all".equals(bldeptnumfalse)){
					sb1= sb1+" and  remark2='"+bldept_num+"' ";
				}
			}
			

			if(!usdeptnumflag){
				if(!"all".equals(usdept_num)){
			      sb1= sb1+" and  remark2!='"+usdept_num+"' ";
				}
			}else{
				if(!"all".equals(usdept_num)){
				sb1= sb1+" and  remark2='"+usdept_num+"' ";
				}
			}
			
			
			
			sb1= sb1+" order by z.remark2 ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs1.next()) {
				ZlReqPacsItemDTO ri= new ZlReqPacsItemDTO();
				ri.setReq_id(rs1.getString("req_id"));
				ri.setZl_pacs_id(rs1.getString("zl_pacs_id"));
				ri.setPacs_req_code(rs1.getString("pacs_req_code"));
				ri.setZl_pat_id(rs1.getString("zl_pat_id"));
				ri.setRemark1(rs1.getString("remark1"));
				ri.setRemark2(rs1.getString("remark2"));
				ri.setRemark3(rs1.getString("remark3"));
				ri.setCharging_item_ids(rs1.getString("charging_item_ids"));
				ri.setPacs_id(rs1.getLong("id"));
				req_ids.add(ri);
			} 
			rs1.close();
		} catch (SQLException ex) {
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
		return req_ids; 
	}
	
	private PacsReqBean getExamClass(String chargitem_code,String logname){
		Connection tjtmpconnect = null;
		PacsReqBean rb= new PacsReqBean();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.examClass,a.examSubClass "
					+ " from charging_item a where a.item_code='"+chargitem_code+"'";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1);
			if (rs1.next()) {
				rb.setExamClass(rs1.getString("examClass"));
				rb.setExamSubClass(rs1.getString("examSubClass"));
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
		return rb;
	}
	
	/**
	 * 
	 * @param eu
	 * @param map
	 * @param logname
	 * @throws Exception
	 */
	private void proc_pacs_report_dbgj(ExamInfoUserDTO eu, Map<String, List<ProcPacsResult>> map,boolean handflag,String logname) throws Exception {
		for (String dept_num : map.keySet()) {
			 List<ProcPacsResult> plrList = new ArrayList<ProcPacsResult>();	
			 plrList = map.get(dept_num);
			String shys = "";
			String tjys = "";
			String desc = "";
			String res = "";	
			int pic_num = 0;
			String datatime = com.hjw.util.DateTimeUtil.getDate();
			String pacs_summary_id =dept_num;//com.hjw.util.DateTimeUtil.getDateTimes2();
			String file_img = "";
			if("XGNS".equals(dept_num.toUpperCase())&&plrList!=null&&plrList.size()>1){
				plrList=setProcPacsResult(plrList,logname);
			}/*else if("US".equals(dept_num.toUpperCase())&&plrList!=null&&plrList.size()>1){
				plrList=setProcPacsResultUS(plrList,logname);
			}*/
			for (ProcPacsResult plr : plrList) {
				
				if(plr.getCheck_doct()!=null){
				if (shys.indexOf(plr.getCheck_doct().trim()) < 0) {
					shys = shys + "," + plr.getCheck_doct().trim();
				}
				}
			    if(plr.getAudit_doct()!=null){
				if (tjys.indexOf(plr.getAudit_doct().trim()) < 0) {
					tjys = tjys + "," + plr.getAudit_doct().trim();
				}
			    }
				desc = desc + "\r\n\r\n" + plr.getExam_desc();
				res = res + "\r\n\r\n" + plr.getExam_result();
				
				String PACS_NO_PIC_ITEMNAME="";
				com.hjw.DTO.CenterConfigurationDTO cc = configService.getCenterconfigByKey("PACS_NO_PIC_ITEMNAME");
				if (cc!=null&&cc.getConfig_value()!=null&&cc.getConfig_value().trim().length()>0){
					PACS_NO_PIC_ITEMNAME= cc.getConfig_value();
				}
				if(!checknopic(PACS_NO_PIC_ITEMNAME,plr.getPacs_name(),logname))
				{
					if (plr.getImg_file().length() > 10) {
						String picpath = this.commService.getDatadis("TPLJ").getName();
						pic_num++;
						File f = new File(picpath);
						if (!f.exists() && !f.isDirectory())
							f.mkdir();

						picpath = picpath + "\\pacs_img";
						String picname = "/pacs_img";
						f = new File(picpath);
						if (!f.exists() && !f.isDirectory())
							f.mkdir();
						picpath = picpath + "\\" + datatime;
						picname = picname + "/" + datatime;
						f = new File(picpath);
						if (!f.exists() && !f.isDirectory())
							f.mkdir();

						picpath = picpath + "\\" + dept_num;
						picname = picname + "/" + dept_num;
						f = new File(picpath);
						if (!f.exists() && !f.isDirectory())
							f.mkdir();

						picpath = picpath + "\\" + eu.getExam_num();
						picname = picname + "/" + eu.getExam_num();
						f = new File(picpath);
						if (!f.exists() && !f.isDirectory())
							f.mkdir();

						picpath = picpath + "\\" + pacs_summary_id;
						picname = picname + "/" + pacs_summary_id;
						f = new File(picpath);
						if (!f.exists() && !f.isDirectory())
							f.mkdir();
						// String filepath = pacs_summary_id + "-" +
						// Timeutils.getFileData() + "_"+ pic_num + ".jpg";
						// String jpgpath = pic_num+".jpg";
						String pdfpath = picpath + "\\" + pic_num + ".pdf";
						picpath = picpath + "\\" + pic_num;
						picname = picname + "/" + pic_num;

						FileOutputStream fos = null;
						try {
							if (new HttpDownloader(plr.getImg_file(), pdfpath, logname).download()) {
								File file = new File(pdfpath);
								if (file.exists() && file.isFile()) {
									String fileName = file.getName();
									TranLogTxt.liswriteEror_to_txt(logname,
											"res:文件----" + fileName + "的大小是：" + file.length());
									boolean writeflag = false;
									if (file.length() / 1024 < 5) {
										TranLogTxt.liswriteEror_to_txt(logname,
												"res:小文件----" + fileName + "的大小是：" + file.length());
										writeflag = checkPDF(pdfpath, logname);
										TranLogTxt.liswriteEror_to_txt(logname, "判断文件是否是pdf：" + writeflag);
									} else {
									 	writeflag = true;
									}
									if (writeflag) {
										PdfToJpg pjpg = new PdfToJpg();
										TranLogTxt.liswriteEror_to_txt(logname, "res:pdf文件生产jpg开始");
										TranLogTxt.liswriteEror_to_txt(logname, "res:pdf文件" + pdfpath);
										TranLogTxt.liswriteEror_to_txt(logname, "res:jpg路径" + picpath);
										int picsize = 200;
										int picnum = 0;
										TranLogTxt.liswriteEror_to_txt(logname, dept_num);
										if ("ECG".equals(dept_num)) {
											picsize = 20;
											TranLogTxt.liswriteEror_to_txt(logname, "ECG取图");
											picnum = pjpg.pdf2jpgRotate(pdfpath, picpath, picsize, logname);
										} else {
											picnum = pjpg.pdf2jpg1(pdfpath, picpath, picsize);
										}
										TranLogTxt.liswriteEror_to_txt(logname, "res:pdf文件生产jpg完成");
										for (int j = 1; j <= picnum; j++) {
											file_img = file_img + ";" + picname + "_" + j + ".jpg";
										}
										try{
										    file.delete();
										}catch(Exception e){
											TranLogTxt.liswriteEror_to_txt(logname, "res:pdf删除失败"+com.hjw.interfaces.util.StringUtil.formatException(e));
										}
									} else {
										file.delete();
									}
								}

							}
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
							if (fos != null) {
								fos.close();
							}
						}
						/*
						 * PdfToJpg pjpg = new PdfToJpg(); int picnum =
						 * pjpg.pdf2jpg(pdfpath, picpath);
						 * 
						 * for (int j = 1; j <= picnum; j++) { file_img =
						 * file_img + ";" + picname + "_" + j + ".jpg"; }
						 */
						TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + file_img);
					}
				}
			}
			if (file_img.length() > 1) {
				file_img = file_img.substring(1, file_img.length());
			}
					
			if (shys.length() > 0)
				shys = shys.substring(1, shys.length());
			if (tjys.length() > 0)
				tjys = tjys.substring(1, tjys.length());
			Connection tjtmpconnect = null;
			try {
				tjtmpconnect = this.jdbcQueryManager.getConnection();
				String selectsql = "select id from view_exam_detail where exam_info_id='" + eu.getId()
						+ "' and exam_item_id=347 and dept_num='" + dept_num + "'";

				TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句：1.1 ");
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(selectsql);
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + selectsql);
				if (rs1.next()) {
					long id = rs1.getLong("id");
					String inssql = "update view_exam_detail set exam_doctor='" + tjys + "',exam_date='"
							+ plrList.get(0).getAudit_date() + "',exam_desc='" + desc + "',exam_result='" + res + "',"
							+ "center_num='001'," + "approver='" + shys + "',approve_date='"
							+ plrList.get(0).getCheck_date() + "',creater='14',create_time='"
							+ DateTimeUtil.getDateTime() + "',pacs_id='0' where id='" + id + "'";
					TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + inssql);
					tjtmpconnect.createStatement().execute(inssql);
					
					if (file_img.length() > 4) {
						savepic(id,file_img,logname);
					}

					String upsql = " update examinfo_charging_item set exam_status='Y',exam_doctor_name='"+tjys+"',exam_date='"+plrList.get(0).getAudit_date()+"' where id in ( "
							+ "select a.id from examinfo_charging_item a,charging_item b,department_dep c "
							+ "where a.charge_item_id=b.id " + "and a.isActive='Y' " + "and b.dep_id=c.id "
							+ " and a.examinfo_id='" + eu.getId() + "' "
							+ " and c.dep_num='" + dept_num + "')";
					TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + upsql);
					tjtmpconnect.createStatement().execute(upsql);
				} else {
					if((desc!=null)&&(desc.trim().length()>0)){
						desc=desc.replaceAll("'", "''");
					}
					if((res!=null)&&(res.trim().length()>0)){
						res=res.replaceAll("'", "''");
					}
					String inssql = "insert into view_exam_detail(exam_info_id,exam_item_id,exam_doctor,exam_date,exam_desc,exam_result,"
							+ "center_num," + "approver,approve_date,creater,create_time,pacs_id,dept_num) values('"
							+ eu.getId() + "','347','" + tjys + "','" + plrList.get(0).getAudit_date() + "','" + desc
							+ "','" + res + "','001','" + shys + "','" + plrList.get(0).getCheck_date() + "','14','"
							+ DateTimeUtil.getDateTime() + "','0','" + dept_num + "')";
					TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + inssql);
					//tjtmpconnect.createStatement().execute(inssql);

					PreparedStatement preparedStatement = null;
					preparedStatement = tjtmpconnect.prepareStatement(inssql, Statement.RETURN_GENERATED_KEYS);
					preparedStatement.executeUpdate();
					ResultSet rs = null;
					rs = preparedStatement.getGeneratedKeys();
					int retId = 0;
					if (rs.next()) {
						retId = rs.getInt(1);						
					} else
						throw new Exception("insert or generate keys failed..");
					rs.close();
					preparedStatement.close();
					
					if (file_img.length() > 4) {
						savepic(retId,file_img,logname);
					}
					
					String upsql = " update examinfo_charging_item set exam_status='Y',exam_doctor_name='"+tjys+"',exam_date='"+plrList.get(0).getAudit_date()+"' where id in ( "
							+ "select a.id from examinfo_charging_item a,charging_item b,department_dep c "
							+ "where a.charge_item_id=b.id " + "and a.isActive='Y' " + "and b.dep_id=c.id "
							+ " and a.examinfo_id='" + eu.getId() + "' "
							+ " and c.dep_num='" + dept_num + "')";
					TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + upsql);
					tjtmpconnect.createStatement().execute(upsql);
				}
				rs1.close();
				if (handflag){
					 updatezl_req_itemflags(eu.getId(),dept_num,logname);
				 }
			} catch (SQLException ex) {
				TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
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
		}
	}
	
	/**
	 * 
	 * @param exam_info_id 体检id
	 * @param chargitem_id 收费项目id
	 * @param lis_req_no  体检申请单号
	 * @param req_id  收费项目对应第三方系统返回的id
	 * @param logname 日志名称
	 * @param jyxmbm_num//第三方检验项目编码
	 * @return
	 * @throws Exception
	 */
	public boolean updatezl_req_itemflags(long exam_info_id,String dept_num,String logname) throws Exception{
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();			
				String updatesql = "update zl_req_pacs_item set flags='1',createdate='"
						+DateTimeUtil.getDateTime()+"' where  exam_info_id='"
					+ exam_info_id + "' and remark2='"+dept_num+"' ";
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +updatesql);
				tjtmpconnect.createStatement().executeUpdate(updatesql);
			
			return true;
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
			return false;
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	private void savepic(long view_exam_id,String file_img,String logname){
		Connection tjtmpconnect = null;
		try{
			String[] simg = file_img.split(";");
			int countsnew=simg.length;
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select count(*) as counts from view_exam_image where view_exam_id='"+view_exam_id+"' ";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1);
			int countold=0;
			if (rs1.next()) {
				countold=rs1.getInt("counts");
			}
			rs1.close();
			if(countold==0)
			{
				for (int mi = 0; mi < simg.length; mi++) {
					String instr = "insert into view_exam_image(view_exam_id,image_path,creater,create_time) values" + "('" + view_exam_id + "','"+ simg[mi] + "','14','"
							+ DateTimeUtil.getDateTime() + "')";
					TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + instr);
					tjtmpconnect.createStatement().execute(instr);
				}
			}else if(countold==countsnew){
				String selectsql = "select a.create_time,a.creater,a.image_path,a.update_time,a.updater,a.view_exam_id,a.id from view_exam_image a where a.view_exam_id='"+view_exam_id+"' order by id";
		        ResultSet rs0 = tjtmpconnect.createStatement().executeQuery(selectsql);
		        TranLogTxt.liswriteEror_to_txt(logname, "res:" + selectsql);
		        int i=0;
		        while (rs0.next()) {
			       long id = rs0.getLong("id");		
			       String upsql="update view_exam_image set image_path='"+simg[i]+"',updater='14',update_time='"+DateTimeUtil.getDateTime()+"' where id='"+id+"' ";
			       i++;
			       TranLogTxt.liswriteEror_to_txt(logname, "res:" + upsql);
			       tjtmpconnect.createStatement().execute(upsql);
		        }
		        rs0.close();
			}else if(countold<countsnew){
				String selectsql = "select a.create_time,a.creater,a.image_path,a.update_time,a.updater,a.view_exam_id,a.id from view_exam_image a where a.view_exam_id='"+view_exam_id+"' order by id";
		        ResultSet rs0 = tjtmpconnect.createStatement().executeQuery(selectsql);
		        TranLogTxt.liswriteEror_to_txt(logname, "res:" + selectsql);
		        int i=0;
		        while (rs0.next()) {
			       long id = rs0.getLong("id");		
			       String upsql="update view_exam_image set image_path='"+simg[i]+"',updater='14',update_time='"+DateTimeUtil.getDateTime()+"' where id='"+id+"' ";
			       i++;
			       TranLogTxt.liswriteEror_to_txt(logname, "res:" + upsql);
			       tjtmpconnect.createStatement().execute(upsql);
		        }
		        rs0.close();
		        
				for (int mi = countold; mi < simg.length; mi++) {
					String instr = "insert into view_exam_image(view_exam_id,image_path,creater,create_time) values" + "('" + view_exam_id + "','"+ simg[mi] + "','14','"
							+ DateTimeUtil.getDateTime() + "')";
					TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + instr);
					tjtmpconnect.createStatement().execute(instr);
				}
			}else if(countold>countsnew){
				String instr = " delete view_exam_image where id in (select top "+countold+" id from view_exam_image where view_exam_id='" + view_exam_id + "' and "
						+ "id not in (select top "+countsnew+" id from view_exam_image where view_exam_id='" + view_exam_id + "'  order by id) order by id ) ";
				TranLogTxt.liswriteEror_to_txt(logname, "res::操作语句： " + instr);
				tjtmpconnect.createStatement().execute(instr);
				
				String selectsql = "select a.create_time,a.creater,a.image_path,a.update_time,a.updater,a.view_exam_id,a.id from view_exam_image a where a.view_exam_id='"+view_exam_id+"' order by id";
		        ResultSet rs0 = tjtmpconnect.createStatement().executeQuery(selectsql);
		        TranLogTxt.liswriteEror_to_txt(logname, "res:" + selectsql);
		        int i=0;
		        while (rs0.next()) {
			       long id = rs0.getLong("id");		
			       String upsql="update view_exam_image set image_path='"+simg[i]+"',updater='14',update_time='"+DateTimeUtil.getDateTime()+"' where id='"+id+"' ";
			       i++;
			       TranLogTxt.liswriteEror_to_txt(logname, "res:" + upsql);
			       tjtmpconnect.createStatement().execute(upsql);
		        }
		        rs0.close();
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
	}

	private static String parse5(String param) {
		Date date = null;
		if ((param != null) && (param.trim().length() == 14)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			try {
				date = sdf.parse(param);
			} catch (ParseException ex) {
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	private void delpic(long view_exam_id,String logname){
		Connection tjtmpconnect = null;
		PacsReqBean rb= new PacsReqBean();
		try {
			String picpath = this.commService.getDatadis("TPLJ").getName(); 
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select image_path from view_exam_image where view_exam_id='"+view_exam_id+"'";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1);
			if (rs1.next()) {
				String imagespath=rs1.getString("image_path");
				imagespath=imagespath.replaceAll("/", "\\\\");
				
				picpath=picpath+"\\"+imagespath;
				try{
					deleteFile(picpath,logname);
				}catch(Exception ex){
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
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
	}
	
	public static boolean deleteFile(String fileName,String logname) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
            	TranLogTxt.liswriteEror_to_txt(logname, "res:删除单个文件" + fileName + "成功！");
                return true;
            } else {
            	TranLogTxt.liswriteEror_to_txt(logname, "res:删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
        	TranLogTxt.liswriteEror_to_txt(logname, "res:删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
	
	private boolean checkDeptnum(long examid,String deptnum,String logname){
		Connection tjtmpconnect = null;
		boolean  bflag=false;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select dd.dep_num from examinfo_charging_item eci,charging_item ci,department_dep dd "
					+ " where eci.pay_status<>'M' "
					+ " and eci.isActive='Y' "
					+ " and eci.charge_item_id=ci.id "
					+ " and ci.dep_id=dd.id "
					+ "and eci.examinfo_id='"+examid+"' "
					+ "and dd.dep_num='"+deptnum+"'";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1);
			if (rs1.next()) {
				bflag=true;
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
		return bflag;
	}
	
	public static void main(String[] args) {
		double dd =-9.765625E-4;
		System.out.println(dd);

	}
	
	 /**  
     * 利用itext打开pdf文档  
     */  
    private static boolean checkPDF(String file,String logname) { 
    	TranLogTxt.liswriteEror_to_txt(logname, "res:" + file);
        boolean flag1 = false;  
        int n = 0;  
        try {  
            Document document = new Document(new PdfReader(file).getPageSize(1));  
            document.open();  
            PdfReader reader = new PdfReader(file);  
            n = reader.getNumberOfPages();  
            if (n != 0)  
                flag1 = true;  
            document.close();  
        } catch (IOException e) {  
             
  
        }  
        return flag1;    
    }  
    
    /**
     * 
     * @param plrList 比较大小，只取时间最大的保存
     * @return
     */
    private List<ProcPacsResult> setProcPacsResult(List<ProcPacsResult> plrList,String logname){
    	List<ProcPacsResult> plrListnew = new ArrayList<ProcPacsResult>();
    	int m=0;
    	String firdate=plrList.get(0).getAudit_date();
    	for(int i=1;i<plrList.size();i++){
    		int dxdate=com.hjw.interfaces.util.DateUtil.compare_date(firdate, plrList.get(i).getAudit_date(), "yyyyMMddHHmmss");
    		TranLogTxt.liswriteEror_to_txt(logname, "date:" + firdate+" : "+plrList.get(i).getAudit_date() +" : "+dxdate);
    		if(dxdate<0){
    			m=i;
    			firdate=plrList.get(i).getAudit_date();
    		}
    	}
    	plrListnew.add(plrList.get(m));
    	return plrListnew;
    }
    
	private boolean checknopic(String PACS_NO_PIC_ITEMNAME, String itemname, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, itemname + "-res:-" + PACS_NO_PIC_ITEMNAME);
		boolean exist = false;
		if (itemname == null || itemname.trim().length() <= 0) {
			exist = false;
		} else if (PACS_NO_PIC_ITEMNAME == null || PACS_NO_PIC_ITEMNAME.trim().length() <= 0) {
			exist = false;
		} else {
			String[] str = PACS_NO_PIC_ITEMNAME.split(",");

			for (int i = 0; i < str.length; i++) {
				String oldstr = str[i].trim();
				if (itemname.indexOf(oldstr) >= 0) {
					exist = true;
					break;
				}
			}
		}
		return exist;
	}
    
    /**
     * 
     * @param plrList 比较大小，只取时间最大的保存
     * @return
     */
    private List<ProcPacsResult> setProcPacsResultUS(List<ProcPacsResult> plrList,String logname){
    	List<ProcPacsResult> plrListnew = new ArrayList<ProcPacsResult>();
    	for(int i=0;i<plrList.size();i++)
    	{
    		int m=i;
    		ProcPacsResult ppr=new ProcPacsResult();
    		ppr=(ProcPacsResult)plrList.get(i);
    		String name1=((ProcPacsResult)plrList.get(i)).getPacs_name();
    		String firdate1=plrList.get(i).getAudit_date();
    		   		
    		for(int j=i;j<plrList.size();j++)
        	{
    			String name2=((ProcPacsResult)plrList.get(j)).getPacs_name();
    			String firdate2=plrList.get(0).getAudit_date();
    			if(name2.equals(name1)){
    				int dxdate=com.hjw.interfaces.util.DateUtil.compare_date(firdate1,firdate2, "yyyyMMddHHmmss");
    	    		TranLogTxt.liswriteEror_to_txt(logname, "date:" + firdate1+" : "+firdate2 +" : "+dxdate);
    	    		if(dxdate<0){
    	    			ppr=(ProcPacsResult)plrList.get(j);    			
    	    		}
    			}        	
    		}
    		boolean flag=true; 
    		for(ProcPacsResult pr:plrListnew){
    			if(pr.getPacs_name().equals(ppr.getPacs_name())){
    				flag=false;	
    			}
    		}
    		if(flag){
    		plrListnew.add(ppr);
    		}

    	}
    	return plrListnew;
    }
 
 
	
}
