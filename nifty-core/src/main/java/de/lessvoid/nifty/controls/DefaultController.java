package de.lessvoid.nifty.controls;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class DefaultController implements Controller {
  private FocusHandler focusHandler;
  private Element element;
  private NextPrevHelper nextPrevHelper;

  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.element = element;
    focusHandler = screen.getFocusHandler();
    nextPrevHelper = new NextPrevHelper(element, focusHandler);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
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
