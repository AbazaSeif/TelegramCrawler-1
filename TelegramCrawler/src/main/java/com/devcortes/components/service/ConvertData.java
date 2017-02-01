package com.devcortes.components.service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.csvreader.CsvWriter;
import com.devcortes.components.entity.Link;
import com.devcortes.components.entity.StorageSetsLinks;
import com.devcortes.service.CrawlerService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ConvertData {
	private static Logger log = Logger.getLogger(CrawlerService.class.getName());
	private String path = "/var/www/crawler.com/public_html/results/";
	private StorageSetsLinks storageSetsLinks;	
	ConvertDataToTxtService convertTxtService = new ConvertDataToTxtService();
	ConvertDataToPdfService convertDataToPdfService = new ConvertDataToPdfService();
	ConvertDataToCsvService convertDataToCsvService = new ConvertDataToCsvService();
	
	/**
	 * Run conversion data that get from crawler
	 * @throws Exception
	 */
	public void convert(StorageSetsLinks storageSetsLinks) throws Exception{
		this.storageSetsLinks = storageSetsLinks;
		FileWriter writer = new FileWriter(path + storageSetsLinks.getUrl().replace('/', ' ') + ".txt", false);
		writer.close();		
		CsvWriter csvOutput = new CsvWriter(new FileWriter(path + storageSetsLinks.getUrl().replace('/', ' ') + ".csv", false), ',');
		csvOutput.close();
		showLinks();
	
		
	}
	

	/**
	 * Conversion data
	 * @throws Exception
	 */
	public void showLinks(){
		Link fll = new Link();
		for (Link onceLink : storageSetsLinks.getSetLinks()) {
			if (onceLink.getDeph() == 0) {
				fll = onceLink;
				break;
			}
		}
		recursiveShowTXT(fll);
		convertTxtService.writeToTxtExternalLink(storageSetsLinks.getAlienLink(), storageSetsLinks.getUrl().replace('/', ' '));
		convertDataToPdfService.writeToPdfLinks(storageSetsLinks.getUrl().replace('/', ' '));
		
		//convertToCSV(fll);
		//writeExternalLinkToCSV();
	}
	
	/**
	 * Convert data to txt format
	 * @param ll
	 */
	public void recursiveShowTXT(Link ll) {		
		StringBuilder spacing = new StringBuilder();
		for (int i = 1; i <= ll.getDeph(); i++) {			
			spacing.append("\t");
		}
		
		convertTxtService.writeToTxtLocalLink(spacing, ll.getDeph(), storageSetsLinks.getUrl(), storageSetsLinks.getUrl().replace('/', ' '));
		
		if (ll.getListUrl() != null) {
			for (String url : ll.getListUrl()) {
				Link ll1 = null;
				for (Link onceLink : storageSetsLinks.getSetLinks()) {
					if (onceLink.getUrl() == url) {
						ll1 = onceLink;
						break;
					}
				}
				if (ll1 != null) {
					recursiveShowTXT(ll1);
				}
			}
		}
	}
	
	
	
	
	
	/**
	 * Convert data to csv format
	 * @param ll
	 */
	/*public void convertToCSV(Link ll) throws Exception{		
		convertDataToCsvService.writeToCsvLocalLink(ll.getDeph(), storageSetsLinks.getUrl(), storageSetsLinks.getUrl().replace('/', ' '));	
		if (ll.getListUrl() != null) {
			for (String url : ll.getListUrl()) {
				Link ll1 = null;
				for (Link onceLink : storageSetsLinks.getSetLinks()) {
					if (onceLink.getUrl() == url) {
						ll1 = onceLink;
						break;
					}
				}
				if (ll1 != null) {
					convertToCSV(ll1);
				}
			}
		}
		
	}*/
	
	
	

	
	
}
