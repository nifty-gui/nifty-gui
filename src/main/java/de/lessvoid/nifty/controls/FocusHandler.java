package de.lessvoid.nifty.controls;

import java.util.ArrayList;

import de.lessvoid.nifty.elements.Element;

/**
 * FocusHandler.
 * @author void
 */
public class FocusHandler {

  /**
   * all elements that this focus handler controls.
   */
  private ArrayList < Element > entries = new ArrayList < Element >();

  /**
   * add an element to the focus handler.
   * @param element element to add
   */
  public void addElement(final Element element) {
    entries.add(element);
  }

  /**
   * get next element.
   * @param current current element
   * @return next element
   */
  public Element getNext(final Element current) {
    if (entries.isEmpty()) {
      return current;
    }

    int index = entries.indexOf(current);
    if (index == -1) {
      return current;
    }

    index++;
    if (index >= entries.size()) {
      index = 0;
    }
    return entries.get(index);
  }

  /**
   * get prev element.
   * @param current current element
   * @return prev element
   */
  public Element getPrev(final Element current) {
    if (entries.isEmpty()) {
      return current;
    }

    int index = entries.indexOf(current);
    if (index == -1) {
      return current;
    }

    index--;
    if (index < 0) {
      index = entries.size() - 1;
    }
    return entries.get(index);
  }
}
