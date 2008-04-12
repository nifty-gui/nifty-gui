package de.lessvoid.nifty.elements;

/**
 * The MouseFocusHandler can be used to restrict the access to the mouse
 * resource. It is used so that an Element can request exclusive access
 * to the mouse.
 *
 * @author void
 */
public interface MouseFocusHandler {

  /**
   * Change the focus to a new element.
   * @param newFocusElement the new element that should get the focus
   */
  void requestExclusiveFocus(Element newFocusElement);

  /**
   * Give the resource back.
   * @param elementThatLostFocus element that frees the resource
   */
  void lostFocus(Element elementThatLostFocus);

  /**
   * Checks if the given element can process Mouse events. It can process
   * the events when no other element has exclusive access to the mouse
   * or the given element has exclusive access.
   * @param element the element that requests access to the mouse
   * @return true, when the access is granted and false otherwise
   */
  boolean canProcessMouseEvents(Element element);
}
