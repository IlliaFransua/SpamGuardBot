package com.fransua.spamguardbot.handler.factory;

import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.handler.core.FilterProcessorHandler;
import com.fransua.spamguardbot.handler.core.Handler;
import com.fransua.spamguardbot.handler.core.Processor;
import com.fransua.spamguardbot.handler.filter.FilterChainBuilder;
import com.fransua.spamguardbot.handler.filter.NotFilter;
import com.fransua.spamguardbot.handler.filter.OnlyAdminFilter;
import com.fransua.spamguardbot.handler.filter.OnlyCallbackQueryFilter;
import com.fransua.spamguardbot.handler.filter.OnlySuperGroupMessageFilter;
import com.fransua.spamguardbot.handler.filter.OnlyChatIdFilter;
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
    Filter filter = Filter.builder()
        .add(new NotFilter(new OnlyAdminFilter(telegramClient)))
        .add(new OnlyChatIdFilter(configService.getChatId()))
        .build();

    Processor processor = new ProfanitySpamProcessor(telegramClient, configService);
    return new FilterProcessorHandler(filter, processor);
  }

  public static Handler createDeleteMessageQueryHandler(TelegramClient telegramClient,
      BotConfigService configService) {
    Filter filter = Filter.builder()
        .add(new OnlyCallbackQueryFilter())
        .add(new OnlyAdminFilter(telegramClient))
        .add(new OnlyChatIdFilter(configService.getLogChannelId()))
        .build();

    Processor processor = new DeleteMessageQueryProcessor(telegramClient, configService);
    return new FilterProcessorHandler(filter, processor);
  }

  public static Handler createRestrictUserCallbackHandler(TelegramClient telegramClient,
      BotConfigService configService) {
    Filter filter = Filter.builder()
        .add(new OnlyCallbackQueryFilter())
        .add(new OnlyAdminFilter(telegramClient))
        .add(new OnlyChatIdFilter(configService.getLogChannelId()))
        .build();

    Processor processor = new RestrictUserCallbackProcessor(telegramClient, configService);
    return new FilterProcessorHandler(filter, processor);
  }
}
