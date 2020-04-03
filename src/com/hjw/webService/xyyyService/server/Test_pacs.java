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

public class Test_pacs {
	public static void main(String[] args) {
		
		String str ="MSH|^~\\&|MEDEX|10|HIS|10|20180329131611||ORU^R01^ORU_R01|USBG20180329131611|P|2.5.1||||CHN|UNICODE UTF-8\r"
                +"PID|||000827424300^^^^21||pacs测试1|||F||||||||\r"
                +"PV1||T|||||||||||||||||000827424300^1|||||||||||000000|||||000000|||||||||0000\r"
                +"ORC||18032900093^HIS^18032900093^HIS|USTJ2180329130718176^MEDEX^USTJ2180329130718176^MEDEX||CM||||\"\"|||纪艳丽|超声科门诊||0000\r"
                +"OBR||18032900093^HIS^18032900093^HIS|USTJ2180329130718176^MEDEX|052^彩超泌尿系：双肾、输尿管、膀胱^^01^B超|||20180329131231|20180329131611||||    双方的ASDFASFDASDF||||||||||20180329131611|||F|||||||纪艳丽|纪艳丽|^^^超声科门诊\r"
                +"OBX|1|TX|1037^检查所见^LN||    双方的ASDFASFDASDF||||||F|||20180329131231\r"
                +"OBX|2|ST|1038^检查印象^LN||    FSFSFSDAFAS||||||F|||20180329131231";
		str="MSH|^~\\&|MEDEX|10|HIS|10|20180716112229||ORU^R01^ORU_R01|USBG20180716112229|P|2.5.1||||CHN|UNICODE UTF-8\r"
				+"PID|||000844547200^^^^21||徐永辉|||M||||||||\r"
+"PV1||T|||||||||||||||||000844547200^1|||||||||||000000|||||000000|||||||||0000\r"
+"ORC||18012600870^HIS^18012600870^HIS|USTJ2180716111415181^MEDEX^USTJ2180716111415181^MEDEX||CM||||\"\"|||纪艳丽|超声科门诊||0000\r"
+"OBR||18012600870^HIS^18012600870^HIS|USTJ2180716111415181^MEDEX|023^彩色多普勒超声颈部血管^^01^B超|||20180716112021|20180716112229||||    右侧颈总动脉分叉处后壁内中膜厚约0.13cm，余双侧颈动脉内中膜无明显增厚，内壁不光滑，左侧颈总动脉分叉处后壁见稍强回声斑块，厚约0.24cm，右侧颈总动脉分叉处右前壁见混合回声斑块，厚约0.18cm，余管腔内未见异常回声。    CDFI：双侧颈动脉血流通畅，斑块处血流充盈缺损，频谱形态未见异常。    双侧椎动脉（起始部显示不清）内中膜无明显增厚，内壁光滑，管腔内未见明    CDFI：双侧椎动脉血流通畅，方向正常。||||||||||20180716112229|||F|||||||纪艳丽|纪艳丽|^^^超声科门诊\r"
+"OBX|1|TX|1037^检查所见^LN||    右侧颈总动脉分叉处后壁内中膜厚约0.13cm，余双侧颈动脉内中膜无明显增厚，内壁不光滑，左侧颈总动脉分叉处后壁见稍强回声斑块，厚约0.24cm，右侧颈总动脉分叉处右前壁见混合回声斑块，厚约0.18cm，余管腔内未见异常回声。    CDFI：双侧颈动脉血流通畅，斑块处血流充盈缺损，频谱形态未见异常。    双侧椎动脉（起始部显示不清）内中膜无明显增厚，内壁光滑，管腔内未见明显异常回声。    CDFI：双侧椎动脉血流通畅，方向正常。||||||F|||20180716112021\r"
+"OBX|2|ST|1038^检查印象^LN||    双侧颈动脉粥样硬化斑块形成||||||F|||20180716112021\r";
		
		str="MSH|^~\\&|MEDEX|10|HIS|10|20180807145159||ORU^R01^ORU_R01|USBG20180807145159|P|2.5.1||||CHN|UNICODE UTF-8\r"
				+"PID|||000850052200^^^^21||梁秀文|||F||||||||\r"
				+"PV1||T|||||||||||||||||000850052200^1|||||||||||000000|||||000000|||||||||0000\r"
				+"ORC||18012508993^HIS^18012508993^HIS|USTJ2180807141528374^MEDEX^USTJ2180807141528374^MEDEX||CM||||\"\"|||纪艳丽|超声科门诊||0000\r"
				+"OBR||18012508993^HIS^18012508993^HIS|USTJ2180807141528374^MEDEX|004^彩超妇科：子宫、附件、膀胱及周围^^01^B超|||20180807142154|20180807145159||||    经腹部超声检查结果如下：    子宫5.0*4.2*3.0cm，内膜厚约0.3cm，肌层见两三个低回声，大者约1.2*0.9cm，形态尚规则，边界尚清，CDFI：未见明显血流。    右侧卵巢1.9*1.0cm，左侧卵巢1.5*1.4cm。    双侧附件区均未见明确囊实性包块。    盆腔未见明显游离液性暗区。||||||||||20180807145159|||F|||||||纪艳丽|纪艳丽|^^^超声科门诊\r"
				+"OBX|1|TX|1037^检查所见^LN||    经腹部超声检查结果如下：    5 2018-08-07 14:42:40:收到/10.2.30.70:62898的消息:.0*4.2*3.0cm，内膜厚约0.3cm，肌层见两三个低回声，大者约1.2*0.9cm，形态尚规则，边界尚清，CDFI：未见明显血流。    右侧卵巢1.9*1.0cm，左侧卵巢1.5*1.4cm。    双侧附件区均未见明确囊实性包块。    盆腔未见明显游离液性暗区。||||||F|||20180807142154\r"
				+"OBX|2|ST|1038^检查印象^LN||    子宫多发小肌瘤||||||F|||20180807142154\r";
			
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
			//String exam_num = queryExamNum(patient_id,logname);
			ORC orc = adt.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getORC();
			String pacs_req_code = orc.getOrc2_PlacerOrderNumber().getEi3_UniversalID().getValue();
			
			OBR obr = adt.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getOBR();
			String type_code=obr.getObr4_UniversalServiceIdentifier().getCe4_AlternateIdentifier().getValue();
			//---begin
			//ftp下载图片返回图片路径
			
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
					if(exam_desc!=null&&exam_desc.length()>0){
						exam_desc=exam_desc;
					}else{
						exam_desc="";
					}
				} else if (type.equals("ST")) {
					exam_result = obx.getObx5_ObservationValue(0).getData().toString();
					if(exam_result!=null&&exam_result.length()>0){
						exam_result=exam_result;
					}else{
						exam_result="";
					}
				}
			}
			ProcPacsResult plr = new ProcPacsResult();
			//plr.setExam_num(exam_num);
			plr.setPacs_req_code(pacs_req_code);
			plr.setCheck_date(check_date);
			plr.setCheck_doct(check_doct);
			plr.setAudit_date(audit_date);
			plr.setAudit_doct(audit_doct);
			plr.setExam_result(exam_result);
			plr.setExam_desc(exam_desc);
			plr.setImg_file("");//图像文件路径以分号隔开

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
