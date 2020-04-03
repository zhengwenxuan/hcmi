package com.hjw.webService.client.Bean;

public class QueueNextBean {
	
	private String code="";//体检编号id，必填
	private String currentQueueName="";//当前位置名称, 可选填
    private String nextQueueName="";//下一位置名称, 可选填
    private String nextIndex=""; //下一位置队列位置名称, 可选填，当nextQueueName由值时，必填
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCurrentQueueName() {
		return currentQueueName;
	}
	public void setCurrentQueueName(String currentQueueName) {
		this.currentQueueName = currentQueueName;
	}
	public String getNextQueueName() {
		return nextQueueName;
	}
	public void setNextQueueName(String nextQueueName) {
		this.nextQueueName = nextQueueName;
	}
	public String getNextIndex() {
		return nextIndex;
	}
	public void setNextIndex(String nextIndex) {
		this.nextIndex = nextIndex;
	}


}
