package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public interface StandardControl {
  Element createControl(Nifty nifty, Screen screen, Element parent) throws Exception;
}
