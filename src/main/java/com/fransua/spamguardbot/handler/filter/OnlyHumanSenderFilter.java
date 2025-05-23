package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.util.UpdateUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class OnlyHumanSenderFilter extends Filter {

  @Override
  public boolean canHandle(Update update) {
    return isSenderHuman(update) && canNextHandle(update);
  }

  private boolean isSenderHuman(Update update) {
    Message message = UpdateUtils.extractMessage(update).orElse(null);
    if (message == null || message.getFrom() == null) {
      return false;
    }
    User sender = message.getFrom();
    return !sender.getIsBot();
  }
}
