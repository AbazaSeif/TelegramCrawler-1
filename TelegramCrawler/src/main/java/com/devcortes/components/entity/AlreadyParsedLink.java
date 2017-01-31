package com.devcortes.components.entity;

import java.util.Set;

import org.springframework.stereotype.Component;

/**
 * Class for already parsed links
 * 
 * @author cortes
 *
 */

public class AlreadyParsedLink {
	
	private String currentUrl;
	private Integer currentDepth;
	private Set<String> setLinksOnCurrentUrl;
	private String parentUrl;

	public AlreadyParsedLink() {
	}

	public AlreadyParsedLink(String url, Integer deph, Set<String> listUrl, String parentUrl) {
		
		this.currentUrl = url;
		this.currentDepth = deph;
		this.setLinksOnCurrentUrl = listUrl;
		this.parentUrl = parentUrl;
	}

	public String getCurrentUrl() {
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}

	public Integer getCurrentDepth() {
		return currentDepth;
	}

	public void setCurrentDepth(Integer currentDepth) {
		this.currentDepth = currentDepth;
	}

	public Set<String> getSetLinksOnCurrentUrl() {
		return setLinksOnCurrentUrl;
	}

	public void setSetLinksOnCurrentUrl(Set<String> setLinksOnCurrentUrl) {
		this.setLinksOnCurrentUrl = setLinksOnCurrentUrl;
	}

	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

}
