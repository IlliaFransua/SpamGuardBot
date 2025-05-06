package com.fransua.spamguardbot.service;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;


public class BotConfigService {

  private static final Path PROPERTIES_PATH =
      Paths.get("src", "main", "resources", "application.properties");

  private static final String PROPERTY_KEY = "log.channel.id";

  private long logChannelId;

  public BotConfigService() {
    loadLogChannelId();
  }

  private void loadLogChannelId() {
    Properties props = new Properties();
    try (InputStream in = Files.newInputStream(PROPERTIES_PATH)) {
      props.load(in);
      String value = props.getProperty(PROPERTY_KEY);
      if (value != null) {
        logChannelId = Long.parseLong(value.trim());
      } else {
        throw new IllegalStateException("Свойство " + PROPERTY_KEY + " не найдено");
      }
    } catch (IOException | NumberFormatException e) {
      throw new RuntimeException("Не удалось загрузить " + PROPERTY_KEY, e);
    }
  }

  public long getLogChannelId() {
    return logChannelId;
  }

  public synchronized void setLogChannelId(long newLogChannelId) {
    this.logChannelId = newLogChannelId;
    saveLogChannelIdToProperties(newLogChannelId);
  }

  private void saveLogChannelIdToProperties(long newLogChannelId) {
    Properties props = new Properties();
    try (InputStream in = Files.newInputStream(PROPERTIES_PATH)) {
      props.load(in);
    } catch (IOException e) {
      throw new RuntimeException("Не удалось прочитать свойства для сохранения", e);
    }

    props.setProperty(PROPERTY_KEY, Long.toString(newLogChannelId));

    try (FileOutputStream out = new FileOutputStream(PROPERTIES_PATH.toFile())) {
      props.store(out, null);
    } catch (IOException e) {
      throw new RuntimeException("Не удалось сохранить свойства", e);
    }
  }
}
