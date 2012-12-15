package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.screen.Screen;

public class ElementRemoveAction implements Action {
  public void perform(final Screen screen, final Element element) {
    element.removeFromFocusHandler();

    // we'll now resetAllEffects here when an element is removed. this was especially
    // introduced to reset all onHover effects that were used for changing the mouse cursor image.
    // without this reset the mouse cursor image might hang when elements are being removed
    // that changed the image.
    element.resetAllEffects();
    element.onEndScreen(screen);

    removeSingleElement(screen, element);
    Element parent = element.getParent();
    if (parent != null) {
      parent.internalRemoveElement(element);

      // when the parent is the root element then the element we're removing is a layer element
      if (parent == screen.getRootElement()) {
        screen.removeLayerElement(element);
      } else {
        parent.layoutElements();
      }
    }
  }

  private void removeSingleElement(final Screen screen, final Element element) {
    element.internalRemoveElementWithChilds();
  }
}