package com.hjw.webService.client.Bean;

import java.io.Serializable;

public class ZlReqMqBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;

	private String messagetype;
	
	private String messages="";

	private String createtime="";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessagetype() {
		return messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

		
}
