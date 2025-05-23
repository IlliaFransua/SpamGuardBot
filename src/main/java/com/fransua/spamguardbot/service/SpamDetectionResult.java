package com.fransua.spamguardbot.service;

// TODO: refactor
public record SpamDetectionResult(String modelName, double probability, Exception exception) {

}
