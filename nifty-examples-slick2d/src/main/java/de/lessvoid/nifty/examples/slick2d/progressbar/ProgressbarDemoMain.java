package de.lessvoid.nifty.examples.slick2d.progressbar;

import de.lessvoid.nifty.examples.progressbar.ProgressbarControl;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

/**
 * Demo class to execute the progressbar demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ProgressbarDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new ProgressbarControl()) {
      /**
       * The time elapsed since the last reset. This is used to calculate the state of the progress bar.
       */
      private int deltaSum = 0;

      /**
       * Overwritten update game implementation in order to update the progressbar.
       */
      @Override
      public void updateGame(final GameContainer container, final int delta) throws SlickException {
        super.updateGame(container, delta);
        if (delta == 0) {
          return;
        }
        deltaSum += delta;
        if (deltaSum >= 5000) {
          getNifty().getScreen("start").findControl("my-progress", ProgressbarControl.class).setProgress(1.f);
          if (deltaSum >= 7000) {
            deltaSum = 0;
          }
        } else {
          getNifty().getScreen("start").findControl("my-progress", ProgressbarControl.class).setProgress(
              deltaSum / 5000.f);
        }
      }
    });
  }
}
