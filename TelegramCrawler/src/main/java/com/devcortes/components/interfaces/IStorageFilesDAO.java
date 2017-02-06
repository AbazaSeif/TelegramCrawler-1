package com.devcortes.components.interfaces;

import com.devcortes.components.entity.StorageFiles;

public interface IStorageFilesDAO {
	public void update(String oldUrl, String newUrl, String path);
	public StorageFiles getByUrl(String path, String url);
	public void add(String path, String url);
	public boolean urlIsExistInDB(String url);
}
