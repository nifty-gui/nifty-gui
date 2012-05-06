package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.screen.Screen;

interface Action {
  void perform(Screen screen, Element element);
}