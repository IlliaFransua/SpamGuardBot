package com.fransua.spamguardbot.handler.message;


import com.fransua.spamguardbot.util.UpdateContext;
import com.fransua.spamguardbot.util.UpdateUtils;
import com.fransua.spamguardbot.handler.UpdateHandler;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public abstract class BaseMessageHandler implements UpdateHandler {

  @Override
  public boolean canHandle(Update update) {
    Message msg = UpdateContext.getParsedUpdate().getMessage();
    return !msg.isUserMessage() || msg.isSuperGroupMessage();
  }

  @Override
  public abstract void handle(TelegramClient telegramClient, Update update) throws TelegramApiException;
}
