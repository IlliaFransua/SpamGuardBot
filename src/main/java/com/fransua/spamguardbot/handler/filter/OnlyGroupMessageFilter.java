package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.util.UpdateUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class OnlyGroupMessageFilter extends Filter {

  @Override
  public boolean canHandle(Update update) {
    return OnlyGroupMessageFilter.updateIsGroupMessage(update) && canNextHandle(update);
  }

  static boolean updateIsGroupMessage(Update update) {
    Message message = UpdateUtils.extractMessage(update).orElse(null);
    if (message == null) {
      return false;
    }
    return message.isGroupMessage();
  }
}
