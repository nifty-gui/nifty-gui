package de.lessvoid.nifty.examples.reload;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ScreenController for Hello World Example.
 * @author void
 */
public class ReloadScreen implements ScreenController, NiftyExample {
  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
  }

  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  /**
   * This is called from the initial "start.xml" and the id="start" screen
   */
  public void loadNext() {
    // this loads an additional nifty xml without removing any of the existing screens
    nifty.addXml("reload/next.xml");

    // now that we have loaded an additional screen we can goto the new screen
    nifty.gotoScreen("next");
  }

  /**
   * This is called from the "next.xml" and the id="next" screen
   */
  @NiftyEventSubscriber(id="back")
  public void back(final String id, final ButtonClickedEvent event) {
    // this will go back to the initially loaded screen with the id "start"
    // note that this screen is still there although we've loaded an additional xml file
    nifty.gotoScreen("start");
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "reload/start.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty Reload Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
