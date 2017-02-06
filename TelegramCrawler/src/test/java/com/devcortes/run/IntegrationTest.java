package com.devcortes.run;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import com.devcortes.components.entity.StorageFiles;
import com.devcortes.components.service.ConvertFilesToBlobAndBack;
import com.devcortes.components.service.StorageFilesDAO;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class IntegrationTest {
	
	private static final String PATH_TO_RESULT_FILE = "/var/www/crawler.com/public_html/results/";
	private static final String TEST_URL = "https://www.sites.google.com/site/semenukviktor/home";
	private static final String UPDATE_URL = "https://www.youtube.com";
	private static final String TXT_FILE_FORMAT = ".txt";
	private static final String CSV_FILE_FORMAT = ".csv";
	private static final String PDF_FILE_FORMAT = ".pdf";
	
	@Autowired
	private ConvertFilesToBlobAndBack convertFilesToBlobAndBack; 
	
	@Autowired
	private StorageFilesDAO storageFilesDAO;
	
	private StorageFiles storageFiles;
	
	@Before
	public void setUp() {
		
		storageFiles = new StorageFiles();
		storageFiles.setUrl(TEST_URL);
		String filename = TEST_URL.replace('/', ' ');
		storageFiles.setFileTxt(convertFilesToBlobAndBack.convertFile(PATH_TO_RESULT_FILE, filename, TXT_FILE_FORMAT));
		storageFiles.setFileCsv(convertFilesToBlobAndBack.convertFile(PATH_TO_RESULT_FILE, filename, CSV_FILE_FORMAT));
		storageFiles.setFilePdf(convertFilesToBlobAndBack.convertFile(PATH_TO_RESULT_FILE, filename, PDF_FILE_FORMAT));
	}
	
	@Rollback(false)
	@Test
	public void testDao() {		
		
		storageFilesDAO.add(PATH_TO_RESULT_FILE, TEST_URL);		
		
		assertThat(storageFilesDAO.getByUrl(PATH_TO_RESULT_FILE, TEST_URL).getUrl()).as("url").isEqualTo(TEST_URL.replace('/', ' '));
		assertThat(storageFilesDAO.getByUrl(PATH_TO_RESULT_FILE, TEST_URL).getUrl()).as("url").isEqualTo(storageFiles.getUrl().replace('/', ' '));		
		
		storageFiles.setUrl(UPDATE_URL);
		
		storageFilesDAO.update(TEST_URL, UPDATE_URL, PATH_TO_RESULT_FILE);
		assertThat(storageFilesDAO.getByUrl(PATH_TO_RESULT_FILE, UPDATE_URL).getUrl()).as("url").isEqualTo(UPDATE_URL.replace('/', ' '));
		assertThat(storageFilesDAO.getByUrl(PATH_TO_RESULT_FILE, UPDATE_URL).getUrl()).as("url").isEqualTo(storageFiles.getUrl().replace('/', ' '));	
		
		assertThat(storageFilesDAO.urlIsExistInDB(UPDATE_URL)).as("url").isEqualTo(true);
		assertThat(storageFilesDAO.urlIsExistInDB(storageFiles.getUrl())).as("url").isEqualTo(true);
	}

}
