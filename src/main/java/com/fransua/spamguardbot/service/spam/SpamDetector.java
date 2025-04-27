package com.fransua.spamguardbot.service.spam;


public interface SpamDetector {

  boolean isSpam(String text);
}
