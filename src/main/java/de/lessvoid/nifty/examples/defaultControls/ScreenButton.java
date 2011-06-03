package de.lessvoid.nifty.examples.defaultControls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ScreenButton implements ScreenController, KeyInputHandler {
  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    nifty = newNifty;
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  @NiftyEventSubscriber(id="button")
  public void button(final String id, final ButtonClickedEvent event) {
    System.out.println("button: " + id + " clicked");
  }

  @NiftyEventSubscriber(id="reset")
  public void resetButton(final String id, final ButtonClickedEvent event) {
    nifty.gotoScreen(nifty.getCurrentScreen().getScreenId());
  }

  public boolean keyEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.MoveCursorRight) {
      nifty.gotoScreen("screenChatControl");
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorLeft) {
      nifty.gotoScreen("screenWindow");
    }
    return false;
  }
}
