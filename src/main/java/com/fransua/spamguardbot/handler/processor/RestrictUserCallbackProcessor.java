package com.fransua.spamguardbot.handler.processor;

import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.core.Processor;
import com.fransua.spamguardbot.service.BotConfigService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.time.Instant;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class RestrictUserCallbackProcessor implements Processor {

  private final TelegramClient telegramClient;
  private final BotConfigService botConfigService;
  private final Gson gson = new Gson();

  public RestrictUserCallbackProcessor(
      TelegramClient telegramClient, BotConfigService botConfigService) {
    this.telegramClient = telegramClient;
    this.botConfigService = botConfigService;
  }

  @Override
  public void process(Update update) {
    CallbackQuery callbackQuery = update.getCallbackQuery();
    String callbackQueryData = callbackQuery.getData();

    if (BotConfig.getMuteReporterCallbackDataList().stream()
        .noneMatch(callbackData -> callbackData.equals(callbackQueryData))) {
      return;
    }

    if (callbackQueryData.equals(BotConfig.getMuteReporterCallbackData(true))) {
      try {
        telegramClient.execute(
            AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId())
                .text("🙉 The user is already muted for 3 hours. This action cannot be undone.")
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

    long spamMessageChatId = Long.parseLong(jsonObject.get("chatId").toString());
    long senderUserId = Long.parseLong(jsonObject.get("senderUserId").toString());

    try {
      RestrictChatMember restrictChatMember = RestrictChatMember.builder()
          .chatId(spamMessageChatId)
          .userId(senderUserId)
          .permissions(
              ChatPermissions.builder()
                  .canSendMessages(false)
                  .canSendPolls(false)
                  .canSendOtherMessages(false)
                  .canAddWebPagePreviews(false)
                  .canChangeInfo(false)
                  .canInviteUsers(false)
                  .canPinMessages(false)
                  .build())
          .untilDate((int) Instant.now().getEpochSecond() + 3 * 60 * 60)
          .build();
      telegramClient.execute(restrictChatMember);

      telegramClient.execute(
          AnswerCallbackQuery.builder()
              .callbackQueryId(callbackQuery.getId())
              .text("✅ Offender muted for the next 3 hours.")
              .showAlert(false)
              .build());

      InlineKeyboardMarkup inlineKeyboardMarkup = callbackQueryMessageessage.getReplyMarkup();

      boolean wasDeleted = false;
      boolean wasReporterBanned = false;
      boolean wasReporterMuted = true;

      for (InlineKeyboardRow list : inlineKeyboardMarkup.getKeyboard()) {
        for (InlineKeyboardButton button : list) {
          String callbackData = button.getCallbackData();

          if (callbackData.equals(BotConfig.getDeleteMessageCallbackData(true))) {
            wasDeleted = true;
          }
        }
      }

      telegramClient.execute(
          EditMessageReplyMarkup.builder()
              .chatId(callbackQueryMessageessage.getChatId())
              .messageId(callbackQueryMessageessage.getMessageId())
              .replyMarkup(
                  BotConfig.createInlineKeyboardMarkup(
                      wasDeleted, wasReporterBanned, wasReporterMuted))
              .build());
    } catch (TelegramApiException e) {
      String description = e.getMessage();
      try {
        telegramClient.execute(
            AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId())
                .text("❌ Error: " + e.getMessage())
                .showAlert(true)
                .build());
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
