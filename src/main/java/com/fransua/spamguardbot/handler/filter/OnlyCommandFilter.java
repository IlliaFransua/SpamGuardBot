package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.handler.core.Filter;
import com.fransua.spamguardbot.util.UpdateUtils;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class OnlyCommandFilter extends Filter {

  private final String expectedCommand;

  public OnlyCommandFilter(String commandPrefix, String commandName) {
    this.expectedCommand = commandPrefix + commandName;
  }

  @Override
  public boolean canHandle(Update update) {
    return OnlyCommandFilter.updateHasCommand(update, expectedCommand)
        && canNextHandle(update);
  }

  static boolean updateHasCommand(Update update, String command) {
    Optional<Message> optionalMessage = UpdateUtils.extractMessage(update);
    return optionalMessage.filter(message ->
        UpdateUtils.extractAnyTextFromMessage(message)
            .orElse("")
            .startsWith(command)).isPresent();
  }
}
