package de.lessvoid.nifty.controls.dragndrop;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Draggable;
import de.lessvoid.nifty.controls.DraggableDragCanceledEvent;
import de.lessvoid.nifty.controls.DraggableDragStartedEvent;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.logging.Logger;

/**
 * The controller class for a draggable element.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated Internal control class. Use {@link de.lessvoid.nifty.controls.Draggable}
 */
@Deprecated
public class DraggableControl extends AbstractController implements Draggable {
  @Nonnull
  private static final String POPUP = "draggablePopup";
  @Nonnull
  private static final Logger log = Logger.getLogger(DraggableControl.class.getName());
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;
  @Nullable
  private Element originalParent;
  @Nullable
  private Element popup;
  @Nullable
  private Element handle;
  private boolean revert;
  private boolean dropEnabled;
  private boolean dragged = false;
  private boolean triedDragging = false;
  private int originalPositionX;
  private int originalPositionY;
  @Nonnull
  private SizeValue originalConstraintX;
  @Nonnull
  private SizeValue originalConstraintY;
  private int dragStartX;
  private int dragStartY;
  @Nullable
  private Droppable droppable;

  /**
   * This flag stores if the draggable is supposed to be disabled once the dragging operation is done.
   */
  private boolean disableAtEndOfDrag;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    super.bind(element);
    this.nifty = nifty;
    this.screen = screen;

    String handleId = parameter.get("handle");
    handle = element.findElementById(handleId);
    if (handle == null) {
      handle = element;
    }
    revert = parameter.getAsBoolean("revert", true);
    dropEnabled = parameter.getAsBoolean("drop", true);
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  /**
   * Called by &lt;interact onClick="" /&gt;
   */
  public void bringToFront() {
    if (!dragged) {
      moveToFront();
    }
  }

  /**
   * Called by the dragging function to start a dragging operation.
   *
   * @param mouseX mouse x
   * @param mouseY mouse y
   */
  private void dragStart(final int mouseX, final int mouseY) {
    if (dragged || triedDragging) {
      return;
    }

    triedDragging = true;

    Element draggable = getElement();
    if (draggable == null) {
      return;
    }

    originalParent = draggable.getParent();
    originalPositionX = draggable.getX();
    originalPositionY = draggable.getY();
    originalConstraintX = draggable.getConstraintX();
    originalConstraintY = draggable.getConstraintY();
    dragStartX = mouseX;
    dragStartY = mouseY;

    if (handle != null && handle.isMouseInsideElement(mouseX, mouseY)) {
      moveDraggableToPopup();
      dragged = true;
      notifyObserversDragStarted();
    } else {
      moveToFront();
    }
  }

  /**
   * Called by &lt;interact onMouseMove="" /&gt;
   *
   * @param mouseX mouse x
   * @param mouseY mouse y
   */
  public void drag(final int mouseX, final int mouseY) {
    if (!dragged) {
      dragStart(mouseX, mouseY);
      return;
    }

    Element draggable = getElement();
    if (draggable == null) {
      return;
    }

    int newPositionX = originalPositionX + mouseX - dragStartX;
    int newPositionY = originalPositionY + mouseY - dragStartY;
    draggable.setConstraintX(SizeValue.px(newPositionX));
    draggable.setConstraintY(SizeValue.px(newPositionY));

    if (popup != null) {
      popup.layoutElements();
    }
  }

  /**
   * Called by &lt;interact onRelease="" /&gt;
   */
  public void dragStop() {
    triedDragging = false;
    if (!dragged) {
      moveToFront();
      return;
    }
    Element newDroppable = findDroppableAtCurrentCoordinates();
    if (newDroppable == null || newDroppable == originalParent) {
      dragCancel();
    } else {
      DroppableControl droppableControl = newDroppable.getControl(DroppableControl.class);
      if (droppableControl == null) {
        log.warning("Droppable has no Droppable control. Corrupted controls! Canceling drag.");
        dragCancel();
      } else if (droppableControl.accept(droppable, this)) {
        droppableControl.drop(this, closePopup());
      } else {
        dragCancel();
      }
    }
    dragged = false;
    postDragActions();
  }

  private void moveDraggableToPopup() {
    if (nifty == null || screen == null) {
      return;
    }
    final Element draggable = getElement();
    if (draggable == null) {
      return;
    }
    popup = nifty.createPopup(screen, POPUP);
    String popupId = popup.getId();
    if (popupId == null) {
      log.severe("Newly created popup did not get a ID. Critical internal error. Popup creation failed.");
      return;
    }
    nifty.showPopup(screen, popupId, null);

    draggable.markForMove(popup, new EndNotify() {
      @Override
      public void perform() {
        draggable.getFocusHandler().requestExclusiveMouseFocus(draggable);
        draggable.setConstraintX(SizeValue.px(originalPositionX));
        draggable.setConstraintY(SizeValue.px(originalPositionY));
      }
    });
  }

  @Override
  public void moveToFront() {
    final Element draggable = getElement();
    if (draggable == null) {
      return;
    }
    final Element parent = draggable.getParent();
    final List<Element> siblings = parent.getChildren();
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
    final Element draggable = getElement();
    if (draggable == null) {
      return;
    }
    if (revert) {
      draggable.setConstraintX(originalConstraintX);
      draggable.setConstraintY(originalConstraintY);
    } else {
      draggable.setConstraintX(SizeValue.px(draggable.getX()));
      draggable.setConstraintY(SizeValue.px(draggable.getY()));
    }
    moveDraggableBackToOriginalParent();
    notifyObserversDragCanceled();
  }

  @Nullable
  private EndNotify closePopup() {
    return new EndNotify() {
      @Override
      public void perform() {
        if (popup == null || nifty == null) {
          return;
        }
        final Element draggable = getElement();
        if (draggable == null) {
          return;
        }
        String popupId = popup.getId();
        if (popupId == null) {
          return;
        }
        nifty.closePopup(popupId, new EndNotify() {
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
    if (originalParent == null) {
      return;
    }
    final Element draggable = getElement();
    if (draggable == null) {
      return;
    }
    draggable.markForMove(originalParent, closePopup());
  }

  @Nullable
  private Element findDroppableAtCurrentCoordinates() {
    if (screen == null) {
      return null;
    }
    if (dropEnabled) {
      final Element draggable = getElement();
      if (draggable == null) {
        return null;
      }
      int dragAnkerX = draggable.getX() + draggable.getWidth() / 2;
      int dragAnkerY = draggable.getY() + draggable.getHeight() / 2;
      List<Element> layers = screen.getLayerElements();
      final int layerCount = layers.size();
      for (int i = layerCount - 1; i >= 0; i--) {
        Element layer = layers.get(i);
        if ((popup != null) && (layer != popup)) {
          DroppableSearchResult droppableSearchResult = findDroppableAtCoordinates(layer, dragAnkerX, dragAnkerY);
          if (droppableSearchResult != null) {
            if (droppableSearchResult.getFoundDroppable() != null) {
              // found a droppable!
              return droppableSearchResult.getFoundDroppable();
            }
            // found no droppable, but the mouse was blocked.
            break;
          }
        }
      }
    }
    return originalParent;
  }

  /**
   * This function looks for a valid foundDroppable target on a specified screen position.
   *
   * @param context the element that is the root element to the search
   * @param x the x component of the screen coordinate
   * @param y the y component of the screen coordinate
   * @return {@code null} in case nothing was found and the search should continue with the next element if any or a
   * search result, in that case the search is supposed to be stopped.
   */
  @Nullable
  private DroppableSearchResult findDroppableAtCoordinates(@Nonnull final Element context, final int x, final int y) {
    List<Element> elements = context.getChildren();
    final int childCount = elements.size();
    for (int i = childCount - 1; i >= 0; i--) {
      Element element = elements.get(i);

      // Check if the element we are testing is visible and in covers the tested location
      boolean mouseInsideAndVisible = element.isVisibleWithParent() && element.isMouseInsideElement(x, y);
      if (mouseInsideAndVisible && isDroppable(element)) {
        // its also a droppable. Our search is over.
        return new DroppableSearchResult(element);
      }

      // nothing found for this element check it's child elements
      DroppableSearchResult searchResult = findDroppableAtCoordinates(element, x, y);
      if (searchResult != null) {
        // search at the child returned a result. We are done.
        return searchResult;
      }
      if (mouseInsideAndVisible && element.isVisibleToMouseEvents()) {
        // we did not find a result, how ever we found a element that blocks the mouse. Stop the search without result.
        return new DroppableSearchResult();
      }
    }
    // No results at all. Continue the search with the other elements/layers.
    return null;
  }

  private boolean isDroppable(@Nonnull final Element element) {
    NiftyInputControl control = element.getAttachedInputControl();
    if (control != null) {
      return control.getController() instanceof DroppableControl;
    }
    return false;
  }

  @Nullable
  public Droppable getDroppable() {
    return droppable;
  }

  @Override
  public void setDroppable(@Nullable final Droppable droppable) {
    this.droppable = droppable;
  }

  private void notifyObserversDragStarted() {
    if (nifty == null || droppable == null) {
      return;
    }
    String id = getId();
    if (id != null) {
      nifty.publishEvent(id, new DraggableDragStartedEvent(droppable, this));
    }
  }

  private void notifyObserversDragCanceled() {
    if (nifty == null || droppable == null) {
      return;
    }
    String id = getId();
    if (id != null) {
      nifty.publishEvent(id, new DraggableDragCanceledEvent(droppable, this));
    }
  }

  private class DroppableSearchResult {
    @Nullable
    private final Element foundDroppable;

    DroppableSearchResult() {
      this(null);
    }

    DroppableSearchResult(@Nullable Element foundDroppable) {
      this.foundDroppable = foundDroppable;
    }

    @Nullable
    public Element getFoundDroppable() {
      return foundDroppable;
    }
  }
}
