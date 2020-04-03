package com.hjw.webService.client.dashiqiao.FeeResBean;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.dashiqiao.ResCusBean.Identifier;
import com.hjw.webService.client.dashiqiao.ResCusBean.Meta;

import com.hjw.webService.client.dashiqiao.LisResBean.subject;


public class FeeResBean {

	
	private String resourceType;
	private String id;//姓名
	private Meta meta =new Meta();//
	private List<Identifier> identifier = new ArrayList<Identifier>();
	private String status;
	private Type type = new Type();
	private String EXAM_CHARGEITEM_CODE;//对应体检系统编码
	private String STATUS;//收费状态
	private subject subject = new subject();
	private List<LineItem> lineItem = new ArrayList<LineItem>();
	private String date;
	private TotalGross totalGross =new TotalGross();
	private List<Extension> extension = new ArrayList<Extension>();
	
	
	public List<Extension> getExtension() {
		return extension;
	}
	public void setExtension(List<Extension> extension) {
		this.extension = extension;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	public List<Identifier> getIdentifier() {
		return identifier;
	}
	public void setIdentifier(List<Identifier> identifier) {
		this.identifier = identifier;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getEXAM_CHARGEITEM_CODE() {
		return EXAM_CHARGEITEM_CODE;
	}
	public void setEXAM_CHARGEITEM_CODE(String eXAM_CHARGEITEM_CODE) {
		EXAM_CHARGEITEM_CODE = eXAM_CHARGEITEM_CODE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public subject getSubject() {
		return subject;
	}
	public void setSubject(subject subject) {
		this.subject = subject;
	}
	public List<LineItem> getLineItem() {
		return lineItem;
	}
	public void setLineItem(List<LineItem> lineItem) {
		this.lineItem = lineItem;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public TotalGross getTotalGross() {
		return totalGross;
	}
	public void setTotalGross(TotalGross totalGross) {
		this.totalGross = totalGross;
	}
	
	
	

	
	
	
	
}
