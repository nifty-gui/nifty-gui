package de.lessvoid.nifty.examples.falloff;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

import javax.annotation.Nonnull;

/**
 * This is the example definition of the Falloff example.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FalloffExample implements NiftyExample {
  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "falloff/falloff.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Falloff Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
