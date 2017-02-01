package com.devcortes.components.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

/**
 * Model that have a few sets contain different parsed links
 * 
 * @author cortes
 *
 */
public class StorageResult {
	
	private String url;
	private String domain;
	private Integer accessDepth;
	private Set<ParsePage> parsePages;
	private Set<String> uniqeuLinks;
	private Set<String> externalLinks;

	public StorageResult() {
	}

	public StorageResult(String url, Integer depth, String domen) {
		
		this.url = url;
		this.domain = domen;
		this.accessDepth = depth;
		this.parsePages = new HashSet<>();
		this.uniqeuLinks = new HashSet<>();
		this.externalLinks = new HashSet<>();	
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Integer getAccessDepth() {
		return accessDepth;
	}

	public void setAccessDepth(Integer accessDepth) {
		this.accessDepth = accessDepth;
	}

	public Set<ParsePage> getParsePages() {
		return parsePages;
	}

	public void setParsePages(Set<ParsePage> parsePages) {
		this.parsePages = parsePages;
	}

	public Set<String> getUniqeuLinks() {
		return uniqeuLinks;
	}

	public void setUniqeuLinks(Set<String> uniqeuLinks) {
		this.uniqeuLinks = uniqeuLinks;
	}

	public Set<String> getExternalLinks() {
		return externalLinks;
	}

	public void setExternalLinks(Set<String> externalLinks) {
		this.externalLinks = externalLinks;
	}

	

}
