package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.handler.core.Filter;
import org.telegram.telegrambots.meta.api.objects.Update;

public class OnlyNonAdminUserFilter extends Filter {

  @Override
  public boolean canThisHandle(Update update) {
    return OnlyNonAdminUserFilter.isNotAdmin(update);
  }

  static boolean isNotAdmin(Update update) {
    return !OnlyAdminFilter.isUserAdmin(update);
  }
}
