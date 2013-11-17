package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;

public class DefaultController implements Controller {
  private FocusHandler focusHandler;
  private Element element;
  private NextPrevHelper nextPrevHelper;

  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Parameters parameter) {
    this.element = element;
    focusHandler = screen.getFocusHandler();
    nextPrevHelper = new NextPrevHelper(element, focusHandler);
  }

  @Override
  public void init(final Parameters parameter) {
  }

  public void onStartScreen() {
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (nextPrevHelper.handleNextPrev(inputEvent)) {
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.Activate) {
      element.onClick();
      return true;
    }
    return false;
  }
  
  public void onFocus(final boolean getFocus) {
  }
}
