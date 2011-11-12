package de.lessvoid.nifty;

public class ClipboardNull implements Clipboard {

  @Override
  public void put(String data) {
  }

  @Override
  public String get() {
    return null;
  }

}
