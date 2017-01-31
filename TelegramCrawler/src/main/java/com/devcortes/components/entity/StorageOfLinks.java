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
public class StorageOfLinks {
	
	private String rootUrl;
	private String rootDomain;
	private Integer globalDepth;
	private Set<AlreadyParsedLink> alreadyParsedLinksWithRootDomain;
	private Set<String> allLinks;
	private Set<String> externalLinks;
	private Set<String> localSet;

	public StorageOfLinks() {

	}

	public StorageOfLinks(String url, Integer depth, String domen) {
		
		this.rootUrl = url;
		this.rootDomain = domen;
		this.globalDepth = depth;
		this.alreadyParsedLinksWithRootDomain = new HashSet<>();
		this.allLinks = new HashSet<>();
		this.externalLinks = new HashSet<>();
		this.localSet = new HashSet<>();
	}

	public String getRootDomain() {
		return rootDomain;
	}

	public void setRootDomain(String mainDomain) {
		this.rootDomain = mainDomain;
	}

	public Integer getGlobalDepth() {
		return globalDepth;
	}

	public void setGlobalDepth(Integer globalDeph) {
		this.globalDepth = globalDeph;
	}

	public Set<AlreadyParsedLink> getAlreadyParsedLinksWithRootDomain() {
		return alreadyParsedLinksWithRootDomain;
	}

	public void setAlreadyParsedLinksWithRootDomain(Set<AlreadyParsedLink> setLinks) {
		this.alreadyParsedLinksWithRootDomain = setLinks;
	}

	public Set<String> getAllLinks() {
		return allLinks;
	}

	public void setAllLinks(Set<String> allSetLink) {
		this.allLinks = allSetLink;
	}

	public Set<String> getExternalLinks() {
		return externalLinks;
	}

	public void setExternalLinks(Set<String> alienLink) {
		this.externalLinks = alienLink;
	}

	public Set<String> getLocalSet() {
		return localSet;
	}

	public void setLocalSet(Set<String> localSet) {
		this.localSet = localSet;
	}

	public String getRootUrl() {
		return rootUrl;
	}

	public void setRootUrl(String url) {
		this.rootUrl = url;
	}

}
