package com.hjw.webService.client.dbgj.test;

import java.util.ArrayList;
import java.util.List;

import com.hjw.util.DateTimeUtil;
import com.hjw.webService.client.LISDELSendMessage;
import com.hjw.webService.client.LISSendMessage;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultLisBody;


public class DelLisSendMessage_test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://192.168.111.46:8086/services/Mirth?wsdl";
		LisMessageBody pb = new LisMessageBody();
		pb.setId_extension("TI021");//写死
		pb.setMessageid("");//交互流水号
		pb.setPart_name("东北国际医院");
		pb.setCreationTime_value(DateTimeUtil.getDateTimes());
		pb.getCustom().setPersonid("1234567890");// 患者ID
		pb.getCustom().setPersonno("");//就诊号
		pb.getCustom().setPersonidnum("612322197710141516");//身份证号
		pb.getCustom().setPersioncode("");// 医保卡号
		pb.getCustom().setName("李俊");
		pb.getCustom().setSexcode("1");//1男 2女
		pb.getCustom().setBirthtime("19800101");
		pb.getCustom().setComname("北京火箭蛙信息技术有限公司");
		pb.getCustom().setContact_name("");
		pb.getCustom().setContact_tel("");
		pb.getCustom().setEthnicGroupCode("");//民族编码
		pb.getCustom().setMeritalcode("");//婚否
		pb.getCustom().setOld(25);
		pb.getCustom().setTel("");
		
		pb.getDoctor().setDept_code("");//申请科室编码
		pb.getDoctor().setDept_name("");//申请科室名称
		pb.getDoctor().setDoctorCode("");//医生编码
		pb.getDoctor().setDoctorName("");//医生姓名
		pb.getDoctor().setTime("201610171655");//开单和确认时间

        List<LisComponents> liscomlist= new ArrayList<LisComponents>();
		LisComponents liscoms=new LisComponents();
		liscoms.setDatetime(DateTimeUtil.getDate());
		liscoms.setReq_no("tjl000001");
		liscomlist.add(liscoms);
		
		liscoms.setReq_no("tjl000002");
		liscomlist.add(liscoms);
		pb.setComponents(liscomlist);
		LISDELSendMessage pm = new LISDELSendMessage(pb);
		ResultLisBody rb = pm.lisSend(url,"1",false);
		System.out.println(rb.getResultHeader().getTypeCode() + "-" + rb.getResultHeader().getText());
	}

}
