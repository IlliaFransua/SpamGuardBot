package com.fransua.spamguardbot.handler.core;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Processor {

  void process(Update update);
}
