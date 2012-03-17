package de.lessvoid.nifty.examples.messagebox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.MessageBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * The screen controller for the message box example.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class MessageBoxStartScreen implements ScreenController {
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen screenParam) {
    screen = screenParam;
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  public void changeMessageBoxType(final String newType) {
    final MessageBox msgBox = screen.findNiftyControl("messagebox", MessageBox.class);
    msgBox.setMessageType(newType);
    
    msgBox.getElement().getParent().layoutElements();
  }
}
