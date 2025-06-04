package com.fransua.spamguardbot.handler.processor;

import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.core.Processor;
import com.fransua.spamguardbot.service.BotConfigService;
import com.fransua.spamguardbot.service.SpamSaver;
import com.fransua.spamguardbot.util.UpdateUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class DeleteMessageQueryProcessor implements Processor {

  private final TelegramClient telegramClient;
  private final BotConfigService botConfigService;
  private final Gson gson = new Gson();

  public DeleteMessageQueryProcessor(TelegramClient telegramClient,
      BotConfigService botConfigService) {
    this.telegramClient = telegramClient;
    this.botConfigService = botConfigService;
  }

  @Override
  public void process(Update update) {
    CallbackQuery callbackQuery = update.getCallbackQuery();
    String callbackQueryData = callbackQuery.getData();

    if (BotConfig.getDeleteMessageCallbackDataList().stream()
        .noneMatch(callbackData -> callbackData.equals(callbackQueryData))) {
      return;
    }

    if (callbackQueryData.equals(BotConfig.getDeleteMessageCallbackData(true))) {
      try {
        telegramClient.execute(AnswerCallbackQuery
            .builder()
            .callbackQueryId(callbackQuery.getId())
            .text("🙉 Повідомлення вже видалено. Це дію скасувати неможливо.")
            .showAlert(true)
            .build());
      } catch (Exception e) {
        e.printStackTrace();
      }
      return;
    }

    Message callbackQueryMessageessage = (Message) callbackQuery.getMessage();
    String msgText = callbackQueryMessageessage.getText();

    int beginIndex = msgText.indexOf("🌚") + 2;
    int endIndex = msgText.indexOf("🏆");
    String rawJson = msgText.substring(beginIndex, endIndex).trim();

    JsonObject jsonObject = JsonParser.parseString(rawJson).getAsJsonObject();

    long spamMessageChatId = jsonObject.get("repliedMessageChatId").getAsLong();
    int spamMessageId = jsonObject.get("replyToMessageId").getAsInt();

    try {
      telegramClient.execute(DeleteMessage
          .builder()
          .chatId(spamMessageChatId)
          .messageId(spamMessageId)
          .build());

      telegramClient.execute(AnswerCallbackQuery
          .builder()
          .callbackQueryId(callbackQuery.getId())
          .text("✅ Повідомлення порушника видалено.")
          .showAlert(false)
          .build());

      InlineKeyboardMarkup inlineKeyboardMarkup = callbackQueryMessageessage.getReplyMarkup();

      boolean wasDeleted = true;
      boolean wasReporterBanned = false;
      boolean wasReporterMuted = false;

      for (InlineKeyboardRow list : inlineKeyboardMarkup.getKeyboard()) {
        for (InlineKeyboardButton button : list) {
          String callbackData = button.getCallbackData();

          if (callbackData.equals(BotConfig.getMuteReporterCallbackData(true))) {
            wasReporterMuted = true;
          }
        }
      }

      telegramClient.execute(EditMessageReplyMarkup
          .builder()
          .chatId(callbackQueryMessageessage.getChatId())
          .messageId(callbackQueryMessageessage.getMessageId())
          .replyMarkup(
              BotConfig.createInlineKeyboardMarkup(wasDeleted, wasReporterBanned, wasReporterMuted))
          .build());

      Message replyToMessage = (Message) update.getCallbackQuery().getMessage();
      String messageText = UpdateUtils.extractAnyTextFromMessage(replyToMessage.getReplyToMessage())
          .orElse("");
      try {
        SpamSaver.save(messageText);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } catch (TelegramApiException e) {
      String description = e.getMessage();

      try {
        if (description != null && description.contains("message can't be deleted")) {
          telegramClient.execute(AnswerCallbackQuery
              .builder()
              .callbackQueryId(callbackQuery.getId())
              .text(
                  "⚠️ Помилка видалення повідомлення. Telegram не дозволяє ботам видаляти повідомлення, з моменту відправлення яких пройшло більше 48 годин.")
              .showAlert(true)
              .build());
        } else {
          telegramClient.execute(AnswerCallbackQuery
              .builder()
              .callbackQueryId(callbackQuery.getId())
              .text("❌ Помилка: " + e.getMessage())
              .showAlert(true)
              .build());
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
