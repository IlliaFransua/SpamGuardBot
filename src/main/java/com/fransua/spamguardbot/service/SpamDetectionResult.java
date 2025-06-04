package com.fransua.spamguardbot.service;

public record SpamDetectionResult(String modelName, double probability, Exception exception) {

}
