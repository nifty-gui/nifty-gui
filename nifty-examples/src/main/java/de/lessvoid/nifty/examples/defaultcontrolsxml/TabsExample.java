package de.lessvoid.nifty.examples.defaultcontrolsxml;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

import javax.annotation.Nonnull;

/**
 * This class defines the default controls example for the <b>TabGroup</b> control.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TabsExample implements NiftyExample {
  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "defaultcontrolsxml/15-tabs.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty default controls examples - TabGroup Control";
  }

  @Override
  public void prepareStart(final Nifty nifty) {
    // nothing to do
  }
}
