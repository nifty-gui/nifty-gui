package de.lessvoid.nifty.examples.lwjgl.progressbar;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.lwjgl.NiftyExampleLoaderLWJGL3;
import de.lessvoid.nifty.examples.progressbar.ProgressbarControl;

import javax.annotation.Nonnull;

import static org.lwjgl.Sys.getTime;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class ProgressBarUpdater implements NiftyExampleLoaderLWJGL3.RenderLoopCallback {
  /** The time elapsed since the last reset. This is used to calculate the state of the progress bar. */
  private int deltaSum = 0;

  /** The time at the last frame. */
  private long lastFrame = 0;

  /** Temporary variable to hold the number of milliseconds that have passed since the last frame. */
  private int delta;

  /** Implement custom render loop callback in order to update the progressbar. */
  @Override
  public void process(@Nonnull final Nifty nifty) {
    delta = getDelta();
    if (delta == 0) {
      return;
    }
    deltaSum += delta;
    if (deltaSum >= 5000) {
      nifty.getScreen("start").findControl("my-progress", ProgressbarControl.class).setProgress(1.f);
      if (deltaSum >= 7000) {
        deltaSum = 0;
      }
    } else {
      nifty.getScreen("start").findControl("my-progress", ProgressbarControl.class).setProgress(deltaSum / 5000.f);
    }
  }

  /**
   * Calculates how many milliseconds have passed since the last frame.
   *
   * @return milliseconds passed since last frame
   */
  private int getDelta() {
    long time = getTime();
    int delta = (int) (time - lastFrame);
    lastFrame = time;

    return delta;
  }
}
