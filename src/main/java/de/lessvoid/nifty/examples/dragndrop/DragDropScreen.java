package de.lessvoid.nifty.examples.dragndrop;

import java.util.Random;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dragndrop.CreateDraggableControl;
import de.lessvoid.nifty.controls.dragndrop.controller.DraggableControl;
import de.lessvoid.nifty.controls.dragndrop.controller.DropFilter;
import de.lessvoid.nifty.controls.dragndrop.controller.DropNotify;
import de.lessvoid.nifty.controls.dragndrop.controller.DroppableControl;
import de.lessvoid.nifty.controls.window.CreateWindowControl;
import de.lessvoid.nifty.controls.window.controller.WindowControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class DragDropScreen implements ScreenController {

  private Nifty nifty;
  private Screen screen;
  private Random random = new Random();
  
  private DroppableControl trash;
  private DroppableControl goodStuff;
  private DroppableControl evilStuff;

  @Override
  public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;

    trash = findDroppable("Trash");
    goodStuff = findDroppable("GoodStuff");
    evilStuff = findDroppable("EvilStuff");
    
    trash.addNotify(new DropNotify() {

      @Override
      public void dropped(DroppableControl source, DraggableControl draggable,
          DroppableControl target) {
        draggable.getElement().markForRemoval();
      }
    });
    
    goodStuff.addFilter(new DropFilter() {
      
      @Override
      public boolean accept(DroppableControl source, DraggableControl draggable,
          DroppableControl target) {
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
    Element draggables = screen.findElementByName("draggables");

    CreateDraggableControl draggableAttributes = new CreateDraggableControl();
    draggableAttributes.setWidth("120px");
    draggableAttributes.setHeight("120px");
    draggableAttributes.set("onDragStart", "showDragStartStatus()");
    draggableAttributes.set("onDragCancel", "showDragCancelStatus()");
    draggableAttributes.setBackgroundColor(randomColor());
    draggableAttributes.create(nifty, screen, draggables);
  }
  
  public void spawnWindow() {
    Element windows = screen.findElementByName("windows");

    CreateWindowControl windowAttributes = new CreateWindowControl();
    windowAttributes.setWidth("360px");
    windowAttributes.setHeight("240px");
    windowAttributes.set("title", "New Window");
    WindowControl window = windowAttributes.create(nifty, screen, windows);
//    window.setTitle("New Window");
  }
  
  private String randomColor() {
    return "#" + Integer.toHexString(random.nextInt(255 * 255));
  }
  
  public void showDragStartStatus(DroppableControl source, DraggableControl draggable) {
    setStatus("dragging " + getId(draggable) + " from "  + getId(source));
  }
  
  public void showDragCancelStatus(DroppableControl source, DraggableControl draggable) {
    setStatus("canceled " + getId(draggable) + " reverting back to "  + getId(source));
  }
  
  public void showDropStatus(DroppableControl source, DraggableControl draggable,
      DroppableControl target) {
    if ((target == trash) && (source == evilStuff))
      setStatus("evil " + getId(draggable) + " has been eliminated");
    else if (target == evilStuff)
      setStatus(getId(draggable) + " has become evil");
    else
      setStatus("dropped " + getId(draggable) + " on "  + getId(target));
  }
  
  private String getId(DroppableControl droppable) {
    return (droppable != null) ? droppable.getElement().getId() : null;
  }

  private String getId(DraggableControl draggable) {
    return (draggable != null) ? draggable.getElement().getId() : null;
  }

  private void setStatus(String text)
  {
    System.out.println(text);
    Element status = screen.findElementByName("status");
    status.getRenderer(TextRenderer.class).setText(text);
  }

  private DroppableControl findDroppable(final String id) {
    return screen.findControl(id, DroppableControl.class);
  }

  private DraggableControl findDraggable(final String id) {
    return screen.findControl(id, DraggableControl.class);
  }

}
