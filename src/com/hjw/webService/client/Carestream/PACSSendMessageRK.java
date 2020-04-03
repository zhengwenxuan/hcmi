package com.hjw.webService.client.Carestream;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


import com.hjw.interfaces.util.JaxbUtil;

import com.hjw.service.ConfigService;


import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.Carestream.Bean.RKReturnXml;
import com.hjw.webService.client.Carestream.client.CSHESBService;
import com.hjw.webService.client.Carestream.client.CSHESBServiceLocator;
import com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_PortType;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSSendMessageRK{
	private PacsMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	    static{
	    	init();
	    	}
		public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		}
	public PACSSendMessageRK(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url,String convalue,String logname) {		
		ResultPacsBody rb = new ResultPacsBody();	
		List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
		String[] tokenurls= convalue.split("&");  //系统标记&院区代码&医院代码
		try {
		for (PacsComponents pcs : lismessage.getComponents()) {
			String arch_num = this.getArch_num(logname);
			StringBuffer sb = new StringBuffer("");
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<xmlMessage>");
			//人员基本信息
			sb.append("<Patient>");
			sb.append("<SocialSecurityNo></SocialSecurityNo>");
			sb.append("<PatientID><![CDATA[057101"+arch_num+"]]></PatientID>");
			sb.append("<RemotePID><![CDATA[057101"+arch_num+"]]></RemotePID>");//第三方系统的PatientID	系统标志+院区代码+Patid
			sb.append("<LocalName><![CDATA["+lismessage.getCustom().getName()+"]]></LocalName>");
			sb.append("<EnglishName><![CDATA[]]></EnglishName>");
			sb.append("<Gender><![CDATA["+lismessage.getCustom().getSexname()+"]]></Gender>");
			sb.append("<Birthday><![CDATA["+parse5(lismessage.getCustom().getBirthtime())+"]]></Birthday>");
	        sb.append("<ReferenceNo><![CDATA["+lismessage.getCustom().getPersonidnum()+"]]></ReferenceNo>");
	        sb.append("<MedicareNo></MedicareNo>");
	        sb.append("<IsVIP><![CDATA["+lismessage.getCustom().getVipflag()+"]]></IsVIP>");//是否VIP	0-普通   1-VIP
			sb.append("<Telephone><![CDATA["+lismessage.getCustom().getTel()+"]]></Telephone>");
			sb.append("<Address><![CDATA["+lismessage.getCustom().getAddress()+"]]></Address>");
			sb.append("</Patient>");
			// 检查信息节点
			sb.append("<Order>");
			sb.append("<RemoteAccNo><![CDATA[" + pcs.getReq_no() + "]]></RemoteAccNo>");
			sb.append("<AccNo></AccNo>");
			sb.append("<HospID><![CDATA[057101]]></HospID>");
			sb.append("<HealthHistory></HealthHistory>");
			sb.append("<Observation></Observation>");
			sb.append("<PatientType><![CDATA[4]]></PatientType>");// 病人类型 门诊病人
																	// 住院病人 急诊病人
																	// 体检

			sb.append("<ClinicNo><![CDATA[" + lismessage.getCustom().getExam_num() + "]]></ClinicNo>");
			sb.append("<InhospitalNo></InhospitalNo>");
			sb.append("<InhospitalRegion></InhospitalRegion>");
			sb.append("<BedNo></BedNo>");
			sb.append("<CardNo></CardNo>");
			sb.append("<ApplyDoctor><![CDATA[" + lismessage.getDoctor().getDoctorName() + "]]></ApplyDoctor>");
			sb.append("<ApplyDept><![CDATA[" + lismessage.getDoctor().getDept_name() + "]]></ApplyDept>");
			sb.append("<Comments></Comments>");
			sb.append("<CurrentAge></CurrentAge>");
			sb.append("</Order>");
			for (PacsComponent pc : pcs.getPacsComponent()) {
				pc=getchingItemId(pc,logname);
				String Modality = "",ModalityType = "";
				if(pc.getExam_class() != null){
					String[] str = pc.getExam_class().split(",");
					if(str.length >= 2){
						Modality = str[0];
						ModalityType = str[1];
					}
				}
				sb.append("<Procedure> ");
				sb.append("<ProcedureCode><![CDATA["+pc.getItemCode()+"]]></ProcedureCode>");
				sb.append("<ProcedureID><![CDATA["+pc.getItemId()+"]]></ProcedureID>");
				sb.append("<ProcedureDesc><![CDATA["+pc.getItemName()+"]]></ProcedureDesc>");
				sb.append("<Bodypart><![CDATA[其他]]></Bodypart>");
				sb.append("<BodyCategory><![CDATA[其他]]></BodyCategory>");
				sb.append("<ExamSystem><![CDATA[其他]]></ExamSystem>");
				sb.append("<Modality><![CDATA["+Modality+"]]></Modality>");
				sb.append("<ModalityType><![CDATA["+ModalityType+"]]></ModalityType>");
				sb.append("<ExamDatetime><![CDATA["+parse5(pc.getItemDate())+"]]></ExamDatetime>");
				sb.append("</Procedure>");				
			}
			sb.append("</xmlMessage>");
			//发送申请
			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+lismessage.getMessageid()+":"+sb.toString());
			try {
				CSHESBService  dam = new CSHESBServiceLocator(url);
				CSHESBServiceSoap_PortType dams = dam.getCSHESBServiceSoap();
				String messages = dams.callESB("SyncNewOrder",sb.toString());
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+lismessage.getMessageid()+":"+messages);
					RKReturnXml rt=new RKReturnXml();					
					rt = JaxbUtil.converyToJavaBean(messages, RKReturnXml.class);
					if("1".equals(rt.getResultCode())){
						ApplyNOBean an= new ApplyNOBean();
						an.setApplyNO(pcs.getReq_no());
						anList.add(an);
					}
			
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logname,"req:"+lismessage.getMessageid()+":"+com.hjw.interfaces.util.StringUtil.formatException(ex));
				ex.printStackTrace();
			}
		}		
		ControlActPacsProcess cap = new ControlActPacsProcess();
		cap.setList(anList);
		rb.setControlActProcess(cap);
		rb.getResultHeader().setTypeCode("AA");
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装pacs xml格式文件错误");
		}
		
		return rb;
	}
	
	private PacsComponent getchingItemId(PacsComponent pc,String logname){
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select c.id,d.remark1 from charging_item c "
					+ "left join department_dep d on d.id=c.dep_id where item_code='"+pc.getItemCode()+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +sb1);				
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if(rs1.next()) {
				pc.setExam_class(rs1.getString("remark1"));
				pc.setItemId(rs1.getLong("id"));		
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
		return pc;
	}
	
	private String getArch_num(String logname){
		Connection tjtmpconnect = null;
		String arch_num = "";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select c.arch_num from customer_info c,exam_info e where c.id = e.customer_id and e.exam_num = '"+lismessage.getCustom().getExam_num()+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +sb1);				
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if(rs1.next()) {
				arch_num = rs1.getString("arch_num");
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
		return arch_num;
	}
	
	public static String parse5(String param) {
		Date date = new Date();
		if ((param != null) && (param.trim().length() == 8)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			sdf.setLenient(false);
			try {
				date = sdf.parse(param);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
}
}
