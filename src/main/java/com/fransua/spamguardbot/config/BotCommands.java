package com.fransua.spamguardbot.config;

public class BotCommands {

  public static final String COMMAND_PREFIX = "/";

  /// Start
  public static final String START_COMMAND = COMMAND_PREFIX + "start";
  public static final String START_COMMAND_WITHOUT_PREFIX = "start";

  /// Help
  public static final String HELP_COMMAND = COMMAND_PREFIX + "help";
  public static final String HELP_COMMAND_WITHOUT_PREFIX = "help";

  /// Report
  public static final String REPORT_COMMAND = COMMAND_PREFIX + "report";
  public static final String REPORT_COMMAND_WITHOUT_PREFIX = "report";

  public static class Admin {
    // TODO: add prefix to properties
//    /// setPrefix
//    public static final String SET_PREFIX_COMMAND = COMMAND_PREFIX + "setPrefix";

    /// setLogChannel
    public static final String SET_LOG_CHANNEL_COMMAND = COMMAND_PREFIX + "setLogChannel";
    public static final String SET_LOG_CHANNEL_COMMAND_WITHOUT_PREFIX = "setLogChannel";

    /// setChatCommand
    public static final String SET_CHAT_COMMAND = COMMAND_PREFIX + "setChat";
    public static final String SET_CHAT_COMMAND_WITHOUT_PREFIX = "setChat";

  }
}
