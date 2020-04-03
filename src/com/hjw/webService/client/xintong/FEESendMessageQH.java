package com.hjw.webService.client.xintong;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.DateUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.xintong.bean.HisClinicItemQH;
import com.hjw.webService.client.xintong.client.HIPMessageServiceService;
import com.hjw.webService.client.xintong.client.HIPMessageServiceServiceLocator;
import com.hjw.webService.client.xintong.client.IHIPMessageService;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class FEESendMessageQH {
	
	private static FeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public FEESendMessageQH(FeeMessage feeMessage) {
		this.feeMessage = feeMessage;
	}
	
	/**
	 * 青海收费接口
	 * @param url
	 * @param logname
	 * @return
	 */
	public FeeResultBody getMessage(String url, String logname) {
		
		 String exam_num=feeMessage.getPROJECTS().getPROJECT().get(0).getEXAM_NUM();
	     ExamInfoUserDTO ei= getExamInfoForNum(exam_num,logname);
		
		//读取数据配置信息
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		
		String gettokens = Gettoken.Gettokens(url,logname);
		TranLogTxt.liswriteEror_to_txt(logname, "统一登录的tokens====" + gettokens);
		if(gettokens.equals("AE")){
			
			TranLogTxt.liswriteEror_to_txt(logname, "统一登录失败===");
			rb.getResultHeader().setTypeCode("AE");
			return rb;
		}
		

		StringBuffer sbgettokens = new  StringBuffer();

		sbgettokens.append(" <REQUEST>                                                             ");
		sbgettokens.append(" <TOKENID>"+gettokens+"</TOKENID>                                                     ");
		sbgettokens.append(" <!--消息发送方在平台上的注册标识-->                                   ");
		sbgettokens.append(" <SENDER>HJW</SENDER>                                                     ");
		sbgettokens.append(" <!--请求消息ID,由消息发送方生成单次请求唯一的标识，建议用uuid 32位--> ");
		sbgettokens.append(" <REQ_MSGID>"+UUID.randomUUID().toString().toLowerCase()+"</REQ_MSGID>   ");
		sbgettokens.append(" 	<REQ_PARAMS>                                                         ");
		sbgettokens.append(" <ACCESS_TOKEN>"+gettokens+"</ACCESS_TOKEN>                                         ");
		sbgettokens.append(" </REQ_PARAMS>                                                         ");
		sbgettokens.append(" </REQUEST>                                                            ");
		
		try {
			xml = JaxbUtil.convertToXml(feeMessage, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"传入参数体检系统数据:==="+feeMessage.getREQ_NO()+":"+xml);
			
			HIPMessageServiceService dam = new HIPMessageServiceServiceLocator(url);
			IHIPMessageService dams = dam.getHIPMessageServicePort();
			
			ResultHeader rh = menzhenjiuzhendengji(url, logname, ei, exam_num,gettokens);
		
			if(rh.getTypeCode().equals("AA")){
				
				TranLogTxt.liswriteEror_to_txt(logname,"开始拼接his费用入参==sendXml==:"+"");
				
				String sendXml = sendXML(url,logname,gettokens,rh.getText());
				TranLogTxt.liswriteEror_to_txt(logname,"结束his费用拼接入参==sendXml==:"+sendXml);
				
					String messages = dams.HIPMessageServer2016Ext("XT70005","", sendXml);
					
					TranLogTxt.liswriteEror_to_txt(logname,"调用返回信息==:"+feeMessage.getREQ_NO()+":"+messages);
				
						QHResolveXML ssl = new QHResolveXML();
						
						Map<String, String> mapXML = ssl.resolveXMLQH(messages, true);
						
						TranLogTxt.liswriteEror_to_txt(logname,"返回结果信息typeCode===:"+mapXML.get("RETURN_CODE")+"===text==="+mapXML.get("ERROR_MESSAGE"));
						
						if("1".equals(mapXML.get("RETURN_CODE"))) {
							
							insertzl_req_his_item(feeMessage.getREQ_NO(),rh.getText(),ei.getExam_num(),logname); 
							
							rb.getResultHeader().setTypeCode("AA");
							rb.getResultHeader().setText("发送收费申请成功");
						}else {
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("返回结果错误"+mapXML.get("ERROR_MESSAGE"));
						}
					
			}
			
		
		} catch (Exception ex){
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息调用webservice错误");
		}
		
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "his收费返回结果信息====:" + xml);
		
		return rb;
	}
	
	
	

	/**
	 * 发送拼接XML信息
	 * @param gettokens 
	 * @param jzh 
	 * @return
	 */
	private  String sendXML(String url,String logname, String gettokens, String jzh) {
		TranLogTxt.liswriteEror_to_txt(logname,"开始拼接his入参==sendXml==:"+"组装xml----1");
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
		String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
		String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		
      
        String exam_num=feeMessage.getPROJECTS().getPROJECT().get(0).getEXAM_NUM();
        ExamInfoUserDTO ei= getExamInfoForNum(exam_num,logname);
        List<ExaminfoChargingItemDTO> ecidao = getexaminfocharitem(ei.getId(),logname);

		
		
		
		StringBuffer bufferXml = new StringBuffer();
		bufferXml.append("<REQUEST>                                            ");
		bufferXml.append("<TOKENID>"+gettokens+"</TOKENID>                      ");
		bufferXml.append("<SENDER>HJW</SENDER>                                    ");
		bufferXml.append("<REQ_MSGID>"+UUID.randomUUID().toString().toLowerCase()+"</REQ_MSGID>     ");
		bufferXml.append("   <!--机构ID -->                                    ");
		bufferXml.append("   <ORG_ID>RSS20140108000000001</ORG_ID>             ");
		bufferXml.append("   <!--患者标识ID-->                                 ");
		bufferXml.append("   <PAT_ID>"+ei.getPatient_id()+"</PAT_ID>                                 ");
		bufferXml.append("   <!--患者类型代码-->                               ");
		bufferXml.append("   <PAT_TYPE_CODE>1</PAT_TYPE_CODE>                   ");
		bufferXml.append("   <!--就诊登记ID -->                                ");
		bufferXml.append("   <VISIT_REG_ID>"+jzh+"</VISIT_REG_ID>                     ");
		bufferXml.append("   <!--项目收费登记ID 可用于更新已登记项目数目   -->                                ");
		bufferXml.append("   <CRG_ID></CRG_ID>                     ");
		bufferXml.append("   <!--开单时间-->                                   ");
		
		
		bufferXml.append("   <ORDERED_DATE>"+DateTimeUtil.getDateTime()+"</ORDERED_DATE>                     ");
		bufferXml.append("   <!--开单科室 ID-->                                ");
		bufferXml.append("   <ORDERED_DEPT_ID>"+kddepid+"</ORDERED_DEPT_ID>               ");
		bufferXml.append("   <!--开单医生的ID-->                               ");
		bufferXml.append("   <ORDERED_DOCTOR_ID>"+doctorid+"</ORDERED_DOCTOR_ID>                           ");
		bufferXml.append("   <!--计费科室ID-->                                 ");
		bufferXml.append("   <CRG_DEPT_ID>"+kddepid+"</CRG_DEPT_ID>                       ");
		bufferXml.append("   <!--计费人ID-->                                   ");
		bufferXml.append("   <CRG_PERSON_ID>"+doctorid+"</CRG_PERSON_ID>                   ");
		bufferXml.append("   <!--计费时间-->                                   ");
		bufferXml.append("   <CRG_DATE>"+DateTimeUtil.getDateTime()+"</CRG_DATE>                             ");
		bufferXml.append("   <!--计费项目列表 -->                              ");
		bufferXml.append("   <CRG_LIST>                                        ");
		bufferXml.append("      <!--计费项目信息,1.* -->                       ");
	
		
		
			
		for(Fee pro:feeMessage.getPROJECTS().getPROJECT()){
			
			//List<HisClinicItemPriceListDTO> priceList = configService.getHisjg(dto);
			HisClinicItemQH dto= new HisClinicItemQH();

			dto.setItem_code(pro.getITEM_CODE());
			HisClinicItemQH cliniclist = getHisClinicItem(dto);
			
			bufferXml.append("	  <CRG_INFO>                                       ");
			bufferXml.append("		<!--医嘱ID-->                                  ");
			bufferXml.append("		<ORDR_ID></ORDR_ID>                            ");
			bufferXml.append("		<PACK_FLAG>"+cliniclist.getItem_class()+"</PACK_FLAG>                       ");
			bufferXml.append("		<!--计费项目ID-->                              ");
			bufferXml.append("		<CRG_ITEM_ID>"+pro.getITEM_CODE()+"</CRG_ITEM_ID>                    ");
			bufferXml.append("		<!--计费项目流水号-->                              ");
			bufferXml.append("		<CRG_ITEM_SER>"+this.feeMessage.getREQ_NO()+"</CRG_ITEM_SER>                    ");
			bufferXml.append("        <!--项目代码-->                              ");
			bufferXml.append("        <ITEM_CODE>"+pro.getITEM_CODE()+"</ITEM_CODE>                   ");
			bufferXml.append("        <!--项目名称-->                              ");
			bufferXml.append("        <ITEM_NAME>"+cliniclist.getItem_name()+"</ITEM_NAME>                ");
			bufferXml.append("        <!--规格-->                                  ");
			if(cliniclist.getExpand1() == null || cliniclist.getExpand1().equals("") ){
				bufferXml.append("        <SPEC_NAME>个</SPEC_NAME>                    ");
			}else{
				
				bufferXml.append("        <SPEC_NAME>"+cliniclist.getExpand1()+"</SPEC_NAME>                    ");
			}
			bufferXml.append("        <!--单价-->                                  ");
			bufferXml.append("        <PRICE>"+pro.getCHARGES()+"</PRICE>                              ");
			bufferXml.append("        <!--计价单位-->	                           ");
			
			if(cliniclist.getExpand2() == null || cliniclist.getExpand2().equals("") ){
				bufferXml.append("        <CHARGE_UNIT>元</CHARGE_UNIT>                  ");
			}else{
				
				bufferXml.append("        <CHARGE_UNIT>"+cliniclist.getExpand2()+"</CHARGE_UNIT>                  ");
			}
			
			bufferXml.append("        <!--总计价数量-->                            ");
			bufferXml.append("        <TOTAL_QTY>"+1+"</TOTAL_QTY>                      ");
			bufferXml.append("        <!--总计价数量金额-->                        ");
			double charge = 0.0; //实收总费
			
			bufferXml.append("        <TOTAL_AMT>"+Double.parseDouble(pro.getCHARGES())+"</TOTAL_AMT>                      ");
			
			if(cliniclist.getExpand3() == null || cliniclist.getExpand3().equals("")){
				bufferXml.append("        <!--费别代码-->                              ");
				bufferXml.append("        <CRG_TYPE>99</CRG_TYPE >                     ");
			}else{
				
				bufferXml.append("        <!--费别代码-->                              ");
				bufferXml.append("        <CRG_TYPE>"+cliniclist.getExpand3()+"</CRG_TYPE >                     ");
			}
			bufferXml.append("        <!--病案首页费别代码-->                      ");
			bufferXml.append("        <FP_CRG_TYPE></FP_CRG_TYPE >             ");
			bufferXml.append("        <!--执行科室ID-->                            ");
			bufferXml.append("        <EXEC_DEPT_ID>"+kddepid+"</EXEC_DEPT_ID>                ");
			
			bufferXml.append("        <!--执行人ID-->                              ");
			bufferXml.append("        <EXEC_PERSON_ID>"+doctorid+"</EXEC_PERSON_ID>            ");
			bufferXml.append("        <!--执行时间-->                              ");
			bufferXml.append("        <EXEC_DATE>"+DateTimeUtil.getDateTime()+"</EXEC_DATE>                      ");
			bufferXml.append("	  </CRG_INFO>                                      ");
			
		}
		
		bufferXml.append("	</CRG_LIST>                                        ");
		bufferXml.append("</REQUEST>	                                       ");
		return bufferXml.toString();
		
	}
	
	
	private void insertzl_req_his_item(String req_no, String jzh, String exam_num, String logname) {
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sql = " insert into zl_req_his_item (exam_num,req_no,charging_item_code,his_req_no,createdate,flay,his_num) "
						+ " values ('"+exam_num+"','"+req_no+"','','"+jzh+"','"+DateUtil.getDateTime()+"','0','')";
			
			TranLogTxt.liswriteEror_to_txt(logname, "个人收费流水表插入中间表res==insertsql:" + sql + "\r\n");
			tjtmpconnect.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		
		
	}
	
	public List<ExaminfoChargingItemDTO> getexaminfocharitem(long exam_id, String logname) {
		StringBuffer sb = new StringBuffer();
		sb.append("   select  e.charge_item_id,e.item_amount,e.discount,e.amount,c.his_num,c.item_code,c.item_name "
				+ " from examinfo_charging_item e,charging_item c "
				+ " where e.charge_item_id=c.id and e.isActive='Y' "
				+ " and c.isActive='Y' and e.exam_indicator='G' "
				+ " and e.examinfo_id='"+exam_id+"'  ");
		
		TranLogTxt.liswriteEror_to_txt(logname, "req查询收费项目:" + sb.toString() + "\r\n");
		List<ExaminfoChargingItemDTO> list = jdbcQueryManager.getList(sb.toString(),ExaminfoChargingItemDTO.class);
		
		return list;
	}

	public HisClinicItemQH getHisClinicItem(HisClinicItemQH dto) {
		//查询诊疗项目表
			String sql="select item_class,item_code,item_name,input_code,expand1,expand2,expand3,item_status "
					+ " from his_clinic_item  where item_code='"+dto.getItem_code()+"'";
		    List<HisClinicItemQH> li =jdbcQueryManager.getList(sql,HisClinicItemQH.class);
		    return li.get(0);
	}

	public  ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id,c.visit_no ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	
	
	/**
	 * 查询个人基本信息是否已经注册
	 * @param get_no 
	 * @param ei 
	 * @param logname 
	 * @param url 
	 * @param patiendId 病人ID
	 * @param logname  
	 * @param username 
	 * @param gettokens 
	 * @param charging_summary_num 
	 * @return 
	 * @return
	 */
	
	public  ResultHeader menzhenjiuzhendengji(String url, String logname, ExamInfoUserDTO ei, String exam_num,String gettokens){
		
		ResultHeader rhone  = new ResultHeader();
		HIPMessageServiceService dam = new HIPMessageServiceServiceLocator(url);
		try {
			IHIPMessageService dams = dam.getHIPMessageServicePort();
			//3.查询就诊号
			String uuidjz = UUID.randomUUID().toString().toLowerCase();
			String getjzhxml = getXT10001(url,logname,ei.getPatient_id(),exam_num,uuidjz,gettokens);//门诊就诊登记xml
			String resgetjzhxml = dams.HIPMessageServer2016Ext("XT10001", "",getjzhxml);
			
			TranLogTxt.liswriteEror_to_txt(logname, "==返回 获取就诊号 XML===" + resgetjzhxml);
			
			
			
			rhone = getjzhres(resgetjzhxml,logname);
			
			if(rhone.getTypeCode().equals("1")){
				rhone.setTypeCode("AA");
				//4.门诊就诊登记xml
				String mzjzdj = AmbulatoryEncounterStarted(url,logname,ei,uuidjz,rhone.getText());
				TranLogTxt.liswriteEror_to_txt(logname, "==开始  拼接 门诊就诊服务 XML===" + mzjzdj);
				TranLogTxt.liswriteEror_to_txt(logname, "==开始  拼接 门诊就诊服务第三个参数 XML===" + getjzhxml);
				
				String resultMzjzdj = dams.HIPMessageServer2016Ext("XT-AmbulatoryEncounterStarted", mzjzdj,getjzhxml);
				
				TranLogTxt.liswriteEror_to_txt(logname, "==返回 门诊就诊服务 XML===" + resultMzjzdj);
				
				if("AA".equals(QHResolveXML.getXmlResult(resultMzjzdj,"MCCI_IN000002UV01","typeCode"))){
					TranLogTxt.liswriteEror_to_txt(logname, "==门诊就诊登记成功===" + QHResolveXML.getXmlResult(resultMzjzdj,"MCCI_IN000002UV01","typeCode"));
				}else{
					TranLogTxt.liswriteEror_to_txt(logname, "==门诊就诊登记失败===" + QHResolveXML.getXmlResult(resultMzjzdj,"MCCI_IN000002UV01","typeCode"));
				}
				
			}else{
				TranLogTxt.liswriteEror_to_txt(logname, "==获取就诊号===" + rhone.getText());
				rhone.setTypeCode("AE");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return rhone;
		
	}
	
	
		//门诊就诊登记服务 拼接xml
		public String AmbulatoryEncounterStarted(String url , String logname,ExamInfoUserDTO ei, String uuidjz, String jzh){

			String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//his开单医生id
			String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//his开单医生名称
			
			String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
			String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
			
			
			StringBuffer bufferXml = new StringBuffer();
			bufferXml.append("<PRPA_IN401001UV02 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/PRPA_IN401001UV02.xsd\">   ");
			bufferXml.append("	<id root=\"2.16.156.10011.0\" extension=\""+uuidjz+"\"/>                                                                                                                           ");
			bufferXml.append("	<creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>                                                                                                                                                                     ");
			bufferXml.append("	<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"PRPA_IN201301UV02\"/>                                                                                                                              ");
			bufferXml.append("	<processingCode code=\"P\"/>                                                                                                                                                                                 ");
			bufferXml.append("	<processingModeCode code=\"R\"/>                                                                                                                                                                             ");
			bufferXml.append("	<acceptAckCode code=\"AL\"/>                                                                                                                                                                                 ");
			bufferXml.append("	<receiver typeCode=\"RCV\">                                                                                                                                                                                  ");
			bufferXml.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">                                                                                                                                                   ");
			bufferXml.append("			<id root=\"2.16.156.10011.0.1.1\" extension=\"HIP\"/>                                                                                                                               ");
			bufferXml.append("		</device>                                                                                                                                                                                                ");
			bufferXml.append("	</receiver>                                                                                                                                                                                                  ");
			bufferXml.append("	<sender typeCode=\"SND\">                                                                                                                                                                                    ");
			bufferXml.append("		<device classCode=\"CER\" determinerCode=\"INSTANCE\">                                                                                                                                                   ");
			bufferXml.append("			<id root=\"2.16.156.10011.0.1.2\" extension=\"HJW\"/>                                                                                                                               ");
			bufferXml.append("		</device>                                                                                                                                                                                                ");
			bufferXml.append("	</sender>                                                                                                                                                                                                    ");
			bufferXml.append("	<controlActProcess classCode=\"CACT\" moodCode=\"APT\">                                                                                                                                                      ");
			bufferXml.append("		<subject typeCode=\"SUBJ\">                                                                                                                                                                              ");
			bufferXml.append("			<!--挂号登记事件信息 -->                                                                                                                                                                             ");
			bufferXml.append("			<encounterEvent classCode=\"ENC\" moodCode=\"EVN\">                                                                                                                                                  ");
			bufferXml.append("				<!--门（急）诊号数据元：HDSD00.02.040	DE01.00.010.00 就诊流水号-->                                                                                                                             ");
			bufferXml.append("				<id root=\"2.16.156.10011.0.5.1\" extension=\""+jzh+"\"/>                                                                                                                                   ");
			bufferXml.append("				<!--就诊类别代码（患者类别代码）数据元: HDSD00.02.024	DE02.01.060.00   值域：1.门诊 2.急诊 3.住院 9.其他-->                                                                                    ");
			bufferXml.append("				<code code=\"1\" codeSystem=\"2.16.156.10011.2.3.1.271\" codeSystemName=\"9\" displayName=\"其他\"/>                                                                                ");
			bufferXml.append("				<statusCode code=\"active\"/>                                                                                                                                                                    ");
			bufferXml.append("				<!--就诊日期时间数据元：HDSD00.02.036	DE06.00.062.00 -->                                                                                                                                       ");
			bufferXml.append("				<effectiveTime value=\""+DateTimeUtil.getDateTimes()+"\"/>                                                                                                                                                              ");
			bufferXml.append("				<!--就诊原因数据元：HDSD00.02.037	DE05.10.053.00-->                                                                                                                                            ");
			bufferXml.append("				<reasonCode>                                                                                                                                                                                     ");
			bufferXml.append("					<originalText>体检</originalText>                                                                                                                                              ");
			bufferXml.append("				</reasonCode>                                                                                                                                                                                    ");
			bufferXml.append("				<!--患者-->                                                                                                                                                                                      ");
			bufferXml.append("				<subject typeCode=\"SBJ\" contextControlCode=\"OP\">                                                                                                                                             ");
			bufferXml.append("					<patient classCode=\"PAT\">                                                                                                                                                                  ");
			bufferXml.append("						<!--平台注册的患者ID -->                                                                                                                                                                 ");
			bufferXml.append("						<id code=\""+ei.getPatient_id()+"\"/>                                                                                                                                                                    ");
			bufferXml.append("						<!--患者基本信息-->                                                                                                                                                                      ");
			bufferXml.append("						<patientPerson>                                                                                                                                                                          ");
			bufferXml.append("							<!--姓名数据元：HDSD00.02.027	DE02.01.039.00 -->                                                                                                                                   ");
			bufferXml.append("							<name use=\"L\">"+ei.getUser_name()+"</name>                                                                                                                                                        ");
			bufferXml.append("						</patientPerson>                                                                                                                                                                         ");
			bufferXml.append("					</patient>                                                                                                                                                                                   ");
			bufferXml.append("				</subject>                                                                                                                                                                                       ");
			bufferXml.append("				<!--接诊医生-->                                                                                                                                                                                  ");
			bufferXml.append("				<consultant typeCode=\"CON\">                                                                                                                                                                    ");
			bufferXml.append("					<assignedPerson classCode=\"ASSIGNED\">                                                                                                                                                      ");
			bufferXml.append("						<!--医生的职工号-->                                                                                                                                                                      ");
			bufferXml.append("						<id root=\"2.16.156.10011.0.3.2\" extension=\""+doctorid+"\"/>                                                                                                                                    ");
			bufferXml.append("						<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                                                           ");
			bufferXml.append("							<!--责任医师姓名数据元：HDSD00.02.059	DE02.01.039.00	-->                                                                                                                          ");
			bufferXml.append("							<name>"+doctorname+"</name>                                                                                                                                                                  ");
			bufferXml.append("						</assignedPerson>                                                                                                                                                                        ");
			bufferXml.append("					</assignedPerson>                                                                                                                                                                            ");
			bufferXml.append("				</consultant>                                                                                                                                                                                    ");
			bufferXml.append("				<!--服务机构-->                                                                                                                                                                                  ");
			bufferXml.append("				<location typeCode=\"ORG\">                                                                                                                                                                      ");
			bufferXml.append("					<time/>                                                                                                                                                                                      ");
			bufferXml.append("					<serviceDeliveryLocation classCode=\"SDLOC\">                                                                                                                                                ");
			bufferXml.append("						<statusCode code=\"active\"/>                                                                                                                                                            ");
			bufferXml.append("						<location classCode=\"PLC\" determinerCode=\"INSTANCE\">                                                                                                                                 ");
			bufferXml.append("							<!--科室代码数据元：HDSD00.02.055	DE08.10.052.00	-->                                                                                                                              ");
			bufferXml.append("							<id root=\"2.16.156.10011.0.4.2\" extension=\""+kddepid+"\"/>                                                                                                                                ");
			bufferXml.append("							<!--科室名称数据元：HDSD00.02.054	DE08.10.026.00	-->                                                                                                                              ");
			bufferXml.append("							<name>"+kddepname+"</name>                                                                                                                                                                  ");
			bufferXml.append("						</location>                                                                                                                                                                              ");
			bufferXml.append("						<!--服务机构-->                                                                                                                                                                          ");
			bufferXml.append("						<serviceProviderOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                                                              ");
			bufferXml.append("							<!--医疗机构代码-->                                                                                                                                                                  ");
			bufferXml.append("							<id root=\"2.16.156.10011.1.5\" extension=\"44000115-4\"/>                                                                                                                           ");
			bufferXml.append("						</serviceProviderOrganization>                                                                                                                                                           ");
			bufferXml.append("					</serviceDeliveryLocation>                                                                                                                                                                   ");
			bufferXml.append("				</location>                                                                                                                                                                                      ");
			bufferXml.append("			</encounterEvent>                                                                                                                                                                                    ");
			bufferXml.append("		</subject>                                                                                                                                                                                               ");
			bufferXml.append("	</controlActProcess>                                                                                                                                                                                         ");
			bufferXml.append("</PRPA_IN401001UV02>                                                                                                                                                                                           ");
			
			return bufferXml.toString();
		}
	//就诊号获取
		private String getXT10001(String url, String logname, String patientid, String exam_NUM, String uuidjz, String gettokens) {
			
			StringBuffer bufferXml = new StringBuffer();
			bufferXml.append("  <REQUEST>                                                               ");
			bufferXml.append("  <!--用户在平台统一登录令牌-->                                           ");
			bufferXml.append("  <TOKENID>"+gettokens+"</TOKENID>                                                     ");
			bufferXml.append("  <!--消息发送方在平台上的注册标识-->                                     ");
			bufferXml.append("  <SENDER>HJW</SENDER>                                                       ");
			bufferXml.append("  <!--请求消息ID,由消息发送方生成单次请求唯一的标识，建议用uuid 32位-->   ");
			bufferXml.append("  <REQ_MSGID>"+uuidjz+"</REQ_MSGID>                                                 ");
			bufferXml.append("  	<!-- 机构ID,必须-->                                                 ");
			bufferXml.append("  	<ORG_ID>RSS20140108000000001</ORG_ID>                               ");
			bufferXml.append("  <!-- 获取业务号类型-->                                                  ");
			bufferXml.append("  	<NO_TYPE>1</NO_TYPE>                                                 ");
			bufferXml.append("  </REQUEST>                                                              ");
			
			TranLogTxt.liswriteEror_to_txt(logname, "==收费申请前获取就诊号认证XML===" + bufferXml.toString());  
			return bufferXml.toString();
		}
		
		
		
		//解析获取的就诊卡号
		private ResultHeader getjzhres(String resgetjzh, String logname) {
			ResultHeader rh= new ResultHeader();
			rh.setTypeCode("AE");
			try{
			InputStream is = new ByteArrayInputStream(resgetjzh.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			
			
			
			rh.setTypeCode(document.selectSingleNode("RESPONSE/RETURN_CODE").getText());// 获取消息成功失败 节点;
			rh.setText(document.selectSingleNode("RESPONSE/NO_VAL").getText());//此处 存放 就诊号
			rh.setSourceMsgId(document.selectSingleNode("RESPONSE/NO_VAL").getText());//获取请求消息id 节点
			
			
			TranLogTxt.liswriteEror_to_txt(logname, "解析获取到的就诊号==成功或失败" + rh.getTypeCode()+ "\r\n");
			TranLogTxt.liswriteEror_to_txt(logname, "解析获取到的就诊号==就诊号" + rh.getText()+ "\r\n");
			}catch(Exception ex){
				rh.setTypeCode("AE");
				rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		   
		    return rh;
		}

}
