package com.devcortes.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.devcortes.components.entity.AlreadyParsedLink;
import com.devcortes.components.entity.StorageOfLinks;
import com.devcortes.components.service.DomainService;

@Service
public class CrawlerService {
	private static Logger log = Logger.getLogger(CrawlerService.class.getName());

	/**
	 * It's method that run crawler for url with some deph.
	 * 
	 * @param url
	 * @param deph
	 * @throws Exception
	 */
	public boolean runCrawler(StorageOfLinks storageOfLinks, DomainService domainService) {

		Set<String> firstParsedSet = null;
		if (returnURL(storageOfLinks.getRootUrl(), storageOfLinks, domainService)) {
			firstParsedSet = storageOfLinks.getLocalSet();
		}
		if (firstParsedSet.isEmpty()) {
			return false;
		}

		AlreadyParsedLink alreadyParsedLink = new AlreadyParsedLink(storageOfLinks.getRootUrl(), 0, firstParsedSet, "");

		storageOfLinks.getAlreadyParsedLinksWithRootDomain().add(alreadyParsedLink);
		try {
			recursiveCrawl(alreadyParsedLink, 0, storageOfLinks, domainService);
		} catch (IOException e) {
			log.error("RunCrawler ---  " + e.getMessage());
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
	public void recursiveCrawl(AlreadyParsedLink listLink, int deph, StorageOfLinks storageOfLinks,
			DomainService domainService) throws IOException {
		deph++;
		for (String link : listLink.getSetLinksOnCurrentUrl()) {
			Set<String> recurSive = new HashSet<>();
			if (returnURL(link, storageOfLinks, domainService)) {
				recurSive = storageOfLinks.getLocalSet();
			}
			AlreadyParsedLink ll = new AlreadyParsedLink(link, deph, recurSive, listLink.getCurrentUrl());
			storageOfLinks.getAlreadyParsedLinksWithRootDomain().add(ll);
			if (recurSive != null && deph < storageOfLinks.getGlobalDepth()) {
				recursiveCrawl(ll, deph, storageOfLinks, domainService);
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
	public boolean returnURL(String urlsend, StorageOfLinks storageOfLinks, DomainService domainService) {
		storageOfLinks.setLocalSet(new HashSet<>());
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
				String localDomain = domainService.getDomain(finalString);
				if (localDomain.equals(storageOfLinks.getRootDomain())) {
					storageOfLinks.getLocalSet().add(finalString);
				} else {
					storageOfLinks.getExternalLinks().add(finalString);
				}
				storageOfLinks.getAllLinks().add(finalString);
			}
		}
		storageOfLinks.getAllLinks().add(urlsend);
		return !storageOfLinks.getLocalSet().isEmpty();
	}

}
