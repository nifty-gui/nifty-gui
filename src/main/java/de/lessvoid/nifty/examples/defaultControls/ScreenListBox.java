package de.lessvoid.nifty.examples.defaultControls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ScreenListBox implements ScreenController {
  private Nifty nifty;
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;
    ListBox listBox = screen.findNiftyControl("listbox", ListBox.class);
    listBox.addItem("test-1");
    listBox.addItem("test-2");
    listBox.addItem("test-3");
    listBox.addItem("test-4");
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  @NiftyEventSubscriber(id="reset")
  public void resetButton(final String id, final ButtonClickedEvent event) {
    nifty.gotoScreen("screenListBox");
  }
}
