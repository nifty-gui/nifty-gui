package de.lessvoid.nifty.controls;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.helper.NextPrevHelper;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.screen.Screen;

public class DefaultController implements Controller {
  private FocusHandler focusHandler;
  private Element element;
  private NextPrevHelper nextPrevHelper;

  public void bind(
      final Nifty nifty,
      final Element element,
      final Properties parameter,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    this.element = element;
  }

  public void onStartScreen(final Screen screen) {
    focusHandler = screen.getFocusHandler();
    nextPrevHelper = new NextPrevHelper(element, focusHandler);
  }

  public void inputEvent(final NiftyInputEvent inputEvent) {
    if (nextPrevHelper.handleNextPrev(inputEvent)) {
      return;
    } else if (inputEvent == NiftyInputEvent.Activate) {
      element.onClick();
    }
  }
  
  public void onFocus(final boolean getFocus) {
  }
}
