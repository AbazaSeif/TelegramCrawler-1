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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devcortes.components.entity.Link;
import com.devcortes.components.entity.StorageSetsLinks;
import com.devcortes.components.service.StorageSetsLinksService;

@Service
public class CrawlerService {
	@Autowired
	private StorageSetsLinksService storageSetsLinksService;
	private static Logger log = Logger.getLogger(CrawlerService.class.getName());
	private StorageSetsLinks storageSetsLinks;

	/**
	 * It's method that run crawler for url with some deph.
	 * 
	 * @param url
	 * @param deph
	 * @throws Exception
	 */
	public boolean runCrawler(StorageSetsLinks storageLinks) {
		storageSetsLinks = storageLinks;
		
		Set<String> fListURL = null;
		if (returnURL(storageSetsLinks.getUrl())) {
			fListURL = storageSetsLinks.getLocalSet();
		}
		if (fListURL.isEmpty()) {
			return false;
		}
		Link ll = new Link(storageSetsLinks.getUrl(), 0, fListURL, "");
		storageSetsLinks.getSetLinks().add(ll);
		try {
			recursiveCrawl(ll, 0);
		} catch (IOException e) {
			log.info("RunCrawler ---  " + e.getMessage());
		}
		return true;

	}

	/**
	 * It's recursive method for parse links.
	 * 
	 * @param listLink
	 * @param deph
	 * @throws IOException
	 */
	public void recursiveCrawl(Link listLink, int deph) throws IOException {
		deph++;
		for (String link : listLink.getListUrl()) {
			Set<String> recurSive = null;
			if (returnURL(link)) {
				recurSive = storageSetsLinks.getLocalSet();
			}
			Link ll = new Link(link, deph, recurSive, listLink.getUrl());
			storageSetsLinks.getSetLinks().add(ll);
			if (recurSive != null && deph < storageSetsLinks.getGlobalDeph()) {
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
		storageSetsLinks.setLocalSet(new HashSet<>());
		if (urlsend == null || urlsend.isEmpty()) {
			return false;
		}
		Document doc = null;
		try {
			doc = Jsoup.connect(urlsend).get();
		} catch (IOException e1) {
			log.info("ReturnURL ---  " + e1.getMessage());
		}
		if (doc != null) {
			Elements links = doc.select("a");
			String finalString;
			for (Element e : links) {
				finalString = e.attr("abs:href");
				String localDomain = storageSetsLinksService.getDomain(finalString);
				if (localDomain.equals(storageSetsLinks.getMainDomain())) {
					storageSetsLinks.getLocalSet().add(finalString);
				} else {
					storageSetsLinks.getAlienLink().add(finalString);
				}
				storageSetsLinks.getAllSetLink().add(finalString);
			}
		}
		storageSetsLinks.getAllSetLink().add(urlsend);
		return !storageSetsLinks.getLocalSet().isEmpty();

	}

}
