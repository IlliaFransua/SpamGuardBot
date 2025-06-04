package com.fransua.spamguardbot.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpamSaver {

  private static final HttpClient httpClient = HttpClient.newHttpClient();

  private static final String URL = "http://127.0.0.1:8000/save_as_spam";

  public static void save(String text) {
    if (text == null) {
      throw new IllegalArgumentException("Text can not be null");
    }
    if (text.isBlank()) {
      throw new IllegalArgumentException("Text can not be empty");
    }
    try {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("text", text);
      String body = jsonObject.toString();

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(URL))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      HttpResponse<String> response = null;

      response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());

      String resBody = response.body();
      JsonObject resJsonObject = JsonParser.parseString(resBody).getAsJsonObject();

      resJsonObject.get("result").getAsBoolean();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
