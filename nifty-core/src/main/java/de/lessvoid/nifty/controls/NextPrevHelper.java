package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;

import javax.annotation.Nonnull;

public class NextPrevHelper {
  @Nonnull
  private final Element element;
  @Nonnull
  private final FocusHandler focusHandler;

  public NextPrevHelper(@Nonnull final Element elementParam, @Nonnull final FocusHandler focusHandlerParam) {
    element = elementParam;
    focusHandler = focusHandlerParam;
  }

  public boolean handleNextPrev(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.NextInputElement) {
      focusHandler.getNext(element).setFocus();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.PrevInputElement) {
      focusHandler.getPrev(element).setFocus();
      return true;
    }
    return false;
  }
}
