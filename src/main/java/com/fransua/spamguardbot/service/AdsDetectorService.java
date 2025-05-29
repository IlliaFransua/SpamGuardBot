package com.fransua.spamguardbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

// TODO: refactor
public class AdsDetectorService implements SpamDetector {

  private static final HttpClient httpClient = HttpClient.newHttpClient();

  private final ObjectMapper objectMapper = new ObjectMapper();

  private static final String URL = "http://127.0.0.1:8000/is_spam";

  @Override
  public boolean isSpam(String text) {
    Objects.requireNonNull(text, "Input text for spam detection can't be null");
    if (text.isBlank()) {
      throw new IllegalArgumentException("Input text for spam detection can't be empty");
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

      HttpResponse<String> response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        throw new IllegalArgumentException("Spam detection API returned not 200 status code.");
      }
      String resBody = response.body();
      if (resBody == null || resBody.trim().isEmpty()) {
        throw new IllegalArgumentException(
            "Spam detection API returned null or empty response body.");
      }
      JsonElement jsonElement = getJsonElement(resBody);

      return jsonElement.getAsBoolean();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      System.out.println("Invalid API response structure or data for text: " + text);
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Network error during spam detection API call for text: " + text);
      return false;
    } catch (InterruptedException e) {
      e.printStackTrace();
      System.out.println("Spam detection API call was interrupted for text: " + text);
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(
          "An unexpected exception occurred during spam detection for text: " + text);
      return false;
    }
  }

  @NotNull
  private static JsonElement getJsonElement(String resBody) {
    JsonElement jsonElement;
    try {
      jsonElement = JsonParser.parseString(resBody);
    } catch (JsonSyntaxException e) {
      throw new IllegalArgumentException(
          "Failed to parse JSON body from spam detection response.");
    }

    if (jsonElement == null || !jsonElement.isJsonObject()) {
      throw new IllegalArgumentException(
          "Spam detection JSON response it's null or isn't a JSON object.");
    }

    JsonObject resJsonObject = jsonElement.getAsJsonObject();
    if (resJsonObject.isEmpty()) {
      throw new IllegalArgumentException(
          "Parsed JSON object is empty. Expected 'result' field.");
    }

    JsonElement resultElement = resJsonObject.get("result");
    if (resultElement == null) {
      throw new IllegalArgumentException("Missing 'result' field in JSON response.");
    }
    return resultElement;
  }

  @Override
  public SpamDetectionResult checkSpam(String text) throws Exception {
    return null;
  }
}
