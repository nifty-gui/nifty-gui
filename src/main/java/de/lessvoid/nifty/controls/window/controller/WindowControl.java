package de.lessvoid.nifty.controls.window.controller;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.dragndrop.controller.DragNotify;
import de.lessvoid.nifty.controls.dragndrop.controller.DraggableControl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class WindowControl extends AbstractController {
  private DraggableControl draggableControl = new DraggableControl();
  private Element element;
  private boolean removeCloseButton;

  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    draggableControl.bind(nifty, screen, element, parameter, listener, controlDefinitionAttributes);
    this.element = element;

    removeCloseButton = !controlDefinitionAttributes.getAsBoolean("closeable", true);
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return draggableControl.inputEvent(inputEvent);
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
    draggableControl.onFocus(getFocus);
  }

  public void onStartScreen() {
    draggableControl.onStartScreen();
    if (removeCloseButton) {
      getCloseButton().markForRemoval();
      removeCloseButton = false;
    }
  }

  public void dragStart(final int mouseX, final int mouseY) {
    draggableControl.dragStart(mouseX, mouseY);
  }

  public void drag(final int mouseX, final int mouseY) {
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

  public void setTitle(final String title) {
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

  @Override
  public void removeAllNotifies() {
    draggableControl.removeAllNotifies();
  }
}
