package com.hjw.webService.client.nanfeng.util;

import java.text.SimpleDateFormat;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;

public class Wang1Jianchashenqing {
	public static void main(String[] args) throws Exception {
		String msg = "MSH|^~\\&|HIS|JHIP|JHIP|JHIP|20150118160551||ORM^O01^ORM_O01|JHIP201503181524|P|2.4\r"+
					"PID||300276|300276~300276~~~|0|WuGuoXiang^吴国祥||19360603000000|M|||其他事业移交直管人员^其他事业移交直管人员"+
					"<12600799>||^^^^^^13429119708|||O^其他||330182D156000005000B4C5828A98E95|330126193606031311||||||||||||0||||||||河南省郑州市|河南省郑州市高"+
					"新区|北京嘉和美康信息技术有限公司|软件工程师|B\r"+
					"NTE|1|||^^^反复咳嗽气急 8 年，再发 9 天\r"+
					"PV1|1|I|A31300^^313027^10082&呼吸内科||||314^^翁伟芳||||||||||134^^沈红卫"+
					"|84|10030700||2021||||||||0|||||||||||||10082||20150118144509||||||1|V|127\r"+
					"ORC|NW|1000056067|||||^^^^^4||20150118154440|||316^^陈晨|10082^^^^^^^^呼吸内科||||10082^呼吸内科|||136^1\r"+
					"OBR|1|1000056067||173544^腹部（肝、胆、脾、胰）^^7^彩色多普勒超声常规检查|4|||||||10149^B 超室|||||||||||^136|7\r"+
					"DG1|1|慢性阻塞性肺病||慢性阻塞性肺病||F\r"+
					"ORC|NW|1000056067|||||^^^^^4||20150118154440|||316^^陈晨|10082^^^^^^^^呼吸内科||||10082^呼吸内科|||136^1\r"+
					"OBR|2|1000056067||173546^泌尿系（双肾、输尿管、膀胱、前列腺、肾上腺）^^7^彩色多普勒超声常规检查|4|||||||10149^B 超室|||||||||||^136|7\r"+
					"DG1|1|慢性阻塞性肺病||慢性阻塞性肺病||F\r";
		
		//patientGender  性别 男  patientName姓名 //如果生日不为空则set年龄   19360603000000
		//departmentCode挂号科室FSK001   *目前固定*
		//PatientType就诊类型100  *默认100  先不取了*
		//residentFlag住院标记 0     对应【患者分类】进行转换
		//patientNo就诊编号    医保卡号 PID18
		//RESERVED_001 可能是电话号码 [PID 14.7]   
		//RESIDENT_NUM住院编号[就诊 ID 或者患者住院 ID PV119]  
		//BED_NUM床位号    PV1 第三个
		//AREA_CODE挂号诊区
		PipeParser pipeParser = new PipeParser();
		Message message = pipeParser.parse(msg);
		Terser terser = new Terser(message);
		String sex=terser.get("/.PID-8").equals("M")?"男":"女";//性别
		System.out.println("性别:"+sex);
		String givenName = terser.get("/.PID-5-2");//姓名
		System.out.println("姓名:"+givenName);
		String birthDate=terser.get("/.PID-7");
		SimpleDateFormat fomater = new SimpleDateFormat("yyyyMMddHHmmss");
		String age=birthDate;//年龄
		System.out.println("年龄:"+age);
		String ybkh=terser.get("/.PID-3-1");//就诊编号
		System.out.println("医保卡号:"+ybkh);
		String hzfl=terser.get("/.PV1-2");
		String zybj=hzfl.equals("I")?"1":"0";//住院标记
		System.out.println("是否住院:"+zybj);
		String departmentCode="FSK001";//挂号科室
		String PatientType="100";//就诊类型
		//String hospitalUuid = DepartmentCache.get(departmentCode).getHospitalUuid();
		//String hospitalCode = HospitalCache.get(hospitalUuid).getHospitalCode();//医院
		String tel=terser.get("/.PID-13-7");//电话号码
		System.out.println("电话号码:"+tel);
		String zybh=terser.get("/.PV1-19");//住院编号
		System.out.println("住院编号:"+zybh);
		String cwh=terser.get("/.PV1-3-3");//床位号
		System.out.println("床位号:"+cwh);
		String ghzq="FSK001";//挂号诊区
		String jcbw=terser.get("/.OBR-4");//检查部位
		System.out.println("检查部位:"+jcbw+terser.get("/.OBR-4-2"));
		
		String jcbw1=terser.get("/.OBR(1)-4");//检查部位
		System.out.println("检查部位:"+jcbw1+terser.get("/.OBR(1)-4-2"));
		
		String sqdID=terser.get("/.OBR-2");//申请单ID
		System.out.println("申请单ID:"+sqdID);
	}
}
