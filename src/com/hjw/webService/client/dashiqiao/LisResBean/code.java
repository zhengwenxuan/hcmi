package com.hjw.webService.client.dashiqiao.LisResBean;

import java.util.ArrayList;
import java.util.List;

public class code {
	private List<coding> coding = new ArrayList<coding>();
	private  String text;
	
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<coding> getCoding() {
		return coding;
	}

	public void setCoding(List<coding> coding) {
		this.coding = coding;
	}
}
