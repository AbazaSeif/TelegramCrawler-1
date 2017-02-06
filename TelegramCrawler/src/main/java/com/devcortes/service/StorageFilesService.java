package com.devcortes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devcortes.components.entity.StorageFiles;
import com.devcortes.components.interfaces.IStorageFilesDAO;

@Service
public class StorageFilesService {
	
	@Autowired
	IStorageFilesDAO iStorageFilesDAO;
	
	public IStorageFilesDAO getiStorageFilesDAO() {
		return iStorageFilesDAO;
	}

	public void setiStorageFilesDAO(IStorageFilesDAO iStorageFilesDAO) {
		this.iStorageFilesDAO = iStorageFilesDAO;
	}

	public void add(String path, String url) {
		iStorageFilesDAO.add(path, url);
	}
	
	public StorageFiles getByUrl(String path, String url) {
		return iStorageFilesDAO.getByUrl(path, url);
	}
	
	public void update(String oldUrl, String newUrl, String path) {
		iStorageFilesDAO.update(oldUrl, newUrl, path);
	}
	
	public boolean urlIsExistInDB(String url) {
		return iStorageFilesDAO.urlIsExistInDB(url);
	}
}
