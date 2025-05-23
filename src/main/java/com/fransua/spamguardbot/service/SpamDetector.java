package com.fransua.spamguardbot.service;

public interface SpamDetector {

  boolean isSpam(String text) throws Exception;

  SpamDetectionResult checkSpam(String text) throws Exception;
}
