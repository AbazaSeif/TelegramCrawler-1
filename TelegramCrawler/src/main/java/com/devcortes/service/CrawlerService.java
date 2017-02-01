package com.devcortes.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
	private static final String HTML_LINK_TAG = "a";
	private static final String SELECT_URL_FROM_HTML_LINK_TAG = "abs:href";

	/**
	 * It's method that run crawler for url with some deph.
	 * 
	 * @param storageOfLinks
	 *            storageOfLinks-model where store results of parsing
	 * @param domainService
	 *            domainService-domain links which will parse
	 * @return
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
			recursiveCrawl(alreadyParsedLink, storageOfLinks, domainService);
		} catch (IOException e) {
			log.error("RunCrawler ---  " + e.getMessage());
		}
		return true;

	}

	/**
	 * It's recursive method for parse links.
	 * 
	 * @param alreadyParsedLink
	 *            alreadyParsedLink-set links which will parse
	 * @param storageOfLinks
	 *            storageOfLinks-model where store results of parsing
	 * @param domainService
	 *            domainService-domain links which will parse
	 * @throws IOException
	 */
	public void recursiveCrawl(AlreadyParsedLink alreadyParsedLink, StorageOfLinks storageOfLinks,
			DomainService domainService) throws IOException {

		int depth = alreadyParsedLink.getCurrentDepth() + 1;

		for (String link : alreadyParsedLink.getSetLinksOnCurrentUrl()) {
			Set<String> recurSive = new HashSet<>();

			if (returnURL(link, storageOfLinks, domainService)) {
				recurSive = storageOfLinks.getLocalSet();
			}

			AlreadyParsedLink localAlreadyParsedLink = new AlreadyParsedLink(link, depth, recurSive,
					alreadyParsedLink.getCurrentUrl());
			storageOfLinks.getAlreadyParsedLinksWithRootDomain().add(localAlreadyParsedLink);

			if (!recurSive.isEmpty() && depth < storageOfLinks.getGlobalDepth()) {
				recursiveCrawl(localAlreadyParsedLink, storageOfLinks, domainService);
			}
		}
	}

	/**
	 * It's method that return list links for given url;
	 * 
	 * @param urlsend
	 *            urlsend-link for parsing
	 * @param storageOfLinks
	 *            storageOfLinks-model where store results of parsing
	 * @param domainService
	 *            domainService-domain links which will parse
	 * @return
	 */
	public boolean returnURL(String urlsend, StorageOfLinks storageOfLinks, DomainService domainService) {

		storageOfLinks.setLocalSet(new HashSet<>());
		if (StringUtils.isBlank(urlsend) || urlsend.isEmpty()) {
			return false;
		}

		Document doc = null;
		try {
			doc = Jsoup.connect(urlsend).get();
		} catch (IOException e) {
			log.error("ReturnURL ---  " + e.getMessage());
			throw new RuntimeException(e);
		}

		if (doc != null) {
			Elements links = doc.select(HTML_LINK_TAG);
			String finalString;

			for (Element e : links) {
				finalString = e.attr(SELECT_URL_FROM_HTML_LINK_TAG);
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
