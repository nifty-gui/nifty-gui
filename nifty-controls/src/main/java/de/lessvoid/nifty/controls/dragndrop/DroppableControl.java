package de.lessvoid.nifty.controls.dragndrop;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Draggable;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.DroppableDropFilter;
import de.lessvoid.nifty.controls.DroppableDroppedEvent;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

public class DroppableControl extends AbstractController implements Droppable {
  private Nifty nifty;
  private Collection<DroppableDropFilter> filters = new CopyOnWriteArrayList<DroppableDropFilter>();
  private Element droppableContent;
  private DraggableControl draggable;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Parameters parameter) {
    super.bind(element);
    this.nifty = nifty;
    droppableContent = element.findElementById("#droppableContent");
  }

  @Override
  public void onStartScreen() {
    draggable = findDraggableChild(droppableContent);
    if (draggable != null) {
      drop(draggable, reactivate(draggable.getElement()), false);
    }
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  private static EndNotify reactivate(final Element element) {
    return new EndNotify() {
      @Override
      public void perform() {
        element.reactivate();
      }
    };
  }

  private static DraggableControl findDraggableChild(final Element element) {
    for (final Element child : element.getChildren()) {
      if (isDraggable(child)) {
        return child.getControl(DraggableControl.class);
      }
      final DraggableControl draggable = findDraggableChild(child);
      if (draggable != null) {
        return draggable;
      }
    }
    return null;
  }

  private static boolean isDraggable(final Element element) {
    final NiftyInputControl control = element.getAttachedInputControl();
    if (control != null) {
      return control.getController() instanceof Draggable;
    }
    return false;
  }

  protected void drop(final DraggableControl droppedDraggable, final EndNotify endNotify) {
    drop(droppedDraggable, endNotify, true);
  }

  private void drop(final DraggableControl droppedDraggable, final EndNotify endNotify, final boolean notifyObservers) {
    draggable = droppedDraggable;
    draggable.getElement().setConstraintX(SizeValue.px(0));
    draggable.getElement().setConstraintY(SizeValue.px(0));
    draggable.getElement().markForMove(droppableContent, endNotify);

    final Droppable source = droppedDraggable.getDroppable();
    droppedDraggable.setDroppable(this);

    if (notifyObservers) {
      notifyObservers(source, droppedDraggable);
    }
  }

  public DraggableControl getDraggable() {
    return draggable;
  }

  private void notifyObservers(final Droppable source, final Draggable droppedDraggable) {
    nifty.publishEvent(getElement().getId(), new DroppableDroppedEvent(source, droppedDraggable, this));
  }

  @Override
  public void addFilter(final DroppableDropFilter filter) {
    filters.add(filter);
  }

  @Override
  public void removeFilter(final DroppableDropFilter filter) {
    filters.remove(filter);
  }

  @Override
  public void removeAllFilters() {
    filters.clear();
  }

  protected boolean accept(final Droppable source, final Draggable draggable) {
    for (final DroppableDropFilter filter : filters) {
      if (!filter.accept(source, draggable, this)) {
        return false;
      }
    }
    return true;
  }
}
