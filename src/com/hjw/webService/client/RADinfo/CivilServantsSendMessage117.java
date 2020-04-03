package com.hjw.webService.client.RADinfo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.RADinfo.bean.ResultCivilBody;
import com.hjw.webService.client.RADinfo.client.Exception;
import com.hjw.webService.client.RADinfo.client.WebServiceEntry;
import com.hjw.webService.client.RADinfo.client.WebServiceEntryServiceLocator;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExamSummaryDTO;
import com.hjw.wst.DTO.ExaminfoDiseaseDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

public class CivilServantsSendMessage117 {

	private final String mesType="CIVIL_SERVANTS_SEND";
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
	}
	
	public ResultCivilBody getMessage(String exam_num){
		String logname = "CIVIL_SERVANTS";
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = webserviceConfigurationService.getWebServiceConfig(mesType);
		if(wcd == null || wcd.getConfig_url().split("&").length != 3){
			ResultCivilBody body = new ResultCivilBody();
			body.setTypeCode("AE");
			body.setText("请检查配置表webservice_configuration 中的配置 CIVIL_SERVANTS_SEND");
			return body;
		}
		String[] urls = wcd.getConfig_url().split("&");
		String reqxml = this.getreqxml(exam_num);
		String[] params = {reqxml};
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + reqxml);
		String resultxml = this.getResult(urls[0],urls[1], urls[2], "adapter.transportService", "transportDataByXmlContent", params);
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + resultxml);
		ResultCivilBody body = this.formatResult(resultxml,exam_num);
		return body;
	}

	private String getResult(String urls,String appId,String pwd,String service,String method,String[] params){
		String result = "";
		if(appId.isEmpty() || pwd.isEmpty() || service.isEmpty() || method.isEmpty() || params.toString().isEmpty()){
			result = "调用方法出错，请检查输入信息";
			return result;
		}
		try {
			WebServiceEntryServiceLocator webService = new WebServiceEntryServiceLocator(urls);
			WebServiceEntry s= webService.getWebServiceEntryPort();
			result = s.invoke(appId, pwd, service, method, params);
		} catch (ServiceException e) {
			e.printStackTrace();
			result = com.hjw.interfaces.util.StringUtil.formatException(e);
		} catch (Exception e) {
			e.printStackTrace();
			result = com.hjw.interfaces.util.StringUtil.formatException(e);
		} catch (RemoteException e) {
			e.printStackTrace();
			result = com.hjw.interfaces.util.StringUtil.formatException(e);
		}
		return result;
	}
	
	private ResultCivilBody formatResult(String resultxml,String exam_num){
		ResultCivilBody body = new ResultCivilBody();
		
		try {
			InputStream is = new ByteArrayInputStream(resultxml.getBytes("utf-8"));
			SAXReader sax = new SAXReader();// 创建一个SAXReader对象
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			
			String code = document.selectSingleNode("/response/code").getStringValue();// 获取根节点
			String text = document.selectSingleNode("/response/message/describe").getStringValue();// 获取根节点
			if("200".equals(code)){
				this.jdbcPersistenceManager.executeSql("update exam_info set mope_tran_flag = 1,mope_tran_date = '"+DateTimeUtil.getDateTime()+"' where exam_num = '"+exam_num+"'");
				body.setTypeCode("AA");
				body.setText(text);
			}else{
				body.setTypeCode("AE");
				body.setText(text);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			body.setTypeCode("AE");
			body.setText(com.hjw.interfaces.util.StringUtil.formatException(e));
		} catch (DocumentException e) {
			e.printStackTrace();
			body.setTypeCode("AE");
			body.setText(com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		return body;
	}
	
	private String getreqxml(String exam_num){
		StringBuffer reqxml = new StringBuffer();
		String sql = "select e.exam_num,e.id,c.arch_num,c.user_name,c.sex,convert(varchar(100),c.birthday,23)birthday,c.id_num,"
				+ "c.address,e.phone,e.create_time,convert(varchar(100),e.join_date,23) join_date,convert(varchar(100),e.final_date,"
				+ "23) final_date,e.final_doctor,e.is_marriage,e.customer_type_id,v.com_name company,v.company_id from exam_info e,customer_info c,"
				+ "v_exam_comp_batch_group v where e.customer_id = c.id and v.id = e.id and e.exam_num = '"+exam_num+"'";
		List<ExamInfoUserDTO> infoList = this.jdbcQueryManager.getList(sql, ExamInfoUserDTO.class);
		ExamInfoUserDTO info = null;
		if(infoList.size() > 0){
			info = infoList.get(0);
		}else{
			info = new ExamInfoUserDTO();
		}
		String sexcode = "2";
		if("男".equals(info.getSex())){
			sexcode = "1";
		}else if("女".equals(info.getSex())){
			sexcode = "2";
		}
		String marraycode = "90";
		if("未婚".equals(info.getIs_marriage())){
			marraycode = "10";
		}else if("已婚".equals(info.getIs_marriage())){
			marraycode = "20";
		}
		sql = "select e.id,e.final_exam_result from exam_summary e where e.exam_info_id = '"+info.getId()+"'";
		List<ExamSummaryDTO> summarylist = this.jdbcQueryManager.getList(sql,ExamSummaryDTO.class);
		ExamSummaryDTO summary = new ExamSummaryDTO();
		if(summarylist.size() > 0){
			summary = summarylist.get(0);
		}
		sql = "select d.disease_name,d.suggest from examinfo_disease d where d.exam_info_id = '' order by d.disease_index";
		List<ExaminfoDiseaseDTO> list = this.jdbcQueryManager.getList(sql, ExaminfoDiseaseDTO.class);
		String disease_name = "";
		String suggest = "";
		for (ExaminfoDiseaseDTO disease :list) {
			disease_name += disease.getDisease_name() + "\r\n";
			suggest += disease.getSuggest() + "\r\n";
		}
		
		reqxml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		reqxml.append("<Request>");
		reqxml.append("<Header>");
		reqxml.append("<DocInfo>");
		reqxml.append("<RecordClassifying>InpatientRecordHome</RecordClassifying>");//记录分类  由第三方系统集成商写入
		reqxml.append("<RecordTitle>住院病案首页</RecordTitle>");//记录名称
		reqxml.append("<EffectiveTime>"+DateTimeUtil.getDateTime()+"</EffectiveTime>");//业务发生时间
		reqxml.append("<Authororganization localText='中国人民解放军第117医院'>47011661433010211A1001</Authororganization>");//创建机构     由第三方系统集成商写入，节点属性localText必填，取值为医疗机构名称；节点值为医疗机构编码。
		reqxml.append("<SourceID>"+info.getExam_num()+"</SourceID>");//来源标识    数据集标识符就诊流水号（没有就按照主键）由第三方系统集成商写入
		reqxml.append("<BatchID>"+info.getId()+"</BatchID>"); //批次号  由第三方系统集成商写入
		reqxml.append("<VersionNumber>1.0</VersionNumber>");//版本号
		reqxml.append("<AuthorID>14</AuthorID>");//作者（医生编号）
		reqxml.append("<Author>林龙芳</Author>");//作者（医生）
		reqxml.append("<SystemTime>"+info.getCreate_time()+"</SystemTime>");//2012-06-07T09:28:30  录入系统时间 
		reqxml.append("</DocInfo>");
		
		reqxml.append("<Patient>");//患者信息节点
		reqxml.append("<PersonName>"+info.getUser_name()+"</PersonName>");//姓名
		reqxml.append("<SexCode>"+sexcode+"</SexCode>");//性别    见性别代码
		reqxml.append("<Birthday>"+info.getBirthday()+"</Birthday>");//出生日期 
		reqxml.append("<IdCard>"+info.getId_num()+"</IdCard>"); //证件号
		reqxml.append("<IdType>03</IdType>");//证件类型  见证件类别  
		reqxml.append("<CardNo></CardNo>");//卡号
		reqxml.append("<CardType></CardType>");//卡类型 
		reqxml.append("<AddressType>02</AddressType>"); //地址类型  见地址类别 
		reqxml.append("<Address>"+info.getAddress()+"</Address>");//地址
		reqxml.append("<ContactNo>"+info.getPhone()+"</ContactNo>");//联系电话
		reqxml.append("</Patient>");
		reqxml.append("</Header>");
		
		reqxml.append("<Body DocFormat=\"02\">");
		reqxml.append("<TJLSH>"+info.getExam_num()+"</TJLSH>");//体检流水号
		reqxml.append("<ZZMM></ZZMM>");//保健号
		reqxml.append("<ZJ>05</ZJ>");    //对应职级  
		reqxml.append("<ZZQK>01</ZZQK>");//在职情况
		reqxml.append("<HY>"+marraycode+"</HY>");//婚姻
//		reqxml.append("<DWMC>01</DWMC>");//
//		reqxml.append("<XSDY>01</XSDY>");//
//		reqxml.append("<JFLY>01</JFLY>");//
		reqxml.append("<TJLX>01</TJLX>");//体检类型
		reqxml.append("<RYLX>01</RYLX>");//人员类型
		reqxml.append("<TBDW>"+info.getCompany()+"</TBDW>");//填报单位
		reqxml.append("<DWDZ></DWDZ>");//单位地址
		reqxml.append("<YZBM></YZBM>");//单位邮政编码
		reqxml.append("<LXR></LXR>");//联系人
		reqxml.append("<LXRDH></LXRDH>");//联系人电话
		reqxml.append("<LXRSJ></LXRSJ>");//联系人手机
//		reqxml.append("<SFYTJ>1</SFYTJ>");//
		reqxml.append("<BZ></BZ>");//备注
		reqxml.append("<TJZT>01</TJZT>");//是否已体检或修养
		reqxml.append("<TBR></TBR>");//填报人
		reqxml.append("<TBRDH></TBRDH>");//填报人电话
		reqxml.append("<ZDBM>"+summary.getId()+"</ZDBM>");//诊断编码
		reqxml.append("<TJZD>"+summary.getFinal_exam_result()+"</TJZD>");//体检诊断
		reqxml.append("<ZDJG>"+disease_name+"</ZDJG>");//诊断结果
		reqxml.append("<TJRQ>"+info.getJoin_date()+"</TJRQ>");//体检日期
//		reqxml.append("<BGDHSJM></BGDHSJM>");//
		reqxml.append("<ZJYSXM>"+info.getFinal_doctor()+"</ZJYSXM>");//总检医生
		reqxml.append("<TJJY>"+suggest+"</TJJY>");//体检建议
		reqxml.append("<ZJRQ>"+info.getFinal_date()+"</ZJRQ>");//总检日期
		reqxml.append("<ZT>0</ZT>");//状态
		reqxml.append("<BAK1></BAK1>");//
		reqxml.append("<BAK2></BAK2>");//
		reqxml.append("</Body>");
		reqxml.append("</Request>");
		
		return reqxml.toString();
	}
	public static void main(String[] args) {
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<response><code>901</code><message><describe>数据不符合规范</describe></message></response>";
		CivilServantsSendMessage117 msg = new CivilServantsSendMessage117();
		ResultCivilBody body = msg.formatResult(result,"");
		System.out.println(body.getTypeCode());
		System.out.println(body.getText());
	}
}
