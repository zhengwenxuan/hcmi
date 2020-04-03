package com.hjw.webService.client.yichang.bean.cdr.client.header;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.webService.client.yichang.bean.cdr.client.header.receiver.ReceiverYC;
import com.hjw.webService.client.yichang.bean.cdr.client.header.sender.SenderYC;
import com.hjw.webService.client.yichang.bean.cdr.client.hipPushRequestFormToXD.HIPMessageServer;
import com.hjw.webService.client.yichang.bean.cdr.client.hipPushRequestFormToXD.ResponseXD;
import com.hjw.webService.client.yichang.bean.cdr.client.hipPushRequestFormToXD.FindOrd;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "PRPA_IN000001UV03")  
@XmlType(propOrder = {})
public class PRPA_IN000001UV03 {
	
	@XmlElement
	private IdYC id;
	@XmlElement
	private CreationTime creationTime = new CreationTime();//
	@XmlElement
	private InteractionId interactionId = new InteractionId();//
	@XmlElement
	private ReceiverYC receiver = new ReceiverYC();//
	@XmlElement
	private SenderYC sender = new SenderYC();//
	@XmlElement
	private ControlActProcess controlActProcess = new ControlActProcess();//
	
	public PRPA_IN000001UV03() {
	}
	
	public PRPA_IN000001UV03(String arch_num) {
		id = new IdYC(arch_num);//
	}
	
	public IdYC getId() {
		return id;
	}
	public void setId(IdYC id) {
		this.id = id;
	}
	public CreationTime getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(CreationTime creationTime) {
		this.creationTime = creationTime;
	}
	public InteractionId getInteractionId() {
		return interactionId;
	}
	public void setInteractionId(InteractionId interactionId) {
		this.interactionId = interactionId;
	}
	public ReceiverYC getReceiver() {
		return receiver;
	}
	public void setReceiver(ReceiverYC receiver) {
		this.receiver = receiver;
	}
	public SenderYC getSender() {
		return sender;
	}
	public void setSender(SenderYC sender) {
		this.sender = sender;
	}
	public ControlActProcess getControlActProcess() {
		return controlActProcess;
	}
	public void setControlActProcess(ControlActProcess controlActProcess) {
		this.controlActProcess = controlActProcess;
	}
	
	public static void main(String[] args) {
		ResponseXD response = new ResponseXD();
		String responseXml = JaxbUtil.convertToXmlWithOutHead(response, true);
		
		HIPMessageServer hIPMessageServer = new HIPMessageServer();
		hIPMessageServer.setMessage(responseXml);
		
		PRPA_IN000001UV03 message = new PRPA_IN000001UV03("tijianhao");
		message.getControlActProcess().getSubject().getRequest().setHIPMessageServer(hIPMessageServer);
		System.out.println(JaxbUtil.convertToXmlWithCDATA(message, "^message"));
	}
}
