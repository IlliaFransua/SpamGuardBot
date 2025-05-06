package com.fransua.spamguardbot.config;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;


public class BotConfig {

  public static final String SpamGuardBot_TOKEN = System.getenv("SpamGuardBot_TOKEN");

  public static final long SpamGuardBot_ID = Long.parseLong(System.getenv("SpamGuardBot_ID"));

  public static final String SpamGuardBot_USERNAME = System.getenv("SpamGuardBot_USERNAME");

  public static final long[] adminTeam = {6794636124L, 456};

  private static long logChannelId = Long.parseLong(System.getenv("LOG_CHANNEL_ID"));

  public static void setLogChannelId(long logChannelId) {
    BotConfig.logChannelId = logChannelId;
  }

  public static long getLogChannelId() {
    return BotConfig.logChannelId;
  }

  private static String getCorrectlyTextForDeleteMessageButton(boolean isDeleted) {
    return isDeleted
        ? "✅ Сообщение нарушителя удалено"
        : "🗑 Удалить сообщение нарушителя";
  }

  private static String getCorrectlyTextForBanReporterButton(boolean isReporterBanned) {
    return isReporterBanned
        ? "🚫 Заявитель заблокирован"
        : "⛔ Заблокировать заявителя";
  }

  private static String getCorrectlyTextForMuteReporterButton(boolean isReporterMuted) {
    return isReporterMuted
        ? "🔕 Заявитель замьючен"
        : "🔇 Замьютить заявителя в чате на 3 часа";
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
    List<String> deleteMessageCallbackDataList = new ArrayList<>();
    deleteMessageCallbackDataList.add(MESSAGE_DELETED);
    deleteMessageCallbackDataList.add(DELETE_MESSAGE);
    return deleteMessageCallbackDataList;
  }

  public static List<String> getBanReporterCallbackDataList() {
    List<String> deleteMessageCallbackDataList = new ArrayList<>();
    deleteMessageCallbackDataList.add(REPORTER_BANNED);
    deleteMessageCallbackDataList.add(BAN_REPORTER);
    return deleteMessageCallbackDataList;
  }

  public static List<String> getMuteReporterCallbackDataList() {
    List<String> deleteMessageCallbackDataList = new ArrayList<>();
    deleteMessageCallbackDataList.add(REPORTER_MUTED);
    deleteMessageCallbackDataList.add(MUTE_REPORTER);
    return deleteMessageCallbackDataList;
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
      boolean isReporterMuted
  ) {
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
      InlineKeyboardButton muteReporterInChatButton
  ) {
    List<InlineKeyboardRow> keyboard = new ArrayList<>();
    keyboard.add(new InlineKeyboardRow(deleteMessageButton));
//    keyboard.add(new InlineKeyboardRow(banReporterButton));
//    keyboard.add(new InlineKeyboardRow(muteReporterInChatButton));
    return InlineKeyboardMarkup
        .builder()
        .keyboard(keyboard)
        .build();
  }
}
