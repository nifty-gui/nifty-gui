package de.lessvoid.nifty.examples.messagebox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.MessageBox;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class MessageBoxStartScreen implements ScreenController, NiftyExample {
  private Screen screen;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen screenParam) {
    screen = screenParam;
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void changeMessageBoxType(final String newType) {
    final MessageBox msgBox = screen.findNiftyControl("messagebox", MessageBox.class);
    msgBox.setMessageType(newType);

    msgBox.getElement().getParent().layoutElements();
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "messagebox/messagebox.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty MessageBox Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
