package com.hjw.webService.client.dashiqiao.FeeResBean;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.dashiqiao.ResCusBean.Coding;

public class Type {
	private List<Coding> coding = new ArrayList<>();
	private String text;

	public List<Coding> getCoding() {
		return coding;
	}

	public void setCoding(List<Coding> coding) {
		this.coding = coding;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
