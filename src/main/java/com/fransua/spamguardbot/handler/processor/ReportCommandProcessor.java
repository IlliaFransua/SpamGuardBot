package com.fransua.spamguardbot.handler.processor;

import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.core.Processor;
import com.fransua.spamguardbot.service.BotConfigService;
import com.fransua.spamguardbot.util.UpdateUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class ReportCommandProcessor implements Processor {

  private final TelegramClient telegramClient;
  private final BotConfigService botConfigService;
  private final Gson gson = new Gson();

  public ReportCommandProcessor(TelegramClient telegramClient, BotConfigService botConfigService) {
    this.telegramClient = telegramClient;
    this.botConfigService = botConfigService;
  }

  @Override
  public void process(Update update) {
    Message message = UpdateUtils.extractMessage(update).orElse(null);
    String fullCommandText = UpdateUtils.extractAnyTextFromMessage(message).orElse("");
    String note = extractReportNote(fullCommandText);
    ReportMessageDetails details = extractMessageDetails(message);

    String userResponse = "*✅ Complaint sent.*";
    try {
      long logChannelId = botConfigService.getLogChannelId();
      synchronized (this) {
        Message sentReportedMessage = forwardReportedMessage(details, logChannelId);
        String reportTextForLog = buildReportTextForLog(details, note);
        sendReportToLogChannel(reportTextForLog, sentReportedMessage.getMessageId(), logChannelId);
      }
    } catch (TelegramApiException e) {
      e.printStackTrace();
      userResponse = "*❌ Complaint not sent due to an error.*";
    } catch (Exception e) {
      e.printStackTrace();
      userResponse = "*❌ Complaint not sent due to an unknown error.*";
    } finally {
      try {
        cleanUpAndNotifyUser(details, userResponse);
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }
  }

  private String extractReportNote(String fullCommandText) {
    return fullCommandText.substring(BotConfig.Commands.REPORT_COMMAND.length());
  }

  private record ReportMessageDetails(
      // Fields from the message that triggered report
      int messageId,
      long chatId,
      long senderUserId,
      String senderFirstName,
      String senderLastName,
      String senderUserName,

      // Fields from the message that was replied
      int replyToMessageId,
      long repliedMessageChatId,
      long replyToUserId,
      String replyToFirstName,
      String replyToLastName,
      String replyToUserName) {
  }

  private ReportMessageDetails extractMessageDetails(Message message) {
    if (message == null || message.getFrom() == null) {
      throw new IllegalArgumentException("Report message must be from a user message.");
    }
    Message repliedMessage = message.getReplyToMessage();
    if (repliedMessage == null || repliedMessage.getFrom() == null) {
      throw new IllegalArgumentException("Report message must be replied to a user message.");
    }
    return new ReportMessageDetails(
        // Fields from the message that triggered report
        message.getMessageId(),
        message.getChatId(),
        message.getFrom().getId(),
        message.getFrom().getFirstName(),
        message.getFrom().getLastName(),
        message.getFrom().getUserName(),

        // Fields from the message that was replied
        repliedMessage.getMessageId(),
        repliedMessage.getChatId(),
        repliedMessage.getFrom().getId(),
        repliedMessage.getFrom().getFirstName(),
        repliedMessage.getFrom().getLastName(),
        repliedMessage.getFrom().getUserName());
  }

  private Message forwardReportedMessage(ReportMessageDetails details, long logChannelId)
      throws TelegramApiException {
    return telegramClient.execute(
        ForwardMessage.builder()
            .messageId(details.replyToMessageId())
            .fromChatId(details.repliedMessageChatId())
            .chatId(logChannelId)
            .build());
  }

  private String buildReportTextForLog(ReportMessageDetails details, String note) {
    ZonedDateTime currentTimeUtc = ZonedDateTime.now(java.time.ZoneOffset.UTC);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
    String timeString = currentTimeUtc.format(formatter);

    String internalId = String.valueOf(details.chatId).replaceFirst("^-100", "");
    String msgLink = String.format("https://t.me/c/%s/%d", internalId, details.replyToMessageId());

    JsonObject jsonObject = getJsonObject(details);

    String hiddenJson = gson.toJson(jsonObject);

    return String.format(
        """
            ⌚️ <b>%s</b>

            💎 <a href="%s">Link to message</a>

            🌚 <span class="tg-spoiler">%s</span>

            🏆 The command <code>/report</code> was triggered

            📮 <i>Complaint received from:</i>
            Name: <b>%s</b>
            Surname: <b>%s</b>
            username: <b>@%s</b>

            📍 <i>Complaint received about:</i>
            Name: <b>%s</b>
            Surname: <b>%s</b>
            username: <b>@%s</b>

            📝 <i>Note from the complainant:</i>
            <code>%s</code>
            """,
        timeString,
        msgLink,
        hiddenJson,
        details.senderFirstName(),
        details.senderLastName(),
        details.senderUserName(),
        details.replyToFirstName(),
        details.replyToLastName(),
        details.replyToUserName(),
        note);
  }

  @NotNull
  private static JsonObject getJsonObject(ReportMessageDetails details) {
    JsonObject jsonObject = new JsonObject();

    // Fields from the message that triggered report
    jsonObject.addProperty("messageId", details.messageId());
    jsonObject.addProperty("chatId", details.chatId());
    jsonObject.addProperty("senderUserId", details.senderUserId());
    jsonObject.addProperty("senderFirstName", details.senderFirstName());
    jsonObject.addProperty("senderLastName", details.senderLastName());
    jsonObject.addProperty("senderUserName", details.senderUserName());

    // Fields from the message that was replied
    jsonObject.addProperty("replyToMessageId", details.replyToMessageId());
    jsonObject.addProperty("repliedMessageChatId", details.repliedMessageChatId());
    jsonObject.addProperty("replyToUserId", details.replyToUserId());
    jsonObject.addProperty("replyToFirstName", details.replyToFirstName());
    jsonObject.addProperty("replyToLastName", details.replyToLastName());
    jsonObject.addProperty("replyToUserName", details.replyToUserName());

    return jsonObject;
  }

  private void sendReportToLogChannel(String reportText, int replyToMessageId, long logChannelId)
      throws TelegramApiException {
    telegramClient.execute(
        SendMessage.builder()
            .text(reportText)
            .replyToMessageId(replyToMessageId)
            .replyMarkup(BotConfig.createInlineKeyboardMarkup(false, false, false))
            .chatId(logChannelId)
            .parseMode("HTML")
            .build());
  }

  private void cleanUpAndNotifyUser(ReportMessageDetails details, String userResponse)
      throws TelegramApiException {
    telegramClient.execute(
        DeleteMessage.builder().chatId(details.chatId()).messageId(details.messageId()).build());

    telegramClient.execute(
        SendMessage.builder()
            .chatId(details.chatId())
            .text(userResponse)
            .parseMode("Markdown")
            .build());
  }
}
