package com.hjw.webService.client.hanshou;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.hanshou.bean.GetReqBean;
import com.hjw.webService.client.hokai.bean.ResLisStatusBeanHK;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.DTO.LisSendDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class LisMessageHS  extends ServletEndpointSupport {

	
	private static ConfigService configService;
	private static CustomerInfoService customerInfoService;
	private static JdbcQueryManager jdbcQueryManager;
	private static Calendar checkDay;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		
		
	}

	public String acceptMessageTest(String xmlmessage) {
		return "返回ok----参数为：" + xmlmessage;
	}
	
	

	

	public String GetLisApplyInfo(String xmlstr) {
		String logname="LisReq";
	
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
		String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
		String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		
		GetReqBean reqBean = getExamNumLisReq(xmlstr,logname);
		String message ="";
		
		if(reqBean.getCode().equals("Success")){
			if(!reqBean.getExam_num().equals("") && reqBean.getExam_num() != null && !reqBean.getServerName().equals("") && reqBean.getServerName()!= null    ){
				ExamInfoUserDTO eu = getExamInfoForNumLisReq(reqBean.getExam_num(), logname);
				
				if(eu != null){
					StringBuffer sb = new StringBuffer();
					
					
					sb.append(" select c.Id as chargingitemId,c.item_name,c.exam_num,d.remark,d.data_name,ec.amount,c.his_num,c.item_code,c.remark as item_sample_type,       ");
					sb.append(" ec.item_amount,hd.dept_code,hd.dept_name,ec.id,ec.pay_status,c.hiscodeClass,s.sample_barcode ");
					sb.append(" from examinfo_charging_item ec,sample_exam_detail s,examResult_chargingItem er,sample_demo sd,data_dictionary d, ");
					sb.append(" department_dep dd ,charging_item  c left join his_dict_dept hd on c.perform_dept = hd.dept_code                  ");
					sb.append(" where ec.examinfo_id = s.exam_info_id and ec.charge_item_id = c.id and s.sample_id = sd.id 	                     ");
					sb.append(" and sd.demo_category = d.id and ec.isActive = 'Y'  and c.interface_flag = '2' and ec.exam_status in ('N','D') 	 ");
					sb.append(" and s.id = er.exam_id  and er.charging_id = ec.charge_item_id  and er.result_type = 'sample' 	                 ");
					sb.append(" and ec.change_item != 'C' and ec.pay_status != 'M' and ec.examinfo_id ='"+eu.getId()+"' 	                     ");
					sb.append(" and c.id in (select charging_id from examinfo_charging_item where examinfo_id='"+eu.getId()+"') 	             ");
					sb.append(" and ec.is_application = 'N'  and (sd.BarCode_Class = 0 or (sd.BarCode_Class = 1 and s.is_binding = 1))	         ");
				
					if(reqBean.getServerName().equals("GetLabApplyInfo")){
						//内镜体检单  dep_num根据数据库内 实际的dep_num 修改
						sb.append(" and dd.dep_num='009'  ");
					}else{
						//所传服务名不存在  不返回申请单信息
						sb.append(" and dd.dep_num='xxxxxxxxxx'  ");
					}
					
					 List<LisSendDTO> sendList = this.jdbcQueryManager.getList(sb.toString(), LisSendDTO.class);
					 
					
					 StringBuffer sb1 = new StringBuffer();
					 if(sendList != null && sendList.size()>0){
						 sb1.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
						 sb1.append("<root>");
						 sb1.append("<returnContents>");
						 
						 
						 for (int i = 0; i < sendList.size(); i++) {
							 sb1.append("<returnContent>");
							 sb1.append("<visitNo>"+eu.getExam_num()+"</visitNo>");
							 sb1.append("<visitType>3</visitType>");
							 sb1.append("<rateTypeName></rateTypeName>                     ");
							 sb1.append("<visitNumber></visitNumber>");
							 sb1.append("<emerFlag></emerFlag>");
							 sb1.append("<diagnoseName></diagnoseName>");
							 sb1.append("<patientId>"+eu.getExam_num()+"</patientId>");
							 sb1.append("<patientName>"+eu.getUser_name()+"</patientName>");
							 sb1.append("<patientSex>"+eu.getSex()+"</patientSex>");
							 sb1.append("<patientBirthDay>"+eu.getBirthday()+"</patientBirthDay>");
							 sb1.append("<patientAge>"+eu.getAge()+"</patientAge>");
							 sb1.append("<applyDate>"+DateTimeUtil.getDateTimes()+"</applyDate>");
							 sb1.append("<applyDeptCode></applyDeptCode>");
							 sb1.append("<applyDeptName>"+kddepid+"</applyDeptName>");
							 sb1.append("<applyOperator>"+doctorname+"</applyOperator>");
							 sb1.append("<bedNo></bedNo>");
							 sb1.append("<icCardNo></icCardNo>");
							 sb1.append("<identityNo>"+eu.getId_num()+"</identityNo>");
							 sb1.append("<labItems>");
							 sb1.append("<labItem>");
							 sb1.append("<seqNO>"+i+1+"</seqNO>");
							 sb1.append("<applyNo>"+sendList.get(i).getSample_barcode()+"</applyNo>");
							 sb1.append("<clinicItemID>"+sendList.get(i).getExam_num()+"</clinicItemID>");
							 sb1.append("<clinicItemName>"+sendList.get(i).getItem_name()+"</clinicItemName>");
							 sb1.append("<itemQuantity>"+1+"</itemQuantity>");
							 sb1.append("<itemPrice>"+sendList.get(i).getAmount()+"</itemPrice>");
							 sb1.append("<itemCharges>"+sendList.get(i).getItem_amount()+"</itemCharges>");
							 sb1.append("<specimenTypeName>"+sendList.get(i).getItem_sample_type()+"</specimenTypeName>");
							 sb1.append("<specimenTypeCode>标本类型代码</specimenTypeCode>");
							 sb1.append("<specimenInfoRemark>标本备注</specimenInfoRemark>");
							 sb1.append("</labItem>");
							 sb1.append("</labItems>");
							 sb1.append("</returnContent>");
							}
						 
						 sb1.append("</returnContents>");
						 sb1.append("</root>");
					 
						 return sb1.toString();
					 }
				
				}else {
					message="error:此体检号,未查询到申请单信息!!";
				}
				
			}else{
				
				message="error:体检号或服务名,不能为空!!";
			}
		}else{
			message="error:获取申请单出现错误";
		}
		
		
		return message;
	}

	public GetReqBean getExamNumLisReq(String xmlstr, String logName) {
		
		GetReqBean ReqBean = new GetReqBean();
		
		try {
			InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			
			ReqBean.setServerName(document.selectSingleNode("root/serverName").getText());//服务名
			ReqBean.setExam_num(document.selectSingleNode("root/visitNo").getText());//体检号
			ReqBean.setCode("Success");
		
			
			}catch(Exception ex){
				ReqBean.setCode("error");
			}
		return ReqBean;
	}
	
	
	public ExamInfoUserDTO getExamInfoForNumLisReq(String exam_num, String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");
		sb.append(" and c.exam_num = '" + exam_num + "' ");
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if ((map != null) && (map.getList().size() > 0)) {
			eu = (ExamInfoUserDTO) map.getList().get(0);
		}
		return eu;
	}
	
	
	public String SendLisLabReport(String reqStr) {
		String logName ="LisRes";
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + reqStr);
		
		String ResMessage ="";
		
		try {
				List<LisResult> lrList =  getLisResXMLLisRes(reqStr,logName);
				
				LisResult lisResult0 = lrList.get(0);
				if((lisResult0.getRead_flag()+"").equals("0")){
					boolean flag = true;
					for (LisResult lisResult : lrList) {
						ExamInfoUserDTO ei = new ExamInfoUserDTO();
				    	
						if(!"".equals(lisResult.getSample_barcode())) {
							ei = configService.getExamInfoForBarcode(lisResult.getSample_barcode());
							if ((ei == null) || (ei.getId() <= 0)) {
								flag = false;
								ResMessage= "error-根据申请单号查无此人-ApplyNo：" + lisResult.getSample_barcode();
							} else if ("Z".equals(ei.getStatus())) {
								flag = false;
								ResMessage = "error-此人已经总检，请先取消总检再回传结果";
							} else {
								
								boolean succ = this.configService.insert_lis_result(lisResult);
								if (succ) {
									ResMessage = "success-lis信息 入库成功";
								}else{
									ResMessage = "error-lis信息 入库错误";	
								}
							}
						} else {
							flag = false;
							ResMessage = "入参中申请单号为空-ApplyNo";
							TranLogTxt.liswriteEror_to_txt(logName, "入参中申请单号为空-ApplyNo"+ResMessage);
						}
					}
					
					if(flag) {
						for (LisResult lisResult : lrList) {
							boolean succ = this.configService.insert_lis_result(lisResult);
							if (succ) {
								ResMessage = "success-lis信息 入库成功";
							}else{
								ResMessage = "error-lis信息 入库错误";	
							}
						}
					}
				}else if((lisResult0.getRead_flag()+"").equals("3")){
					ResMessage="error-pacs信息 接口已过期，请联系火箭蛙。" ;
				}else {
					ResMessage = "error-lis信息-解析错误 ";
				}
				
				
		} catch (Exception ex) {
			ResMessage = "error-lis信息-解析错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex);
		}
		
		return ResMessage;
	}

	public List<LisResult> getLisResXMLLisRes(String xmlstr, String logname) {
		
		List<LisResult> lrList = new ArrayList<LisResult>();
		try {
			
			
			
			boolean flay=false;
			
			InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		
			
			/////////////////////////////日期限制及体检系统通知功能-开始/////////////////////////////
			Calendar deadline = Calendar.getInstance();
			SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");
			//JANUARY一月	FEBRUARY二月		MARCH三月		APRIL四月		MAY五月			JUNE六月
			//JULY七月		AUGUST八月		SEPTEMBER九月	OCTOBER十月		NOVEMBER十一月	DECEMBER十二月
			deadline.set(2020, Calendar.JANUARY, 31, 0, 0, 0);
			String viewDateStr = df.format(deadline.getTime());
			if(new Date().after(deadline.getTime())) {
			/*rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口已过期，请联系火箭蛙，截止日期："+viewDateStr);*/
			flay=true;
			TranLogTxt.liswriteEror_to_txt(logname,"接口已过期，请联系火箭蛙，截止日期："+viewDateStr);
		//	return rb;
			}
			
			if(checkDay == null) {
			checkDay = Calendar.getInstance(); checkDay.add(Calendar.DATE, -1);
			}
			Calendar today = Calendar.getInstance(); today.set(Calendar.HOUR, 0); today.set(Calendar.MINUTE, 0); today.set(Calendar.SECOND, 0);
			if(today.after(checkDay)) {//每天仅检查一遍
			checkDay = today;
			Connection connection = null;
			try {
				//每次先将旧的通知信息打到日志文件
				connection = jdbcQueryManager.getConnection();
				String sql = " select notices from examinatioin_center ";
				ResultSet rs = connection.createStatement().executeQuery(sql);
				String notices="";
				while (rs.next()) {
					notices = rs.getString("notices");
				}
				TranLogTxt.liswriteEror_to_txt(logname, "原来的notices是:"+notices);
				//判断系统到期时间，提前10天提醒客户
				Calendar alertDate = deadline;
				alertDate.add(Calendar.DATE, -10);
				if(new Date().after(alertDate.getTime())) {
					String noticesNew = "系统到期时间为:"+viewDateStr+"，请尽快联系火箭蛙销售人员!!";
					String updatesql = " update examinatioin_center set notices='"+noticesNew+"' ";
					connection.createStatement().executeUpdate(updatesql);
					TranLogTxt.liswriteEror_to_txt(logname, updatesql);
				} else {
					String updatesql = " update examinatioin_center set notices='' ";
					connection.createStatement().executeUpdate(updatesql);
					TranLogTxt.liswriteEror_to_txt(logname, updatesql);
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				connection.close();
			}
			}
			
			Element root = document.getRootElement();
			Element element = (Element) root.element("labDetails");
			List<Element> labEle = element.elements("labDetail");
			
			for(int i=0;i<labEle.size();i++) {
				LisResult lisResult = new LisResult();
				lisResult.setTil_id(logname);
				lisResult.setExam_num(document.selectSingleNode("root/visitNo").getText());//体检号
				
				
				lisResult.setExam_date(document.selectSingleNode("root/labTime").getText());//	检查时间
				
				lisResult.setSh_doctor(document.selectSingleNode("root/auditOperator").getText());//	审核医生
				lisResult.setSample_barcode(((Element) labEle.get(i).element("applyNoLoinc")).getText());//	申请单号
				lisResult.setLis_item_code(((Element) labEle.get(i).element("masterItemCode")).getText());//	LIS组合项目代码
				lisResult.setLis_item_name(((Element) labEle.get(i).element("masterItemName")).getText());//	LIS组合项目代码
				lisResult.setSeq_code(Integer.parseInt(((Element) labEle.get(i).element("sortNo")).getText()));////	顺序号
				lisResult.setReport_item_code(((Element) labEle.get(i).element("itemLoincName")).getText());//	LIS报告细项代码
				lisResult.setReport_item_name(((Element) labEle.get(i).element("itemChiName")).getText());//	LIS报告细项名称
				lisResult.setItem_result(((Element) labEle.get(i).element("itemResult")).getText());//	项目结果
				lisResult.setItem_unit(((Element) labEle.get(i).element("itemResultUnit")).getText());//	单位
				lisResult.setRef(((Element) labEle.get(i).element("referenceDesc")).getText());//	参考范围  referenceHighLimit  referenceLowLimit
				
				if(flay){
					//如果接口到期 中间表的 read_flay设置为3
					lisResult.setRead_flag(3);
				}else{
					lisResult.setRead_flag(0);
				}
				//结果标识含义 ： H偏高、HH偏高报警、L偏低、LL偏低报警、P阳性、Q弱阳性、E错误，由LIS判断，仪器接口不用管
				//结果值标志（N正常L偏低H偏高） 汉寿
				lisResult.setFlag(((Element) labEle.get(i).element("itemResultFlag")).getText());//	高低标志	H-高 L-低N-正常HH-偏高报警LL-偏低报警C-危急  
				
				lrList.add(lisResult);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		return lrList;
	}
	
	public String UpdateLisLabApplyInfo(String strbody) {
		String logname ="LisStatus";
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + strbody);
		ResLisStatusBeanHK rb= new ResLisStatusBeanHK();
		rb=getreqNoLisStatus(strbody);
			if("Success".equals(rb.getCode())){
				ExamInfoUserDTO ei = ei=this.getExamInfoForNumLisStatus(rb.getMessageId());
				if(ei!=null) {
					
					List<String> req_nums = new ArrayList<>();
					String[] req_numss = rb.getReqno().split(",");
					for(int i=0;i<req_numss.length;i++) {
						
						String req_num = req_numss[i].substring(0,req_numss[i].length()-1);
						String statuss = req_numss[i].substring(req_numss[i].length()-1);
						req_nums.add(req_num);
						
						ExaminfoChargingItemDTO eciDTO = this.getExamstatusByReqno(req_num, ei.getExam_num());
						if(eciDTO!=null) {
							String samstatus="W";
							//操作代码和名称：分配条码(备管)(code=10)，采集(code=20),
							//1 条码已打印，11 条码已取消，2已采样，22取消采样，3标本送出，33取消送出，4已核收，44取消核收，5已检验，55取消检验，6已报告，66取消报告，7 已审核, 77取消审核，8 报告已打印,9 已作废
			                 
							if(("1".equals(statuss))||("2".equals(statuss))||("3".equals(statuss))||("4".equals(statuss))||("5".equals(statuss)) || ("6".equals(statuss)) || ("7".equals(statuss)) ||("8".equals(statuss))){//核收
								statuss="C";
								samstatus="H";
								this.configService.setExamInfoChargeItemLisStatus(req_nums, ei.getExam_num(),statuss,samstatus);
							}else if("11".equals(statuss)||"33".equals(statuss)||"44".equals(statuss)||"55".equals(statuss)) {
								if(!"Y".equals(eciDTO.getExam_status())) {
									statuss="N";
									this.configService.setExamInfoChargeItemLisStatus(req_nums, ei.getExam_num(),statuss,samstatus);
								}else {
									rb.setCode("error");
									rb.setText("error:"+req_nums+"申请单号已检查，不可撤销！");
									break;
								}
							}else if(("9".equals(statuss))) {
								if("Z".equals(ei.getStatus())) {
									rb.setCode("error");
									rb.setText("error:体检中心已总检，不可撤销");
									break;
								}
							}
						}else {
							rb.setCode("error");
							rb.setText("error:体检号或申请单号无效！");
							break;
						}
						
						
					}
					
					
				}else {
					rb.setCode("error");
					rb.setText("error:体检号无效！");
				}
				
			}
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getCode()+"----"+rb.getText());
			if("error".equals(rb.getCode())) {
				return rb.getText();
			}
			
		return rb.getCode();
	}
		
	public ResLisStatusBeanHK getreqNoLisStatus(String xmlstr){
		ResLisStatusBeanHK reqno= new ResLisStatusBeanHK();
		
		try{
			InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			
			reqno.setPersionid(document.selectSingleNode("root/patientId").getText());//患者id
			reqno.setMessageId(document.selectSingleNode("root/visitNo").getText());//体检号
			String req_nums = "";
			
			Element root = document.getRootElement();
			Element element = (Element) root.element("applyInfos");
			List<Element> applyNoEle = element.elements("applyInfo");
			
			for (int i = 0; i < applyNoEle.size(); i++) {
				String applyStatus = req_nums+applyNoEle.get(i).element("applyStatus").getText();
				String req_num = req_nums+applyNoEle.get(i).element("applyNo").getText()+applyStatus;
				
				if(i==applyNoEle.size()-1) {
					req_nums = req_nums+req_num;
					
				}else {
					req_nums = req_nums+req_num+",";
				}
			}
			reqno.setReqno(req_nums);//条码
			reqno.setStatus(document.selectSingleNode("root/visitType").getText());//申请单状态
			
			reqno.setCode("Success");
		}catch(Exception ex){
				reqno.setCode("error");
				reqno.setText("error:xml解析失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return reqno;
		
	}
	
		/**
		 * 
		 * @param url
		 * @param view_num
		 * @return
		 */
		public ExamInfoUserDTO getExamInfoForNumLisStatus(String exam_info_id) throws ServiceException {
			StringBuffer sb = new StringBuffer();
			sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
					+ ",c.register_date,c.join_date,c.exam_times ");
			sb.append(" from exam_info c ");
			sb.append(" where c.is_Active='Y' and c.status != 'Z' ");		
			sb.append(" and c.exam_num = '" + exam_info_id + "' order by c.create_time desc ");	
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
			ExamInfoUserDTO eu = null;
			if((map!=null)&&(map.getList().size()>0)){
				eu= (ExamInfoUserDTO)map.getList().get(0);			
			}
			return eu;
		} 
		
		public ExaminfoChargingItemDTO getExamstatusByReqno(String sample_barcode,String exam_num) throws ServiceException {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT e.id AS examid,e.exam_status AS examstau,a.id AS samid,a.status AS samsta " + 
					" FROM sample_exam_detail a,exam_info b,examResult_chargingItem c,examinfo_charging_item e " + 
					" WHERE a.sample_barcode='"+sample_barcode+"' AND a.exam_info_id=b.id AND b.exam_num='"+exam_num+"' " + 
					" AND a.id=c.exam_id AND c.charging_id=e.charge_item_id AND e.examinfo_id=b.id AND c.result_type = 'sample' "  );
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExaminfoChargingItemDTO.class);
			ExaminfoChargingItemDTO eciDTO = null;
			if((map!=null)&&(map.getList().size()>0)){
				eciDTO= (ExaminfoChargingItemDTO)map.getList().get(0);			
			}
			return eciDTO;
		}
		
}
