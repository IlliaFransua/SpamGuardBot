package com.fransua.spamguardbot.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

// TODO: refactor
public class BotConfigService {

  private static final Path PROPERTIES_PATH =
      Paths.get("src", "main", "resources", "application.properties");

  private static final String LOG_CHANNEL_ID_PROPERTY_KEY = "log.channel.id";

  private static final String CHAT_ID_PROPERTY_KEY = "chat.id";

  private long logChannelId;

  private long chatId;

  public BotConfigService() {
    loadLogChannelId();
    loadChatId();
  }

  private void loadLogChannelId() {
    Properties props = new Properties();
    try (InputStream in = Files.newInputStream(PROPERTIES_PATH)) {
      props.load(in);
      String value = props.getProperty(LOG_CHANNEL_ID_PROPERTY_KEY);
      if (value != null) {
        logChannelId = Long.parseLong(value.trim());
      } else {
        throw new IllegalStateException(
            "Property " + LOG_CHANNEL_ID_PROPERTY_KEY + " is not found.");
      }
    } catch (IOException | NumberFormatException e) {
      throw new RuntimeException("Failed to load " + LOG_CHANNEL_ID_PROPERTY_KEY, e);
    }
  }

  private void loadChatId() {
    Properties props = new Properties();
    try (InputStream in = Files.newInputStream(PROPERTIES_PATH)) {
      props.load(in);
      String value = props.getProperty(CHAT_ID_PROPERTY_KEY);
      if (value != null) {
        chatId = Long.parseLong(value.trim());
      } else {
        throw new IllegalStateException("Property " + CHAT_ID_PROPERTY_KEY + " is not found.");
      }
    } catch (IOException | NumberFormatException e) {
      throw new RuntimeException("Failed to load " + CHAT_ID_PROPERTY_KEY, e);
    }
  }

  public long getLogChannelId() {
    return logChannelId;
  }

  public long getChatId() {
    return chatId;
  }

  public synchronized void setLogChannelId(long newLogChannelId) {
    this.logChannelId = newLogChannelId;
    saveLogChannelIdToProperties(newLogChannelId);
  }

  public synchronized void setChatId(long newChatId) {
    this.chatId = newChatId;
    saveChatIdToProperties(newChatId);
  }

  private void saveLogChannelIdToProperties(long newLogChannelId) {
    Properties props = new Properties();
    try (InputStream in = Files.newInputStream(PROPERTIES_PATH)) {
      props.load(in);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read properties to save", e);
    }

    props.setProperty(LOG_CHANNEL_ID_PROPERTY_KEY, Long.toString(newLogChannelId));

    try (FileOutputStream out = new FileOutputStream(PROPERTIES_PATH.toFile())) {
      props.store(out, null);
    } catch (IOException e) {
      throw new RuntimeException("Failed to save properties", e);
    }
    loadLogChannelId();
  }

  private void saveChatIdToProperties(long newChatId) {
    Properties props = new Properties();
    try (InputStream in = Files.newInputStream(PROPERTIES_PATH)) {
      props.load(in);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read properties to save", e);
    }

    props.setProperty(CHAT_ID_PROPERTY_KEY, Long.toString(newChatId));

    try (FileOutputStream out = new FileOutputStream(PROPERTIES_PATH.toFile())) {
      props.store(out, null);
    } catch (IOException e) {
      throw new RuntimeException("Failed to save properties", e);
    }
    loadChatId();
  }
}
