package com.fransua.spamguardbot.handler.message.text.command;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fransua.spamguardbot.config.BotCommands;
import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.message.text.BaseTextHandler;
import com.fransua.spamguardbot.service.BotConfigService;
import com.fransua.spamguardbot.util.UpdateContext;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public class ReportCommandHandler extends BaseTextHandler {

  @Override
  public boolean canHandle(Update update) {
    if (!super.canHandle(update)) {
      return false;
    }
    String text = UpdateContext.getParsedUpdate().getAnyTextFromMessage();
    return text.startsWith(BotCommands.REPORT_COMMAND);
  }

  @Override
  public int getPriority() {
    return 10;
  }

  @Override
  public void handle(TelegramClient telegramClient, Update update) throws Exception {
    Message msg = UpdateContext.getParsedUpdate().getMessage();
    String text = UpdateContext.getParsedUpdate().getAnyTextFromMessage();
    if (!text.startsWith(BotCommands.REPORT_COMMAND)) {
      return;
    }
    String note = text.substring(BotCommands.REPORT_COMMAND.length()).strip();

    int msgId = msg.getMessageId();
    long chatId = msg.getChatId();
    long fromChatId = msg.getReplyToMessage().getChatId();
    int replyToMessageId = msg.getReplyToMessage().getMessageId();

    String answer = """
        *✅ Жалоба отправлена*
        """;

    try {
      BotConfigService botConfigService = new BotConfigService();
      long logChannelId = botConfigService.getLogChannelId();

      Message sentMessage = telegramClient.execute(ForwardMessage
          .builder()
          .chatId(logChannelId)
          .fromChatId(fromChatId)
          .messageId(replyToMessageId)
          .build());

      ZonedDateTime currentTimeUtc = ZonedDateTime.now(java.time.ZoneOffset.UTC);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
      String timeString = currentTimeUtc.format(formatter);

      String internalId = String.valueOf(fromChatId).replaceFirst("^-100", "");
      String msgLink = String.format("https://t.me/c/%s/%d", internalId, replyToMessageId);

      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("chatId", fromChatId);
      jsonObject.addProperty("messageId", replyToMessageId);

      Gson gson = new Gson();
      String hiddenJson = gson.toJson(jsonObject);

      String reportText = String.format("""
              ⌚️ <b>%s</b>
              
              💎 <a href="%s">Ссылка на сообщение</a>
              
              🌚 <span class="tg-spoiler">%s</span>
              
              🏆 Сработала команда <code>/report</code>
              
              📮 <i>Поступиола жаолба от:</i>
              Имя: <b>%s</b>
              Фамилия: <b>%s</b>
              username: <b>@%s</b>
              
              📍 <i>Поступиола жаолба на:</i>
              Имя: <b>%s</b>
              Фамилия: <b>%s</b>
              username: <b>@%s</b>
              
              📝 <i>Заметка от жалующегося:</i>
              <code>%s</code>
              """, timeString, msgLink, hiddenJson,
          msg.getFrom().getFirstName(),
          msg.getFrom().getLastName(),
          msg.getFrom().getUserName(),
          msg.getReplyToMessage().getFrom().getFirstName(),
          msg.getReplyToMessage().getFrom().getLastName(),
          msg.getReplyToMessage().getFrom().getUserName(),
          note);

      telegramClient.execute(SendMessage
          .builder()
          .text(reportText)
          .replyToMessageId(sentMessage.getMessageId())
          .replyMarkup(BotConfig.createInlineKeyboardMarkup(false, false, false))
          .chatId(logChannelId)
          .parseMode("HTML")
          .build());
    } catch (Exception e) {
      e.printStackTrace();
      answer = """
          *Жалоба не отправлена потому что ошибка*
          """;
    } finally {
      try {
        telegramClient.execute(DeleteMessage
            .builder()
            .chatId(chatId)
            .messageId(msgId)
            .build());

        telegramClient.execute(SendMessage
            .builder()
            .chatId(chatId)
            .text(answer)
            .parseMode("Markdown")
            .build());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
