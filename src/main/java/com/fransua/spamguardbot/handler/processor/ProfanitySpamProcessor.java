package com.fransua.spamguardbot.handler.processor;

import com.fransua.spamguardbot.handler.core.Processor;
import com.fransua.spamguardbot.service.AdsDetectorService;
import com.fransua.spamguardbot.service.BotConfigService;
import com.fransua.spamguardbot.util.UpdateContext;
import com.fransua.spamguardbot.util.UpdateUtils;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

// TODO: refactor
public class ProfanitySpamProcessor implements Processor {

  private final TelegramClient telegramClient;
  private final BotConfigService botConfigService;

  public ProfanitySpamProcessor(TelegramClient telegramClient,
      BotConfigService botConfigService) {
    this.telegramClient = telegramClient;
    this.botConfigService = botConfigService;
  }

  @Override
  public void process(Update update) {
    Optional<Message> optionalMessage = UpdateUtils.extractMessage(update);
    if (optionalMessage.isEmpty()) {
      return;
    }
    Message msg = optionalMessage.get();
    Optional<String> optionalText = UpdateUtils.extractAnyTextFromMessage(msg);
    if (optionalText.isEmpty()) {
      return;
    }
    String text = optionalText.get();
    AdsDetectorService adsDetector = new AdsDetectorService();
    if (adsDetector.isSpam(text)) {
      String answer = """
          *👊 А це я видалю*
          """;
      try {
        telegramClient.execute(SendMessage
            .builder()
            .chatId(msg.getChatId())
            .replyToMessageId(msg.getMessageId())
            .text(answer)
            .parseMode("Markdown")
            .build());
      } catch (Exception e) {
        e.printStackTrace();

      }

      BotConfigService configService = new BotConfigService();
      long logChannelId = configService.getLogChannelId();
      Message sentMessage = null;
      try {
        sentMessage = telegramClient.execute(ForwardMessage
            .builder()
            .chatId(logChannelId)
            .fromChatId(msg.getChatId())
            .messageId(msg.getMessageId())
            .build());
      } catch (Exception e) {
        e.printStackTrace();
      }

      answer = """
          *🔍 Повідомлення було видалено автоматично.*
          """;
      if (sentMessage != null) {
        try {
          telegramClient.execute(SendMessage
              .builder()
              .chatId(logChannelId)
              .replyToMessageId(sentMessage.getMessageId())
              .text(answer)
              .parseMode("Markdown")
              .build());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      try {
        telegramClient.execute(DeleteMessage
            .builder()
            .chatId(msg.getChatId())
            .messageId(msg.getMessageId())
            .build());
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
  }
}
