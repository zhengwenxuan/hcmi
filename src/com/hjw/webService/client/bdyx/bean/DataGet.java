package com.hjw.webService.client.bdyx.bean;

import java.util.List;

public class DataGet {

	private List<MessageGet> messages;
	private List<String> messageIds;
	private Object lastOffsets;
	private boolean hasMsg;
	
	public List<String> getMessageIds() {
		return messageIds;
	}
	public Object getLastOffsets() {
		return lastOffsets;
	}
	public boolean isHasMsg() {
		return hasMsg;
	}
	public void setMessageIds(List<String> messageIds) {
		this.messageIds = messageIds;
	}
	public void setLastOffsets(Object lastOffsets) {
		this.lastOffsets = lastOffsets;
	}
	public void setHasMsg(boolean hasMsg) {
		this.hasMsg = hasMsg;
	}
	public List<MessageGet> getMessages() {
		return messages;
	}
	public void setMessages(List<MessageGet> messages) {
		this.messages = messages;
	}
}
