package com.devcortes.components.service.exception;

import org.apache.log4j.Logger;

import com.devcortes.service.CrawlerService;

public class NotParsePageException extends RuntimeException{

	private static Logger log = Logger.getLogger(CrawlerService.class.getName());
	
	@Override
	public void printStackTrace() {
		log.error("Cannot parse this page");
	}

}
