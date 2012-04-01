package de.lessvoid.nifty.examples.defaultcontrolsxml;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

/**
 * This class defines the default controls example for the <b>Textfield</b> control.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TextfieldExample implements NiftyExample {
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "defaultcontrolsxml/13-textfield.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty default controls examples - Textfield Control";
  }

  @Override
  public void prepareStart(final Nifty nifty) {
    // nothing to do
  }
}
