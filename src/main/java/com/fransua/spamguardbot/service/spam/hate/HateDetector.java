package com.fransua.spamguardbot.service.spam.hate;


import com.fransua.spamguardbot.service.spam.SpamDetector;


public class HateDetector implements SpamDetector {

  @Override
  public boolean isSpam(String text) {
    return false;
  }
}
