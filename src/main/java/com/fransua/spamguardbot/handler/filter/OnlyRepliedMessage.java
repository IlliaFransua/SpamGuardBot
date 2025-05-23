package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.util.UpdateUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class OnlyRepliedMessage extends Filter {

  @Override
  public boolean canThisHandle(Update update) {
    return OnlyRepliedMessage.hasReplyToMessage(update);
  }

  static boolean hasReplyToMessage(Update update) {
    Message message = UpdateUtils.extractMessage(update).orElse(null);
    if (message == null) {
      return false;
    }
    return message.getReplyToMessage() != null;
  }
}
