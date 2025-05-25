package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.handler.core.Filter;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotFilter extends Filter {

  private final Filter filter;

  public NotFilter(Filter filter) {
    this.filter = filter;
  }

  @Override
  public boolean canThisHandle(Update update) {
    return filter != null && !filter.canHandle(update);
  }
}
