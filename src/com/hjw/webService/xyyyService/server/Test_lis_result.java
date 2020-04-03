package com.hjw.webService.xyyyService.server;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hjw.webService.xyyyService.util.XYYYMsgUtil;
import com.hjw.wst.DTO.ProcListResult;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.datatype.EIP;
import ca.uhn.hl7v2.model.v251.group.OUL_R24_ORDER;
import ca.uhn.hl7v2.model.v251.group.OUL_R24_RESULT;
import ca.uhn.hl7v2.model.v251.group.OUL_R24_SPECIMEN;
import ca.uhn.hl7v2.model.v251.message.OUL_R24;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.OBX;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

public class Test_lis_result {
	public static void main(String[] args) {
		String str ="MSH|^~\\&|LIS|10|ESB|10|20190812145820||OUL^R24^OUL_R24|820539|P|2.5.1|||AL|AL|CHN|UNICODE UTF-8\r"
				+ "PID|1|000954555300|000954555300^^^^21~000954555300~^^^^51~131022199409250021^^^^1~^^^^25||辛佳檬||19940925|2|||^^^^^^H|||||||||||HA^汉族|110108\r"
				+ "PV1|1|T|1010901^^^10|R|||00223^张  晋||||||||||||^8|||||||||||000000|||||000000|||||||||0000\r"
				+ "OBR|1|TSQ_000000853510^HIS^TYZ_000000853510^HIS||9020000104^体检肝功I(停用)||||20190812110630|||||||C&血清|50945^丛晓东||||||20190812145810|||||||||||50708&刘文娟|00478&董  彬\r"
				+ "ORC|OK|TSQ_000000853510^HIS^TYZ_000000853510^HIS||send|1||||20190801000000|00223^张  晋||00223^张  晋|||0000||10^西苑医院-本院||||||||||||3^化验类\r"
				+ "SPM||&&09201907315367||\r"
				+ "OBX|1|ST|0301032^糖化血清蛋白(果糖胺)^^^FRA||290.00|^umol/L|205.00-285.00|H|||F|||||C^0^BAA^205.0000^285.0000\r"
				+ "OBX|2|ST|0301013^*丙氨酸氨基转移酶^^^ALT||7.50|^U/L|7.00-40.00|M|||F|||||C^0^AAA\r"
				+ "OBX|3|ST|0301014^*天门冬氨酸氨基转移酶^^^AST||15.40|^U/L|13.00-40.00|M|||F|||||C^0^AAA\r"
				+ "OBX|4|ST|0301007^*总胆红素^^^TBIL||14.60|^μmol/L|2.00-21.00|M|||F|||||C^0^AAA^2.0000^21.0000\r"
				+ "OBX|5|ST|0301008^直接胆红素^^^DBIL||4.90|^μmol/L|0.00-5.00|M|||F|||||C^0^AAA^0.0000^5.0000\r"
				+ "OBX|6|ST|0301009^间接胆红素^^^IBIL||9.70|^μmol/L|0.00-16.00|M|||F|||||C^0^AAA^0.0000^16.0000\r"
				+ "OBX|7|ST|0301012^*γ谷氨酰转肽酶^^^GGT||10.19|^U/L|7.00-45.00|M|||F|||||C^0^AAA^10.0000^60.0000\r"
				+ "OBX|8|ST|0301028^*尿素^^^UREA||5.00|^mmol/L|2.76-8.07|M|||F|||||C^0^AAA^2.7600^8.0700\r"
				+ "OBX|9|ST|0301513^尿素氮^^^BUN||14.00|^mg/dL|7.73-22.60|M|||F|||||C^0^AAA^7.7300^22.6000\r"
				+ "OBX|10|ST|0301029^*肌酐^^^CREA||70.00|^μmol/L|45.00-84.00|M|||F|||||C^0^AAA\r"
				+ "OBX|11|ST|0301030^*尿酸^^^UA||312.00|^μmol/L|142.80-339.20|M|||F|||||C^0^AAA\r"
				+ "OBX|12|ST|0301046^*葡萄糖^^^GLU||4.86|^mmol/L|3.90-6.10|M|||F|||||C^0^AAA\r"
				+ "OBX|13|ST|0301514^溶血程度^^^H||13.00||轻：31-49，中：50-69，重：>70|M|||F|||||C^0^AAA^^30.0000\r"
				+ "OBX|14|ST|0301515^浑浊程度^^^L||12.00||轻：26-49，中：50-129，重：>129|M|||F|||||C^0^AAA^^25.0000\r"
				+ "OBX|15|ST|0301516^黄染程度^^^I||1.00||0-10|M|||F|||||C^0^AAA^0.0000^10.0000\r";
//		String str =""
//				+"MSH|^~\\&|LIS^瑞美|10|ESB|10|20181114170256||OUL^R24^OUL_R24|3e5319f8-36f6-4ebc-ae69-b4dd416a50c0_19606|P|2.5.1|||AL|AL|CHN|UNICODE UTF-8\r"
//				+"PID|1|000885349000|000885349000^^^^21~1810190079^^^^51~320911198407014914^^^^1~000885349000^^^^25||刘少伟||19840701|1|||^^^^^^H|||||||||||HA^汉族|110108\r"
//				+"PV1|1|T|1010901^^^10|R|||00223^张  晋||||||||||||^1|||||||||||||||||||||||||0000\r"
//				+"OBR|1|TSQ_000000500200^HIS^TYZ_000000500200^HIS|09201810190224^LIS^19606^LIS|1029000102^尿常规||20181114080823|20181114094258|20181114170252|||||||2&尿液|00223^张  晋|||||20181114170252|20181114094822|||F|||||||00479&郭  盼|50787&陈吉祥|00479&郭  盼^^^1400111\r"
////+"SPM|1|||2^尿液\r"
//+"SPM||&&aaaaaaaaaaaa||\r"
//+"OBX|1|ST|0102011^尿比重^^^SG||1.019||1.003-1.030|M||0|F|||||2^0^AAA^1.0030^1.0300\r"
//+"OBX|2|ST|0102012^酸碱度^^^PH||6.00||4.5-8|M||0|F|||||2^0^AAA^4.5000^8.0000\r"
//+"OBX|3|ST|0102009^白细胞^^^LEU||阴性（-）||阴性|N||1|F|||||2^0^AAA\r"
//+"OBX|4|ST|0102008^亚硝酸盐^^^NIT||阴性（-）||阴性|N||1|F|||||2^0^AAA\r"
//+"OBX|5|ST|0102007^尿蛋白^^^PRO||阴性（-）||阴性|N||1|F|||||2^0^AAA\r"
//+"OBX|6|ST|0102010^尿葡萄糖^^^GLU||阴性（-）||阴性|N||1|F|||||2^0^AAA\r"
//+"OBX|7|ST|0102005^尿酮体^^^KET||阴性（-）||阴性|N||1|F|||||2^0^AAA\r"
//+"OBX|8|ST|0102003^尿胆原^^^URO||阴性（-）||阴性|N||1|F|||||2^0^AAA\r"
//+"OBX|9|ST|0102004^尿胆红素^^^BIL||阴性（-）||阴性|N||1|F|||||2^0^AAA\r"
//+"OBX|10|ST|0102006^尿潜血^^^ERY||阴性（-）||阴性|N||1|F|||||2^0^AAA\r"
//+"OBX|11|ST|0102001^颜色^^^COLOR||浅黄色|||||1|F|||||2^0^AAA\r"
//+"OBX|12|ST|0102803^透明度^^^TURB||澄清|||||1|F|||||2^0^AAA\r"
//+"OBX|13|ST|0103017^镜检^^^NY-JJ||未见异常|||M||1|F|||||2^0^AAA\r";

		
		System.out.println(str);
		
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
			System.out.println(patient_id);
			List<OUL_R24_ORDER> orderList=adt.getORDERAll();
			boolean flagss = true;

			System.out.println( "res:检验结果解析数据1" +"\r\n");
			for(int i=0;i<orderList.size();i++){
				try{
				OBR obr=orderList.get(i).getOBR();
//				String bar_code=obr.getObr2_PlacerOrderNumber().getEi3_UniversalID().getValue();
				String bar_code = orderList.get(i).getSPECIMEN(0).getSPM().getSpm2_SpecimenID().getEip1_PlacerAssignedIdentifier().getEi3_UniversalID().getValue();
				String shifouyouzhi="1";
				System.out.println("res:检验结果解析数据2" +shifouyouzhi+"\r\n");
				if(shifouyouzhi.equals("1")){
					String lis_item_code=obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().getValue();
//					String exam_doctor=obr.getObr32_PrincipalResultInterpreter().getNdl1_Name().getCnn2_FamilyName().getValue();
					String exam_doctor=obr.getObr34_Technician(0).getNdl1_Name().getCnn2_FamilyName().getValue();
					String exam_date=obr.getObr22_ResultsRptStatusChngDateTime().getTime().getValue();
					String approver=obr.getObr33_AssistantResultInterpreter(0).getNdl1_Name().getCnn2_FamilyName().getValue();
					String approve_date=obr.getObr8_ObservationEndDateTime().getTime().getValue();
					List<OBX> obxList=orderList.get(i).getSPECIMEN(0).getOBXAll();
					System.out.println( "res:检验结果解析数据3" +obxList.size()+"\r\n");
					for(int j=0;j<obxList.size();j++){
						try{
						OBX obx=obxList.get(j);
						System.out.println("res:检验结果解析数据4" +obx.getObx11_ObservationResultStatus().getValue()+"\r\n");
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
//						if(ref_indicator==null || "".equals(ref_indicator)){
//							if(exam_result!=null && !"".equals(exam_result)){
//								if(exam_result.contains("阴")){
//									ref_indicator="0";
//								}else if(exam_result.contains("阳")){
//									ref_indicator="3";
//								}else{
//									ref_indicator="0";
//								}
//							}else {
//								ref_indicator="0";
//							}
//						}
						
						ProcListResult plr = new ProcListResult();
						plr.setBar_code(bar_code);
						plr.setExam_num("");
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
//						int resflag = commService.doproc_Lis_result(plr);
//						if (resflag != 0) {
//							resflag = commService.doproc_Lis_result(plr);
//							if (resflag != 0) {
//								resflag = commService.doproc_Lis_result(plr);
//								flagss=false;
//							}
//						}
						}catch(Exception ex){
							ex.printStackTrace();
						}
				}
			}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				res=XYYYMsgUtil.ACKCommon("ACK", "R24", messageId,"AA");
				System.out.println("AA");
				}
		
			//}
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(""+com.hjw.interfaces.util.StringUtil.formatException(e));
			res=XYYYMsgUtil.ACKCommon("ACK", "R24", messageId,"AE");	
		}
		
	}
	private static String dateutilto(String date){
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8)+" "+date.substring(8,10)+":"+date.substring(10,12)+":"+date.substring(12,14);
	}
}
