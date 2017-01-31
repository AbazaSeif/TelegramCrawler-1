package com.devcortes.components.service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.FileWriter;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csvreader.CsvWriter;
import com.devcortes.components.entity.AlreadyParsedLink;
import com.devcortes.components.entity.StorageOfLinks;

/**
 * Service that convert result data to pdf, txt, csv
 * 
 * @author cortes
 *
 */
@Service
public class ConvertData {

	private static final Logger log = Logger.getLogger(ConvertData.class.getName());
	private static final String PATH_TO_RESULT_FILE = "/var/www/crawler.com/public_html/results/";
	private static final String TXT_FILE_FORMAT = ".txt";
	private static final String CSV_FILE_FORMAT = ".csv";
	private static final char SEPARATOR_FOR_CSV_FILE = ',';
	private static final boolean BANAPPENDFILE = false;
	private String fileName;

	

	@Autowired
	private ConvertDataToTxt convertTxtService;

	@Autowired
	private ConvertDataToPdf convertDataToPdfService;

	@Autowired
	private ConvertDataToCsv convertDataToCsvService;

	/**
	 * Run conversion data that get from crawler
	 * 
	 * @throws Exception
	 */
	public void runConvertResult(StorageOfLinks storageSetsLinks) throws Exception {

		fileName = storageSetsLinks.getRootUrl().replace('/', ' ');
		FileWriter writer = null;
		CsvWriter csvOutput = null;
		
		try {
			
			writer = new FileWriter(PATH_TO_RESULT_FILE + fileName + TXT_FILE_FORMAT, BANAPPENDFILE);
			csvOutput = new CsvWriter(new FileWriter(PATH_TO_RESULT_FILE + fileName + CSV_FILE_FORMAT, BANAPPENDFILE), SEPARATOR_FOR_CSV_FILE);
			
		} finally {
			writer.close();			
			csvOutput.close();
		}		
		conversationData(storageSetsLinks);
	}

	/**
	 * Conversion data into pdf, txt, csv formats
	 * 
	 * @throws Exception
	 */
	public void conversationData(StorageOfLinks storageSetsLinks) throws Exception {
		
		AlreadyParsedLink alreadyParsedLink = new AlreadyParsedLink();
		
		for (AlreadyParsedLink onceLink : storageSetsLinks.getAlreadyParsedLinksWithRootDomain()) {
			
			int depthOfRootLink = 0;
			if (onceLink.getCurrentDepth() == depthOfRootLink) {
				alreadyParsedLink = onceLink;
				break;
			}
		}
		
		recursiveShowTXT(alreadyParsedLink, storageSetsLinks);
		convertTxtService.writeToTxtExternalLink(storageSetsLinks.getExternalLinks(), fileName);
		convertDataToPdfService.writeToPdfLinks(fileName);

		convertToCSV(alreadyParsedLink, storageSetsLinks);
		convertDataToCsvService.writeToCsvExternalLink(storageSetsLinks.getExternalLinks(), fileName);
	}

	/**
	 * Convert data to txt format
	 * 
	 * @param alreadyParsedLink
	 */
	public void recursiveShowTXT(AlreadyParsedLink alreadyParsedLink, StorageOfLinks storageSetsLinks) {
		
		convertTxtService.writeToTxtLocalLink(alreadyParsedLink.getCurrentDepth(), alreadyParsedLink.getCurrentUrl(),
				fileName);
		
		if (!alreadyParsedLink.getSetLinksOnCurrentUrl().isEmpty()) {
			
			for (String url : alreadyParsedLink.getSetLinksOnCurrentUrl()) {
				
				AlreadyParsedLink localAlreadyParsedLink = null;
				for (AlreadyParsedLink onceLink : storageSetsLinks.getAlreadyParsedLinksWithRootDomain()) {
					
					if (onceLink.getCurrentUrl() == url) {
						localAlreadyParsedLink = onceLink;
						break;
					}
				}
				
				if (localAlreadyParsedLink != null) {
					recursiveShowTXT(localAlreadyParsedLink, storageSetsLinks);
				}
			}
		}
	}

	/**
	 * Convert data to csv format
	 * 
	 * @param alreadyParsedLink
	 */
	public void convertToCSV(AlreadyParsedLink alreadyParsedLink, StorageOfLinks storageSetsLinks) throws Exception {
		
		convertDataToCsvService.writeToCsvLocalLink(alreadyParsedLink.getCurrentDepth(),
				alreadyParsedLink.getCurrentUrl(), fileName);
		
		if (!alreadyParsedLink.getSetLinksOnCurrentUrl().isEmpty()) {
			
			for (String url : alreadyParsedLink.getSetLinksOnCurrentUrl()) {
				
				AlreadyParsedLink localAlreadyParsedLink = null;
				
				for (AlreadyParsedLink onceLink : storageSetsLinks.getAlreadyParsedLinksWithRootDomain()) {
					
					if (onceLink.getCurrentUrl() == url) {
						localAlreadyParsedLink = onceLink;
						break;
					}
				}
				
				if (localAlreadyParsedLink != null) {
					convertToCSV(localAlreadyParsedLink, storageSetsLinks);
				}
			}
		}

	}

	
}
