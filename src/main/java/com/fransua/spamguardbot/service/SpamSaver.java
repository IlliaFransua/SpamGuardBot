package com.fransua.spamguardbot.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// TODO: refactor
public abstract class SpamSaver {

  private static final HttpClient httpClient = HttpClient.newHttpClient();

  private static final String URL = "http://127.0.0.1:5000/save_as_spam";

  public static boolean save(String text) {
    if (text == null) {
      throw new IllegalArgumentException("Text can not be null");
    }
    if (text.isBlank()) {
      throw new IllegalArgumentException("Text can not be empty");
    }
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("text", text);
    String body = jsonObject.toString();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build();

    HttpResponse<String> response = null;
    try {
      response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());
    } catch (Exception exception) {
      exception.printStackTrace();
    }

    String resBody = response.body();
    JsonObject resJsonObject = JsonParser.parseString(resBody).getAsJsonObject();

    return resJsonObject.get("result").getAsBoolean();
  }
}
