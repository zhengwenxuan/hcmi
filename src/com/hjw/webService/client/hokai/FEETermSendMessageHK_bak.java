package com.hjw.webService.client.hokai;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.hokai.bean.HisPriceListDTOHK;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.webService.client.hokai.bean.TeamAccBeanHK;
import com.hjw.webService.client.hokai.bean.TeamItemBeanHK;
import com.hjw.webService.client.huojianwa.bean.ComAccBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.DTO.HisPriceItemDTO;
import com.hjw.wst.service.CompanyService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Description: 发送团体缴费申请
 * @version V2.0.0.0
 */
public class FEETermSendMessageHK_bak {
	private String accnum="";
	private String personid="";
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private static CompanyService companyService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		companyService = (CompanyService) wac.getBean("companyService");
	}

	public FEETermSendMessageHK_bak(String personid,String accnum) {
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
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + ":"+this.accnum+":" + xml);
		try {
			getString(url,logname);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("");
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "Exception： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息解析返回值错误");
		}
		
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + ":" + xml);
		return rb;
	}
	
	private void getString(String url,String logname){
		ComAccBean cb= new ComAccBean();
		cb=this.getAcc_nums(accnum, logname);
        
		List<TeamAccBeanHK> tblist= new ArrayList<TeamAccBeanHK>();
		tblist= this.getAccnumList(this.accnum, logname);
		
		
		String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
		String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
		String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
		String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
	
		for(TeamAccBeanHK tb:tblist){
		ResultHeader rh= new ResultHeader();
		try{
		StringBuffer sb= new StringBuffer();
		String exam_num=tb.getExam_num();
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ei=getExamInfoForNum(exam_num,logname);
		sb.append("<POOR_IN200901UV xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\">\n");
		sb.append("  <!-- 消息流水号 -->\n");
		sb.append("  <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\" root=\"2.16.156.10011.0\"/>\n");
		sb.append("  <!-- 消息创建时间 -->\n");
		sb.append("  <creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>\n");
		sb.append("  <!-- 消息的服务标识-->\n");
		sb.append("  <interactionId extension=\"S0090\" root=\"2.16.840.1.113883.1.6\"/>\n");
		sb.append("  <!--处理代码，标识此消息是否是产品、训练、调试系统的一部分。 D：调试； P：产品； T：训练 -->\n");
		sb.append("  <processingCode code=\"P\"/>\n");
		sb.append("  <!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive); T(Current\n");
		sb.append("    processing) -->\n");
		sb.append("  <processingModeCode/>\n");
		sb.append("  <!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->\n");
		sb.append("  <acceptAckCode code=\"AL\"/>\n");
		sb.append("  <!-- 接受者 -->\n");
		sb.append("  <receiver typeCode=\"RCV\">\n");
		sb.append("    <device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("      <!-- 接受者 ID -->\n");
		sb.append("      <id>\n");
		sb.append("        <item extension=\"SYS001\" root=\"2.16.156.10011.0.1.1\"/>\n");
		sb.append("      </id>\n");
		sb.append("    </device>\n");
		sb.append("  </receiver>\n");
		sb.append("  <!-- 发送者 -->\n");
		sb.append("  <sender typeCode=\"SND\">\n");
		sb.append("    <device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("      <!-- 发送者 ID -->\n");
		sb.append("      <id>\n");
		sb.append("        <item extension=\"SYS009\" root=\"2.16.156.10011.0.1.2\"/>\n");
		sb.append("      </id>\n");
		sb.append("    </device>\n");
		sb.append("  </sender>\n");
		sb.append("  <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">\n");
		sb.append("    <subject typeCode=\"SUBJ\">\n");
		sb.append("      <placerGroup classCode=\"GROUPER\" moodCode=\"RQO\">\n");
		sb.append("        <!--体检信息-->\n");
		sb.append("        <componentOf1 contextConductionInd=\"false\">\n");
		sb.append("		    <!--PERSONAL=个检;GROUP=团检-->\n");
		sb.append("          <code code=\"GROUP\" codeSystem=\"2.16.840.1.113883.1.6\" displayName=\"团体结算\"/>\n");
		sb.append("          <encounter classCode=\"ENC\" moodCode=\"EVN\">\n");
		sb.append("            <!--费用标志-->\n");
		sb.append("            <feeFlag value=\"记账\"/>\n");
		sb.append("            <!--体检号码-->\n");
		sb.append("            <peId value=\""+exam_num+"\"/>\n");
		sb.append("            <!-- 体检批次,包含年份信息(1..1) -->\n");
		sb.append("            <peTimes value=\""+(ei.getCompany_id()+"-"+ei.getBatch_name())+"\"/>\n");
		sb.append("            <!--申请单号-->\n");
		sb.append("            <applyNo value=\""+cb.getAcc_num()+"\"/>\n");
		sb.append("            <!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->\n");
		sb.append("            <code code=\"3\" codeSystem=\"2.16.156.10011.2.3.1.271\">\n");
		sb.append("              <!-- 就诊类别名称(1..1) -->\n");
		sb.append("              <displayName value=\""+kddepname+"\"/>\n");
		sb.append("            </code>\n");
		sb.append("            <chargeInfos>\n");
		
		String SQKS = companyService.getDatadis("SQKS").get(0).getRemark();
        
        List<TeamItemBeanHK> tilist= new ArrayList<TeamItemBeanHK>();
        tilist=this.getAccItemList(tb, logname);
		for(TeamItemBeanHK fee:tilist){
			//jdbcQueryManager.equals(obj)
		ExaminfoChargingItemDTO chargingitem =  getexaminfochargingitme(exam_num,fee.getHis_num());
		List<HisPriceListDTOHK> listHisPrice = getHisPriceListDtoFindHisNum(fee.getHis_num());
		for (int i = 0; i < listHisPrice.size(); i++) {
			
			double discount = 0.0;
			if(chargingitem.getAmount() == 0.0 ){
				discount=0.0;
			} else {
				 discount =  chargingitem.getAmount()/(chargingitem.getItem_amount()*chargingitem.getItemnum());
			}
			double yuanjia = Integer.parseInt(listHisPrice.get(i).getAmount())*listHisPrice.get(i).getPrice()*chargingitem.getItemnum();
			double shishoujia =  yuanjia*discount;
					
			BigDecimal needPayyingshou = new BigDecimal(yuanjia);	
			BigDecimal needPaysshishou = new BigDecimal(shishoujia);	
			//BigDecimal realPay = new BigDecimal(price.getAmount()*price.getPrice()*fee.getDiscount()*0.1);
			//BigDecimal realPay = new BigDecimal(price.getAmount()*price.getPrice()*discount);//将折扣打到每一条价表上
			sb.append("            <!--收费信息(1..1) -->\n");
			sb.append("            <chargeInfo>\n");
			sb.append("              <!-- 项目编码 对应收费项目目录的编码-->\n");
			sb.append("              <chargeCategory value=\""+listHisPrice.get(i).getItem_code()+"\"/>\n");
			sb.append("              <!-- 收费项目名称 -->\n");
			sb.append("              <chargeDetails value=\""+listHisPrice.get(i).getItem_name()+"\"/>\n");
			sb.append("              <!-- 实收金额-->\n");
			sb.append("              <payNumber value=\""+needPaysshishou.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"\"/>\n");
			sb.append("              <!--应收金额-->\n");
			sb.append("              <receivable value=\""+needPayyingshou.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"\"/>\n");
			sb.append("              <!--数量-->\n");
			sb.append("              <count value=\""+listHisPrice.get(i).getAmount()+"\"/>\n");
			sb.append("              <!--标准价格-->\n");
			sb.append("              <standardUnit value=\""+listHisPrice.get(i).getPrice()+"\"/>\n");
			sb.append("              <!-- 项单位 对应收费细目的计算单位-->\n");
			sb.append("              <chargeUnit value=\"元\"/>\n");
			sb.append("            </chargeInfo>\n");
		}
		
	}
		sb.append("            </chargeInfos>\n");
		
		String sexcode="1";
		if("男".endsWith(ei.getSex())){
			sexcode="1";
		}else if("女".endsWith(ei.getSex())){
			sexcode="2";
		}
		sb.append("            <!--操作员工id和姓名-->\n");
		sb.append("            <operator code=\""+doctorid+"\" name=\""+doctorname+"\"/>\n");
		sb.append("            <!--操作时间 格式例如：20111129220000：-->\n");
		sb.append("            <operTime value=\""+DateTimeUtil.getDateTimes()+"\"/>\n");
		sb.append("            <!--开单科室编码、名称-->\n");
		sb.append("            <applyDept code=\""+kddepid+"\" name=\""+kddepname+"\"/>\n");
		sb.append("            <!--开单人编码、名称药品开单-->\n");
		sb.append("            <applyPerson code=\""+doctorid+"\" name=\""+doctorname+"\"/>\n");
		sb.append("            <!--执行科室编码、名称ORDERED_BY_DEPT-->\n");
		sb.append("            <executeDept code=\""+kddepid+"\" name=\""+kddepname+"\"/>\n");
		sb.append("            <bakText value=\"\"/>\n");
		sb.append("            <subject typeCode=\"SBJ\">\n");
		sb.append("              <patient classCode=\"PAT\">\n");
		sb.append("                <id>\n");
		sb.append("                  <!--体检号(0..1) -->\n");
		sb.append("                  <item extension=\""+exam_num+"\" root=\"2.16.156.10011.1.10\"/>\n");
		sb.append("                </id>\n");
		sb.append("                <!--患者角色状态-->\n");
		sb.append("                <statusCode code=\"active\"/>\n");
		sb.append("                <patientPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">\n");
		sb.append("                  <!--患者身份证号(1..1)-->\n");
		sb.append("                  <id extension=\""+ei.getId_num()+"\" root=\"2.16.156.10011.1.3\"/>\n");
		sb.append("                  <!--患者id(1..1)-->\n");
		sb.append("                  <patientId value=\""+ei.getPatient_id()+"\"/>\n");
		sb.append("                  <!--患者姓名(1..1) 团检传单位名称-->\n");
		sb.append("                  <name xsi:type=\"LIST_EN\">\n");
		sb.append("                    <item>\n");
		sb.append("                      <part value=\""+ei.getCompany()+"\"/>\n");
		sb.append("                    </item>\n");
		sb.append("                  </name>\n");
		sb.append("                  <!--性别-->\n");
		sb.append("                  <administrativeGenderCode code=\""+sexcode+"\" codeSystem=\"2.16.156.10011.2.3.3.4\"  codeSystemName=\"生理性别代码表（GB/T 2261.1）\">\n");
		sb.append("                    <displayName value=\""+ei.getSex()+"\"/>\n");
		sb.append("                  </administrativeGenderCode>\n");
		sb.append("                  <!--出生日期(0..1)-->\n");
		sb.append("                  <birthTime value=\""+ei.getBirthday()+"\"/>\n");
		sb.append("                  <!--年龄(0..1)-->\n");
		sb.append("                  <originalText value=\""+ei.getAge()+"\" displayName=\"岁\"/>\n");
		sb.append("                  <!-- 联系电话 (0..1)-->\n");
		sb.append("                  <telecom xsi:type=\"BAG_TEL\">\n");
		sb.append("                    <item value=\""+ei.getPhone()+"\"/>\n");
		sb.append("                  </telecom>\n");
		sb.append("                  <!--住址 (0..1)-->\n");
		sb.append("                  <addr xsi:type=\"BAG_AD\">\n");
		sb.append("                    <item use=\"H\">\n");
		sb.append("                      <part type=\"AL\" value=\"\"/>\n");
		sb.append("                    </item>\n");
		sb.append("                  </addr>\n");
		sb.append("                  <!--患者科室编码、名称-->\n");
		sb.append("                  <petientDept code=\""+kddepid+"\" name=\""+kddepname+"\"/>\n");
		sb.append("                </patientPerson>\n");
		sb.append("              </patient>\n");
		sb.append("            </subject>\n");
		sb.append("          </encounter>\n");
		sb.append("        </componentOf1>\n");
		sb.append("      </placerGroup>\n");
		sb.append("    </subject>\n");
		sb.append("  </controlActProcess>\n");
		sb.append("</POOR_IN200901UV>\n");
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		String result = HttpUtil.doPost_Xml(url,sb.toString(), "utf-8");
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
		if ((result != null) && (result.trim().length() > 0)) {
			result = result.trim();				
			rh = ResContralBeanHK.getRes(result);	
			String sql ="update examinfo_charging_item set his_req_status ='Y' where examinfo_id = '"+ei.getId()+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + sql.toString() + "\r\n");
			int update = this.jdbcQueryManager.getConnection().createStatement().executeUpdate(sql);
			if(update>0){
				rh.setTypeCode("AA");
				rh.setText("接口发送成功,修改数据库状态失败");
			}
		}else{
			rh.setTypeCode("AE");
			rh.setText("接口无返回");
		}
		}catch(Exception ex){
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		}
	}
		
	

		

		/**
		 * 
		 * @param url
		 * @param view_num
		 * @return
		 */
		public ExamInfoUserDTO getExamInfoForNum(String exam_num,String logname) throws ServiceException {
			StringBuffer sb = new StringBuffer();
		/*	sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
					+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id ");
			sb.append(" from exam_info c,customer_info a ");
			sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
			sb.append(" and c.exam_num = '" + exam_num + "' ");	*/
			
			sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
					+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id,c.company_id,c.company,b.batch_name ");
			sb.append(" from exam_info c,customer_info a, batch b ");//,company_info com
			sb.append(" where a.id=c.customer_id and c.batch_id = b.id and c.is_Active='Y' ");//and c.company_id = com.id		
			sb.append(" and c.exam_num = '" + exam_num + "' ");	
			
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			if((map!=null)&&(map.getList().size()>0)){
				eu= (ExamInfoUserDTO)map.getList().get(0);			
			}
			return eu;
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
	
	/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_id
	 * @param lis_req_no
	 * @param logname
	 * @return
	 */
	public List<TeamAccBeanHK> getAccnumList(String account_num,String logname){
		Connection tjtmpconnect = null;
		List<TeamAccBeanHK> tblist= new ArrayList<TeamAccBeanHK>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select distinct exam_num,acc_num from team_account_exam_list where acc_num in("
					+ "select distinct acc_num from team_invoice_account where account_num='"+account_num+"')";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs1.next()) {
				TeamAccBeanHK  tb= new TeamAccBeanHK();
				tb.setAcc_num(rs1.getString("acc_num"));
				tb.setExam_num(rs1.getString("exam_num"));
				tblist.add(tb);
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
		
		return tblist;
	}
	
	/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_id
	 * @param lis_req_no
	 * @param logname
	 * @return
	 */
	public List<TeamItemBeanHK> getAccItemList(TeamAccBeanHK tb,String logname){
		Connection tjtmpconnect = null;
		List<TeamItemBeanHK> tblist= new ArrayList<TeamItemBeanHK>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select ta.price,ta.acc_charge,ci.his_num,ci.item_name from team_account_item_list ta,"
					+ "charging_item ci where ta.acc_num='"+tb.getAcc_num()+"' and ta.exam_num='"+tb.getExam_num()+"' "
					+ "and ci.id=ta.charging_item_id";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs1.next()) {
				TeamItemBeanHK  tbi= new TeamItemBeanHK();
				tbi.setAcc_charge(rs1.getDouble("acc_charge"));
				tbi.setHis_num(rs1.getString("his_num"));
				tbi.setItem_name(rs1.getString("item_name"));
				tbi.setPrice(rs1.getDouble("price"));
				tblist.add(tbi);
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
		
		return tblist;
	}
	
	
	//根据hisnum(诊疗编码)获取  收费项目信息
	public List<HisPriceListDTOHK> getHisPriceListDtoFindHisNum(String his_num) {
		Connection tjtmpconnect = null;
		List<HisPriceListDTOHK> hisPricelist= new ArrayList<HisPriceListDTOHK>();
		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String  sql = "select hpl.item_code,hpl.item_name,hpl.price,prefer_price,hcipl.amount "
					+ " from his_clinic_item_v_price_list hcipl,his_clinic_item hci,his_price_list hpl "
					+ " where  hcipl.clinic_item_code=hci.item_code and hcipl.charge_item_code=hpl.item_code and hci.item_code='"+his_num+"'";
			//TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sql);
			while (rs1.next()) {
				HisPriceListDTOHK  his= new HisPriceListDTOHK();
				his.setItem_code(rs1.getString("item_code"));
				his.setItem_name(rs1.getString("item_name"));
				his.setPrice(rs1.getDouble("price"));
				his.setAmount(rs1.getString("amount"));//这里用于存储  数量
				hisPricelist.add(his);
				
			}
			
			rs1.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hisPricelist;
}
	public ExaminfoChargingItemDTO getexaminfochargingitme(String exam_num, String his_num) {
		Connection tjtmpconnect = null;
		ExaminfoChargingItemDTO chargingitem= new ExaminfoChargingItemDTO();
		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String  sql = " select eci.item_amount,eci.itemnum,eci.amount ,c.amount as chargingamount"
					+ " from exam_info e, charging_item c, examinfo_charging_item eci "
					+ " where eci.examinfo_id=e.id and eci.charge_item_id=c.id and c.isActive='Y' and eci.his_req_status='N' "
					+ " and c.his_num='"+his_num+"' and e.exam_num='"+exam_num+"'";
			//TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sql);
			while (rs1.next()) {
				chargingitem.setAmount(rs1.getDouble("amount"));
				chargingitem.setItem_amount(rs1.getDouble("chargingamount"));//此处用于放置his  诊疗价表
				chargingitem.setItemnum(rs1.getInt("itemnum"));
				//chargingitem.setChargingamount(rs1.getDouble("chargingamount"));
				
			}
			
			rs1.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return chargingitem;
	}
}
