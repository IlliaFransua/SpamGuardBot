package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.util.UpdateUtils;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class OnlyAdminFilter extends Filter {

  @Override
  public boolean canHandle(Update update) {
    return OnlyAdminFilter.isUserAdmin(update) && canNextHandle(update);
  }

  static boolean isUserAdmin(Update update) {
    Optional<User> userOptional = UpdateUtils.extractUser(update);
    if (userOptional.isEmpty()) {
      return false;
    }

    long userId = userOptional.get().getId();
    return BotConfig.isUserAdmin(userId);
  }
}
