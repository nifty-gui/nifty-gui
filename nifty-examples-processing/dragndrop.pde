import java.util.Random;

public static class DragDropScreen implements ScreenController, NiftyExample {
  Nifty nifty;
  Screen screen;  
  Random random = new Random();  
  Droppable trash;  
  Droppable evilStuff;

  
  void bind( Nifty nifty,  Screen screen) {
    this.nifty = nifty;
    this.screen = screen;

    trash = findDroppable("Trash");
    Droppable goodStuff = findDroppable("GoodStuff");
    evilStuff = findDroppable("EvilStuff");

    // this filter demonstrates a drop filter. in this case you can't drag something from the "EvilStuff"
    // dropable to the "GoodStuff" dropable
    goodStuff.addFilter(new DroppableDropFilter() {
      
      public boolean accept(
           Droppable source,
           Draggable draggable,
           Droppable target) {
        return source != evilStuff;
      }
    });
  }
  
  void onStartScreen() {
  }
  
  void onEndScreen() {
  }
  
  /**
   * quit method called from the dragndrop.xml.
   */
  void quit() {
    nifty.setAlternateKeyForNextLoadXml("fade");
    nifty.fromXml("all/intro.xml", "menu");
  }
  
  void spawnDraggable() {
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

  void spawnWindow() {
    Element windows = screen.findElementById("windows");
    if (windows == null) {
      throw new IllegalStateException("Window parent control not found.");
    }

    String windowId = NiftyIdCreator.generate() + 1000;
    WindowBuilder windowBuilder = new WindowBuilder("window-" + windowId, "New Window [" + windowId + "]");
    windowBuilder.width("360px");
    windowBuilder.height("240px");
    windowBuilder.build(nifty, screen, windows);
  }

  @NiftyEventSubscriber(pattern = "window-.*")
  void onAnyWindowClose(String id,  WindowClosedEvent event) {
    setStatus("Window [" + id + "] " + (event.isHidden() ? "hidden" : "closed"));
  }
  
  String randomColor() {
    return "#" + Integer.toHexString(random.nextInt(200)) + Integer.toHexString(random.nextInt(200)) + Integer
        .toHexString(random.nextInt(200)) + "ff";
  }

  /**
   * Called for all Draggables when the Drag operation starts.
   *
   * @param event the DraggableDragStartedEvent
   */
  @NiftyEventSubscriber(pattern = ".*")
  void showDragStartStatus(String id,  DraggableDragStartedEvent event) {
    setStatus("Dragging [" + getId(event.getDraggable()) + "] from [" + getId(event.getSource()) + "].");
  }

  /**
   * Called for all Draggables when the Drag operation stops.
   *
   * @param event the DraggableDragCanceledEvent
   */
  @NiftyEventSubscriber(pattern = ".*")
  void showDragCancelStatus(String id,  DraggableDragCanceledEvent event) {
    setStatus("Canceled [" + getId(event.getDraggable()) + "] reverting back to [" + getId(event.getSource()) + "].");
  }

  /**
   * Called for all Dropables when something is dropped on them.
   *
   * @param event the DropableDroppedEvent
   */
  @NiftyEventSubscriber(pattern = ".*")
  void showDropStatus(String id,  DroppableDroppedEvent event) {
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
  void onTrashDrop(String id,  DroppableDroppedEvent event) {
    event.getDraggable().getElement().markForRemoval();
  }
  
  String getId( Droppable droppable) {
    return (droppable != null) ? droppable.getElement().getId() : null;
  }
  
  String getId( Draggable draggable) {
    return (draggable != null) ? draggable.getElement().getId() : null;
  }
  
  void setStatus(String text) {
    Label status = screen.findNiftyControl("status", Label.class);
    if (status != null) {
      status.setText(text);
    }
  }
  
  Droppable findDroppable(String id) {
    Droppable droppable = screen.findNiftyControl(id, Droppable.class);
    if (droppable == null) {
      throw new IllegalArgumentException("Requested id " + id + " does not match a droppable.");
    }
    return droppable;
  }  
  
  String getStartScreen() {
    return "start";
  }  
  
  String getMainXML() {
    return "dragndrop/dragndrop.xml";
  }  
  
  String getTitle() {
    return "Nifty Drag'n'Drop Example";
  }
  
  void prepareStart(Nifty nifty) {
    // nothing
  }
}