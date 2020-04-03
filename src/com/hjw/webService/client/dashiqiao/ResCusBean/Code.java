package com.hjw.webService.client.dashiqiao.ResCusBean;

import java.util.ArrayList;
import java.util.List;

public class Code {

	private List<Coding> coding = new ArrayList<>();//
	private String text ;
	
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Coding> getCoding() {
		return coding;
	}

	public void setCoding(List<Coding> coding) {
		this.coding = coding;
	}

	
	
	
	
}
