package com.hjw.webService.client.nanhua;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.FeeTermBean;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.huojianwa.bean.ComAccBean;
import com.hjw.webService.client.nanhua.bean.NHRequest;
import com.hjw.webService.client.nanhua.bean.NHResponse;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServices;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesLocator;
import com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_PortType;
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
public class FEETermSendMessageNH {
	private String accnum="";
	private String personid="";
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public FEETermSendMessageNH(String personid,String accnum) {
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
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + personid + ":"+this.accnum);
		try {
			try {
				if((personid!=null)&&(personid.trim().length()>30)){
					personid=personid.substring(0,30);
				}
				TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
				ComAccBean accnums = new ComAccBean();
				accnums=getAcc_nums(accnum,logname);
				
				String sql = "select f.charging_item_id,f.item_num,CAST(f.acc_charge as decimal(18,2)) as acc_charge,CAST(f.item_amount as decimal(18,2)) as item_amount,c.item_code,c.his_num from "
						+ "( select l.charging_item_id, eci.item_amount,"
						+ "sum(eci.itemnum) as item_num, sum(l.acc_charge) as acc_charge "
						+ "from team_account_item_list l, examinfo_charging_item eci, exam_info e "
						+ "where l.exam_num=e.exam_num and eci.examinfo_id=e.id and eci.charge_item_id=l.charging_item_id and eci.isActive='Y' "
						+ "and l.acc_num = '"+accnums.getAcc_num()+"' and l.acc_charge>0 "
						+ "group by l.charging_item_id,eci.item_amount ) f,charging_item c where  c.id=f.charging_item_id ";
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sql);
				List<FeeTermBean> eciList = this.jdbcQueryManager.getList(sql, FeeTermBean.class);					
				double amount = 0.0; 
				for (FeeTermBean eci:eciList) {
					amount += eci.getAcc_charge();
				}
				
				NHRequest request = new NHRequest();
				request.getData().setTjno(accnum);
				request.getData().setName(accnums.getCom_name());
				request.getData().setZje(""+amount);
				String strRequest = JaxbUtil.convertToXml(request, true);
				TranLogTxt.liswriteEror_to_txt(logname, "传入参数:" + strRequest);
				
				GWI_TJJKServices gwiServices = new GWI_TJJKServicesLocator(url);
				GWI_TJJKServicesSoap_PortType gwi = gwiServices.getGWI_TJJKServicesSoap();
				String messages = gwi.doBlance(strRequest);
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+this.accnum+":"+messages);
				try {
					NHResponse response = JaxbUtil.converyToJavaBean(messages, NHResponse.class);
					if("0".equals(response.getHead().getResultCode())) {
						String hisno = response.getInfo().get(0).getHisno();
						ReqId req= new ReqId();
						req.setReq_id(this.accnum);
						req.setThird_req_id(hisno);
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("发送收费申请成功！");
						rb.getControlActProcess().getList().add(req);
					} else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(response.getHead().getErrorMsg());
					}
				} catch (Exception ex) {
					TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("费用信息解析返回值错误");
				}
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("费用信息调用webservice错误");
			}
		} catch (Throwable ex){
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
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
					+ "left join (select c.id,c.batch_num,d.com_name from batch c,company_info d where c.company_id=d.id) f on f.id=a.batch_id"
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
