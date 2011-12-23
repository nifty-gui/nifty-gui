package de.lessvoid.nifty.slick2d.time;

import org.lwjgl.util.Timer;

import de.lessvoid.nifty.spi.time.TimeProvider;

/**
 * This time provider uses the timer that is provided by LWJGL.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class LWJGLTimeProvider implements TimeProvider {
  /**
   * The conversation factor from the time provided by the LWJGL time to the
   * time expected by Nifty.
   */
  private static final float CONVERSATION_FACTOR = 1000.f;
  
  /**
   * The LWJGL timer used to get the current time.
   */
  private final Timer internalTimer;
  
  /**
   * Prepare the LWJGL based timer.
   */
  public LWJGLTimeProvider() {
    internalTimer = new Timer();
    internalTimer.set(System.currentTimeMillis() / CONVERSATION_FACTOR);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public long getMsTime() {
    return (long) (internalTimer.getTime() * CONVERSATION_FACTOR);
  }

}
