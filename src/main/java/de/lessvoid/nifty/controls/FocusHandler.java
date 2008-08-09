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
   * buffer to save stuff.
   */
  private ArrayList < ArrayList < Element >> elementBuffer = new ArrayList < ArrayList < Element >>();

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
    Element nextElement = entries.get(index);
    if (nextElement.isFocusable()) {
      return nextElement;
    } else {
      return getNext(nextElement);
    }
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
    Element prevElement = entries.get(index);
    if (prevElement.isFocusable()) {
      return prevElement;
    } else {
      return getPrev(prevElement);
    }
  }

  /**
   * remove this element.
   * @param element element
   */
  public void remove(final Element element) {
    entries.remove(element);
  }

  /**
   * get first entry.
   * @return first
   */
  public Element getFirstFocusElement() {
    if (entries.isEmpty()) {
      return null;
    }
    return entries.get(0);
  }

  /**
   * save all states.
   */
  public void pushState() {
    ArrayList < Element > copy = new ArrayList < Element >();
    copy.addAll(entries);
    elementBuffer.add(copy);

    entries.clear();
  }

  /**
   * restore all states.
   */
  public void popState() {
    entries.clear();
    entries.addAll(elementBuffer.get(elementBuffer.size() - 1));
  }
}
