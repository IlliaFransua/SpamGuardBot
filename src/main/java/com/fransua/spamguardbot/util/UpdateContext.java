package com.fransua.spamguardbot.util;

public class UpdateContext {

  private static final ThreadLocal<ParsedUpdate> currentUpdate = new ThreadLocal<>();

  public static void setParsedUpdate(ParsedUpdate parsedUpdate) {
    currentUpdate.set(parsedUpdate);
  }

  public static ParsedUpdate getParsedUpdate() {
    ParsedUpdate parsedUpdate = currentUpdate.get();
    if (parsedUpdate == null) {
      throw new IllegalStateException("No parsed update available for this thread.");
    }
    return currentUpdate.get();
  }

  public static void clear() {
    currentUpdate.remove();
  }
}
