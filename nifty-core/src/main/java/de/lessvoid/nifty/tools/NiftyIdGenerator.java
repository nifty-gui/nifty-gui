package de.lessvoid.nifty.tools;

import java.util.concurrent.atomic.AtomicInteger;

public class NiftyIdGenerator {
  private static final AtomicInteger id = new AtomicInteger(1);

  public static String generate() {
    return String.valueOf(id.incrementAndGet());
  }
}
