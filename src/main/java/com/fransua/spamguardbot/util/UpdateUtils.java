package com.fransua.spamguardbot.util;


import com.fransua.spamguardbot.config.BotConfig;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.ArrayList;
import java.util.Arrays;
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

  public static boolean isAdminMessage(Message msg) {
    long fromUserId = msg.getFrom().getId();
    return Arrays.stream(BotConfig.adminTeam).anyMatch((adminId) -> adminId == fromUserId);
  }
}
