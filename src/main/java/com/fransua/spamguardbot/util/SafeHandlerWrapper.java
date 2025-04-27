//package com.fransua.spamguardbot.util;
//
//
//import com.fransua.spamguardbot.handler.UpdateHandler;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.generics.TelegramClient;
//
//
//public class SafeHandlerWrapper {
//
//  public void safeHandle(UpdateHandler handler, TelegramClient telegramClient, Update update) {
//    try {
//      UpdateContext.setParsedUpdate(new ParsedUpdate(update));
//      handler.handle(telegramClient, update);
//    } catch (Exception e) {
//      System.out.println(
//          "Handler error: " +
//              handler.getClass().getSimpleName() +
//              ": " + e.getMessage() + ":\n" + e);
//    } finally {
//      UpdateContext.clear();
//    }
//  }
//}
