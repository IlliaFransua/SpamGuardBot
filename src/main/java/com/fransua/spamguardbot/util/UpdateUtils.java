package com.fransua.spamguardbot.util;


import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Optional;


public class UpdateUtils {

  public static Optional<Message> extractMessage(Update update) {
    if (update.hasMessage()) {
      return Optional.of(update.getMessage());
    } else if (update.hasEditedMessage()) {
      return Optional.of(update.getEditedMessage());
    }
    return Optional.empty();
  }

  public static Optional<String> extractAnyTextFromMessage(Message msg) {
    if (msg.hasText()) {
      return Optional.of(msg.getText());
    } else if (msg.hasCaption()) {
      return Optional.of(msg.getCaption());
    }
    return Optional.empty();
  }
}
