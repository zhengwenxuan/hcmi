package com.hjw.webService.client.hokai305;

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
public class FEETermSendMessageHK305 {

	private FeeMessage feeMessage;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}
	public FEETermSendMessageHK305(FeeMessage feeMessage){
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
		
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
		String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
		String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		String ORDERED_BY_DEPT="";
		String ORDERED_BY_DOCTOR="";
		String PERFORMED_BY="";
		
		try{
			String exam_num = this.feeMessage.getPROJECTS().getPROJECT().get(0).getEXAM_NUM();
			ExamInfoUserDTO ei= new ExamInfoUserDTO();
			ei=getExamInfoForNum(exam_num,logname);
		StringBuffer sb= new StringBuffer();
		
		sb.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  "                      );
		sb.append("  <!-- 消息ID(系统生成唯一UUID)(1..1) -->                                                                                  "                      );
		sb.append("  <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>                                                                 "            );
		sb.append("  <!-- 消息创建时间(1..1) -->                                                                                              "                      );
		sb.append("  <creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>                                                                                 "     );
		sb.append("  <!-- 服务编码，S0085代表收费申请(1..1)-->                                                                                "                      );
		sb.append("  <interactionId extension=\"S0084\"/>                                                                                     "                      );
		sb.append("  <!-- 接受者(1..1) -->                                                                                                    "                      );
		sb.append("  <receiver code=\"SYS002\"/>                                                                                        "                            );
		sb.append("  <!-- 发送者(1..1) -->                                                                                                    "                      );
		sb.append("  <sender code=\"SYS009\"/>                                                                                          "                            );
		sb.append("  <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">                                                                  "                      );
		sb.append("    <!-- 消息交互类型 @code: 新增/更新 :new 撤销:delete -->                                                                "                      );
		sb.append("    <code value=\"new\"/>                                                                                                  "                      );
		sb.append("    <subject typeCode=\"SUBJ\">                                                                                            "                      );
		sb.append("      <observationRequest classCode=\"OBS\" moodCode=\"RQO\">                                                              "                      );
		sb.append("        <!-- 申请单号(1..1) -->                                                                                            "                      );
		sb.append("        <id>                                                                                                               "                      );
		sb.append("          <item extension=\""+this.feeMessage.getREQ_NO()+"\" root=\"2.16.156.10011.1.24\"/>                                                    "  );
		sb.append("        </id>                                                                                                              "                      );
		sb.append("        <!--申请时间(0..1)-->                                                                                              "                      );
		sb.append("        <effectiveTime value=\""+DateTimeUtil.getDateTimes()+"\"/>                                                                          "     );
		sb.append("        <!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->                                                      "                      );
		sb.append("        <code code=\"3\" displayName=\"体检\"/>                                                                            "                      );
		sb.append("        <!--开立者(1..1)-->                                                                                                "                      );
		sb.append("        <author typeCode=\"AUT\">                                                                                          "                      );
		sb.append("          <!--开单时间(0..1)-->                                                                                            "                      );
		sb.append("          <time value=\""+DateTimeUtil.getDateTimes()+"\"/>                                                                                 "     );
		sb.append("          <assignedEntity classCode=\"ASSIGNED\">                                                                          "                      );
		sb.append("            <!--开立者 ID(0..1)-->                                                                                         "                      );
		sb.append("            <id extension=\""+doctorid+"\" root=\"2.16.156.10011.1.4\"/>                                             "                            );
		sb.append("            <!--开立者姓名(0..1)-->                                                                                        "                      );
		sb.append("            <name value=\""+doctorname+"\"/>                                                                                       "              );
		sb.append("            <!-- 申请科室信息(0..1) -->                                                                                    "                      );
		sb.append("            <representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                        "                      );
		sb.append("              <!--医疗卫生机构（科室） ID(0..1)-->                                                                         "                      );
		sb.append("              <id extension=\""+kddepid+"\" root=\"2.16.156.10011.1.26\"/>                                                  "                     );
		sb.append("              <!--开立科室(0..1)-->                                                                                        "                      );
		sb.append("              <name value=\""+kddepname+"\"/>                                                                                     "               );
		sb.append("            </representedOrganization>                                                                                     "                      );
		sb.append("          </assignedEntity>                                                                                                "                      );
		sb.append("        </author>                                                                                                          "                      );
		sb.append("        <!--执行科室(0..1) -->                                                                                             "                      );
		sb.append("        <location code=\""+kddepid+"\" displayName=\""+kddepname+"\"/>                                                                   "        );
		sb.append("        <!--申请备注(0..1) -->                                                                                             "                      );
		sb.append("        <memo value=\"注意XXX\"/>                                                                                          "                      );
		sb.append("        <!--体检信息(0..1)-->                                                                                              "                      );
		sb.append("        <encounter classCode=\"ENC\" moodCode=\"EVN\">                                                                     "                      );
		sb.append("          <id>                                                                                                             "                      );
		sb.append("            <!--体检号标识(0..1)-->                                                                                        "                      );
		sb.append("            <item extension=\""+exam_num+"\" root=\"2.16.156.10011.1.13\"/>                                           "                            );
		sb.append("            <!--患者 ID 标识(0..1)-->                                                                                      "                      );
		sb.append("            <item extension=\""+ei.getPatient_id()+"\" root=\"2.16.156.10011.0.2.2\"/>                                          "                 );
		sb.append("          </id>                                                                                                            "                      );
		sb.append("          <!--PERSONAL=个检;GROUP=团检(0..1)-->                                                                            "                      );
		sb.append("          <code code=\"GROUP\" displayName=\"团检\"/>                                                                   "                      );
		sb.append("          <!--合同单位，团检时有此节点(0..1)-->                                                                            "                      );
		sb.append("          <unitInContract code=\""+ei.getBatch_id()+"\" displayName=\""+ei.getBatch_name()+"\"/>                                                                     "                      );
		sb.append("          <!-- 申请类型编码，名称，用于区分此申请是收费还是算绩效，1:收费，2:绩效(1..1) -->                                "                      );
		sb.append("          <typeCode code=\"2\" displayName=\"绩效\"/>                                                                           "                      );
		sb.append("          <!-- 体检批次,包含年份信息(1..1) -->                                                                             "                      );
		sb.append("          <peTimes value=\""+ei.getAcc_num()+"\"/>                                                                                            "                      );
		sb.append("          <!--收费明细信息,团检时申请状态是绩效时用此节点(1..1) -->                                                        "                      );
		sb.append("          <chargeInfos>                                                                                                    "                      );
		sb.append("            <!--待收费信息(1..*) -->                                                                                       "                      );
		sb.append("            <chargeInfo>                                                                                                   "                      );
		sb.append("              <!-- 项目序号(1..1) -->                                                                                      "                      );
		sb.append("              <itemNo>1</itemNo>                                                                                           "                      );
		sb.append("              <!-- 项目编码 名称，对应收费项目目录的编码(1..1)-->                                                          "                      );
		sb.append("              <item code=\"\" displayName=\"\"/>                                                                           "                      );
		sb.append("              <!-- 实收金额(0..1)-->                                                                                       "                      );
		sb.append("              <costs value=\"\"/>                                                                                          "                      );
		sb.append("              <!-- 应收金额(0..1) -->                                                                                      "                      );
		sb.append("              <charge value=\"\"/>                                                                                         "                      );
		sb.append("              <!-- 数量以及单位(0..1)-->                                                                                   "                      );
		sb.append("              <count units=\"\" value=\"2\"/>                                                                            "                      );
		sb.append("            </chargeInfo>                                                                                                  "                      );
		sb.append("          </chargeInfos>                                                                                                   "                      );
		sb.append("          <!--收费汇总信息，状态为收费时用此节点(0..1) -->                                                                 "                      );
		sb.append("          <counts>                                                                                                          "                      );

		
		for(Fee fee:this.feeMessage.getPROJECTS().getPROJECT()){
			ChargingItemDTO chargingItem = configService.findChargeItemByHis_num(fee.getITEM_CODE());
			HisClinicItemPriceListDTO dto = new HisClinicItemPriceListDTO();
			dto.setClinic_item_code(chargingItem.getHis_num());
			dto.setSystemdate(DateTimeUtil.getDate());//系统时间
			
			int itemNo = 0;
	        int orderNo =50;
	        
			List<HisClinicItemPriceListDTO> priceList = configService.getHisjg305(dto);
			for(HisClinicItemPriceListDTO price : priceList) {
				System.out.println(fee.getCHARGES());
				System.out.println(chargingItem.getAmount());
				System.out.println(chargingItem.getItem_name());
				double discount = Double.parseDouble(fee.getCHARGES()) / chargingItem.getAmount();
				if(chargingItem.getAmount() == 0.0 ){
					discount=0.0;
				}
				BigDecimal needPay = new BigDecimal(price.getAmount()*price.getPrice());
				//BigDecimal realPay = new BigDecimal(price.getAmount()*price.getPrice()*fee.getDiscount()*0.1);
				BigDecimal realPay = new BigDecimal(price.getAmount()*price.getPrice()*discount);//将折扣打到每一条价表上
				ORDERED_BY_DEPT=fee.getORDERED_BY_DEPT();
				 
	             ORDERED_BY_DOCTOR=fee.getORDERED_BY_DOCTOR();
	             PERFORMED_BY=fee.getPERFORMED_BY();
	             exam_num=fee.getEXAM_NUM();
	             sb.append("          <count>                                                                                                          "                      );
	             sb.append("            <!-- 项目编码/名称(1..1) -->                                                                                   "                      );
	             sb.append("            <item code=\""+price.getItem_code_p()+"\" displayName=\""+price.getItem_name_p()+"\"/>                         "                      );                   
	             sb.append("            <!-- 人数，团检时有此节点(0..1) -->                                                                            "			);
	             sb.append("            <number value=\"\"/>                                                                                           "      );
	             sb.append("            <!-- 实收总金额(0..1) -->                                                                                      "      );
	             sb.append("            <costs value=\""+realPay.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"\"/>                             "      );                                                  
	             sb.append("            <!-- 应收总金额(0..1) -->                                                                                      "      );
	             sb.append("            <charge value=\""+needPay.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"\"/>                            "      );                                                  
	             sb.append("            <!-- 项目序号(1..1) -->                                                                                      "      );
	             sb.append("            <itemNo>"+ itemNo++ +"</itemNo>                           "      );                                                  
	             sb.append("            <!-- 医嘱序号(1..1) -->                                                                                     "      );
	             sb.append("            <orderNo  value=\""+ orderNo++ +"\"/>                            "      );                                                  
	             sb.append("            <!-- 医嘱类别(1..1) -->                                                                                     "      );
	             sb.append("            <orderClass  value=\"J\"/>                            "      );                                                  
	             sb.append("          </count>                                                                                                         "      );
			}
	}
	    
		 sb.append("          </counts>                                                                                                         "      );
		 sb.append("        </encounter>                                                                                                       "      );
		 sb.append("      </observationRequest>                                                                                                "      );
		 sb.append("    </subject>                                                                                                             "      );
		 sb.append("    <!--操作者信息(0..1)-->                                                                                                "      );
		 sb.append("    <author code=\""+doctorid+"\" displayName=\""+doctorname+"\"/>                                                         "      );      
		 sb.append("  </controlActProcess>                                                                                                     "      );
		 sb.append("</POOR_IN200901UV>    																										"      );
		
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
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id,c.company,b.batch_name ");
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
