package com.fransua.spamguardbot.config;


public class BotCommands {

  public static final String COMMAND_PREFIX = "/";

  /// Start
  public static final String START_COMMAND = COMMAND_PREFIX + "start";

  /// Help
  public static final String HELP_COMMAND = COMMAND_PREFIX + "help";

  /// Report
  public static final String REPORT_COMMAND = COMMAND_PREFIX + "report";

  /// commands
  /// ban
  /// unban
  /// stats
  /// reputation/rep/info
  
  public static class Admin {
    // TODO: add prefix to properties
//    /// setPrefix
//    public static final String SET_PREFIX_COMMAND = COMMAND_PREFIX + "setPrefix";

    /// setLogChannel
    public static final String SET_LOG_CHANNEL_COMMAND = COMMAND_PREFIX + "setLogChannel";
  }
}
