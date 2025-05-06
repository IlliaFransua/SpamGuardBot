package com.fransua.spamguardbot.service.spam.profanity;


import com.fransua.spamguardbot.service.spam.SpamDetectionResult;
import com.fransua.spamguardbot.service.spam.SpamDetector;


public class ProfanityDetector implements SpamDetector {

  @Override
  public boolean isSpam(String text) throws Exception {
    return false;
  }

  @Override
  public SpamDetectionResult checkSpam(String text) throws Exception {
    return null;
  }
}
