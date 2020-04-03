package com.hjw.webService.client.body;

public class HealthFileResBody {
	private String rescode = "";
	private long imageID;
	private String restext = "";
	private String filePath="";
	
	public String getRescode() {
		return rescode;
	}
	public void setRescode(String rescode) {
		this.rescode = rescode;
	}
	public long getImageID() {
		return imageID;
	}
	public void setImageID(long imageID) {
		this.imageID = imageID;
	}
	public String getRestext() {
		return restext;
	}
	public void setRestext(String restext) {
		this.restext = restext;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
		
}
