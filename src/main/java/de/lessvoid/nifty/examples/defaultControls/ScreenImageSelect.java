package de.lessvoid.nifty.examples.defaultControls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ScreenImageSelect implements ScreenController, KeyInputHandler {
  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    nifty = newNifty;
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
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorLeft) {
      nifty.gotoScreen("screenButton");
    }
    return false;
  }
}
