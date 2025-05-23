package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.handler.core.Filter;
import org.telegram.telegrambots.meta.api.objects.Update;

public class OnlyDirectMessageFilter extends Filter {

  @Override
  public boolean canHandle(Update update) {
    return OnlyDirectMessageFilter.updateIsDirectMessage(update)
        && canNextHandle(update);
  }

  static boolean updateIsDirectMessage(Update update) {
    return update.getMessage().isUserMessage();
  }
}
