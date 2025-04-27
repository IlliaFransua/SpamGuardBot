package com.fransua.spamguardbot.util;


import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;


public class ParsedUpdate {

  private final Update update;
  private Message msg;
  private String anyTextFromMsg;

  public ParsedUpdate(Update update) {
    this.update = update;
  }

  public Message getMessage() {
    if (msg == null) {
      msg = UpdateUtils.extractMessage(update).orElseThrow(() -> new IllegalArgumentException("Message not found in update"));
    }
    return msg;
  }

  public String getAnyTextFromMessage() {
    if (anyTextFromMsg == null) {
      anyTextFromMsg = UpdateUtils.extractAnyTextFromMessage(getMessage()).orElseThrow(() -> new IllegalArgumentException("Message contains no text"));
    }
    return anyTextFromMsg;
  }

}
