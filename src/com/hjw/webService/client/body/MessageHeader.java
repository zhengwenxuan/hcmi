package com.hjw.webService.client.body;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.Bean.dbgj   
     * @Description:  消息头
     * @author: yangm     
     * @date:   2016年10月7日 上午11:07:21   
     * @version V2.0.0.0
 */
public class MessageHeader {
	private String messageid;//交互id
	private String id_extension = "BS006";// 消息ID
	private String creationTime_value = "2012010611";// 消息创建时间
	private String processingCode_code = "P";// 消息用途: P(Production);
												// D(Debugging); T(Training)
	private String processingMode_Code = "T";// 消息处理模式: A(Archive); I(Initial
												// load); R(Restore from
												// archive); T(Current
												// processing)
	private String acceptAckCode_code = "NE";// 消息应答: AL(Always);
												// ER(Error/reject only);
												// NE(Never)
	private String receiver_id;// 接受者ID  暂时不填写
	private String sender_id;// 发送者ID    暂时不填写	

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public String getId_extension() {
		return id_extension;
	}

	public void setId_extension(String id_extension) {
		this.id_extension = id_extension;
	}

	public String getCreationTime_value() {
		return creationTime_value;
	}

	public void setCreationTime_value(String creationTime_value) {
		this.creationTime_value = creationTime_value;
	}

	public String getProcessingCode_code() {
		return processingCode_code;
	}

	public void setProcessingCode_code(String processingCode_code) {
		this.processingCode_code = processingCode_code;
	}

	public String getProcessingMode_Code() {
		return processingMode_Code;
	}

	public void setProcessingMode_Code(String processingMode_Code) {
		this.processingMode_Code = processingMode_Code;
	}

	public String getAcceptAckCode_code() {
		return acceptAckCode_code;
	}

	public void setAcceptAckCode_code(String acceptAckCode_code) {
		this.acceptAckCode_code = acceptAckCode_code;
	}

	public String getReceiver_id() {
		return receiver_id;
	}

	public void setReceiver_id(String receiver_id) {
		this.receiver_id = receiver_id;
	}

	public String getSender_id() {
		return sender_id;
	}

	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}

}
