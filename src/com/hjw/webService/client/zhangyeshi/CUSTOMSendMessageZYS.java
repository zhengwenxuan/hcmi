package com.hjw.webService.client.zhangyeshi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.wst.DTO.UserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

public class CUSTOMSendMessageZYS {
	
	//操作员ID
	private static final String userId = "2"; 
	
	private Custom custom=new Custom();
	
	public CUSTOMSendMessageZYS(Custom custom){
		this.custom=custom;
	}
	
	private static JdbcQueryManager jdbcQueryManager;
	
	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	
	public ResultBody getMessage(String url,String logname) {
		
		TranLogTxt.liswriteEror_to_txt(logname,"=====调用CUSTOMSendMessageZYS开始====");
		
		ResultBody rb = new ResultBody();
		
		//读取数据配置信息
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		
		String xml = JaxbUtil.convertToXml(this.custom, true);		
		
		TranLogTxt.liswriteEror_to_txt(logname,"传入参数custom===:"+xml);
		
		String sexcode = "";
		if("男".equals(custom.getSEX()) || "男性".equals(custom.getSEX())) {
			sexcode = "1";
			custom.setSEX("男性");
		} else if("女".equals(custom.getSEX()) || "女性".equals(custom.getSEX())) {
			sexcode = "2";
			custom.setSEX("女性");
		} else {
			sexcode = "0";
			custom.setSEX("未知的性别");
		}
		
		TranLogTxt.liswriteEror_to_txt(logname,"性别sexcode===:"+sexcode);
		
		StringBuffer bufferXml = new StringBuffer();
		
		bufferXml.append("<Root>");
			bufferXml.append("<VAA>");
				bufferXml.append("<Ie ");
				
					/*String patientId = getPatientId(custom.getEXAM_NUM(), logname);
					
					TranLogTxt.liswriteEror_to_txt(logname,"patientId===:"+patientId);
					
					if("".equals(patientId) || patientId == null) {
						bufferXml.append("VAA01 = \"0\" ");
					}else {
						bufferXml.append("VAA01 = \""+custom.getPATIENT_ID()+"\" ");
					}*/
					
					if (("".equals(this.custom.getPATIENT_ID())) || (this.custom.getPATIENT_ID() == null) || ("null".equals(this.custom.getPATIENT_ID()))) {
				      bufferXml.append("VAA01 = \"0\" ");
				    } else {
				      bufferXml.append("VAA01 = \"" + this.custom.getPATIENT_ID() + "\" ");
				    }
					
					bufferXml.append("VAA05 = \""+custom.getNAME()+"\" ");
					bufferXml.append("ABW01 = \""+sexcode+"\" "); 
					bufferXml.append("BDP02 = \"体检\" ");
					bufferXml.append("VAA10 = \""+custom.getAGE()+"\" "); 
					
					bufferXml.append("AAU01 = \"Y\" "); //年龄单位
					bufferXml.append("VAA12 = \""+custom.getDATE_OF_BIRTH()+"\" "); //出生日期 
					bufferXml.append("ABBRP = \""+custom.getNAME_PHONETIC()+"\" "); //拼音码
					bufferXml.append("ABBRW = \"\" "); //五笔码 
					bufferXml.append("VAA15 = \""+custom.getID_NO()+"\" "); //
				bufferXml.append("/>");
			bufferXml.append("</VAA>");
			
			bufferXml.append("<VAC1>");
				bufferXml.append("<Ie ");
					bufferXml.append("BCB01 = \"0\" ");
					bufferXml.append("BDP02 = \"体检\" "); 
					bufferXml.append("VAA10 = \""+custom.getAGE()+"\" ");
					
					String GHLB_ID = configService.getCenterconfigByKey("IS_HIS_GHLB_ID").getConfig_value().trim();//挂号类别ID
					bufferXml.append("BCB01A = \""+GHLB_ID+"\" "); //挂号类别ID
					
					String GHLB_NAME = configService.getCenterconfigByKey("IS_HIS_GHLB_NAME").getConfig_value().trim();//挂号类别ID
					bufferXml.append("BCB03A = \""+GHLB_NAME+"\" "); //挂号类别名称
					
					String GHKS_ID = configService.getCenterconfigByKey("IS_HIS_GHKS_ID").getConfig_value().trim();//挂号类别ID
					bufferXml.append("BCK01A = \""+GHKS_ID+"\" "); //挂号科室(体检调用为体检科ID)
					
					String GHYS_ID = configService.getCenterconfigByKey("IS_HIS_GHYS_ID").getConfig_value().trim();//挂号类别ID
					bufferXml.append("BCE01A = \""+GHYS_ID+"\" "); //挂号医生ID(总检医生ID)
					
					String GHYS_CODE = configService.getCenterconfigByKey("IS_HIS_GHYS_CODE").getConfig_value().trim();//挂号类别ID
					bufferXml.append("BCE02A = \""+GHYS_CODE+"\" "); //挂号医生编码(总检医生编码)
					
					String GHYS_NAME = configService.getCenterconfigByKey("IS_HIS_GHYS_NAME").getConfig_value().trim();//挂号类别ID
					bufferXml.append("BCE03A = \""+GHYS_NAME+"\" "); //挂号医生(总检医生)
					
					bufferXml.append("EmpId = \""+userId+"\" "); //操作员Id
					UserDTO userDto = FEESendMessageZYS.getUserInfoForId(userId,logname);
					bufferXml.append("EmpNo = \""+userDto.getWork_num()+"\" "); //操作员编码
					bufferXml.append("EmpName = \""+userDto.getName()+"\" "); //操作员名称
				    bufferXml.append("VAC09 = \"-3\" ");
			    bufferXml.append("/>");
			bufferXml.append("</VAC1>");
		bufferXml.append("</Root>");
		
		TranLogTxt.liswriteEror_to_txt(logname,"拼接字符串信息:" + bufferXml.toString());
		
		//http://127.0.0.1:3336/getinfo.html
		String param = "&paramStr="+bufferXml.toString()+" &InterfaceID=3";
		
		Map<String, Object> para = new HashMap<String, Object>();
		
		para.put("paramStr", bufferXml.toString());
		
		para.put("InterfaceID", 3);
		
		TranLogTxt.liswriteEror_to_txt(logname,"url路径打印=== :" +url+"参数param=== :" +param);
		
		String result = HttpUtil.doPost(url,para, "UTF-8");
		
		TranLogTxt.liswriteEror_to_txt(logname,"====请求后返回结果=== :" +result);
		
		if ((result != null) && (result.trim().length() > 0)) {
			result = result.trim();
			//解析XML
			ZYSResolveXML ssl = new ZYSResolveXML();
			Map<String, String> mapXML = ssl.resolveXML(result, true);
			
			if ("1".equals(mapXML.get("resultCode"))) {
				//插入数据库表 zl_req_patInfo
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("就诊登记信息成功");
				
				CustomResBean cr = new CustomResBean();
				TranLogTxt.liswriteEror_to_txt(logname,"====挂号单号=== :" +mapXML.get("挂号单号")+"====病人ID=== :"+mapXML.get("病人ID")+"====挂号ID===="+mapXML.get("挂号ID"));
				cr.setCLINIC_NO(mapXML.get("挂号单号")); //门诊号
				cr.setPATIENT_ID(mapXML.get("病人ID")); //病人ID
				cr.setVISIT_DATE(DateTimeUtil.getDate2()); //日期
				cr.setVISIT_NO(mapXML.get("挂号ID")); // 就诊序号
				rb.getControlActProcess().getLIST().add(cr);
			} else {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("不识别的错误代码"+mapXML.get("resultMsg"));
			}
		} else {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("返回结果错误："+result);
		}
		TranLogTxt.liswriteEror_to_txt(logname,"===getResultHeader().getTypeCode()==="+rb.getResultHeader().getTypeCode());
		
		TranLogTxt.liswriteEror_to_txt(logname,"===VISIT_NO==="+rb.getControlActProcess().getLIST().get(0).getVISIT_NO());
		
		return rb;
		
	}
	
	
	/**
	 * 查询返回结果的patient_id
	 * @param req_no
	 * @param logname
	 * @return
	 */
	public static String getPatientId(String exam_num, String logname) {
		Connection tjtmpconnect = null;
		String patient_id = "";
		try {
			tjtmpconnect = jdbcQueryManager.getConnection();
			String sb1 = "select patient_id from exam_info where exam_num ='"+exam_num+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "查询patient_id的SQL： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				patient_id = rs1.getString("patient_id");
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "查询收费单据:操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return patient_id;
	}
	
}
