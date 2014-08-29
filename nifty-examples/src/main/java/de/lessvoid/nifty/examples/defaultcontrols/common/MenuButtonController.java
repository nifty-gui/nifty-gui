package de.lessvoid.nifty.examples.defaultcontrols.common;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;

public class MenuButtonController implements Controller {
  private Element element;
  private FocusHandler focusHandler;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    this.element = element;
    this.focusHandler = screen.getFocusHandler();
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.Activate) {
      element.onClickAndReleasePrimaryMouseButton();
      return true;
    }
    return false;
  }
}
