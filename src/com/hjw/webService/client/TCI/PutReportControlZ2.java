package com.hjw.webService.client.TCI;


import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.TCI.bean.TJjianCha;
import com.hjw.webService.client.TCI.bean.TJjianYan;
import com.hjw.webService.client.TCI.bean.TJkeshijc;
import com.hjw.webService.client.TCI.bean.TJxiaoJie;
import com.hjw.webService.client.TCI.bean.TJzongJian;
import com.hjw.webService.client.TCI.bean.TransmitData1;
import com.hjw.webService.client.TCI.bean.TransmitData2;
import com.hjw.webService.client.TCI.bean.TransmitResData;
import com.hjw.webService.client.TCI.bean.TransmitResData2;
import com.hjw.webService.client.TCI.client.ITCI;
import com.hjw.webService.client.TCI.client.TCIService;
import com.hjw.webService.client.TCI.client.TCIServiceLocator;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.PacsResItemBean;
import com.hjw.webService.job.bean.CommonExamDetailDTO;
import com.hjw.wst.DTO.DepExamResultDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExamResultDetailDTO;
import com.hjw.wst.DTO.ExaminfoDiseaseDTO;
import com.hjw.wst.DTO.ViewExamDetailDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class PutReportControlZ2 {
	private final String YIYUANBM = "915245";//
	private final String USER = "915245";//用户名
	private final String PWD = "915245";//密码
	private final String CODE = "HRC04_S_TJB";//交易编码
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	/**
	 * 
	 * @param web_url  http://192.26.3.63:9080/RHIN/TCIService?wsdl
	 * @param exam_num   体检号
	 * @param logName   handPutReport
	 * @return
	 */
	public ResultHeader getMessage(String web_url, String exam_num,long userid, String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
		+ "---------------------------------");
		
		Calendar deadline = Calendar.getInstance();
		deadline.set(2018, Calendar.NOVEMBER, 28, 0, 0, 0);
		if(new Date().after(deadline.getTime())) {
			ResultHeader resultHeader = new ResultHeader();
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			resultHeader.setTypeCode("AE");
			resultHeader.setText("接口已过期，请联系火箭蛙，截止日期："+df.format(deadline.getTime()));
			TranLogTxt.liswriteEror_to_txt(logName,"接口已过期，请联系火箭蛙，截止日期："+df.format(deadline.getTime()));
			return resultHeader;
		}
		
		ResultHeader resultHeader = new ResultHeader();
		try {
			TransmitData1 data1 = getTransmitData1(exam_num);
			JSONObject json = JSONObject.fromObject(data1);// 将java对象转换为json对象
			String str1 = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logName, "传输data1参数："+str1);
			
			TransmitData2 data2 = new TransmitData2();
			data2.setJiaoyi(CODE);
			data2.setYiyuanbh(YIYUANBM);
			JSONObject json2 = JSONObject.fromObject(data2);// 将java对象转换为json对象
			String str2 = json2.toString();// 将json对象转换为字符串
			
			TCIService tcis= new TCIServiceLocator(web_url);
			ITCI itci=tcis.getTCIPort();
			String result = itci.g_Apply(USER,PWD,CODE,str1,str2);
			TranLogTxt.liswriteEror_to_txt(logName, "返回参数："+result);
			JSONObject jsonobject = JSONObject.fromObject(result);
			Map classMap = new HashMap();
			classMap.put("resultlist", TransmitResData2.class);
			TransmitResData trans = (TransmitResData)JSONObject.toBean(jsonobject,TransmitResData.class,classMap);
			if(trans.getAppcode().equals("0")){//成功
				resultHeader.setTypeCode("AA");
				resultHeader.setText(trans.getDatabuffer());
				insertupload(exam_num,userid,logName);
			}else{
				resultHeader.setTypeCode("AE");
				resultHeader.setText(trans.getDatabuffer());
			}
		} catch (RemoteException e) {
			resultHeader.setTypeCode("AE");
			resultHeader.setText("调用接口出错");
			TranLogTxt.liswriteEror_to_txt(logName, "调用接口出错："+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		} catch (ServiceException e) {
			resultHeader.setTypeCode("AE");
			resultHeader.setText("调用接口出错");
			TranLogTxt.liswriteEror_to_txt(logName, "调用接口出错："+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		}
		return resultHeader;
	}
	
	private TransmitData1 getTransmitData1(String exam_num){
		TransmitData1 data1 = new TransmitData1();
		String sql = "select e.id,e.exam_num,e.age,c.id_num,c.user_name,c.sex,c.address,CONVERT(varchar(12),c.birthday,112) birthday"
				+ ",CONVERT(varchar(12),e.join_date,112) join_date,e.final_doctor,CONVERT(varchar(12),e.final_date,112) final_date,"
				+ "u.chi_name check_doctor,CONVERT(varchar(12),es.check_time,112) exam_times,es.final_exam_result remark1,es.update_time "
				+ "from exam_info e,customer_info c,exam_summary es left join user_usr u on u.id = es.check_doc where "
				+ "c.id = e.customer_id and es.exam_info_id = e.id and e.exam_num = '"+exam_num+"'";
		List<ExamInfoUserDTO> examinfolist = this.jdbcQueryManager.getList(sql, ExamInfoUserDTO.class);
		if(examinfolist.size() > 0){
			ExamInfoUserDTO examinfo = examinfolist.get(0);
			List<TJzongJian> tjzongjian = new ArrayList<TJzongJian>();
			TJzongJian zj = new TJzongJian();
			
			sql = "select ed.disease_name,ed.suggest from examinfo_disease ed where ed.exam_info_id = "+examinfo.getId()+" order by ed.disease_index";
			List<ExaminfoDiseaseDTO> diseaselist = this.jdbcQueryManager.getList(sql, ExaminfoDiseaseDTO.class);
			String zhujianxj = "";
			String zhujianyj = "";
			for(ExaminfoDiseaseDTO disease : diseaselist){
				zhujianxj += disease.getDisease_name() + "\r\n";
				zhujianyj += disease.getSuggest() + "\r\n";
			}
			zj.setLsh(YIYUANBM + examinfo.getExam_num());
			zj.setShenfenz(examinfo.getId_num());
			zj.setYiyuanbh(YIYUANBM);
			zj.setTijianxh(examinfo.getExam_num());
			zj.setXingming(examinfo.getUser_name());
			if("男".equals(examinfo.getSex())){
				zj.setXingbie("1");
			}else{
				zj.setXingbie("2");
			}
			zj.setNianling(examinfo.getAge()+"");
			zj.setZongjianbh("");
			zj.setZhujianys(examinfo.getFinal_doctor());
			zj.setZhujianrq(examinfo.getFinal_date());
			zj.setShenheys(examinfo.getCheck_doctor());
			zj.setShenherq(examinfo.getExam_times());
			zj.setFafangz("");
			zj.setFafangrq("");
			zj.setZhujianzd(examinfo.getRemark1());
			zj.setZhujianxj(zhujianxj);
			zj.setZhujianyj(zhujianyj);
			zj.setJiatingdz(examinfo.getAddress());
			zj.setTijianlx("");
			zj.setChushengrq(examinfo.getBirthday());
			zj.setJilusj(convetDate(examinfo.getUpdate_time()));
			zj.setKahao("");
			zj.setKalx("");
			zj.setTijianrq(examinfo.getJoin_date());
			zj.setBaojianhao("");
			tjzongjian.add(zj);
			data1.setTjzongjian(tjzongjian); //封装总检信息
			
			List<TJxiaoJie> tjxiaojie = new ArrayList<TJxiaoJie>();
			sql = "select ed.id,ed.exam_result_summary exam_result,d.dep_num,d.dep_inter_num dep"
				+ ",d.dep_name,ed.exam_doctor,CONVERT(varchar(12),ed.create_time,112) exam_date,ed.create_time exam_status "
				+ "from exam_dep_result ed,department_dep d where ed.dep_id = d.id and ed.exam_info_id = "+examinfo.getId()+" order by d.seq_code";
			List<DepExamResultDTO> deplist = this.jdbcQueryManager.getList(sql, DepExamResultDTO.class);
			for(DepExamResultDTO depresult : deplist){
				TJxiaoJie xiaoJie = new TJxiaoJie();
				xiaoJie.setLsh(YIYUANBM + depresult.getId());
				xiaoJie.setShenfenz(examinfo.getId_num());
				xiaoJie.setYiyuanbh(YIYUANBM);
				xiaoJie.setTijianxjbh(depresult.getId()+"");
				xiaoJie.setTijianxh(examinfo.getExam_num());
				xiaoJie.setKeshibm(depresult.getDep_num());
				xiaoJie.setKeshimc(depresult.getDep_name());
				xiaoJie.setKeshixj(depresult.getExam_result());
				xiaoJie.setTijianrq(depresult.getExam_date());
				xiaoJie.setTijianys(depresult.getExam_doctor());
				xiaoJie.setYichangms("");
				xiaoJie.setJilusj(convetDate(depresult.getExam_status()));
				tjxiaojie.add(xiaoJie);
			}
			data1.setTjxiaojie(tjxiaojie); //封装科室小结信息
			
			List<TJkeshijc> tjkeshijc = new ArrayList<TJkeshijc>();
			sql = "select c.id,d.dep_num,d.dep_name,e.item_name,e.item_num,c.health_level,c.exam_result,c.exam_doctor,"
					+ "CONVERT(varchar(12),c.exam_date,112) exam_date,d.seq_code dep_id,e.seq_code,e.item_unit,c.create_time "
					+ "from common_exam_detail c,examination_item e,charging_item_exam_item ce,charging_item ci,department_dep d,examinfo_charging_item ec "
					+ "where c.exam_item_id = e.id and e.id = ce.exam_item_id and ce.charging_item_id = ci.id and ci.dep_id = d.id "
					+ "and ec.examinfo_id = c.exam_info_id and ec.charge_item_id = ci.id and ec.isActive = 'Y' and ec.pay_status <> 'M' "
					+ "and c.exam_info_id = "+examinfo.getId() +" order by d.seq_code,ci.item_seq,e.seq_code";
			List<CommonExamDetailDTO> detailList = this.jdbcQueryManager.getList(sql, CommonExamDetailDTO.class);
			for(CommonExamDetailDTO detail : detailList){
				TJkeshijc keshijc = new TJkeshijc();
				
				keshijc.setLsh(YIYUANBM + detail.getId());
				keshijc.setShenfenz(examinfo.getId_num());
				keshijc.setYiyuanbh(YIYUANBM);
				keshijc.setTijianxh(examinfo.getExam_num());
				keshijc.setTijiansb(detail.getId()+"");
				keshijc.setKeshibh(detail.getDep_num());
				keshijc.setKeshimc(detail.getDep_name());
				keshijc.setXiangmubh(detail.getItem_num());
				keshijc.setXiangmumc(detail.getItem_name());
				if("Z".equals(detail.getHealth_level())){
					keshijc.setYichangbz("0");
				}else{
					keshijc.setYichangbz("1");
				}
				keshijc.setTijianjg(detail.getExam_result());
				keshijc.setJianchays(detail.getExam_doctor());
				keshijc.setJiancharq(detail.getExam_date());
				keshijc.setKeshixssx(detail.getDep_id());
				keshijc.setXiangmuxssx(detail.getSeq_code());
				keshijc.setTijianjgdw(detail.getItem_unit());
				keshijc.setJilusj(convetDate(detail.getCreate_time()));
				tjkeshijc.add(keshijc);
			}
			data1.setTjkeshijc(tjkeshijc);//封装科室检查信息
			
			List<TJjianYan> tjjianyan = new ArrayList<TJjianYan>();
			sql = "select r.id,d.dep_num,d.dep_name,ci.item_code c_item_code,ci.item_name c_item_name,s.sample_barcode demo_name,e.item_name"
				+ ",e.item_num item_code,r.item_unit,r.ref_indicator,r.ref_value,r.exam_result,r.exam_doctor,CONVERT(varchar(12),r.exam_date,112) "
				+ "exam_date,r.approver,CONVERT(varchar(12),r.approve_date,112) approve_date,d.seq_code d_seq_code,ci.item_seq c_seq_code,e.seq_code e_seq_code,r.create_time "
				+ "from exam_result_detail r,examination_item e,examResult_chargingItem er,charging_item ci,department_dep d,sample_exam_detail s "
				+ "where r.exam_item_id = e.id and r.id = er.exam_id and er.result_type = 'exam' and er.charging_id = ci.id "
				+ "and ci.dep_id = d.id and s.exam_info_id = r.exam_info_id and s.sample_id = ci.sam_demo_id and r.exam_info_id =  "+examinfo.getId()
				+ " order by d.seq_code,ci.item_seq,e.seq_code";
			List<ExamResultDetailDTO> examresultList = this.jdbcQueryManager.getList(sql, ExamResultDetailDTO.class);
			for(ExamResultDetailDTO result : examresultList){
				TJjianYan jianYan = new TJjianYan();
				jianYan.setLsh(YIYUANBM + result.getId());
				jianYan.setShenfenz(examinfo.getId_num());
				jianYan.setYiyuanbh(YIYUANBM);
				jianYan.setTijianxh(examinfo.getExam_num());
				jianYan.setKeshibm(result.getDep_num());
				jianYan.setKeshimc(result.getDep_name());
				jianYan.setZuhebh(result.getC_item_code());
				jianYan.setZuhemc(result.getC_item_name());
				jianYan.setYangbenbh(result.getDemo_name());
				jianYan.setGuanchabh(result.getId()+"");
				jianYan.setGuanchamc(result.getItem_name());
				jianYan.setJiliangdw(result.getItem_unit());
				if("1".equals(result.getRef_indicator())){
					jianYan.setTishi("↑");
				}else if("2".equals(result.getRef_indicator())){
					jianYan.setTishi("↓");
				}
				jianYan.setJianyanjg(result.getExam_result());
				jianYan.setCankaofw(result.getRef_value());
				jianYan.setJianyanys(result.getExam_doctor());
				jianYan.setJianyanrq(result.getExam_date());
				jianYan.setShenheys(result.getApprover());
				jianYan.setShenherq(result.getApprove_date());
				jianYan.setKeshixssx(result.getD_seq_code());
				jianYan.setZuhexssx(result.getC_seq_code());
				jianYan.setShiyanxssx(result.getE_seq_code());
				jianYan.setJilusj(convetDate(result.getCreate_time()));
				jianYan.setGuanchabm(result.getItem_code());
				tjjianyan.add(jianYan);
			}
			data1.setTjjianyan(tjjianyan);
			
			List<TJjianCha> tjjiancha = new ArrayList<TJjianCha>();
			sql = "select v.id,d.dep_num,d.dep_name,c.item_name,c.item_code,v.exam_desc,v.exam_result,v.exam_doctor,CONVERT(varchar(12),v.exam_date,112) exam_date,"
				+ "v.approver,CONVERT(varchar(12),v.approve_date,112) approve_date,d.seq_code d_seq_code,c.item_seq c_seq_code,v.create_time from view_exam_detail v,"
				+ "pacs_summary ps,pacs_detail pd,charging_item c,department_dep d where v.pacs_id = ps.id and ps.id = pd.summary_id "
				+ "and pd.chargingItem_num = c.item_code and c.dep_id = d.id and ps.examinfo_num = '"+exam_num+"' "
				+ "order by d.seq_code,c.item_seq";
			List<ViewExamDetailDTO> viewList = this.jdbcQueryManager.getList(sql, ViewExamDetailDTO.class);
			for(ViewExamDetailDTO view : viewList){
				TJjianCha jianCha = new TJjianCha();
				jianCha.setLsh(YIYUANBM + view.getId());
				jianCha.setShenfenz(examinfo.getId_num());
				jianCha.setYiyuanbh(YIYUANBM);
				jianCha.setTijianxh(examinfo.getExam_num());
				jianCha.setJianchasb(view.getId()+"");
				jianCha.setKeshibm(view.getDep_num());
				jianCha.setKeshimc(view.getDep_name());
				jianCha.setXiangmubh(view.getItem_code());
				jianCha.setXiangmumc(view.getItem_name());
				jianCha.setJianchajgsj(view.getExam_desc());
				jianCha.setJianchajl(view.getExam_result());
				jianCha.setJianchays(view.getExam_doctor());
				jianCha.setJiancharq(view.getExam_date());
				jianCha.setShenheys(view.getApprover());
				jianCha.setShenherq(view.getApprove_date());
				jianCha.setKeshixssx(view.getD_seq_code());
				jianCha.setXiangmusxsx(view.getC_seq_code());
				jianCha.setBeizhu("");
				jianCha.setJilusj(convetDate(view.getCreate_time()));
				tjjiancha.add(jianCha);
			}
			data1.setTjjiancha(tjjiancha);
		}
		return data1;
	}
	
	private void insertupload(String exam_num,long userid,String logname){
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String insertsql = "insert into z2_report_upload(exam_num,upload_date,upload_user) "
					+ "values('" + exam_num + "','" + DateTimeUtil.getDateTime() + "','"+userid+"')";
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +insertsql);
				tjtmpconnect.createStatement().executeUpdate(insertsql);
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
	private String convetDate(String date){
		String str = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = sdf.parse(date);
			SimpleDateFormat sdfs = new SimpleDateFormat("yyyyMMdd HHmmss");
			str = sdfs.format(d);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return str;
	}
}
