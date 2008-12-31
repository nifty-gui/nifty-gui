package de.lessvoid.nifty.controls.scrollbar.impl;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

public class ScrollBarImplVertical implements ScrollBarImpl {
  
  public void resizeHandle(final Element scrollPos, final SizeValue sizeValue) {
    if (scrollPos != null) {
      scrollPos.setConstraintHeight(sizeValue);
    }
  }
  
  public void setPosition(final Element scrollPos, final int newPos) {
    if (scrollPos != null) {
      scrollPos.setConstraintX(new SizeValue(0 + "px"));
      scrollPos.setConstraintY(new SizeValue(newPos + "px"));
    }
  }
  
  public int translateValue(int mouseX, int mouseY) {
    return mouseY;
  }
}
