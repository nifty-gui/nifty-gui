package de.lessvoid.nifty.examples.defaultControls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ScreenDropDown implements ScreenController, KeyInputHandler {
  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen screen) {
    nifty = newNifty;
    DropDown dropDown = screen.findNiftyControl("dropdown", DropDown.class);
    dropDown.addItem("1");
    dropDown.addItem("2");
    dropDown.addItem("3");
    dropDown.addItem("4");
    dropDown.addItem("5");
    dropDown.addItem("6");
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  @NiftyEventSubscriber(id="reset")
  public void resetButton(final String id, final ButtonClickedEvent event) {
    nifty.gotoScreen(nifty.getCurrentScreen().getScreenId());
  }

  public boolean keyEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.MoveCursorRight) {
      nifty.gotoScreen("screenImageSelect");
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorLeft) {
      nifty.gotoScreen("screenConsole");
    }
    return false;
  }
}
