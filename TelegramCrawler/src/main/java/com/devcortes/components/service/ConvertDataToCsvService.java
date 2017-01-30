package com.devcortes.components.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import com.csvreader.CsvWriter;

public class ConvertDataToCsvService {
	private String path = "/var/www/crawler.com/public_html/results/";
	private static Logger log = Logger.getLogger(ConvertDataToTxtService.class.getName());
	public void writeToCsvLocalLink(int depth, String url, String nameFile){
		try {
			CsvWriter csvOutput = new CsvWriter(new FileWriter(path + nameFile + ".csv", true), ',');
			for (int i = 1; i <= depth; i++) {
				csvOutput.write("");
			}					
			csvOutput.write(url);	
			csvOutput.endRecord();	
			csvOutput.close();
		} catch (IOException e) {
			log.info("writeToCsvLocalLink ---  " + e.getMessage());
		}
	}
	
	public void writeToTxtExternalLink(Set<String> alienLink, String nameFile){		
		try{		
			CsvWriter csvOutput = new CsvWriter(new FileWriter(path + nameFile + ".csv", true), ',');
			csvOutput.write("All external links:");	
			csvOutput.endRecord();	
			for (String string : alienLink) {					
				csvOutput.write(string);	
				csvOutput.endRecord();
			}			
			csvOutput.close();
	    } catch (Exception e) {			
	    	log.info("WriteExternalLinkToCSV ---  " + e.getMessage());
		}
	}
}
