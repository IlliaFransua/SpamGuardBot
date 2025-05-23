package com.fransua.spamguardbot.handler.filter;

import com.fransua.spamguardbot.handler.core.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Update;

public class FilterChainBuilder {

  private final List<Filter> filterList = new ArrayList<>();

  public FilterChainBuilder add(Filter filter) {
    filterList.add(filter);
    return this;
  }

  public Filter build() {
    if (filterList.isEmpty()) {
      return new Filter() {
        @Override
        public boolean canThisHandle(Update update) {
          return true;
        }
      };
    }
    if (filterList.size() == 1) {
      return filterList.getFirst();
    }
    for (int i = 0; i < filterList.size() - 1; ++i) {
      filterList.get(i).setNextFilter(filterList.get(i + 1));
    }
    return filterList.getFirst();
  }
}