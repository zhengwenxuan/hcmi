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
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;
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
     * @Description:  2.19	项目减项  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class DELFEESendMessageHK305 {

	private DelFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	public DELFEESendMessageHK305(DelFeeMessage feeMessage){
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
	public FeeReqBody getMessage(String url,String logname) {
		FeeReqBody rb = new FeeReqBody();
		String xml = "";
		try {
			xml = JaxbUtil.convertToXml(this.feeMessage, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+feeMessage.getREQ_NO()+":"+xml);
			ResultHeader rh=getString(url,logname);
            if("AA".equals(rh.getTypeCode())){
            	ReqNo rqid = new ReqNo();
            	rqid.setREQ_NO(this.feeMessage.getREQ_NO());
            	rb.getControlActProcess().getList().add(rqid);
            	rb.getResultHeader().setTypeCode("AA");
    			rb.getResultHeader().setText("撤销收费申请成功!");
            }

		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		}
		return rb;
	}

	private ResultHeader  getString(String url,String logname){
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
		String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
		String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		ResultHeader rh= new ResultHeader();
		try{
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
		sb.append("    <code value=\"delete\"/>                                                                                                  "                      );
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
		sb.append("            <item extension=\""+this.feeMessage.getExam_num()+"\" root=\"2.16.156.10011.1.13\"/>                                           "                            );
		sb.append("            <!--患者 ID 标识(0..1)-->                                                                                      "                      );
		sb.append("            <item extension=\""+this.feeMessage.getPATIENT_ID()+"\" root=\"2.16.156.10011.0.2.2\"/>                                          "                 );
		sb.append("          </id>                                                                                                            "                      );
		sb.append("          <!--PERSONAL=个检;GROUP=团检(0..1)-->                                                                            "                      );
		sb.append("          <code code=\"PERSONAL\" displayName=\"个检\"/>                                                                   "                      );
		sb.append("          <!--合同单位，团检时有此节点(0..1)-->                                                                            "                      );
		sb.append("          <unitInContract code=\"\" displayName=\"\"/>                                                                     "                      );
		sb.append("          <!-- 申请类型编码，名称，用于区分此申请是收费还是算绩效，1:收费，2:绩效(1..1) -->                                "                      );
		sb.append("          <typeCode code=\"1\" displayName=\"收费\"/>                                                                           "                      );
		sb.append("          <!-- 体检批次,包含年份信息(1..1) -->                                                                             "                      );
		sb.append("          <peTimes value=\"\"/>                                                                                            "                      );
		sb.append("          <!--收费明细信息,团检时申请状态是绩效时用此节点(1..1) -->                                                        "                      );
		sb.append("          <chargeInfos>                                                                                                    "                      );
		sb.append("            <!--待收费信息(1..*) -->                                                                                       "                      );
		
		double shishoujia=0;
		double yingshoujia=0;
		int count=1;
		for (int i = 0; i < this.feeMessage.getItemCodeList().size(); i++) {
			//Double.parseDouble(fee.getCHARGES());/ (chargingItem.getAmount()*eci.getItemnum());
			shishoujia  +=	this.feeMessage.getItemCodeList().get(i).getAmount();
			yingshoujia += this.feeMessage.getItemCodeList().get(i).getItem_amount();
		
			ChargingItemDTO chargingItem = configService.findChargeItemByHis_num(feeMessage.getItemCodeList().get(i).getHis_num());
		
			sb.append("            <!--待收费信息(1..*) -->                                                                                       "                      );
			sb.append("            <chargeInfo>                                                                                                   "                      );
			sb.append("              <!-- 项目序号(1..1) -->                                                                                      "                      );
			sb.append("              <itemNo>"+ count++ +"</itemNo>                                                                                           "                      );
			sb.append("              <!-- 项目编码 名称，对应收费项目目录的编码(1..1)-->                                                          "                      );
			sb.append("              <item code=\""+chargingItem.getHis_num()+"\" displayName=\""+chargingItem.getItem_name()+"\"/>                                                                           "                      );
			sb.append("              <!-- 实收金额(0..1)-->                                                                                       "                      );
			sb.append("              <costs value=\""+this.feeMessage.getItemCodeList().get(i).getItem_amount()+"\"/>                                                                                          "                      );
			sb.append("              <!-- 应收金额(0..1) -->                                                                                      "                      );
			sb.append("              <charge value=\""+this.feeMessage.getItemCodeList().get(i).getAmount()+"\"/>                                                                                         "                      );
			sb.append("              <!-- 数量以及单位(0..1)-->                                                                                   "                      );
			sb.append("              <count units=\"\" value=\"1\"/>                                                                            "                      );
			sb.append("            </chargeInfo>                                                                                                  "                      );
		
		}
		
		
		sb.append("          </chargeInfos>                                                                                                   "                      );
		sb.append("          <!--收费汇总信息，状态为收费时用此节点(0..1) -->                                                                 "                      );
		sb.append("          <counts>                                                                                                          "                      );  
		ExamInfoUserDTO ei = this.getExamInfoForNum(this.feeMessage.getExam_num(),logname);
		//HisClinicItemPriceListDTO dto = new HisClinicItemPriceListDTO();
		//dto.setClinic_item_code(chargingItem.getHis_num());
		//dto.setSystemdate(DateTimeUtil.getDate());//系统时间
		//List<HisClinicItemPriceListDTO> priceList = configService.getHisjg305(dto);
		
		
		
		
		/*for(HisClinicItemPriceListDTO price : priceList) {
//			double needPay = price.getAmount()*price.getPrice();
			BigDecimal needPay = new BigDecimal(price.getAmount()*price.getPrice());
			BigDecimal realPay = new BigDecimal(price.getAmount()*price.getPrice()*feeMessage.getItemCodeList().get(0).getDiscount()*0.1); 
             int itemNo = 0;
             int orderNo =50;
             }*/
		 sb.append("          <count>                                                                                                          "                      );
         sb.append("            <!-- 项目编码/名称(1..1) -->                                                                                   "                      );
         sb.append("            <item class=\"D\" code=\"CTF001\" displayName=\"查体费\"/>                         "                      );                   
         sb.append("            <units value=\"次\"/>           "      );    
         sb.append("            <spec   value=\"\\\"/>              "      );                                                  
         sb.append("            <amount value=\""+1+"\"/>                            "      );
         sb.append("            <!-- 人数，团检时有此节点(0..1) -->                                                                            "			);
         sb.append("            <number value=\"\"/>                                                                                           "      );
         sb.append("            <!-- 实收总金额(0..1) -->                                                                                      "      );
         sb.append("            <costs value=\""+shishoujia+"\"/>                             "      );                                                  
         sb.append("            <!-- 应收总金额(0..1) -->                                                                                      "      );
         sb.append("            <charge value=\""+yingshoujia+"\"/>                            "      );                                                  
         sb.append("            <!-- 项目序号(1..1) -->                                                                                      "      );
         sb.append("            <itemNo>"+ 1 +"</itemNo>                           "      );                                                  
         sb.append("            <!-- 医嘱序号(1..1) -->                                                                                     "      );
         sb.append("            <orderNo  value=\""+1 +"\"/>                            "      );                                                  
         sb.append("            <!-- 医嘱类别(1..1) -->                                                                                     "      );
         sb.append("            <orderClass  value=\"D\"/>                            "      );                                                  
         sb.append("          </count>                                                                                                         "      );  
		
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