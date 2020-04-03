package com.hjw.webService.client.dbgj.test;

import java.util.ArrayList;
import java.util.List;

import com.hjw.util.DateTimeUtil;
import com.hjw.webService.client.PACSDELSendMessage;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;

public class PacsDelSendMessage_test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://192.168.111.166:8089/services/Mirth?wsdl";
		PacsMessageBody pb = new PacsMessageBody();
		pb.setId_extension("TI004");//写死
		pb.setMessageid("");//交互流水号
		pb.setPart_name("东北国际医院");
		pb.setCreationTime_value(DateTimeUtil.getDateTimes());
		pb.getCustom().setPersonid("1234567890");// 患者ID
		pb.getCustom().setPersonno("");//就诊号
		pb.getCustom().setPersonidnum("612322197710141516");//身份证号
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
		
		pb.getDoctor().setDept_code("");//申请科室编码
		pb.getDoctor().setDept_name("");//申请科室名称
		pb.getDoctor().setDoctorCode("");//医生编码
		pb.getDoctor().setDoctorName("");//医生姓名
		pb.getDoctor().setTime(DateTimeUtil.getDateTimes());//开单和确认时间
		
		List<PacsComponents> pacsList = new ArrayList<PacsComponents>();
		
		PacsComponents pcts = new PacsComponents();
		pcts.setDatetime(DateTimeUtil.getDate());
		pcts.setReq_no("TJP000001");
		
		List<PacsComponent> itemList = new ArrayList<PacsComponent>();
		PacsComponent pc= new PacsComponent();
        pc.setExam_class("CT");
        pc.setItemDate(DateTimeUtil.getDate());
        pc.setItemName("全身骨显像");
        pc.setItemtime(DateTimeUtil.getDateTimes());
        itemList.add(pc);
        pcts.setPacsComponent(itemList);
        pacsList.add(pcts);
        pb.setComponents(pacsList);
        
        pcts = new PacsComponents();
		pcts.setDatetime(DateTimeUtil.getDate());
		pcts.setReq_no("TJP000002");
        
        pc= new PacsComponent();
        pc.setExam_class("US");
        pc.setItemDate(DateTimeUtil.getDate());
        pc.setItemName("腹部彩照");
        pc.setItemtime(DateTimeUtil.getDateTimes());	
		itemList.add(pc);
		pcts.setPacsComponent(itemList);
		pacsList.add(pcts);
        pb.setComponents(pacsList);
		
		PACSDELSendMessage pm = new PACSDELSendMessage(pb);
		ResultPacsBody rb = pm.pacsSend(url,"1", true);
		System.out.println(rb.getResultHeader().getTypeCode() + "-" + rb.getResultHeader().getText());
	}

}
