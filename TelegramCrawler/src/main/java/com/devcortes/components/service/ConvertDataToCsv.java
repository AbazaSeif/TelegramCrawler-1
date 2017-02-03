package com.devcortes.components.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.csvreader.CsvWriter;
/**
 * Service that convert result data to csv
 * 
 * @author cortes
 *
 */
@Service
public class ConvertDataToCsv {

	private static final Logger log = Logger.getLogger(ConvertDataToCsv.class.getName());
	private static final String PATH = "/var/www/crawler.com/public_html/results/";
	private static final String CSV = ".csv";
	private static final char SEPARATOR_FOR_CSV_FILE = ',';
	private boolean permissionAppendFile = true;

	/**
	 * Write to csv file all links which have root domain
	 * 
	 * @param depth
	 *            depth-depth of parsing current link
	 * @param url
	 *            url-current url
	 * @param nameFile
	 *            nameFile-name file in which will write result parsing
	 */
	public void writeToCsvLocalLink(int depth, String url, String nameFile) {

		CsvWriter csvOutput = null;
		try {

			csvOutput = new CsvWriter(new FileWriter(PATH + nameFile + CSV, permissionAppendFile), SEPARATOR_FOR_CSV_FILE);

			for (int i = 0; i < depth; i++) {
				csvOutput.write("");
			}

			csvOutput.write(url);
			csvOutput.endRecord();

		} catch (IOException e) {
			log.error("Error in writeToCsvLocalLink ---  " + e.getMessage());
		} finally {
			csvOutput.close();
		}
	}

	/**
	 * Append to result csv file all external links which will meet in websites
	 * 
	 * @param externalLinks
	 *            externalLinks-all external links which will meet in websites
	 *            which will parse
	 * @param nameFile
	 *            nameFile-name file in which will write result parsing
	 */
	public void writeToCsvExternalLink(Set<String> externalLinks, String nameFile, String title) {

		CsvWriter csvOutput = null;
		try {

			csvOutput = new CsvWriter(new FileWriter(PATH + nameFile + CSV, permissionAppendFile), SEPARATOR_FOR_CSV_FILE);
			csvOutput.write("All external links:");
			csvOutput.endRecord();

			for (String string : externalLinks) {
				csvOutput.write(string);
				csvOutput.endRecord();
			}

			csvOutput.close();

		} catch (Exception e) {
			log.error("Error in writeExternalLinkToCSV ---  " + e.getMessage());
		} finally {
			csvOutput.close();
		}
	}
}
