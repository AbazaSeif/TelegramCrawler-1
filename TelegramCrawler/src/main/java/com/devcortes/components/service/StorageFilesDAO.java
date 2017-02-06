package com.devcortes.components.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.devcortes.components.entity.StorageFiles;
import com.devcortes.components.interfaces.IConvertFilesToBlobAndBack;
import com.devcortes.components.interfaces.IStorageFilesDAO;

/**
 * Dao that work with storage files in database
 * 
 * @author cortes
 *
 */
@Repository
public class StorageFilesDAO extends HibernateUtil implements IStorageFilesDAO {

	private static final Logger log = Logger.getLogger(StorageFilesDAO.class.getName());
	private static final String TXT_FILE_FORMAT = ".txt";
	private static final String CSV_FILE_FORMAT = ".csv";
	private static final String PDF_FILE_FORMAT = ".pdf";
	private static final String URL = "url";

	@Autowired
	IConvertFilesToBlobAndBack iConvertFilesToBLOB;

	@Override
	public void update(String oldUrl, String newUrl, String path) {

		StorageFiles infoByUrl = null;

		try (Session session = getSessionFactory().openSession()) {

			session.beginTransaction();

			String filename = oldUrl.replace('/', ' ');

			infoByUrl = (StorageFiles) session.createCriteria(StorageFiles.class).add(Restrictions.eq(URL, filename))
					.uniqueResult();

			filename = newUrl.replace('/', ' ');
			infoByUrl.setUrl(filename);
			infoByUrl.setFileTxt(iConvertFilesToBLOB.convertFile(path, filename, TXT_FILE_FORMAT));
			infoByUrl.setFilePdf(iConvertFilesToBLOB.convertFile(path, filename, PDF_FILE_FORMAT));
			infoByUrl.setFileCsv(iConvertFilesToBLOB.convertFile(path, filename, CSV_FILE_FORMAT));
			session.save(infoByUrl);
			session.getTransaction().commit();

		}

		log.info("StorageFiles update successful");
	}

	@Transactional
	@Override
	public StorageFiles getByUrl(String path, String url) {

		StorageFiles infoByUrl = null;

		try (Session session = getSessionFactory().openSession()) {

			String filename = url.replace('/', ' ');
			infoByUrl = (StorageFiles) session.createCriteria(StorageFiles.class).add(Restrictions.eq(URL, filename))
					.uniqueResult();

			iConvertFilesToBLOB.convertBlobToFile(infoByUrl.getFileTxt(), path, filename, TXT_FILE_FORMAT);
			iConvertFilesToBLOB.convertBlobToFile(infoByUrl.getFilePdf(), path, filename, PDF_FILE_FORMAT);
			iConvertFilesToBLOB.convertBlobToFile(infoByUrl.getFileCsv(), path, filename, CSV_FILE_FORMAT);

		}

		log.info("getByUrl performed successful");

		return infoByUrl;
	}

	@Transactional
	@Override
	public void add(String path, String url) {

		try (Session session = getSessionFactory().openSession()) {

			StorageFiles storageFiles = new StorageFiles();

			String filename = url.replace('/', ' ');
			storageFiles.setUrl(filename);
			storageFiles.setFileTxt(iConvertFilesToBLOB.convertFile(path, filename, TXT_FILE_FORMAT));
			storageFiles.setFilePdf(iConvertFilesToBLOB.convertFile(path, filename, PDF_FILE_FORMAT));
			storageFiles.setFileCsv(iConvertFilesToBLOB.convertFile(path, filename, CSV_FILE_FORMAT));

			session.save(storageFiles);
			log.info("StorageFiles add successful");
		}

	}

	@Override
	public boolean urlIsExistInDB(String url) {
		
		StorageFiles infoByUrl = null;
		
		try (Session session = getSessionFactory().openSession()) {

			String filename = url.replace('/', ' ');
			infoByUrl = (StorageFiles) session.createCriteria(StorageFiles.class).add(Restrictions.eq(URL, filename))
					.uniqueResult();
		}
		
		log.info("urlIsExistInDB check successful");
		
		return infoByUrl != null;
	}

}
