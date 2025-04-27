package com.fransua.spamguardbot.processor;


import com.fransua.spamguardbot.handler.UpdateHandler;
import com.fransua.spamguardbot.util.ParsedUpdate;
import com.fransua.spamguardbot.util.SafeHandlerWrapper;
import com.fransua.spamguardbot.util.UpdateContext;
import jakarta.annotation.PreDestroy;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public class ThreadedUpdateProcessor implements UpdateProcessor {

  private final ExecutorService executorService;

  private final TelegramClient telegramClient;

  private final List<UpdateHandler> updateHandlerList;

  public ThreadedUpdateProcessor(
      ExecutorService executorService,
      TelegramClient telegramClient,
      List<UpdateHandler> updateHandlerList
  ) {
    this.executorService = executorService;
    this.telegramClient = telegramClient;
    this.updateHandlerList = updateHandlerList;
  }

  @Override
  public void process(List<Update> updateList) {
    try {
      for (Update update : updateList) {
        executorService.submit(() -> {
          process(update);
        });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void process(Update update) {
    ParsedUpdate parsedUpdate = new ParsedUpdate(update);
    UpdateContext.setParsedUpdate(parsedUpdate);
    try {
      updateHandlerList.stream()
          .filter(handler -> handler.canHandle(update))
          .sorted(Comparator.comparingInt(UpdateHandler::getPriority))
          .forEach(handler -> SafeHandlerWrapper.safeHandle(handler, telegramClient, update));
    } finally {
      UpdateContext.clear();
    }
  }

  @PreDestroy
  public void shutdown() {
    executorService.shutdown();
    try {
      if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
          System.err.println("ExecutorService не смог завершиться");
        }
      }
    } catch (Exception e) {
      executorService.shutdownNow();
      e.printStackTrace();
    }
  }
}
