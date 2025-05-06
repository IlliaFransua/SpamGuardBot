package com.fransua.spamguardbot.handler.message.text;


import com.fransua.spamguardbot.handler.message.BaseMessageHandler;
import com.fransua.spamguardbot.util.UpdateContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public abstract class BaseTextHandler extends BaseMessageHandler {

  @Override
  public boolean canHandle(Update update) {
    if (!super.canHandle(update)) {
      return false;
    }
    String text = UpdateContext.getParsedUpdate().getAnyTextFromMessage();
    return !text.trim().isEmpty();
  }

  @Override
  public abstract void handle(TelegramClient telegramClient, Update update) throws Exception;
}
