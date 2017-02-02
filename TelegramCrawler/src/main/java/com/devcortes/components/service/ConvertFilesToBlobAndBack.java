package com.devcortes.components.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.devcortes.components.interfaces.IConvertFilesToBlobAndBack;

@Service
public class ConvertFilesToBlobAndBack implements IConvertFilesToBlobAndBack {

	private static final Logger log = Logger.getLogger(ConvertFilesToBlobAndBack.class.getName());

	@Override
	public byte[] convertFile(String path, String filename, String formatOfFile) {
		File file = new File(path + filename + formatOfFile);
		byte[] byteFile = new byte[(int) file.length()];

		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			
			fileInputStream.read(byteFile);
			
		} catch (Exception e) {
			log.error("Error in convertFile ---  " + e.getMessage());
		}
		return byteFile;
	}

	@Override
	public void convertBlobToFile(byte[] byteFile, String path, String filename, String formatOfFile) {

		try(FileOutputStream fileOutputStream = new FileOutputStream(path + filename + formatOfFile)) {
			
			fileOutputStream.write(byteFile);
		
		} catch (Exception e) {
			log.error("Error in convertBlobToFile ---  " + e.getMessage());
		}
	}

}
