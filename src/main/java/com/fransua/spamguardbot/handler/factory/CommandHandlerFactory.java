package com.fransua.spamguardbot.handler.factory;

import com.fransua.spamguardbot.config.BotCommands;
import com.fransua.spamguardbot.config.BotCommands.Admin;
import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.handler.core.FilterProcessorHandler;
import com.fransua.spamguardbot.handler.core.Handler;
import com.fransua.spamguardbot.handler.core.Processor;
import com.fransua.spamguardbot.handler.filter.FilterChainBuilder;
import com.fransua.spamguardbot.handler.filter.OnlyAdminFilter;
import com.fransua.spamguardbot.handler.filter.OnlyCommandFilter;
import com.fransua.spamguardbot.handler.filter.OnlyHumanSenderFilter;
import com.fransua.spamguardbot.handler.filter.OnlyRepliedMessage;
import com.fransua.spamguardbot.handler.filter.OnlySuperGroupMessageFilter;
import com.fransua.spamguardbot.handler.filter.OnlyChatIdFilter;
import com.fransua.spamguardbot.handler.processor.ReportCommandProcessor;
import com.fransua.spamguardbot.handler.processor.SetChatCommandProcessor;
import com.fransua.spamguardbot.handler.processor.SetLogChannelCommandProcessor;
import com.fransua.spamguardbot.handler.processor.StartCommandProcessor;
import com.fransua.spamguardbot.service.BotConfigService;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class CommandHandlerFactory {

  private static final String commandPrefix = BotCommands.COMMAND_PREFIX;

  private CommandHandlerFactory() {
  }

  public static Handler createStartCommandHandler(TelegramClient telegramClient,
      BotConfigService configService) {
    Filter filterChain = Filter.builder()
        .add(new OnlySuperGroupMessageFilter())
        .add(new OnlyCommandFilter(commandPrefix, BotCommands.START_COMMAND_WITHOUT_PREFIX))
        .add(new OnlyChatIdFilter(configService.getChatId()))
        .build();

    Processor processor = new StartCommandProcessor(telegramClient, configService);
    return new FilterProcessorHandler(filterChain, processor);
  }

  public static Handler createReportCommandHandler(TelegramClient telegramClient,
      BotConfigService configService) {
    Filter filterChain = Filter.builder()
        .add(new OnlySuperGroupMessageFilter())
        .add(new OnlyHumanSenderFilter())
        .add(new OnlyRepliedMessage())
        .add(new OnlyCommandFilter(commandPrefix, BotCommands.REPORT_COMMAND_WITHOUT_PREFIX))
        .add(new OnlyChatIdFilter(configService.getChatId()))
        .build();

    Processor processor = new ReportCommandProcessor(telegramClient, configService);
    return new FilterProcessorHandler(filterChain, processor);
  }

  public static Handler createSetLogChannelCommandHandler(TelegramClient telegramClient,
      BotConfigService configService) {
    Filter filterChain = Filter.builder()
        .add(new OnlySuperGroupMessageFilter())
        .add(new OnlyAdminFilter())
        .add(new OnlyCommandFilter(commandPrefix, Admin.SET_LOG_CHANNEL_COMMAND_WITHOUT_PREFIX))
        .build();

    Processor processor = new SetLogChannelCommandProcessor(telegramClient, configService);
    return new FilterProcessorHandler(filterChain, processor);
  }

  public static Handler createSetChatCommandHandler(TelegramClient telegramClient,
      BotConfigService configService) {
    Filter filterChain = Filter.builder()
        .add(new OnlySuperGroupMessageFilter())
        .add(new OnlyAdminFilter())
        .add(new OnlyCommandFilter(commandPrefix, Admin.SET_CHAT_COMMAND_WITHOUT_PREFIX))
        .build();

    Processor processor = new SetChatCommandProcessor(telegramClient, configService);
    return new FilterProcessorHandler(filterChain, processor);
  }
}
