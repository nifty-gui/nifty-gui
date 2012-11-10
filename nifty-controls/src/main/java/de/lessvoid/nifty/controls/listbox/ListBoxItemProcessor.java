package de.lessvoid.nifty.controls.listbox;

import de.lessvoid.nifty.elements.Element;

/**
 * This interfaces defines a class that has to process a list box label item once its created.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface ListBoxItemProcessor {
  /**
   * This function is called for every element that is displayed inside the list box once its created.
   *
   * @param element the element to process
   */
  void processElement(Element element);
}
