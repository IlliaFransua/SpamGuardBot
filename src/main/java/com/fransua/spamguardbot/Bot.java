package com.fransua.spamguardbot;


import com.fransua.spamguardbot.processor.UpdateProcessor;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


public class Bot implements LongPollingUpdateConsumer {

  private final UpdateProcessor processor;

  public Bot(UpdateProcessor processor) {
    this.processor = processor;
  }

  @Override
  public void consume(List<Update> list) {
    try {
      /// TODO: idempotency
      processor.process(list);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
