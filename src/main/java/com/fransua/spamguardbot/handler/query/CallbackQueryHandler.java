package com.fransua.spamguardbot.handler.query;


import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.UpdateHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;


public abstract class CallbackQueryHandler extends UpdateHandler {

  @Override
  public boolean canHandle(Update update) {
    if (!update.hasCallbackQuery()) {
      return false;
    }
    // TODO: instanceof можно вообще вынести отдельно. Подумать нужно, как лучше сделать, когда у всех handlers гора методов к проверке и обработки update, что есть нарушение первого правила SOLID: 1 класс – 1 ответственность.
    CallbackQuery callbackQuery = update.getCallbackQuery();
    if (!(callbackQuery.getMessage() instanceof Message)) {
      return false;
    }
    String callbackQueryData = callbackQuery.getData();
    return BotConfig.getCallbackDataList().stream()
        .anyMatch(callbackData -> callbackData.equals(callbackQueryData));
  }
}
