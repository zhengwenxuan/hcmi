package com.hjw.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hjw.util.DateTimeUtil;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.bjxy.util.BJXYhl7;
import com.hjw.webService.client.bjxy.util.XyyyClient;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.wst.DTO.JobDTO;
import com.hjw.wst.DTO.UserDTO;
import com.hjw.wst.service.LisPacsApplicationService;

public class XyLisTest {
//	public static void main(String[] args) throws IOException {
		
//		
//		
//		
//		
//		
//		
//		
//			LisMessageBody lismessage = new LisMessageBody();
//			
//			lismessage.setId_extension("TI021");//写死
//			lismessage.setMessageid("");//交互流水号
//			lismessage.setPart_name("东北国际医院");
//			lismessage.setCreationTime_value(DateTimeUtil.getDateTimes());
//			lismessage.getCustom().setPersonid(examInfo.getPatient_id());// 患者ID
//			lismessage.getCustom().setPersonno(examInfo.getVisit_no());//就诊号
//			lismessage.getCustom().setExam_num(examInfo.getExam_num());
//			lismessage.getCustom().setPersonidnum(customerInfo.getId_num());//身份证号
//			lismessage.getCustom().setPersioncode("");// 医保卡号
//			lismessage.getCustom().setMc_no(examInfo.getMc_no());//就诊卡号
//			lismessage.getCustom().setName(customerInfo.getUser_name());
//			lismessage.getCustom().setSexcode(customerInfo.getSex());
//			lismessage.getCustom().setBirthtime(DateTimeUtil.shortFmt4(customerInfo.getBirthday()));
//			lismessage.getCustom().setComname(examInfo.getCompany());
//			lismessage.getCustom().setContact_name("");
//			lismessage.getCustom().setContact_tel("");
//			lismessage.getCustom().setEthnicGroupCode("");//民族编码
//			lismessage.getCustom().setMeritalcode(examInfo.getIs_marriage());//婚否
//			lismessage.getCustom().setOld(new Long(examInfo.getAge()).intValue());
//			lismessage.getCustom().setTel(examInfo.getPhone());
//			lismessage.getCustom().setAddress(examInfo.getAddress());
//			if("男".equals(customerInfo.getSex())){
//				lismessage.getCustom().setSexcode("1");
//				lismessage.getCustom().setSexname("男");
//			}else if("女".equals(customerInfo.getSex())){
//				lismessage.getCustom().setSexcode("2");
//				lismessage.getCustom().setSexname("女");
//			}else{
//				lismessage.getCustom().setSexcode("3");
//				lismessage.getCustom().setSexname("未知");
//			}
//			List<JobDTO> dataList = companyService.getDatadis("SQKS");
//			if(dataList.size() == 0){
//				return "error-请在数据字典中维护申请科室(data_code = SQKS)";
//			}
//			
//			lismessage.getDoctor().setDept_code(dataList.get(0).getRemark());//申请科室编码
//			lismessage.getDoctor().setDept_name(dataList.get(0).getName());//申请科室名称
//			lismessage.getDoctor().setDoctorCode(user.getWork_num());//医生编码
//			lismessage.getDoctor().setDoctorName(user.getName());//医生姓名
//			lismessage.getDoctor().setTime(DateTimeUtil.getDateTimes());//开单和确认时间
//			
//			
//			
//			
//			
//			
//			
//			ResultLisBody rb = new ResultLisBody();
//			List<LisComponents> components=lismessage.getComponents();
//			ControlActLisProcess con= new ControlActLisProcess();
//			List<ApplyNOBean> appList=new ArrayList<ApplyNOBean>();
//			for(int i=0;i<components.size();i++){
//				LisComponents liscoms= new LisComponents();
//				liscoms=components.get(i);
//				List<LisComponent> component=liscoms.getItemList();
//				boolean flags=true;
//				for(int j=0;j<component.size();j++){
//					
//					String str=BJXYhl7.omlO21hl7(lismessage, "O21", "NW",i,j,"LisTest");
//					ResultHeader rh=new XyyyClient().talkFee(str,"LISAPPLYTest");
//					
//					if(!"AA".equals(rh.getTypeCode())){
//						flags=false;
//						break;
//					}
//				}
//				if(flags){
//					ApplyNOBean aob= new ApplyNOBean();
//					aob.setApplyNO(liscoms.getReq_no());
//					appList.add(aob);
//				}
//			}
//	}
	public static void main(String[] args) {

	}
}
