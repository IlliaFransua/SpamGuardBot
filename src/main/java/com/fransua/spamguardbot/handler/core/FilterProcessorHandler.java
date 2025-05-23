package com.fransua.spamguardbot.handler.core;

import org.telegram.telegrambots.meta.api.objects.Update;

public class FilterProcessorHandler implements Handler {

  private final Filter filter;
  private final Processor processor;

  public FilterProcessorHandler(Filter filter, Processor processor) {
    this.filter = filter;
    this.processor = processor;
  }

  @Override
  public boolean canHandle(Update update) {
    return filter.canHandle(update);
  }

  @Override
  public void process(Update update) {
    processor.process(update);
  }
}
