package com.devcortes.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.devcortes.components.entity.LinksList;

@Service
public class CrawlerService {
	private static Logger log = Logger.getLogger(CrawlerService.class.getName());
	private String mainDomain;
	private Integer globalDeph;
	private Set<LinksList> setLinks;
	private Set<String> allSetLink;
	private Set<String> alienLink;
	private Set<String> localSet;
	/**
	 * It's method that run crawler for url with some deph.
	 * 
	 * @param url
	 * @param deph
	 * @throws Exception
	 */
	public boolean runCrawler(String url, Integer deph) {
		setLinks = new HashSet<>();
		allSetLink = new HashSet<>();
		alienLink = new HashSet<>();
		globalDeph = deph;
		mainDomain = getDomain(url);
		Set<String> fListURL = null;
		if(returnURL(url)){
			fListURL = localSet;
		}
		if (fListURL != null) {
			LinksList ll = new LinksList(url, 0, fListURL, "");
			setLinks.add(ll);
			try {
				recursiveCrawl(ll, 0);
			} catch (IOException e) {
				log.info("RunCrawler ---  " + e.getMessage());
			}
			return true;
		} else {
			return false;
		}

	}

	/**
	 * It's method that search main domain from general url;
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	public String getDomain(String url) {
		String domen = null;
		try {
			URL nURL = new URL(url);
			domen = nURL.getHost();
			domen = domen.startsWith("www.") ? domen.substring(4) : domen;

		} catch (MalformedURLException e) {
			log.info("GetDomain ---  " + e.getMessage());
		}
		return domen;
	}

	/**
	 * It's recursive method for parse links.
	 * 
	 * @param listLink
	 * @param deph
	 * @throws IOException
	 */
	public void recursiveCrawl(LinksList listLink, int deph) throws IOException {
		deph++;
		for (String link : listLink.getListUrl()) {
			Set<String> recurSive = null;
			if(returnURL(link)){
				recurSive = localSet;
			}
			LinksList ll = new LinksList(link, deph, recurSive, listLink.getUrl());
			setLinks.add(ll);
			if (recurSive != null && deph < globalDeph) {
				recursiveCrawl(ll, deph);
			}
		}
	}

	/**
	 * It's method that return list links for given url;
	 * 
	 * @param urlsend
	 * @return
	 * @throws IOException
	 */
	public boolean returnURL(String urlsend) {	
		localSet = null;
		if (urlsend != null && !urlsend.isEmpty()) {
			String localDomain;
			localSet = new HashSet<>();
			try {
				Document doc = Jsoup.connect(urlsend).get();
				if (doc != null) {
					Elements links = doc.select("a");
					String finalString;
					for (Element e : links) {
						finalString = e.attr("abs:href");
						localDomain = getDomain(finalString);
						if (localDomain.equals(mainDomain)) {
							if (!allSetLink.contains(finalString)) {
								localSet.add(finalString);
								allSetLink.add(finalString);
							}
						} else {
							alienLink.add(finalString);
							allSetLink.add(finalString);
						}
					}
				}
			} catch (Exception e) {
				log.info("ReturnURL ---  " + e.getMessage());
			}

			allSetLink.add(urlsend);

			if (!localSet.isEmpty()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public String getMainDomain() {
		return mainDomain;
	}

	public void setMainDomain(String mainDomain) {
		this.mainDomain = mainDomain;
	}

	public Set<LinksList> getSetLinks() {
		return setLinks;
	}

	public void setSetLinks(Set<LinksList> setLinks) {
		this.setLinks = setLinks;
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

}
