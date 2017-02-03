package com.devcortes.service;

import java.io.IOException;
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
import com.devcortes.components.service.exception.NotParsePageException;

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
	 *
	 * @return true-when result parsing is successful 
	 *    	   false-when result parsing is not successful
	 */
	public boolean runCrawler(StorageResult storageResult) {

	    int accessDepth = 0;
	    
		ParsePage parsePage = new ParsePage(storageResult.getUrl(), accessDepth, "");

		try {
			fillURLs(storageResult, parsePage);
		} catch (NotParsePageException e) {
			storageResult.getNotParsedLinks().add(storageResult.getUrl());
			storageResult.getParsePages().add(parsePage);
			return false;
		}
		

		if (parsePage.getLocalLinks().isEmpty()) {
			return false;
		}

		storageResult.getParsePages().add(parsePage);

		try {
			recursiveCrawl(parsePage, storageResult);
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
	public void recursiveCrawl(ParsePage parsePage, StorageResult storageResult) throws IOException {

		for (String parseLink : parsePage.getLocalLinks()) {

			ParsePage localAlreadyParsedLink = new ParsePage(parseLink, parsePage.getDepth() + 1,
					parsePage.getUrl());			
			
			try {
				fillURLs(storageResult, localAlreadyParsedLink);
			} catch (NotParsePageException e) {
				storageResult.getNotParsedLinks().add(localAlreadyParsedLink.getUrl());							
			}

			storageResult.getParsePages().add(localAlreadyParsedLink);

			if (!parsePage.getLocalLinks().isEmpty()
					&& localAlreadyParsedLink.getDepth() < storageResult.getAccessDepth()) {
				recursiveCrawl(localAlreadyParsedLink, storageResult);
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
	public void fillURLs(StorageResult storageResult, ParsePage parsePage) {

		if (StringUtils.isBlank(parsePage.getUrl())) {
			return;
		}

		Document doc = null;
		try {
			doc = Jsoup.connect(parsePage.getUrl()).get();
		} catch (IOException e) {
			log.error("fillURLs ---  " + e.getMessage());
			throw new NotParsePageException();
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
		storageResult.getUniqeuLinks().add(parsePage.getUrl());
	}

}
