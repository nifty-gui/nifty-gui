package de.lessvoid.nifty;

public class NiftyIdCreator {
  private static int id = 0x1;

  public static synchronized String generate() {
    id++;
    return String.valueOf(id);
  }
}
