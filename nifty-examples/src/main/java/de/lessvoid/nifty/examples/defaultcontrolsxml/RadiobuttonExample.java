package de.lessvoid.nifty.examples.defaultcontrolsxml;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

/**
 * This class defines the default controls example for the <b>Radiobutton</b> control.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class RadiobuttonExample implements NiftyExample {
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "defaultcontrolsxml/09-radiobutton.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty default controls examples - Radiobutton Control";
  }

  @Override
  public void prepareStart(final Nifty nifty) {
    // nothing to do
  }
}
