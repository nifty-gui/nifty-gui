package de.lessvoid.nifty;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.Stack;
import java.util.logging.Logger;

public class NiftyStopwatch {
  private static final Logger log = Logger.getLogger(NiftyStopwatch.class.getName());
  @Nonnull
  private static final Stack<Long> stack = new Stack<Long>();

  public static void start() {
    stack.push(now());
  }

  public static void stop(final String message) {
    long length = now() - stack.pop();
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < stack.size(); i++) {
      b.append(".");
    }
    log.fine("[" + String.format("%04d", length) + "] " + b + message);
  }

  public static long stop() {
    return now() - stack.pop();
  }

  private static long now() {
    return new Date().getTime();
  }
}
