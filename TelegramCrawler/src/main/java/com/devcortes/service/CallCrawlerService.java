package com.devcortes.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devcortes.components.entity.StorageResult;
import com.devcortes.components.service.ConvertData;
import com.devcortes.components.service.DomainService;

/**
 * Service that call CrawlerService and ConverdData
 * 
 * @author cortes
 *
 */
@Service
public class CallCrawlerService {

	private static final Logger log = Logger.getLogger(DomainService.class);

	@Autowired
	private CrawlerService crawlerService;

	@Autowired
	private ConvertData convertData;

	@Autowired
	private DomainService domainService;

	/**
	 * Call CrawlerService and ConverdData
	 * 
	 * @param url
	 * @return true when parsing website and conversation result is successful
	 *         false when parsing website and conversation result is not
	 *         successful
	 */
	public boolean callCrawler(String url) {

		String domen = domainService.getDomain(url);

		int accessDepth = 2;
		StorageResult storageLinks = new StorageResult(url, accessDepth, domen);

		if (!StringUtils.isEmpty(domen) && crawlerService.runCrawler(storageLinks)
				&& !storageLinks.getParsePages().isEmpty()) {

			try {
				convertData.runConvertResult(storageLinks);
			} catch (Exception e) {
				log.error("Error in runConvertResult ---  " + e.getMessage());
			}
			return true;
		} else
			return false;
	}
	
}