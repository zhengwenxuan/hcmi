package com.hjw.webService.client.hokai305;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fees;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.hokai305.bean.LisReqCustomerMessage305;
import com.hjw.wst.DTO.ChargingSummaryGroupDTO;
import com.hjw.wst.DTO.ChargingSummarySingleDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.domain.ChargingSummarySingle;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class FEEReqMessageHK305 {

	private FeeMessage feeMessage;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	
	public String getFeereqmeassgae(String strxml,String logname){
		ResultHeader rh= new ResultHeader();
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
		String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
		String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		String ORDERED_BY_DEPT="";
        String ORDERED_BY_DOCTOR="";
        String PERFORMED_BY="";
		LisReqCustomerMessage305  customer =  getReqPatid(strxml,logname);
		
		if((customer.getPATIENT_ID().equals("")|| customer.getPATIENT_ID()==null) && (customer.getComid().equals("") || customer.getComid()==null)){
			
			LisReqCustomerMessage305 lisreq = new LisReqCustomerMessage305();
			lisreq.setCode("AE");
			lisreq.setMessage("患者id或单位编码不能为空!!");
			
			StringBuffer sbxml = new StringBuffer();
			
			sbxml.append("<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> ");
			sbxml.append("    <!-- 消息ID(系统生成唯一UUID)(1..1) -->                                                                                 ");
			sbxml.append("    <id extension=\"22a0f9e0-4454-11dc-a6be-3603d6866807\"/>                                                                ");
			sbxml.append("    <!-- 消息创建时间(1..1) -->                                                                                             ");
			sbxml.append("    <creationTime value=\"20111129220000\"/>                                                                                ");
			sbxml.append("    <!-- 服务编码，S0040代表检验申请查询(1..1)-->                                                                           ");
			sbxml.append("    <interactionId extension=\"S0040\"/>                                                                                    ");
			sbxml.append("    <!-- 接受者(1..1) -->                                                                                                   ");
			sbxml.append("    <receiver code=\"SYS002\"/>                                                                                       ");
			sbxml.append("    <!-- 发送者(1..1) -->                                                                                                   ");
			sbxml.append("    <sender code=\"SYS009\"/>                                                                                         ");
			sbxml.append("    <!--typeCode为处理结果， AA表示成功 AE表示失败-->                                                                       ");
			sbxml.append("    <acknowledgement typeCode=\""+lisreq.getCode()+"\">                                                                                       ");
			sbxml.append("        <targetMessage>                                                                                                     ");
			sbxml.append("            <id extension=\"1ee83ff1-08ab-4fe7-b573-ea777e9bad51\"/>                                                        ");
			sbxml.append("        </targetMessage>                                                                                                    ");
			sbxml.append("        <acknowledgementDetail>                                                                                             ");
			sbxml.append("            <text value=\""+lisreq.getMessage()+"\"/>                                                                                  ");
			sbxml.append("        </acknowledgementDetail>                                                                                            ");
			sbxml.append("    </acknowledgement>                                                                                                      ");
			sbxml.append("</MCCI_IN000002UV01>		");
		
		return sbxml.toString();
		}else{
			
			
			ExamInfoUserDTO eu = getExamInfoForNum(customer.getEXAM_NUM(),customer.getPATIENT_ID(),logname);
			ArrayList<ChargingSummarySingle> hisSampList = ExamInfoIdForHisSamp(eu.getId());
			
			StringBuffer sb= new StringBuffer();
			try{
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
				
				
				for (ChargingSummarySingle hisList : hisSampList) {
					sb.append("    <subject typeCode=\"SUBJ\">                                                                                            "                      );
					sb.append("      <observationRequest classCode=\"OBS\" moodCode=\"RQO\">                                                              "                      );
					sb.append("        <!-- 申请单号(1..1) -->                                                                                            "                      );
					sb.append("        <id>                                                                                                               "                      );
					sb.append("          <item extension=\""+hisList.getReq_num()+"\" root=\"2.16.156.10011.1.24\"/>                                                    "  );
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
					sb.append("            <item extension=\""+eu.getExam_num()+"\" root=\"2.16.156.10011.1.13\"/>                                           "                            );
					sb.append("            <!--患者 ID 标识(0..1)-->                                                                                      "                      );
					sb.append("            <item extension=\""+eu.getPatient_id()+"\" root=\"2.16.156.10011.0.2.2\"/>                                          "                 );
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

					
					/*fee.setCHARGES(charges+"");//实收金额
					fee.setCOSTS(charges+"");  //计价金额
			*/		
					/*for(Fee fee:this.feeMessage.getPROJECTS().getPROJECT()){
						String charges = fee.getCHARGES();
						String costs = fee.getCOSTS();
						ChargingItemDTO chargingItem = configService.findChargeItemByHis_num(fee.getITEM_CODE());
						HisClinicItemPriceListDTO dto = new HisClinicItemPriceListDTO();
						dto.setClinic_item_code(chargingItem.getHis_num());
						dto.setSystemdate(DateTimeUtil.getDate());//系统时间
						//dto.setBody_part(chargingItem.getItem_abbreviation());
						//dto.setMethod(chargingItem.getNotices());
						List<HisClinicItemPriceListDTO> priceList = configService.getHisjg305(dto);
						  int itemNo = 0;
				          int orderNo =50;
						for(HisClinicItemPriceListDTO price : priceList) {
//							double needPay = price.getAmount()*price.getPrice();
							BigDecimal needPay = new BigDecimal(price.getAmount()*price.getPrice());
							BigDecimal realPay = new BigDecimal(price.getAmount()*price.getPrice()*fee.getDiscount()*0.1); 
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
				}*/
					
					
					/*double shishoujia=0;
					double yingshoujia=0;
					for (int i = 0; i < this.feeMessage.getPROJECTS().getPROJECT().size(); i++) {
						//Double.parseDouble(fee.getCHARGES());/ (chargingItem.getAmount()*eci.getItemnum());
						shishoujia  +=	Double.parseDouble(this.feeMessage.getPROJECTS().getPROJECT().get(i).getCHARGES());
						yingshoujia += Double.parseDouble(this.feeMessage.getPROJECTS().getPROJECT().get(i).getITEM_PRICE());
					}*/
					
					 sb.append("          <count>                                                                                                          "                      );
			         sb.append("            <!-- 项目编码/名称(1..1) -->                                                                                   "                      );
			         sb.append("            <item code=\""+234+"\" displayName=\"查体费\"/>                         "                      );                   
			         sb.append("            <!-- 人数，团检时有此节点(0..1) -->                                                                            "			);
			         sb.append("            <number value=\"\"/>                                                                                           "      );
			         sb.append("            <!-- 实收总金额(0..1) -->                                                                                      "      );
			         sb.append("            <costs value=\""+hisList.getAmount1()+"\"/>                             "      );                                                  
			         sb.append("            <!-- 应收总金额(0..1) -->                                                                                      "      );
			         sb.append("            <charge value=\""+hisList.getAmount2()+"\"/>                            "      );                                                  
			         sb.append("            <!-- 项目序号(1..1) -->                                                                                      "      );
			         sb.append("            <itemNo>"+ 1 +"</itemNo>                           "      );                                                  
			         sb.append("            <!-- 医嘱序号(1..1) -->                                                                                     "      );
			         sb.append("            <orderNo  value=\""+1 +"\"/>                            "      );                                                  
			         sb.append("            <!-- 医嘱类别(1..1) -->                                                                                     "      );
			         sb.append("            <orderClass  value=\"J\"/>                            "      );                                                  
			         sb.append("          </count>                                                                                                         "      );
					
					
					
					 sb.append("          </counts>                                                                                                         "      );
					 sb.append("        </encounter>                                                                                                       "      );
					 sb.append("      </observationRequest>                                                                                                "      );
					 sb.append("    </subject>                                                                                                             "      );
					 
				}
				
				 sb.append("    <!--操作者信息(0..1)-->                                                                                                "      );
				 sb.append("    <author code=\""+doctorid+"\" displayName=\""+doctorname+"\"/>                                                         "      );      
				 sb.append("  </controlActProcess>                                                                                                     "      );
				 sb.append("</POOR_IN200901UV>    																										"      );
				
				}catch(Exception ex){
					rh.setTypeCode("AE");
					rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
				}
			
			return sb.toString();
		}
		
	}
	private ArrayList<ChargingSummarySingle> ExamInfoIdForHisSamp(long id) {
		StringBuffer sb = new StringBuffer();
		
		ArrayList<ChargingSummarySingle> SummaryResList = new ArrayList<ChargingSummarySingle>();
		sb.append(" select  * from charging_summary_single css where css.exam_id='"+id+"'  and css.is_active='Y' ");
		
		 SummaryResList =	(ArrayList<ChargingSummarySingle>) this.jdbcQueryManager.getList(sb.toString(), ChargingSummarySingle.class);
				
		
		
		return SummaryResList;
	}


	public ExamInfoUserDTO getExamInfoForNum(String exam_num,String patid,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' and a.is_Active='Y'");		
		sb.append(" and c.patient_id = '" + patid + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
	
	
	
		//获取入参值
		private LisReqCustomerMessage305 getReqPatid(String xmlstr,String logname){
			//ResLisStatusBeanHK reqno= new ResLisStatusBeanHK();
			LisReqCustomerMessage305 lisreq = new LisReqCustomerMessage305();
			try{
				InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
				Map<String, String> xmlMap = new HashMap<>();
				xmlMap.put("abc", "urn:hl7-org:v3");
				SAXReader sax = new SAXReader();
				sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
				Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
				
				//	String patient_id = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:recordTarget/abc:patient/abc:id/item[@root='2.16.156.10011.0.2.2']/@extension").getText();// 获取根节点
				//类型 个检 团检
				lisreq.setExamtype(document.selectSingleNode("abc:POOR_IN200901UV/abc:controlActProcess/abc:code/@displayName").getText());
				
				if(lisreq.getExamtype().equals("个检")){
					//患者id
					lisreq.setPATIENT_ID(document.selectSingleNode("abc:POOR_IN200901UV/abc:controlActProcess/abc:queryByParameter/abc:id/abc:item[@root='2.16.156.10011.0.2.2']/@extension").getText());
					
				}else{
					//单位id
					lisreq.setComid(document.selectSingleNode("abc:POOR_IN200901UV/abc:controlActProcess/abc:queryByParameter/abc:unitInContract/@code").getText());
					//单位名称
					lisreq.setComname(document.selectSingleNode("abc:POOR_IN200901UV/abc:controlActProcess/abc:queryByParameter/abc:unitInContract/@displayName").getText());
				}
				lisreq.setCode("AA");
			}catch(Exception ex){
				lisreq.setCode("AE");
				lisreq.setMessage("xml解析失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
			return lisreq;
			
		}
}
