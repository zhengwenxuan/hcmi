package com.hjw.webService.xyyyService.server;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.xyyyService.util.XYYYHL7Message;
import com.hjw.webService.xyyyService.util.XYYYMsgUtil;
import com.hjw.wst.DTO.ProcListResult;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.datatype.CE;
import ca.uhn.hl7v2.model.v251.datatype.CX;
import ca.uhn.hl7v2.model.v251.group.OUL_R24_ORDER;
import ca.uhn.hl7v2.model.v251.message.OUL_R24;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.OBX;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

public class Test2 {
	public static void main(String[] args) {
		
		String str ="MSH|^~\\&|LIS|10|ESB|10|20180321230233||OUL^R24^OUL_R24|4317674|P|2.5.1|||AL|AL|CHN|UNICODE UTF-8|||12345^HIS^23456^HIS\r"
                    +"PID|||000825544200^^^^21~^^^^51~^^^^1~^^^^25||杨娜娜|||||||||||\r"
                    +"PV1|1|T|^^^10||||00223^张  晋||||||||||||^1|||||||||||||||||||||||||0000\r"
                    +"OBR|1|9080703^LIS^09201801251658^LIS||201^生化报告1||20180322100310|20180322100310|20180322134025||||||||00223^张  晋||||||20180322140210|||2|||||||00000&全院|00460&王海龙|52040&张何锐^^^临床检验\r"
                    +"SPM|1|||C^血清\r"
                    +"OBX|0|ST|3010070304^糖化血清蛋白(果糖胺)^^FRA^糖化血清蛋白(果糖胺)||302.00|^umol/L|205.0000-285.0000||||1|||20180322140210\r"
                    +"OBX|1|ST|3020010101^*葡萄糖^^GLU^*葡萄糖||5.09|^mmol/L|3.9000-6.1000||||0|||20180322140210\r"
                    +"OBX|2|ST|3030010101^*总胆固醇^^TCH^*总胆固醇||8.02|^mmol/L|3.1000-5.7000||||1|||20180322140210\r"
                    +"OBX|3|ST|3030020101^*甘油三酯^^TG^*甘油三酯||1.02|^mmol/L|0.6000-1.7000||||0|||20180322140210\r"
                    +"OBX|4|ST|3030040101^高密度脂蛋白^^HDL-C^高密度脂蛋白||2.74|^mmol/L|1.0900-2.0600||||1|||20180322140210\r"
                    +"OBX|5|ST|3030050101^低密度脂蛋白^^LDL-C^低密度脂蛋白||4.72|^mmol/L|0.0000-3.3700||||1|||20180322140210\r"
                    +"OBX|6|ST|3030050201^极低密度脂蛋白^^VLDL^极低密度脂蛋白||0.56|^mmol/L|0.2000-0.9000||||0|||20180322140210\r"
                    +"OBX|7|ST|3050010101^*总胆红素^^TBIL^*总胆红素||18.20|^μmol/L|2.0000-21.0000||||0|||20180322140210\r"
                    +"OBX|8|ST|3050020101^直接胆红素^^DBIL^直接胆红素||5.90|^μmol/L|0.0000-5.0000||||1|||20180322140210\r"
                    +"OBX|9|ST|3050030101^间接胆红素^^IBIL^间接胆红素||12.30|^μmol/L|0.0000-16.0000||||0|||20180322140210\r"
                    +"OBX|10|ST|3050070101^*丙氨酸氨基转移酶^^ALT^*丙氨酸氨基转移酶||25.10|^U/L|7.0000-40.0000||||0|||20180322140210\r"
                    +"OBX|11|ST|3050080101^*天门冬氨酸氨基转移酶^^AST^*天门冬氨酸氨基转移酶||39.40|^U/L|13.0000-40.0000||||0|||20180322140210\r"
                    +"OBX|12|ST|3050100101^* γ谷氨酰转肽酶^^GGT^* γ谷氨酰转肽酶||25.82|^U/L|7.0000-45.0000||||0|||20180322140210\r"
                    +"OBX|13|ST|3070010101^尿素氮^^BUN^尿素氮||10.64|^mg/dL|7.7300-22.6000||||0|||20180322140210\r"
                    +"OBX|14|ST|3070010103^*尿素^^UREA^*尿素||3.80|^mmol/L|2.7600-8.0700||||0|||20180322140210\r"
                    +"OBX|15|ST|3070020101^*肌酐^^CREA^*肌酐||57.00|^μmol/L|45.0000-84.0000||||0|||20180322140210\r"
                    +"OBX|16|ST|3070050301^*尿酸^^UA^*尿酸||305.00|^μmol/L|142.8000-339.2000||||0|||20180322140210\r"
                    +"OBX|17|ST|8990010101^溶血程度^^H^溶血程度||9.00||0.0000-30.0000||||0|||20180322140210\r"
                    +"OBX|18|ST|8990040101^浑浊程度^^L^浑浊程度||13.00||0.0000-25.0000||||0|||20180322140210\r"
                    +"OBX|19|ST|8990050101^黄染程度^^I^黄染程度||3.00||0.0000-10.0000||||0|||20180322140210\r";
		
		String rs1="MSH|^~\\&|LIS|10|ESB|10|20180321181854||OUL^R24^OUL_R24|4315644|P|2.5.1|||AL|AL|CHN|UNICODE UTF-8\r"
                    +"PID|||000825585500^^^^21~^^^^51~^^^^1~^^^^25||测试的人|||||||||||\r"
                    +"PV1|1|T|^^^10||||00223^张  晋||||||||||||^1|||||||||||||||||||||||||0000\r"
                    +"OBR|1|9079756^LIS^09201803220050^LIS||141^杂项报告(定性)||20180322000000|20180322091732|20180322091840||||||||00223^张  晋||||||20180322091843|||2|||||||00000&全院|50339&董美衬|00468&杨  元^^^临床检验\r"
                    +"SPM|1|||3^粪便\r"
                    +"OBX|0|ST|1021240501^脂肪滴^^脂肪滴^脂肪滴|||||||||||20180322091843\r"
                    +"OBX|1|ST|1030011801^寄生虫卵^^寄生虫卵^寄生虫卵|||||||||||20180322091843\r"
                    +"OBX|2|ST|1030060102^不消化食物^^不消化食物^不消化食物|||||||||||20180322091843\r"
                    +"OBX|3|ST|1039001001^颜色^^颜色^颜色||棕色||棕色|||||||20180322091843\r"
                    +"OBX|4|ST|1039001002^硬度^^硬度^硬度||软||软|||||||20180322091843\r"
                    +"OBX|5|ST|1039001003^粘液^^粘液^粘液||无||无|||||||20180322091843\r"
                    +"OBX|6|ST|1039001004^白细胞(高倍视野)^^白细胞^白细胞(高倍视野)||未见|^个/HP|未见|||||||20180322091843\r"
                    +"OBX|7|ST|1039001005^红细胞(高倍视野)^^红细胞^红细胞(高倍视野)||未见|^个/HP|未见|||||||20180322091843\r"
                    +"OBX|8|ST|1039001006^潜血^^潜血^潜血||阴性||阴性|||||||20180322091843\r"
                    +"OBX|9|ST|1039001007^动力^^动力^动力|||||||||||20180322091843\r"
                    +"OBX|10|ST|1039001008^制动^^制动^制动|||||||||||20180322091843";
		String rs2="MSH|^~\\&|LIS|10|ESB|10|20180326215039||OUL^R24^OUL_R24|4328989|P|2.5.1|||AL|AL|CHN|UNICODE UTF-8\r"
                    +"PID|||000826561300^^^^21~^^^^51~^^^^1~^^^^25||赵子峰|||||||||||\r"
                    +"PV1|1|T|^^^10||||00223^张  晋||||||||||||^1|||||||||||||||||||||||||0000\r"
                    +"OBR|1|9103099^LIS^09201803260100^LIS||141^杂项报告(定性)||20180327000000|20180327123843|20180327125018||||||||00223^张  晋||||||20180327125020|||2|||||||00000&全院|51839&张艺靖|51932&宋北^^^临床检验\r"
                    +"SPM|1|||3^粪便\r"
                    +"OBX|0|ST|1021240501^脂肪滴^^脂肪滴^脂肪滴|||||||||||20180327125020\r"
                    +"OBX|1|ST|1030011801^寄生虫卵^^寄生虫卵^寄生虫卵|||||||||||20180327125020\r"
                    +"OBX|2|ST|1030060102^不消化食物^^不消化食物^不消化食物|||||||||||20180327125020\r"
                    +"OBX|3|ST|1039001001^颜色^^颜色^颜色||棕色|||||||||20180327125020\r"
                    +"OBX|4|ST|1039001002^硬度^^硬度^硬度||软|||||||||20180327125020\r"
                    +"OBX|5|ST|1039001003^粘液^^粘液^粘液||无|||||||||20180327125020\r"
                    +"OBX|6|ST|1039001004^白细胞(高倍视野)^^白细胞^白细胞(高倍视野)||未见|^个/HP||||||||20180327125020\r"
                    +"OBX|7|ST|1039001005^红细胞(高倍视野)^^红细胞^红细胞(高倍视野)||未见|^个/HP||||||||20180327125020\r"
                    +"OBX|8|ST|1039001006^潜血^^潜血^潜血||阴性(-)|||||||||20180327125020\r"
                    +"OBX|9|ST|1039001007^动力^^动力^动力|||||||||||20180327125020\r"
                    +"OBX|10|ST|1039001008^制动^^制动^制动|||||||||||20180327125020";
		str="MSH|^~\\&|LIS|10|ESB|10|20180604143425||OUL^R24^OUL_R24|4512346|P|2.5.1|||AL|AL|CHN|UNICODE UTF-8\r"
				+"PID|||000844484600^^^^21~^^^^51~^^^^1~^^^^25||何晓|||||||||||\r"
				+"PV1|1|T|^^^10||||00223^张  晋||||||||||||^1|||||||||||||||||||||||||0000\r"
				+"OBR|1|9401751^LIS^09201806013171^LIS||301^免疫定量报告||20180604111116|20180604111116|20180604141438||||||||00223^张  晋||||||20180604142745|||2|||||||00000&全院|51931&杜娟|51835&吴晶^^^临床检验\r"
				+"SPM|1|||C^血清\r"
				+"OBX|0|ST|4040010101^*癌胚抗原测定^^CEA^*癌胚抗原测定||1.82|^ug/L|0.0000-10.0000||||0|||20180604142745\r"
				+"OBX|1|ST|4040020101^*甲胎蛋白测定^^AFP^*甲胎蛋白测定||1.96|^ug/L|0.0000-20.0000||||0|||20180604142745\r"
				+"OBX|2|ST|4040050101^*总前列腺特异性抗原测定^^TPSA^*总前列腺特异性抗原测定(TPSA)||1.34|^ug/L|0.0000-4.4000||||0|||20180604142745\r"
				+"OBX|3|ST|4040060101^游离前列腺特异性抗原测定^^FPSA^游离前列腺特异性抗原测定(FPSA)||0.33|^ug/L|0.0000-1.3000||||0|||20180604142745\r"
				+"OBX|4|ST|4040070101^游离和总前列腺特异抗原比值^^F/TPSA^游离和总前列腺特异抗原比值||0.25||0.0000-1.0000||||0|||20180604142745\r";
      /* str ="MSH|^~\\&|MEDEX|10|HIS|10|20180709134907||ORU^R01^ORU_R01|USBG20180709134907|P|2.5.1||||CHN|UNICODE UTF-8\r"
    		   +"PID|||000853094200^^^^21||吴佳|||F||||||||\r"
+"PV1||T|||||||||||||||||000853094200^1|||||||||||000000|||||000000|||||||||0000\r"
+"ORC||18012507109^HIS^18012507109^HIS|1101211807090908521515354^MEDEX^1101211807090908521515354^MEDEX||CM||||\"\"|||李军|心电图||0000\r"
+"OBR||18012507109^HIS^18012507109^HIS|1101211807090908521515354^MEDEX|001^电脑多导联心电图^^04^心血管病检查|||20180709134907|20180709134907||||||||||||||20180709134859|||F|||||||李军|李军|^^^心电图\r"
+"OBX|1|TX|1037^检查所见^LN||||||||F|||20180709090954\r"
+"OBX|2|ST|1038^检查印象^LN||窦性心律，正常心电图||||||F|||20180709134907";*/
		Parser p = new GenericParser();
		Message msg = null;
		String res="";
		String messageId="";
		try {
			System.out.println("555");
			msg=p.parse(str);
			OUL_R24 adt=(OUL_R24) msg;
			MSH msh=adt.getMSH();
			messageId=msh.getMessageControlID().getValue();
			PID pid=adt.getPATIENT().getPID();
			CX[] md= pid.getPatientIdentifierList();
			System.out.println(md.length);
			CX[] mdm= pid.getPid3_PatientIdentifierList();
			System.out.println(mdm.length);
			String patient_id=pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().getValue();
			//String exam_num = queryExamNum(patient_id,"RIZHI");
			List<OUL_R24_ORDER> orderList=adt.getORDERAll();
			boolean flagss = true;
			//if(exam_num!=null&&exam_num.length()>0){
				for(int i=0;i<orderList.size();i++){
					OBR obr=orderList.get(i).getOBR();
					String bar_code=obr.getObr2_PlacerOrderNumber().getEi3_UniversalID().getValue();
					//String shifouyouzhi=QuerySqlData.getShiFouCunZai(exam_num, bar_code,logname);
					//if(shifouyouzhi.equals("1")){
						String lis_item_code=obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().getValue();
//						String exam_doctor=obr.getObr32_PrincipalResultInterpreter().getNdl1_Name().getCnn2_FamilyName().getValue();
						String exam_doctor=obr.getObr34_Technician(0).getNdl1_Name().getCnn2_FamilyName().getValue();
						String exam_date=obr.getObr22_ResultsRptStatusChngDateTime().getTime().getValue();
						String approver=obr.getObr33_AssistantResultInterpreter(0).getNdl1_Name().getCnn2_FamilyName().getValue();
						String approve_date=obr.getObr8_ObservationEndDateTime().getTime().getValue();
						List<OBX> obxList=orderList.get(i).getSPECIMEN(0).getOBXAll();
						for(int j=0;j<obxList.size();j++){
							OBX obx=obxList.get(j);
							String lis_rep_item_code=obx.getObx3_ObservationIdentifier().getCe1_Identifier().getValue();
							String exam_result=obx.getObx5_ObservationValue(0).getData().toString();
							String ref_value=obx.getObx7_ReferencesRange().getValue();
							String item_unit=obx.getObx6_Units().getCe2_Text().getValue();
							CE ce =obx.getObx6_Units();
							//System.out.println(obx.getObx6_Units().getCe2_Text().getValue());
							String ref_indicator=obx.getObx11_ObservationResultStatus().getValue();
							//System.out.println(ref_indicator);
							if(ref_indicator==null || "".equals(ref_indicator)){
								if(exam_result!=null && !"".equals(exam_result)){
									if(exam_result.contains("阴")){
										ref_indicator="0";
									}else if(exam_result.contains("阳")){
										ref_indicator="3";
									}else{
										ref_indicator="0";
									}
								}else {
									ref_indicator="0";
								}
							}
							ProcListResult plr = new ProcListResult();
							plr.setBar_code(bar_code);
							plr.setItem_unit(item_unit);
							//plr.setExam_num(exam_num);
							plr.setLis_item_code(lis_item_code);
							//System.out.println(plr.getItem_unit());
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
							//plr.setExam_date(dateutilto(exam_date));
							plr.setExam_result(exam_result);
							plr.setRef_value(ref_value);
							plr.setRef_indicator(ref_indicator);
					
							
							plr.setApprover(approver);
							if((plr.getItem_unit()==null)||("null".equals(plr.getItem_unit()))){
								plr.setItem_unit("");
							}
							if((plr.getExam_result()==null)||("null".equals(plr.getExam_result()))){
								plr.setExam_result("");
							}
							System.out.println(plr.getItem_unit());
							System.out.println(plr.getExam_result());
							//plr.setApprove_date(dateutilto(approve_date));
							//int resflag = commService.doproc_Lis_result(plr);
						}
					//}
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
