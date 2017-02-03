package com.devcortes.components.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Class for already parsed links
 * 
 * @author cortes
 *
 */
public class ParsePage {

	private String url;
	private Integer depth;
	private String parentUrl;
	private Set<String> localLinks;

	public ParsePage() {
	}

	public ParsePage(String url, Integer deph, String parentUrl) {

		this.url = url;
		this.depth = deph;
		this.parentUrl = parentUrl;
		this.localLinks = new HashSet<>();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

	public Set<String> getLocalLinks() {
		return localLinks;
	}

	public void setLocalLinks(Set<String> localLinks) {
		this.localLinks = localLinks;
	}

}