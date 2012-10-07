package de.lessvoid.nifty.controls;

import java.util.ArrayList;
import java.util.logging.Logger;

import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * FocusHandler.
 * @author void
 */
public class FocusHandler {
  private static Logger log = Logger.getLogger(FocusHandler.class.getName());

  private ArrayList < Element > entries = new ArrayList < Element >();
  private ArrayList < ArrayList < Element >> elementBuffer = new ArrayList < ArrayList < Element >>();

  private Element mouseFocusElement;
  private ArrayList < Element > mouseFocusElementBuffer = new ArrayList < Element >();

  private Element keyboardFocusElement;
  private ArrayList < Element > keyboardFocusElementBuffer = new ArrayList < Element >();

  public FocusHandler() {
    mouseFocusElement = null;
    keyboardFocusElement = null;
  }

  /**
   * add the given element to the focushandler (as the last element).
   * @param element element to add
   */
  public void addElement(final Element element) {
    addElement(element, null);
  }

  /**
   * add an element to the focus handler.
   * @param element element to add
   * @param focusableInsertBeforeElement the element before which to add the new element
   */
  public void addElement(final Element element, final Element focusableInsertBeforeElement) {
    if (focusableInsertBeforeElement == null) {
      entries.add(element);
    } else {
      int idx = entries.indexOf(focusableInsertBeforeElement);
      if (idx == -1) {
        log.warning("requesting to add focusable element before [" + focusableInsertBeforeElement + "] but I can't find it on the current screen. Adding it to the end of the list (like in the regular case)");
        entries.add(element);
      } else {
        entries.add(idx, focusableInsertBeforeElement);
      }
    }
  }

  /**
   * add an element to the focus handler after an existing element already added to it
   * @param existingElement element that already exists in the focushandler
   * @param element new element to add
   */
  public void addElementAfter(final Element existingElement, final Element element) {
    int idx = entries.indexOf(existingElement);
    if (idx == -1) {
      log.warning("requesting to add focusable element after [" + existingElement + "] but I can't find it on the current screen. Adding it to the end of the list (like in the regular case)");
      entries.add(element);
    } else {
      if (idx == entries.size() - 1) {
        entries.add(element);
      } else {
        entries.add(idx + 1, element);
      }
    }
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

    int index = entries.indexOf(keyboardFocusElement);
    if (index == -1) {
      return current;
    }

    while (true) {
      index++;
      if (index >= entries.size()) {
        index = 0;
      }
      Element nextElement = entries.get(index);
      if (nextElement == current) {
        return current;
      }
      if (nextElement.isFocusable()) {
        return nextElement;
      }
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

    int index = entries.indexOf(keyboardFocusElement);
    if (index == -1) {
      return current;
    }

    while (true) {
      index--;
      if (index < 0) {
        index = entries.size() - 1;
      }
      Element prevElement = entries.get(index);
      if (prevElement == current) {
        return current;
      }
      if (prevElement.isFocusable()) {
        return prevElement;
      }
    }
  }

  /**
   * remove this element.
   * @param element element
   */
  public void remove(final Element element) {
    entries.remove(element);
    lostKeyboardFocus(element);
    lostMouseFocus(element);
  }

  /**
   * get first entry.
   * @return first
   */
  public Element getFirstFocusElement() {
    if (entries.isEmpty()) {
      return null;
    }
    for (int i=0; i<entries.size(); i++) {
      if (entries.get(i).isFocusable()) {
        return entries.get(i);
      }
    }
    return null;
  }

  /**
   * save all states.
   */
  public void pushState() {
    ArrayList < Element > copy = new ArrayList < Element >();
    copy.addAll(entries);
    elementBuffer.add(copy);

    entries.clear();

    keyboardFocusElementBuffer.add(keyboardFocusElement);
    lostKeyboardFocus(keyboardFocusElement);

    mouseFocusElementBuffer.add(mouseFocusElement);
    lostMouseFocus(mouseFocusElement);
  }

  /**
   * restore all states.
   */
  public void popState() {
    entries.clear();
    entries.addAll(elementBuffer.get(elementBuffer.size() - 1));

    setKeyFocus(keyboardFocusElementBuffer.remove(keyboardFocusElementBuffer.size() - 1));
    mouseFocusElement = mouseFocusElementBuffer.remove(mouseFocusElementBuffer.size() - 1);

    // TODO: review this later for the moment let's just clear the mouseFocusElement
    //
    // background: at the moment a popup is opened there is a mouseFocusElement available with exclusive
    // access to the mouse. this element gets saved when the popup is opened and restored here. the mouse event
    // that informs the release of the mouse is long gone and leads to the effect that the mouse is still
    // exclusiv to this element.
    mouseFocusElement = null;
  }

  public void resetFocusElements() {
    entries.clear();
    lostKeyboardFocus(keyboardFocusElement);
    lostMouseFocus(mouseFocusElement);
  }

  /**
   * set the focus to the given element.
   * @param newFocusElement new focus element
   */
  public void setKeyFocus(final Element newFocusElement) {
    if (keyboardFocusElement == newFocusElement) {
      return;
    }

    if (keyboardFocusElement != null) {
      keyboardFocusElement.stopEffect(EffectEventId.onFocus);
      keyboardFocusElement.startEffect(EffectEventId.onLostFocus);
    }

    boolean startOnGetFocus = false;
    if (keyboardFocusElement != newFocusElement) {
      startOnGetFocus = true;
    }

    keyboardFocusElement = newFocusElement;
    log.fine("keyboard focus element now changed to [" + (keyboardFocusElement == null ? "" : keyboardFocusElement.toString()) + "]");

    if (keyboardFocusElement != null) {
      keyboardFocusElement.startEffect(EffectEventId.onFocus);
      if (startOnGetFocus) {
        keyboardFocusElement.startEffect(EffectEventId.onGetFocus);
      }
    }
  }

  public void lostKeyboardFocus(final Element elementThatLostFocus) {
    if (elementThatLostFocus != null) {
      log.fine("lostKeyboardFocus for [" + elementThatLostFocus.toString() + "]");
      if (keyboardFocusElement == elementThatLostFocus) {
        keyboardFocusElement.stopEffect(EffectEventId.onFocus);
        keyboardFocusElement.startEffect(EffectEventId.onLostFocus);
        keyboardFocusElement = null;
      }
    }
  }

  public boolean keyEvent(final KeyboardInputEvent inputEvent) {
    if (keyboardFocusElement != null) {
      return keyboardFocusElement.keyEvent(inputEvent);
    }
    return false;
  }

  public void requestExclusiveMouseFocus(final Element newFocusElement) {
    if (mouseFocusElement == newFocusElement) {
      return;
    }
    mouseFocusElement = newFocusElement;
    log.fine("requestExclusiveMouseFocus for [" + mouseFocusElement.toString() + "]");
  }

  public boolean hasExclusiveMouseFocus(final Element element) {
    return element.equals(mouseFocusElement);
  }

  public boolean canProcessMouseEvents(final Element element) {
    if (mouseFocusElement == null) {
      return true;
    }

    boolean canProcess = mouseFocusElement == element;
    log.fine(
        "canProcessMouseEvents for [" + element.toString() + "] ==> "
        + canProcess + " (" + mouseFocusElement.toString() + ")");
    return canProcess;
  }

  public void lostMouseFocus(final Element elementThatLostFocus) {
    if (elementThatLostFocus != null) {
      log.fine("lostMouseFocus for [" + elementThatLostFocus.toString() + "]");
      if (mouseFocusElement == elementThatLostFocus) {
        mouseFocusElement = null;
      }
    }
  }

  public String toString() {
    String mouseFocusString = "---";
    if (mouseFocusElement != null) {
      mouseFocusString = mouseFocusElement.toString();
    }

    String keyboardFocusString = "---";
    if (keyboardFocusElement != null) {
      keyboardFocusString = keyboardFocusElement.toString();
    }

    StringBuffer focusElements = new StringBuffer();
    for (int i=0; i<entries.size(); i++) {
      Element e = entries.get(i);
      if (i > 0) {
        focusElements.append(", ");
      }
      focusElements.append(e.getId() + (!e.isFocusable() ? "*" : ""));
    }
    return
      "\n"
       + "focus element (mouse):    " + mouseFocusString + "\n"
       + "focus element (keyboard): " + keyboardFocusString + "\n"
       + "focus element size: " + entries.size() + " [" + focusElements.toString() + "]";
  }

  public boolean hasAnyElementTheKeyboardFocus() {
    return keyboardFocusElement != null;
  }

  public boolean hasAnyElementTheMouseFocus() {
    return mouseFocusElement != null;
  }

  public Element findElement(final String defaultFocusElementId) {
    for (Element element : entries) {
      if (defaultFocusElementId.equals(element.getId())) {
        return element;
      }
    }
    return null;
  }

  public Element getKeyboardFocusElement() {
    return keyboardFocusElement;
  }
  
  public Element getMouseFocusElement() {
    return mouseFocusElement;
  }
}
