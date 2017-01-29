package com.devcortes.components.entity;

import java.util.Set;

public class LinksList {
	private String url;
	private Integer deph;
	private Set<String> listUrl;
	private String parentUrl;		
	
	public LinksList() {}
	
	public LinksList(String url, Integer deph, Set<String> listUrl, String parentUrl ){		
		this.url = url;
		this.deph = deph;
		this.listUrl = listUrl;
		this.parentUrl = parentUrl;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getDeph() {
		return deph;
	}
	public void setDeph(Integer deph) {
		this.deph = deph;
	}
	public Set<String> getListUrl() {
		return listUrl;
	}
	public void setListUrl(Set<String> listUrl) {
		this.listUrl = listUrl;
	}
	public String getParentUrl() {
		return parentUrl;
	}
	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}
}
