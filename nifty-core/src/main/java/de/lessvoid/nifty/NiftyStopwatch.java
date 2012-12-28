package de.lessvoid.nifty;

import java.util.Date;
import java.util.Stack;
import java.util.logging.Logger;

public class NiftyStopwatch {
  private static Logger log = Logger.getLogger(NiftyStopwatch.class.getName());
  private static Stack<Long> stack = new Stack<Long>();

  {
  }

  public static void start() {
    stack.push(now());
  }

  public static void stop(final String message) {
    long length = now() - stack.pop();
    StringBuffer b = new StringBuffer();
    for (int i=0; i<stack.size(); i++) {
      b.append(".");
    }
    log.fine("[" + String.format("%04d", length) + "] " + b + message);
  }

  private static long now() {
    return new Date().getTime();
  }
}
