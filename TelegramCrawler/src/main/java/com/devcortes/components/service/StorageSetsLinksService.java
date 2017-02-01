package com.devcortes.components.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

@Service
public class StorageSetsLinksService {
	private static Logger log = Logger.getLogger(StorageSetsLinksService.class.getName());
	
	/**
	 * It's method that search main domain from general url;
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	public String getDomain(String url) {
		System.out.println(url);
		String domen = "";
		try {
			URL nURL = new URL(url);
			domen = nURL.getHost();
			domen = domen.startsWith("www.") ? domen.substring(4) : domen;
			System.out.println("in -------- " + domen);
		} catch (MalformedURLException e) {
			log.info("GetDomain ---  " + e.getMessage());
		}
		System.out.println(domen);
		return domen;
	}
}
