package de.lessvoid.nifty.examples.defaultcontrolsxml;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

import javax.annotation.Nonnull;

/**
 * This class defines the default controls example for the <b>Scrollbar</b> control.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ScrollbarExample implements NiftyExample {
  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "defaultcontrolsxml/10-scrollbar.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty default controls examples - Scrollbar Control";
  }

  @Override
  public void prepareStart(final Nifty nifty) {
    // nothing to do
  }
}
