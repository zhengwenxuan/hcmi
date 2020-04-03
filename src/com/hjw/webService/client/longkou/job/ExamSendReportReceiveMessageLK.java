package com.hjw.webService.client.longkou.job;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hsqldb.lib.StringUtil;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.SendReportReceive;
import com.hjw.webService.client.longkou.bean.ExamDetailBean;
import com.hjw.webService.client.longkou.bean.ExamInfoUserLKBean;
import com.hjw.webService.client.longkou.bean.ExamResDEPBean;
import com.hjw.webService.client.longkou.bean.ExamSummaryLKBean;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class ExamSendReportReceiveMessageLK {
	private static String KLX="3";//	卡类型	字符串	16	必填	复合主键；见说明(1)SD3.1.006 填写默认3
	private static String YLJGDM="49365151300";//医疗机构代码	字符串	22	必填	复合主键   需要医院提供
	private static String YLJGMC="龙口市人民医院";
	private static String FKDQ="370681";
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
	}
	
	public void getMessage(String url,int days, String logname) {
		String sdatetime = DateTimeUtil.DateDiff2(days+1);
		String edatetime = DateTimeUtil.DateDiff2(days);
		Connection peis_connect = null;
		Statement statement = null;
		List<SendReportReceive> list = new ArrayList<SendReportReceive>();
		try {
			peis_connect = jdbcQueryManager.getConnection();
			StringBuffer sql1 = new StringBuffer("SELECT TOP 10 a.id,a.exam_doctor_id,a.exam_info_id,a.final_exam_result,a.result_Y,a.result_D,a.suggest,"
					+ "a.center_num,a.check_doc,a.check_time,a.approve_status,a.creater,a.create_time,a.updater,a.update_time,"
					+ "a.acceptance_check,a.acceptance_doctor,a.acceptance_date,a.read_status,a.read_time,b.exam_num,"
					+ "x.chi_name as examdocname,y.chi_name as checkdocname" 
					+ " FROM exam_info b,exam_summary a "
					+ " left join user_usr x on x.id=a.exam_doctor_id" 
					+ " left join user_usr y on y.id=a.check_doc "
					+ "where a.read_status='0' and a.exam_info_id=b.id and a.approve_status ='A' "
					+ "and CONVERT(varchar(50),b.join_date,23)>='"+sdatetime+"' "
					+ "and CONVERT(varchar(50),b.join_date,23)<='"+edatetime+"' ");
				TranLogTxt.liswriteEror_to_txt(logname, "===sql1.toString()===" + sql1.toString()+"\r\n");
				List<ExamSummaryLKBean> userList = this.jdbcQueryManager.getList(sql1.toString(), ExamSummaryLKBean.class);		
				TranLogTxt.liswriteEror_to_txt(logname, "==userList长度==" + userList.size()+"\r\n");
				for (ExamSummaryLKBean es:userList) {
					boolean flag1=TB_YL_Patient_Information(url,es,logname);
					TranLogTxt.liswriteEror_to_txt(logname, "===flag1标识===" +flag1);
					if(flag1){
						String sql = "update exam_summary set read_status='1',read_time='" + DateTimeUtil.getDateTime()
						+ "' where id ='" + es.getId() + "' ";
				        this.jdbcPersistenceManager.executeSql(sql);
				        TranLogTxt.liswriteEror_to_txt(logname, "===sql 更新状态标识===" +sql);
					}
				}
				
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname, "===com.hjw.interfaces.util.StringUtil.formatException(e)===" +com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			try {
				if (statement != null){
					statement.close();
				}
				if (peis_connect != null) {
					peis_connect.close();
				}
				
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		for(SendReportReceive s:list){
			JSONObject json = JSONObject.fromObject(list);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "===str===" + str+"===="+url.trim());
			String result = HttpUtil.doPost(url.trim(),s,"utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "====result===" + result+"\r\n");
		}
	}
	
	//1.1. 体检基本信息表(TB_YL_Patient_Information)
	private boolean TB_YL_Patient_Information(String url,ExamSummaryLKBean es,String logname){
		
		ExamInfoUserLKBean ei = getCustomer(es.getExam_info_id(),logname);
		if(ei==null || ei.getId()<=0){
			return false;
		}else{
			String KH=ei.getArch_num(); //卡号		字符串	32	必填	复合主键  档案号
			//String KLX="3";//	卡类型	字符串	16	必填	复合主键；见说明(1)SD3.1.006 填写默认3
			//String YLJGDM="";//医疗机构代码	字符串	22	必填	复合主键   需要医院提供
			//String 	FKDQ="370681";//	发卡地区字符串	6	必填	指社保卡发卡地区，上传行政区划编码
			String ZJHM=ei.getId_num();//	证件号码字符串	32	必填	见说明（2），社保卡患者必填
			String ZJLX="01";//	证件类型字符串	2	必填	见说明（3），CV02.01.101身份证件类别代码
			if(StringUtil.isEmpty(ZJHM)){
				ZJHM="000000000000000000";//	证件号码字符串	32	必填	见说明（2），社保卡患者必填
				ZJLX="99";//	证件类型字符串	2	必填	见说明（3），CV02.01.101身份证件类别代码
			}
			String XB=ei.getSex(); //性别	字符串	1	必填	见说明（4）性别代码（GB/T2261.1-2003）
			if(XB==null){
				XB="0";
			}else if(XB.trim().length()<=0){
				XB="0";
			}else if("男".equals(XB)){
				XB="1";
			}else if("女".equals(XB)){
				XB="2";
			}
			String XM=ei.getUser_name(); //姓名	字符串	64	必填	
			String HZLX="5";//	患者类型字符串	1	可选	编码。1：本市；2：外地；3：境外（港澳台）；4：外国；5：未知 SD3.1.020
			String HYZK=ei.getIs_marriage();// 婚姻状况		字符串	2	可选	见说明（5）婚姻状况（GB/ T2261.2-2003）
			if(StringUtil.isEmpty(HYZK)){
				HYZK="90";
			}else if(HYZK.indexOf("已")>=0){
				HYZK="20";
			}else if(HYZK.indexOf("未")>=0){
				HYZK="10";
			}
			Date date=DateTimeUtil.parse(ei.getBirthday());
			String CSRQ=DateTimeUtil.shortFmt4(date); //出生日期	CSRQ	字符串	8	可选	格式：YYYYMMDD
			//出生地	CSD	字符串	32	可选	编码。按国标GB/T2260-2007执行。
			//民族	MZ	字符串	5	可选	见说明（6）民族GB3304-1991
			//国籍	GJ	字符串	10	可选	见说明（7）国家代码GB/T 2659-2000			
			String DHHM=ei.getPhone(); //电话号码	DHHM	字符串	24	可选	社保卡患者必填写
			String SJHM =ei.getPhone(); //手机号码	SJHM	字符串	20	可选	
			//工作单位邮编	GZDWYB	字符串	6	可选	
			//工作单位名称	GZDWMC	字符串	128	可选	
			//工作单位地址	GZDWDZ	字符串	128	可选	
			String JZDZ = ei.getAddress(); //居住地址	JZDZ	字符串	128	可选	社保卡患者必填写			
			//户口地址	HKDZ	字符串	128	可选	
			//户口地址邮编	HKDZYB	字符串	6	可选	
			//联系人姓名	LXRXM	字符串	32	可选	
			//联系人关系	LXRGX	字符串	8	可选	见说明（8）
			//联系人地址	LXRDZ	字符串	128	可选	
			//联系人邮编	LXRYB	字符串	6	可选	
			//联系人电话	LXRDH	字符串	24	可选	
			//密级	MJ	字符串	16	可选	见说明（9）
			//修改标志	XGBZ	数字	1	必填	编码。0：正常、1：撤销；SD3.1.011 	见说明（10）
			//数据生成时间	YWSCSJ	DATETIME		可选	业务操作获取该患者信息的时间
			//医院内部档案号	YYDAH	字符串	64	可选	见说明（11）
			//预留一	YLYL1	字符串	128	可选	为系统处理该数据而预留
			//预留二	YLYL2	字符串	128	可选	为系统处理该数据而预留
		Connection connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			
			TranLogTxt.liswriteEror_to_txt(logname, "==dburl=="+dburl+"===user=="+user);
			
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + es.getExam_info_id() + ":" + url);
			String sql="delete from TB_YL_Patient_Information where KLX='"+KLX+"' and YLJGDM='"+YLJGDM+"' and KH='"+KH+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "==deleteSql==" + sql);
			connect.createStatement().executeUpdate(sql);
			sql ="insert into TB_YL_Patient_Information(KH,KLX,YLJGDM,FKDQ,ZJHM,ZJLX,XB,XM,HZLX,"
					+ "HYZK,CSRQ,DHHM,SJHM,JZDZ,YWSCSJ) values('"+KH+"','"+KLX+"','"+YLJGDM+"','"+FKDQ+"','"+ZJHM+"','"
					+ZJLX+"','"+XB+"','"+XM+"','"+HZLX+"','"+HYZK+"','"+CSRQ+"','"+DHHM+"','"+SJHM+"','"+JZDZ+"',sysdate)";
			TranLogTxt.liswriteEror_to_txt(logname, "==insertSql==" + sql);
			connect.createStatement().executeUpdate(sql);
			
			boolean flag1=TB_YL_TJBGSY(url,es,ei,logname);
			boolean flag2=getTB_YL_TJBG(url,ei.getId(),ei,logname);
			boolean flag3=getTB_YL_TJMX(url,ei.getId(),ei,logname);
			TranLogTxt.liswriteEror_to_txt(logname, "==标识333==" +flag1+"===="+flag2+"===="+flag3); 
			return flag1&&flag2&&flag3;
			
			
		}catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "-------com.hjw.interfaces.util.StringUtil.formatException(ex)11------" +com.hjw.interfaces.util.StringUtil.formatException(ex));
			return false;
			} finally {
				try {
					if (connect != null) {
						OracleDatabaseSource.close(connect);
					}
				} catch (Exception sqle4) {
					sqle4.printStackTrace();
				}
			}	
		
		
		}
	}
	
	/**
	 * 
	 */
	private ExamInfoUserLKBean getCustomer(long examid,String logname){
		ExamInfoUserLKBean ei = new ExamInfoUserLKBean();
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,a.id as customer_id,a.nation,a.idtype,a.user_name,a.arch_num,a.id_num,c.patient_id,"
				+ "a.sex,a.membership_card,c.age,c.exam_num,c.is_marriage,c.exam_type,c.is_need_guide,c.is_need_barcode,"
				+ "c.position,c._level,c.group_id,c.email,c.chargingType,c.remarke,"
				+ "c.others,c.status,c.phone,c.customer_type,c.customer_type_id,c.customerType,a.address");
		sb.append(",c.group_id,c.group_index,a.birthday,c._level,c.status,c.register_date"
				+ ",c.join_date,c.exam_times,c.exam_num,c.past_medical_history,"
				+ "c.picture_Path,c.is_after_pay,c.batch_id,n.batch_name,c.is_Active,"
				+ "c.employeeID,c.patient_id,c.mc_no,c.visit_date,c.visit_no,c.clinic_no,c.exam_indicator,c.batch_id,"
				+ "c.company_id,m.group_name,c.freeze,c.company,c.budget_amount,c.introducer,c.is_marriage ");
		sb.append(", c.getReportWay ");
		sb.append(" from customer_info a ,exam_info c ");
		 sb.append(" left join batch n on n.id=c.batch_id ");
		 sb.append(" left join group_info m on m.id=c.group_id ");
		 sb.append(" left join exam_ext_info eei on c.exam_num=eei.exam_num ");
		sb.append(" where c.customer_id=a.id ");
		sb.append(" and c.is_Active='Y' ");
		sb.append(" and a.is_Active='Y' ");
		sb.append(" and c.id=" + examid + " ");
//		System.out.println(sb.toString());
		
		TranLogTxt.liswriteEror_to_txt(logname, "-------sb.toString()===SQL------" +sb.toString());
		
		List map = this.jdbcQueryManager.getList(sb.toString(), ExamInfoUserLKBean.class);
		if ((map != null) && (map.size() > 0)) {
			ei = (ExamInfoUserLKBean) map.get(0);
		}
		return ei;
	}
	
	private boolean TB_YL_TJBGSY(String url,ExamSummaryLKBean es,ExamInfoUserLKBean ei,String logname){
		try{
		//医疗机构代码	YLJGDM	字符串	22	必填	复合主键；填写本院的机构代码
		String TJBH=es.getExam_num();//体检编号	TJBH	字符串	50	必填	复合主键；对某人一次完整体检过程的唯一标识编号
		//医疗机构名称	YLJGMC	字符串	70	必填	
		String KH=ei.getArch_num();// 卡号		字符串	32	必填	患者识别码，与患者信息表进行关联，公卫健康体检可填写健康档案编号。
		//卡类型	KLX	字符串	16	必填	
		String TJMDDM="999"; //体检目的代码	TJMDDM	字符	1	必填	见说明（1）
		if("41".equals(ei.getCustomer_type())&&"T".equals(ei.getExam_indicator())){ //其他团体健康体检
			TJMDDM="404";
		}else if("41".equals(ei.getCustomer_type())&&"G".equals(ei.getExam_indicator())){ //个人健康体检
			TJMDDM="401";
		}else if("42".equals(ei.getCustomer_type())){ //个人健康体检
			TJMDDM="102";
		}
		String XM=ei.getUser_name();//	姓名 字符串	50	必填	
		String XB=ei.getSex(); //性别	字符串	1	必填	见说明（4）性别代码（GB/T2261.1-2003）
		if(XB==null){
			XB="0";
		}else if(XB.trim().length()<=0){
			XB="0";
		}else if("男".equals(XB)){
			XB="1";
		}else if("女".equals(XB)){
			XB="2";
		}
		
		String NL=ei.getAge()+"";//年龄	NL	字符串	10	必填			
		Date date=DateTimeUtil.parse(ei.getBirthday());
		String CSRQ=DateTimeUtil.shortFmt4(date); //出生日期	CSRQ	日期		必填	
		String ZJHM=ei.getId_num();//	证件号码字符串	32	必填	见说明（2），社保卡患者必填
		String ZJLX="01";//	证件类型字符串	2	必填	见说明（3），CV02.01.101身份证件类别代码
		if(StringUtil.isEmpty(ZJHM)){
			ZJHM="000000000000000000";//	证件号码字符串	32	必填	见说明（2），社保卡患者必填
			ZJLX="99";//	证件类型字符串	2	必填	见说明（3），CV02.01.101身份证件类别代码
		}
		String HYZK=ei.getIs_marriage();// 婚姻状况		字符串	2	可选	见说明（5）婚姻状况（GB/ T2261.2-2003）
		if(StringUtil.isEmpty(HYZK)){
			HYZK="90";
		}else if(HYZK.indexOf("已")>=0){
			HYZK="20";
		}else if(HYZK.indexOf("未")>=0){
			HYZK="10";
		}
		
		String ZYMC="未知";//职业名称	ZYMC	字符串	100	必填	职业名称
		String ZYLBDM="1-69";//职业类别代码	ZYLBDM	字符串	10	必填	GB/T6565-2009代码
		
		String SG=getcustomComm(es.getExam_info_id(),14,logname);//身高	SG	数字	3		单位：cm  该项来自山东省平台标准
		String TZ=getcustomComm(es.getExam_info_id(),15,logname);//体重	TZ	数字	5,2		单位：kg 该项来自山东省平台标准
		String SZY=getcustomComm(es.getExam_info_id(),18,logname);//舒张压	SZY	数字	3		该项来自山东省平台标准
		String SSY=getcustomComm(es.getExam_info_id(),17,logname);//收缩压	SSY	数字	3		该项来自山东省平台标准
		String[] dis = getdisease(es.getExam_info_id(),logname);
		String ZZDMJKJC =dis[0];//症状代码(健康检查)	ZZDMJKJC	字符串	40	必填	ICD-10 R代码，多选用“；”分隔
		ZZDMJKJC = ZZDMJKJC.replace(" ", "");
		if("".equals(ZZDMJKJC) || ZZDMJKJC==null){
			ZZDMJKJC = "-";
		}
		TranLogTxt.liswriteEror_to_txt(logname, "===ZZDMJKJC去除空格11===" +ZZDMJKJC);
		//案例代码
		ZZDMJKJC = "A00.000;A00.100;A00.900";//这一行注释掉
		TranLogTxt.liswriteEror_to_txt(logname, "===ZZDMJKJC案例Demo===" +ZZDMJKJC);
		String ZZMCJKJC =dis[1];//ICD-10 症状名称(健康检查)	ZZMCJKJC	字符串	100	必填
		TranLogTxt.liswriteEror_to_txt(logname, "===ZZMCJKJC名称Demo===" +ZZMCJKJC);
		String ZRYSXM = es.getExamdocname();//责任医师姓名	ZRYSXM	字符串	50	必填	
		String ZJJG = es.getFinal_exam_result();//总检结果	ZJJG	字符串	1024	必填	总检的结果
		String JBJX="无";//疾病解析	JBJX	字符串	1024		
		String JY="无";//建议	JY	字符串	1024	必填	医生的建议
		String TJSFHGDM="2";//体检是否合格结论代码	TJSFHGDM	字符串	1	必填	1：合格 2：不合格 3：专业受限（适用于高校入学。体检目的类型需要有 结论的报告单。详细 见体检报告说明1） 
		String TJSFHGMS="无";//体检是否合格结论描述	TJSFHGMS	字符串	32	必填	体检目的类型需要有结论的 报告单。详体检报告说明1
		String TJKSSJ="";//体检开始时间	TJKSSJ	DATETIME 必填	
		if(StringUtil.isEmpty(ei.getJoin_date())){
			date=DateTimeUtil.parse(ei.getJoin_date());
			TJKSSJ=DateTimeUtil.shortFmt2(date); //出生日期	CSRQ	日期		必填	
		}else{
			TJKSSJ=DateTimeUtil.getDateTime();
		}		
		String TJJSSJ =TJKSSJ; //体检结束时间	TJJSSJ	DATETIME		必填	
		String ZJRQ = "";//总检日期	ZJRQ	日期		必填	总检的日期
		if(StringUtil.isEmpty(es.getCreate_time())){
			date=DateTimeUtil.parse(es.getCreate_time());
			ZJRQ=DateTimeUtil.shortFmt2(date); //出生日期	CSRQ	日期		必填	
		}else{
			ZJRQ=DateTimeUtil.getDateTime();
		}	
		String ZJYSBH = es.getCheck_doc()+"";//总检医生编号	ZJYSBH	字符串	36	必填	总检医生编号
		String ZJYS = es.getCheckdocname();//总检医生姓名	ZJYS	字符串	50	必填	总检的医生
		String ZJYSSFZH="000000000000000000"; //总检医生身份证号	ZJYSSFZH	字符串	18	必填	  
		int XGBZ=0;//	修改标志	XGBZ	数字	1	必填	编码。0：正常、1：撤销、2：异常；SD3.1.011
		Connection connect = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + es.getExam_info_id() + ":" + url);
			String sql="delete from TB_YL_TJBGSY where TJBH='"+es.getExam_num()+"' and YLJGDM='"+YLJGDM+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "+++sql+++" + sql);
			connect.createStatement().executeUpdate(sql);
			
			sql ="insert into TB_YL_TJBGSY(YLJGDM,TJBH,YLJGMC,KH,KLX,TJMDDM,XM,XB,NL,CSRQ,ZJHM,ZJLX,"
					+ "HYZK,ZYMC,ZYLBDM,SHXG_GZYD,SHXG_SM,SHXG_YSQK,SG,TZ,SZY,SSY,ZZDMJKJC,ZZMCJKJC,ZRYSXM,ZJJG,JBJX,JY,TJSFHGDM,"
					+ "TJSFHGMS,TJKSSJ,TJJSSJ,ZJRQ,ZJYSBH,ZJYS,ZJYSSFZH,XGBZ) values('"+YLJGDM+"','"+TJBH+"','"+YLJGMC+"','"
					+KH+"','"+KLX+"','"+TJMDDM+"','"+XM+"','"+XB+"','"+NL+"',to_date('"+CSRQ+"','yyyyMMdd'),'"+ZJHM+"','"+ZJLX+"','"
					+HYZK+"','"+ZYMC+"','"+ZYLBDM+"','无','无','无','"+SG+"','"+TZ+"','"+SZY+"','"+SSY+"','"+ZZDMJKJC+"','"
					+ZZMCJKJC+"','"+ZRYSXM+"','"+ZJJG+"','"+JBJX+"','"+JY+"','"+TJSFHGDM+"','"+TJSFHGMS+"',"
					+"to_date('"+TJKSSJ+"','yyyy-MM-dd HH24:mi:ss'),to_date('"+TJJSSJ+"','yyyy-MM-dd HH24:mi:ss'),to_date('"+ZJRQ+"','yyyy-MM-dd HH24:mi:ss'),'"+ZJYSBH+"','"+ZJYS+"','"+ZJYSSFZH+"',"+XGBZ+")";
			TranLogTxt.liswriteEror_to_txt(logname, "===sql2222===" + sql);
			connect.createStatement().executeUpdate(sql);
			return true;
		}catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "===com.hjw.interfaces.util.StringUtil.formatException(ex)22===" +com.hjw.interfaces.util.StringUtil.formatException(ex));
			return false;
			} finally {
				try {
					if (connect != null) {
						OracleDatabaseSource.close(connect);
					}
				} catch (Exception sqle4) {
					sqle4.printStackTrace();
				}
			}		
	}catch (Exception ex) {
		return false;
	}
	}
	
   private 	boolean getTB_YL_TJBG(String url,long examid,ExamInfoUserLKBean ei,String logname){	   
	   List<ExamResDEPBean> edlist= new ArrayList<ExamResDEPBean>();
	   edlist=getResDep(examid,ei,logname);
	   for(ExamResDEPBean ed:edlist){
		   // 医疗机构代码	YLJGDM	字符串	22	必填	复合主键；代码，填写本院的机构代码
		   String BGLSH= String.valueOf(ed.getId());  // 分科报告流水号	BGLSH	字符串	50	必填	复合主键；唯一标识此项分科体检。见说明1
		   //医疗机构名称	YLJGMC	字符串	70	必填	
		   String KH = ei.getArch_num(); //卡号	KH	字符串	32	必填	患者识别码，与患者信息表进行关联
		   String KLX = "3" ;//卡类型	KLX	字符串	16	必填	
		   String TJBH =ei.getExam_num() ;//体检编号	TJBH	字符串	50	必填	外键。关联体检报告首页
		   String KSBM ="160";	  // 执行科室编码	KSBM	字符串	64	必填	
		   String KSMC="执行科室名称";//	KSMC	字符串	76	必填	
		   String ZHMC=ed.getDep_name()+"检查"   ;// 组合名称	ZHMC	字符串	50	必填	指相关科室开展体检业务项目的统称，如口腔科的组合名称为“口腔检查”，内科对应的组合名称为“内科检查”等
		   String TJKBBM=ed.getRemark();//体检科别编码	TJKBBM	字符串	2	必填	DMD-54 体检科别代码表
		   String TJKBMC=ed.getDep_name();//体检科别名称	TJKBMC	字符串	36	必填	  
		   String TJXJ=ed.getExam_result_summary(); //体检小结	TJXJ	字符串	1024	必填	体检结论的描述
		   String BGRQ =ed.getCreate_time()+" 00:00:00" ;//报告日期	BGRQ	date		必填	
		   TranLogTxt.liswriteEror_to_txt(logname, "====BGRQ报告日期===" + BGRQ);
		   String BGYSBH="BGYSBH";//报告医师编号	BGYSBH	字符串	36	必填	报告医师编号
		   String BGYSXM=ed.getExam_doctor();  //报告医师姓名	BGYSXM	字符串	50	必填	报告医师姓名
		   String BGYSSFZH="000000000000000000"; //报告医师身份证号	BGYSSFZH	字符串	18	必填	
		   String XGBZ = "0";//修改标志	XGBZ	数字	1	必填	编码。0：正常、1：撤销、2：异常；SD3.1.011
		   Connection connect = null;
			try {
				String dburl = url.split("&")[0];
				String user = url.split("&")[1];
				String passwd = url.split("&")[2];
				connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
				TranLogTxt.liswriteEror_to_txt(logname, "req:" +ei.getExam_num()+ ":" + url);
				String sql="delete from TB_YL_TJBG where TJBH='"+ei.getExam_num()+"' and YLJGDM='"+YLJGDM+"' and BGLSH='"+ed.getId()+"' ";
				TranLogTxt.liswriteEror_to_txt(logname, "====sql33===" + sql);
				connect.createStatement().executeUpdate(sql);
				sql ="insert into TB_YL_TJBG(YLJGDM,BGLSH,YLJGMC,KH,KLX,TJBH,KSBM,KSMC,ZHMC,TJKBBM,"
						+ "TJKBMC,TJXJ,BGRQ,BGYSBH,BGYSXM,BGYSSFZH,XGBZ) values('"+YLJGDM+"','"+BGLSH+"','"+YLJGMC+"','"
						+KH+"','"+KLX+"','"+TJBH+"','"+KSBM+"','"+KSMC+"','"+ZHMC+"','"+TJKBBM+"','"+TJKBMC+"','"
						+TJXJ+"',to_date('"+BGRQ+"','yyyy-MM-dd HH24:mi:ss'),'"+BGYSBH+"','"+BGYSXM+"','"+BGYSSFZH+"',"+XGBZ+")";
				TranLogTxt.liswriteEror_to_txt(logname, "===SQL===" + sql);
				connect.createStatement().executeUpdate(sql);
			}catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logname, "====com.hjw.interfaces.util.StringUtil.formatException(ex)33====" +com.hjw.interfaces.util.StringUtil.formatException(ex));
				return false;
				} finally {
					try {
						if (connect != null) {
							OracleDatabaseSource.close(connect);
						}
					} catch (Exception sqle4) {
						sqle4.printStackTrace();
					}
				}		

	   }
	   
		return true;
   }
   
   private boolean getTB_YL_TJMX(String url,long examid,ExamInfoUserLKBean ei,String logname){
	   List<ExamDetailBean> edlist= new ArrayList<ExamDetailBean>();
	   TranLogTxt.liswriteEror_to_txt(logname, "===getTB_YL_TJMX方法==");
	   edlist=getDetail(examid,ei,logname);
	   TranLogTxt.liswriteEror_to_txt(logname, "===经过getDetail方法==");
	   for(ExamDetailBean ed:edlist){
	      String XMMXID=ed.getId() ;// 项目明细ID	XMMXID	字符串	64	必填	复合主键；唯一标示一条明细记录的序号。见说明1
	      // 医疗机构代码	YLJGDM	字符串	22	必填	复合主键；代码，填写本院的机构代码
	      //医疗机构名称	YLJGMC	字符串	70	必填	
	      String BGLSH="无";//分科报告流水号	BGLSH	字符串	50	必填	外键，关联体检分科报告  此字段无法关联
	      int XMXH = 1 ;//项目序号	XMXH	数字	4	必填	项目在分科报告中的排序，其中公卫健康体检，住院史、病床史、用药记录、疫苗等填写记录顺序号。
	      String XMDM=""; //卫标项目代码	XMDM	字符串	20	可选	填写CC99.00100 健康体检卫标代码中的数据元标识符即可  公共卫生健康体检本项必填
	      String TJXMBM=ed.getRemark(); //体检项目编码	TJXMBM	字符串	20	必填	  CC99.00008 体检项目字典表
	      String TJXMMC=ed.getItem_name();//体检项目名称	TJXMMC	字符串	40	必填	  
	      String XMBMYN=ed.getItem_num()   ;//项目代码（院内）	XMBMYN	字符串	36	必填	医院自定义的体检项目编码
	      String XMBMMC=ed.getItem_name() ;//项目名称（院内）	XMBMMC	字符串	120	必填	  
	      String XMJCJG=ed.getExam_result();//项目检查结果	XMJCJG	字符串	100	必填	检查项目的结果，如心率的检查结果为：80 单位为分钟，多个结果需用‘；’分割。
	      String XMJCJGDW=ed.getItem_unit() ;//单位	XMJCJGDW	字符串	16	可选	项目检查结果
	      double CKZSX=ed.getRef_Fmax();//正常参考值下限	CKZSX	字符串	100	可选	  
	      double CKZXX=ed.getRef_Fmin();//正常参考值上限	CKZXX	字符串	100;	可选	  
	      String CKZBZ="";//参考值备注	CKZBZ	字符串	1024	可选	  
	      String CKZJS=ed.getHealth_level();////结果值的解释	CKZJS	字符串	1	必填	0：正常；1：高；2：低；3：弱阳性；4：阳性。
	      String JCYCBZ=ed.getYcbz();//检查异常标志	JCYCBZ	字符串	1	必填	1：正常；2：无法量化的异常；3：异常偏高；4：异常偏低；9：其他情况
	      String BGRQ=DateTimeUtil.getDate();//报告日期	BGRQ	字符串	8	必填	
	      String JCSJ=ed.getExam_date(); // 检测时间	JCSJ	字符串	15	必填	
	      int XGBZ =1;//修改标志	XGBZ	数字	1	必填	编码。0：正常、1：撤销、2：异常；SD3.1.011
	      Connection connect = null;
			try {
				String dburl = url.split("&")[0];
				String user = url.split("&")[1];
				String passwd = url.split("&")[2];
				connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
				TranLogTxt.liswriteEror_to_txt(logname, "===ei.getExam_num()==" +ei.getExam_num()+ ":" + url);
				String sql="delete from TB_YL_TJMX where XMMXID='"+XMMXID+"' and YLJGDM='"+YLJGDM+"' ";
				TranLogTxt.liswriteEror_to_txt(logname, "====deleteRes===" + sql);
				connect.createStatement().executeUpdate(sql);
				sql ="insert into TB_YL_TJMX(XMMXID,YLJGDM,YLJGMC,BGLSH,XMXH,XMDM,TJXMBM,TJXMMC,XMBMYN,XMBMMC,XMJCJG,"
						+ "XMJCJGDW,CKZSX,CKZXX,CKZBZ,CKZJS,JCYCBZ,BGRQ,JCSJ,XGBZ) values('"+XMMXID+"','"+YLJGDM+"','"
						+YLJGMC+"','"+BGLSH+"','"+XMXH+"','"+XMDM+"','"+TJXMBM+"','"+TJXMMC+"','"+XMBMYN+"','"
						+XMBMMC+"','"+XMJCJG+"','"+XMJCJGDW+"','"+CKZSX+"','"+CKZXX+"','"+CKZBZ+"','"+CKZJS+"','"
						+JCYCBZ+"','"+BGRQ+"', '"+JCSJ+"',"+XGBZ+")";
				TranLogTxt.liswriteEror_to_txt(logname, "===20190422===" + sql);
				connect.createStatement().executeUpdate(sql);
			}catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logname, "===com.hjw.interfaces.util.StringUtil.formatException(ex)444==" +com.hjw.interfaces.util.StringUtil.formatException(ex));
				//return false;
				} finally {
					try {
						if (connect != null) {
							OracleDatabaseSource.close(connect);
						}
					} catch (Exception sqle4) {
						sqle4.printStackTrace();
					}
				}		

	   }
	   return true;
   }
	
   /**
    * 获取部门结论信息
    * @param examid
    * @param ei
    * @param logname
    * @return
    */
   private List<ExamDetailBean> getDetail(long examid,ExamInfoUserLKBean ei,String logname){
	   TranLogTxt.liswriteEror_to_txt(logname, "===getDetail方法==");
	   StringBuffer sb = new StringBuffer();
		sb.append("SELECT 'comm'+convert(varchar,com.id) as id ,e.exam_num, com.exam_info_id, eci.charge_item_id AS charging_id,"
				+ " com.exam_item_id, com.exam_result, CONVERT(varchar(100),com.exam_date,112) as exam_date, eci.exam_doctor_name,"
				+ "ei.item_num,ei.item_name,ei.remark,com.health_level,ei.item_unit,ei.ref_Fmax,ei.ref_Fmin FROM common_exam_detail com,examinfo_charging_item eci,charging_item ci,"
				+ "charging_item_exam_item cei,examination_item ei, exam_info e WHERE  com.exam_info_id = e.id "
				+ "AND eci.examinfo_id = e.id AND eci.charge_item_id = ci.id AND cei.charging_item_id = ci.id "
				+ "AND cei.exam_item_id = ei.id AND com.exam_item_id = ei.id AND e.is_Active = 'Y' AND eci.isActive = 'Y' "
				+ "and e.exam_num='"+ei.getExam_num()+"'");
		List<ExamDetailBean> map1 = this.jdbcQueryManager.getList(sb.toString(), ExamDetailBean.class);
		sb.append(" SELECT 'lis'+convert(varchar,ed.id) as id,e.exam_num, ed.exam_info_id, eci.charge_item_id AS charging_id,"
				+ "ed.exam_item_id, ed.exam_result,CONVERT(varchar(100),ed.exam_date,112) as exam_date,"
				+ "eci.exam_doctor_name,ei.item_num,ei.item_name,ei.remark,ed.ref_indicator as health_level,ed.item_unit,ei.ref_Fmax,ei.ref_Fmin "
				+ " FROM exam_result_detail ed,"
				+ "examinfo_charging_item eci, charging_item ci, charging_item_exam_item cei, examination_item ei, "
				+ "exam_info e WHERE ed.exam_info_id = e.id AND eci.examinfo_id = e.id AND eci.charge_item_id = ci.id "
				+ "AND cei.charging_item_id = ci.id AND cei.exam_item_id = ei.id AND ed.exam_item_id = ei.id AND "
				+ "e.is_Active = 'Y' AND eci.isActive = 'Y' and e.exam_num='"+ei.getExam_num()+"'");
		List<ExamDetailBean> map2 = this.jdbcQueryManager.getList(sb.toString(), ExamDetailBean.class);
		sb.append(" SELECT 'pacs'+convert(varchar,v.id) as id,p.examinfo_num, v.exam_info_id, ci.id AS charging_id, "
				+ "v.exam_item_id, v.exam_result, CONVERT(varchar(100),v.exam_date,112) as exam_date, v.exam_doctor,"
				+ "ei.item_num,ei.item_name,ei.remark FROM pacs_detail p, view_exam_detail v "
				+ "left join examination_item ei on ei.id=v.exam_item_id , charging_item ci WHERE "
				+ "p.summary_id = v.pacs_id AND p.chargingItem_num = ci.item_code and p.examinfo_num='"+ei.getExam_num()+"'");
		List<ExamDetailBean> map3 = this.jdbcQueryManager.getList(sb.toString(), ExamDetailBean.class);
		List map=new ArrayList<ExamDetailBean>();
		for(ExamDetailBean ed:map1){
			if(ed.getHealth_level()==null){
				ed.setHealth_level("0");
				ed.setYcbz("2");
			}else if("Z".equals(ed.getHealth_level())){
				ed.setHealth_level("0");
				ed.setYcbz("1");
			}else if("Y".equals(ed.getHealth_level())){
				ed.setHealth_level("0");
				ed.setYcbz("2");
			}else if("W".equals(ed.getHealth_level())){
				ed.setHealth_level("0");
				ed.setYcbz("2");
			}else{
				ed.setHealth_level("0");
				ed.setYcbz("2");
			}
			map.add(ed);
		}
		
		
		for(ExamDetailBean ed:map2){
			if(ed.getHealth_level()==null){
				ed.setHealth_level("0");
				ed.setYcbz("2");
			}else if("1".equals(ed.getHealth_level())){
				ed.setHealth_level("1");
				ed.setYcbz("1");
			}else if("2".equals(ed.getHealth_level())){
				ed.setHealth_level("2");
				ed.setYcbz("9");
			}else if("3".equals(ed.getHealth_level())){
				ed.setHealth_level("3");
				ed.setYcbz("9");
			}else if("4".equals(ed.getHealth_level())){
				ed.setHealth_level("2");
				ed.setYcbz("3");
			}else if("5".equals(ed.getHealth_level())){
				ed.setHealth_level("3");
				ed.setYcbz("4");
			}else if(ed.getHealth_level()==null){
				ed.setHealth_level("0");
				ed.setYcbz("2");
			}
			map.add(ed);
		}
		
		
		for (ExamDetailBean ed : map3) {
			ed.setHealth_level("0");
			ed.setYcbz("9");
			map.add(ed);
		}
		
		return map;
   }
   
   /**
    * 获取部门结论信息
    * @param examid
    * @param ei
    * @param logname
    * @return
    */
   private List<ExamResDEPBean> getResDep(long examid,ExamInfoUserLKBean ei,String logname){
	   StringBuffer sb = new StringBuffer();
		sb.append("select der.id,dd.dep_num,dd.dep_name,dd.remark,der.exam_doctor,der.exam_result_summary,"
				+ "CONVERT(varchar(100),der.create_time, 112) as create_time "
				+ "from exam_dep_result der "
				+ "left join department_dep dd on dd.id=der.dep_id "
				+ "where der.exam_info_id='" + examid + "'");
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb.toString());
		List<ExamResDEPBean> map = this.jdbcQueryManager.getList(sb.toString(), ExamResDEPBean.class);
		return map;
   }
	
	/**
	 * 一般科室信息
	 * @param ei
	 * @param exam_item_id
	 * @param logname
	 * @return
	 */
	private String getcustomComm(long examid,long exam_item_id,String logname){
		Connection peis_connect = null;
		String res=" ";
		try {
			peis_connect = jdbcQueryManager.getConnection();
			String sql="select ced.exam_result from common_exam_detail ced where ced.exam_item_id='"+exam_item_id+"' and ced.exam_info_id='"+examid+"' --身高";
			ResultSet rs = peis_connect.createStatement().executeQuery(sql);		
			if (rs.next()) {
				res=rs.getString("exam_result");
			}
			rs.close();
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			try {
				
				if (peis_connect != null) {
					peis_connect.close();
				}
				
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		return res;
	}
	
	
	/**
	 * 一般科室信息
	 * @param ei
	 * @param exam_item_id
	 * @param logname
	 * @return
	 */
	private String[] getdisease(long examid,String logname){
		Connection peis_connect = null;
		String[] res={"",""};
		String icd_10="";
		String disease_name="";
		try {
			peis_connect = jdbcQueryManager.getConnection();
			String sql="select dkl.icd_10,ed.disease_name from examinfo_disease ed "
					+ "left join disease_knowloedge_lib dkl on dkl.id=ed.disease_id "
					+ " where exam_info_id='"+examid+"' and ed.isActive='Y' order by disease_index";
			ResultSet rs = peis_connect.createStatement().executeQuery(sql);		
			while (rs.next()) {
				disease_name=disease_name+";"+rs.getString("disease_name");
				icd_10=icd_10+";"+rs.getString("icd_10");
			}
			rs.close();
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			try {				
				if (peis_connect != null) {
					peis_connect.close();
				}				
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		if(disease_name.trim().length()>0){
			disease_name=disease_name.substring(1,disease_name.length());
		}
		if(icd_10.trim().length()>0){
			icd_10=icd_10.substring(1,icd_10.length());
		}
		res[0]=icd_10;
		res[1]=disease_name;
		return res;
	}
}