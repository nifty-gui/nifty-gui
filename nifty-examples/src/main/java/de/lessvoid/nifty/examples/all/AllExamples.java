package de.lessvoid.nifty.examples.all;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;

import javax.annotation.Nonnull;

/**
 * @author void
 */
public class AllExamples implements NiftyExample {
  private static final String ALL_INTRO_XML = "all/intro.xml";
  private final String startScreen;

  public AllExamples() {
    this("start");
  }

  private AllExamples(final String screen) {
    startScreen = screen;
  }

  @Override
  public String getStartScreen() {
    return startScreen;
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return ALL_INTRO_XML;
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Examples";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
