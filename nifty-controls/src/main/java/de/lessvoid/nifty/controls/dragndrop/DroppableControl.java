package de.lessvoid.nifty.controls.dragndrop;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The controller class for a droppable element.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated Internal control class. Use {@link de.lessvoid.nifty.controls.Droppable}
 */
@Deprecated
public class DroppableControl extends AbstractController implements Droppable {
  @Nonnull
  private static final Logger log = Logger.getLogger(DroppableControl.class.getName());
  @Nullable
  private Nifty nifty;
  @Nullable
  private List<DroppableDropFilter> filters;
  @Nullable
  private Element droppableContent;
  @Nullable
  private DraggableControl draggable;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    super.bind(element);
    this.nifty = nifty;
    droppableContent = element.findElementById("#droppableContent");
    if (droppableContent == null) {
      log.severe("Failed to locate content area of the droppable. Droppable element will not work. Looked for: " +
          "#droppableContent");
    }
  }

  @Override
  public void onStartScreen() {
    if (droppableContent == null) {
      log.severe("No droppable content set. The binding failed or did not run at all.");
    } else {
      draggable = findDraggableChild(droppableContent);
      if (draggable != null) {
        Element draggableElement = draggable.getElement();
        if (draggableElement != null) {
          drop(draggable, reactivate(draggableElement), false);
        }
      }
    }
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @Nonnull
  private static EndNotify reactivate(@Nonnull final Element element) {
    return new EndNotify() {
      @Override
      public void perform() {
        element.reactivate();
      }
    };
  }

  @Nullable
  private static DraggableControl findDraggableChild(@Nonnull final Element element) {
    List<Element> children = element.getChildren();
    final int childrenCount = children.size();
    for (int i = 0; i < childrenCount; i++) {
      Element child = children.get(i);
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

  private static boolean isDraggable(@Nonnull final Element element) {
    final NiftyInputControl control = element.getAttachedInputControl();
    if (control != null) {
      return control.getController() instanceof Draggable;
    }
    return false;
  }

  protected void drop(@Nonnull final DraggableControl droppedDraggable, final EndNotify endNotify) {
    drop(droppedDraggable, endNotify, true);
  }

  private void drop(
      @Nonnull final DraggableControl droppedDraggable,
      @Nullable final EndNotify endNotify,
      final boolean notifyObservers) {
    if (droppableContent == null) {
      return;
    }
    draggable = droppedDraggable;
    Element draggableElement = draggable.getElement();
    if (draggableElement != null) {
      draggableElement.setConstraintX(SizeValue.px(0));
      draggableElement.setConstraintY(SizeValue.px(0));
      draggableElement.markForMove(droppableContent, endNotify);
    }

    final Droppable source = droppedDraggable.getDroppable();
    droppedDraggable.setDroppable(this);

    if (notifyObservers) {
      notifyObservers(source, droppedDraggable);
    }
  }

  @Nullable
  public DraggableControl getDraggable() {
    return draggable;
  }

  private void notifyObservers(@Nullable final Droppable source, @Nonnull final Draggable droppedDraggable) {
    if (nifty == null) {
      return;
    }
    String id = getId();
    if (id != null) {
      nifty.publishEvent(id, new DroppableDroppedEvent(source, droppedDraggable, this));
    }
  }

  @Override
  public void addFilter(@Nonnull final DroppableDropFilter filter) {
    if (filters == null) {
      filters = new ArrayList<DroppableDropFilter>();
    }
    filters.add(filter);
  }

  @Override
  public void removeFilter(@Nonnull final DroppableDropFilter filter) {
    if (filters != null) {
      filters.remove(filter);
      if (filters.isEmpty()) {
        filters = null;
      }
    }
  }

  @Override
  public void removeAllFilters() {
    filters = null;
  }

  protected boolean accept(@Nullable final Droppable source, @Nonnull final Draggable draggable) {
    if (filters == null) {
      return true;
    }

    final int filterCount = filters.size();
    for (int i = 0; i < filterCount; i++) {
      DroppableDropFilter filter = filters.get(i);
      if (!filter.accept(source, draggable, this)) {
        return false;
      }
    }
    return true;
  }
}
