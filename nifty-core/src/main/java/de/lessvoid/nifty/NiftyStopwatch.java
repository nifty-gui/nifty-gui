package de.lessvoid.nifty;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This stop watch is used to measure the time required for special operations. This is only used for logging purposes.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class NiftyStopwatch {
  /**
   * The logger that handles showing the output of this class.
   */
  @Nonnull
  private static final Logger log = Logger.getLogger(NiftyStopwatch.class.getName());

  /**
   * The level used to show the logging output.
   */
  @Nonnull
  private static final Level usedLogLevel = Level.FINE;

  /**
   * The LIFO queue used to store the time values
   */
  @Nonnull
  private static final Queue<Long> stack = Collections.asLifoQueue(new LinkedList<Long>());

  /**
   * Private constructor
   */
  private NiftyStopwatch() {
  }

  /**
   * Start measuring the time.
   */
  public static void start() {
    stack.offer(now());
  }

  /**
   * Stop the time measurement and show the logging output along with the supplied message.
   *
   * @param message the message to show in the logging output
   */
  public static void stop(@Nonnull final String message) {
    long length = stop();
    if (log.isLoggable(usedLogLevel)) {
      StringBuilder b = new StringBuilder();
      b.append("[").append(String.format("%04d", length)).append("] ");
      for (int i = 0; i < stack.size(); i++) {
        b.append(".");
      }
      b.append(' ').append(message);
      log.log(usedLogLevel, b.toString());
    }
  }

  /**
   * Stop the time measuring.
   *
   * @return the time since the last start call in milliseconds
   */
  public static long stop() {
    return now() - stack.poll();
  }

  /**
   * Get the current timestamp.
   *
   * @return the current timestamp
   */
  private static long now() {
    return System.currentTimeMillis();
  }
}
