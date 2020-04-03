package com.hjw.webService.client.dashiqiao.ResCusBean;

import java.util.ArrayList;
import java.util.List;

public class Identifier {

	private String use;
	private String value;

	private List<Type> Type = new ArrayList<>();

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Type> getType() {
		return Type;
	}

	public void setType(List<Type> type) {
		Type = type;
	}

	

	
	
	

}
