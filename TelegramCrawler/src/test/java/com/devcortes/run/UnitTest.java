package com.devcortes.run;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.api.objects.Update;

import com.devcortes.controller.TelegramController;
import com.devcortes.service.TelegramService;

public class UnitTest {
	private TelegramService telegramService;
	
	
	@Before
	public void setUp(){
		telegramService = Mockito.mock(TelegramService.class);		
	}
	
	@Test
	public void onUpdateReceivedTest(){
		Update update = new Update();
		
		doNothing().when(telegramService).onUpdateReceived(update);
		telegramService.onUpdateReceived(update);
		verify(telegramService, times(1)).onUpdateReceived(update);
	}
}
