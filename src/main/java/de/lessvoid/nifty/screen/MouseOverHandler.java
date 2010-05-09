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
  private ArrayList < Element > elements = new ArrayList < Element >();

  /**
   * Reset mouse over elements.
   */
  public void reset() {
    elements.clear();
  }

  /**
   * Add Element.
   * @param element Element
   */
  public void addElement(final Element element) {
    elements.add(element);
  }

  /**
   * Get current state as a String supposed for debug output.
   * @return info
   */
  public String getInfoString() {
    if (elements.isEmpty()) {
      return "  ---";
    } else {
      StringBuffer result = new StringBuffer();
      for (int i = elements.size() - 1; i >= 0; i--) {
        Element element = elements.get(i);
        result.append("  " + element.getId());
      }
      return result.toString();
    }
  }


  /**
   * Process Event.
   * @param rootElement element
   * @param mouseEvent mouse event
   * @param eventTime time
   */
  public void processMouseOverEvent(
      final Element rootElement,
      final MouseInputEvent mouseEvent,
      final long eventTime) {
    for (int i = elements.size() - 1; i >= 0; i--) {
      Element element = elements.get(i);
      if (element.mouseOverEvent(mouseEvent, eventTime)) {
        return;
      }
    }
  }

  public boolean hitsElement() {
    return !elements.isEmpty();
  }
}
