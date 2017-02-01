package com.devcortes.components.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ConvertDataToPdfService {
	private String path = "/var/www/crawler.com/public_html/results/";
	private static Logger log = Logger.getLogger(ConvertDataToTxtService.class.getName());

	public void writeToPdfLinks(String nameFile) {
		try {
			FileReader fileReader = new FileReader(path + nameFile + ".txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(path + nameFile + ".pdf"));
			document.open();
			String text = "";
			while ((text = bufferedReader.readLine()) != null) {
				document.add(new Paragraph(text));
			}
			document.close();
			bufferedReader.close();
			fileReader.close();
		} catch (DocumentException e) {
			log.info("ConvertDataToPdfService --- DocumentException ---  " + e.getMessage());
		} catch (FileNotFoundException e) {
			log.info("ConvertDataToPdfService --- FileNotFoundException ---  " + e.getMessage());
		} catch (IOException e) {
			log.info("ConvertDataToPdfService --- IOException ---  " + e.getMessage());
		}
	}
}
