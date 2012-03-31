package de.lessvoid.nifty.examples.defaultcontrolsxml;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

/**
 * This class defines the default controls example for the <b>ListBox</b> control.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ListboxExample implements NiftyExample {
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "defaultcontrolsxml/08-listbox.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty default controls examples - ListBox Control";
  }

  @Override
  public void prepareStart(final Nifty nifty) {
    // nothing to do
  }
}
