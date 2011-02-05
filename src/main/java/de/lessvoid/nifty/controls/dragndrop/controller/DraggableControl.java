package de.lessvoid.nifty.controls.dragndrop.controller;

import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class DraggableControl extends AbstractController {
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
  private List<DragNotify> notifies = new CopyOnWriteArrayList<DragNotify>();
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
    this.nifty = nifty;
    this.screen = screen;
    draggable = element;

    String handleId = controlDefinitionAttributes.get("handle");
    handle = draggable.findElementByName(handleId);
    if (handle == null) {
      handle = draggable;
    }
    revert = controlDefinitionAttributes.getAsBoolean("revert", true);
    dropEnabled = controlDefinitionAttributes.getAsBoolean("drop", true);

    addOnDragStartMethodNotify(controlDefinitionAttributes.get("onDragStart"));
    addOnDragCancelMethodNotify(controlDefinitionAttributes.get("onDragCancel"));
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  private void addOnDragStartMethodNotify(final String methodName) {
    if (methodName != null) {
      addNotify(new OnDragStartMethodNotify(methodName));
    }
  }

  private void addOnDragCancelMethodNotify(final String methodName) {
    if (methodName != null) {
      addNotify(new OnDragCancelMethodNotify(methodName));
    }
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
  }

  @Override
  public void onStartScreen() {
  }

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

  private void moveDraggableToPopup() {
    popup = nifty.createPopup(POPUP);

    draggable.markForMove(popup, new EndNotify() {
      @Override
      public void perform() {
        draggable.setConstraintX(new SizeValue(originalPositionX + "px"));
        draggable.setConstraintY(new SizeValue(originalPositionY + "px"));
        nifty.showPopup(screen, POPUP, null);
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
        nifty.closePopup(POPUP, new EndNotify() {
          @Override
          public void perform() {
            draggable.reactivate();
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
        if (layer != popup) {
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
      boolean mouseInside = element.isMouseInsideElement(x, y);
      if (mouseInside && isDroppable(element)) {
        return element;
      }
      Element droppable = findDroppableAtCoordinates(element, x, y);
      if (droppable != null) {
        return droppable;
      }
      if (mouseInside && element.isVisibleToMouseEvents()) {
        return originalParent;
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

  public Element getElement() {
    return draggable;
  }

  public DroppableControl getDroppable() {
    return droppable;
  }

  protected void setDroppable(final DroppableControl droppable) {
    this.droppable = droppable;
  }

  public void addNotify(final DragNotify notify) {
    notifies.add(notify);
  }

  public void removeNotify(final DragNotify notify) {
    notifies.remove(notify);
  }

  @Override
  public void removeAllNotifies() {
    notifies.clear();
  }

  private void notifyObserversDragStarted() {
    for (DragNotify notify : notifies) {
      notify.dragStarted(droppable, this);
    }
  }

  private void notifyObserversDragCanceled() {
    for (DragNotify notify : notifies) {
      notify.dragCanceled(droppable, this);
    }
  }

  private class OnDragStartMethodNotify implements DragNotify {

    private String methodName;

    public OnDragStartMethodNotify(final String methodName) {
      this.methodName = methodName;
    }

    @Override
    public void dragStarted(final DroppableControl source, final DraggableControl draggable) {
      NiftyMethodInvoker methodInvoker = new NiftyMethodInvoker(nifty, methodName, screen.getScreenController());
      methodInvoker.invoke(source, draggable);
    }

    @Override
    public void dragCanceled(final DroppableControl source, final DraggableControl draggable) {
    }
  }

  private class OnDragCancelMethodNotify implements DragNotify {

    private String methodName;

    public OnDragCancelMethodNotify(final String methodName) {
      this.methodName = methodName;
    }

    @Override
    public void dragStarted(final DroppableControl source, final DraggableControl draggable) {
    }

    @Override
    public void dragCanceled(final DroppableControl source, final DraggableControl draggable) {
      NiftyMethodInvoker methodInvoker = new NiftyMethodInvoker(nifty, methodName, screen.getScreenController());
      methodInvoker.invoke(source, draggable);
    }
  }
}
