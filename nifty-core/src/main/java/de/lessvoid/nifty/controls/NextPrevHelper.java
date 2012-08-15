package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;

public class NextPrevHelper {
  private Element element;
  private FocusHandler focusHandler;

  public NextPrevHelper(final Element elementParam, final FocusHandler focusHandlerParam) {
    element = elementParam;
    focusHandler = focusHandlerParam;
  }

  public boolean handleNextPrev(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      if (focusHandler != null) {
        focusHandler.getNext(element).setFocus();
      }
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      if (focusHandler != null) {
        focusHandler.getPrev(element).setFocus();
      }
      return true;
    }
    return false;
  }
}
