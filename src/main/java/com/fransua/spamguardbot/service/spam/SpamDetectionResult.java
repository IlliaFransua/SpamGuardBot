package com.fransua.spamguardbot.service.spam;


public record SpamDetectionResult(String modelName, double probability, Exception exception) {

}
