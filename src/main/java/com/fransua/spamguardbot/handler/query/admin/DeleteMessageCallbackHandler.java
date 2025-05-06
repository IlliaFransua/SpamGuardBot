package com.fransua.spamguardbot.handler.query.admin;


import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.query.CallbackQueryHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

// напишу тут коменты чтобы потом быстро отрефакторить

public class DeleteMessageCallbackHandler extends CallbackQueryHandler {

  private boolean isDataValid(String callbackQueryData) {
    return BotConfig.getCallbackDataList().stream()
        .anyMatch(callbackData -> callbackData.equals(callbackQueryData));
  }

  @Override
  public boolean canHandle(Update update) { // TODO: разобраться с хранилищем спарсенных апдейтов
    if (!update.hasCallbackQuery()) {
      return false;
    }
    CallbackQuery callbackQuery = update.getCallbackQuery();
    if (!(callbackQuery.getMessage() instanceof Message)) {
      return false;
    }
    String callbackQueryData = callbackQuery.getData();

//    System.out.println(BotConfig.getCallbackDataList().stream()
//        .anyMatch(callbackData -> callbackData.equals(callbackQuery.getData())));

//    long messageId = update.getCallbackQuery().getMessage().getMessageId();

    return isDataValid(callbackQueryData);
  }

  @Override
  public void handle(TelegramClient telegramClient, Update update) throws Exception {
//    System.out.println(update);
    CallbackQuery callbackQuery = update.getCallbackQuery();
    String callbackQueryData = callbackQuery.getData();

    // проверяем совпадет ли вообще хоть с одним делит каалбеком на делит меседжс
    if (BotConfig.getDeleteMessageCallbackDataList().stream()
        .noneMatch(callbackData -> callbackData.equals(callbackQueryData))) {
      return;
    }

    // определяем судьбу ветки развития
    if (callbackQueryData.equals(BotConfig.getDeleteMessageCallbackData(true))) {
      telegramClient.execute(AnswerCallbackQuery
          .builder()
          .callbackQueryId(callbackQuery.getId())
          .text("🙉 Сообщение уже удалено. Данное действие отменить нельзя.")
          .showAlert(true)
          .build());
      return;
    }

    // в случае успеха парсим каллбек пытаясь извлеч из него ссылку
    Message callbackQueryMessageessage = (Message) callbackQuery.getMessage();
    String msgText = callbackQueryMessageessage.getText();

    int beginIndex = msgText.indexOf("🌚") + 2;
    int endIndex = msgText.indexOf("🏆");
    String rawJson = msgText.substring(beginIndex, endIndex).trim();

    JsonObject jsonObject = JsonParser.parseString(rawJson).getAsJsonObject();

    long spamMessageChatId = jsonObject.get("chatId").getAsLong();
    int spamMessageId = Integer.parseInt(jsonObject.get("messageId").toString());

    // тут развилка от того, как у нас там получилось извлеч данные (ну ты понял)
    try {
      telegramClient.execute(DeleteMessage
          .builder()
          .chatId(spamMessageChatId)
          .messageId(spamMessageId)
          .build());

      telegramClient.execute(AnswerCallbackQuery
          .builder()
          .callbackQueryId(callbackQuery.getId())
          .text("✅ Сообщение нарушителя удалено.")
          .showAlert(false)
          .build());

      telegramClient.execute(EditMessageReplyMarkup
          .builder()
          .chatId(callbackQueryMessageessage.getChatId())
          .messageId(callbackQueryMessageessage.getMessageId())
          .replyMarkup(BotConfig.createInlineKeyboardMarkup(true, false, false))
          .build());
    } catch (TelegramApiException e) {
      // TODO: добавить бы логгер
      String description = e.getMessage();

      if (description != null && description.contains("message can't be deleted")) {
        telegramClient.execute(AnswerCallbackQuery
            .builder()
            .callbackQueryId(callbackQuery.getId())
            .text("⚠️ Ошибка удаления сообщения. Telegram не даёт ботам удалять сообщения с момента отправки которых прошло более 48 часов.")
            .showAlert(true)
            .build());
      } else {
        telegramClient.execute(AnswerCallbackQuery
            .builder()
            .callbackQueryId(callbackQuery.getId())
            .text("❌ Ошибка: " + e.getMessage())
            .showAlert(true)
            .build());
      }
    }
  }
}
