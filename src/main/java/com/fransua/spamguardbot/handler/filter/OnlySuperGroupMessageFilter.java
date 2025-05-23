package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.util.UpdateUtils;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class OnlySuperGroupMessageFilter extends Filter {

  @Override
  public boolean canThisHandle(Update update) {
    return OnlySuperGroupMessageFilter.updateIsGroupMessage(update);
  }

  static boolean updateIsGroupMessage(Update update) {
    Optional<Message> optionalMessage = UpdateUtils.extractMessage(update);
    if (optionalMessage.isPresent()) {
      return optionalMessage.map(Message::isSuperGroupMessage).orElse(false);
    }

    Optional<Message> optionalCallbackQueryMessage = UpdateUtils.extractCallbackQueryMessage(
        update);
    if (optionalCallbackQueryMessage.isPresent()) {
      return optionalCallbackQueryMessage.map(Message::isSuperGroupMessage).orElse(false);
    }
    return false;
  }
}
