package de.lessvoid.nifty.examples.dragndrop;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class WindowControl implements Controller {

  private DraggableControl draggableControl = new DraggableControl();
  private Element element;

  public void bind(Nifty nifty, Screen screen, Element element,
      Properties parameter, ControllerEventListener listener,
      Attributes controlDefinitionAttributes) {
    draggableControl.bind(nifty, screen, element, parameter, listener,
        controlDefinitionAttributes);
    this.element = element;
  }

  public void inputEvent(NiftyInputEvent inputEvent) {
    draggableControl.inputEvent(inputEvent);
  }

  public void onFocus(boolean getFocus) {
    draggableControl.onFocus(getFocus);
  }

  public void onStartScreen() {
    draggableControl.onStartScreen();
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
    return element.findElementByName("title");
  }
  
  public Element getContent() {
    return element.findElementByName("windowContent");
  }

}
