package com.fransua.spamguardbot.config;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class BotConfig {

  public static final String SpamGuardBot_TOKEN = System.getenv("SpamGuardBot_TOKEN");

  public static final long SpamGuardBot_ID = Long.parseLong(System.getenv("SpamGuardBot_ID"));

  public static final String SpamGuardBot_USERNAME = System.getenv("SpamGuardBot_USERNAME");

  public static final long[] adminTeam = {6794636124L, 456L};

  public static boolean isUserAdmin(long userId) {
    return LongStream.of(adminTeam).anyMatch(adminId -> adminId == userId);
  }

  private static long logChannelId = Long.parseLong(System.getenv("LOG_CHANNEL_ID"));

  public static void setLogChannelId(long logChannelId) {
    BotConfig.logChannelId = logChannelId;
  }

  public static long getLogChannelId() {
    return BotConfig.logChannelId;
  }

  private static String getCorrectlyTextForDeleteMessageButton(boolean isDeleted) {
    return isDeleted
        ? "✅ Повідомлення порушника видалено"
        : "🗑 Видалити повідомлення порушника";
  }

  private static String getCorrectlyTextForBanReporterButton(boolean isReporterBanned) {
    return isReporterBanned
        ? "🚫 Заявник заблокований"
        : "⛔ Заблокувати заявника";
  }

  private static String getCorrectlyTextForMuteReporterButton(boolean isReporterMuted) {
    return isReporterMuted
        ? "🔕 Заявник зам'ючен"
        : "🔇 Зам'ютити заявника в чаті на 3 години";
  }

  private static final String MESSAGE_DELETED = "message_deleted";

  private static final String DELETE_MESSAGE = "delete_message";

  private static final String REPORTER_BANNED = "reporter_banned";

  private static final String BAN_REPORTER = "ban_reporter";

  private static final String REPORTER_MUTED = "reporter_muted";

  private static final String MUTE_REPORTER = "mute_reporter";

  public static String getDeleteMessageCallbackData(boolean isDeleted) {
    return isDeleted
        ? MESSAGE_DELETED
        : DELETE_MESSAGE;
  }

  public static String getBanReporterCallbackData(boolean isReporterBanned) {
    return isReporterBanned
        ? REPORTER_BANNED
        : BAN_REPORTER;
  }

  public static String getMuteReporterCallbackData(boolean isReporterMuted) {
    return isReporterMuted
        ? REPORTER_MUTED
        : MUTE_REPORTER;
  }

  public static List<String> getDeleteMessageCallbackDataList() {
    List<String> callbackDataList = new ArrayList<>();
    callbackDataList.add(MESSAGE_DELETED);
    callbackDataList.add(DELETE_MESSAGE);
    return callbackDataList;
  }

  public static List<String> getBanReporterCallbackDataList() {
    List<String> callbackDataList = new ArrayList<>();
    callbackDataList.add(REPORTER_BANNED);
    callbackDataList.add(BAN_REPORTER);
    return callbackDataList;
  }

  public static List<String> getMuteReporterCallbackDataList() {
    List<String> callbackDataList = new ArrayList<>();
    callbackDataList.add(REPORTER_MUTED);
    callbackDataList.add(MUTE_REPORTER);
    return callbackDataList;
  }

  public static List<String> getCallbackDataList() {
    List<String> callbackDataList = new ArrayList<>();
    callbackDataList.addAll(getDeleteMessageCallbackDataList());
    callbackDataList.addAll(getBanReporterCallbackDataList());
    callbackDataList.addAll(getMuteReporterCallbackDataList());
    return callbackDataList;
  }

  public static InlineKeyboardMarkup createInlineKeyboardMarkup(
      boolean isDeleted,
      boolean isReporterBanned,
      boolean isReporterMuted) {
    return buildInlineKeyboardMarkup(
        BotConfig.buildDeleteMessageButton(isDeleted),
        BotConfig.buildBanReporterButton(isReporterBanned),
        BotConfig.buildMuteReporterInChatButton(isReporterMuted));
  }

  private static InlineKeyboardButton buildDeleteMessageButton(boolean isDeleted) {
    return InlineKeyboardButton.builder()
        .text(getCorrectlyTextForDeleteMessageButton(isDeleted))
        .callbackData(BotConfig.getDeleteMessageCallbackData(isDeleted))
        .build();
  }

  private static InlineKeyboardButton buildBanReporterButton(boolean isReporterBanned) {
    return InlineKeyboardButton.builder()
        .text(getCorrectlyTextForBanReporterButton(isReporterBanned))
        .callbackData(BotConfig.getBanReporterCallbackData(isReporterBanned))
        .build();
  }

  private static InlineKeyboardButton buildMuteReporterInChatButton(boolean isReporterMuted) {
    return InlineKeyboardButton.builder()
        .text(getCorrectlyTextForMuteReporterButton(isReporterMuted))
        .callbackData(BotConfig.getMuteReporterCallbackData(isReporterMuted))
        .build();
  }

  private static InlineKeyboardMarkup buildInlineKeyboardMarkup(
      InlineKeyboardButton deleteMessageButton,
      InlineKeyboardButton banReporterButton,
      InlineKeyboardButton muteReporterInChatButton) {
    List<InlineKeyboardRow> keyboard = new ArrayList<>();
    keyboard.add(new InlineKeyboardRow(deleteMessageButton));
    // TODO: create banReportButton
    // keyboard.add(new InlineKeyboardRow(banReporterButton));
    keyboard.add(new InlineKeyboardRow(muteReporterInChatButton));
    return InlineKeyboardMarkup
        .builder()
        .keyboard(keyboard)
        .build();
  }

  public static class Commands {

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
}
