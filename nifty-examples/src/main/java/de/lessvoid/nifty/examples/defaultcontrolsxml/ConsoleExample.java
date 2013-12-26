package de.lessvoid.nifty.examples.defaultcontrolsxml;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

import javax.annotation.Nonnull;

/**
 * This class defines the default controls example for the <b>Console</b> control.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ConsoleExample implements NiftyExample {
  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "defaultcontrolsxml/04-console.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty default controls examples - Console Control";
  }

  @Override
  public void prepareStart(final Nifty nifty) {
    // nothing to do
  }
}
