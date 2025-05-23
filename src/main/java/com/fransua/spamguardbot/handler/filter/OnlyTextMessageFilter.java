package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.util.UpdateUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class OnlyTextMessageFilter extends Filter {

  @Override
  public boolean canHandle(Update update) {
    return OnlyTextMessageFilter.messageHasNonBlankText(update) && canNextHandle(update);
  }

  static boolean messageHasNonBlankText(Update update) {
    Message message = UpdateUtils.extractMessage(update).orElse(null);
    if (message == null) {
      return false;
    }
    return UpdateUtils.extractAnyTextFromMessage(message)
        .map(text -> !text.isBlank())
        .orElse(false);
  }
}
