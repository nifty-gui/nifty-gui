package de.lessvoid.nifty.examples.defaultcontrolsxml;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

/**
 * This class defines the default controls example for the <b>Window</b> control.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class WindowExample implements NiftyExample {
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "defaultcontrolsxml/14-window.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty default controls examples - Window Control";
  }

  @Override
  public void prepareStart(final Nifty nifty) {
    // nothing to do
  }
}
