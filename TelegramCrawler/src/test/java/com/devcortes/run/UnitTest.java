package com.devcortes.run;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.annotation.Rollback;
import org.telegram.telegrambots.api.objects.Update;

import com.devcortes.components.entity.StorageFiles;
import com.devcortes.components.entity.StorageResult;
import com.devcortes.components.service.ConvertData;
import com.devcortes.components.service.DomainService;
import com.devcortes.components.service.StorageFilesDAO;
import com.devcortes.service.CallCrawlerService;
import com.devcortes.service.CrawlerService;
import com.devcortes.service.StorageFilesService;
import com.devcortes.service.TelegramService;

@Rollback(false)
public class UnitTest {

	private static final String PATH_TO_RESULT_FILE = "/var/www/crawler.com/public_html/results/";
	private static final String TEST_URL = "https://www.sites.google.com/site/semenukviktor/home";
	
	private StorageResult storageResult;
	private StorageFilesService storageFilesService;
	private CallCrawlerService callCrawlerService;
	private DomainService domainService;
	private TelegramService telegramService;
	private CrawlerService crawlerService;
	private ConvertData convertData;	

	@Before
	public void setUp() {
		
		domainService = Mockito.mock(DomainService.class);
		callCrawlerService = Mockito.mock(CallCrawlerService.class);
		storageFilesService = Mockito.mock(StorageFilesService.class);
		telegramService = Mockito.mock(TelegramService.class);
		storageResult = new StorageResult();
		crawlerService = Mockito.mock(CrawlerService.class);
		convertData = Mockito.mock(ConvertData.class);
	}

	@Test
	public void onUpdateReceivedTest() {

		Update update = new Update();

		doNothing().when(telegramService).onUpdateReceived(update);
		telegramService.onUpdateReceived(update);
		verify(telegramService, times(1)).onUpdateReceived(update);
	}
	
	@Test
	public void getDomainTest(){
		
		when(domainService.getDomain(TEST_URL)).thenReturn("sites.google.com");
		domainService.getDomain(TEST_URL);
		verify(domainService, times(1)).getDomain(TEST_URL);
		assertThat(domainService.getDomain(TEST_URL)).as("domen").isEqualTo("sites.google.com");
	}
	
	@Test
	public void urlIsExistInDBTest(){
		
		when(storageFilesService.urlIsExistInDB(TEST_URL)).thenReturn(true);
		storageFilesService.urlIsExistInDB(TEST_URL);
		verify(storageFilesService, times(1)).urlIsExistInDB(TEST_URL);
		assertThat(storageFilesService.urlIsExistInDB(TEST_URL)).isEqualTo(true);
	}
	
	@Test
	public void callCrawlerTest(){
		
		when(callCrawlerService.callCrawler(TEST_URL)).thenReturn(true);
		callCrawlerService.callCrawler(TEST_URL);
		verify(callCrawlerService, times(1)).callCrawler(TEST_URL);
		assertThat(callCrawlerService.callCrawler(TEST_URL)).isEqualTo(true);
	}
	
	@Test
	public void runCrawlerTest(){
		
		when(crawlerService.runCrawler(storageResult)).thenReturn(true);
		crawlerService.runCrawler(storageResult);
		verify(crawlerService, times(1)).runCrawler(storageResult);
		assertThat(crawlerService.runCrawler(storageResult)).isEqualTo(true);
	} 
	
	@Test
	public void runConvertResultTest() throws Exception{
		
		doNothing().when(convertData).runConvertResult(storageResult);
		convertData.runConvertResult(storageResult);
		verify(convertData, times(1)).runConvertResult(storageResult);
	}
	
	@Test
	public void DaoTest(){
		
		StorageFiles storageFiles = new StorageFiles();
		storageFiles.setUrl(TEST_URL);		
		
		StorageFilesDAO storageFilesDAO = Mockito.mock(StorageFilesDAO.class);
		StorageFilesService storageFilesServ = new StorageFilesService();
		storageFilesServ.setiStorageFilesDAO(storageFilesDAO);		
		
		doNothing().when(storageFilesDAO).add(PATH_TO_RESULT_FILE, TEST_URL);
		when(storageFilesDAO.getByUrl(PATH_TO_RESULT_FILE, TEST_URL)).thenReturn(storageFiles);
		
		storageFilesDAO.add(PATH_TO_RESULT_FILE, TEST_URL);
		verify(storageFilesDAO, times(1)).add(PATH_TO_RESULT_FILE, TEST_URL);
		assertThat(storageFilesDAO.getByUrl(PATH_TO_RESULT_FILE, TEST_URL).getUrl().replace('/', ' ')).as("url").isEqualTo(storageFiles.getUrl().replace('/', ' '));
	}
	
}
