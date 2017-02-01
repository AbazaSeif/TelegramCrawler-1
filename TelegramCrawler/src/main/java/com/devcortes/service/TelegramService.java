package com.devcortes.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.devcortes.components.entity.ParsePage;
import com.devcortes.components.entity.StorageResult;
import com.devcortes.components.service.ConvertData;
import com.devcortes.components.service.DomainService;

/**
 * Service that manage telegram bot
 * 
 * @author cortes
 *
 */
@Service
public class TelegramService extends TelegramLongPollingBot {

	private static final Logger log = Logger.getLogger(DomainService.class);
	private static final String BOTTOKEN = "261462589:AAHBE65vOUd6hEIqD--QJezegwXL63JZfUk";
	private static final String BOTUSERNAME = "java_practice_bot";
	private static final String FIRST_PART_OF_ANSWER_BOT = "Result in this website  http://crawler.com/?url=";
	private static final String REQUEST_TO_BOT_ON_HELP = "/help";
	private static final String REQUEST_TO_BOT_ON_START = "/start";
	private static final String ANSWER_FROM_BOT_ON_HELP = "Hello, my name is Cortesbot";
	private static final String ANSWER_FROM_BOT_ON_START = "I'm working";
	private static final String DEFAULT_ANSWER_FROM_BOT = "I don`t know how answer to you";

	/**
	 * Initialize Api Context
	 */
	static {
		ApiContextInitializer.init();
	}

	@Autowired
	private CrawlerService crawlerService;

	@Autowired
	private ConvertData convertData;

	@Autowired
	private DomainService domainService;

	/**
	 * Initialization of Api Context
	 */
	public void initBot() {

		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			telegramBotsApi.registerBot(this);
		} catch (TelegramApiException e) {
			log.error("Error in getDomain ---  " + e.getMessage());
			throw new RuntimeException(e);
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
	 * This method must always return Bot Token
	 */
	@Override
	public String getBotToken() {
		return BOTTOKEN;
	}

	/**
	 * This method will be called when an Update is received by your bot
	 */
	@Override
	public void onUpdateReceived(Update update) {

		Message message = update.getMessage();

		try {
			String url = message.getText();
			String domen = domainService.getDomain(url);
			int accessDepth = 2;
			ParsePage parsePage = new ParsePage();
			StorageResult storageLinks = new StorageResult(url, accessDepth, domen);

			if (!StringUtils.isEmpty(domen) && crawlerService.runCrawler(parsePage, storageLinks)
					&& !storageLinks.getParsePages().isEmpty()) {

				convertData.runConvertResult(storageLinks);
				String s = FIRST_PART_OF_ANSWER_BOT + message.getText();
				sendMsg(message, s);

			} else {
				String key = message.getText();
				switch (key) {
				case REQUEST_TO_BOT_ON_HELP:
					sendMsg(message, ANSWER_FROM_BOT_ON_HELP);
					break;
				case REQUEST_TO_BOT_ON_START:
					sendMsg(message, ANSWER_FROM_BOT_ON_START);
					break;
				default:
					sendMsg(message, DEFAULT_ANSWER_FROM_BOT);
					break;
				}
			}

		} catch (Exception e) {
			log.error("Error in getDomain ---  " + e.getMessage());
		}
	}

	private void sendMsg(Message message, String text) {

		SendMessage sendMessage = new SendMessage();
		sendMessage.enableMarkdown(false);
		sendMessage.setChatId(message.getChatId().toString());
		sendMessage.setText(text);

		try {
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			log.error("Error in sendMsg ---  " + e.getMessage());
		}

	}
}
