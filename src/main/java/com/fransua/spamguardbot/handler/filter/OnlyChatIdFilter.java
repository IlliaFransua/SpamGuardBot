package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.util.UpdateUtils;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class OnlyChatIdFilter extends Filter {

  private final long targetChatId;

  public OnlyChatIdFilter(long targetChatId) {
    this.targetChatId = targetChatId;
  }

  @Override
  public boolean canThisHandle(Update update) {
    return OnlyChatIdFilter.isUpdateFromTargetChat(update, targetChatId);
  }

  static boolean isUpdateFromTargetChat(Update update, long targetChatId) {
    Optional<Message> optionalMessage = UpdateUtils.extractMessage(update);
    return optionalMessage
        .map(Message::getChat)
        .map(chat -> chat.getId() == targetChatId)
        .orElse(false);
  }
}
