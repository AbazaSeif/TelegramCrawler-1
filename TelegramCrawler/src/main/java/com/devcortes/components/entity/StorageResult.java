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
	private Set<String> notParsedLinks;
	private Set<String> canNotFindDomen;

	public StorageResult() {
	}

	public StorageResult(String url, Integer depth, String domen) {

		this.url = url;
		this.domain = domen;
		this.accessDepth = depth;
		this.parsePages = new HashSet<>();
		this.uniqeuLinks = new HashSet<>();
		this.externalLinks = new HashSet<>();
		this.notParsedLinks = new HashSet<>();
		this.canNotFindDomen = new HashSet<>();
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

	public Set<String> getNotParsedLinks() {
		return notParsedLinks;
	}

	public void setNotParsedLinks(Set<String> notParsedLinks) {
		this.notParsedLinks = notParsedLinks;
	}

	public Set<String> getCanNotFindDomen() {
		return canNotFindDomen;
	}

	public void setCanNotFindDomen(Set<String> canNotFindDomen) {
		this.canNotFindDomen = canNotFindDomen;
	}

}