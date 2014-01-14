package de.lessvoid.nifty.examples.tutorial;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

import javax.annotation.Nonnull;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class TutorialExampleMain implements NiftyExample {
  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "tutorial/tutorial.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty 1.2 Tutorial";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
