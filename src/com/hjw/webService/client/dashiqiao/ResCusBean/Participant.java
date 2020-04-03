package com.hjw.webService.client.dashiqiao.ResCusBean;

import java.util.ArrayList;
import java.util.List;

public class Participant {

	private String status;
	private String required;
	private Actor actor =new Actor();
	private List<Type> type = new ArrayList<>();
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public Actor getActor() {
		return actor;
	}
	public void setActor(Actor actor) {
		this.actor = actor;
	}
	public List<Type> getType() {
		return type;
	}
	public void setType(List<Type> type) {
		this.type = type;
	}
	
	
	
}
