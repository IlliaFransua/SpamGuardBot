package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.util.UpdateUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class OnlyMessageWithSender extends Filter {

  @Override
  public boolean canHandle(Update update) {
    return hasMessageSender(update) && canNextHandle(update);
  }

  private boolean hasMessageSender(Update update) {
    Message message = UpdateUtils.extractMessage(update).orElse(null);
    return message != null && message.getFrom() != null;
  }
}
