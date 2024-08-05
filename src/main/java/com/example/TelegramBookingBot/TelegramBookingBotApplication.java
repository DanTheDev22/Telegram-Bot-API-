package com.example.TelegramBookingBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TelegramBookingBotApplication {

	public static void main(String[] args) throws TelegramApiException {
		ConfigurableApplicationContext ctx = SpringApplication.run(TelegramBookingBotApplication.class, args);
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		BookingBot bookingBot = ctx.getBean(BookingBot.class);
		botsApi.registerBot(bookingBot);
	}

}
