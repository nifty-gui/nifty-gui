package de.lessvoid.nifty.controls.dragndrop;

import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Draggable;
import de.lessvoid.nifty.controls.DraggableDragCanceledEvent;
import de.lessvoid.nifty.controls.DraggableDragStartedEvent;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class DraggableControl extends AbstractController implements Draggable {
  private static final String POPUP = "draggablePopup";
  private static Logger logger = Logger.getLogger(DraggableControl.class.getName());
  private Nifty nifty;
  private Screen screen;
  private Element draggable;
  private Element originalParent;
  private Element popup;
  private Element handle;
  private boolean revert;
  private boolean dropEnabled;
  private boolean dragged = false;  
  private int originalPositionX;
  private int originalPositionY;
  private SizeValue originalConstraintX;
  private SizeValue originalConstraintY;
  private int dragStartX;
  private int dragStartY;
  private DroppableControl droppable;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    super.bind(element);
    this.nifty = nifty;
    this.screen = screen;
    this.draggable = element;

    String handleId = controlDefinitionAttributes.get("handle");
    handle = draggable.findElementByName(handleId);
    if (handle == null) {
      handle = draggable;
    }
    revert = controlDefinitionAttributes.getAsBoolean("revert", true);
    dropEnabled = controlDefinitionAttributes.getAsBoolean("drop", true);
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  /**
   * Called by <interact onClick="" />
   * @param mouseX mouse x
   * @param mouseY mouse y
   */
  public void dragStart(final int mouseX, final int mouseY) {
    if (dragged) {
      return;
    }

    originalParent = draggable.getParent();
    originalPositionX = draggable.getX();
    originalPositionY = draggable.getY();
    originalConstraintX = draggable.getConstraintX();
    originalConstraintY = draggable.getConstraintY();
    dragStartX = mouseX;
    dragStartY = mouseY;

    if (handle.isMouseInsideElement(mouseX, mouseY)) {
      moveDraggableToPopup();
      dragged = true;
      notifyObserversDragStarted();
    } else {
      moveDraggableOnTop();
    }
  }

  /**
   * Called by <interact onMouseMove="" /
   * @param mouseX mouse x
   * @param mouseY mouse y
   */
  public void drag(final int mouseX, final int mouseY) {
    if (!dragged) {
      return;
    }

    int newPositionX = originalPositionX + mouseX - dragStartX;
    int newPositionY = originalPositionY + mouseY - dragStartY;
    draggable.setConstraintX(new SizeValue(newPositionX + "px"));
    draggable.setConstraintY(new SizeValue(newPositionY + "px"));

    popup.layoutElements();
  }

  /**
   * Called by <interact onRelease="" />
   */
  public void dragStop() {
    if (!dragged) {
      return;
    }
    logger.fine("dragStop()");
    Element newDroppable = findDroppableAtCurrentCoordinates();
    if (newDroppable == originalParent) {
      dragCancel();
    } else {
      DroppableControl droppableControl = newDroppable.getControl(DroppableControl.class);
      if (droppableControl.accept(droppable, this)) {
        droppableControl.drop(this, closePopup());
      } else {
        dragCancel();
      }
    }
    dragged = false;
  }

  private void moveDraggableToPopup() {
    popup = nifty.createPopup(POPUP);
    nifty.showPopup(screen, popup.getId(), null);

    draggable.markForMove(popup, new EndNotify() {
      @Override
      public void perform() {
        draggable.setConstraintX(new SizeValue(originalPositionX + "px"));
        draggable.setConstraintY(new SizeValue(originalPositionY + "px"));
        draggable.getParent().layoutElements();
      }
    });
  }

  private void moveDraggableOnTop() {
    draggable.markForMove(originalParent, new EndNotify() {
      @Override
      public void perform() {
        draggable.reactivate();
      }
    });
  }

  private void dragCancel() {
    if (revert) {
      draggable.setConstraintX(originalConstraintX);
      draggable.setConstraintY(originalConstraintY);
    } else {
      draggable.setConstraintX(new SizeValue(draggable.getX() + "px"));
      draggable.setConstraintY(new SizeValue(draggable.getY() + "px"));
    }
    moveDraggableBackToOriginalParent();
    notifyObserversDragCanceled();
  }

  private EndNotify closePopup() {
    return new EndNotify() {
      @Override
      public void perform() {
        nifty.closePopup(popup.getId(), new EndNotify() {
          @Override
          public void perform() {
            draggable.reactivate();
            popup.markForRemoval(new EndNotify() {
              @Override
              public void perform() {
                popup = null;
              }
            });
          }
        });
      }
    };
  }

  private void moveDraggableBackToOriginalParent() {
    draggable.markForMove(originalParent, closePopup());
  }

  private Element findDroppableAtCurrentCoordinates() {
    if (dropEnabled) {
      int dragAnkerX = draggable.getX() + draggable.getWidth() / 2;
      int dragAnkerY = draggable.getY() + draggable.getHeight() / 2;
      List<Element> layers = screen.getLayerElements();
      ListIterator<Element> iter = layers.listIterator(layers.size());
      while (iter.hasPrevious()) {
        Element layer = iter.previous();
        if (popup != null && layer != popup) {
          Element droppable = findDroppableAtCoordinates(layer, dragAnkerX, dragAnkerY);
          if (droppable != null) {
            return droppable;
          }
        }
      }
    }
    return originalParent;
  }

  private Element findDroppableAtCoordinates(final Element context, final int x, final int y) {
    List<Element> elements = context.getElements();
    ListIterator<Element> iter = elements.listIterator(elements.size());
    while (iter.hasPrevious()) {
      Element element = iter.previous();

      // we can only drop stuff on visible droppables
      boolean mouseInsideAndVisible = element.isMouseInsideElement(x, y) && element.isVisible();
      if (mouseInsideAndVisible && isDroppable(element)) {
        return element;
      }

      // nothing found for this element check it's child elements
      Element droppable = findDroppableAtCoordinates(element, x, y);
      if (droppable != null) {
        return droppable;
      }
    }
    return null;
  }

  private boolean isDroppable(final Element element) {
    NiftyInputControl control = element.getAttachedInputControl();
    if (control != null) {
      return control.getController() instanceof DroppableControl;
    }
    return false;
  }

  public DroppableControl getDroppable() {
    return droppable;
  }

  protected void setDroppable(final DroppableControl droppable) {
    this.droppable = droppable;
  }

  private void notifyObserversDragStarted() {
    nifty.publishEvent(getElement().getId(), new DraggableDragStartedEvent(droppable, this));
  }

  private void notifyObserversDragCanceled() {
    nifty.publishEvent(getElement().getId(), new DraggableDragCanceledEvent(droppable, this));
  }
}
