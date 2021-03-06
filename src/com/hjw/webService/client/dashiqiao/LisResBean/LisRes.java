package com.hjw.webService.client.dashiqiao.LisResBean;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.dashiqiao.ResCusBean.Link;
import com.hjw.webService.client.dashiqiao.ResCusBean.Meta;

public class LisRes {

	private String resourceType;
	private String id;
	private Meta meta = new Meta();
	private String type;
	private String total;
	private List<Link> link = new ArrayList<>();
	private List<entry> entry = new ArrayList<>();

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<Link> getLink() {
		return link;
	}

	public void setLink(List<Link> link) {
		this.link = link;
	}

	public List<entry> getEntry() {
		return entry;
	}

	public void setEntry(List<entry> entry) {
		this.entry = entry;
	}

}
