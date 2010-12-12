package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.elements.Element;

/**
 * You'll need to implement this interface to change the way your model class T needs
 * to be displayed in the given element. If you omit it then Nifty will use its default
 * implementation which simply calls T.toString();
 * @author void
 * @param <T>
 */
public interface ListBoxViewConverter<T> {

  /**
   * Display the given item in the given element.
   * @param element the element to display the item in
   * @param item the item to display
   */
  void display(Element element, T item);
}