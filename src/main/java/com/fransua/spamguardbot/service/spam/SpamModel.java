package com.fransua.spamguardbot.service.spam;


public interface SpamModel {

  double predict(String text) throws Exception;
}
