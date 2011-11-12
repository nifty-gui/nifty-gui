package de.lessvoid.nifty.screen;

public class NullScreen extends Screen {
  public NullScreen() {
    super(null, null, new DefaultScreenController(), null);
  }

  public boolean isNull() {
    return true;
  }
}
