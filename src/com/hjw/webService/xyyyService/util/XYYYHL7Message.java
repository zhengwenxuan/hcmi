package com.hjw.webService.xyyyService.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.SqlServerDatabaseSource;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.bjxy.util.QuerySqlData;
import com.hjw.wst.DTO.ProcListResult;
import com.hjw.wst.DTO.ProcPacsResult;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.GenericMessage;
import ca.uhn.hl7v2.model.GenericSegment;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v251.datatype.CE;
import ca.uhn.hl7v2.model.v251.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v251.group.OUL_R24_ORDER;
import ca.uhn.hl7v2.model.v251.message.ADT_A05;
import ca.uhn.hl7v2.model.v251.message.DFT_P03;
import ca.uhn.hl7v2.model.v251.message.OMG_O19;
import ca.uhn.hl7v2.model.v251.message.ORU_R01;
import ca.uhn.hl7v2.model.v251.message.OUL_R24;
import ca.uhn.hl7v2.model.v251.segment.MFI;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.OBX;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

public class XYYYHL7Message {
	
	public static String MFNM14HL7(String str,String logname){
		Parser p = new GenericParser();
		Message msg;
		String res="";
		String rest="";
		String messageId="";
		String strs="";
		try {
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			MSH msh=(MSH) adt.get("MSH");
			MFI mfi=(MFI) adt.get("MFI");
			messageId=msh.getMessageControlID().getValue();
			CE master=mfi.getMasterFileIdentifier();
			 strs=master.getCe1_Identifier().getValue();
			switch (strs) {
			case "ZDT"://4.6.科室字典段
				rest=ZDTHL7(str,logname);
				break;
			case "ZDC"://4.7.ZDC - 科室分类字典段
				rest=ZDCHL7(str,logname);
				break;
			case "ZCI"://4.8.ZCI - 价表字典段
				rest=ZCIHL7(str,logname);
				break;
			case "ZCC"://4.9.ZCC - 收费项目分类字典段
				rest=ZCCHL7(str,logname);
				break;
			case "ZFC"://4.10.ZFC - 财务核算分类字典段
				rest=ZFCHL7(str,logname);
				break;
			case "ZOB"://4.11.ZOB - 门诊账单分类字典段
				rest=ZOBHL7(str,logname);
				break;
			case "ZCG"://4.12.ZCG - 收费项目分组字典段
				rest=ZCGHL7(str,logname);
				break;
			case "ZIB"://4.13.ZIB - 住院账单分类字典段
				rest=ZIBHL7(str,logname);
				break;
			case "ZMB"://4.14.ZMB - 病案费用分类字典段
				rest=ZMBHL7(str,logname);
				break;
			case "ZEM"://4.15.ZEM - 人员字典段
				rest=ZEMHL7(str,logname);
				break;
			case "ZLI"://4.16.ZLI - 检验项目字典段
				rest=ZLIHL7(str,logname);
			case "ZLF"://4.17.ZLF - 检验项目费用对照段
				rest=ZLFHL7(str,logname);
				break;
			case "ZLC"://4.18.ZLC - 检验项目与类别对应段
				rest=ZLCHL7(str,logname);
				break;
			case "ZLS"://4.19.ZLS - 检验类别（样本）字典段
				rest=ZLSHL7(str,logname);
				break;
			case "ZLV"://4.20.ZLV - 容器类型字典段
				rest=ZLVHL7(str,logname);
				break;
			case "ZST"://4.21.ZST - 标本类型字典段
				rest=ZSTHL7(str,logname);
				break;
			case "ZLD"://4.22.ZLD - 检验组合项目字典段
				rest=ZLDHL7(str,logname);
				break;
			case "ZLR"://4.23.ZLR - 检验项目参考值字典段
				rest=ZLRHL7(str,logname);
				break;
			case "ZAC"://4.24.ZAC - 检验申请类型主表字典段
				rest=ZACHL7(str,logname);
				break;
			case "ZAG"://4.25.ZAG - 检验申请分组字典段
				rest=ZAGHL7(str,logname);
				break;
			case "ZAD"://4.26.ZAD - 检验申请类型副表字典段
				rest=ZADHL7(str,logname);
				break;
			case "ZET"://4.27.ZET - 检查项目大类字典段
				rest=ZETHL7(str,logname);
				break;
			case "ZES"://4.28.ZES - 检查项目小类字典段
				rest=ZESHL7(str,logname);
				break;
			case "ZEC"://4.29.ZEC - 检查类型字典段
				rest=ZECHL7(str,logname);
				break;
			case "ZEF"://4.30.ZEF - 检查项目费用对照字典段
				rest=ZEFHL7(str,logname);
				break;
			case "ZOI"://医嘱字典
				rest=ZOIHL7(str,logname);
				break;
			case "ZOC"://医嘱收费字典
				rest=ZOCHL7(str,logname);
				break;
			case "ZXU"://xt_user
				rest=ZXUHL7(str,logname);
				break;
			default:
				break;
			}
			 res=XYYYMsgUtil.ACKDICT("MFK", "M14", messageId,"AA",strs);
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			res=XYYYMsgUtil.ACKDICT("MFK", "M14", messageId,"AE",strs);
		}
		
		return res;
	}
	private static String ZLIHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"apply_item","item_name","item_name_a","item_pycode","item_dcode","deleted_flag","itemclass"};
			String tableName="xy_zd_jy_apply_item";
			String[] indexFields={"apply_item"};
			String[] feiindexFields={"item_name","item_name_a","item_pycode","item_dcode","deleted_flag","itemclass"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZLI");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZEMHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"emp_sn","code","name","py_code","d_code","dept_sn","dept_sn_2","emp_po_code","emp_tit_code","prof_type","ifcadre",
					"mark","deleted_flag","dept_sn_3","clinic_type","order_flag","tj_type","social_no","comment","tj_dept_sn","tj_emp_tit_code",
					"yb_code","image","gh_sf","nutrition_class","sex","Op_level","speciality"};
			String tableName="xy_a_employee_mi";
			String[] indexFields={"emp_sn"};
			String[] feiindexFields={"code","name","py_code","d_code","dept_sn","dept_sn_2","emp_po_code","emp_tit_code","prof_type","ifcadre",
					"mark","deleted_flag","dept_sn_3","clinic_type","order_flag","tj_type","social_no","comment","tj_dept_sn","tj_emp_tit_code",
					"yb_code","image","gh_sf","nutrition_class","sex","Op_level","speciality"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZEM");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZLFHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"item_code","charge_code","sort_no","is_primary"};
			String tableName="xy_zd_jy_item_charge";
			String[] indexFields={"item_code","charge_code"};
			String[] feiindexFields={"sort_no","is_primary"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZLF");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZLCHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"item_code","itemclass_code","sort_no","is_primary"};
			String tableName="xy_zd_jy_item_itemclass";
			String[] indexFields={"item_code","itemclass_code"};
			String[] feiindexFields={"sort_no","is_primary"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZLC");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZLSHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"itemclass_code","itemclass_name","vessel_type","color_desc","color_code","sort_no","samp_type","collect_desc"};
			String tableName="xy_zd_jy_itemclass";
			String[] indexFields={"itemclass_code"};
			String[] feiindexFields={"itemclass_name","vessel_type","color_desc","color_code","sort_no","samp_type","collect_desc"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZLS");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZLVHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"code","name","py_code","d_code"};
			String tableName="xy_zd_jy_vessel_type";
			String[] indexFields={"code"};
			String[] feiindexFields={"name","py_code","d_code"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZLV");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZSTHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"samp_type","samp_name","manual_item","list_flag","exp_id"};
			String tableName="xy_zd_jy_samp_type";
			String[] indexFields={"samp_type"};
			String[] feiindexFields={"samp_name","manual_item","list_flag","exp_id"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZST");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZLDHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"apply_item","detail_item","item_sort","item_qty"};
			String tableName="xy_zd_jy_apply_detail";
			String[] indexFields={"apply_item","detail_item"};
			String[] feiindexFields={"item_sort","item_qty"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZLD");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	//检验申请类型主表字典段
	private static String ZACHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"code","name","title","diagclass_title","comment","pycode","dcode","use_status","samp_type","exec_unit","show_sort","apply_mode","apply_group"};
			String tableName="xy_zd_jy_applyclass";
			String[] indexFields={"code"};
			String[] feiindexFields={"name","title","diagclass_title","comment","pycode","dcode","use_status","samp_type","exec_unit","show_sort","apply_mode","apply_group"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZAC");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZLRHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"item_code","sex","age_segment","samp_type","dnlimit","uplimit","cllimit","chlimit","temp"};
			String tableName="xy_zd_jy_item_range";
			String[] indexFields={"item_code","sex","age_segment","samp_type"};
			String[] feiindexFields={"dnlimit","uplimit","cllimit","chlimit","temp"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZLR");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	//检验申请分组字典段
	private static String ZAGHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"code","name","py_code","d_code","deleted_flag","info_data"};
			String tableName="xy_zd_jy_apply_group";
			String[] indexFields={"code"};
			String[] feiindexFields={"name","py_code","d_code","deleted_flag","info_data"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZAG");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	//检验申请类型副表字典段
	private static String ZADHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"apclass_code","patient_type","apply_item","item_sort","addition_type","apply_group"};
			String tableName="xy_zd_jy_applyclass_detail";
			String[] indexFields={"apclass_code","patient_type","apply_item"};
			String[] feiindexFields={"item_sort","addition_type","apply_group"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZAD");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	
	private static String ZECHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"code","name","py_code","d_code","sort_no"};
			String tableName="xy_jc_zd_class";
			String[] indexFields={"code"};
			String[] feiindexFields={"name","py_code","d_code","sort_no"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZEC");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZESHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"type_code","sub_code","name","py_code","d_code","deleted_flag","sort_no","class",
					"exec_unit","yz_order_code"};
			String tableName="xy_jc_zd_exam_sub_type";
			String[] indexFields={"type_code","sub_code"};
			String[] feiindexFields={"name","py_code","d_code","deleted_flag","sort_no","class",
					"exec_unit","yz_order_code"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZES");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZETHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"code","name","py_code","d_code","deleted_flag","sort_no"};
			String tableName="xy_jc_zd_exam_type";
			String[] indexFields={"code"};
			String[] feiindexFields={"name","py_code","d_code","deleted_flag","sort_no"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZET");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZEFHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"type_code","sub_code","charge_code","amount","serial_no","group_no","charge_price"};
			String tableName="xy_jc_exam_charge";
			String[] indexFields={"type_code","sub_code","charge_code"};
			String[] feiindexFields={"amount","serial_no","group_no","charge_price"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZEF");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZOIHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"order_code","order_name","d_code","deleted_flag","discription","exclu_group_type","order_type","py_code"};
			String tableName="xy_yz_order_item";
			String[] indexFields={"order_code"};
			String[] feiindexFields={"order_name","d_code","deleted_flag","discription","exclu_group_type","order_type","py_code"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZOI");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZOCHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"amount","charge_code","order_code"};
			String tableName="xy_yz_order_charge";
			String[] indexFields={"charge_code","order_code"};
			String[] feiindexFields={"amount"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZEF");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZXUHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"user_name","subsys_id","pass_word","user_mi"};
			String tableName="xy_xt_user";
			String[] indexFields={"subsys_id","user_name"};
			String[] feiindexFields={"pass_word","user_mi"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZXU");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZMBHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"code","d_code","name","py_code"};
			String tableName="xy_ba_zd_charge_second";
			String[] indexFields={"code"};
			String[] feiindexFields={"d_code","name","py_code"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZMB");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZIBHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"charge_code","code","cw_code","cw_name","d_code","name","py_code","seq"};
			String tableName="xy_zy_bill_item";
			String[] indexFields={"code"};
			String[] feiindexFields={"charge_code","cw_code","cw_name","d_code","name","py_code","seq"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZIB");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZCGHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"code","d_code","deleted_flag","name","py_code"};
			String tableName="xy_zd_charge_group";
			String[] indexFields={"code"};
			String[] feiindexFields={"d_code","deleted_flag","name","py_code"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZCG");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZOBHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"code","cw_code","cw_name","d_code","delete_flag","name","py_code","sort_order"};
			String tableName="xy_mz_bill_item";
			String[] indexFields={"code"};
			String[] feiindexFields={"cw_code","cw_name","d_code","delete_flag","name","py_code","sort_order"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZOB");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZFCHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"code","d_code","deleted_flag","in_out_flag","name","py_code"};
			String tableName="xy_zy_audit2_item";
			String[] indexFields={"code"};
			String[] feiindexFields={"d_code","deleted_flag","in_out_flag","name","py_code"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZFC");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZCCHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"code","d_code","deleted_flag","in_out_flag","name","py_code"};
			String tableName="xy_zy_audit_item";
			String[] indexFields={"code"};
			String[] feiindexFields={"d_code","deleted_flag","in_out_flag","name","py_code"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZCC");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZCIHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"code","name","print_name","py_code","d_code","exec_unit","audit_code","audit_code2","charge_price","charge_unit",
					"zy_bill_item","mz_bill_item","zy_mz_flag","percentag1","percentag2","percentag3","percentag4","percentag5","amount1",
					"amount2","amount3","amount4","amount5","self_flag","separate_flag","suprice_flag","zy_charge_group","mz_charge_group",
					"charge_class","zy_confirm_flag","mz_confirm_flag","comment","deleted_flag","second_code"};
			String tableName="xy_zd_charge_item";
			String[] indexFields={"code"};
			String[] feiindexFields={"name","print_name","py_code","d_code","exec_unit","audit_code","audit_code2","charge_price","charge_unit",
					"zy_bill_item","mz_bill_item","zy_mz_flag","percentag1","percentag2","percentag3","percentag4","percentag5","amount1",
					"amount2","amount3","amount4","amount5","self_flag","separate_flag","suprice_flag","zy_charge_group","mz_charge_group",
					"charge_class","zy_confirm_flag","mz_confirm_flag","comment","deleted_flag","second_code"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZCI");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String ZDCHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"code","d_code","deleted_flag","name","py_code"};
			String tableName="xy_zd_unit_flag";
			String[] indexFields={"code"};
			String[] feiindexFields={"d_code","deleted_flag","name","py_code"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZDC");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String commonSql(GenericSegment gs,String[] fields,String tableName,String[] indexFields,String[] feiindexFields,String logname){
		String[] fieldValues=new String[fields.length];
		String result="";
		try {
		for(int i=0;i<fields.length;i++){
				if(gs.getField(i+1).length!=0){
					fieldValues[i]=((Varies) gs.getField(i+1)[0]).getData().toString();
					if(fieldValues[i].contains("GenericComposite")){
						fieldValues[i]=fieldValues[i].substring(17, fieldValues[i].indexOf("^"));
					}
				}else{
					fieldValues[i]="";
				}
		}
		StringBuilder sb=new StringBuilder();
		sb.append(" merge into "+tableName+" p using ");
		sb.append(" (select ");
		for(int i=0;i<fields.length;i++){
			sb.append(" '"+fieldValues[i]+"' as "+fields[i]+" , ");
			
		}
		sb.append(" '"+DateTimeUtil.getDateTime()+"' as action_date , 0 as action_flag ) np on ( ");
		for(int j=0;j<indexFields.length;j++){
			sb.append(" p."+indexFields[j]+"=np."+indexFields[j]+"   ");
			if(j+1!=indexFields.length){
				sb.append(" and ");
			}
		}
		sb.append("  )  WHEN MATCHED THEN UPDATE set");
		for(int k=0;k<feiindexFields.length;k++){
			sb.append(" p."+feiindexFields[k]+"=np."+feiindexFields[k]+" , ");
			
		}
		sb.append(" p.action_date=np.action_date , p.action_flag=np.action_flag  WHEN NOT MATCHED THEN INSERT (");
		for(int x=0;x<fields.length;x++){
			sb.append(" "+fields[x]+" , ");
			
		}
		sb.append(" action_date,action_flag) values ( ");
		for(int y=0;y<fields.length;y++){
			sb.append(" np."+fields[y]+" ,");
			
		}
		sb.append(" np.action_date,np.action_flag) ;");
		TranLogTxt.liswriteEror_to_txt(logname,sb.toString());
		result=runSql(sb.toString(),logname);
		} catch (HL7Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		return result;
	}
	private static String runSql(String sql,String logname){
		WebApplicationContext wac =ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		Connection connection = null;
		try {
		    connection = jdbcQueryManager.getConnection();
			int rs = connection.createStatement().executeUpdate(sql);
			return "1";
		} catch (SQLException e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			return "0";
		}finally{
			try {
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	private static String queryExamNum(String patient_id,String logname) throws ServiceException, SQLException {
		WebApplicationContext wac =ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql="select exam_num from exam_info where patient_id='"+patient_id+"' and is_Active = 'Y' and status != 'Z' ";
		Connection connection = null;
		Statement statement = null;
		String result="";
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			TranLogTxt.liswriteEror_to_txt(logname,"\r\n sql:"+sql);
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				if(rs.getString("exam_num")==null||rs.getString("exam_num").length()==0){
					result="";	
				}else{
					result=rs.getString("exam_num");
				}
			}
			rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
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
		return result;
	}
	private static Boolean querySampleId(String sample_id,String logname) throws ServiceException, SQLException {
		Boolean query=true;
		WebApplicationContext wac =ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql="select ";
		Connection connection = null;
		Statement statement = null;
		String result="";
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if (!rs.next()) {
				query=false;
			}
			rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
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
		return query;
	}
	
	private static String ZDTHL7(String str,String logname){
		String res="";
		Parser p = new GenericParser();
		Message msg;
		try {
			String[] fields={"standard_code","standard_name","code","name","unit_sn","abbname","py_code","dept_class",
				"virtual_flag","deleted_flag","mz_flag","pc_flag","comment","comment1","comment2","yb_dept_code","yb_dept_name",
					"d_code","show_dept_flag","special_flag","gh_max_yy","adt_dept_no","OA_no","OA_code","OA_name","mz_sequence_no","extend_code","abbcode","yb_unit_code"};
			String tableName="xy_zd_unit_code";
			String[] indexFields={"unit_sn"};
			String[] feiindexFields={"standard_code","standard_name","code","name","abbname","py_code","dept_class",
				"virtual_flag","deleted_flag","mz_flag","pc_flag","comment","comment1","comment2","yb_dept_code","yb_dept_name",
					"d_code","show_dept_flag","special_flag","gh_max_yy","adt_dept_no","OA_no","OA_code","OA_name","mz_sequence_no","extend_code","abbcode","yb_unit_code"};
			msg=p.parse(str);
			GenericMessage adt=(GenericMessage) msg;
			Structure[] st=adt.getAll("ZDT");
			for(int i=0;i<st.length;i++){
				GenericSegment gs= (GenericSegment) st[i];
				String result=commonSql(gs, fields, tableName, indexFields, feiindexFields,logname);
				if(result.equals("0")){
					res="AE";
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		if(!res.equals("AE")){
			res="AA";
		}
		return res;
	}
	private static String dateutilto(String date){
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8)+" "+date.substring(8,10)+":"+date.substring(10,12)+":"+date.substring(12,14);
	}
	
	/**
	 * 解析lis
	 * @param str
	 * @param logname
	 * @return
	 */
	public static String OULR24HL7(String str,String logname){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		CommService commService = (CommService) wac.getBean("commService");
		Parser p = new GenericParser();
		Message msg;
		String res="";
		String messageId="";
		try {
			msg=p.parse(str);
			OUL_R24 adt=(OUL_R24) msg;
			MSH msh=adt.getMSH();
			messageId=msh.getMessageControlID().getValue();
			PID pid=adt.getPATIENT().getPID();
			String patient_id=pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().getValue();
			String exam_num = queryExamNum(patient_id,logname);
			List<OUL_R24_ORDER> orderList=adt.getORDERAll();
			boolean flagss = true;
			if(exam_num!=null&&exam_num.length()>0){
				TranLogTxt.liswriteEror_to_txt("lisproc_success_jieguo", "res:检验结果解析数据1" +"\r\n");
				for(int i=0;i<orderList.size();i++){
					try{
					OBR obr=orderList.get(i).getOBR();
//					String bar_code=obr.getObr3_FillerOrderNumber().getEi1_EntityIdentifier().getValue();
					String bar_code = orderList.get(i).getSPECIMEN(0).getSPM().getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi3_UniversalID().getValue();
					String shifouyouzhi=QuerySqlData.getShiFouCunZai(exam_num, bar_code,logname);
					TranLogTxt.liswriteEror_to_txt("lisproc_success_jieguo", "res:检验结果解析数据2" +shifouyouzhi+"\r\n");
					if(shifouyouzhi.equals("1")){
						String lis_item_code=obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().getValue();
//						String exam_doctor=obr.getObr32_PrincipalResultInterpreter().getNdl1_Name().getCnn2_FamilyName().getValue();
						String exam_doctor=obr.getObr34_Technician(0).getNdl1_Name().getCnn2_FamilyName().getValue();
						String exam_date=obr.getObr22_ResultsRptStatusChngDateTime().getTime().getValue();
						String approver=obr.getObr33_AssistantResultInterpreter(0).getNdl1_Name().getCnn2_FamilyName().getValue();
						String approve_date=obr.getObr8_ObservationEndDateTime().getTime().getValue();
						List<OBX> obxList=orderList.get(i).getSPECIMEN(0).getOBXAll();
						TranLogTxt.liswriteEror_to_txt("lisproc_success_jieguo", "res:检验结果解析数据3" +obxList.size()+"\r\n");
						for(int j=0;j<obxList.size();j++){
							try{
							OBX obx=obxList.get(j);
							TranLogTxt.liswriteEror_to_txt("lisproc_success_jieguo", "res:检验结果解析数据4" +obx.getObx11_ObservationResultStatus().getValue()+"\r\n");
							String lis_rep_item_code=obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue();
							String exam_result=obx.getObx5_ObservationValue(0).getData().toString();
							String ref_value=obx.getObx7_ReferencesRange().getValue();
							String item_unit=obx.getObx6_Units().getCe2_Text().getValue();
							String ref_indicator= "";
							ref_indicator = obx.getObx8_AbnormalFlags(0).getValue();
							//瑞美LIS：E错误 H偏高 HH偏高报警 I中介 L偏低 LL偏低报警 M正常 N阴性 P阳性 Q弱阳性
							//火箭蛙：0 正常 1高 2 低 3 阳性4危急5超高6超低
							if("M".equals(ref_indicator) || "N".equals(ref_indicator) || "I".equals(ref_indicator)) {
								ref_indicator = "0";
							} else if("H".equals(ref_indicator)) {
								ref_indicator = "1";
							} else if("L".equals(ref_indicator)) {
								ref_indicator = "2";
							} else if("P".equals(ref_indicator) || "Q".equals(ref_indicator)) {
								ref_indicator = "3";
							} else if("HH".equals(ref_indicator) || "LL".equals(ref_indicator)) {
								ref_indicator = "4";
							} else if ("E".equals(ref_indicator)) {
								new Exception("LIS传入结果中高低标识为E：错误，此种情况体检不接收");
							} else if(null == ref_indicator) {
								ref_indicator = "0";
							} else {//其他不识别的字符，均按照正常来处理
								ref_indicator = "0";
								//new Exception("不支持的高低标识："+ref_indicator);
							}
							if(ref_value == null) {
								ref_value = "";
							}
							if(item_unit == null) {
								item_unit = "";
							}
							//旧LIS
//							if(ref_indicator==null || "".equals(ref_indicator)){
//								if(exam_result!=null && !"".equals(exam_result)){
//									if(exam_result.contains("阴")){
//										ref_indicator="0";
//									}else if(exam_result.contains("阳")){
//										ref_indicator="3";
//									}else{
//										ref_indicator="0";
//									}
//								}else {
//									ref_indicator="0";
//								}
//							}
							
							ProcListResult plr = new ProcListResult();
							plr.setBar_code(bar_code);
							plr.setExam_num(exam_num);
							plr.setLis_item_code(lis_item_code);
							plr.setItem_unit(item_unit);
							
							boolean setflag1=false;
							boolean setflag2=false;
							if((exam_result!=null)&&(exam_result.trim().length()>0)){
							 Pattern zhenshu = Pattern.compile("^[-\\+]?[\\d]*$");  
							 Matcher zs = zhenshu.matcher(exam_result);  
							 setflag1=zs.matches();

							 Pattern xiaoshu = Pattern.compile("-?[0-9]+.?[0-9]+"); 
							 Matcher xs = xiaoshu.matcher(exam_result);
							 setflag2=xs.matches();
							}
							if((plr.getItem_unit()==null)||(plr.getItem_unit().trim().length()<=0)){
								if((setflag1==true)||(setflag2==true)){
									plr.setItem_unit(item_unit);
								} else{
									plr.setItem_unit("");
								}
							}
							
							plr.setLis_rep_item_code(lis_rep_item_code);
							plr.setExam_doctor(exam_doctor);
							plr.setExam_date(dateutilto(exam_date));
							plr.setExam_result(exam_result);
							plr.setRef_value(ref_value);
							plr.setRef_indicator(ref_indicator);
					        
							if((plr.getItem_unit()==null)||("null".equals(plr.getItem_unit()))){
								plr.setItem_unit("");
							}
							if((plr.getExam_result()==null)||("null".equals(plr.getExam_result()))){
								plr.setExam_result("");
							}
							
							plr.setApprover(approver);
							plr.setApprove_date(dateutilto(approve_date));
							
							//由于同一条码下，一个小项可能对应多个收费项目，且对方回传提供的收费项目有误，所以需要循环每个收费项目调用存储过程
							String sql="select ci.exam_num "
									+ " from examinfo_charging_item eci, charging_item ci, charging_item_exam_item cei, sample_exam_detail sed, examination_item ei, examination_item_vs_lis l,examResult_chargingItem eri "
									+ " where eci.examinfo_id=(select id from exam_info where exam_num = '"+exam_num+"') and eci.charge_item_id=ci.id  and ei.id=l.exam_item_id "
									+ " and eci.charge_item_id=cei.charging_item_id and cei.exam_item_id=ei.id and eri.exam_id=sed.id and eri.result_type='sample' and sed.exam_info_id=eci.examinfo_id "
									+ " and l.lis_item_id='"+lis_rep_item_code+"' and eci.isActive='Y' and eri.isActive='Y' "
									+ " and eri.charging_id=eci.charge_item_id and sed.sample_barcode='"+bar_code+"'";
							Connection connection = null;
							Statement statement = null;
							try {
								// 读取记录数
								JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
								connection = jdbcQueryManager.getConnection();
								statement = connection.createStatement();
								TranLogTxt.liswriteEror_to_txt(logname,"\r\n sql:"+sql);
								ResultSet rs = statement.executeQuery(sql);
								while (rs.next()) {
									if(rs.getString("exam_num")==null||rs.getString("exam_num").length()==0){
									}else{
										plr.setLis_item_code(rs.getString("exam_num"));
										int resflag = commService.doproc_Lis_result(plr);
										if (resflag != 0) {
											resflag = commService.doproc_Lis_result(plr);
											if (resflag != 0) {
												resflag = commService.doproc_Lis_result(plr);
												flagss=false;
											}
										}
									}
								}
								rs.close();
							} catch (SQLException ex) {
								TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
							} finally {
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
							
							}catch(Throwable ex){
								ex.printStackTrace();
								TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
							}
					}
					TranLogTxt.liswriteEror_to_txt(logname,"\r\n所有项目入库完毕");
				}
					}catch(Throwable ex){
						ex.printStackTrace();
						TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
					}
					res=XYYYMsgUtil.ACKCommon("ACK", "R24", messageId,"AA");
					TranLogTxt.liswriteEror_to_txt(logname,"\r\n响应平台:"+messageId+"-AA-"+res);
					}
			}else{
				TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:查询体检编号无效");
				res=XYYYMsgUtil.ACKCommon("ACK", "R24", messageId,"AE");
			}
			//}
		} catch (Exception e) {
			//e.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			res=XYYYMsgUtil.ACKCommon("ACK", "R24", messageId,"AE");	
		}
		return res;
	}
	public static List<String> getFtpFile(String pacs_id,String typeCode,String date,String exam_num,String logname){
		List<String> paths=new ArrayList<String>();
		if(!typeCode.equals("")){
			String url=queryUrl(typeCode,"url",logname);
			String ftpurl=queryUrl(typeCode,"ftpurl",logname);
			String ftpport=queryUrl(typeCode,"ftpport",logname);
			 List<String[]> ftp=queryFtp(url,pacs_id,logname);
			 if(ftp!=null&&ftp.size()>0&&ftp.get(0)[0]!=null&&ftp.get(0)[0].trim().length()>0){
				 for(int i=0;i<ftp.size();i++){
					 ftp.get(i)[0]= ftp.get(i)[0].replaceAll("\\\\", "/");
					 String server=ftpurl;
					 String filem=ftp.get(i)[0].substring( ftp.get(i)[0].indexOf("/"));
					 String name= ftp.get(i)[1];
					 paths.add(getFile(server,ftpport,filem,name,typeCode,date,exam_num,pacs_id,ftp.get(i)[2],logname));
				 }
			 }
		}
		return paths;
	}
	private static String getFile(String server,String ftpport,String filem,String name,String typeCode,String date,String exam_num,String pacs_id,String num,String logname){
        File localFile=null;
        FTPClient ftp = new FTPClient();
        String filename="";
        try {  
            int reply;  
            //1.连接服务器
         //   if(typeCode.equals("04")){
          //  	ftp.connect(server);
          //  }else{
            	ftp.connect(server,Integer.valueOf(ftpport));
                //2.登录服务器 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器  
                ftp.login("hjw","xyyytj");  
                //3.判断登陆是否成功  
                reply = ftp.getReplyCode();  
                if (!FTPReply.isPositiveCompletion(reply)) {  
                    ftp.disconnect();  
                }  
          //  }
            //4.指定要下载的目录  
            ftp.changeWorkingDirectory(filem);// 转移到FTP服务器目录 

            //5.遍历下载的目录  
            FTPFile[] fs = ftp.listFiles();  
            for (FTPFile ff : fs) {  
                //解决中文乱码问题，两次解码  
                byte[] bytes=ff.getName().getBytes("iso-8859-1");  
                String fn=new String(bytes,"utf-8");  
                if (fn.equals(name)) {
                	if(num==null||num.equals("")){
                		num="1";
                	}
                	filename="E:"+File.separator+"picture"+File.separator+"pacs_img"+File.separator+date+File.separator+typeCode+File.separator+exam_num+File.separator+pacs_id+File.separator+pacs_id+"_"+num+".JPG";
                	localFile=new File(filename);
                	if(!localFile.getParentFile().exists()){
                		localFile.getParentFile().mkdirs();
                		localFile.createNewFile();
                	}
                	if(localFile.exists()){
                		localFile.delete();
                	}
                	
                	//OutputStream is = new FileOutputStream(localFile);
                	
                	OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
					
                	/*BufferedImage image2 = ImageIO.read(localFile);  
                	image2=ImgXuanzhuan.rotateCounterclockwise90(image2);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(image2,"jpg",os);

                    InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
                    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                            int ch;
                            while ((ch = inputStream.read()) != -1) {   
                                 swapStream.write(ch);   
                            }
                    ftp.retrieveFile(ff.getName(),swapStream);
                    swapStream.close(); */  
                }  
            }  
            ftp.logout();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }
        filename=filename.substring(10);
        filename=filename.replaceAll("\\\\", "/");
		return filename;  
    }  
	private static List<String[]> queryFtp(String url,String pacs_id,String logname){
		Connection connection = null;
		Statement statement = null;
		String sql="select report_path,report_name,report_num from v_hjw where apply_id='"+pacs_id+"' and source='体检'";
		String result="";
		List<String[]> strsList=new ArrayList<String[]>();
		try {
			// 读取记录数
			connection = SqlServerDatabaseSource.getConnection(url, "hjw", "xyyytj");
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				String[] strs=new String[3];
				strs[0]=rs.getString("report_path");
				strs[1]=rs.getString("report_name");
				strs[2]=rs.getString("report_num");
				strsList.add(strs);
			}
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
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
		return strsList;
	}
	private static String queryUrl(String typeCode,String type,String logname){
		WebApplicationContext wac =ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		Connection connection = null;
		Statement statement = null;
		if(type.equals("url")){
			typeCode="XYYY_"+typeCode+"_URL";	
		}else if(type.equals("ftpurl")){
			typeCode="XYYY_"+typeCode+"_FTPURL";
		}else{
			typeCode="XYYY_"+typeCode+"_FTPPORT";
		}
		String sql="select config_value from center_configuration where config_key='"+typeCode+"'";
		String result="";
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
					result=rs.getString("config_value");
			}
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
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
		return result;
	}
	
	public static String ORUR01HL7(String str,String logname) {
		String nowtime=DateTimeUtil.shortFmt3(new Date());
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		CommService commService = (CommService) wac.getBean("commService");
		Parser p = new GenericParser();
		Message msg;
		String res = "";
		boolean flagss = true;
		String messageId="";
		String resultCode="AE";
		try {
			msg = p.parse(str);
			ORU_R01 adt = (ORU_R01) msg;
			MSH msh = adt.getMSH();
			 messageId = msh.getMessageControlID().getValue();

			PID pid = adt.getPATIENT_RESULT(0).getPATIENT().getPID();
			String patient_id = pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().getValue();
			String exam_num = queryExamNum(patient_id,logname);
			ORC orc = adt.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getORC();
			String pacs_req_code = orc.getOrc2_PlacerOrderNumber().getEi3_UniversalID().getValue();
			
			OBR obr = adt.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getOBR();
			String type_code=obr.getObr4_UniversalServiceIdentifier().getCe4_AlternateIdentifier().getValue();
			//---begin
			//ftp下载图片返回图片路径
			String img_file = "";
			try{
			List<String> paths=getFtpFile(pacs_req_code,type_code,nowtime,exam_num,logname);
			
			if(paths!=null&&paths.size()>0){
				for(int x=0;x<paths.size();x++){
					img_file=paths.get(x);
					if(x+1!=paths.size()){
						img_file=img_file+";";
					}
				}
			}
			}catch(Exception ex){
				TranLogTxt.liswriteEror_to_txt(logname,"\r\n文件下载错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
				TranLogTxt.liswriteEror_to_txt(logname,"\r\n pacs_req_code:"+pacs_req_code);
				TranLogTxt.liswriteEror_to_txt(logname,"\r\n type_code:"+type_code);
				TranLogTxt.liswriteEror_to_txt(logname,"\r\n nowtime:"+nowtime);
				TranLogTxt.liswriteEror_to_txt(logname,"\r\n exam_num:"+exam_num);
			}
			//---end---
			String check_date = obr.getObr22_ResultsRptStatusChngDateTime().getTime().getValue();
			check_date=check_date.substring(0, 4) + "-" + check_date.substring(4, 6) + "-"
			+ check_date.substring(6, 8)+" "+check_date.substring(8, 10)+":"+check_date.substring(10,12)+":"+check_date.substring(12,14);
			String check_doct = obr.getObr32_PrincipalResultInterpreter().getNdl1_Name().getCnn1_IDNumber().getValue();
			String audit_date = obr.getObr8_ObservationEndDateTime().getTime().getValue();
			audit_date=audit_date.substring(0, 4) + "-" + audit_date.substring(4, 6) + "-"
					+ audit_date.substring(6, 8)+" "+audit_date.substring(8, 10)+":"+audit_date.substring(10,12)+":"+audit_date.substring(12,14);
			String audit_doct = obr.getObr33_AssistantResultInterpreter(0).getNdl1_Name().getCnn1_IDNumber().getValue();
			String exam_result = "";
			String exam_desc = "";
			List<ORU_R01_OBSERVATION> list = adt.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getOBSERVATIONAll();
			for (int i = 0; i < list.size(); i++) {
				OBX obx = list.get(i).getOBX();
				String type = obx.getObx2_ValueType().getValue();
				if (type.equals("TX")) {
					exam_desc = obx.getObx5_ObservationValue(0).getData().toString();
					if(exam_desc==null||exam_desc.length()<=0){
					  exam_desc="";
					}
				} else if (type.equals("ST")) {
					exam_result = obx.getObx5_ObservationValue(0).getData().toString();
					if(exam_result==null||exam_result.length()<=0){
					  exam_result="";
					}
				}
			}
			ProcPacsResult plr = new ProcPacsResult();
			plr.setExam_num(exam_num);
			plr.setPacs_req_code(pacs_req_code);
			plr.setCheck_date(check_date);
			plr.setCheck_doct(check_doct);
			plr.setAudit_date(audit_date);
			plr.setAudit_doct(audit_doct);
			plr.setExam_result(exam_result);
			plr.setExam_desc(exam_desc);
			plr.setImg_file(img_file);//图像文件路径以分号隔开
			//结果写入正式库
			int falgint = commService.proc_pacs_report_dbgj(plr);
			if (falgint != 0) {
				flagss = false;
			}
			if (flagss) {
				resultCode = "AA";
			} else {
				resultCode = "AE";
			}
			res = XYYYMsgUtil.ACKCommon("ACK", "R01", messageId, resultCode);
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
			res = XYYYMsgUtil.ACKCommon("ACK", "R01", messageId, "AE");
		}
		return res;
	}
	public static String DFTP03HL7(String str,String logname){
		Parser p = new GenericParser();
		Message msg;
		String res="";
		try {
			msg=p.parse(str);
			DFT_P03 adt=(DFT_P03) msg;
			MSH msh=adt.getMSH();
			String messageId=msh.getMessageControlID().getValue();
			PID pid=adt.getPID();
			String patientId=pid.getPatientID().getCx1_IDNumber().getValue();
			System.out.println(patientId);
			 res=XYYYMsgUtil.ACKCommon("ACK", "P03", messageId,"CA");
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		}
		
		return res;
	}
	public static String ADTA28HL7(String str,String logname){
		Parser p = new GenericParser();
		Message msg;
		String res="";
		try {
			msg=p.parse(str);
			ADT_A05 adt=(ADT_A05) msg;
			MSH msh=adt.getMSH();
			String messageId=msh.getMessageControlID().getValue();
			PID pid=adt.getPID();
			String patientId=pid.getPatientID().getCx1_IDNumber().getValue();
			System.out.println(patientId);
			 res=XYYYMsgUtil.ACKCommon("ACK", "A28", messageId,"AA");
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static String ADTA31HL7(String str,String logname){
		Parser p = new GenericParser();
		Message msg;
		String res="";
		String messageId="";
		try {
			msg=p.parse(str);
			ADT_A05 adt=(ADT_A05) msg;
			MSH msh=adt.getMSH();
			 messageId=msh.getMessageControlID().getValue();
			PID pid=adt.getPID();
			String patientId=pid.getPatientID().getCx1_IDNumber().getValue();
			System.out.println(patientId);
			 res=XYYYMsgUtil.ACKCommon("ACK", "A31", messageId,"AA");
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
			 res=XYYYMsgUtil.ACKCommon("ACK", "A31", messageId,"AE");
		}
		return res;
	}
	
	public static String OMGO19HL7(String str,String logname){
		WebApplicationContext wac =ContextLoader.getCurrentWebApplicationContext();
		CommService commService = (CommService) wac.getBean("commService");
		String res="";
		Parser p = new GenericParser();
		Message msg;
		String messageId="";
		try {
			msg=p.parse(str);
			OMG_O19 adt=(OMG_O19) msg;
			MSH msh=adt.getMSH();
			messageId=msh.getMessageControlID().getValue();
			PID pid=adt.getPATIENT().getPID();
			String patient_id=pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().getValue();
			String exam_num=queryExamNum(patient_id,logname);
			ORC orc=adt.getORDER(0).getORC();
			List<String> pac_nos = new ArrayList<String>(); 
			String pac_no=orc.getOrc2_PlacerOrderNumber().getEi3_UniversalID().getValue();
			pac_nos.add(pac_no);
			commService.setExamInfoChargeItemPacsStatus(pac_nos, exam_num, "N");
			res=XYYYMsgUtil.ACKPACS(messageId,"AA","");
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
			res=XYYYMsgUtil.ACKPACS(messageId,"AE",com.hjw.interfaces.util.StringUtil.formatException(e));
		}	
		return res;
	}
	
	public static String OMLO21HL7(String str,String logname){
		String res="";
		try {
			res=XYYYMsgUtil.ACKCommon("ACK", "O22", "123123","AA");
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\nreq:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		}
		
		return res;
	}
	
}
