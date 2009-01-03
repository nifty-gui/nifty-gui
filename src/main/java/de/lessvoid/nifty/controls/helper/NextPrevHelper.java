package de.lessvoid.nifty.controls.helper;

import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;

public class NextPrevHelper {
  private Element element;
  private FocusHandler focusHandler;

  public NextPrevHelper(final Element elementParam, final FocusHandler focusHandlerParam) {
    element = elementParam;
    focusHandler = focusHandlerParam;
  }

  public boolean handleNextPrev(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.NextInputElement) {
      if (focusHandler != null) {
        focusHandler.getNext(element).setFocus();
      }
      return true;
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      if (focusHandler != null) {
        focusHandler.getPrev(element).setFocus();
      }
      return true;
    }
    return false;
  }
}
