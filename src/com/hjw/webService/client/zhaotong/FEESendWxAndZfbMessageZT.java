package com.hjw.webService.client.zhaotong;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.stable.core.UUID;
import com.hjw.interfaces.util.DateUtil;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.LockCenterDateUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.FeeResultWxAndZfbBody;
import com.hjw.webService.client.body.FeeWxAndZfbMessage;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.wst.DTO.ChargingDetailSingleDTO;
import com.hjw.wst.DTO.ChargingSummarySingleDTO;
import com.hjw.wst.DTO.ExamInfoDTO;
import com.hjw.wst.domain.ChargingWaySingle;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

public class FEESendWxAndZfbMessageZT {

	
	private FeeWxAndZfbMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static ConfigService configService;
	private static CustomerInfoService customerInfoService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		configService = (ConfigService) wac.getBean("configService");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	}

	public FEESendWxAndZfbMessageZT(FeeWxAndZfbMessage feeMessage) {
		this.feeMessage = feeMessage;
	}
	
	public FeeResultWxAndZfbBody getMessage(String url, String logname) {
		
		FeeResultWxAndZfbBody fr = new FeeResultWxAndZfbBody();
		String exam_num = feeMessage.getExam_num();
		ExamInfoDTO ei = customerInfoService.getExamInfoForexamNum(exam_num);
		
		StringBuffer sb = new StringBuffer();
		
		ResHdMeessage rhd = LockCenterDateUtil.SetEaminatioinCenterDate(2020, Calendar.APRIL, 10, logname);
		if(rhd.getStatus().equals("AE")){
			System.err.println(rhd.getStatus()+"===日期");
			
		}else{
			double  number=feeMessage.getAmount2()*100;
			 String Amount2;
		        if (((int) number * 1000) == (int) (number * 1000)) {
		            //如果是一个整数
		        	Amount2 = String.valueOf((int) number);
		        } else {
		            DecimalFormat df = new DecimalFormat("######0.00");
		            Amount2 = df.format(number);
		        }
		        
		        
			sb.append(" <?xml version=\"1.0\" encoding=\"UTF-8\"?>    ");
			sb.append(" <req>                                     ");
			sb.append(" <businessId>"+feeMessage.getReq_nums()+"</businessId>    ");
			sb.append(" <businessType>6</businessType>            ");
			sb.append(" <orderAmout>"+Amount2+"</orderAmout>          ");
			sb.append(" <authCode>"+feeMessage.getPeis_trade_code()+"</authCode>   ");
			sb.append(" <machineId></machineId>           ");
			
			String payMode="";
			if(feeMessage.getChargeType().equals("WX")){
				payMode="9801";
			}else{
				payMode="9901";
			}
			sb.append(" <payMode>"+payMode+"</payMode>                   ");
			sb.append(" <remark></remark>          ");
			
			
			if(ei.getId_num().equals("") || ei.getId_num()==null){
				
				sb.append(" <healthCardNo>"+ei.getExam_num()+"</healthCardNo> ");
			}else{
				sb.append(" <healthCardNo>"+ei.getId_num()+"</healthCardNo> ");
			}
			
			sb.append(" <patientName>"+ei.getUser_name()+"</patientName>       ");
			sb.append(" </req>                                    ");
			
			TranLogTxt.liswriteEror_to_txt(logname, "支付申请入参" + sb.toString() + "\r\n");
			try {
				String res = HttpUtil.doPost_Str(url, sb.toString(), "utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "支付申请返回" + res + "\r\n");
				if(res.length()>10 && !res.equals("") && res !=null){
					
				fr = getResdata(res,logname);
				if(fr.getResultCode().equals("AA")){
					
					String  sqlPos = "select  cws.*  from  charging_summary_single   css ,  charging_way_single cws where css.id = cws.summary_id and css.is_active = 'Y' and css.req_num = '"+feeMessage.getReq_nums()+"'";
					List<ChargingWaySingle> list = this.jdbcQueryManager.getList(sqlPos, ChargingWaySingle.class);
					if (list.size() > 0&&list!=null) {
						ChargingWaySingle chargingWaySingle = list.get(0);
						String addsql = " insert  into  pos_detail(pay_way , peis_trade_code , amount , pos_type ,daily_status,trans_code) "
								    + " values('"+chargingWaySingle.getCharging_way()+"','"+feeMessage.getReq_nums()+"','"+chargingWaySingle.getAmount()+"','1','0','00')";
						
						TranLogTxt.liswriteEror_to_txt(logname, "支付申请正常返回 插入 pos_detail==" + addsql + "\r\n");
						this.jdbcPersistenceManager.execSql(addsql);
						 
						 String REC_ID = UUID.randomUUID().toString().replaceAll("-", "");
							
						 
						 String pos_list = "select  * from pos_trans_list where original_voucher_no='"+fr.getOrderId()+"' and original_trade_no='"+fr.getTradeNo()+"' and is_valid='0'";
						 Statement createStatement = this.jdbcQueryManager.getConnection().createStatement();
						 ResultSet rs = createStatement.executeQuery(pos_list);
						 if (!rs.next()) {
							 String pos_trans_list=" insert into pos_trans_list (is_valid,REC_ID,pos_code,pay_class,peis_trade_code,op_code,app_type,trans_code,original_trade_date,original_trade_no,original_voucher_no,trade_no,voucher_no,trade_amount,amount,trade_date,trade_time,empower_code,create_time,UserId)  "
										+ "values ('0','"+REC_ID+"','01','01','"+feeMessage.getReq_nums()+"','1','1','00','"+DateUtil.getDateTime()+"','"+fr.getTradeNo()+"','"+fr.getOrderId()+"','"+fr.getTradeNo()+"','"+fr.getOrderId()+"','"+Amount2+"',"
										+ "'"+fr.getPayAmout()+"','"+fr.getPayTime()+"','"+fr.getPayTime()+"','"+feeMessage.getPeis_trade_code()+"','"+DateUtil.getDateTime()+"','"+feeMessage.getUserid()+"')";
							
								TranLogTxt.liswriteEror_to_txt(logname, "支付申请正常返回 插入 pos_trans_list==" + pos_trans_list + "\r\n");
								this.jdbcPersistenceManager.execSql(pos_trans_list);
						}
						 
					}
					
				}else{
					
					String  sqlPos = "select  cws.*  from  charging_summary_single   css ,  charging_way_single cws where css.id = cws.summary_id and css.is_active = 'Y' and css.req_num = '"+feeMessage.getReq_nums()+"'";
					List<ChargingWaySingle> list = this.jdbcQueryManager.getList(sqlPos, ChargingWaySingle.class);
					if (list.size() > 0&&list!=null) {
						ChargingWaySingle chargingWaySingle = list.get(0);
						 //此处存的金额 单位为分
						String addsql = "insert  into  pos_detail(pay_way , peis_trade_code , amount , pos_type ,daily_status,trans_code) "
								    + "values('"+chargingWaySingle.getCharging_way()+"','"+feeMessage.getReq_nums()+"','"+chargingWaySingle.getAmount()*100+"','1','0','00')";
						TranLogTxt.liswriteEror_to_txt(logname, "支付申请正常返回 插入 pos_detail==" + addsql + "\r\n");
						this.jdbcPersistenceManager.execSql(addsql);
						 
						 String REC_ID = UUID.randomUUID().toString().replaceAll("-", "");
							
						 //此处存的金额 单位为分
						String pos_trans_list="insert into pos_trans_list (is_valid,REC_ID,pos_code,pay_class,peis_trade_code,op_code,app_type,trans_code,original_trade_date,original_trade_no,original_voucher_no,trade_no,voucher_no,trade_amount,amount,trade_date,trade_time,empower_code,create_time,UserId)  "
								+ "values ('-1','"+REC_ID+"','01','01','"+feeMessage.getReq_nums()+"','1','1','00','"+DateUtil.getDateTime()+"','"+fr.getTradeNo()+"','','"+fr.getTradeNo()+"','','"+Amount2+"',"
								+ "'"+fr.getPayAmout()+"','"+fr.getPayTime()+"','"+fr.getPayTime()+"','"+feeMessage.getPeis_trade_code()+"','"+DateUtil.getDateTime()+"','"+feeMessage.getUserid()+"')";
						TranLogTxt.liswriteEror_to_txt(logname, "支付申请正常返回 插入 pos_trans_list==" + pos_trans_list + "\r\n");
						this.jdbcPersistenceManager.execSql(pos_trans_list);
						
						String chargingSummarysql = "  select top 1 exam_id from charging_summary_single where id='"+list.get(0).getSummary_id()+"' and is_active='Y'";
						List<ChargingSummarySingleDTO> SummaryList = this.jdbcQueryManager.getList(chargingSummarysql, ChargingSummarySingleDTO.class);
						
						
						String charging_detail_singlesql =" select charging_item_id from charging_detail_single where summary_id='"+list.get(0).getSummary_id()+"' ";
						List<ChargingDetailSingleDTO> DetailList = this.jdbcQueryManager.getList(charging_detail_singlesql, ChargingDetailSingleDTO.class);
						
						for (ChargingDetailSingleDTO chargingDetailSingleDTO : DetailList) {
							String ecisql =  " update examinfo_charging_item set pay_status='N' where  charge_item_id='"+chargingDetailSingleDTO.getCharging_item_id()+"' and  examinfo_id='"+SummaryList.get(0).getExam_id()+"' ";
							this.jdbcPersistenceManager.execSql(ecisql);
						}
						
						
						String charsummarysql = " update charging_summary_single set is_active='N' where id='"+list.get(0).getSummary_id()+"' ";
						this.jdbcPersistenceManager.execSql(charsummarysql);
						
						
						String delwaysql = "delete charging_way_single where summary_id='"+list.get(0).getSummary_id()+"' ";
						this.jdbcPersistenceManager.execSql(delwaysql);
						
						 
					}
					
					fr.setResultCode("AE");
					fr.setResultDesc("支付失败");
				}
					
				}else{
					fr.setResultCode("AE");
					fr.setResultDesc("第三方返回数据异常");
				}
			} catch (Exception e) {
				fr.setResultCode("AE");
				fr.setResultDesc("调用地方支付接口出现异常");
			}
		}
		
		 
		
		
		return fr;
	}

	private FeeResultWxAndZfbBody getResdata(String res, String logname) {
		
		FeeResultWxAndZfbBody rf = new FeeResultWxAndZfbBody();
		
		 try {
			 InputStream is = new ByteArrayInputStream(res.getBytes("utf-8"));
			 SAXReader sax = new SAXReader();
			 Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			 
			 rf.setResultCode(document.selectSingleNode("res/resultCode").getText());
			 
			 if(document.selectSingleNode("res/resultCode").getText().equals("0")){
				
				 rf.setResultCode("AA");
				 rf.setResultDesc(document.selectSingleNode("res/resultDesc").getText());
				 rf.setOrderId(document.selectSingleNode("res/orderId").getText());
				 rf.setTradeNo(document.selectSingleNode("res/tradeNo").getText());
				 rf.setPayTime(document.selectSingleNode("res/payTime").getText());
				 rf.setPayAmout(document.selectSingleNode("res/payAmout").getText());
			 }else{
				 rf.setResultCode("AE");
				 rf.setResultDesc(document.selectSingleNode("res/resultDesc").getText());
				 rf.setOrderId("");
				 rf.setTradeNo("");
				 rf.setPayTime(DateUtil.getDateTime());
				 rf.setPayAmout("0");
			 }
			 
			
			 
		} catch (Exception e) {
		
			rf.setResultCode("AE");
			rf.setResultDesc("调用第三方支付出现错误");
		
			e.printStackTrace();
		}
		return rf;
	}
		
		
	}

