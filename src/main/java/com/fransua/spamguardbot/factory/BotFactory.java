package com.fransua.spamguardbot.factory;


import com.fransua.spamguardbot.Bot;
import com.fransua.spamguardbot.processor.UpdateProcessor;


public class BotFactory {

  public static Bot createBot() throws Exception {
    try {
      return new Bot(createUpdateProcessor());
    } catch (Exception e) {
      throw new RuntimeException("Failed to create SpamGuardBot", e);
    }
  }

  private static UpdateProcessor createUpdateProcessor() {
    return ProcessorFactory.createProcessor();
  }
}
