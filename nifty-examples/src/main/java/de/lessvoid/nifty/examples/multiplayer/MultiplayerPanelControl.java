package de.lessvoid.nifty.examples.multiplayer;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;

/**
 * @author void
 */
public class MultiplayerPanelControl implements Controller {
  private Nifty nifty;
  private Screen screen;
  private Element element;

  @Override
  public void bind(
      @Nonnull final Nifty niftyParam,
      @Nonnull final Screen screenParam,
      @Nonnull final Element newElement,
      @Nonnull final Parameters properties) {
    nifty = niftyParam;
    screen = screenParam;
    element = newElement;
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
  }

  @Override
  public void onStartScreen() {
    setDifficulty("easy");
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  public void removePanel() {
    nifty.removeElement(screen, element);
  }

  public void setDifficulty(final String mode) {
    element.findElementById("#easy").setStyle("unselected");
    element.findElementById("#medium").setStyle("unselected");
    element.findElementById("#hard").setStyle("unselected");
    element.findElementById("#expert").setStyle("unselected");

    if ("easy".equals(mode)) {
      element.findElementById("#easy").setStyle("selected");
    } else if ("medium".equals(mode)) {
      element.findElementById("#medium").setStyle("selected");
    } else if ("hard".equals(mode)) {
      element.findElementById("#hard").setStyle("selected");
    } else if ("expert".equals(mode)) {
      element.findElementById("#expert").setStyle("selected");
    }
  }
}
