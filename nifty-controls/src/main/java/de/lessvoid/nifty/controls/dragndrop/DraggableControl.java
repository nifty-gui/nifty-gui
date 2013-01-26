package de.lessvoid.nifty.controls.dragndrop;

import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
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
  private boolean triedDragging = false;
  private int originalPositionX;
  private int originalPositionY;
  private SizeValue originalConstraintX;
  private SizeValue originalConstraintY;
  private int dragStartX;
  private int dragStartY;
  private Droppable droppable;

  /**
   * This flag stores if the draggable is supposed to be disabled once the dragging operation is done.
   */
  private boolean disableAtEndOfDrag;

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
  public void bringToFront(final int mouseX, final int mouseY) {
    if (!dragged) {
      moveToFront();
    }
  }

  /**
   * Called by the dragging function to start a dragging operation.
   * @param mouseX mouse x
   * @param mouseY mouse y
   */
  private void dragStart(final int mouseX, final int mouseY) {
    if (dragged || triedDragging) {
      return;
    }

    triedDragging = true;

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
      moveToFront();
    }
  }

  /**
   * Called by <interact onMouseMove="" /
   * @param mouseX mouse x
   * @param mouseY mouse y
   */
  public void drag(final int mouseX, final int mouseY) {
    if (!dragged) {
      dragStart(mouseX, mouseY);
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
    triedDragging = false;
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
    postDragActions();
  }

  private void moveDraggableToPopup() {
    popup = nifty.createPopup(POPUP);
    nifty.showPopup(screen, popup.getId(), null);

    draggable.markForMove(popup, new EndNotify() {
      @Override
      public void perform() {
        draggable.getFocusHandler().requestExclusiveMouseFocus(draggable);
        draggable.setConstraintX(new SizeValue(originalPositionX + "px"));
        draggable.setConstraintY(new SizeValue(originalPositionY + "px"));
      }
    });
  }

  @Override
  public void moveToFront() {
    final Element parent = draggable.getParent();
    final List<Element> siblings = parent.getElements();
    //noinspection ObjectEquality
    if (siblings.get(siblings.size() - 1) == draggable) {
      return;
    }
    draggable.markForMove(parent, new EndNotify() {
      @Override
      public void perform() {
        draggable.reactivate();
      }
    });
  }

  /**
   * Overwritten default disable function to ensure the proper deactivation of a currently dragged draggable.
   */
  @SuppressWarnings("RefusedBequest")
  @Override
  public void disable() {
    disable(true);
  }

  @Override
  public void disable(final boolean cancelCurrentDrag) {
    if (dragged) {
      if (cancelCurrentDrag) {
        dragCancel();
        dragged = false;
        super.disable();
      } else {
        disableAtEndOfDrag = true;
      }
    } else {
      super.disable();
    }
  }

  private void postDragActions() {
    if (disableAtEndOfDrag) {
      disableAtEndOfDrag = false;
      super.disable();
    }
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
        if (popup == null) {
          return;
        }
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

  public Droppable getDroppable() {
    return droppable;
  }

  public void setDroppable(final Droppable droppable) {
    this.droppable = droppable;
  }

  private void notifyObserversDragStarted() {
    nifty.publishEvent(getElement().getId(), new DraggableDragStartedEvent(droppable, this));
  }

  private void notifyObserversDragCanceled() {
    nifty.publishEvent(getElement().getId(), new DraggableDragCanceledEvent(droppable, this));
  }
}
