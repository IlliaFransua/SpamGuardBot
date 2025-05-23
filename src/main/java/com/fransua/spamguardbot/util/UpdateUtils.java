package com.fransua.spamguardbot.util;

import com.fransua.spamguardbot.config.BotConfig;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class UpdateUtils {

  public static Optional<Message> extractMessage(Update update) {
    if (update.hasMessage()) {
      return Optional.of(update.getMessage());
    }
    if (update.hasEditedMessage()) {
      return Optional.of(update.getEditedMessage());
    }
    return Optional.empty();
  }

  public static Optional<Message> extractCallbackQueryMessage(Update update) {
    if (update.hasCallbackQuery()) {
      MaybeInaccessibleMessage maybeInaccessibleMessage = update.getCallbackQuery().getMessage();
      if (maybeInaccessibleMessage instanceof Message actualMessage) {
        return Optional.of(actualMessage);
      }
    }
    return Optional.empty();
  }

  public static Optional<User> extractUser(Update update) {
    Optional<Message> optionalMessage = UpdateUtils.extractMessage(update);
    if (optionalMessage.isPresent()) {
      User user = optionalMessage.get().getFrom();
      if (user != null) {
        return Optional.of(user);
      }
    }

    if (update.hasCallbackQuery()) {
      CallbackQuery callbackQuery = update.getCallbackQuery();
      if (callbackQuery != null && callbackQuery.getFrom() != null) {
        User user = callbackQuery.getFrom();

        return Optional.of(user);
      }
    }
    return Optional.empty();
  }

  public static Optional<String> extractAnyTextFromMessage(Message msg) {
    if (msg == null) {
      return Optional.empty();
    }
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
