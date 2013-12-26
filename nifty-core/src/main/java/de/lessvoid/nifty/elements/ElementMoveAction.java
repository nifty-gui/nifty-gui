package de.lessvoid.nifty.elements;

import javax.annotation.Nonnull;
import java.util.List;

public class ElementMoveAction implements Action {
  @Nonnull
  private final Element movedElement;
  @Nonnull
  private final Element destinationElement;

  public ElementMoveAction(
      @Nonnull final Element movedElement,
      @Nonnull final Element destinationElement) {
    this.movedElement = movedElement;
    this.destinationElement = destinationElement;
  }

  @Override
  public void perform() {
    if (movedElement.hasParent()) {
      movedElement.getParent().internalRemoveElement(movedElement);
    }
    movedElement.setParent(destinationElement);
    destinationElement.addChild(movedElement);

    // now we'll need to add elements back to the focus handler
    addToFocusHandler(movedElement);

    movedElement.getParent().layoutElements();
    destinationElement.layoutElements();
  }

  private void addToFocusHandler(@Nonnull final Element element) {
    if (element.isFocusable()) {
      // currently add the element to the end of the focus handler
      //
      // this is not quite right but at the moment I don't have any idea on how
      // to find the right spot in the focus handler to insert the element into
      // (it should really be to spot where it has been removed from)
      element.getFocusHandler().addElement(element);
    }

    final List<Element> children = element.getChildren();
    final int size = children.size();
    for (int i = 0; i < size; i++) {
      addToFocusHandler(children.get(i));
    }
  }
}
