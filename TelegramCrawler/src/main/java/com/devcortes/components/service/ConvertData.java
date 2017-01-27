package com.devcortes.components.service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.csvreader.CsvWriter;
import com.devcortes.components.entity.LinksList;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ConvertData {
	
	private HashSet<LinksList> links;
	private HashSet<String> alienLink;	
	
	/**
	 * Run convertation data that get from crawler
	 * @throws Exception
	 */
	public void convert() throws Exception{
		FileWriter writer = new FileWriter("/home/cortes/TelegramCrawler/ServerForData/WebContent/results/Crawler.txt", false);
		writer.close();
		CsvWriter csvOutput = new CsvWriter(new FileWriter("/home/cortes/TelegramCrawler/ServerForData/WebContent/results/Crawler.csv", false), ',');
		csvOutput.close();
		showLinks();
	
		
	}

	/**
	 * Convertation data
	 * @throws Exception
	 */
	public void showLinks() throws Exception {
		LinksList fll = new LinksList();
		for (LinksList onceLink : links) {
			if (onceLink.getDeph() == 0) {
				fll = onceLink;
				break;
			}
		}
		recursiveShowTXT(fll);
		writeExternalLinkToTXT();
		convertToPdf();
		convertToCSV(fll);
		writeExternalLinkToCSV();
	}
	
	/**
	 * Convert data to txt format
	 * @param ll
	 */
	public void recursiveShowTXT(LinksList ll) {
		String spacing1 = "";
		for (int i = 1; i <= ll.getDeph(); i++) {
			spacing1 += "\t";
		}
		try(FileWriter writer = new FileWriter("/home/cortes/TelegramCrawler/ServerForData/WebContent/results/Crawler.txt", true))
	     {			 
				writer.write(spacing1 + ll.getDeph() + ".  " + ll.getUrl() + "\n");			
				writer.close();
	     } catch (Exception e) {			
			e.printStackTrace();
		}		
		if (ll.getListUrl() != null) {
			for (String url : ll.getListUrl()) {
				LinksList ll1 = null;
				for (LinksList onceLink : links) {
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
	 * Add to txt file external links form site
	 */
	public void writeExternalLinkToTXT() {
		try(FileWriter writer = new FileWriter("/home/cortes/TelegramCrawler/ServerForData/WebContent/results/Crawler.txt", true)){			 
			writer.write("All external links:" + "\n");	
			for (String string : alienLink) {
				writer.write(string + "\n");		
			}			
			writer.close();	
	    } catch (Exception e) {			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Convert data to pdf format
	 * @param ll
	 */
	public void convertToPdf() throws Exception{
		FileReader fileReader = new FileReader("/home/cortes/TelegramCrawler/ServerForData/WebContent/results/Crawler.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("/home/cortes/TelegramCrawler/ServerForData/WebContent/results/Crawler.pdf"));
        document.open();
        String text = "";
        while((text = bufferedReader.readLine()) != null){
        	document.add(new Paragraph(text));   
        }
        document.close();        
        bufferedReader.close();
        fileReader.close();
	}	
	
	/**
	 * Convert data to csv format
	 * @param ll
	 */
	public void convertToCSV(LinksList ll) throws Exception{		
		try
	     {			
			CsvWriter csvOutput = new CsvWriter(new FileWriter("/home/cortes/TelegramCrawler/ServerForData/WebContent/results/Crawler.csv", true), ',');			
			for (int i = 1; i <= ll.getDeph(); i++) {
				csvOutput.write("");
			}					
			csvOutput.write(ll.getUrl());	
			csvOutput.endRecord();	
			csvOutput.close();
	     } catch (Exception e) {			
			e.printStackTrace();
		}		
		if (ll.getListUrl() != null) {
			for (String url : ll.getListUrl()) {
				LinksList ll1 = null;
				for (LinksList onceLink : links) {
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
		
	}
	
	/**
	 * Add to csv file external links form site
	 */
	public void writeExternalLinkToCSV() {
		try{		
			CsvWriter csvOutput = new CsvWriter(new FileWriter("/home/cortes/TelegramCrawler/ServerForData/WebContent/results/Crawler.csv", true), ',');
			csvOutput.write("All external links:");	
			csvOutput.endRecord();	
			for (String string : alienLink) {					
				csvOutput.write(string);	
				csvOutput.endRecord();
			}			
			csvOutput.close();
	    } catch (Exception e) {			
			e.printStackTrace();
		}
		
	}
	
	public HashSet<LinksList> getLinks() {
		return links;
	}

	public void setLinks(HashSet<LinksList> links) {
		this.links = links;
	}

	public HashSet<String> getAlienLink() {
		return alienLink;
	}

	public void setAlienLink(HashSet<String> alienLink) {
		this.alienLink = alienLink;
	}
	
}
