package de.lessvoid.nifty.examples.slick2d.messagebox;

import de.lessvoid.nifty.examples.messagebox.MessageBoxStartScreen;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;

/**
 * Demo class to execute the message box demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class MessageBoxDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    SlickExampleLoader.createGame(new SlickExampleLoader(new MessageBoxStartScreen()));
  }
}
