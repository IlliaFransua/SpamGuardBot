package com.fransua.spamguardbot.handler;


public enum HandlerProcessingStatus {
  NOT_CHECKED,
  CAN_BE_HANDLED,
  CAN_NOT_BE_HANDLED;

  public static HandlerProcessingStatus defaultStatus() {
    return NOT_CHECKED;
  }
}
