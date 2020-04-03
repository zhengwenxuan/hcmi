package com.hjw.webService.client.dashiqiao.LisResBean;

import com.hjw.webService.client.dashiqiao.ResCusBean.Response;

public class entry {

	private String fullUrl;
	private resource resource = new resource();
	
	private search search = new search();
	private Response response = new  Response();

	
	
	
	public search getSearch() {
		return search;
	}

	public void setSearch(search search) {
		this.search = search;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public resource getResource() {
		return resource;
	}

	public void setResource(resource resource) {
		this.resource = resource;
	}

}
