package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.util.UpdateUtils;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class OnlyAdminFilter extends Filter {

  private final TelegramClient telegramClient;

  public OnlyAdminFilter(TelegramClient telegramClient) {
    this.telegramClient = telegramClient;
  }

  @Override
  public boolean canThisHandle(Update update) {
    return OnlyAdminFilter.isUserAdmin(update) || hasAdminPermissions(telegramClient, update);
  }

  static boolean isUserAdmin(Update update) {
    Optional<User> userOptional = UpdateUtils.extractUser(update);
    if (userOptional.isEmpty()) {
      return false;
    }

    long userId = userOptional.get().getId();
    return BotConfig.isUserAdmin(userId);
  }

  static boolean hasAdminPermissions(TelegramClient telegramClient, Update update) {
    Optional<Message> messageOptional = UpdateUtils.extractMessage(update);
    if (messageOptional.isEmpty()) {
      return false;
    }
    Message message = messageOptional.get();
    Optional<User> optionalUser = UpdateUtils.extractUser(update);
    if (optionalUser.isEmpty()) {
      return false;
    }
    User user = optionalUser.get();
    GetChatMember getChatMember = GetChatMember
        .builder()
        .chatId(message.getChatId())
        .userId(user.getId())
        .build();
    try {
      ChatMember chatMember = telegramClient.execute(getChatMember);
      return chatMember instanceof ChatMemberAdministrator;
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
    return false;
  }
}
