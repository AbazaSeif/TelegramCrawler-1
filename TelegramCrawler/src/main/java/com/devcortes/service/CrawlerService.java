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
	
	private String mainDomain;
	private Integer globalDeph;
	private HashSet<LinksList> setLinks = new HashSet<LinksList>();
	private Set<String> allSetLink = new HashSet<String>();
	private HashSet<String> alienLink = new HashSet<String>();

	/**
	 * It's method that run crawler for url with some deph.
	 * @param url
	 * @param deph
	 * @throws IOException
	 */
	public void runCrawler(String url, Integer deph) throws IOException {
		globalDeph = deph;
		mainDomain = getDomain(url);
		HashSet<String> fListURL = returnURL(url);
		if (fListURL != null) {
			LinksList ll = new LinksList(url, 0, fListURL, "");
			setLinks.add(ll);
			recursiveCrawl(ll, 0);
			//showLinks();
		} else {
			System.out.println("URL is not correct");
		}

	}

	/**
	 * It's method that begin show list link;
	 */
	public void showLinks() {
		LinksList fll = new LinksList();
		for (LinksList onceLink : setLinks) {
			if (onceLink.getDeph() == 0) {
				fll = onceLink;
				break;
			}
		}
		recursiveShow(fll);
	}

	/**
	 * It's recursive method for show link;
	 * @param ll
	 */
	public void recursiveShow(LinksList ll) {
		String spacing1 = "";
		for (int i = 1; i <= ll.getDeph(); i++) {
			spacing1 += "\t";
		}
		//fileopen
		System.out.println(spacing1 + ll.getUrl());
		//fileclose
		if (ll.getListUrl() != null) {
			for (String url : ll.getListUrl()) {
				LinksList ll1 = null;
				for (LinksList onceLink : setLinks) {
					if (onceLink.getUrl() == url) {
						ll1 = onceLink;
						break;
					}
				}
				if (ll1 != null) {
					recursiveShow(ll1);
				}
			}
		}
	}

	/**
	 * It's method that search main domain from general url;
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	public String getDomain(String url) throws MalformedURLException {
		URL nURL = new URL(url);
		String domen = nURL.getHost();
		return domen.startsWith("www.") ? domen.substring(4) : domen;
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
	public synchronized HashSet<String> returnURL(String urlsend) throws IOException {
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
}
