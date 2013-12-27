package de.lessvoid.nifty.examples.dragndrop;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.controls.window.builder.WindowBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class DragDropScreen implements ScreenController, NiftyExample {

  private Nifty nifty;
  private Screen screen;
  @Nonnull
  private final Random random = new Random();

  @Nullable
  private Droppable trash;
  @Nullable
  private Droppable evilStuff;

  @Override
  public void bind(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;

    trash = findDroppable("Trash");
    Droppable goodStuff = findDroppable("GoodStuff");
    evilStuff = findDroppable("EvilStuff");

    // this filter demonstrates a drop filter. in this case you can't drag something from the "EvilStuff"
    // dropable to the "GoodStuff" dropable
    goodStuff.addFilter(new DroppableDropFilter() {
      @Override
      public boolean accept(
          @Nullable final Droppable source,
          @Nonnull final Draggable draggable,
          @Nonnull final Droppable target) {
        return source != evilStuff;
      }
    });
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  /**
   * quit method called from the dragndrop.xml.
   */
  public final void quit() {
    nifty.setAlternateKeyForNextLoadXml("fade");
    nifty.fromXml("all/intro.xml", "menu");
  }

  public void spawnDraggable() {
    DraggableBuilder builder = new DraggableBuilder() {{
      width("120px");
      height("120px");
      backgroundColor(randomColor());
      childLayoutCenter();
      valignTop();
      text(new TextBuilder() {{
        text("Drag Me!");
        style("descriptionText");
      }});
    }};

    Element draggables = screen.findElementById("draggables");
    if (draggables != null) {
      builder.build(nifty, screen, draggables);
    }
  }

  public void spawnWindow() {
    Element windows = screen.findElementById("windows");
    if (windows == null) {
      throw new IllegalStateException("Window parent control not found.");
    }

    String windowId = NiftyIdCreator.generate() + 1000;
    final WindowBuilder windowBuilder = new WindowBuilder("window-" + windowId, "New Window [" + windowId + "]");
    windowBuilder.width("360px");
    windowBuilder.height("240px");
    windowBuilder.build(nifty, screen, windows);
  }

  @NiftyEventSubscriber(pattern = "window-.*")
  public void onAnyWindowClose(final String id, @Nonnull final WindowClosedEvent event) {
    setStatus("Window [" + id + "] " + (event.isHidden() ? "hidden" : "closed"));
  }

  @Nonnull
  private String randomColor() {
    return "#" + Integer.toHexString(random.nextInt(200)) + Integer.toHexString(random.nextInt(200)) + Integer
        .toHexString(random.nextInt(200)) + "ff";
  }

  /**
   * Called for all Draggables when the Drag operation starts.
   *
   * @param event the DraggableDragStartedEvent
   */
  @NiftyEventSubscriber(pattern = ".*")
  public void showDragStartStatus(final String id, @Nonnull final DraggableDragStartedEvent event) {
    setStatus("Dragging [" + getId(event.getDraggable()) + "] from [" + getId(event.getSource()) + "].");
  }

  /**
   * Called for all Draggables when the Drag operation stops.
   *
   * @param event the DraggableDragCanceledEvent
   */
  @NiftyEventSubscriber(pattern = ".*")
  public void showDragCancelStatus(final String id, @Nonnull final DraggableDragCanceledEvent event) {
    setStatus("Canceled [" + getId(event.getDraggable()) + "] reverting back to [" + getId(event.getSource()) + "].");
  }

  /**
   * Called for all Dropables when something is dropped on them.
   *
   * @param event the DropableDroppedEvent
   */
  @NiftyEventSubscriber(pattern = ".*")
  public void showDropStatus(final String id, @Nonnull final DroppableDroppedEvent event) {
    if ((event.getTarget() == trash) && (event.getSource() == evilStuff)) {
      setStatus("Evil [" + getId(event.getDraggable()) + "] has been eliminated.");
    } else if (event.getTarget() == evilStuff) {
      setStatus("[" + getId(event.getDraggable()) + "] has become evil");
    } else {
      setStatus("Dropped [" + getId(event.getDraggable()) + "] on [" + getId(event.getTarget()) + "].");
    }
  }

  /**
   * Called when something is dropped on the Trash.
   *
   * @param event the DropableDroppedEvent
   */
  @NiftyEventSubscriber(id = "Trash")
  public void onTrashDrop(final String id, @Nonnull final DroppableDroppedEvent event) {
    event.getDraggable().getElement().markForRemoval();
  }

  @Nullable
  private String getId(@Nullable final Droppable droppable) {
    return (droppable != null) ? droppable.getElement().getId() : null;
  }

  @Nullable
  private String getId(@Nullable final Draggable draggable) {
    return (draggable != null) ? draggable.getElement().getId() : null;
  }

  private void setStatus(final String text) {
    Label status = screen.findNiftyControl("status", Label.class);
    if (status != null) {
      status.setText(text);
    }
  }

  @Nonnull
  private Droppable findDroppable(final String id) {
    final Droppable droppable = screen.findNiftyControl(id, Droppable.class);
    if (droppable == null) {
      throw new IllegalArgumentException("Requested id " + id + " does not match a droppable.");
    }
    return droppable;
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "dragndrop/dragndrop.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Drag'n'Drop Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing
  }
}
