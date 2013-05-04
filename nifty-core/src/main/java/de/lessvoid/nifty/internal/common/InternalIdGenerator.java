package de.lessvoid.nifty.internal.common;

import java.util.concurrent.atomic.AtomicInteger;

public class InternalIdGenerator {
  private static final AtomicInteger id = new AtomicInteger(1);

  public static int generate() {
    return id.incrementAndGet();
  }
}
