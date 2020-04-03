package com.hjw.webService.client.hokai;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.DTO.HisClinicItemPriceListDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.14	挂号信息服务   天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class FEETermSendMessageHK {

	private FeeMessage feeMessage;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}
	public FEETermSendMessageHK(FeeMessage feeMessage){
		this.feeMessage=feeMessage;
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
	public FeeResultBody getMessage(String url,String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		try {
			this.feeMessage.setMSG_TYPE("TJ602");
			xml = JaxbUtil.convertToXml(this.feeMessage, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+feeMessage.getREQ_NO()+":"+xml);
			ResultHeader rh=getString(url,logname);
            if("AA".equals(rh.getTypeCode())){
            	ReqId rqid = new ReqId();
            	rqid.setReq_id(this.feeMessage.getREQ_NO());
            	rb.getControlActProcess().getList().add(rqid);
            	rb.getResultHeader().setTypeCode("AA");
    			rb.getResultHeader().setText("发送收费申请成功!");
            } else {
            	 rb.getResultHeader().setTypeCode(rh.getTypeCode());
     			rb.getResultHeader().setText(rh.getText());
            }
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		}
		return rb;
	}

	private ResultHeader  getString(String url,String logname){
		ResultHeader rh= new ResultHeader();
		try{
			String exam_num = this.feeMessage.getPROJECTS().getPROJECT().get(0).getEXAM_NUM();
			ExamInfoUserDTO ei= new ExamInfoUserDTO();
			ei=getExamInfoForNum(exam_num,logname);
		StringBuffer sb= new StringBuffer();
		sb.append("<POOR_IN200901UV xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\">\n");
		sb.append("  <!-- 消息流水号 -->\n");
		sb.append("  <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\" root=\"2.16.156.10011.0\"/>\n");
		sb.append("  <!-- 消息创建时间 -->\n");
		sb.append("  <creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>\n");
		sb.append("  <!-- 消息的服务标识-->\n");
		sb.append("  <interactionId extension=\"S0090\" root=\"2.16.840.1.113883.1.6\"/>\n");
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
		sb.append("    <subject typeCode=\"SUBJ\">\n");
		sb.append("      <placerGroup classCode=\"GROUPER\" moodCode=\"RQO\">\n");
		sb.append("        <!--体检信息-->\n");
		sb.append("        <componentOf1 contextConductionInd=\"false\">\n");
		sb.append("		    <!--PERSONAL=个检;GROUP=团检-->\n");
		sb.append("          <code code=\"GROUP\" codeSystem=\"2.16.840.1.113883.1.6\" displayName=\"团检\"/>\n");
		sb.append("          <encounter classCode=\"ENC\" moodCode=\"EVN\">\n");
		sb.append("            <!--费用标志-->\n");
		sb.append("            <feeFlag value=\"记账\"/>\n");
		sb.append("            <!--体检号码-->\n");
		sb.append("            <peId value=\""+exam_num+"\"/>\n");
		sb.append("            <!-- 体检批次,包含年份信息(1..1) -->\n");
		sb.append("            <peTimes value=\""+(ei.getCompany_id()+"-"+ei.getBatch_name())+"\"/>\n");
		sb.append("            <!--申请单号-->\n");
		sb.append("            <applyNo value=\""+this.feeMessage.getREQ_NO()+"\"/>\n");
		sb.append("            <!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->\n");
		sb.append("            <code code=\"3\" codeSystem=\"2.16.156.10011.2.3.1.271\">\n");
		sb.append("              <!-- 就诊类别名称(1..1) -->\n");
		sb.append("              <displayName value=\"体检\"/>\n");
		sb.append("            </code>\n");
		sb.append("            <chargeInfos>     "
				+ "\n");
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
		String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
		String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		String ORDERED_BY_DEPT="";
        String ORDERED_BY_DOCTOR="";
        String PERFORMED_BY="";
		for(Fee fee:this.feeMessage.getPROJECTS().getPROJECT()){
			ChargingItemDTO chargingItem = configService.findChargeItemByHis_num(fee.getITEM_CODE());
			ExaminfoChargingItemDTO eci = configService.getExaminfoChargingItem(ei.getId(), chargingItem.getId(), logname);
			HisClinicItemPriceListDTO dto = new HisClinicItemPriceListDTO();
			dto.setClinic_item_code(chargingItem.getHis_num());
			dto.setSystemdate(DateTimeUtil.getDate());//系统时间
			dto.setBody_part(chargingItem.getItem_abbreviation());
			dto.setMethod(chargingItem.getNotices());
			List<HisClinicItemPriceListDTO> priceList = configService.getHisjg(dto);
			for(HisClinicItemPriceListDTO price : priceList) {
				System.out.println(fee.getCHARGES());
				System.out.println(chargingItem.getAmount());
				System.out.println(chargingItem.getItem_name());
				double discount = 0.0;
				if(chargingItem.getAmount() == 0.0 ){
					discount=0.0;
				} else {
					discount = Double.parseDouble(fee.getCHARGES()) / (chargingItem.getAmount()*eci.getItemnum());
				}
				BigDecimal needPay = new BigDecimal(eci.getItemnum()*price.getAmount()*price.getPrice());
				//BigDecimal realPay = new BigDecimal(price.getAmount()*price.getPrice()*fee.getDiscount()*0.1);
				BigDecimal realPay = new BigDecimal(eci.getItemnum()*price.getAmount()*price.getPrice()*discount);//将折扣打到每一条价表上
				ORDERED_BY_DEPT=fee.getORDERED_BY_DEPT();
				 
	             ORDERED_BY_DOCTOR=fee.getORDERED_BY_DOCTOR();
	             PERFORMED_BY=fee.getPERFORMED_BY();
	             exam_num=fee.getEXAM_NUM();
				sb.append("            <!--收费信息(1..1) -->\n");
			sb.append("            <chargeInfo>\n");
			sb.append("              <!-- 项目编码 对应收费项目目录的编码-->\n");
			sb.append("              <chargeCategory value=\""+price.getItem_code_p()+"\"/>\n");
			sb.append("              <!-- 收费项目名称 -->\n");
			sb.append("              <chargeDetails value=\""+price.getItem_name_p()+"\"/>\n");
			sb.append("              <!-- 实收金额-->\n");
			sb.append("              <payNumber value=\""+realPay.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"\"/>\n");
			sb.append("              <!--应收金额-->\n");
			sb.append("              <receivable value=\""+needPay.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"\"/>\n");
			sb.append("              <!--数量-->\n");
			sb.append("              <count value=\""+eci.getItemnum()*price.getAmount()+"\"/>\n");
			sb.append("              <!--标准价格-->\n");
			sb.append("              <standardUnit value=\""+price.getPrice()+"\"/>\n");
			sb.append("              <!-- 项单位 对应收费细目的计算单位-->\n");
			sb.append("              <chargeUnit value=\"元\"/>\n");
			sb.append("            </chargeInfo>\n");
			}
	}
		sb.append("            </chargeInfos>\n");
		String sexcode="1";
		if("男".endsWith(ei.getSex())){
			sexcode="1";
		}else if("女".endsWith(ei.getSex())){
			sexcode="2";
		}
	    
		sb.append("            <!--操作员工id和姓名-->\n");
		sb.append("            <operator code=\""+doctorid+"\" name=\""+doctorname+"\"/>\n");
		sb.append("            <!--操作时间 格式例如：20111129220000：-->\n");
		sb.append("            <operTime value=\""+DateTimeUtil.getDateTimes()+"\"/>\n");
		sb.append("            <!--开单科室编码、名称-->\n");
		sb.append("            <applyDept code=\""+kddepid+"\" name=\""+kddepname+"\"/>\n");
		sb.append("            <!--开单人编码、名称药品开单-->\n");
		sb.append("            <applyPerson code=\""+doctorid+"\" name=\""+doctorname+"\"/>\n");
		sb.append("            <!--执行科室编码、名称ORDERED_BY_DEPT-->\n");
		sb.append("            <executeDept code=\""+kddepid+"\" name=\""+kddepname+"\"/>\n");
		sb.append("            <!-- 备注 -->\n");
		sb.append("            <bakText value=\"\"/>\n");
		sb.append("            <subject typeCode=\"SBJ\">\n");
		sb.append("              <patient classCode=\"PAT\">\n");
		sb.append("                <id>\n");
		sb.append("                  <!--体检号(0..1) -->\n");
		sb.append("                  <item extension=\""+exam_num+"\" root=\"2.16.156.10011.1.10\"/>\n");
		sb.append("                </id>\n");
		sb.append("                <!--患者角色状态-->\n");
		sb.append("                <statusCode code=\"active\"/>\n");
		sb.append("                <patientPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">\n");
		sb.append("                  <!--患者身份证号(1..1)-->\n");
		sb.append("                  <id extension=\""+ei.getId_num()+"\" root=\"2.16.156.10011.1.3\"/>\n");
		sb.append("                  <!--患者id(1..1)-->\n");
		sb.append("                  <patientId value=\""+ei.getPatient_id()+"\"/>\n");
		sb.append("                  <!--患者姓名(1..1) 团检传单位名称-->\n");
		sb.append("                  <name xsi:type=\"LIST_EN\">\n");
		sb.append("                    <item>\n");
		sb.append("                      <part value=\""+ei.getCompany()+"\"/>\n");
		sb.append("                    </item>\n");
		sb.append("                  </name>\n");
		sb.append("                  <!--性别-->\n");
		sb.append("                  <administrativeGenderCode code=\""+sexcode+"\" codeSystem=\"2.16.156.10011.2.3.3.4\"  codeSystemName=\"生理性别代码表（GB/T 2261.1）\">\n");
		sb.append("                    <displayName value=\""+ei.getSex()+"\"/>\n");
		sb.append("                  </administrativeGenderCode>\n");
		sb.append("                  <!--出生日期(0..1)-->\n");
		sb.append("                  <birthTime value=\""+ei.getBirthday()+"\"/>\n");
		sb.append("                  <!--年龄(0..1)-->\n");
		sb.append("                  <originalText value=\""+ei.getAge()+"\" displayName=\"岁\"/>\n");
		sb.append("                  <!-- 联系电话 (0..1)-->\n");
		sb.append("                  <telecom xsi:type=\"BAG_TEL\">\n");
		sb.append("                    <item value=\""+ei.getPhone()+"\"/>\n");
		sb.append("                  </telecom>\n");
		sb.append("                  <!--住址 (0..1)-->\n");
		sb.append("                  <addr xsi:type=\"BAG_AD\">\n");
		sb.append("                    <item use=\"H\">\n");
		sb.append("                      <part type=\"AL\" value=\"\"/>\n");
		sb.append("                    </item>\n");
		sb.append("                  </addr>\n");
		sb.append("                  <!--患者科室编码、名称-->\n");
		sb.append("                  <petientDept code=\""+kddepid+"\" name=\""+kddepname+"\"/>\n");
		sb.append("                </patientPerson>\n");
		sb.append("              </patient>\n");
		sb.append("            </subject>\n");
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
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id,c.company_id,c.company,b.batch_name ");
		sb.append(" from exam_info c,customer_info a, batch b ");//,company_info com
		sb.append(" where a.id=c.customer_id and c.batch_id = b.id and c.is_Active='Y' ");//and c.company_id = com.id		
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
