package com.fransua.spamguardbot.handler.core;

import com.fransua.spamguardbot.handler.filter.FilterChainBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class Filter {

  protected Filter next;

  public abstract boolean canHandle(Update update);

  // TODO: изменить чтобы созданные нами фильтры не вызывали этот метод дальше, а он сам будет вызываться (то есть наследники будут реализовывать метод который мы потом отдельно вызовем для цепочки)
  protected boolean canNextHandle(Update update) {
    return next == null || next.canHandle(update);
  }

  public void setNextFilter(Filter next) {
    this.next = next;
  }

  public FilterChainBuilder builder() {
    return new FilterChainBuilder();
  }
}
