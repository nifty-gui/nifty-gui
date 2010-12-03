package de.lessvoid.nifty.controls.window.controller;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.dragndrop.controller.DragNotify;
import de.lessvoid.nifty.controls.dragndrop.controller.DraggableControl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class WindowControl implements Controller {

  private DraggableControl draggableControl = new DraggableControl();
  private Element element;
  private boolean removeCloseButton;

  public void bind(Nifty nifty, Screen screen, Element element,
      Properties parameter, ControllerEventListener listener,
      Attributes controlDefinitionAttributes) {
    draggableControl.bind(nifty, screen, element, parameter, listener,
        controlDefinitionAttributes);
    this.element = element;
    
    removeCloseButton = !controlDefinitionAttributes.getAsBoolean("closeable", true);
  }

  public boolean inputEvent(NiftyInputEvent inputEvent) {
    return draggableControl.inputEvent(inputEvent);
  }

  public void onFocus(boolean getFocus) {
    draggableControl.onFocus(getFocus);
  }

  public void onStartScreen() {
    draggableControl.onStartScreen();
    if (removeCloseButton) {
      getCloseButton().markForRemoval();
      removeCloseButton = false;
    }
  }

  public void dragStart(int mouseX, int mouseY) {
    draggableControl.dragStart(mouseX, mouseY);
  }

  public void drag(int mouseX, int mouseY) {
    draggableControl.drag(mouseX, mouseY);
  }

  public void dragStop() {
    draggableControl.dragStop();
  }
  
  public void closeWindow() {
    element.markForRemoval();
  }
  
  public String getTitle() {
    return getTitleElement().getRenderer(TextRenderer.class).getOriginalText();
  }
  
  public void setTitle(String title) {
    getTitleElement().getRenderer(TextRenderer.class).setText(title);
  }
  
  private Element getTitleElement() {
    return element.findElementByName("window-title");
  }
  
  private Element getCloseButton() {
    return element.findElementByName("window-close-button");
  }
  
  public Element getContent() {
    return element.findElementByName("window-content");
  }

  public void addNotify(final DragNotify notify) {
    draggableControl.addNotify(notify);
  }

  public void removeNotify(final DragNotify notify) {
    draggableControl.removeNotify(notify);
  }

  public void removeAllNotifies() {
    draggableControl.removeAllNotifies();
  }
}
