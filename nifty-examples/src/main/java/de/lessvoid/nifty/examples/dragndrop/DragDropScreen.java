package de.lessvoid.nifty.examples.dragndrop;

import java.util.Random;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Draggable;
import de.lessvoid.nifty.controls.DraggableDragCanceledEvent;
import de.lessvoid.nifty.controls.DraggableDragStartedEvent;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.DroppableDropFilter;
import de.lessvoid.nifty.controls.DroppableDroppedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.WindowClosedEvent;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.controls.window.builder.CreateWindow;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class DragDropScreen implements ScreenController, NiftyExample {

  private Nifty nifty;
  private Screen screen;
  private Random random = new Random();
  
  private Droppable trash;
  private Droppable goodStuff;
  private Droppable evilStuff;

  @Override
  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;

    trash = findDroppable("Trash");
    goodStuff = findDroppable("GoodStuff");
    evilStuff = findDroppable("EvilStuff");
    
    // this filter demonstrates a drop filter. in this case you can't drag something from the "EvilStuff"
    // dropable to the "GoodStuff" dropable
    goodStuff.addFilter(new DroppableDropFilter() {
      @Override
      public boolean accept(final Droppable source, final Draggable draggable, final Droppable target) {
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

    Element draggables = screen.findElementByName("draggables");
    builder.build(nifty, screen, draggables);
  }
  
  public void spawnWindow() {
    Element windows = screen.findElementByName("windows");

    String windowId = NiftyIdCreator.generate() + 1000;
    CreateWindow windowAttributes = new CreateWindow("window-" + windowId, "New Window [" + windowId + "]");
    windowAttributes.setWidth("360px");
    windowAttributes.setHeight("240px");
    windowAttributes.create(nifty, screen, windows);
  }

  @NiftyEventSubscriber(pattern="window-.*")
  public void onAnyWindowClose(final String id, final WindowClosedEvent event) {
    setStatus("Window [" + id + "] " + (event.isHidden() ? "hidden" : "closed"));
  }

  private String randomColor() {
    return "#" + Integer.toHexString(random.nextInt(200)) + Integer.toHexString(random.nextInt(200)) + Integer.toHexString(random.nextInt(200)) + "ff";
  }
  
  /**
   * Called for all Draggables when the Drag operation starts.
   * @param event the DraggableDragStartedEvent
   */
  @NiftyEventSubscriber(pattern=".*")
  public void showDragStartStatus(final String id, final DraggableDragStartedEvent event) {
    setStatus("Dragging [" + getId(event.getDraggable()) + "] from ["  + getId(event.getSource()) + "].");
  }

  /**
   * Called for all Draggables when the Drag operation stops.
   * @param event the DraggableDragCanceledEvent
   */
  @NiftyEventSubscriber(pattern=".*")
  public void showDragCancelStatus(final String id, final DraggableDragCanceledEvent event) {
    setStatus("Canceled [" + getId(event.getDraggable()) + "] reverting back to ["  + getId(event.getSource()) + "].");
  }

  /**
   * Called for all Dropables when something is dropped on them.
   * @param event the DropableDroppedEvent
   */
  @NiftyEventSubscriber(pattern=".*")
  public void showDropStatus(final String id, final DroppableDroppedEvent event) {
    if ((event.getTarget() == trash) && (event.getSource() == evilStuff)) {
      setStatus("Evil [" + getId(event.getDraggable()) + "] has been eliminated.");
    } else if (event.getTarget() == evilStuff) {
      setStatus("[" + getId(event.getDraggable()) + "] has become evil");
    } else {
      setStatus("Dropped [" + getId(event.getDraggable()) + "] on ["  + getId(event.getTarget()) + "].");
    }
  }

  /**
   * Called when something is dropped on the Trash.
   * @param event the DropableDroppedEvent
   */
  @NiftyEventSubscriber(id="Trash")
  public void onTrashDrop(final String id, final DroppableDroppedEvent event) {
    event.getDraggable().getElement().markForRemoval();
  }

  private String getId(final Droppable droppable) {
    return (droppable != null) ? droppable.getElement().getId() : null;
  }

  private String getId(final Draggable draggable) {
    return (draggable != null) ? draggable.getElement().getId() : null;
  }

  private void setStatus(final String text) {
    Label status = screen.findNiftyControl("status", Label.class);
    status.setText(text);
  }

  private Droppable findDroppable(final String id) {
    return screen.findNiftyControl(id, Droppable.class);
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "dragndrop/dragndrop.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty Drag'n'Drop Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing
  }
}
