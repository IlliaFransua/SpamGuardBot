package com.fransua.spamguardbot.handler;


import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public abstract class UpdateHandler {

  protected final ThreadLocal<HandlerProcessingStatus> handlerProcessingStatus =
      ThreadLocal.withInitial(HandlerProcessingStatus::defaultStatus);
  
  public abstract boolean canHandle(Update update);

  public abstract void handle(TelegramClient telegramClient, Update update) throws Exception;

  public int getPriority() {
    return 100;
  }
}
