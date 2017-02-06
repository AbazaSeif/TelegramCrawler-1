package com.devcortes.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.devcortes.components.service.DomainService;

/**
 * Service that manage telegram bot
 * 
 * @author cortes
 *
 */
@Service
@Scope(scopeName = "prototype")
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
	private static final String PARSE_PAGE = "yes";
	private static final String GET_INFO_FROM_DB = "no";
	private static final String PATH_TO_RESULT_FILE = "/var/www/crawler.com/public_html/results/";
	private static final String QUESTION_FOR_USER = "I have already parsed results. Can you want wait some time or you want get information quickly (yes/no)?";
	private static final String PLEASE_WAIT = "Please wait........";

	private Map<Long, String> lastMessagesOfUsers;

	private String urlFromTelegram;
	private boolean requestFromBot;

	@Autowired
	private StorageFilesService storageFilesService;

	@Autowired
	private CallCrawlerService callCrawlerService;

	@Autowired
	private DomainService domainService;

	/**
	 * Initialize Api Context
	 */
	static {
		ApiContextInitializer.init();
	}

	/**
	 * Initialization of Api Context
	 */
	public void initBot() {

		lastMessagesOfUsers = new HashMap<>();
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

		String key = message.getText();

		urlFromTelegram = message.getText();

		Long chatId = message.getChatId();

		if (!StringUtils.isBlank(domainService.getDomain(urlFromTelegram))) {

			lastMessagesOfUsers.put(chatId, message.getText());

			if (storageFilesService.urlIsExistInDB(urlFromTelegram)) {
				requestFromBot = true;
			} else {
				requestFromBot = false;
				key = PARSE_PAGE;
			}
		}

		if (requestFromBot) {
			sendMsg(message, QUESTION_FOR_USER);
			requestFromBot = false;
			return;
		}

		switch (key) {
		case REQUEST_TO_BOT_ON_HELP:
			sendMsg(message, ANSWER_FROM_BOT_ON_HELP);
			break;
		case REQUEST_TO_BOT_ON_START:
			sendMsg(message, ANSWER_FROM_BOT_ON_START);
			break;
		case PARSE_PAGE:

			sendMsg(message, PLEASE_WAIT);
			if (callCrawlerService.callCrawler(lastMessagesOfUsers.get(chatId))) {

				String s = FIRST_PART_OF_ANSWER_BOT + lastMessagesOfUsers.get(chatId);
				sendMsg(message, s);

			} else {
				sendMsg(message, DEFAULT_ANSWER_FROM_BOT);
			}

			break;
		case GET_INFO_FROM_DB:
			storageFilesService.getByUrl(PATH_TO_RESULT_FILE, lastMessagesOfUsers.get(chatId));
			String s = FIRST_PART_OF_ANSWER_BOT + lastMessagesOfUsers.get(chatId);
			sendMsg(message, s);
			break;
		default:

			sendMsg(message, DEFAULT_ANSWER_FROM_BOT);

			break;
		}

	}

	/**
	 * Function for send message from bot to telegram
	 * 
	 * @param message
	 *            message - message information that telegram get
	 * @param text
	 *            text-text that bot send to telegram
	 */
	private void sendMsg(Message message, String text) {

		boolean enableMarkdownMessage = false;
		SendMessage messageForSend = new SendMessage();
		messageForSend.enableMarkdown(enableMarkdownMessage);
		messageForSend.setChatId(message.getChatId().toString());
		messageForSend.setText(text);

		try {
			sendMessage(messageForSend);
		} catch (TelegramApiException e) {
			log.error("Error in sendMsg ---  " + e.getMessage());
		}

	}
}
