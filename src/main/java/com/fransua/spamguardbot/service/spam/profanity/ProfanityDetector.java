package com.fransua.spamguardbot.service.spam.profanity;


import com.fransua.spamguardbot.service.spam.SpamDetector;


public class ProfanityDetector implements SpamDetector {

  @Override
  public boolean isSpam(String text) {
    return false;
  }
}
