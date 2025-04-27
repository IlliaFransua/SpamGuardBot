package com.fransua.spamguardbot.service.spam.advertisement;


import com.fransua.spamguardbot.service.spam.SpamDetector;


public class AdvertisementDetector implements SpamDetector {

  @Override
  public boolean isSpam(String text) {
    return false;
  }
}
