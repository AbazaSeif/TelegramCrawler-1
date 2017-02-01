package com.devcortes.components.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
/**
 * Service that convert result data to pdf
 * 
 * @author cortes
 *
 */
@Service
public class ConvertDataToPdf {

	private static final Logger log = Logger.getLogger(ConvertDataToPdf.class.getName());
	private static final String PATH_TO_RESULT_FILE = "/var/www/crawler.com/public_html/results/";
	private static final String TXT_FILE_FORMAT = ".txt";
	private static final String PDF_FILE_FORMAT = ".pdf";

	/**
	 * Write to pdf file result of parsing
	 * 
	 * @param nameFile
	 *            nameFile-name file in which will write result parsing
	 */
	public void writeToPdfLinks(String nameFile) {

		Document document = null;
		try (FileReader fileReader = new FileReader(PATH_TO_RESULT_FILE + nameFile + TXT_FILE_FORMAT);
				BufferedReader bufferedReader = new BufferedReader(fileReader);) {

			document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(PATH_TO_RESULT_FILE + nameFile + PDF_FILE_FORMAT));
			document.open();
			String text = "";

			while ((text = bufferedReader.readLine()) != null) {
				document.add(new Paragraph(text));
			}

		} catch (DocumentException e) {
			log.error("Error in convertDataToPdfService --- DocumentException ---  " + e.getMessage());
		} catch (FileNotFoundException e) {
			log.error("Error in convertDataToPdfService --- FileNotFoundException ---  " + e.getMessage());
		} catch (IOException e) {
			log.error("Error in convertDataToPdfService --- IOException ---  " + e.getMessage());
		} finally {
			document.close();
		}
	}
}
