//package com.fransua.spamguardbot.handler.message.photo;
//
//
//import com.fransua.spamguardbot.handler.message.BaseMessageHandler;
//import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
//import org.telegram.telegrambots.meta.api.objects.InputFile;
//import org.telegram.telegrambots.meta.api.objects.message.Message;
//import org.telegram.telegrambots.meta.generics.TelegramClient;
//
//
//public class PhotoHandler extends BaseMessageHandler {
//
//  @Override
//  protected boolean canHandleMessage(Message msg) {
//    return msg.hasPhoto() && !msg.getPhoto().isEmpty();
//  }
//
//  @Override
//  protected void handleMessage(TelegramClient telegramClient, Message msg) {
//    try {
//      telegramClient.execute(SendPhoto
//          .builder()
//          .chatId(msg.getChatId())
//          .caption(msg.getPhoto().getLast().getFileId())
//          .photo(new InputFile(msg.getPhoto().getLast().getFileId()))
//          .build());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//}
