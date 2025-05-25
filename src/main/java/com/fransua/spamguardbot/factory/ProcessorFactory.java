package com.fransua.spamguardbot.factory;

import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.core.UpdateHandler;
import com.fransua.spamguardbot.handler.core.Handler;
import com.fransua.spamguardbot.handler.core.Processor;
import com.fransua.spamguardbot.handler.factory.CommandHandlerFactory;
import com.fransua.spamguardbot.handler.factory.FilterProcessorFactory;
import com.fransua.spamguardbot.handler.processor.ProfanitySpamProcessor;
import com.fransua.spamguardbot.processor.ThreadedUpdateProcessor;
import com.fransua.spamguardbot.service.BotConfigService;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

// TODO: refactor
public class ProcessorFactory {

  private ProcessorFactory() {
  }

  public static ThreadedUpdateProcessor createProcessor() {
    return createProcessor(16, BotConfig.SpamGuardBot_TOKEN);
  }

  public static ThreadedUpdateProcessor createProcessor(int nThreads, String SpamGuardBot_TOKEN) {
    ExecutorService executorService = createExecutorService(nThreads);
    TelegramClient telegramClient = createTelegramClient(SpamGuardBot_TOKEN);
    BotConfigService configService = new BotConfigService();
    List<UpdateHandler> updateHandlerList = createUpdateHandlerList(telegramClient, configService);
    return new ThreadedUpdateProcessor(executorService, telegramClient, updateHandlerList);
  }

  private static ExecutorService createExecutorService(int nThreads) {
    return Executors.newFixedThreadPool(nThreads);
  }

  private static TelegramClient createTelegramClient(String SpamGuardBot_TOKEN) {
    return new OkHttpTelegramClient(SpamGuardBot_TOKEN);
  }

  private static List<UpdateHandler> createUpdateHandlerList(TelegramClient telegramClient,
      BotConfigService configService) {
    List<UpdateHandler> handlerList = new ArrayList<>();
    handlerList.addAll(ProcessorFactory.initializeCommandHandlers(telegramClient, configService));
    handlerList.addAll(ProcessorFactory.initializeSpamTextHandlers(telegramClient, configService));
    handlerList.addAll(
        ProcessorFactory.initializeCallbackQueryHandlers(telegramClient, configService));
    handlerList = handlerList.stream()
        .sorted(Comparator.comparingInt(UpdateHandler::getPriority))
        .collect(Collectors.toList());
    return handlerList;
  }

  private static List<UpdateHandler> initializeCommandHandlers(TelegramClient telegramClient,
      BotConfigService configService) {
    List<UpdateHandler> handlerList = new ArrayList<>();
    // StartCommandHandler
    Handler startCommandHandler = CommandHandlerFactory.createStartCommandHandler(telegramClient,
        configService);

    handlerList.add(new UpdateHandler() {

      @Override
      public boolean canHandle(Update update) {
        return startCommandHandler.canHandle(update);
      }

      @Override
      public void handle(TelegramClient telegramClient, Update update) throws Exception {
        startCommandHandler.process(update);
      }
    });

    // ReportCommandHandler
    Handler reportCommandHandler = CommandHandlerFactory.createReportCommandHandler(telegramClient,
        configService);
    handlerList.add(new UpdateHandler() {

      @Override
      public boolean canHandle(Update update) {
        return reportCommandHandler.canHandle(update);
      }

      @Override
      public void handle(TelegramClient telegramClient, Update update) throws Exception {
        reportCommandHandler.process(update);
      }
    });

    // SetLogChannelCommandHandler
    Handler setLogChannelCommandHandler = CommandHandlerFactory.createSetLogChannelCommandHandler(
        telegramClient,
        configService);
    handlerList.add(new UpdateHandler() {

      @Override
      public boolean canHandle(Update update) {
        return setLogChannelCommandHandler.canHandle(update);
      }

      @Override
      public void handle(TelegramClient telegramClient, Update update) throws Exception {
        setLogChannelCommandHandler.process(update);
      }
    });

    // SetChatCommandHandler
    Handler setChatCommandHandler = CommandHandlerFactory.createSetChatCommandHandler(
        telegramClient,
        configService);
    handlerList.add(new UpdateHandler() {

      @Override
      public boolean canHandle(Update update) {
        return setChatCommandHandler.canHandle(update);
      }

      @Override
      public void handle(TelegramClient telegramClient, Update update) throws Exception {
        setChatCommandHandler.process(update);
      }
    });
    return handlerList;
  }

  private static List<UpdateHandler> initializeSpamTextHandlers(TelegramClient telegramClient,
      BotConfigService configService) {
    List<UpdateHandler> handlerList = new ArrayList<>();
    // ProfanitySpamHandler
    Handler profanitySpamHandler = FilterProcessorFactory.createProfanitySpamHandler(telegramClient,
        configService);

    Processor profanitySpamProcessor = new ProfanitySpamProcessor(telegramClient, configService);
    handlerList.add(new UpdateHandler() {

      @Override
      public boolean canHandle(Update update) {
        return profanitySpamHandler.canHandle(update);
      }

      @Override
      public void handle(TelegramClient telegramClient, Update update) throws Exception {
        profanitySpamProcessor.process(update);
      }
    });
    return handlerList;
  }

  private static List<UpdateHandler> initializeCallbackQueryHandlers(TelegramClient telegramClient,
      BotConfigService configService) {
    List<UpdateHandler> handlerList = new ArrayList<>();
    // DeleteMessageCallbackHandler
    Handler deleteMessageQueryHandler = FilterProcessorFactory.createDeleteMessageQueryHandler(
        telegramClient,
        configService);
    handlerList.add(new UpdateHandler() {

      @Override
      public boolean canHandle(Update update) {
        return deleteMessageQueryHandler.canHandle(update);
      }

      @Override
      public void handle(TelegramClient telegramClient, Update update) throws Exception {
        deleteMessageQueryHandler.process(update);
      }
    });

    // RestrictUserCallbackHandler
    Handler restrictUserCallbackHandler = FilterProcessorFactory.createRestrictUserCallbackHandler(
        telegramClient, configService);
    handlerList.add(new UpdateHandler() {

      @Override
      public boolean canHandle(Update update) {
        return restrictUserCallbackHandler.canHandle(update);
      }

      @Override
      public void handle(TelegramClient telegramClient, Update update) throws Exception {
        restrictUserCallbackHandler.process(update);
      }
    });
    return handlerList;
  }

}
