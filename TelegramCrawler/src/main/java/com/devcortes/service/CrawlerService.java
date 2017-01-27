package com.devcortes.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.devcortes.components.entity.LinksList;


@Service
public class CrawlerService {	
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private String mainDomain;
	private Integer globalDeph;
	private HashSet<LinksList> setLinks = new HashSet<LinksList>();
	private Set<String> allSetLink = new HashSet<String>();
	private HashSet<String> alienLink = new HashSet<String>();

	/**
	 * It's method that run crawler for url with some deph.
	 * @param url
	 * @param deph
	 * @throws Exception 
	 */
	public HashSet<LinksList>  runCrawler(String url, Integer deph) throws Exception {
		globalDeph = deph;
		mainDomain = getDomain(url);
		HashSet<String> fListURL = returnURL(url);
		if (fListURL != null) {
			LinksList ll = new LinksList(url, 0, fListURL, "");
			setLinks.add(ll);
			recursiveCrawl(ll, 0);			
			return setLinks;
		} else {
			return null;
		}
		

	}	

	/**
	 * It's method that search main domain from general url;
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	public String getDomain(String url) throws Exception {
		String domen =null;
		try {
			URL nURL = new URL(url);
			domen = nURL.getHost();
			domen = domen.startsWith("www.") ? domen.substring(4) : domen;
		} catch (Exception e) {			
		}
		if(domen == null) return null;
		else return domen; 
	}

	/**
	 * It's recursive method for parse links. 
	 * @param listLink
	 * @param deph
	 * @throws IOException
	 */
	public void recursiveCrawl(LinksList listLink, int deph) throws IOException {
		deph++;
		for (String link : listLink.getListUrl()) {
			HashSet<String> recurSive = returnURL(link);
			LinksList ll = new LinksList(link, deph, recurSive, listLink.getUrl());
			setLinks.add(ll);
			if (recurSive != null) {
				if (deph < globalDeph) {
					recursiveCrawl(ll, deph);
				}
			}
		}
	}

	/**
	 * It's method that return list links for given url;
	 * @param urlsend
	 * @return
	 * @throws IOException
	 */
	public HashSet<String> returnURL(String urlsend) throws IOException {
		if (urlsend != null && !urlsend.equals("")) {
			String localDomain;
			HashSet<String> localSet = new HashSet<String>();
			try {
				Document doc = Jsoup.connect(urlsend).get();
				if (doc != null) {
					Elements links = doc.select("a");
					String FinalString = "";
					for (Element e : links) {
						FinalString = e.attr("abs:href");
						localDomain = getDomain(FinalString);
						if (localDomain.equals(mainDomain)) {
							if (!allSetLink.contains(FinalString)) {
								localSet.add(FinalString);
								allSetLink.add(FinalString);
							}
						}else{
							alienLink.add(FinalString);
							allSetLink.add(FinalString);
						}
					}
				}
			} catch (Exception e) {
			}

			allSetLink.add(urlsend.toString());

			if (localSet.size() != 0) {
				return localSet;
			} else {
				return null;
			}
		} else {
			return null;
		}

	}	
	
	

	public String getMainDomain() {
		return mainDomain;
	}

	public void setMainDomain(String mainDomain) {
		this.mainDomain = mainDomain;
	}

	public HashSet<LinksList> getSetLinks() {
		return setLinks;
	}

	public void setSetLinks(HashSet<LinksList> setLinks) {
		this.setLinks = setLinks;
	}

	public Set<String> getAllSetLink() {
		return allSetLink;
	}

	public void setAllSetLink(Set<String> allSetLink) {
		this.allSetLink = allSetLink;
	}

	public HashSet<String> getAlienLink() {
		return alienLink;
	}

	public void setAlienLink(HashSet<String> alienLink) {
		this.alienLink = alienLink;
	}
	
}
