package de.lessvoid.nifty.controls.window;

import java.util.Properties;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Window;
import de.lessvoid.nifty.controls.WindowClosedEvent;
import de.lessvoid.nifty.controls.dragndrop.DraggableControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class WindowControl extends AbstractController implements Window {
  private DraggableControl draggableControl = new DraggableControl();
  private Nifty nifty;
  private boolean removeCloseButton;
  private boolean hideOnClose;

  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    super.bind(element);
    this.nifty = nifty;
    draggableControl.bind(nifty, screen, element, parameter, controlDefinitionAttributes);
    removeCloseButton = !controlDefinitionAttributes.getAsBoolean("closeable", true);
    hideOnClose = controlDefinitionAttributes.getAsBoolean("hideOnClose", false);
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
    if (hideOnClose) {
      getElement().hide(new CloseEndNotify(nifty, true));
    } else {
      getElement().markForRemoval(new CloseEndNotify(nifty, false));
    }
  }

  private class CloseEndNotify implements EndNotify {
    private final Nifty nifty;
    private final boolean hidden;

    public CloseEndNotify(final Nifty nifty, final boolean hidden) {
      this.nifty = nifty;
      this.hidden = hidden;
    }

    @Override
    public void perform() {
      nifty.publishEvent(getElement().getId(), new WindowClosedEvent(WindowControl.this, hidden));
    }
  }
}
