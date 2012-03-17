package de.lessvoid.nifty.slick2d.time;

import org.lwjgl.Sys;

import de.lessvoid.nifty.spi.time.TimeProvider;

/**
 * This time provider uses the timer that is provided by LWJGL.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class LWJGLTimeProvider implements TimeProvider {
  /**
   * The conversation factor from the time provided by the LWJGL time to the time expected by Nifty.
   */
  private static final long CONVERSATION_FACTOR = 1000;

  @Override
  public long getMsTime() {
    return (Sys.getTime() * CONVERSATION_FACTOR) / Sys.getTimerResolution();
  }

}
