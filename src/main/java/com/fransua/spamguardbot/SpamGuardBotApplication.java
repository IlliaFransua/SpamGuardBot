package com.fransua.spamguardbot;


import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.factory.BotFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;


@SpringBootApplication
public class SpamGuardBotApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpamGuardBotApplication.class, args);
    try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
      botsApplication.registerBot(BotConfig.SpamGuardBot_TOKEN, BotFactory.createBot());
      Thread.currentThread().join();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
