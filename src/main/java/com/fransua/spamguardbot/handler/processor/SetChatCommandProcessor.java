package com.fransua.spamguardbot.handler.processor;

import com.fransua.spamguardbot.config.BotConfig;
import com.fransua.spamguardbot.handler.core.Processor;
import com.fransua.spamguardbot.service.BotConfigService;
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

public class SetChatCommandProcessor implements Processor {

  private final TelegramClient telegramClient;
  private final BotConfigService botConfigService;

  public SetChatCommandProcessor(TelegramClient telegramClient, BotConfigService botConfigService) {
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
    if (text.startsWith(BotConfig.Commands.Admin.SET_CHAT_COMMAND)) {
      long chatId = msg.getChatId();
      int messageId = msg.getMessageId();

      GetChatMember getChatMember = GetChatMember.builder().chatId(chatId)
          .userId(BotConfig.SpamGuardBot_ID).build();
      ChatMember member = null;
      try {
        member = telegramClient.execute(getChatMember);
      } catch (Exception e) {
        e.printStackTrace();
      }
      String answer;
      if (member instanceof ChatMemberAdministrator admin) {
        answer = String.format(
            """
                ✅ *Bot is an ADMIN. Rights:*
                🔧 *Change chat info:* %s
                🗑 *Delete messages:* %s
                🗄 *Delete stories:* %s
                ✏️ *Edit other's messages:* %s
                📝 *Edit stories:* %s
                👥 *Invite users:* %s
                ⚙️ *Manage chat:* %s
                📹 *Manage video chats:* %s
                📌 *Pin messages:* %s
                📢 *Post in channel:* %s
                📖 *Post stories:* %s
                🚀 *Promote members:* %s
                🔒 *Restrict users:* %s
                🔑 *Edit admin rights:* %s
                🗂 *Manage topics:* %s\
                """,
            admin.getCanChangeInfo(),
            admin.getCanDeleteMessages(),
            admin.getCanDeleteStories(),
            admin.getCanChangeInfo(),
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
        answer =
            "Promote my role to administrator. These are Telegram's requirements for me to be able"
                + " to delete text spam and restrict users.";
      }

      try {
        botConfigService.setChatId(chatId);

        telegramClient.execute(DeleteMessage.builder().chatId(chatId).messageId(messageId).build());

        telegramClient.execute(
            SendMessage.builder()
                .chatId(chatId)
                .text("This chat will now be filtered for spam.")
                .build());
      } catch (Exception e) {
        e.printStackTrace();
        answer =
            "Due to the error of missing chat ID, the report could not be sent. Please set the chat"
                + " where spam filtering will be enabled.";

        try {
          telegramClient.execute(
              SendMessage.builder().chatId(chatId).text(answer).parseMode("Markdown").build());
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }
}
