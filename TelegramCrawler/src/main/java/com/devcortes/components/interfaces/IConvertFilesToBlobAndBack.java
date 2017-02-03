package com.devcortes.components.interfaces;

public interface IConvertFilesToBlobAndBack {
	
	public byte[] convertFile(String path, String url, String formatOfFile);
	public void convertBlobToFile(byte[] byteFile, String path, String url, String formatOfFile);
}
