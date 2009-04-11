package de.lessvoid.nifty.screen;

public class NullScreen extends Screen {
  public NullScreen() {
    super(null, null, null, null);
  }

  public boolean isNull() {
    return true;
  }
}
