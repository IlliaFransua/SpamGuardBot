package com.fransua.spamguardbot.handler.core;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {

  boolean canHandle(Update update);

  void process(Update update);
}
