package de.lessvoid.nifty.controls.window;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Window;
import de.lessvoid.nifty.controls.dragndrop.DraggableControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class WindowControl extends AbstractController implements Window {
  private DraggableControl draggableControl = new DraggableControl();
  private boolean removeCloseButton;

  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    super.bind(element);
    draggableControl.bind(nifty, screen, element, parameter, controlDefinitionAttributes);
    removeCloseButton = !controlDefinitionAttributes.getAsBoolean("closeable", true);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  @Override
  public void onStartScreen() {
    draggableControl.onStartScreen();
    if (removeCloseButton) {
      getCloseButton().markForRemoval();
      removeCloseButton = false;
    }
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return draggableControl.inputEvent(inputEvent);
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
    draggableControl.onFocus(getFocus);
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

  private Element getTitleElement() {
    return getElement().findElementByName("#window-title");
  }

  private Element getCloseButton() {
    return getElement().findElementByName("#window-close-button");
  }

  public Element getContent() {
    return getElement().findElementByName("#window-content");
  }

  // Window implementation

  @Override
  public String getTitle() {
    return getTitleElement().getRenderer(TextRenderer.class).getOriginalText();
  }

  @Override
  public void setTitle(final String title) {
    getTitleElement().getRenderer(TextRenderer.class).setText(title);
  }

  @Override
  public void closeWindow() {
    getElement().markForRemoval();
  }
}
