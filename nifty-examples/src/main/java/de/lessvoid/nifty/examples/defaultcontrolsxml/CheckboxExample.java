package de.lessvoid.nifty.examples.defaultcontrolsxml;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

/**
 * This class defines the default controls example for the <b>Checkbox</b> control.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class CheckboxExample implements NiftyExample {
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "defaultcontrolsxml/03-checkbox.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty default controls examples - Checkbox Control";
  }

  @Override
  public void prepareStart(final Nifty nifty) {
    // nothing to do
  }
}
