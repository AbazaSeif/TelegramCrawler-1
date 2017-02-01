package com.devcortes.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan({ "com.devcortes" })

public class TelegramCrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramCrawlerApplication.class, args);
	}
}
