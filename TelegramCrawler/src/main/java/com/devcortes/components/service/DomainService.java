package com.devcortes.components.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Service that return domain of website
 * 
 * @author cortes
 *
 */
@Service
public class DomainService {
	private static final Logger log = Logger.getLogger(DomainService.class);

	/**
	 * It's method that search main domain from general url;
	 * 
	 * @param url
	 * @return return domen website that parse
	 * @throws MalformedURLException
	 */
	public String getDomain(String url) {

		String domen = "";
		try {

			URL nURL = new URL(url);
			domen = nURL.getHost();
			domen = domen.startsWith("www.") ? domen.substring(4) : domen;

		} catch (MalformedURLException e) {
			log.error("Error in getDomain ---  " + e.getMessage());
		}
		return domen;
	}
}
