package de.lessvoid.nifty.controls.scrollbar.impl;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

public interface ScrollBarImpl {
  int translateValue(int mouseX, int mouseY);
  void setPosition(Element scrollPos, int newPos);
  void resizeHandle(Element scrollPos, SizeValue sizeValue);
}
