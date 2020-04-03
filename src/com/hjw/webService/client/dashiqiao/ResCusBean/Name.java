package com.hjw.webService.client.dashiqiao.ResCusBean;

import java.util.ArrayList;
import java.util.List;

public class Name {

	private String use;
	private String text;
	
	private List<String> given = new ArrayList<>();
	
	
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<String> getGiven() {
		return given;
	}
	public void setGiven(List<String> given) {
		this.given = given;
	}
	
	
	
}
