package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.util.UpdateUtils;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public class OnlyCallbackQueryFilter extends Filter {

  @Override
  public boolean canThisHandle(Update update) {
    return OnlyCallbackQueryFilter.callbackQueryHasValidMessage(update)
        && OnlyCallbackQueryFilter.callbackQueryHasValidData(update);
  }

  static boolean callbackQueryHasValidMessage(Update update) {
    return UpdateUtils.extractCallbackQueryMessage(update).isPresent();
  }

  static boolean callbackQueryHasValidData(Update update) {
    CallbackQuery callbackQuery = update.getCallbackQuery();
    if (callbackQuery == null || callbackQuery.getData() == null) {
      return false;
    }
    String callbackQueryData = callbackQuery.getData();
    return BotConfig.getCallbackDataList().stream()
        .anyMatch(callbackData -> callbackData.equals(callbackQueryData));
  }
}
