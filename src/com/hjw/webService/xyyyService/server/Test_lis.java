package com.hjw.webService.xyyyService.server;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.xyyyService.util.XYYYHL7Message;
import com.hjw.webService.xyyyService.util.XYYYMsgUtil;
import com.hjw.wst.DTO.ProcListResult;
import com.hjw.wst.DTO.ProcPacsResult;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.datatype.CE;
import ca.uhn.hl7v2.model.v251.datatype.CX;
import ca.uhn.hl7v2.model.v251.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v251.group.OUL_R24_ORDER;
import ca.uhn.hl7v2.model.v251.message.ORU_R01;
import ca.uhn.hl7v2.model.v251.message.OUL_R24;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.model.v251.segment.OBX;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

public class Test_lis {
	public static void main(String[] args) {
		String str ="MSH|^~\\&|MEDEX|10|HIS|10|20180709134907||ORU^R01^ORU_R01|USBG20180709134907|P|2.5.1||||CHN|UNICODE UTF-8\r"
    		   +"PID|||000853094200^^^^21||吴佳|||F||||||||\r"
+"PV1||T|||||||||||||||||000853094200^1|||||||||||000000|||||000000|||||||||0000\r"
+"ORC||18012507109^HIS^18012507109^HIS|1101211807090908521515354^MEDEX^1101211807090908521515354^MEDEX||CM||||\"\"|||李军|心电图||0000\r"
+"OBR||18012507109^HIS^18012507109^HIS|1101211807090908521515354^MEDEX|001^电脑多导联心电图^^04^心血管病检查|||20180709134907|20180709134907||||||||||||||20180709134859|||F|||||||李军|李军|^^^心电图\r"
+"OBX|1|TX|1037^检查所见^LN||||||||F|||20180709090954\r"
+"OBX|2|ST|1038^检查印象^LN||窦性心律，正常心电图||||||F|||20180709134907";
		Parser p = new GenericParser();
		Message msg = null;
		String res="";
		String messageId="";
		try {
			msg = p.parse(str);
			ORU_R01 adt = (ORU_R01) msg;
			MSH msh = adt.getMSH();
			 messageId = msh.getMessageControlID().getValue();

			PID pid = adt.getPATIENT_RESULT(0).getPATIENT().getPID();
			String patient_id = pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().getValue();
			//String exam_num = queryExamNum(patient_id,testlis);
			ORC orc = adt.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getORC();
			String pacs_req_code = orc.getOrc2_PlacerOrderNumber().getEi3_UniversalID().getValue();
			
			OBR obr = adt.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getOBR();
			String type_code=obr.getObr4_UniversalServiceIdentifier().getCe4_AlternateIdentifier().getValue();
			//---begin
			//ftp下载图片返回图片路径
			String img_file = "";
			/*try{
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
			}*/
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
			plr.setExam_num("121313");
			plr.setPacs_req_code(pacs_req_code);
			plr.setCheck_date(check_date);
			plr.setCheck_doct(check_doct);
			plr.setAudit_date(audit_date);
			plr.setAudit_doct(audit_doct);
			plr.setExam_result(exam_result);
			plr.setExam_desc(exam_desc);
			plr.setImg_file(img_file);//图像文件路径以分号隔开
			//结果写入正式库
			
		} catch (Exception e) {
			System.out.println(com.hjw.interfaces.util.StringUtil.formatException(e));
			e.printStackTrace();
		
		}
		
	}
}
