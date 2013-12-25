package de.lessvoid.nifty.controls.window;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.controls.Window;
import de.lessvoid.nifty.controls.WindowClosedEvent;
import de.lessvoid.nifty.controls.dragndrop.DraggableControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

@Deprecated
public class WindowControl extends AbstractController implements Window {
  @Nonnull
  private static final Logger log = Logger.getLogger(WindowControl.class.getName());
  @Nonnull
  private final DraggableControl draggableControl;
  @Nullable
  private Nifty nifty;
  private boolean removeCloseButton;
  private boolean hideOnClose;

  public WindowControl() {
    draggableControl = new DraggableControl();
  }

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    super.bind(element);
    this.nifty = nifty;
    draggableControl.bind(nifty, screen, element, parameter);
    removeCloseButton = !parameter.getAsBoolean("closeable", true);
    hideOnClose = parameter.getAsBoolean("hideOnClose", false);

    // testing children
    Element content = getContent();
    if (content == null) {
      log.severe("Content element of window not found. Window will not display properly.");
    }
    Element title = getTitleElement();
    if (title == null) {
      log.severe("Title element of window not found. Window will not display its head properly.");
    }
    Element closeButton = getCloseButton();
    if (closeButton == null) {
      log.severe("Close button of window not found. Window will not offer a control to close the window.");
    }
  }

  @Override
  public void onStartScreen() {
    draggableControl.onStartScreen();
    if (removeCloseButton) {
      Element closeButton = getCloseButton();
      if (closeButton != null) {
        closeButton.markForRemoval();
      }
    }
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return draggableControl.inputEvent(inputEvent);
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
    draggableControl.onFocus(getFocus);
  }

  public void bringToFront() {
    draggableControl.bringToFront();
  }

  public void drag(final int mouseX, final int mouseY) {
    draggableControl.drag(mouseX, mouseY);
  }

  public void dragStop() {
    draggableControl.dragStop();
  }

  @Nullable
  private Element getTitleElement() {
    Element element = getElement();
    if (element == null) {
      return null;
    }
    return element.findElementById("#window-title");
  }

  @Nullable
  private Element getCloseButton() {
    Element element = getElement();
    if (element == null) {
      return null;
    }
    return element.findElementById("#window-close-button");
  }

  @Nullable
  public Element getContent() {
    Element element = getElement();
    if (element == null) {
      return null;
    }
    return element.findElementById("#window-content");
  }

  // Window implementation

  @Nullable
  @Override
  public String getTitle() {
    Element title = getTitleElement();
    if (title == null) {
      return null;
    }
    TextRenderer renderer = title.getRenderer(TextRenderer.class);
    if (renderer == null) {
      return null;
    }
    return renderer.getOriginalText();
  }

  @Override
  public void setTitle(@Nonnull final String title) {
    Element titleElement = getTitleElement();
    if (titleElement == null) {
      return;
    }
    TextRenderer renderer = titleElement.getRenderer(TextRenderer.class);
    if (renderer == null) {
      return;
    }
    renderer.setText(title);
  }

  @Override
  public void closeWindow() {
    if (nifty != null) {
      Element element = getElement();
      if (element != null) {
        if (hideOnClose) {
          element.hide(new CloseEndNotify(nifty, this, element, true));
        } else {
          element.markForRemoval(new CloseEndNotify(nifty, this, element, false));
        }
      }
    }
  }

  @Override
  public void moveToFront() {
    draggableControl.moveToFront();
  }

  private static class CloseEndNotify implements EndNotify {
    @Nonnull
    private final Nifty nifty;
    @Nonnull
    private final Window window;
    @Nonnull
    private final Element element;
    private final boolean hidden;

    public CloseEndNotify(
        @Nonnull Nifty nifty,
        @Nonnull Window window,
        @Nonnull Element element,
        final boolean hidden) {
      this.nifty = nifty;
      this.window = window;
      this.element = element;
      this.hidden = hidden;
    }

    @Override
    public void perform() {
      String id = element.getId();
      if (id != null) {
        nifty.publishEvent(element.getId(), new WindowClosedEvent(window, hidden));
      }
    }
  }
}
