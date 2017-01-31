package com.devcortes.components.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ConvertDataToTxt {
	private static final Logger log = Logger.getLogger(ConvertDataToTxt.class.getName());
	private static final String PATH = "/var/www/crawler.com/public_html/results/";
	private static final String TXT = ".txt";
	private boolean permissionAppendFile = true;

	public void writeToTxtLocalLink(int depth, String url, String nameFile) {
		try (FileWriter writer = new FileWriter(PATH + nameFile + TXT, permissionAppendFile)) {
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

	public void writeToTxtExternalLink(Set<String> alienLink, String nameFile) {
		try (FileWriter writer = new FileWriter(PATH + nameFile + TXT, permissionAppendFile)) {
			writer.write("All external links:" + "\n");
			for (String string : alienLink) {
				writer.write(string + "\n");
			}
			writer.close();
		} catch (IOException e) {
			log.error("Error in writeToTxtExternalLink: " + e.getMessage());
		}
	}
}
