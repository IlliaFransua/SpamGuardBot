package com.fransua.spamguardbot.factory;


import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.processor.ThreadedUpdateProcessor;
import com.fransua.spamguardbot.handler.UpdateHandler;
import com.fransua.spamguardbot.handler.message.text.command.StartCommandHandler;
import com.fransua.spamguardbot.handler.message.text.spam.SpamTextHandler;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ProcessorFactory {

  private ProcessorFactory() {
  }

  public static ThreadedUpdateProcessor createProcessor() {
    return createProcessor(8, BotConfig.SpamGuardBot_TOKEN);
  }

  public static ThreadedUpdateProcessor createProcessor(int nThreads, String SpamGuardBot_TOKEN) {
    ExecutorService executorService = createExecutorService(nThreads);
    TelegramClient telegramClient = createTelegramClient(SpamGuardBot_TOKEN);
    List<UpdateHandler> updateHandlerList = createUpdateHandlerList();
    return new ThreadedUpdateProcessor(executorService, telegramClient, updateHandlerList);
  }

  private static ExecutorService createExecutorService(int nThreads) {
    return Executors.newFixedThreadPool(nThreads);
  }

  private static TelegramClient createTelegramClient(String SpamGuardBot_TOKEN) {
    return new OkHttpTelegramClient(SpamGuardBot_TOKEN);
  }

  private static List<UpdateHandler> createUpdateHandlerList() {
    List<UpdateHandler> updateHandlerList = new ArrayList<>();
    updateHandlerList.add(new StartCommandHandler());
    updateHandlerList.add(new SpamTextHandler());
//    updateHandlerList.add(new PhotoHandler());
    return updateHandlerList;
  }

}
