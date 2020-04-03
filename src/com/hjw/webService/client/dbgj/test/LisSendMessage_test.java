package com.hjw.webService.client.dbgj.test;

import java.util.ArrayList;
import java.util.List;

import com.hjw.util.DateTimeUtil;
import com.hjw.webService.client.LISSendMessage;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultLisBody;


public class LisSendMessage_test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://192.168.111.46:8086/services/Mirth?wsdl";
		LisMessageBody pb = new LisMessageBody();
		pb.setId_extension("TI021");//写死
		pb.setMessageid("");//交互流水号
		pb.setPart_name("东北国际医院");
		pb.setCreationTime_value(DateTimeUtil.getDateTimes());
		pb.getCustom().setPersonid("1234567890");// 患者ID
		pb.getCustom().setPersonno("T0001110");//就诊号 传体检编号 必须填写
		pb.getCustom().setPersonidnum("612322197710141516");//身份证号
		pb.getCustom().setExam_num("T0001199");
		pb.getCustom().setPersioncode("");// 医保卡号
		pb.getCustom().setName("李俊");
		pb.getCustom().setSexcode("男");//男 女
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
		liscoms.setReq_no("tjl123");
				
		LisComponent pc= new LisComponent();
		pc.setItemCode("10001");
		pc.setItemName("血液三项");
		pc.setExtension("T201601234");
		pc.setServiceDeliveryLocation_code("TJ010");
		pc.setServiceDeliveryLocation_name("体检中心");
		pc.setSpecimenNatural("111");
		pc.setSpecimenNaturalname("");
		
		List<LisComponent> itemList = new ArrayList<LisComponent>();
		itemList.add(pc);		
		
		pc= new LisComponent();
		pc.setItemCode("10002");
		pc.setItemName("血液无项");
		pc.setExtension("T201601235");
		pc.setServiceDeliveryLocation_code("TJ010");
		pc.setServiceDeliveryLocation_name("体检中心");
		pc.setSpecimenNatural("111");
		pc.setSpecimenNaturalname("");
		itemList.add(pc);
		liscoms.setItemList(itemList);
		
		liscomlist.add(liscoms);
		
		pb.setComponents(liscomlist);
		
		
		LISSendMessage pm = new LISSendMessage(pb);
		ResultLisBody rb = pm.lisSend(url,"1",true);
		System.out.println(rb.getResultHeader().getTypeCode() + "-" + rb.getResultHeader().getText());
	}

}
