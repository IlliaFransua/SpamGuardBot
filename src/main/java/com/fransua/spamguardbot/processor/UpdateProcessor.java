package com.fransua.spamguardbot.processor;


import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


public interface UpdateProcessor {

  void process(List<Update> updateList);

  void process(Update update);
}
