package com.fransua.spamguardbot.handler.factory;

import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.handler.core.FilterProcessorHandler;
import com.fransua.spamguardbot.handler.core.Handler;
import com.fransua.spamguardbot.handler.core.Processor;
import com.fransua.spamguardbot.handler.filter.FilterChainBuilder;
import com.fransua.spamguardbot.handler.filter.OnlyAdminFilter;
import com.fransua.spamguardbot.handler.filter.OnlyCallbackQueryFilter;
import com.fransua.spamguardbot.handler.filter.OnlyNonAdminUserFilter;
import com.fransua.spamguardbot.handler.filter.OnlySuperGroupMessageFilter;
import com.fransua.spamguardbot.handler.processor.DeleteMessageQueryProcessor;
import com.fransua.spamguardbot.handler.processor.ProfanitySpamProcessor;
import com.fransua.spamguardbot.handler.processor.RestrictUserCallbackProcessor;
import com.fransua.spamguardbot.service.BotConfigService;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class FilterProcessorFactory {

  private FilterProcessorFactory() {
  }

  public static Handler createProfanitySpamHandler(TelegramClient telegramClient,
      BotConfigService configService) {
    Filter filter = new FilterChainBuilder()
        .add(new OnlySuperGroupMessageFilter())
        .add(new OnlyNonAdminUserFilter())
        .build();

    Processor processor = new ProfanitySpamProcessor(telegramClient, configService);
    return new FilterProcessorHandler(filter, processor);
  }

  public static Handler createDeleteMessageQueryHandler(TelegramClient telegramClient,
      BotConfigService configService) {
    Filter filter = new FilterChainBuilder()
        .add(new OnlySuperGroupMessageFilter())
        .add(new OnlyAdminFilter())
        .add(new OnlyCallbackQueryFilter())
        .build();

    Processor processor = new DeleteMessageQueryProcessor(telegramClient, configService);
    return new FilterProcessorHandler(filter, processor);
  }

  public static Handler createRestrictUserCallbackHandler(TelegramClient telegramClient,
      BotConfigService configService) {
    Filter filter = new FilterChainBuilder()
        .add(new OnlySuperGroupMessageFilter())
        .add(new OnlyAdminFilter())
        .add(new OnlyCallbackQueryFilter())
        .build();

    Processor processor = new RestrictUserCallbackProcessor(telegramClient, configService);
    return new FilterProcessorHandler(filter, processor);
  }
}
