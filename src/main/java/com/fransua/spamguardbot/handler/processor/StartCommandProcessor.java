package com.fransua.spamguardbot.handler.processor;

import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.core.Processor;
import com.fransua.spamguardbot.service.BotConfigService;
import com.fransua.spamguardbot.util.UpdateUtils;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

// TODO: refactor
public class StartCommandProcessor implements Processor {

  private final TelegramClient telegramClient;
  private final BotConfigService botConfigService;

  public StartCommandProcessor(TelegramClient telegramClient, BotConfigService botConfigService) {
    this.telegramClient = telegramClient;
    this.botConfigService = botConfigService;
  }

  @Override
  public void process(Update update) {
    Optional<Message> optionalMessage = UpdateUtils.extractMessage(update);
    if (optionalMessage.isEmpty()) {
      return;
    }
    Message actualMessage = optionalMessage.get();
    String text = UpdateUtils.extractAnyTextFromMessage(actualMessage).orElse("");
    if (text.startsWith(BotConfig.Commands.START_COMMAND)) {
      String answer = """
          *⚔️ Привіт путник*
          """;
      try {
        telegramClient.execute(SendMessage
            .builder()
            .chatId(actualMessage.getChatId())
            .text(answer)
            .replyToMessageId(actualMessage.getMessageId())
            .parseMode("Markdown")
            .build());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
