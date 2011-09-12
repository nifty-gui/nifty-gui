package de.lessvoid.nifty.controls.dragndrop;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.DroppableDropFilter;
import de.lessvoid.nifty.controls.DroppableDroppedEvent;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class DroppableControl extends AbstractController implements Droppable {
  private Nifty nifty;
  private List<DroppableDropFilter> filters = new CopyOnWriteArrayList<DroppableDropFilter>();
  private Element droppable;
  private DraggableControl draggable;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    super.bind(element);
    this.nifty = nifty;
    droppable = element;
  }

  @Override
  public void onStartScreen() {
    draggable = findDraggableChild(droppable);
    if (draggable != null) {
      drop(draggable, reactivate(draggable.getElement()), false);
    }
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
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

  private void notifyObservers(final DroppableControl source, final DraggableControl droppedDraggable) {
    nifty.publishEvent(getElement().getId(), new DroppableDroppedEvent(source, droppedDraggable, this));
  }

  public void addFilter(final DroppableDropFilter filter) {
    filters.add(filter);
  }

  public void removeFilter(final DroppableDropFilter filter) {
    filters.remove(filter);
  }

  public void removeAllFilters() {
    filters.clear();
  }

  protected boolean accept(final DroppableControl source, final DraggableControl draggable) {
    for (DroppableDropFilter filter : filters) {
      if (!filter.accept(source, draggable, this)) {
        return false;
      }
    }
    return true;
  }
}
