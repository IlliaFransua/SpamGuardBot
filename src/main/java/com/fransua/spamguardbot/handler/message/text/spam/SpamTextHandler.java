package com.fransua.spamguardbot.handler.message.text.spam;


import com.fransua.spamguardbot.handler.message.text.BaseTextHandler;
import com.fransua.spamguardbot.service.spam.ads.AdsDetector;
import com.fransua.spamguardbot.util.UpdateContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public class SpamTextHandler extends BaseTextHandler {

  @Override
  public boolean canHandle(Update update) {
    return super.canHandle(update);
  }

  @Override
  public void handle(TelegramClient telegramClient, Update update) throws Exception {
    Message msg = UpdateContext.getParsedUpdate().getMessage();
    String text = UpdateContext.getParsedUpdate().getAnyTextFromMessage();
    AdsDetector adsDetector = new AdsDetector();
    if (adsDetector.isSpam(text)) {
      String answer = """
          *👊 А это я удалю*
          """;
      telegramClient.execute(SendMessage
          .builder()
          .chatId(msg.getChatId())
          .replyToMessageId(msg.getMessageId())
          .text(answer)
          .parseMode("Markdown")
          .build());
      telegramClient.execute(DeleteMessage
          .builder()
          .chatId(msg.getChatId())
          .messageId(msg.getMessageId())
          .build());
    }
  }

  private boolean isSpam(String text) {
    return text.trim().toLowerCase().startsWith("spam");
  }

  @Override
  public int getPriority() {
    return 1;
  }
}
