package com.devcortes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.devcortes.service.TelegramService;

@RestController
@RequestMapping(value = "/bot")
public class TelegramController {
	
	@Autowired
	private TelegramService telegramService;	
	
	@RequestMapping(value="/run", method = RequestMethod.GET)
	public void runBot(){		
		
		telegramService.initBot();			
	}
}


	

	
	
