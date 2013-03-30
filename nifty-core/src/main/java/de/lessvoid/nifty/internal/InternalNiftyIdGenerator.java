package de.lessvoid.nifty.internal;

import java.util.concurrent.atomic.AtomicInteger;

public class InternalNiftyIdGenerator {
  private static final AtomicInteger id = new AtomicInteger(1);

  public static String generate() {
    return String.valueOf(id.incrementAndGet());
  }
}
