package de.lessvoid.nifty.screen;

import java.util.ArrayList;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;

/**
 * The MouseOverHandler manages mouse over elements.
 * @author void
 */
public class MouseOverHandler {

  /**
   * Elements with mouse over.
   */
  private ArrayList < Element > mouseOverElements = new ArrayList < Element >();

  /**
   * Elements that can handle mouse events but have no mouse over.
   */
  private ArrayList < Element > mouseElements = new ArrayList < Element >();

  /**
   * Reset mouse over elements.
   */
  public void reset() {
    mouseOverElements.clear();
    mouseElements.clear();
  }

  /**
   * Add Element.
   * @param element Element
   */
  public void addMouseOverElement(final Element element) {
    mouseOverElements.add(element);
  }

  /**
   * Get current state as a String supposed for debug output.
   * @return info
   */
  public String getInfoString() {
    StringBuffer result = new StringBuffer();
    result.append("mouse over elements: ");
    outputElements(result, mouseOverElements);
    result.append(" mouse elements: ");
    outputElements(result, mouseElements);
    return result.toString();
  }

  private void outputElements(final StringBuffer result, final ArrayList<Element> elements) {
    if (elements.isEmpty()) {
      result.append("---");
    } else {
      for (int i = elements.size() - 1; i >= 0; i--) {
        Element element = elements.get(i);
        result.append("[" + element.getId() + "]");
      }
    }
  }


  public void processMouseOverEvent(
      final Element rootElement,
      final MouseInputEvent mouseEvent,
      final long eventTime) {
    for (int i = mouseOverElements.size() - 1; i >= 0; i--) {
      Element element = mouseOverElements.get(i);
      if (element.mouseOverEvent(mouseEvent, eventTime)) {
        return;
      }
    }
  }

  public void processMouseEvent(final MouseInputEvent mouseEvent, final long eventTime) {
    // first step is to preprocess hover effects for all elements
    // this will deactivate all hover effects that are not active anymore
    // Note: This will make sure that all hover effects will be deactivated before a new
    // hover effect will be activated. This was necessary for the ChangeMouseCursor effect to
    // work correctly when you quickly changed hover from one element to another it was possible
    // that the hover effect for the new element started before the old one was deactivated so
    // the reset of the mousecursor (see ChangeMouseCursor effect) did not worked correctly.
    for (int i = mouseElements.size() - 1; i >= 0; i--) {
      Element element = mouseElements.get(i);
      element.mouseEventHoverPreprocess(mouseEvent, eventTime);
    }

    // second step is to process mouse over elements first
    for (int i = mouseOverElements.size() - 1; i >= 0; i--) {
      Element element = mouseOverElements.get(i);
      if (element.mouseEvent(mouseEvent, eventTime)) {
        return;
      }
    }

    // last step is to process all other elements.
    for (int i = mouseElements.size() - 1; i >= 0; i--) {
      Element element = mouseElements.get(i);
      if (element.mouseEvent(mouseEvent, eventTime)) {
        return;
      }
    }
}

  public boolean hitsElement() {
    return !mouseOverElements.isEmpty();
  }

  public void addMouseElement(final Element element) {
    mouseElements.add(element);
  }
}
