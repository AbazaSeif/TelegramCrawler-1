package com.devcortes.components.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
/**
 * Service that convert result data to txt
 * 
 * @author cortes
 *
 */
@Service
public class ConvertDataToTxt {

	private static final Logger log = Logger.getLogger(ConvertDataToTxt.class.getName());
	private static final String PATH_TO_RESULT_FILE = "/var/www/crawler.com/public_html/results/";
	private static final String TXT_FILE_FORMAT = ".txt";
	private static final boolean PERMISSION_TO_APPEND_FILE = true;

	/**
	 * Write to txt file all links which have root domain
	 * 
	 * @param depth
	 *            depth-depth of parsing current link
	 * @param url
	 *            url-current url
	 * @param nameFile
	 *            nameFile-name file in which will write result parsing
	 */
	public void writeToTxtLocalLink(int depth, String url, String nameFile) {

		try (FileWriter writer = new FileWriter(PATH_TO_RESULT_FILE + nameFile + TXT_FILE_FORMAT,
				PERMISSION_TO_APPEND_FILE)) {

			StringBuilder spacingForStructure = new StringBuilder();

			for (int i = 0; i < depth; i++) {
				spacingForStructure.append("\t");
			}

			writer.write(spacingForStructure.toString() + depth + ".  " + url + "\n");
			writer.close();

		} catch (IOException e) {
			log.error("Error in writeToTxtLocalLink: " + e.getMessage());
		}
	}

	/**
	 * Append to result txt file all external links which will meet in websites
	 * 
	 * @param externalLinks
	 *            externalLinks-all external links which will meet in websites
	 *            which will parse
	 * @param nameFile
	 *            nameFile-name file in which will write result parsing
	 */
	public void writeToTxtExternalLink(Set<String> externalLinks, String nameFile, String title) {

		try (FileWriter writer = new FileWriter(PATH_TO_RESULT_FILE + nameFile + TXT_FILE_FORMAT,
				PERMISSION_TO_APPEND_FILE)) {

			writer.write(title + "\n");

			for (String string : externalLinks) {
				writer.write(string + "\n");
			}

			writer.close();

		} catch (IOException e) {
			log.error("Error in writeToTxtExternalLink: " + e.getMessage());
		}
	}	
}
