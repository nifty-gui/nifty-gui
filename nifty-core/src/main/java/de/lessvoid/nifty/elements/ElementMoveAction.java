package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.screen.Screen;

public class ElementMoveAction implements Action {
  private Element destinationElement;

  public ElementMoveAction(final Element destinationElement) {
    this.destinationElement = destinationElement;
  }

  public void perform(final Screen screen, final Element element) {
    Element parent = element.getParent();
    if (parent != null) {
      parent.internalRemoveElement(element);
    }
    element.setParent(destinationElement);
    destinationElement.add(element);

    // now we'll need to add elements back to the focushandler
    addToFocusHandler(element);

    screen.layoutLayers();
  }

  private void addToFocusHandler(final Element element) {
    if (element.isFocusable()) {
      // currently add the element to the end of the focushandler
      //
      // this is not quite right but at the moment I don't have any idea on how
      // to find the right spot in the focushandler to insert the element into
      // (it should really be to spot where it has been removed from)
      element.getFocusHandler().addElement(element);
    }
    for (int i=0; i<element.getElements().size(); i++) {
      addToFocusHandler(element.getElements().get(i));
    }
  }
}