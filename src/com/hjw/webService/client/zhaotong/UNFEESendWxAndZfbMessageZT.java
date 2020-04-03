package com.hjw.webService.client.zhaotong;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.stable.core.UUID;
import com.hjw.interfaces.util.DateUtil;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.FeeResultWxAndZfbBody;
import com.hjw.webService.client.body.FeeWxAndZfbMessage;
import com.hjw.wst.DTO.ExamInfoDTO;
import com.hjw.wst.DTO.PosDailySummaryDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

public class UNFEESendWxAndZfbMessageZT {

	
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

	public UNFEESendWxAndZfbMessageZT(FeeWxAndZfbMessage feeMessage) {
		this.feeMessage = feeMessage;
	}
	
	public FeeResultWxAndZfbBody getMessage(String url, String logname) {
		
		FeeResultWxAndZfbBody fr = new FeeResultWxAndZfbBody();
		String exam_num = feeMessage.getExam_num();
		ExamInfoDTO ei = customerInfoService.getExamInfoForexamNum(exam_num);
		
		
		if(feeMessage.getAcctype().equals("TF")){//收费成功 正常 退费
			PosDailySummaryDTO  PosDaily = 	getPosDetailForReqNum(feeMessage.getReq_nums(),logname);
			
			
			 double  number=PosDaily.getPos_charge_amount()*100;
			 String numberStr;
		        if (((int) number * 1000) == (int) (number * 1000)) {
		            //如果是一个整数
		            numberStr = String.valueOf((int) number);
		        } else {
		            DecimalFormat df = new DecimalFormat("######0.00");
		            numberStr = df.format(number);
		        }
			StringBuffer sb = new StringBuffer();
			sb.append(" <?xml version=\"1.0\" encoding=\"UTF-8\"?>                 ");
			sb.append(" <req>                                                  ");
			sb.append(" <orderId>"+PosDaily.getOriginal_voucher_no()+"</orderId>                      ");
			sb.append(" <notifyUrl></notifyUrl>                                ");
			sb.append(" <refundTime>"+DateUtil.getDateTime()+"</refundTime>           ");
			sb.append(" <refundFee>"+numberStr+"</refundFee>                            ");
			sb.append(" <reason>住院预缴金退还</reason>                       ");
			sb.append(" <refundNo>"+feeMessage.getReq_nums()+"</refundNo>                    ");
			/*sb.append(" <medicareSettleLogId>                                  ");
			sb.append(" </medicareSettleLogId>                                 ");*/
			
			
			String empower_code = PosDaily.getEmpower_code();
			String payMode;
			if(empower_code.substring(0, 2).equals("28")){//支付宝9901
				payMode="9901";
			}else{
				payMode="9801";
			}
			
			sb.append(" <payMode>"+payMode+"</payMode>                       ");
			sb.append(" </req>                                                 ");
			
			TranLogTxt.liswriteEror_to_txt(logname, "退费申请入参" + sb.toString() + "\r\n");
			try {
				String res = HttpUtil.doPost_Str(url, sb.toString(), "utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "退费申请返回" + res + "\r\n");
				if(res.length()>10 && !res.equals("") && res !=null){
					
				fr = getResTuiFeidata(res,logname);
				if(fr.getResultCode().equals("AA")){
					
					String addsql = "insert  into  pos_detail(pay_way , peis_trade_code , amount , pos_type ,daily_status,trans_code) "
							    + "values('"+PosDaily.getPay_way()+"','"+feeMessage.getReq_nums()+"','"+PosDaily.getAmount()+"',"+PosDaily.getPos_type()+",'0','02')";
					TranLogTxt.liswriteEror_to_txt(logname, "退费申请返回插入 pos_detail==" + addsql + "\r\n");
					 this.jdbcPersistenceManager.execSql(addsql);
					
					String REC_ID = UUID.randomUUID().toString().replaceAll("-", "");
					
					String pos_trans_list="insert into pos_trans_list (is_valid,REC_ID,pos_code,pay_class,peis_trade_code,op_code,app_type,trans_code,original_trade_date,original_trade_no,original_voucher_no,trade_no,voucher_no,trade_amount,amount,trade_date,trade_time,empower_code,create_time,UserId)  "
							+ "values ('0','"+REC_ID+"','01','01','"+feeMessage.getReq_nums()+"','1','1','02','"+DateUtil.getDateTime()+"','"+PosDaily.getOriginal_trade_no()+"','"+PosDaily.getOriginal_voucher_no()+"','"+PosDaily.getOriginal_trade_no()+"','"+PosDaily.getOriginal_voucher_no()+"','"+PosDaily.getAmount()+"',"
							+ "'"+PosDaily.getAmount()+"','"+DateUtil.getDateTime()+"','"+DateUtil.getDateTime()+"','"+PosDaily.getEmpower_code()+"','"+DateUtil.getDateTime()+"','"+feeMessage.getUserid()+"')";
					TranLogTxt.liswriteEror_to_txt(logname, "退费申请返回插入 pos_trans_list==" + pos_trans_list + "\r\n");
					this.jdbcPersistenceManager.execSql(pos_trans_list);
					fr.setResultCode("AA");
					fr.setResultDesc("退费成功");
				
				}else{
					String addsql = "insert  into  pos_detail(pay_way , peis_trade_code , amount , pos_type ,daily_status,trans_code) "
						    + "values('"+PosDaily.getPay_way()+"','"+feeMessage.getReq_nums()+"','"+PosDaily.getAmount()+"',"+PosDaily.getPos_type()+",'0','02')";
					TranLogTxt.liswriteEror_to_txt(logname, "退费申请返回插入 pos_detail==" + addsql + "\r\n");
					 this.jdbcPersistenceManager.execSql(addsql);
					
					String REC_ID = UUID.randomUUID().toString().replaceAll("-", "");
					
					String pos_trans_list="insert into pos_trans_list (is_valid,REC_ID,pos_code,pay_class,peis_trade_code,op_code,app_type,trans_code,original_trade_date,original_trade_no,original_voucher_no,trade_no,voucher_no,trade_amount,amount,trade_date,trade_time,empower_code,create_time,UserId)  "
							+ "values ('-1','"+REC_ID+"','01','01','"+feeMessage.getReq_nums()+"','1','1','02','"+DateUtil.getDateTime()+"','"+PosDaily.getOriginal_trade_no()+"','"+PosDaily.getOriginal_voucher_no()+"','"+PosDaily.getOriginal_trade_no()+"','"+PosDaily.getOriginal_voucher_no()+"','"+PosDaily.getAmount()+"',"
							+ "'"+PosDaily.getAmount()+"','"+DateUtil.getDateTime()+"','"+DateUtil.getDateTime()+"','"+PosDaily.getEmpower_code()+"','"+DateUtil.getDateTime()+"','"+feeMessage.getUserid()+"')";
					TranLogTxt.liswriteEror_to_txt(logname, "退费申请返回插入 pos_trans_list==" + pos_trans_list + "\r\n");
					this.jdbcPersistenceManager.execSql(pos_trans_list);
					fr.setResultCode("AE");
					fr.setResultDesc("退费失败");
				}
					
				}else{
					fr.setResultCode("AE");
					fr.setResultDesc("第三方返回数据异常");
				}
			} catch (Exception e) {
				fr.setResultCode("AE");
				fr.setResultDesc("调用第三方支付接口出现异常");
			}
		}else{//  对账退费
			 double  number=feeMessage.getAmount2()*100;
			 String numberStr;
		        if (((int) number * 1000) == (int) (number * 1000)) {
		            //如果是一个整数
		            numberStr = String.valueOf((int) number);
		        } else {
		            DecimalFormat df = new DecimalFormat("######0.00");
		            numberStr = df.format(number);
		        }
			StringBuffer sb = new StringBuffer();
			sb.append(" <?xml version=\"1.0\" encoding=\"UTF-8\"?>                 ");
			sb.append(" <req>                                                  ");
			sb.append(" <orderId>"+feeMessage.getOriginal_voucher_no()+"</orderId>                      ");
			sb.append(" <notifyUrl></notifyUrl>                                ");
			sb.append(" <refundTime>"+DateUtil.getDateTime()+"</refundTime>           ");
			sb.append(" <refundFee>"+numberStr+"</refundFee>                            ");
			sb.append(" <reason>住院预缴金退还</reason>                       ");
			sb.append(" <refundNo>"+feeMessage.getReq_nums()+"</refundNo>                    ");
			/*sb.append(" <medicareSettleLogId>                                  ");
			sb.append(" </medicareSettleLogId>                                 ");*/
			
			
			String empower_code = feeMessage.getOriginal_voucher_no();
			String payMode;
			if(empower_code.substring(0, 1).equals("a")){//支付宝9901
				payMode="9901";
			}else{
				payMode="9801";
			}
			
			sb.append(" <payMode>"+payMode+"</payMode>                       ");
			sb.append(" </req>                                                 ");
		
			TranLogTxt.liswriteEror_to_txt(logname, "对账退费申请入参" + sb.toString() + "\r\n");
			try {
				
				
				String res = HttpUtil.doPost_Str(url, sb.toString(), "utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "退费申请返回" + res + "\r\n");
				if(res.length()>10 && !res.equals("") && res !=null){
					
				fr = getResTuiFeidata(res,logname);
				
				if(fr.getResultCode().equals("AA")){
					
					
					String REC_ID = UUID.randomUUID().toString().replaceAll("-", "");
					
					String pos_trans_list="insert into tuifei_trans_list (is_valid,REC_ID,pos_code,pay_class,peis_trade_code,op_code,app_type,trans_code,original_trade_date,original_trade_no,original_voucher_no,trade_no,voucher_no,trade_amount,amount,trade_date,trade_time,empower_code,create_time,UserId)  "
							+ "values ('0','"+REC_ID+"','01','01','','1','1','02','"+DateUtil.getDateTime()+"','"+feeMessage.getOriginal_trade_no()+"','"+feeMessage.getOriginal_voucher_no()+"','"+feeMessage.getOriginal_trade_no()+"','"+feeMessage.getOriginal_voucher_no()+"','"+feeMessage.getAmount1()+"',"
							+ "'"+feeMessage.getAmount2()+"','"+DateUtil.DateAdd2(1)+"','"+DateUtil.DateAdd2(1)+"','','"+DateUtil.DateAdd2(1)+"','')";
					TranLogTxt.liswriteEror_to_txt(logname, "对账退费申请返回插入 tuifei_trans_list==" + pos_trans_list + "\r\n");
					this.jdbcPersistenceManager.execSql(pos_trans_list);
					
					
					String updatejd = " update jd_wxzfb_accouns set trans_status='1',trans_time='"+DateUtil.getDateTime()+"'  where order_id='"+feeMessage.getOriginal_voucher_no()+"' and trade_no='"+feeMessage.getOriginal_trade_no()+"' and trans_status !='1'  ";
					this.jdbcPersistenceManager.execSql(updatejd);
					
					fr.setResultCode("AA");
					fr.setResultDesc("退费成功");
				
				}else{
					
					
					String REC_ID = UUID.randomUUID().toString().replaceAll("-", "");
					
					String pos_trans_list="insert into tuifei_trans_list (is_valid,REC_ID,pos_code,pay_class,peis_trade_code,op_code,app_type,trans_code,original_trade_date,original_trade_no,original_voucher_no,trade_no,voucher_no,trade_amount,amount,trade_date,trade_time,empower_code,create_time,UserId)  "
							+ "values ('-1','"+REC_ID+"','01','01','','1','1','02','"+DateUtil.getDateTime()+"','"+feeMessage.getOriginal_trade_no()+"','"+feeMessage.getOriginal_voucher_no()+"','"+feeMessage.getOriginal_trade_no()+"','"+feeMessage.getOriginal_voucher_no()+"','"+feeMessage.getAmount1()+"',"
							+ "'"+feeMessage.getAmount2()+"','"+DateUtil.DateAdd2(1)+"','"+DateUtil.DateAdd2(1)+"','','"+DateUtil.DateAdd2(1)+"','')";
					TranLogTxt.liswriteEror_to_txt(logname, "对账退费申请返回插入 tuifei_trans_list==" + pos_trans_list + "\r\n");
					this.jdbcPersistenceManager.execSql(pos_trans_list);
					fr.setResultCode("AE");
					fr.setResultDesc("退费失败");
				}
					
				}else{
					fr.setResultCode("AE");
					fr.setResultDesc("第三方返回数据异常");
				}
				
				
			} catch (Exception e) {
				fr.setResultCode("AE");
				fr.setResultDesc("对账出现异常错误");
			}
		
		}
		
		return fr;
	}

	private PosDailySummaryDTO getPosDetailForReqNum(String req_nums,String logname) {
		PosDailySummaryDTO	PosDailydto =	new PosDailySummaryDTO();
		String sql =  " select  pd.daily_status,pd.pay_way,pd.pos_type,pd.peis_trade_code ,pd.amount as pos_charge_amount,ptl.amount,ptl.trans_code,ptl.original_trade_no,ptl.original_voucher_no,ptl.empower_code from  pos_detail  pd ,pos_trans_list ptl  "
				+ " where pd.peis_trade_code = ptl.peis_trade_code  and pd.trans_code = ptl.trans_code  and pd.peis_trade_code='"+req_nums+"' and pd.trans_code='00' and is_valid='0' ";
		TranLogTxt.liswriteEror_to_txt(logname, "退费申请查询 数据" + sql + "\r\n");
		List<PosDailySummaryDTO> list = this.jdbcQueryManager.getList(sql, PosDailySummaryDTO.class);
		
		if(list !=null ){
			PosDailydto=list.get(0);
		}
		return PosDailydto;
	}

	private FeeResultWxAndZfbBody getResTuiFeidata(String res, String logname) {
		
		FeeResultWxAndZfbBody rf = new FeeResultWxAndZfbBody();
		
		 try {
			 InputStream is = new ByteArrayInputStream(res.getBytes("utf-8"));
			 SAXReader sax = new SAXReader();
			 Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			 
			 rf.setResultCode(document.selectSingleNode("res/resultCode").getText());
			 
			 if(document.selectSingleNode("res/resultCode").getText().equals("0")){
				
				 rf.setResultCode("AA");
				 rf.setResultDesc(document.selectSingleNode("res/resultDesc").getText());
				
			 }else{
				 rf.setResultCode("AE");
			 }
			 
			
			 
		} catch (Exception e) {
		
			rf.setResultCode("AE");
			rf.setResultDesc("调用第三方支付出现错误");
		
			e.printStackTrace();
		}
		return rf;
	}
		
		
	}

