package com.fransua.spamguardbot.handler.core;

import com.fransua.spamguardbot.handler.filter.FilterChainBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class Filter {

  protected Filter next;

  public boolean canHandle(Update update) {
    return canThisHandle(update) && canNextHandle(update);
  }

  public abstract boolean canThisHandle(Update update);

  protected boolean canNextHandle(Update update) {
    return next == null || next.canHandle(update);
  }

  public void setNextFilter(Filter next) {
    this.next = next;
  }

  public static FilterChainBuilder builder() {
    return new FilterChainBuilder();
  }
}
