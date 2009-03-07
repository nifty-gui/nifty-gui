package de.lessvoid.nifty.controls.scrollbar.controller.impl;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

public class ScrollBarImplHorizontal implements ScrollBarImpl {
  
  public void resizeHandle(final Element scrollPos, final SizeValue sizeValue) {
    if (scrollPos != null) {
      scrollPos.setConstraintWidth(sizeValue);
    }
  }

  public void setPosition(final Element scrollPos, final int newPos) {
    if (scrollPos != null) {
      scrollPos.setConstraintX(new SizeValue(newPos + "px"));
      scrollPos.setConstraintY(new SizeValue(0 + "px"));
    }
  }
  
  public int translateValue(int mouseX, int mouseY) {
    return mouseX;
  }
}
