package de.lessvoid.nifty.tools;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class TargetElementResolver {
  private final static String PARENT = "#parent";

  private Screen screen;
  private Element base;

  public TargetElementResolver(final Screen screen, final Element baseElement) {
    this.screen = screen;
    this.base = baseElement;
  }

  public Element resolve(final String id) {
    if (id == null) {
      return null;
    }
    if (id.startsWith(PARENT)) {
      String subParentId = id.replaceFirst(PARENT, "");
      if (subParentId.startsWith("#")) {
        return base.getParent().findElementByName(subParentId.replaceFirst("#", ""));
      }
      return base.getParent();
    }
    return screen.findElementByName(id);
  }  
}
