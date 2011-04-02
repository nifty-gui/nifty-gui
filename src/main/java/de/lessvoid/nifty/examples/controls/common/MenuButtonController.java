package de.lessvoid.nifty.examples.controls.common;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class MenuButtonController implements Controller {
  private Element element;
  private FocusHandler focusHandler;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.element = element;
    this.focusHandler = screen.getFocusHandler();
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
      return true;
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
      return true;
    } else if (inputEvent == NiftyInputEvent.Activate) {
      element.onClick();
      return true;
    }
    return false;
  }
}
