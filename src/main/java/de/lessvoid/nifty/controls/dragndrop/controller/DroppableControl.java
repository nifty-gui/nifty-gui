package de.lessvoid.nifty.controls.dragndrop.controller;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

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

public class DroppableControl extends AbstractController {
  private Nifty nifty;
  private Screen screen;
  private List<DropNotify> notifies = new CopyOnWriteArrayList<DropNotify>();
  private List<DropFilter> filters = new CopyOnWriteArrayList<DropFilter>();
  private Element droppable;
  private DraggableControl draggable;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.nifty = nifty;
    this.screen = screen;
    droppable = element;

    addOnDropMethodNotify(controlDefinitionAttributes.get("onDrop"));
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  private void addOnDropMethodNotify(final String methodName) {
    if (methodName != null) {
      addNotify(new OnDropMethodNotify(methodName));
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
    draggable = findDraggableChild(droppable);
    if (draggable != null) {
      drop(draggable, reactivate(draggable.getElement()), false);
    }
  }

  private EndNotify reactivate(final Element element) {
    return new EndNotify() {

      @Override
      public void perform() {
        element.reactivate();
      }
    };
  }

  private DraggableControl findDraggableChild(final Element element) {
    for (Element child : element.getElements()) {
      if (isDraggable(child)) {
        return child.getControl(DraggableControl.class);
      }
      DraggableControl draggable = findDraggableChild(child);
      if (draggable != null) {
        return draggable;
      }
    }
    return null;
  }

  private boolean isDraggable(final Element element) {
    NiftyInputControl control = element.getAttachedInputControl();
    if (control != null) {
      return control.getController() instanceof DraggableControl;
    }
    return false;
  }

  protected void drop(final DraggableControl droppedDraggable, final EndNotify endNotify) {
    drop(droppedDraggable, endNotify, true);
  }

  private void drop(final DraggableControl droppedDraggable, final EndNotify endNotify, final boolean notifyObservers) {
    draggable = droppedDraggable;
    draggable.getElement().setConstraintX(new SizeValue("0px"));
    draggable.getElement().setConstraintY(new SizeValue("0px"));
    draggable.getElement().markForMove(droppable, endNotify);

    DroppableControl source = droppedDraggable.getDroppable();
    droppedDraggable.setDroppable(this);

    if (notifyObservers) {
      notifyObservers(source, droppedDraggable);
    }
  }

  public DraggableControl getDraggable() {
    return draggable;
  }

  public void addNotify(final DropNotify notify) {
    notifies.add(notify);
  }

  public void removeNotify(final DropNotify notify) {
    notifies.remove(notify);
  }

  @Override
  public void removeAllNotifies() {
    notifies.clear();
  }

  private void notifyObservers(final DroppableControl source, final DraggableControl droppedDraggable) {
    for (DropNotify notify : notifies) {
      notify.dropped(source, droppedDraggable, this);
    }
  }

  public void addFilter(final DropFilter filter) {
    filters.add(filter);
  }

  public void removeFilter(final DropFilter filter) {
    filters.remove(filter);
  }

  public void removeAllFilters() {
    filters.clear();
  }

  protected boolean accept(final DroppableControl source, final DraggableControl draggable) {
    for (DropFilter filter : filters) {
      if (!filter.accept(source, draggable, this)) {
        return false;
      }
    }
    return true;
  }

  public Element getElement() {
    return droppable;
  }

  private class OnDropMethodNotify implements DropNotify {

    private String methodName;

    public OnDropMethodNotify(final String methodName) {
      this.methodName = methodName;
    }

    @Override
    public void dropped(final DroppableControl source, final DraggableControl draggable, final DroppableControl target) {
      NiftyMethodInvoker methodInvoker = new NiftyMethodInvoker(nifty, methodName, screen.getScreenController());
      methodInvoker.invoke(source, draggable, target);
    }
  }
}
