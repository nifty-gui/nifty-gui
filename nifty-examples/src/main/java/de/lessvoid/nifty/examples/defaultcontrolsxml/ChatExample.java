package de.lessvoid.nifty.examples.defaultcontrolsxml;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

/**
 * This class defines the default controls example for the <b>Chat</b> control.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ChatExample implements NiftyExample {
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "defaultcontrolsxml/02-chat.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty default controls examples - Chat Control";
  }

  @Override
  public void prepareStart(final Nifty nifty) {
    // nothing to do
  }
}
