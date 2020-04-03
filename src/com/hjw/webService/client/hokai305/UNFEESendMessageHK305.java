package com.hjw.webService.client.hokai305;

import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.UnFeeMessage;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.17	收费退费
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class UNFEESendMessageHK305 {

	private UnFeeMessage feeMessage;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	public UNFEESendMessageHK305(UnFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}
	static {
	   	init();
	}
	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeReqBody getMessage(String url,String logName) {
		FeeReqBody rb = new FeeReqBody();
		String xml = "";
		try {
			this.feeMessage.setMSG_TYPE("TJ604");
			xml = JaxbUtil.convertToXml(this.feeMessage, true);			
			TranLogTxt.liswriteEror_to_txt(logName,"req:"+feeMessage.getRCPT_NO()+":"+xml);
			for(String reqno:this.feeMessage.getREQ_NOS().getREQ_NO()){
			ResultHeader rh=getString(url,reqno,logName);
            if("AA".equals(rh.getTypeCode())){
            	ReqNo rqid = new ReqNo();
            	rqid.setREQ_NO(reqno);
            	rb.getControlActProcess().getList().add(rqid);
            	rb.getResultHeader().setTypeCode("AA");
    			rb.getResultHeader().setText("发送退费申请成功!");
            }
			}
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		}
		return rb;
	}

	private ResultHeader  getString(String url,String reqno,String logname){
		ResultHeader rh= new ResultHeader();
		try{
		StringBuffer sb= new StringBuffer();
		sb.append("<POOR_IN200901UV xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\">\n"); 
		sb.append("  <!-- 消息流水号 -->\n");
		sb.append("  <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\" root=\"2.16.156.10011.0\"/>\n");
		sb.append("  <!-- 消息创建时间 -->\n");
		sb.append("  <creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>\n");
		sb.append("  <!-- 消息的服务标识-->\n");//
		sb.append("  <interactionId extension=\"S0069\" root=\"2.16.840.1.113883.1.6\"/>\n");
		sb.append("  <!--处理代码，标识此消息是否是产品、训练、调试系统的一部分。 D：调试； P：产品； T：训练 -->\n");
		sb.append("  <processingCode code=\"P\"/>\n");
		sb.append("  <!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive); T(Current\n");
		sb.append("    processing) -->\n");
		sb.append("  <processingModeCode/>\n");
		sb.append("  <!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->\n");
		sb.append("  <acceptAckCode code=\"AL\"/>\n");
		sb.append("  <!-- 接受者 -->\n");
		sb.append("  <receiver typeCode=\"RCV\">\n");
		sb.append("    <device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("      <!-- 接受者 ID -->\n");
		sb.append("      <id>\n");
		sb.append("        <item extension=\"SYS001\" root=\"2.16.156.10011.0.1.1\"/>\n");
		sb.append("      </id>\n");
		sb.append("    </device>\n");
		sb.append("  </receiver>\n");
		sb.append("  <!-- 发送者 -->\n");
		sb.append("  <sender typeCode=\"SND\">\n");
		sb.append("    <device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("      <!-- 发送者 ID -->\n");
		sb.append("      <id>\n");
		sb.append("        <item extension=\"SYS009\" root=\"2.16.156.10011.0.1.2\"/>\n");
		sb.append("      </id>\n");
		sb.append("    </device>\n");
		sb.append("  </sender>\n");
		sb.append("  <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">\n");
		sb.append("\n");
		sb.append("    <subject typeCode=\"SUBJ\">\n");
		sb.append("      <placerGroup classCode=\"GROUPER\" moodCode=\"RQO\">\n");
		sb.append("        <!--体检信息-->\n");
		sb.append("        <componentOf1 contextConductionInd=\"false\">\n");
		sb.append("		    <!--PERSONAL=个检;GROUP=团检-->\n");
		sb.append("          <code code=\"PERSONAL\" codeSystem=\"2.16.840.1.113883.1.6\" displayName=\"个检\"/>\n");
		sb.append("          <encounter classCode=\"ENC\" moodCode=\"EVN\">\n");
		ExamInfoUserDTO ei = this.getExamInfoForNum(this.feeMessage.getEXAM_NUM(),logname);
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
		sb.append("            <!--单据号-->\n");
		sb.append("            <documentNo value=\""+reqno+"\"/>\n");
		sb.append("            <!--申请单号-->\n");
		sb.append("            <applyNo value=\""+reqno+"\"/>\n");
		sb.append("            <!--团检id/患者id-->\n");
		sb.append("            <patientId value=\""+ei.getPatient_id()+"\"/>\n");
		sb.append("            <!--操作员工id和姓名-->\n");
		sb.append("            <operator code=\""+doctorid+"\" name=\""+doctorname+"\"/>\n");
		sb.append("            <!--操作时间 格式例如：20111129220000：-->\n");
		sb.append("            <operTime value=\""+DateTimeUtil.getDateTimes()+"\"/>\n");
		sb.append("          </encounter>\n");
		sb.append("        </componentOf1>\n");
		sb.append("      </placerGroup>\n");
		sb.append("    </subject>\n");
		sb.append("  </controlActProcess>\n");
		sb.append("</POOR_IN200901UV>\n");
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		String result = HttpUtil.doPost_Xml(url,sb.toString(), "utf-8");
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
		if ((result != null) && (result.trim().length() > 0)) {
			result = result.trim();				
			rh = ResContralBeanHK.getRes(result);				
		}else{
			rh.setTypeCode("AE");
			rh.setText("接口无返回");
		}
		}catch(Exception ex){
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return rh;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
	
}
