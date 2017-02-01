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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devcortes.components.entity.ParsePage;
import com.devcortes.components.entity.StorageResult;
import com.devcortes.components.service.DomainService;

/**
 * Service that perform parsing website
 * 
 * @author cortes
 *
 */

@Service
public class CrawlerService {
	
	private static Logger log = Logger.getLogger(CrawlerService.class.getName());
	private static final String HTML_LINK_TAG = "a";
	private static final String SELECT_URL_FROM_HTML_LINK_TAG = "abs:href";
	
	@Autowired
	private DomainService domainService;
	

	/**
	 * It's method that run crawler for url with some deph.
	 * 
	 * @param storageOfLinks
	 *            storageOfLinks-model where store results of parsing
	 * @param domainService
	 *            domainService-domain links which will parse
	 * @return
	 */
	public boolean runCrawler(ParsePage parsePage, StorageResult storageResult) {

		fillURLs(storageResult.getUrl(), storageResult, parsePage);
		
		
		if (parsePage.getLocalLinks().isEmpty()) {
			return false;
		}

		ParsePage alreadyParsedLink = new ParsePage(storageResult.getUrl(), 0, parsePage.getLocalLinks(), "");

		storageResult.getParsePages().add(alreadyParsedLink);
		
		try {
			recursiveCrawl(alreadyParsedLink, storageResult, domainService);
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
	public void recursiveCrawl(ParsePage parsePage, StorageResult storageResult,
			DomainService domainService) throws IOException {

		for (String parseLink : parsePage.getLocalLinks()) {
			
			fillURLs(parseLink, storageResult, parsePage);
			
			ParsePage localAlreadyParsedLink = new ParsePage(parseLink, parsePage.getDepth() + 1, parsePage.getLocalLinks(), parsePage.getUrl());
			
			storageResult.getParsePages().add(localAlreadyParsedLink);

			if (!parsePage.getLocalLinks().isEmpty() && localAlreadyParsedLink.getDepth() < storageResult.getAccessDepth()) {
				recursiveCrawl(localAlreadyParsedLink, storageResult, domainService);
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
	 */
	public void fillURLs(String urlsend, StorageResult storageResult, ParsePage parsePage) {

		parsePage.setLocalLinks(new HashSet<>());
		if (StringUtils.isBlank(urlsend) || urlsend.isEmpty()) {
			return;
		}

		Document doc = null;
		try {
			doc = Jsoup.connect(urlsend).get();
		} catch (IOException e) {
			log.error("ReturnURL ---  " + e.getMessage());			
		}

		if (doc != null) {
			Elements links = doc.select(HTML_LINK_TAG);
			String finalString;

			for (Element e : links) {
				finalString = e.attr(SELECT_URL_FROM_HTML_LINK_TAG);
				String localDomain = domainService.getDomain(finalString);

				if (localDomain.equals(storageResult.getDomain())) {
					parsePage.getLocalLinks().add(finalString);
				} else {
					storageResult.getExternalLinks().add(finalString);
				}
				storageResult.getUniqeuLinks().add(finalString);
			}
		}
		storageResult.getUniqeuLinks().add(urlsend);
	}

}
