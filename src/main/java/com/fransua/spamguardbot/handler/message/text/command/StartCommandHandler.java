package com.fransua.spamguardbot.handler.message.text.command;


import com.fransua.spamguardbot.config.BotCommands;
import com.fransua.spamguardbot.handler.message.text.BaseTextHandler;
import com.fransua.spamguardbot.util.UpdateContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public class StartCommandHandler extends BaseTextHandler {

  @Override
  public boolean canHandle(Update update) {
    if (!super.canHandle(update)) {
      return false;
    }
    String text = UpdateContext.getParsedUpdate().getAnyTextFromMessage();
    return text.equalsIgnoreCase(BotCommands.START_COMMAND);
  }

  @Override
  public int getPriority() {
    return 10;
  }

  @Override
  public void handle(TelegramClient telegramClient, Update update) throws Exception {
    Message msg = UpdateContext.getParsedUpdate().getMessage();
    String text = UpdateContext.getParsedUpdate().getAnyTextFromMessage();
    if (text.startsWith(BotCommands.START_COMMAND)) {
      String answer = """
          *🏳️‍🌈 Привет путник*
          """;
      telegramClient.execute(SendMessage
          .builder()
          .chatId(msg.getChatId())
          .text(answer)
          .replyToMessageId(msg.getMessageId())
          .parseMode("Markdown")
          .build());
    }
  }
}
