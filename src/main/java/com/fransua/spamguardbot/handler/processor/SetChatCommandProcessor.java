package com.fransua.spamguardbot.handler.processor;

import com.fransua.spamguardbot.config.BotCommands;
import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.core.Processor;
import com.fransua.spamguardbot.service.BotConfigService;
import com.fransua.spamguardbot.util.UpdateContext;
import com.fransua.spamguardbot.util.UpdateUtils;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

// TODO: refactor
public class SetChatCommandProcessor implements Processor {

  private final TelegramClient telegramClient;
  private final BotConfigService botConfigService;

  public SetChatCommandProcessor(TelegramClient telegramClient,
      BotConfigService botConfigService) {
    this.telegramClient = telegramClient;
    this.botConfigService = botConfigService;
  }

  @Override
  public void process(Update update) {
    Optional<Message> optionalMessage = UpdateUtils.extractMessage(update);
    if (optionalMessage.isEmpty()) {
      return;
    }
    Message msg = optionalMessage.get();
    Optional<String> optionalText = UpdateUtils.extractAnyTextFromMessage(msg);
    if (optionalText.isEmpty()) {
      return;
    }
    String text = optionalText.get();
    if (text.startsWith(BotCommands.Admin.SET_CHAT_COMMAND)) {
      long chatId = msg.getChatId();
      int messageId = msg.getMessageId();

      GetChatMember getChatMember = GetChatMember
          .builder()
          .chatId(chatId)
          .userId(BotConfig.SpamGuardBot_ID)
          .build();
      ChatMember member = null;
      try {
        member = telegramClient.execute(getChatMember);
      } catch (Exception e) {
        e.printStackTrace();
      }
      String answer;
      if (member instanceof ChatMemberAdministrator admin) {
        answer = String.format("""
                ✅ *Бот — АДМІНІСТРАТОР. Права:*
                🔧 *Зміна інформації про чат:* %s
                🗑 *Видалення повідомлень:* %s
                🗄 *Видалення історій:* %s
                ✏️ *Редагування повідомлень інших:* %s
                📝 *Редагування історій:* %s
                👥 *Запрошення користувачів:* %s
                ⚙️ *Управління чатом:* %s
                📹 *Управління відеочатами:* %s
                📌 *Закріплення повідомлень:* %s
                📢 *Постинг у каналі:* %s
                📖 *Постинг історій:* %s
                🚀 *Просування учасників:* %s
                🔒 *Обмеження користувачів:* %s
                🔑 *Редагування прав адміністратора:* %s
                🗂 *Управління темами:* %s""",
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
        answer = "Підвищте мій рівень ролі до адміністратора. Такі вимоги Telegram, щоб у мене була можливість видаляти текстовий спам і обмежувати користувачів.";
      }

      try {
        botConfigService.setChatId(chatId);

        telegramClient.execute(DeleteMessage
            .builder()
            .chatId(chatId)
            .messageId(messageId)
            .build());

        telegramClient.execute(SendMessage
            .builder()
            .chatId(chatId)
            .text("Тепер цей чат буде фільтруватися на спам.")
            .build());
      } catch (Exception e) {
        e.printStackTrace();
        answer = "Через помилку відсутності ID чату, в якому потрібно фільтрувати спам – не вдалося надіслати репорт. Встановіть чат, в якому буде фільтрація спаму.";
        try {
          telegramClient.execute(SendMessage
              .builder()
              .chatId(chatId)
              .text(answer)
              .parseMode("Markdown")
              .build());
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }
}
