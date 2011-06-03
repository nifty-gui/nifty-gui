package de.lessvoid.nifty.examples.defaultControls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.chatcontrol.ChatControl;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ScreenChatControl implements ScreenController, KeyInputHandler {
  private Nifty nifty;

  public void bind(final Nifty newNifty, final Screen screen) {
    nifty = newNifty;
    screen.findNiftyControl("chat", ChatControl.class).addPlayer("tester", null);
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  @NiftyEventSubscriber(id="reset")
  public void resetButton(final String id, final ButtonClickedEvent event) {
    nifty.gotoScreen(nifty.getCurrentScreen().getScreenId());
  }

  @NiftyEventSubscriber(id="chat")
  public void chatTextSend(final String id, final ChatTextSendEvent event) {
    event.getChatControl().receivedChatLine(event.getText(), null);
  }

  public boolean keyEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.MoveCursorRight) {
      nifty.gotoScreen("screenCheckBox");
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorLeft) {
      nifty.gotoScreen("screenButton");
    }
    return false;
  }
}
