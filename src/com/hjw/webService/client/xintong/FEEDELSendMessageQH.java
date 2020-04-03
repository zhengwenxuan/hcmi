package com.hjw.webService.client.xintong;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
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
import com.hjw.webService.client.xintong.client.HIPMessageServiceService;
import com.hjw.webService.client.xintong.client.HIPMessageServiceServiceLocator;
import com.hjw.webService.client.xintong.client.IHIPMessageService;
import com.hjw.wst.DTO.ExamInfoUserDTO;
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
public class FEEDELSendMessageQH {

	private DelFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	public FEEDELSendMessageQH(DelFeeMessage feeMessage){
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
		
		String gettokens = Gettoken.Gettokens(url,logname);
		TranLogTxt.liswriteEror_to_txt(logname, "统一登录的tokens====" + gettokens);
		if(gettokens.equals("AE")){
			
			TranLogTxt.liswriteEror_to_txt(logname, "统一登录失败===");
			rb.getResultHeader().setTypeCode("AE");
			return rb;
		}
		try {
			xml = JaxbUtil.convertToXml(this.feeMessage, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+feeMessage.getREQ_NO()+":"+xml);
			ResultHeader rh = DELHisMessage(url,logname,this.feeMessage.getExam_num(),gettokens);
            if("AA".equals(rh.getTypeCode())){
            	
            	updatehisreqitem(this.feeMessage.getExam_num(),feeMessage.getREQ_NO(),logname);
            	ReqNo rqid = new ReqNo();
            	rqid.setREQ_NO(this.feeMessage.getREQ_NO());
            	rb.getControlActProcess().getList().add(rqid);
            	rb.getResultHeader().setTypeCode("AA");
            	
    			rb.getResultHeader().setText("撤销收费申请成功!");
    			TranLogTxt.liswriteEror_to_txt(logname, "撤销收费申请成功" +rb.getResultHeader().getTypeCode() + "\r\n");
    			TranLogTxt.liswriteEror_to_txt(logname, "撤销收费申请成功" +rb.getResultHeader().getText() + "\r\n");
            }else{
            	
            	rb.getResultHeader().setTypeCode("AE");
    			rb.getResultHeader().setText("撤销收费申请失败");
    			TranLogTxt.liswriteEror_to_txt(logname, "撤销收费申请失败" +rb.getResultHeader().getTypeCode() + "\r\n");
    			TranLogTxt.liswriteEror_to_txt(logname, "撤销收费申请失败" +rb.getResultHeader().getText() + "\r\n");
            }

		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
			TranLogTxt.liswriteEror_to_txt(logname, "撤销收费申请失败" +rb.getResultHeader().getTypeCode() + "\r\n");
			TranLogTxt.liswriteEror_to_txt(logname, "撤销收费申请失败" +rb.getResultHeader().getText() + "\r\n");
			
		}
		TranLogTxt.liswriteEror_to_txt(logname, "撤销收费========申请" +rb.getResultHeader().getTypeCode() + "\r\n");
		TranLogTxt.liswriteEror_to_txt(logname, "撤销收费========申请" +rb.getResultHeader().getText() + "\r\n");
		return rb;
	}

	

	private ResultHeader  DELHisMessage(String url,String logname, String exam_num, String gettokens){
		ExamInfoUserDTO ei = getExamInfoForNum(exam_num, logname);
		String jzh =  gethisreqitem(this.feeMessage.getREQ_NO(),logname,exam_num);
		//get
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
		String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室id
		String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
		StringBuffer bufferXml = new StringBuffer();
		ResultHeader rhone = new ResultHeader();
			bufferXml.append("<REQUEST>                                                    ");
			bufferXml.append("	<!--接入认证token  未开启认证则传空 值 -->                 ");
			bufferXml.append("	<TOKENID>"+gettokens+"</TOKENID>        ");
			bufferXml.append("	<!-- 发送系统代码 -->                                      ");
			bufferXml.append("	<SENDER>HJW</SENDER>                                       ");
			bufferXml.append("	<!--请求消息ID  -->                                        ");
			bufferXml.append("	<REQ_MSGID>"+UUID.randomUUID().toString().replaceAll("-", "")+"</REQ_MSGID>    ");
			bufferXml.append("	<REQ_PARAMS>                                               ");
			bufferXml.append("		<!-- 需要更新的业务编号 -->                            ");
			bufferXml.append("		<ACTION>XT70005</ACTION>                         ");
			bufferXml.append("		<!-- 具体发往的系统名称 -->                            ");
			bufferXml.append("		<SYSTEM>HIS</SYSTEM>                                   ");
			bufferXml.append("		<!--具体业务号:唯一标识号,申请单号,就诊登记号等  -->   ");
			bufferXml.append("		<BUSINESS_NO>"+jzh+"|"+this.feeMessage.getREQ_NO()+"</BUSINESS_NO>                 ");
			bufferXml.append("		<!--业务状态 execute:执行 active:有效,cancel:撤销-->   ");
			bufferXml.append("		<BUSINESS_STATUS>40</BUSINESS_STATUS>             ");
			bufferXml.append("		<!-- 执行科室ID -->                                    ");
			bufferXml.append("		<EXEC_DEPT_ID>"+kddepid+"</EXEC_DEPT_ID>                      ");
			bufferXml.append("		<!--执行科室名称  -->                                  ");
			bufferXml.append("		<EXEC_DEPT_NAME>"+kddepname+"</EXEC_DEPT_NAME>              ");
			bufferXml.append("		<!-- 执行医生ID -->                                    ");
			bufferXml.append("		<EXEC_EMP_ID>"+doctorid+"</EXEC_EMP_ID>                   ");
			bufferXml.append("		<!-- 执行医生姓名 -->                                  ");
			bufferXml.append("		<EXEC_EMP_NAME>"+doctorname+"</EXEC_EMP_NAME>                    ");
			bufferXml.append("	</REQ_PARAMS>                                              ");
			bufferXml.append("</REQUEST>                                                   ");

			TranLogTxt.liswriteEror_to_txt(logname, "HIS撤销申请入参" + bufferXml.toString() + "\r\n");
			HIPMessageServiceService dam = new HIPMessageServiceServiceLocator(url);
			try {
				IHIPMessageService dams = dam.getHIPMessageServicePort();
				
					String result = dams.HIPMessageServer2016Ext("XT70009", "", bufferXml.toString());
					TranLogTxt.liswriteEror_to_txt(logname, "HIS撤销返回结果" + result + "\r\n");
					if ((result != null) && (result.trim().length() > 0)) {
						//解析申请单结果
						TranLogTxt.liswriteEror_to_txt(logname, "解析his申请单撤销11==" +"" + "\r\n");
						
						 rhone = DelHisresMessage(logname,result);
						 
						TranLogTxt.liswriteEror_to_txt(logname, "HIS撤销返回结果code44==1" + rhone.getTypeCode() + "\r\n");
						TranLogTxt.liswriteEror_to_txt(logname, "HIS撤销返回结果code44==1-2" + rhone.getText() + "\r\n");
						if(rhone.getTypeCode().equals("1")){
							rhone.setTypeCode("AA");
							TranLogTxt.liswriteEror_to_txt(logname, "解析HIS申请单撤销55==11" +rhone.getTypeCode() + "\r\n");
							TranLogTxt.liswriteEror_to_txt(logname, "解析HIS申请单撤销55==11-22" +rhone.getText() + "\r\n");
							
						}else{
							rhone.setTypeCode("AE");
							TranLogTxt.liswriteEror_to_txt(logname, "解析HIS申请单撤销55==222" +rhone.getTypeCode() + "\r\n");
							TranLogTxt.liswriteEror_to_txt(logname, "解析HIS申请单撤销55==3333" +rhone.getText() + "\r\n");
							
						}
						
					}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rhone.setTypeCode("AE");
				TranLogTxt.liswriteEror_to_txt(logname, "解析HIS申请单撤销55==222" +rhone.getTypeCode() + "\r\n");
				TranLogTxt.liswriteEror_to_txt(logname, "解析HIS申请单撤销55==222" +rhone.getText() + "\r\n");
			}
			
		return rhone;
	}
	

	private ResultHeader DelHisresMessage(String logname, String result) {
		TranLogTxt.liswriteEror_to_txt(logname, "解析HIS申请单撤销22==" + "解析申请单撤销22 "+ "\r\n");
		ResultHeader rh= new ResultHeader();
		rh.setTypeCode("AE");
		try{
		InputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
		//Map<String, String> xmlMap = new HashMap<>();
		//xmlMap.put("abc", "urn:hl7-org:v3");
		SAXReader sax = new SAXReader();
		//sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
		Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		
		
		
		rh.setTypeCode(document.selectSingleNode("RESPONSE/RETURN_CODE").getText());// 获取消息成功失败 节点;
		rh.setText(document.selectSingleNode("RESPONSE/RETURN_MESSAGE").getText());//获取消息  描述节点
		rh.setSourceMsgId(document.selectSingleNode("RESPONSE/RESP_MSGID").getText());//获取请求消息id 节点
		
		TranLogTxt.liswriteEror_to_txt(logname, "解析HIS申请单撤销22==111" + rh.getTypeCode()+ "\r\n");
		TranLogTxt.liswriteEror_to_txt(logname, "解析HIS申请单撤销22==222" + rh.getText()+ "\r\n");
		
		}catch(Exception ex){
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
	   
		TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销33---111==" +rh.getTypeCode() + "\r\n");
		TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销33---222==" +rh.getText() + "\r\n");
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
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id,c.visit_no ");
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
	
	//查询就诊号 和申请单号关系表
	private String gethisreqitem(String req_no, String logname, String exam_num) {
		TranLogTxt.liswriteEror_to_txt(logname, "开始查询就诊号 和申请单号关系表111：");
		Connection connection = null;
		Statement statement = null;
		String jzh="";
		try {
			connection=jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			String sql = " select his_req_no from zl_req_his_item where req_no='"+req_no+"' and flay='0' and exam_num='"+exam_num+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "select-his_req_no："+sql);
			TranLogTxt.liswriteEror_to_txt(logname, "开始查询就诊号 和申请单号关系表222：");
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				 jzh = rs.getString("his_req_no");
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return jzh;
	}
	
	//退费成功修改his_req_item faly为1
	private void updatehisreqitem(String exam_num, String req_no, String logname) {
		Connection connection = null;
		Statement statement = null;
		TranLogTxt.liswriteEror_to_txt(logname, "修改就诊号 和申请单号关系表111：");
		
		try {
			connection=jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			String sql = " update zl_req_his_item set flay='1' where req_no='"+req_no+"' and exam_num='"+exam_num+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "update-his_req_no："+sql);
			TranLogTxt.liswriteEror_to_txt(logname, "修改就诊号 和申请单号关系表222：");
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		
	}
	
}
