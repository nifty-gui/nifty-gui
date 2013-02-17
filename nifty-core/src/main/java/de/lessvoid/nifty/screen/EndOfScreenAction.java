package de.lessvoid.nifty.screen;

import de.lessvoid.nifty.elements.Action;
import de.lessvoid.nifty.elements.Element;

public class EndOfScreenAction implements Action {

  public void perform(final Screen screen, final Element element) {
    screen.onEndScreenHasEnded();
  }
}