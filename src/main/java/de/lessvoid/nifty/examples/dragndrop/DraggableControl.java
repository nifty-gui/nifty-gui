package de.lessvoid.nifty.examples.dragndrop;

import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.EndNotify;
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
  private Element handle;
  private boolean revert;
  private boolean drop;
  
  private boolean dragged = false;
  private int originalPositionX;
  private int originalPositionY;
  private SizeValue originalConstraintX;
  private SizeValue originalConstraintY;
  private int dragStartX;
  private int dragStartY;
  
  @Override
  public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, ControllerEventListener listener, Attributes controlDefinitionAttributes) {
    this.nifty = nifty;
    this.screen = screen;
    draggable = element;
    
    String handleId= controlDefinitionAttributes.get("handle");
    handle = draggable.findElementByName(handleId);
    if (handle == null) 
      handle = draggable;
    revert = controlDefinitionAttributes.getAsBoolean("revert", true);
    drop = controlDefinitionAttributes.getAsBoolean("drop", true);
  }

  public void dragStart(final int mouseX, final int mouseY) {
    System.out.println("dragStart(" + dragged + ")");
    if (dragged)
      return;
    if (!handle.isMouseInsideElement(mouseX, mouseY))
      return;
    dragged = true;
    logger.fine(String.format("dragStart(mouseX=%s, mouseY=%s), draggable.id=%s, draggable.x=%s, draggable.y=%s", mouseX, mouseY, draggable.getId(), draggable.getX(), draggable.getY()));
    originalParent = draggable.getParent();
    originalPositionX = draggable.getX();
    originalPositionY = draggable.getY();
    originalConstraintX = draggable.getConstraintX();
    originalConstraintY = draggable.getConstraintY();
    dragStartX = mouseX;
    dragStartY = mouseY;

    popup = nifty.createPopup(POPUP);
    draggable.markForMove(popup, new EndNotify() {
      @Override
      public void perform() {
        draggable.setConstraintX(new SizeValue(originalPositionX + "px"));
        draggable.setConstraintY(new SizeValue(originalPositionY + "px"));
        // popup.layoutElements();
        nifty.showPopup(screen, POPUP, null);
      }
    });
  }

  public void drag(int mouseX, int mouseY) {
    if (!dragged)
      return;
    
    int newPositionX = originalPositionX + mouseX - dragStartX;
    int newPositionY = originalPositionY + mouseY - dragStartY;
    draggable.setConstraintX(new SizeValue(newPositionX + "px"));
    draggable.setConstraintY(new SizeValue(newPositionY + "px"));
    
    popup.layoutElements();
  }

  public void dragStop() {
    if (!dragged)
      return;
    logger.fine("dragStop()");
    Element droppable = findDroppableAtCurrentCoordinates();
    if (droppable == originalParent) {
      if (revert) {
        draggable.setConstraintX(originalConstraintX);
        draggable.setConstraintY(originalConstraintY);
      } else {
        SizeValue newConstraintX = translateValue(originalConstraintX, draggable.getX() - originalPositionX);
        SizeValue newConstraintY = translateValue(originalConstraintY, draggable.getY() - originalPositionY);
        draggable.setConstraintX(new SizeValue(draggable.getX() + "px"));
        draggable.setConstraintY(new SizeValue(draggable.getY() + "px"));
      }
    } else {
      draggable.setConstraintX(new SizeValue("0px"));
      draggable.setConstraintY(new SizeValue("0px"));
    }

    draggable.markForMove(droppable, new EndNotify() {
      @Override
      public void perform() {
        nifty.closePopup(POPUP, new EndNotify() {
          @Override
          public void perform() {
            draggable.reactivate();
          }
        });
      }
    });
    dragged = false;
  }
  
  private SizeValue translateValue(SizeValue value,
      int movement) {
    int oldValue = (value != null) ? value.getValueAsInt(1.0f) : 0;
    oldValue = 0;
    return new SizeValue(oldValue + movement +  "px");
  }

  private Element findDroppableAtCurrentCoordinates() {
    if (drop) {
      int dragAnkerX = draggable.getX() + draggable.getWidth() / 2;
      int dragAnkerY = draggable.getY() + draggable.getHeight() / 2;
      List<Element> layers = screen.getLayerElements();
      ListIterator<Element> iter = layers.listIterator(layers.size()); 
      while (iter.hasPrevious()) {
        Element layer = iter.previous();
        if (layer != popup) {
          Element droppable = findDroppableAtCoordinates(layer, dragAnkerX, dragAnkerY);
          if (droppable != null)
            return droppable;
        }
      }
    }
    return originalParent;
  }
  
  private Element findDroppableAtCoordinates(Element context, int x, int y) {
    List<Element> elements = context.getElements();
    ListIterator<Element> iter = elements.listIterator(elements.size());
    while (iter.hasPrevious()) {
      Element element = iter.previous();
      boolean mouseInside = element.isMouseInsideElement(x, y);
      if (mouseInside && isDroppable(element))
        return element;
      Element droppable = findDroppableAtCoordinates(element, x, y);
      if (droppable != null)
        return droppable;
      if (mouseInside) 
        return originalParent;
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
