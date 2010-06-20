package de.lessvoid.nifty.examples.dragndrop;

import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class DraggableControl implements Controller {
  private static final String POPUP = "draggablePopup";
  private static final Logger logger = Logger.getLogger(DraggableControl.class.getName());
  private Nifty nifty;
  private Screen screen;
  private Element draggable;
  private Element originalParent;
  private Element popup;
  private boolean dragged = false;
  private int originalPositionX;
  private int originalPositionY;
  private int dragStartX;
  private int dragStartY;
  
  @Override
  public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, ControllerEventListener listener, Attributes controlDefinitionAttributes) {
    this.nifty = nifty;
    this.screen = screen;
    draggable = element;
  }
  
  public void dragStart(int mouseX, int mouseY) {
    if (dragged)
      return;
    dragged = true;
    logger.fine(String.format("dragStart(mouseX=%s, mouseY=%s), draggable.id=%s, draggable.x=%s, draggable.y=%s", mouseX, mouseY, draggable.getId(), draggable.getX(), draggable.getY()));
    originalParent = draggable.getParent();
    originalPositionX = draggable.getX();
    originalPositionY = draggable.getY();
    dragStartX = mouseX;
    dragStartY = mouseY;
//    nifty.removeElement(screen, draggable);
    popup = nifty.createPopup(POPUP);
    popup.add(draggable);
    nifty.showPopup(screen, POPUP, null);
  }
  
  public void drag(int mouseX, int mouseY) {
    if (!dragged)
      return;
    logger.fine(String.format("drag(mouseX=%s, mouseY=%s)", mouseX, mouseY));
    int newPositionX = originalPositionX + mouseX - dragStartX;
    int newPositionY = originalPositionY + mouseY - dragStartY;
    draggable.setConstraintX(new SizeValue(newPositionX + "px"));
    draggable.setConstraintY(new SizeValue(newPositionY + "px"));
    popup.layoutElements();
    logger.info("popup: " + popup.getId());
    logger.info(String.format("drag() newPos.x=%s, newPos.y=%s", newPositionX, newPositionY));
    logger.info(String.format("drag() element.x=%s, element.y=%s", draggable.getX(), draggable.getY()));
  }
  
  public void dragStop() {
    if (!dragged)
      return;
    logger.fine("dragStop()");
    // logger.fine(String.format("dragStop(mouseX=%s, mouseY=%s)", mouseX,
    // mouseY));
    Element droppable = findDroppableAtCurrentCoordinates();
    if (droppable == originalParent) {
      draggable.setConstraintX(new SizeValue(originalPositionX + "px"));
      draggable.setConstraintY(new SizeValue(originalPositionY + "px"));
    } else {
      draggable.setConstraintX(new SizeValue(droppable.getX() + "px"));
      draggable.setConstraintY(new SizeValue(droppable.getY() + "px"));
    }
    droppable.add(draggable);
    droppable.layoutElements();
    // element.setVisibleToMouseEvents(true);
    nifty.closePopup(POPUP);
    dragged = false;
  }
  
  private Element findDroppableAtCurrentCoordinates() {
    int dragAnkerX = draggable.getX() + draggable.getWidth() / 2;
    int dragAnkerY = draggable.getY() + draggable.getHeight() / 2;
    for (Element layer : screen.getLayerElements()) {
      Element droppable = findDroppableAtCoordinates(layer, dragAnkerX, dragAnkerY);
      if (droppable != null)
        return droppable;
    }
    return originalParent;
  }
  
  private Element findDroppableAtCoordinates(Element context, int x, int y) {
    for (Element element : context.getElements())
      if (isDroppable(element) && element.isMouseInsideElement(x, y))
        return element;
      else {
        Element droppable = findDroppableAtCoordinates(element, x, y);
        if (droppable != null)
          return droppable;
      }
    return null;
  }
  
  private boolean isDroppable(Element element) {
    NiftyInputControl control = element.getAttachedInputControl();
    if (control != null)
      return control.getController() instanceof DroppableControl;
    return false;
  }
  
  @Override
  public void inputEvent(NiftyInputEvent inputEvent) {
  }
  
  @Override
  public void onFocus(boolean getFocus) {
  }
  
  @Override
  public void onStartScreen() {
  }
}
