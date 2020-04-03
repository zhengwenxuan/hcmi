package com.hjw.webService.client.liubaxian;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.FeeTermBean;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.huojianwa.bean.ComAccBean;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 发送团体缴费申请
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class FEETermSendMessageLBX {
	private String accnum="";
	private String personid="";
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public FEETermSendMessageLBX(String personid,String accnum) {
		this.accnum = accnum;
		this.personid=personid;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		Connection his_connect = null;
		Connection hjw_connect = null;
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + personid + ":"+this.accnum+":" + xml);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + ":" + url);
			if((personid!=null)&&(personid.trim().length()>30)){
				personid=personid.substring(0,30);
			}
			ComAccBean accnums = getAcc_nums(accnum,logname);
		
			hjw_connect = jdbcQueryManager.getConnection();
			hjw_connect.setAutoCommit(false);
			String sb1 = "select patID from Pat_Info where patID='" + accnum+ "' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = hjw_connect.createStatement().executeQuery(sb1);
			if (!rs1.next()) {
				String insertsql = "insert into Pat_Info(patID,vipid,patName,sex,birth,ptFlag,regDate,req_no,ChargeFlag) values('" 
						+ accnums.getAcc_num() + "','" + accnums.getBatch_num()+"-"+accnums.getCom_name()+ "','"
						+personid+"','','','T','"+DateTimeUtil.getDateTime()+"','"+accnum+"','0')";
						TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +insertsql);	
				hjw_connect.createStatement().execute(insertsql);
			}
			rs1.close();
			
			String delsql="delete from Pat_Charge_List where req_no='"+accnum+"' and chargeFlag='0'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +delsql);	
			hjw_connect.createStatement().execute(delsql);
			
					String sql2 = "select f.charging_item_id,f.item_num,CAST(f.acc_charge as decimal(18,2)) as acc_charge,CAST(f.item_amount as decimal(18,2)) as item_amount,c.item_code,c.his_num from "
							+ "( select l.charging_item_id, eci.item_amount,"
							+ "sum(eci.itemnum) as item_num, sum(l.acc_charge) as acc_charge "
							+ "from team_account_item_list l, examinfo_charging_item eci, exam_info e "
							+ "where l.exam_num=e.exam_num and eci.examinfo_id=e.id and eci.charge_item_id=l.charging_item_id and eci.isActive='Y' "
							+ "and l.acc_num = '"+accnums.getAcc_num()+"' and l.acc_charge>0 "
							+ "group by l.charging_item_id,eci.item_amount ) f,charging_item c where  c.id=f.charging_item_id ";
					TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sql2);
					List<FeeTermBean> eciList = this.jdbcQueryManager.getList(sql2,
							FeeTermBean.class);					
					double amount = 0.0; 
					double amt = 0.0; 
					for (FeeTermBean eci:eciList) {
						//long chargingid = eci.getCharging_item_id();
						//double prices = eci.getItem_amount();
						amount += eci.getAcc_charge();
						amt += eci.getAcc_charge();
					}
					String doctor= configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
					FeeTermBean eci = eciList.get(0);
					String insertsql = "insert into Pat_Charge_List(req_no,PatID,itemCode,hisItemCode,hisItemName,"
							+ "itemNum,oderDoctNo,oderDeptNo,deptNo,price,charge,rate,chargeFlag,readFlag) values('"+accnum+"','"+accnums.getAcc_num()+"','" +eci.getItem_code() + "','"
							+eci.getHis_num()+"','"+accnums.getCom_name()+"',1,'"+doctor+"','','',"+amount+","+amt+",10,'0','0')";
					TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +insertsql);	
					hjw_connect.createStatement().execute(insertsql);
					hjw_connect.commit();
					rb.getResultHeader().setTypeCode("AA");
					rb.getResultHeader().setText("");
					ReqId r= new ReqId();
					r.setReq_id(accnum);
					List<ReqId> list=new ArrayList<ReqId>();
					list.add(r);
					rb.getControlActProcess().setList(list);;
				} catch (Exception ex) {
					try{
					hjw_connect.rollback();
					}catch(Exception ed){}
					TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("费用信息解析返回值错误");
				}
		
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + ":" + xml);
		return rb;
	}

	/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_id
	 * @param lis_req_no
	 * @param logname
	 * @return
	 */
	public ComAccBean getAcc_nums(String account_num,String logname){
		Connection tjtmpconnect = null;
		ComAccBean accnums = new ComAccBean();
		List<String> list = new ArrayList<String>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select f.batch_num,f.com_name,b.acc_num from charging_summary_group a "
					+ "left join (select c.id,c.batch_num,c.invoice_title as com_name from batch c,company_info d where c.company_id=d.id) f on f.id=a.batch_id"
					+ ",team_invoice_account b  "
					+ "where a.account_num=b.account_num and a.account_num='"+account_num+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				accnums.setAcc_num(rs1.getString("acc_num"));
				accnums.setBatch_num(rs1.getString("batch_num"));
				accnums.setCom_name(rs1.getString("com_name"));
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		
		return accnums;
	}
}
