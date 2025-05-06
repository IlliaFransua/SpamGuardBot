package com.fransua.spamguardbot.handler.message.text.command.admin;


import com.fransua.spamguardbot.config.BotCommands;
import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.HandlerProcessingStatus;
import com.fransua.spamguardbot.handler.message.text.BaseTextHandler;
import com.fransua.spamguardbot.service.BotConfigService;
import com.fransua.spamguardbot.util.ParsedUpdate;
import com.fransua.spamguardbot.util.UpdateContext;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.*;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;


// TODO: refactor
public class SetLogChannelCommandHandler extends BaseTextHandler {

  @Override
  public boolean canHandle(Update update) {
    switch (handlerProcessingStatus.get()) {
      case HandlerProcessingStatus.CAN_BE_HANDLED -> {
        return true;
      }
      case HandlerProcessingStatus.CAN_NOT_BE_HANDLED -> {
        return false;
      }
    }
    if (!super.canHandle(update)) {
      return false;
    }
    ParsedUpdate parsedUpdate = UpdateContext.getParsedUpdate();
    String text = parsedUpdate.getAnyTextFromMessage();
    if (text.equalsIgnoreCase(BotCommands.Admin.SET_LOG_CHANNEL_COMMAND) &&
        parsedUpdate.isAdminMessage()) {
      handlerProcessingStatus.set(HandlerProcessingStatus.CAN_BE_HANDLED);
      return true;
    }
    handlerProcessingStatus.set(HandlerProcessingStatus.CAN_NOT_BE_HANDLED);
    return false;
  }

  @Override
  public int getPriority() {
    return 10;
  }

  @Override
  public void handle(TelegramClient telegramClient, Update update) throws Exception {
    if (!canHandle(update)) {
      throw new IllegalArgumentException("Can not handle the provided update.");
    }
    Message msg = UpdateContext.getParsedUpdate().getMessage();
    String text = UpdateContext.getParsedUpdate().getAnyTextFromMessage();
    if (text.startsWith(BotCommands.Admin.SET_LOG_CHANNEL_COMMAND)) {
//      text = text.substring(BotCommands.Admin.SET_LOG_CHANNEL_COMMAND.length());

//        long newLogChannelId = Long.parseLong(text.strip());

      long chatId = msg.getChatId();
      int messageId = msg.getMessageId();

      GetChatMember getChatMember = GetChatMember
          .builder()
          .chatId(chatId)
          .userId(BotConfig.SpamGuardBot_ID)
          .build();
      ChatMember member = telegramClient.execute(getChatMember);
      String answer;
      if (member instanceof ChatMemberAdministrator admin) {
        answer = String.format("""
                ✅ *Бот — АДМИНИСТРАТОР. Права:*
                🔧 *Изменение информации о чате:* %s
                🗑 *Удаление сообщений:* %s
                🗄 *Удаление историй:* %s
                ✏️ *Редактирование сообщений других:* %s
                📝 *Редактирование историй:* %s
                👥 *Приглашение пользователей:* %s
                ⚙️ *Управление чатом:* %s
                📹 *Управление видео чатами:* %s
                📌 *Закрепление сообщений:* %s
                📢 *Постинг в канале:* %s
                📖 *Постинг историй:* %s
                🚀 *Продвижение участников:* %s
                🔒 *Ограничение пользователей:* %s
                🔑 *Редактирование прав администратора:* %s
                🗂 *Управление темами:* %s""",
            admin.getCanChangeInfo(),
            admin.getCanDeleteMessages(),
            admin.getCanDeleteStories(),
            admin.getCanEditMessages(),
            admin.getCanEditStories(),
            admin.getCanInviteUsers(),
            admin.getCanManageChat(),
            admin.getCanManageVideoChats(),
            admin.getCanPinMessages(),
            admin.getCanPostMessages(),
            admin.getCanPostStories(),
            admin.getCanPromoteMembers(),
            admin.getCanRestrictMembers(),
            admin.getCanBeEdited(),
            admin.getCanManageTopics());
      } else {
        answer = "Повысте мою уровень уроли до администратора. Такие требования телеграма чтобы у меня была возможность удалять текстовый спам.";
      }

//        telegramClient.execute(ForwardMessage
//            .builder()
//            .chatId(newLogChannelId)
//            .fromChatId(fromChatId)
//            .messageId(messageId)
//            .disableNotification(true)
//            .protectContent(true)
//            .build());

      try {
        BotConfigService botConfigService = new BotConfigService();
        botConfigService.setLogChannelId(chatId);

        System.out.println("chatId: " + chatId);

        telegramClient.execute(DeleteMessage
            .builder()
            .chatId(chatId)
            .messageId(messageId)
            .build());

        telegramClient.execute(SendMessage
            .builder()
            .chatId(chatId)
            .text("Канал успешно установлен для логов")
            .build());
      } catch (Exception e) {
        e.printStackTrace();
        answer = "Из-за ошибки отсуствия айди чата логов – не получилось отправить репорт. Установите чат для логов.";
        telegramClient.execute(SendMessage
            .builder()
            .chatId(chatId)
            .text(answer)
            .parseMode("Markdown")
            .build());
      }
    }
  }
}
