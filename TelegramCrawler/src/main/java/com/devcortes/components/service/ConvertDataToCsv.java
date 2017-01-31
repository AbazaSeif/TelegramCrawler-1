package com.devcortes.components.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.csvreader.CsvWriter;

@Service
public class ConvertDataToCsv {

	private static final Logger log = Logger.getLogger(ConvertDataToCsv.class.getName());
	private static final String PATH = "/var/www/crawler.com/public_html/results/";
	private static final String CSV = ".csv";
	private static final char SEPARATOR = ',';
	private boolean permissionAppendFile = true;

	public void writeToCsvLocalLink(int depth, String url, String nameFile) {
		
		CsvWriter csvOutput = null;
		try {
			
			csvOutput = new CsvWriter(new FileWriter(PATH + nameFile + CSV, permissionAppendFile), SEPARATOR);
			
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

	public void writeToCsvExternalLink(Set<String> alienLink, String nameFile) {
		
		CsvWriter csvOutput = null;
		try {
			
			csvOutput = new CsvWriter(new FileWriter(PATH + nameFile + CSV, permissionAppendFile), SEPARATOR);
			csvOutput.write("All external links:");
			csvOutput.endRecord();
			
			for (String string : alienLink) {
				csvOutput.write(string);
				csvOutput.endRecord();
			}
			
			csvOutput.close();
			
		} catch (Exception e) {
			log.error("Error in writeExternalLinkToCSV ---  " + e.getMessage());
		}
		finally {
			csvOutput.close();
		}
	}
}
