package com.devcortes.components.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import ch.qos.logback.classic.Level;

public class ConvertDataToTxtService {
	
	private String path = "/var/www/crawler.com/public_html/results/";

	private static Logger log = Logger.getLogger(ConvertDataToTxtService.class.getName());

	public void writeToTxtLocalLink(StringBuilder spacing, int depth, String url, String nameFile) {
		try (FileWriter writer = new FileWriter(path + nameFile, true)) {
			writer.write(spacing.toString() + depth + ".  " + url + "\n");
			writer.close();
		} catch (IOException e) {
			log.info("WriteToTxtLocalLink ---  " + e.getMessage());
		}
	}

	public void writeToTxtExternalLink(Set<String> alienLink, String nameFile) {
		try (FileWriter writer = new FileWriter(path + nameFile + ".txt", true)) {
			writer.write("All external links:" + "\n");
			for (String string : alienLink) {
				writer.write(string + "\n");
			}
			writer.close();
		} catch (IOException e) {
			log.log(Level.ERROR, "WriteExternalLinkToTXT ---  " + e.getMessage());
		}
	}

}
