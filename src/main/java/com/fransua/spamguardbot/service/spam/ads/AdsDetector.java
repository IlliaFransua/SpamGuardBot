package com.fransua.spamguardbot.service.spam.ads;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fransua.spamguardbot.service.spam.SpamDetectionResult;
import com.fransua.spamguardbot.service.spam.SpamDetector;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class AdsDetector implements SpamDetector {

  private static final HttpClient httpClient = HttpClient.newHttpClient();

  private final ObjectMapper objectMapper = new ObjectMapper();

  private static final String url = "http://localhost:8000/is_spam";

  @Override
  public boolean isSpam(String text) throws Exception {
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
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    String resultHeader = response.headers().firstValue("SpamResult").orElse("false");
    return Boolean.parseBoolean(resultHeader);
  }

  @Override
  public SpamDetectionResult checkSpam(String text) throws Exception {
    return null;
  }
}
