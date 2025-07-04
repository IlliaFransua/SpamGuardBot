package com.fransua.spamguardbot.util;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class ParsedUpdate {

  private final Update update;

  private Message message;

  private String anyTextFromMessage;

  private boolean isAdminMsg;

  public ParsedUpdate(Update update) {
    this.update = update;
  }

  public Message getMessage() {
    return null;
  }

  public String getAnyTextFromMessage() {
    if (anyTextFromMessage == null) {
      anyTextFromMessage = UpdateUtils.extractAnyTextFromMessage(getMessage())
          .orElseThrow(() -> new IllegalArgumentException("Message contains no text."));
    }
    return anyTextFromMessage;
  }

  public boolean isAdminMessage() {
    return UpdateUtils.isAdminMessage(message);
  }

}
