package com.hjw.webService.client.dbgj.test;

import java.util.ArrayList;
import java.util.List;

import com.hjw.util.DateTimeUtil;
import com.hjw.webService.client.PACSSendMessage;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;


public class PacsSendMessage_test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://192.168.111.46:8086/services/Mirth?wsdl";
		PacsMessageBody pb = new PacsMessageBody();
		pb.setId_extension("TI001");//写死
		pb.setMessageid("");//交互流水号
		pb.setPart_name("东北国际医院");
		pb.setCreationTime_value(DateTimeUtil.getDateTimes());
		pb.getCustom().setPersonid("1234567890");// 患者ID
		pb.getCustom().setPersonno("");//就诊号 空
		pb.getCustom().setPersonidnum("612322197710141516");//身份证号
		pb.getCustom().setExam_num("T1000111111");
		pb.getCustom().setPersioncode("");// 医保卡号
		pb.getCustom().setName("李俊");
		pb.getCustom().setSexcode("男");
		pb.getCustom().setBirthtime("19800101");
		pb.getCustom().setComname("北京火箭蛙信息技术有限公司");
		pb.getCustom().setContact_name("");
		pb.getCustom().setContact_tel("");
		pb.getCustom().setEthnicGroupCode("");//民族编码
		pb.getCustom().setMeritalcode("");//婚否
		pb.getCustom().setOld(25);
		pb.getCustom().setTel("");
		
		pb.getDoctor().setDept_code("TJ001");//申请科室编码
		pb.getDoctor().setDept_name("体检科");//申请科室名称
		pb.getDoctor().setDoctorCode("T1001110");//医生编码
		pb.getDoctor().setDoctorName("张三");//医生姓名
		pb.getDoctor().setTime(DateTimeUtil.getDateTimes());//开单和确认时间
		
		List<PacsComponents> comsList = new ArrayList<PacsComponents>();
		PacsComponents pcoms=new PacsComponents();
		pcoms.setReq_no("100011");
		pcoms.setDatetime(DateTimeUtil.getDate());
		
		List<PacsComponent> itemList = new ArrayList<PacsComponent>();
		PacsComponent pc= new PacsComponent();

        pc.setExam_class("EL");
        pc.setItemDate(DateTimeUtil.getDate());
        pc.setItemName("心电图");
        pc.setItemtime(DateTimeUtil.getDateTimes());
        pc.setServiceDeliveryLocation_code("TJ11001");
        pc.setServiceDeliveryLocation_name("体检中心");
        itemList.add(pc);
        pcoms.setPacsComponent(itemList);        
        comsList.add(pcoms);
        
        pcoms=new PacsComponents();
		pcoms.setReq_no("100012");
		pcoms.setDatetime(DateTimeUtil.getDate());
        
        pc= new PacsComponent();
        pc.setExam_class("US");
        pc.setItemDate(DateTimeUtil.getDate());
        pc.setItemName("腹部彩照");
        pc.setItemtime(DateTimeUtil.getDateTimes());	
        pc.setServiceDeliveryLocation_code("TJ11001");
        pc.setServiceDeliveryLocation_name("体检中心");
		itemList.add(pc);
		pcoms.setPacsComponent(itemList);		
		comsList.add(pcoms);
		
		pb.setComponents(comsList);		
		PACSSendMessage pm = new PACSSendMessage(pb);
		ResultPacsBody rb = pm.pacsSend(url,"1", false);
		System.out.println(rb.getResultHeader().getTypeCode() + "-" + rb.getResultHeader().getText());
	}

}
