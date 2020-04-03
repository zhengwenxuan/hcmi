package com.hjw.webService.client.hanshou.bean;

public class ExamInfoUserDTOHS {
	private static final long serialVersionUID = -97502163798576023L;

	private long id;

    private long group_id;
    
	private long customer_id;
	
	private String exam_num="";
	
	private String status="";
	
	private String register_date="";
	
	private String join_date="";
	
	private long join_operator;
	
	private String final_date="";
	
	private String final_doctor="";
	
	private String check_doctor="";
	
	private String exam_status;
	
	private String exam_type="";
	
	private String exam_types="";
	
	private String customer_type="";
	
	private String is_sampled_directly="";
	
	private String is_adjusted="";
	
	private String center_num="";
	
	private String getReportWay="";
	
	private String getReportWays="";
	
	private String reportAddress="";
	
	private String report_class;
	
	private String chargingType="";
	
	private String customerType="";
	
	private String group_index="";
	
	private String is_Active="";
	
	private String is_Actives="";
	
	private long creater;
	
	private String create_time="";
	
	private long updater;
	
	private String update_time=null;
	
	private String is_guide_back="";
	
	private String is_guide_backs="";
	
	private String company_check_status="";
	
	private long customer_type_id;
	
	private String customer_type_name;
	
	private String is_marriage="";
	
	private long age;
	
	private String address="";
	
	private String email="";
	
	private String phone="";
	
	private String company="";
	
	private String position="";
	
	private String _level="";
	
	private String is_after_pay="";
	
	private String past_medical_history="";
	
	private String remarke="";
	
	private String introducer="";

	private String degreeOfedu = "";
	
	private int political_status;
	
	private String counter_check="";
	
	private String counter_checks;
	
	private String guide_nurse="";
	
	private String appointment="";
	
	private String data_source="";
	
	private String others="";
	
	private String order_id="";
	
	private String user_name="";
	
	private String sex="";		
	
	private String arch_num="";	
	
	private String id_num="";		
	
	private String birthday="";
	
	private String is_need_barcode="";
	
	private String is_need_guide="";
	
    private String is_need_barcodes="";
	
	private String is_need_guides="";
	
	private String dep_name="";
	
	private String group_name="";		
	
	private String set_name="";	
	
	private String remark1="";		
	
	private String statuss="";
	
	private String exam_time="";	
	
	private String exam_times="";	
	
	private long wpacs;
	
	private long ypacs;
	
	private long wlis;
	
	private long ylis;
	
	private String pacs="";
	
	private String lis="";		
	
	private String nation="";
	
	private long batch_id;
	
	private String batch_name="";
	
	private long company_id;
    
    private String picture_Path="";

    private String patient_id="";
    
    private String chi_name="";

	private String employeeID="";
	
	private String medical_insurance_card;

	private String mc_no="";//就诊卡号

	private String visit_date="";//就诊日期

	private String visit_no="";//就诊号
	
	private String clinic_no="";//门诊号	
	
	private String exam_indicator="";//团体付费状态 T团体结算 G 自费结算	
	
	private String type_name="";//人员类型
	
    private String actiontype="";
    
    private String actiontypes="";	 
    
    private int isnotpay;//是否包含弃检项目 0不包含1 包含
    
    private int isprepay;//是否预结算	   0不预结算 1 预结算 	  
    
    private long c_id;
	
    private double personal_pay;
    
    private double team_pay;
    
    private String examcount="您是第 0次体检.";	 
    
    private String acc_num="";
    
    private String createdate="";
   
    private String printflag="";
    
    private long printer;
    
    private String printerName;
    
    private String printdate="";
    
    private String flag="0";	
    
    private long freeze;
    
    private String freezename="";	
    
    private String occusectorid;
    
    private String read_status_str;
    
    private long read_status;//读取状态
    private String bunk;//床位
    private String allocationdate;//分配时间
    
    private String receive_type;//是否邮寄
    private String disease_name;//
    
    private String c_name;
    private String u_name;
    
    private String exam_desc;
    private String exam_result;
    private String exam_doctor;
    private String item_name;
    private String data_name;
    private String marker;
    private String mark_time;
    private int item_number;
    private String item_num_s;
    private double budget_amount;//预算金额
    private String idtypename;
    private String visit_no_j;	    
    private String tjlx;//体检类型
    private int idtype;	    
    private String is_djt_type;
    private String deptname;
    private long marriage_age;//婚龄
    private int h0;
    private int h1; 
    private String h0date="";
    private String h1date="";
    
    private String strh0="";
    private String strh1="";  
    
    private int s;
    private int s0;
    private int s1; 
    private String sdate="";
    private String s0date="";
    private String s1date="";
    private String strs0="";
    private String strs1="";   
    
    
    private int p0;
    private int p1; 
    private String p0date="";
    private String p1date="";        
    private String strp0="";
    private String strp1="";  
    
    private String edesc;
    private int e0;
    private int e1; 
    private String e0date="";
    private String e1date="";        
    private String stre0="";
    private String stre1="";
    private String e1creater="";
    
    
    private int z0;
    private int z1;
    private String strz0="";
    private String strz1="";  
    private String z0date;
    private String z1date;
    private int c;
    private String cdate;
    private int f;
    private String strc="";
    private String strf="";  
    private String fdate;
	private String z0user;
	private String z1user;
	private String cuser;
	private String fuser;
	
	private int m;
	private String mdate="";
	private String strm="";
	
	private String vipflag="";
	private String visit_num="";
	private String customer_feedback="";
	private String visit_result="";
	private String visit_type="";
	private String visit_doctor="";
	private String remark="";
	private String recordID="";
	
	private int kaidanren;
	private String kaidanrens;
	private int wuxuzongjian;
	
	private int num;
	private String p0creater;
	private String p1creater;
	private String s0creater;
	private String s1creater;
	
     private long acccreater;
     private long sendcreater;
     private String senddate;
     private int flow_type;
     private String flow_types;
     private String flow_name="";
     private String senduname="";
	 private String accuname="";
	 
	 private String is_report_print="";
	private String is_report_print_y="";
	private String is_report_tidy;
	private String is_report_tidy_y;
	private String receive_type1="";
	private String receive_type_y="";
	
	private String receive_address="";
	private String receive_name="";
	private String receive_date="";
	private String receive_remark="";
	private String vtcreater="";
	private String lc_remark;
	private String receive_phone;
	private String receive_postcode;
	
	private long dept_id;
	private String remark_time;
	private long remark_user;
	private double calculation_amount;
	private int calculation_rate;
	private int v;
	private String v_name="";
	private long t;		
	private String swuxuzongjian;
	
	private String billdep="";		
    private String notices;     
    
    private int isaffixedcode;//是否贴码
    private int isshowtable;//是否显示表格
    private String isaffixedcodes="否";//是否贴码
    private String isshowtables="否";//是否显示表格
    private String customertypename;//人员类型名称
    
    private int top_row;//是否高亮置顶（团体金额和分组金额不同）
    
    private String membership_card;
    
    private String  check_time; //审核时间
    
    private String pay_status; //付费状态
    
    private int weijian;
    
    private String join_operatorName;//报到人
    
    private long ecd_id;//危急值id
    private String DJD_path;

    private double pay_amount;
    private double no_pay_amount;
    
    private String  chargeType;
    private int  vipsigin;
    
    private String dep_num;
    
    
    
	public String getDep_num() {
		return dep_num;
	}

	public void setDep_num(String dep_num) {
		this.dep_num = dep_num;
	}

	public int getVipsigin() {
		return vipsigin;
	}

	public void setVipsigin(int vipsigin) {
		this.vipsigin = vipsigin;
	}

	public double getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(double pay_amount) {
		this.pay_amount = pay_amount;
	}

	public double getNo_pay_amount() {
		return no_pay_amount;
	}

	public void setNo_pay_amount(double no_pay_amount) {
		this.no_pay_amount = no_pay_amount;
	}

	public long getJoin_operator() {
		return join_operator;
	}

	public void setJoin_operator(long join_operator) {
		this.join_operator = join_operator;
	}

	public int getWeijian() {
		return weijian;
	}

	public void setWeijian(int weijian) {
		this.weijian = weijian;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}

	public String getCheck_time() {
		return check_time;
	}

	public void setCheck_time(String check_time) {
		this.check_time = check_time;
	}

	public String getKaidanrens() {
		return kaidanrens;
	}

	public void setKaidanrens(String kaidanrens) {
		this.kaidanrens = kaidanrens;
	}

	public String getCustomertypename() {
		return customertypename;
	}

	public void setCustomertypename(String customertypename) {
		this.customertypename = customertypename;
	}

	public int getIsaffixedcode() {
		return isaffixedcode;
	}

	public void setIsaffixedcode(int isaffixedcode) {
		this.isaffixedcode = isaffixedcode;
		if(isaffixedcode==0){
			this.setIsaffixedcodes("否");
		}else if(isaffixedcode==1){
			this.setIsaffixedcodes("是");
		}
	}

	public int getIsshowtable() {
		return isshowtable;
	}

	public void setIsshowtable(int isshowtable) {
		this.isshowtable = isshowtable;
		if(isshowtable==0){
			this.setIsshowtables("否");
		}else if(isshowtable==1){
			this.setIsshowtables("是");
		}
	}

	public String getIsaffixedcodes() {
		return isaffixedcodes;
	}

	public void setIsaffixedcodes(String isaffixedcodes) {
		this.isaffixedcodes = isaffixedcodes;
	}

	public String getIsshowtables() {
		return isshowtables;
	}

	public void setIsshowtables(String isshowtables) {
		this.isshowtables = isshowtables;
	}

	public String getNotices() {
		return notices;
	}

	public void setNotices(String notices) {
		this.notices = notices;
	}

	public String getBilldep() {
		return billdep;
	}

	public void setBilldep(String billdep) {
		this.billdep = billdep;
	}

	public String getIs_Actives() {
		return is_Actives;
	}

	public void setIs_Actives(String is_Actives) {
		this.is_Actives = is_Actives;
	}

	public String getSwuxuzongjian() {
		return swuxuzongjian;
	}

	public void setSwuxuzongjian(String swuxuzongjian) {
		this.swuxuzongjian = swuxuzongjian;
	}

	public long getT() {
		return t;
	}

	public void setT(long t) {
		this.t = t;
	}

	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	public String getV_name() {
		return v_name;
	}

	public void setV_name(String v_name) {
		this.v_name = v_name;
	}

	public double getCalculation_amount() {
		return calculation_amount;
	}

	public void setCalculation_amount(double calculation_amount) {
		this.calculation_amount = calculation_amount;
	}

	public int getCalculation_rate() {
		return calculation_rate;
	}

	public void setCalculation_rate(int calculation_rate) {
		this.calculation_rate = calculation_rate;
	}

	public long getRemark_user() {
		return remark_user;
	}

	public void setRemark_user(long remark_user) {
		this.remark_user = remark_user;
	}

	public long getDept_id() {
		return dept_id;
	}

	public void setDept_id(long dept_id) {
		this.dept_id = dept_id;
	}

	public String getRemark_time() {
		return remark_time;
	}

	public void setRemark_time(String remark_time) {
		this.remark_time = remark_time;
	}

	public String getLc_remark() {
		return lc_remark;
	}

	public void setLc_remark(String lc_remark) {
		this.lc_remark = lc_remark;
	}

	public String getIs_report_print() {
			return is_report_print;
		}

		public void setIs_report_print(String is_report_print) {
			this.is_report_print = is_report_print;
			if("Y".equals(is_report_print)){
				this.setIs_report_print_y("是");
			}else{
				this.setIs_report_print_y("否");
			}
		}

		public String getIs_report_tidy() {
			return is_report_tidy;
		}

		public void setIs_report_tidy(String is_report_tidy) {
			this.is_report_tidy = is_report_tidy;
			if("Y".equals(is_report_tidy)){
				this.setIs_report_tidy_y("已整理");
			}else{
				this.setIs_report_tidy_y("未整理");
			}
		}

		public String getIs_report_tidy_y() {
			return is_report_tidy_y;
		}

		public void setIs_report_tidy_y(String is_report_tidy_y) {
			this.is_report_tidy_y = is_report_tidy_y;
		}
		
		public String getReceive_type1() {
			return receive_type1;
		}

		public void setReceive_type1(String receive_type1) {
			this.receive_type1 = receive_type1;
			if("0".equals(receive_type1)){
				this.setReceive_type_y("未邮寄,未自取");
			}else if("1".equals(receive_type1)){
				this.setReceive_type_y("已邮寄");
			}else if("2".equals(receive_type1)){
				this.setReceive_type_y("已自取");
			}
		}

		public String getIs_report_print_y() {
			return is_report_print_y;
		}

		public void setIs_report_print_y(String is_report_print_y) {
			this.is_report_print_y = is_report_print_y;
		}

		public String getReceive_type_y() {
			return receive_type_y;
		}

		public void setReceive_type_y(String receive_type_y) {
			this.receive_type_y = receive_type_y;
		}
	
	public int getKaidanren() {
		return kaidanren;
	}

	public void setKaidanren(int kaidanren) {
		this.kaidanren = kaidanren;
	}

	public int getWuxuzongjian() {
		return wuxuzongjian;
	}

	public void setWuxuzongjian(int wuxuzongjian) {
		this.wuxuzongjian = wuxuzongjian;
		if(this.wuxuzongjian==1){
			this.setSwuxuzongjian("否");
		}else{
			this.setSwuxuzongjian("是");
		}
	}

	public String getMedical_insurance_card() {
		return medical_insurance_card;
	}

	public void setMedical_insurance_card(String medical_insurance_card) {
		this.medical_insurance_card = medical_insurance_card;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
		if(m==0){
			this.strm="未解读";
		}else if(m==1){
			this.strm="已解读";
		}
	}

	public String getMdate() {
		return mdate;
	}

	public void setMdate(String mdate) {
		this.mdate = mdate;
	}

	public String getStrm() {
		return strm;
	}

	public void setStrm(String strm) {
		this.strm = strm;
	}

	public String getStrz0() {
		return strz0;
	}

	public void setStrz0(String strz0) {
		this.strz0 = strz0;
	}

	public String getStrz1() {
		return strz1;
	}

	public void setStrz1(String strz1) {
		this.strz1 = strz1;
	}

	public String getStrc() {
		return strc;
	}

	public void setStrc(String strc) {
		this.strc = strc;
	}

	public String getStrf() {
		return strf;
	}

	public void setStrf(String strf) {
		this.strf = strf;
	}

	public int getZ0() {
		return z0;
	}

	public void setZ0(int z0) {
		this.z0 = z0;
		if(z0==1){
			this.strz0="总检进行中";
		}else if(z0==0){
			this.strz0="总检未开始";
		}
	}

	public int getZ1() {
		return z1;
	}

	public void setZ1(int z1) {
		this.z1 = z1;
		if(z1==0){
			this.strz1="总检未结束";
		}else if(z1==1){
			this.strz1="总检已完成";
		}
	}

	public String getZ0date() {
		return z0date;
	}

	public void setZ0date(String z0date) {
		this.z0date = z0date;
	}

	public String getZ1date() {
		return z1date;
	}

	public void setZ1date(String z1date) {
		this.z1date = z1date;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
		if(c==0){
			this.strc="审核未完成";
		}else if(c==1){
			this.strc="审核已完成";
		}
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
		if(c==0){
			this.strc="复审未完成";
		}else if(c==1){
			this.strc="复审已完成";
		}
	}

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public String getZ0user() {
		return z0user;
	}

	public void setZ0user(String z0user) {
		this.z0user = z0user;
	}

	public String getZ1user() {
		return z1user;
	}

	public void setZ1user(String z1user) {
		this.z1user = z1user;
	}

	public String getCuser() {
		return cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public String getFuser() {
		return fuser;
	}

	public void setFuser(String fuser) {
		this.fuser = fuser;
	}

	public int getE0() {
		return e0;
	}

	public void setE0(int e0) {
		this.e0 = e0;
		if(e0==0){
			this.stre0="发送室未接收";
		}else if(e0==1){
			this.stre0="发送室已接收";
		}
	}

	public int getE1() {
		return e1;
	}

	public void setE1(int e1) {
		this.e1 = e1;
		if(e1==0){
			this.stre1="发送室未上传";
		}else if(e1==1){
			this.stre1="发送室已上传";
		}
	}

	public String getE0date() {
		return e0date;
	}

	public void setE0date(String e0date) {
		this.e0date = e0date;
	}

	public String getE1date() {
		return e1date;
	}

	public void setE1date(String e1date) {
		this.e1date = e1date;
	}

	public String getStre0() {
		return stre0;
	}

	public void setStre0(String stre0) {
		this.stre0 = stre0;
	}

	public String getStre1() {
		return stre1;
	}

	public void setStre1(String stre1) {
		this.stre1 = stre1;
	}

	public int getP0() {
		return p0;
	}

	public void setP0(int p0) {
		this.p0 = p0;
		if(p0==0){
			this.strp0="打印未接收";
		}else if(p0==1){
			this.strp0="打印已接收";
		}
	}

	public int getP1() {
		return p1;
	}

	public void setP1(int p1) {
		this.p1 = p1;
		if(p1==0){
			this.strp1="打印未上传";
		}else if(p1==1){
			this.strp1="打印已上传";
		}
	}

	public String getP0date() {
		return p0date;
	}

	public void setP0date(String p0date) {
		this.p0date = p0date;
	}

	public String getP1date() {
		return p1date;
	}

	public void setP1date(String p1date) {
		this.p1date = p1date;
	}

	public String getStrp0() {
		return strp0;
	}

	public void setStrp0(String strp0) {
		this.strp0 = strp0;
	}

	public String getStrp1() {
		return strp1;
	}

	public int getS0() {
		return s0;
	}

	public void setS0(int s0) {
		this.s0 = s0;
		if(s0==0){
			this.strs0="整单未接收";
		}else if(s0==1){
			this.strs0="整单已接收";
		}
	}

	public int getS1() {
		return s1;			
	}

	public void setS1(int s1) {
		this.s1 = s1;
		if(s1==0){
			this.strs1="整单未上传";
		}else if(s1==1){
			this.strs1="整单已上传";
		}
	}

	public String getS0date() {
		return s0date;
	}

	public void setS0date(String s0date) {
		this.s0date = s0date;
	}

	public String getS1date() {
		return s1date;
	}

	public void setS1date(String s1date) {
		this.s1date = s1date;
	}

	public String getStrs0() {
		return strs0;
	}

	public void setStrs0(String strs0) {
		this.strs0 = strs0;
	}

	public String getStrs1() {
		return strs1;
	}

	public void setStrs1(String strs1) {
		this.strs1 = strs1;
	}

	public void setStrh1(String strh1) {
		this.strh1 = strh1;
	}

	public String getH0date() {
		return h0date;
	}

	public void setH0date(String h0date) {
		this.h0date = h0date;
	}

	public String getH1date() {
		return h1date;
	}

	public void setH1date(String h1date) {
		this.h1date = h1date;
	}

	public String getStrh0() {
		return strh0;
	}

	public void setStrh0(String strh0) {
		this.strh0 = strh0;
	}

	public String getStrh1() {
		return strh1;
	}

	public void setStrp1(String strh1) {
		this.strh1 = strh1;
	}

	public int getH0() {
		return h0;
	}

	public void setH0(int h0) {
		this.h0 = h0;
		if(h0==0){
			this.strh0="未核收";
		}else if(h0==1){
			this.strh0="已核收";
		}
	}

	public int getH1() {
		return h1;
	}

	public void setH1(int h1) {
		this.h1 = h1;
		if(h1==0){
			this.strh1="核收未上传";
		}else if(h1==1){
			this.strh1="核收已上传";
		}
	}

	public long getMarriage_age() {
		return marriage_age;
	}

	public void setMarriage_age(long marriage_age) {
		this.marriage_age = marriage_age;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getTjlx() {
		return tjlx;
	}

	public void setTjlx(String tjlx) {
		this.tjlx = tjlx;
	}

	public String getIs_djt_type() {
		return is_djt_type;
	}

	public void setIs_djt_type(String is_djt_type) {
		this.is_djt_type = is_djt_type;
	}
	public String getVisit_no_j() {
		return visit_no_j;
	}

	public void setVisit_no_j(String visit_no_j) {
		this.visit_no_j = visit_no_j;
	}

	public String getIdtypename() {
		return idtypename;
	}

	public void setIdtypename(String idtypename) {
		this.idtypename = idtypename;
	}

	public int getIdtype() {
		return idtype;
	}

	public void setIdtype(int idtype) {
		this.idtype = idtype;
	}

	public String getItem_num_s() {
		return item_num_s;
	}

	public void setItem_num_s(String item_num_s) {
		this.item_num_s = item_num_s;
	}

	public int getItem_number() {
		return item_number;
	}

	public void setItem_number(int item_number) {
		this.item_number = item_number;
		if(item_number>0){
			this.setItem_num_s(item_number+"");
		} 
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public String getMark_time() {
		return mark_time;
	}

	public void setMark_time(String mark_time) {
		this.mark_time = mark_time;
	}

	public String getData_name() {
		return data_name;
	}

	public void setData_name(String data_name) {
		this.data_name = data_name;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getExam_desc() {
		return exam_desc;
	}

	public void setExam_desc(String exam_desc) {
		this.exam_desc = exam_desc;
	}

	public String getExam_result() {
		return exam_result;
	}

	public void setExam_result(String exam_result) {
		this.exam_result = exam_result;
	}

	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public String getU_name() {
		return u_name;
	}

	public void setU_name(String u_name) {
		this.u_name = u_name;
	}

	public String getDisease_name() {
		return disease_name;
	}

	public void setDisease_name(String disease_name) {
		this.disease_name = disease_name;
		if(disease_name!=null && !"".equals(disease_name)){
			String di = disease_name.substring(0,1);
			if("★".equals(di)){
				this.disease_name = "★";
			} else {
				this.disease_name = "";
			}
		}
	}

	public void setReceive_type(String receive_type) {
		if("1".equals(receive_type)){
			this.receive_type = "邮寄";
		} else if("0".equals(receive_type)) {
			this.receive_type = "自取";
		} else {
			this.receive_type = "";
		}
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAllocationdate() {
		return allocationdate;
	}

	public void setAllocationdate(String allocationdate) {
		this.allocationdate = allocationdate;
	}

	public String getBunk() {
		return bunk;
	}

	public void setBunk(String bunk) {
		this.bunk = bunk;
	}

	public long getRead_status() {
		return read_status;
	}

	public void setRead_status(long read_status) {
		this.read_status = read_status;
		if(read_status==0){
			this.read_status_str ="未读取";
		}else if(read_status==1){
			this.read_status_str ="已读取";
		}
	}

	public String getRead_status_str() {
		return read_status_str;
	}

	public void setRead_status_str(String read_status_str) {
		this.read_status_str = read_status_str;
	}

	public String getOccusectorid() {
		return occusectorid;
	}

	public void setOccusectorid(String occusectorid) {
		this.occusectorid = occusectorid;
	}

	public long getFreeze() {
		return freeze;
	}

	public void setFreeze(long freeze) {
		this.freeze = freeze;
		if(freeze==0){
			this.setFreezename("正常");
		}else if(freeze==1){
			this.setFreezename("冻结");
		}
	}

	public String getFreezename() {
		return freezename;
	}

	public void setFreezename(String freezename) {
		this.freezename = freezename;		
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getAcc_num() {
		return acc_num;
	}

	public void setAcc_num(String acc_num) {
		this.acc_num = acc_num;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getPrintflag() {
		return printflag;
	}

	public void setPrintflag(String printflag) {
		this.printflag = printflag;
	}

	public long getPrinter() {
		return printer;
	}

	public void setPrinter(long printer) {
		this.printer = printer;
	}

	public String getPrintdate() {
		return printdate;
	}

	public void setPrintdate(String printdate) {
		this.printdate = printdate;
	}

	public String getExamcount() {
		return examcount;
	}

	public void setExamcount(String examcount) {
		this.examcount = examcount;
	}

	public String getCustomer_type_name() {
		return customer_type_name;
	}

	public void setCustomer_type_name(String customer_type_name) {
		this.customer_type_name = customer_type_name;
	}

	public long getC_id() {
		return c_id;
	}

	public void setC_id(long c_id) {
		this.c_id = c_id;
	}

	public double getPersonal_pay() {
		return personal_pay;
	}

	public void setPersonal_pay(double personal_pay) {
		this.personal_pay = personal_pay;
	}

	public double getTeam_pay() {
		return team_pay;
	}

	public void setTeam_pay(double team_pay) {
		this.team_pay = team_pay;
	}

	public int getIsnotpay() {
		return isnotpay;
	}

	public void setIsnotpay(int isnotpay) {
		this.isnotpay = isnotpay;
	}

	public int getIsprepay() {
		return isprepay;
	}

	public void setIsprepay(int isprepay) {
		this.isprepay = isprepay;
	}

	public String getExam_status() {
		return exam_status;
	}

	public void setExam_status(String exam_status) {
		this.exam_status = exam_status;
	}

	public String getActiontype() {
		return actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
		if("0".equals(actiontype)){
			this.setActiontypes("分发");
		}else if("1".equals(actiontype)){
			this.setActiontypes("签收");
		}else {
			this.setActiontypes("未知");
		}
	}

	public String getActiontypes() {
		return actiontypes;
	}

	public void setActiontypes(String actiontypes) {
		this.actiontypes = actiontypes;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getChi_name() {
		return chi_name;
	}

	public void setChi_name(String chi_name) {
		this.chi_name = chi_name;
	}

	public String getExam_indicator() {
		return exam_indicator;
	}

	public void setExam_indicator(String exam_indicator) {
		this.exam_indicator = exam_indicator;
	}

	public String getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}

	public String getMc_no() {
		return mc_no;
	}

	public void setMc_no(String mc_no) {
		this.mc_no = mc_no;
	}

	public String getVisit_date() {
		return visit_date;
	}

	public void setVisit_date(String visit_date) {
		this.visit_date = visit_date;
	}

	public String getVisit_no() {
		return visit_no;
	}

	public void setVisit_no(String visit_no) {
		this.visit_no = visit_no;
	}

	public String getClinic_no() {
		return clinic_no;
	}

	public void setClinic_no(String clinic_no) {
		this.clinic_no = clinic_no;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getBatch_name() {
		return batch_name;
	}

	public void setBatch_name(String batch_name) {
		this.batch_name = batch_name;
	}

	public String getCheck_doctor() {
		return check_doctor;
	}

	public void setCheck_doctor(String check_doctor) {
		this.check_doctor = check_doctor;
	}

	public String getPicture_Path() {
		return picture_Path;
	}

	public void setPicture_Path(String picture_Path) {
		this.picture_Path = picture_Path;
	}

	public long getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(long batch_id) {
		this.batch_id = batch_id;
	}

	public long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getPacs() {
		return pacs;
	}

	public void setPacs(String pacs) {
		this.pacs = pacs;
	}

	public String getLis() {
		return lis;
	}

	public void setLis(String lis) {
		this.lis = lis;
	}

	public long getWpacs() {
		return wpacs;
	}

	public void setWpacs(long wpacs) {
		this.wpacs = wpacs;
	}

	public long getYpacs() {
		return ypacs;
	}

	public void setYpacs(long ypacs) {
		this.ypacs = ypacs;
	}

	public long getWlis() {
		return wlis;
	}

	public void setWlis(long wlis) {
		this.wlis = wlis;
	}

	public long getYlis() {
		return ylis;
	}

	public void setYlis(long ylis) {
		this.ylis = ylis;
	}

	public String getExam_times() {
		return exam_times;
	}

	public void setExam_times(String exam_times) {
		this.exam_times = exam_times;
	}

	public String getStatuss() {
		return statuss;
	}

	public void setStatuss(String statuss) {
		this.statuss = statuss;
	}

	public String getIs_need_barcodes() {
		return is_need_barcodes;
	}

	public void setIs_need_barcodes(String is_need_barcodes) {
		this.is_need_barcodes = is_need_barcodes;
	}

	public String getIs_need_guides() {
		return is_need_guides;
	}

	public void setIs_need_guides(String is_need_guides) {
		this.is_need_guides = is_need_guides;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getSet_name() {
		return set_name;
	}

	public void setSet_name(String set_name) {
		this.set_name = set_name;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getDep_name() {
		return dep_name;
	}

	public void setDep_name(String dep_name) {
		this.dep_name = dep_name;
	}

	public String getIs_need_barcode() {
		return is_need_barcode;
	}

	public void setIs_need_barcode(String is_need_barcode) {
		this.is_need_barcode = is_need_barcode;
		if("Y".equals(is_need_barcode)){
			this.setIs_need_barcodes("是");
		}else{
			this.setIs_need_barcodes("否");
		}
	}

	public String getIs_need_guide() {
		return is_need_guide;
	}

	public void setIs_need_guide(String is_need_guide) {
		this.is_need_guide = is_need_guide;
		if("Y".equals(is_need_guide)){
			this.setIs_need_guides("是");
		}else{
			this.setIs_need_guides("否");
		}
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getId_num() {
		return id_num;
	}

	public void setId_num(String id_num) {
		this.id_num = id_num;
	}

	public String getArch_num() {
		return arch_num;
	}

	public void setArch_num(String arch_num) {
		this.arch_num = arch_num;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}		

	public long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}

	public long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}

	public String getExam_num() {
		return exam_num;
	}

	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		if("Y".equals(status)){
			this.setStatuss("预约");
		}else if("D".equals(status)){
			this.setStatuss("登记");
		}else if("J".equals(status)){
			this.setStatuss("检查中");
		}else if("Z".equals(status)){
			this.setStatuss("已终检");
		}else{
			this.setStatuss("未知");
		}
	}

	public String getRegister_date() {
		return register_date;
	}

	public void setRegister_date(String register_date) {
		this.register_date = register_date;
	}

	public String getJoin_date() {
		return join_date;
	}

	public void setJoin_date(String join_date) {
		this.join_date = join_date;
	}

	public String getFinal_date() {
		return final_date;
	}

	public void setFinal_date(String final_date) {
		this.final_date = final_date;
	}

	public String getFinal_doctor() {
		return final_doctor;
	}

	public void setFinal_doctor(String final_doctor) {
		this.final_doctor = final_doctor;
	}		
	
	public String getExam_types() {
		return exam_types;
	}

	public void setExam_types(String exam_types) {
		this.exam_types = exam_types;
	}

	public String getExam_type() {
		return exam_type;
	}

	public void setExam_type(String exam_type) {
		this.exam_type = exam_type;
		if("T".equals(exam_type)){
			this.setExam_types("团检");
		}else if("G".equals(exam_type)){
			this.setExam_types("个检");
		}
	}

	public String getCustomer_type() {
		return customer_type;
	}

	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}

	public String getIs_sampled_directly() {
		return is_sampled_directly;
	}

	public void setIs_sampled_directly(String is_sampled_directly) {
		this.is_sampled_directly = is_sampled_directly;
	}

	public String getIs_adjusted() {
		return is_adjusted;
	}

	public void setIs_adjusted(String is_adjusted) {
		this.is_adjusted = is_adjusted;
	}

	public String getCenter_num() {
		return center_num;
	}

	public void setCenter_num(String center_num) {
		this.center_num = center_num;
	}

	public String getGetReportWay() {
		return getReportWay;
	}

	public void setGetReportWay(String getReportWay) {
		this.getReportWay = getReportWay;
	}
	
	public String getGetReportWays() {
		if("1".equals(getReportWay)) {
			getReportWays = "待邮寄";
		} else if("2".equals(getReportWay)) {
			getReportWays = "待自取";
		}
		return getReportWays;
	}

	public void setGetReportWays(String getReportWays) {
		this.getReportWays = getReportWays;
	}

	public String getReportAddress() {
		return reportAddress;
	}

	public void setReportAddress(String reportAddress) {
		this.reportAddress = reportAddress;
	}

	public String getChargingType() {
		return chargingType;
	}

	public void setChargingType(String chargingType) {
		this.chargingType = chargingType;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getGroup_index() {
		return group_index;
	}

	public void setGroup_index(String group_index) {
		this.group_index = group_index;
	}

	public String getIs_guide_back() {
		return is_guide_back;
	}

	public void setIs_guide_back(String is_guide_back) {
		this.is_guide_back = is_guide_back;
		if("N".equals(this.is_guide_back)){
			this.setIs_guide_backs("未");
		}else if("Y".equals(this.is_guide_back)){
			this.setIs_guide_backs("已");
		}
	}

	public String getIs_guide_backs() {
		return is_guide_backs;
	}

	public void setIs_guide_backs(String is_guide_backs) {
		this.is_guide_backs = is_guide_backs;
	}

	public String getCompany_check_status() {
		return company_check_status;
	}

	public void setCompany_check_status(String company_check_status) {
		this.company_check_status = company_check_status;
	}

	public long getCustomer_type_id() {
		return customer_type_id;
	}

	public void setCustomer_type_id(long customer_type_id) {
		this.customer_type_id = customer_type_id;
	}

	public String getIs_marriage() {
		return is_marriage;
	}

	public void setIs_marriage(String is_marriage) {
		this.is_marriage = is_marriage;
	}

	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String get_level() {
		return _level;
	}

	public void set_level(String _level) {
		this._level = _level;
	}

	public String getIs_after_pay() {
		return is_after_pay;
	}

	public void setIs_after_pay(String is_after_pay) {
		this.is_after_pay = is_after_pay;
	}

	public String getPast_medical_history() {
		return past_medical_history;
	}

	public void setPast_medical_history(String past_medical_history) {
		this.past_medical_history = past_medical_history;
	}

	public String getRemarke() {
		return remarke;
	}

	public void setRemarke(String remarke) {
		this.remarke = remarke;
	}

	public String getIntroducer() {
		return introducer;
	}

	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}

	public String getCounter_check() {
		return counter_check;
	}

	public void setCounter_check(String counter_check) {
		this.counter_check = counter_check;
		if("N".equals(counter_check)){
			this.setCounter_checks("否");
		}else{
			this.setCounter_checks("是");
		}
	}

	public String getGuide_nurse() {
		return guide_nurse;
	}

	public void setGuide_nurse(String guide_nurse) {
		this.guide_nurse = guide_nurse;
	}

	public String getAppointment() {
		return appointment;
	}

	public void setAppointment(String appointment) {
		this.appointment = appointment;
	}

	public String getData_source() {
		return data_source;
	}

	public void setData_source(String data_source) {
		this.data_source = data_source;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getIs_Active() {
		return is_Active;
	}

	public void setIs_Active(String is_Active) {
		this.is_Active = is_Active.trim();
		if("Y".equals(this.is_Active)){
			this.is_Actives="有效";
		}else if("N".equals(this.is_Active)){
			this.is_Actives="无效";
		}
	}

	public long getCreater() {
		return creater;
	}

	public void setCreater(long creater) {
		this.creater = creater;
	}

	public long getUpdater() {
		return updater;
	}

	public void setUpdater(long updater) {
		this.updater = updater;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getCounter_checks() {
		return counter_checks;
	}

	public void setCounter_checks(String counter_checks) {
		this.counter_checks = counter_checks;
	}

	public double getBudget_amount() {
		return budget_amount;
	}

	public void setBudget_amount(double budget_amount) {
		this.budget_amount = budget_amount;
	}

	public String getVisit_num() {
		return visit_num;
	}

	public void setVisit_num(String visit_num) {
		this.visit_num = visit_num;
	}

	public String getCustomer_feedback() {
		return customer_feedback;
	}

	public void setCustomer_feedback(String customer_feedback) {
		this.customer_feedback = customer_feedback;
	}

	public String getVisit_result() {
		return visit_result;
	}

	public void setVisit_result(String visit_result) {
		this.visit_result = visit_result;
	}

	public String getVisit_type() {
		return visit_type;
	}

	public void setVisit_type(String visit_type) {
		this.visit_type = visit_type;
	}

	public String getVisit_doctor() {
		return visit_doctor;
	}

	public void setVisit_doctor(String visit_doctor) {
		this.visit_doctor = visit_doctor;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVipflag() {
		return vipflag;
	}

	public void setVipflag(String vipflag) {
		this.vipflag = vipflag;
	}

	public String getRecordID() {
		return recordID;
	}

	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getP0creater() {
		return p0creater;
	}

	public void setP0creater(String p0creater) {
		this.p0creater = p0creater;
	}

	public String getP1creater() {
		return p1creater;
	}

	public void setP1creater(String p1creater) {
		this.p1creater = p1creater;
	}

	public long getAcccreater() {
		return acccreater;
	}

	public void setAcccreater(long acccreater) {
		this.acccreater = acccreater;
	}

	public long getSendcreater() {
		return sendcreater;
	}

	public void setSendcreater(long sendcreater) {
		this.sendcreater = sendcreater;
	}

	public String getSenddate() {
		return senddate;
	}

	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}

	public int getFlow_type() {
		return flow_type;
	}

	public void setFlow_type(int flow_type) {
		this.flow_type = flow_type;
		if(this.flow_type==0){
			this.flow_types="删除";
		}else if(this.flow_type==1){
			this.flow_types="正常";
		}
	}
	public String getFlow_types() {
		return flow_types;
	}
	public void setFlow_types(String flow_types) {
		this.flow_types = flow_types;
	}

	public String getFlow_name() {
		return flow_name;
	}

	public void setFlow_name(String flow_name) {
		this.flow_name = flow_name;
	}

	public String getSenduname() {
		return senduname;
	}

	public void setSenduname(String senduname) {
		this.senduname = senduname;
	}

	public String getAccuname() {
		return accuname;
	}

	public void setAccuname(String accuname) {
		this.accuname = accuname;
	}

	public String getReceive_type() {
		return receive_type;
	}

	public String getReceive_address() {
		return receive_address;
	}

	public void setReceive_address(String receive_address) {
		this.receive_address = receive_address;
	}
	
	public String getS0creater() {
		return s0creater;
	}

	public void setS0creater(String s0creater) {
		this.s0creater = s0creater;
	}

	public String getS1creater() {
		return s1creater;
	}

	public void setS1creater(String s1creater) {
		this.s1creater = s1creater;
	}

	public String getVtcreater() {
		return vtcreater;
	}

	public void setVtcreater(String vtcreater) {
		this.vtcreater = vtcreater;
	}
	
	public String getExam_results() {
		if("R".equals(exam_result)) {
			return "复查";
		} else if("Y".equals(exam_result)) {
			return "合格";
		} else if("N".equals(exam_result)) {
			return "不合格";
		} else if("O".equals(exam_result)) {
			return "其他";
		}
		return "合格";
	}

	public String getReceive_remark() {
		return receive_remark;
	}

	public void setReceive_remark(String receive_remark) {
		this.receive_remark = receive_remark;
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getE1creater() {
		return e1creater;
	}

	public void setE1creater(String e1creater) {
		this.e1creater = e1creater;
	}

	public String getReceive_name() {
		return receive_name;
	}

	public void setReceive_name(String receive_name) {
		this.receive_name = receive_name;
	}

	public String getReceive_date() {
		return receive_date;
	}

	public void setReceive_date(String receive_date) {
		this.receive_date = receive_date;
	}

	public String getEdesc() {
		return edesc;
	}

	public void setEdesc(String edesc) {
		this.edesc = edesc;
	}

	public String getExam_doctor() {
		return exam_doctor;
	}

	public void setExam_doctor(String exam_doctor) {
		this.exam_doctor = exam_doctor;
	}

	public String getExam_time() {
		return exam_time;
	}

	public void setExam_time(String exam_time) {
		this.exam_time = exam_time;
	}

	public String getReport_class() {
		return report_class;
	}

	public void setReport_class(String report_class) {
		this.report_class = report_class;
	}

	public int getTop_row() {
		return top_row;
	}

	public void setTop_row(int top_row) {
		this.top_row = top_row;
	}

	public String getDegreeOfedu() {
		return degreeOfedu;
	}

	public int getPolitical_status() {
		return political_status;
	}

	public void setDegreeOfedu(String degreeOfedu) {
		this.degreeOfedu = degreeOfedu;
	}

	public void setPolitical_status(int political_status) {
		this.political_status = political_status;
	}

	public String getMembership_card() {
		return membership_card;
	}

	public void setMembership_card(String membership_card) {
		this.membership_card = membership_card;
	}
	
	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}
	
	public String getJoin_operatorName() {
		return join_operatorName;
	}

	public void setJoin_operatorName(String join_operatorName) {
		this.join_operatorName = join_operatorName;
	}
	
	public long getEcd_id() {
		return ecd_id;
	}

	public void setEcd_id(long ecd_id) {
		this.ecd_id = ecd_id;
	}

	public String getDJD_path() {
		return DJD_path;
	}

	public void setDJD_path(String dJD_path) {
		DJD_path = dJD_path;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getReceive_phone() {
		return receive_phone;
	}

	public String getReceive_postcode() {
		return receive_postcode;
	}

	public void setReceive_phone(String receive_phone) {
		this.receive_phone = receive_phone;
	}

	public void setReceive_postcode(String receive_postcode) {
		this.receive_postcode = receive_postcode;
	}

	@Override
	public String toString() {
		return "ExamInfoUserDTOHS [id=" + id + ", group_id=" + group_id + ", customer_id=" + customer_id + ", exam_num="
				+ exam_num + ", status=" + status + ", register_date=" + register_date + ", join_date=" + join_date
				+ ", join_operator=" + join_operator + ", final_date=" + final_date + ", final_doctor=" + final_doctor
				+ ", check_doctor=" + check_doctor + ", exam_status=" + exam_status + ", exam_type=" + exam_type
				+ ", exam_types=" + exam_types + ", customer_type=" + customer_type + ", is_sampled_directly="
				+ is_sampled_directly + ", is_adjusted=" + is_adjusted + ", center_num=" + center_num
				+ ", getReportWay=" + getReportWay + ", getReportWays=" + getReportWays + ", reportAddress="
				+ reportAddress + ", report_class=" + report_class + ", chargingType=" + chargingType
				+ ", customerType=" + customerType + ", group_index=" + group_index + ", is_Active=" + is_Active
				+ ", is_Actives=" + is_Actives + ", creater=" + creater + ", create_time=" + create_time + ", updater="
				+ updater + ", update_time=" + update_time + ", is_guide_back=" + is_guide_back + ", is_guide_backs="
				+ is_guide_backs + ", company_check_status=" + company_check_status + ", customer_type_id="
				+ customer_type_id + ", customer_type_name=" + customer_type_name + ", is_marriage=" + is_marriage
				+ ", age=" + age + ", address=" + address + ", email=" + email + ", phone=" + phone + ", company="
				+ company + ", position=" + position + ", _level=" + _level + ", is_after_pay=" + is_after_pay
				+ ", past_medical_history=" + past_medical_history + ", remarke=" + remarke + ", introducer="
				+ introducer + ", degreeOfedu=" + degreeOfedu + ", political_status=" + political_status
				+ ", counter_check=" + counter_check + ", counter_checks=" + counter_checks + ", guide_nurse="
				+ guide_nurse + ", appointment=" + appointment + ", data_source=" + data_source + ", others=" + others
				+ ", order_id=" + order_id + ", user_name=" + user_name + ", sex=" + sex + ", arch_num=" + arch_num
				+ ", id_num=" + id_num + ", birthday=" + birthday + ", is_need_barcode=" + is_need_barcode
				+ ", is_need_guide=" + is_need_guide + ", is_need_barcodes=" + is_need_barcodes + ", is_need_guides="
				+ is_need_guides + ", dep_name=" + dep_name + ", group_name=" + group_name + ", set_name=" + set_name
				+ ", remark1=" + remark1 + ", statuss=" + statuss + ", exam_time=" + exam_time + ", exam_times="
				+ exam_times + ", wpacs=" + wpacs + ", ypacs=" + ypacs + ", wlis=" + wlis + ", ylis=" + ylis + ", pacs="
				+ pacs + ", lis=" + lis + ", nation=" + nation + ", batch_id=" + batch_id + ", batch_name=" + batch_name
				+ ", company_id=" + company_id + ", picture_Path=" + picture_Path + ", patient_id=" + patient_id
				+ ", chi_name=" + chi_name + ", employeeID=" + employeeID + ", medical_insurance_card="
				+ medical_insurance_card + ", mc_no=" + mc_no + ", visit_date=" + visit_date + ", visit_no=" + visit_no
				+ ", clinic_no=" + clinic_no + ", exam_indicator=" + exam_indicator + ", type_name=" + type_name
				+ ", actiontype=" + actiontype + ", actiontypes=" + actiontypes + ", isnotpay=" + isnotpay
				+ ", isprepay=" + isprepay + ", c_id=" + c_id + ", personal_pay=" + personal_pay + ", team_pay="
				+ team_pay + ", examcount=" + examcount + ", acc_num=" + acc_num + ", createdate=" + createdate
				+ ", printflag=" + printflag + ", printer=" + printer + ", printerName=" + printerName + ", printdate="
				+ printdate + ", flag=" + flag + ", freeze=" + freeze + ", freezename=" + freezename + ", occusectorid="
				+ occusectorid + ", read_status_str=" + read_status_str + ", read_status=" + read_status + ", bunk="
				+ bunk + ", allocationdate=" + allocationdate + ", receive_type=" + receive_type + ", disease_name="
				+ disease_name + ", c_name=" + c_name + ", u_name=" + u_name + ", exam_desc=" + exam_desc
				+ ", exam_result=" + exam_result + ", exam_doctor=" + exam_doctor + ", item_name=" + item_name
				+ ", data_name=" + data_name + ", marker=" + marker + ", mark_time=" + mark_time + ", item_number="
				+ item_number + ", item_num_s=" + item_num_s + ", budget_amount=" + budget_amount + ", idtypename="
				+ idtypename + ", visit_no_j=" + visit_no_j + ", tjlx=" + tjlx + ", idtype=" + idtype + ", is_djt_type="
				+ is_djt_type + ", deptname=" + deptname + ", marriage_age=" + marriage_age + ", h0=" + h0 + ", h1="
				+ h1 + ", h0date=" + h0date + ", h1date=" + h1date + ", strh0=" + strh0 + ", strh1=" + strh1 + ", s="
				+ s + ", s0=" + s0 + ", s1=" + s1 + ", sdate=" + sdate + ", s0date=" + s0date + ", s1date=" + s1date
				+ ", strs0=" + strs0 + ", strs1=" + strs1 + ", p0=" + p0 + ", p1=" + p1 + ", p0date=" + p0date
				+ ", p1date=" + p1date + ", strp0=" + strp0 + ", strp1=" + strp1 + ", edesc=" + edesc + ", e0=" + e0
				+ ", e1=" + e1 + ", e0date=" + e0date + ", e1date=" + e1date + ", stre0=" + stre0 + ", stre1=" + stre1
				+ ", e1creater=" + e1creater + ", z0=" + z0 + ", z1=" + z1 + ", strz0=" + strz0 + ", strz1=" + strz1
				+ ", z0date=" + z0date + ", z1date=" + z1date + ", c=" + c + ", cdate=" + cdate + ", f=" + f + ", strc="
				+ strc + ", strf=" + strf + ", fdate=" + fdate + ", z0user=" + z0user + ", z1user=" + z1user
				+ ", cuser=" + cuser + ", fuser=" + fuser + ", m=" + m + ", mdate=" + mdate + ", strm=" + strm
				+ ", vipflag=" + vipflag + ", visit_num=" + visit_num + ", customer_feedback=" + customer_feedback
				+ ", visit_result=" + visit_result + ", visit_type=" + visit_type + ", visit_doctor=" + visit_doctor
				+ ", remark=" + remark + ", recordID=" + recordID + ", kaidanren=" + kaidanren + ", kaidanrens="
				+ kaidanrens + ", wuxuzongjian=" + wuxuzongjian + ", num=" + num + ", p0creater=" + p0creater
				+ ", p1creater=" + p1creater + ", s0creater=" + s0creater + ", s1creater=" + s1creater + ", acccreater="
				+ acccreater + ", sendcreater=" + sendcreater + ", senddate=" + senddate + ", flow_type=" + flow_type
				+ ", flow_types=" + flow_types + ", flow_name=" + flow_name + ", senduname=" + senduname + ", accuname="
				+ accuname + ", is_report_print=" + is_report_print + ", is_report_print_y=" + is_report_print_y
				+ ", is_report_tidy=" + is_report_tidy + ", is_report_tidy_y=" + is_report_tidy_y + ", receive_type1="
				+ receive_type1 + ", receive_type_y=" + receive_type_y + ", receive_address=" + receive_address
				+ ", receive_name=" + receive_name + ", receive_date=" + receive_date + ", receive_remark="
				+ receive_remark + ", vtcreater=" + vtcreater + ", lc_remark=" + lc_remark + ", receive_phone="
				+ receive_phone + ", receive_postcode=" + receive_postcode + ", dept_id=" + dept_id + ", remark_time="
				+ remark_time + ", remark_user=" + remark_user + ", calculation_amount=" + calculation_amount
				+ ", calculation_rate=" + calculation_rate + ", v=" + v + ", v_name=" + v_name + ", t=" + t
				+ ", swuxuzongjian=" + swuxuzongjian + ", billdep=" + billdep + ", notices=" + notices
				+ ", isaffixedcode=" + isaffixedcode + ", isshowtable=" + isshowtable + ", isaffixedcodes="
				+ isaffixedcodes + ", isshowtables=" + isshowtables + ", customertypename=" + customertypename
				+ ", top_row=" + top_row + ", membership_card=" + membership_card + ", check_time=" + check_time
				+ ", pay_status=" + pay_status + ", weijian=" + weijian + ", join_operatorName=" + join_operatorName
				+ ", ecd_id=" + ecd_id + ", DJD_path=" + DJD_path + ", pay_amount=" + pay_amount + ", no_pay_amount="
				+ no_pay_amount + ", chargeType=" + chargeType + ", vipsigin=" + vipsigin + ", dep_num=" + dep_num
				+ "]";
	}

	
	
}