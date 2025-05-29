package com.fransua.spamguardbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// TODO: refactor
public class AdsDetectorService implements SpamDetector {

  private static final HttpClient httpClient = HttpClient.newHttpClient();

  private final ObjectMapper objectMapper = new ObjectMapper();

  private static final String URL = "http://127.0.0.1:5000/is_spam";

  @Override
  public boolean isSpam(String text) {
    try {
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

      HttpResponse<String> response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());
      String resBody = response.body();
      JsonElement jsonElement = JsonParser.parseString(resBody);
      if (jsonElement == null || !jsonElement.isJsonObject()) {
        return false;
      }
      JsonObject resJsonObject = jsonElement.getAsJsonObject();
      if (resJsonObject.isEmpty()) {
        return false;
      }
      JsonElement resultElement = resJsonObject.get("result");
      if (resultElement == null) {
        return false;
      }

      return resultElement.getAsBoolean();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public SpamDetectionResult checkSpam(String text) throws Exception {
    return null;
  }
}
