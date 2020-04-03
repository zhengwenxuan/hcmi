package com.hjw.webService.client.bdyx.bean;

public class MessageGet {

	private String msgId = "";//消息全局唯一id，可以用来做去重
	private String head = "";//消息头，参考 消息头结构
	private String body = "";//消息体中的结构参见服务设计书
	
	public String getMsgId() {
		return msgId;
	}
	public String getHead() {
		return head;
	}
	public String getBody() {
		return body;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getHead(int index) {
		String[] split = this.head.split(":");
		if(split.length > index) {
			return split[index];
		}
		return head;
	}
}
