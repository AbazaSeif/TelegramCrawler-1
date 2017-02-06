package com.devcortes.components.service;

import java.io.FileWriter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.csvreader.CsvWriter;
import com.devcortes.components.entity.ParsePage;
import com.devcortes.components.entity.StorageResult;
import com.devcortes.service.StorageFilesService;

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
	private static final boolean BAN_TO_APPEND_FILE = false;
	private static final String TITLE_TO_FILE_EXTERNAL_LINKS = "All external links:";
	private static final String TITLE_TO_FILE_NOTPARSED_LINKS = "All not parsed links:";

	private String fileName;

	@Autowired
	private ConvertDataToTxt convertTxtService;

	@Autowired
	private ConvertDataToPdf convertDataToPdfService;

	@Autowired
	private ConvertDataToCsv convertDataToCsvService;

	@Autowired
	private StorageFilesService storageFilesService;

	/**
	 * Run conversion data that get from crawler
	 * 
	 * @param storageSetsLinks
	 *            storageOfLinks-model where store results of parsing
	 * @throws Exception
	 */
	public void runConvertResult(StorageResult storageResult) throws Exception {

		fileName = storageResult.getUrl().replace('/', ' ');
		FileWriter writer = null;
		CsvWriter csvOutput = null;

		try {

			writer = new FileWriter(PATH_TO_RESULT_FILE + fileName + TXT_FILE_FORMAT, BAN_TO_APPEND_FILE);
			csvOutput = new CsvWriter(
					new FileWriter(PATH_TO_RESULT_FILE + fileName + CSV_FILE_FORMAT, BAN_TO_APPEND_FILE),
					SEPARATOR_FOR_CSV_FILE);

		} finally {
			writer.close();
			csvOutput.close();
		}
		conversationData(storageResult);
	}

	/**
	 * Conversion data into pdf, txt, csv formats
	 * 
	 * @param storageSetsLinks
	 *            storageOfLinks-model where store results of parsing
	 * @throws Exception
	 */
	private void conversationData(StorageResult storageResult) throws Exception {

		ParsePage alreadyParsedLink = new ParsePage();

		for (ParsePage onceLink : storageResult.getParsePages()) {

			int depthOfRootLink = 0;
			if (onceLink.getDepth() == depthOfRootLink) {
				alreadyParsedLink = onceLink;
				break;
			}
		}

		recursiveShowTXT(alreadyParsedLink, storageResult);
		convertTxtService.writeToTxtExternalLink(storageResult.getExternalLinks(), fileName,
				TITLE_TO_FILE_EXTERNAL_LINKS);
		convertTxtService.writeToTxtExternalLink(storageResult.getNotParsedLinks(), fileName,
				TITLE_TO_FILE_NOTPARSED_LINKS);

		convertDataToPdfService.writeToPdfLinks(fileName);

		convertToCSV(alreadyParsedLink, storageResult);
		convertDataToCsvService.writeToCsvExternalLink(storageResult.getExternalLinks(), fileName,
				TITLE_TO_FILE_EXTERNAL_LINKS);
		convertDataToCsvService.writeToCsvExternalLink(storageResult.getNotParsedLinks(), fileName,
				TITLE_TO_FILE_NOTPARSED_LINKS);
		
		storageFilesService.add(PATH_TO_RESULT_FILE, storageResult.getUrl());
	}

	/**
	 * Convert data to txt format
	 * 
	 * @param alreadyParsedLink
	 *            alreadyParsedLink-model which store info about parsing on
	 *            current link
	 * @param storageSetsLinks
	 *            storageOfLinks-model where store results of parsing
	 */
	private void recursiveShowTXT(ParsePage alreadyParsedLink, StorageResult storageResult) {

		convertTxtService.writeToTxtLocalLink(alreadyParsedLink.getDepth(), alreadyParsedLink.getUrl(), fileName);

		if (!alreadyParsedLink.getLocalLinks().isEmpty()) {

			for (String url : alreadyParsedLink.getLocalLinks()) {

				ParsePage localParsedLink = null;
				for (ParsePage onceLink : storageResult.getParsePages()) {

					if (onceLink.getUrl() == url) {
						localParsedLink = onceLink;
						break;
					}
				}

				if (localParsedLink != null) {
					recursiveShowTXT(localParsedLink, storageResult);
				}
			}
		}
	}

	/**
	 * Convert data to csv format
	 * 
	 * @param alreadyParsedLink
	 *            alreadyParsedLink-model which store info about parsing on
	 *            current link
	 * @param storageSetsLinks
	 *            storageOfLinks-model where store results of parsing
	 * @throws Exception
	 */
	private void convertToCSV(ParsePage alreadyParsedLink, StorageResult storageResult) throws Exception {

		convertDataToCsvService.writeToCsvLocalLink(alreadyParsedLink.getDepth(), alreadyParsedLink.getUrl(), fileName);

		if (!alreadyParsedLink.getLocalLinks().isEmpty()) {

			for (String url : alreadyParsedLink.getLocalLinks()) {

				ParsePage localParsedLink = null;

				for (ParsePage onceLink : storageResult.getParsePages()) {

					if (onceLink.getUrl() == url) {
						localParsedLink = onceLink;
						break;
					}
				}

				if (localParsedLink != null) {
					convertToCSV(localParsedLink, storageResult);
				}
			}
		}

	}

}
