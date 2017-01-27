package com.devcortes.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;



import com.devcortes.components.service.ConvertData;

@Service
public class TelegramService extends TelegramLongPollingBot{
	
	
	
	
	private static final String BOTTOKEN = "261462589:AAHBE65vOUd6hEIqD--QJezegwXL63JZfUk";
	private static final String BOTUSERNAME = "java_practice_bot"; 
	
	static{
		ApiContextInitializer.init();
	}
	
	public void initBot(){
		
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			telegramBotsApi.registerBot(new TelegramService());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method must always return Bot username
	 */
	@Override
	public String getBotUsername() {		
		return BOTUSERNAME;
	}

	/**
	 * This method will be called when an Update is received by your bot
	 */
	@Override
	public void onUpdateReceived(Update update) {
		Message message = update.getMessage();
		if (message != null && message.hasText()) {			
			try {
				CrawlerService crawlerService = new CrawlerService();				
				ConvertData convertData = new ConvertData();
				System.out.println("Begin");				
				convertData.setLinks(crawlerService.runCrawler(message.getText(), 2));
				if(convertData.getLinks() != null){
					convertData.setAlienLink(crawlerService.getAlienLink());
					convertData.convert();
					System.out.println("End");					
					String s = "<a href=\"http://localhost:9090/ServerForData/\">" + "Download results" + "</a>";
					sendMsg(message, s);
					
				}
				else{
					String key = message.getText();
					switch (key) {
					case "/help":
						sendMsg(message, "Hello, my name is Cortesbot");
						break;
					case "/start":
						sendMsg(message, "I'm working");
						break;
					default:						
						sendMsg(message, "I don`t know how answer to you");
						break;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}
		
	}

	/**
	 * This method must always return Bot Token
	 */
	@Override
	public String getBotToken() {
		return BOTTOKEN;
	}
	
	private void sendMsg(Message message, String text) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.enableMarkdown(false);
		sendMessage.setChatId(message.getChatId().toString());
		//sendMessage.setReplyToMessageId(message.getMessageId());
		sendMessage.setText(text);
		try {
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
