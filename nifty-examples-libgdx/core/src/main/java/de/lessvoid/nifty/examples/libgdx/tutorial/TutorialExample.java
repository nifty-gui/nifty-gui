package de.lessvoid.nifty.examples.libgdx.tutorial;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class TutorialExample implements NiftyExample {
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "tutorial/tutorial.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty 1.2 Tutorial";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
