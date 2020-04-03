package com.hjw.webService.client.xintong;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.hokai.bean.ResFeeStatusBeanHK;
import com.hjw.wst.DTO.UserDTO;
import com.hjw.wst.service.LisPacsApplicationService;
import com.synjones.framework.persistence.JdbcQueryManager;

public class FEEResMessageQH {

	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private static LisPacsApplicationService lisPacsApplicationService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		lisPacsApplicationService = (LisPacsApplicationService) wac.getBean("lisPacsApplicationService");
	}

	public String getMessage(String strbody, String logName) {
		String rescode="1";
		Connection tjtmpconnect = null;
		TranLogTxt.liswriteEror_to_txt(logName, "res:" + strbody);
		ResFeeStatusBeanHK rb = new ResFeeStatusBeanHK();
		if(strbody!=null && strbody.length()>0){
			
			rb = getreqNo(strbody,logName);
		}else{
			rescode="-1";
			rb.setCode("AE");
			rb.setText("入参不能为空");
		}

		// http 返回客户端消息
		StringBuffer sb = new StringBuffer("");
		if (rb.getCode().equals("AA")) {
			rescode="1";
			try {
				tjtmpconnect = this.jdbcQueryManager.getConnection();

				String sb1 = "select css.exam_id,css.id,css.charging_status,ei.exam_num from charging_summary_single css,exam_info ei where css.exam_id=ei.id "
						+ "and ei.is_Active='Y' "
						//+ "and css.charging_status='R' "
						+ " and css.req_num = '"+rb.getReqno()+"'";
				TranLogTxt.liswriteEror_to_txt(logName, "查询 操作语句： " + sb1);
				ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
				while (rs.next()) {
					long exam_id = rs.getLong("exam_id");
					long summary_id = rs.getLong("id");
					String charging_status = rs.getString("charging_status");
					String exam_num = rs.getString("exam_num");

					if ("R".equals(charging_status)) {
						rescode="1";
						// 修改eci表内的 结算状态
						String updatesql = "update examinfo_charging_item set pay_status='Y' where id in"
								+ "( select eci.id from charging_detail_single cds,examinfo_charging_item eci "
								+ "where cds.summary_id='" + summary_id
								+ "' and eci.charge_item_id=cds.charging_item_id and eci.examinfo_id='" + exam_id
								+ "')";
						TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + updatesql);
						tjtmpconnect.createStatement().execute(updatesql);

						// 修改个人收费总表的 charging_stats状态
						updatesql = "update charging_summary_single set charging_status='Y' where id='" + summary_id+ "'";
							
						
						
						//发送lis、pacs申请
						String doctorid = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_ID").getConfig_value().trim();//开单医生id
						String doctorname = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_NAME").getConfig_value().trim();//开单医生姓名
						UserDTO user = new UserDTO();
						user.setWork_num(doctorid);
						user.setName(doctorname);
						String bangding = configService.getCenterconfigByKey("IS_LIS_BANGDING").getConfig_value().trim();
						String lis = configService.getCenterconfigByKey("IS_HIS_LIS_CHECK").getConfig_value().trim();
						String pacs = configService.getCenterconfigByKey("IS_PACS_INTERFACE").getConfig_value().trim();
						if ("Y".equals(lis) && "N".equals(bangding)) {
							try {
								String ret = lisPacsApplicationService.lisSend(exam_num, null, user, false, bangding,lis);
								TranLogTxt.liswriteEror_to_txt(logName, "发送lis申请返回： " + ret);
							}  catch (Exception ex) {
								TranLogTxt.liswriteEror_to_txt(logName, "发送lis申请： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
							}
						}
						if ("Y".equals(pacs)) {
							try {
								String ret = lisPacsApplicationService.pacsSend(exam_num, null, user, false,pacs);
								TranLogTxt.liswriteEror_to_txt(logName, "发送lis申请返回： " + ret);
							}  catch (Exception ex) {
								TranLogTxt.liswriteEror_to_txt(logName, "发送pacs申请： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
							}
						}
						
						
						TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + updatesql);
						tjtmpconnect.createStatement().execute(updatesql);
						rb.setCode("AA");
						rb.setText("状态通知成功");
						
						
					} else {
						
						rescode="-1";
						TranLogTxt.liswriteEror_to_txt(logName, "收费状态错误：" + exam_num + "-" + charging_status);
						rb.setCode("AA");
						rb.setText("收费状态错误：不是待收费状态");
					}

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				rescode="-1";
				e.printStackTrace();
			} finally {
				try {
					if (tjtmpconnect != null) {
						tjtmpconnect.close();
					}
				} catch (SQLException sqle4) {
					sqle4.printStackTrace();
				}
			}

			// 返回消息
			sb.append("	<RESPONSE>                                                     ");
			sb.append("		<!-- 请求消息ID -->                                        ");
			sb.append("		<REQ_MSGID></REQ_MSGID>    ");
			sb.append("		<!-- 响应消息-->                                           ");
			sb.append("		<RESP_MSGID>" + UUID.randomUUID().toString().toLowerCase() + "</RESP_MSGID>  ");
			sb.append("		<!--反正状态代码 1:成功 !  -1:失败  -->                    ");
			sb.append("		<RETURN_CODE>"+rescode+"</RETURN_CODE>                               ");
			sb.append("		<!-- 执行结果 -->                                          ");
			sb.append("		<RETURN_MSG>HIS收费状态更新操作成功收到业务状态为:!" + rb.getStatus()+ "</RETURN_MSG>                         ");
			sb.append("	</RESPONSE>                                                    ");

		} else {
			rescode="-1";
			// 返回消息
			sb.append("	<RESPONSE>                                                     ");
			sb.append("		<!-- 请求消息ID -->                                        ");
			sb.append("		<REQ_MSGID></REQ_MSGID>    ");
			sb.append("		<!-- 响应消息-->                                           ");
			sb.append("		<RESP_MSGID>" + UUID.randomUUID().toString().toLowerCase() + "</RESP_MSGID>  ");
			sb.append("		<!--反正状态代码 1:成功 !  -1:失败  -->                    ");
			sb.append("		<RETURN_CODE>"+rescode+"</RETURN_CODE>                               ");
			sb.append("		<!-- 执行结果 -->                                          ");
			sb.append("		<RETURN_MSG>HIS收费状态更新操作失败! 收到业务状态为:" + rb.getStatus()+ "</RETURN_MSG>                         ");
			sb.append("	</RESPONSE>                                                    ");

		}

		return sb.toString();

	}

	// 解析 his费用状态xml
	private ResFeeStatusBeanHK getreqNo(String strbody,String logName) {
		ResFeeStatusBeanHK reqno = new ResFeeStatusBeanHK();
	/*	String result = QHResolveXML.getNodeAttVal(strbody,
				"abc:CUST_OUT00004/abc:controlActProcess/abc:subject/abc:message_contents", "val");

		String result2 = result.replaceAll("&lt;", "<");
		String resultXML = result2.replaceAll("&gt;", ">");

		// 解析XML结果
		// String resultBase64 = QHResolveXML.getNodeAttVal(resultXML,
		// "abc:ProvideAndRegisterDocumentSetRequest/abc:Document/abc:Content","val");

		// 消息id
		String MessageId = QHResolveXML.getNodeAttVal(resultXML, "abc:REQUEST/abc:REQ_MSGID", "val");
		// String setMessageId = QHResolveXML.getNodeAttVal(resultXML,
		// "abc:CUST_OUT00004/abc:controlActProcess/abc:subject/abc:message_contents","val");

		// 申请单号
		String Reqno = QHResolveXML.getNodeAttVal(resultXML, "abc:REQUEST/abc:BUSINESS_NO", "val");

		// 业务状态 execute:执行 active:有效,cancel:撤销
		String code = QHResolveXML.getNodeAttVal(resultXML, "abc:REQUEST/abc:BUSINESS_STATUS", "val");

		reqno.setCode(code);
		reqno.setMessageId(MessageId);
		reqno.setReqno(Reqno);*/
		
		try{
			TranLogTxt.liswriteEror_to_txt(logName, "his状态回传入参：" + strbody  );
			InputStream is = new ByteArrayInputStream(strbody.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			
			/*
			<Request>
			 <!--体检号-->
			 <TJH></TJH>
			 <!--收费状态1已收费，2已退费-->
			 <STATUS></STATUS>
			 <!--支付方式-->
			 <PAY_TYPE></PAY_TYPE> 
			</Request>*/
			
			
			reqno.setPersionid(document.selectSingleNode("Request/TJH").getText());//患者id
			reqno.setMessageId(document.selectSingleNode("Request/PAY_TYPE").getText());//支付方式
			reqno.setReqno(document.selectSingleNode("Request/SEQ_NO").getText());//申请单号
			reqno.setStatus(document.selectSingleNode("Request/STATUS").getText());//收费状态1已收费，2已退费
			
			reqno.setCode("AA");
		}catch(Exception ex){
				reqno.setCode("AE");
				reqno.setText("xml解析失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		
		

		return reqno;
	}

}
