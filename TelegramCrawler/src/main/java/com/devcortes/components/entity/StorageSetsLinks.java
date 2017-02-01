package com.devcortes.components.entity;

import java.util.HashSet;
import java.util.Set;

public class StorageSetsLinks {
	private String url;
	private String mainDomain;
	private Integer globalDeph;
	private Set<Link> setLinks;
	private Set<String> allSetLink;
	private Set<String> alienLink;
	private Set<String> localSet;

	public StorageSetsLinks() {

	}

	public StorageSetsLinks(String url, Integer depth, String domen) {
		this.url = url;
		this.mainDomain = domen;
		this.globalDeph = depth;
		this.setLinks = new HashSet<>();
		this.allSetLink = new HashSet<>();
		this.alienLink = new HashSet<>();
		this.localSet = new HashSet<>();
	}

	public String getMainDomain() {
		return mainDomain;
	}

	public void setMainDomain(String mainDomain) {
		this.mainDomain = mainDomain;
	}

	public Integer getGlobalDeph() {
		return globalDeph;
	}

	public void setGlobalDeph(Integer globalDeph) {
		this.globalDeph = globalDeph;
	}

	public Set<String> getAllSetLink() {
		return allSetLink;
	}

	public void setAllSetLink(Set<String> allSetLink) {
		this.allSetLink = allSetLink;
	}

	public Set<String> getAlienLink() {
		return alienLink;
	}

	public void setAlienLink(Set<String> alienLink) {
		this.alienLink = alienLink;
	}

	public Set<String> getLocalSet() {
		return localSet;
	}

	public void setLocalSet(Set<String> localSet) {
		this.localSet = localSet;
	}

	public Set<Link> getSetLinks() {
		return setLinks;
	}

	public void setSetLinks(Set<Link> setLinks) {
		this.setLinks = setLinks;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
