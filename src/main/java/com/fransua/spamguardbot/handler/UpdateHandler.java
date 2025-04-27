package com.fransua.spamguardbot.handler;


import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public interface UpdateHandler {

  boolean canHandle(Update update);

  void handle(TelegramClient telegramClient, Update update) throws TelegramApiException;

  default int getPriority() {
    return 100;
  }
}
