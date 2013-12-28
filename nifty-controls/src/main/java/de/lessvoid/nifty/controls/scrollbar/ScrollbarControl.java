package de.lessvoid.nifty.controls.scrollbar;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

/**
 * This is the controller that takes care for the functionality of a scroll bar. It supports both horizontal and
 * vertical scroll bars.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @deprecated Please use {@link de.lessvoid.nifty.controls.Scrollbar} when accessing NiftyControls.
 */
@Deprecated
public class ScrollbarControl extends AbstractController implements Scrollbar {
  @Nonnull
  private static final Logger log = Logger.getLogger(ScrollbarControl.class.getName());
  @Nonnull
  private final ScrollbarImpl scrollbarImpl;
  @Nullable
  private ScrollbarView scrollbarView;
  @Nullable
  private NextPrevHelper nextPrevHelper;

  public ScrollbarControl() {
    scrollbarImpl = new ScrollbarImpl();
  }

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    bind(element);

    Element elementBackground = element.findElementById("#background");
    Element elementPosition = element.findElementById("#position");
    nextPrevHelper = new NextPrevHelper(element, screen.getFocusHandler());

    if (elementBackground == null) {
      log.severe("Background of scrollbar control not located. Scrollbar will not work properly. Looked for: " +
          "#background");
    }
    if (elementPosition == null) {
      log.severe("Position element of scrollbar control not located. Scrollbar will not work properly. Looked for: " +
          "#position");
    } else if (elementBackground != null) {
      if ("verticalScrollbar".equals(parameter.get("name"))) {
        scrollbarView = new ScrollbarViewVertical(nifty, this, elementBackground, elementPosition,
            elementPosition.getConstraintHeight().getValueAsInt(1.f));
      } else {
        scrollbarView = new ScrollbarViewHorizontal(nifty, this, elementBackground, elementPosition,
            elementPosition.getConstraintHeight().getValueAsInt(1.f));
      }
    }

    if (scrollbarView != null) {
      float worldMax = parameter.getAsFloat("worldMax", 100.f);
      float worldPageSize = parameter.getAsFloat("worldPageSize", 100.f);
      float initial = parameter.getAsFloat("initial", 0.f);
      float buttonStepSize = parameter.getAsFloat("buttonStepSize", 1.f);
      float pageStepSize = parameter.getAsFloat("pageStepSize", 25.f);
      scrollbarImpl.bindToView(scrollbarView, initial, worldMax, worldPageSize, buttonStepSize, pageStepSize);
    }
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void layoutCallback() {
    scrollbarImpl.updateView();
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (nextPrevHelper != null && nextPrevHelper.handleNextPrev(inputEvent)) {
      return true;
    }
    if (inputEvent == NiftyStandardInputEvent.MoveCursorUp || inputEvent == NiftyStandardInputEvent.MoveCursorLeft) {
      scrollbarImpl.stepDown();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorDown || inputEvent == NiftyStandardInputEvent
        .MoveCursorRight) {
      scrollbarImpl.stepUp();
      return true;
    }
    return false;
  }

  public void upClick() {
    scrollbarImpl.stepDown();
  }

  public void downClick() {
    scrollbarImpl.stepUp();
  }

  public void click(final int mouseX, final int mouseY) {
    if (scrollbarView != null) {
      scrollbarImpl.interactionClick(scrollbarView.filter(mouseX, mouseY));
    }
  }

  public void mouseMoveStart(final int mouseX, final int mouseY) {
    if (scrollbarView != null) {
      scrollbarImpl.interactionClick(scrollbarView.filter(mouseX, mouseY));
    }
  }

  public void mouseMove(final int mouseX, final int mouseY) {
    if (scrollbarView != null) {
      scrollbarImpl.interactionMove(scrollbarView.filter(mouseX, mouseY));
    }
  }

  public boolean consumeRelease() {
    return true;
  }

  public boolean mouseWheel(@Nonnull final Element element, @Nonnull final NiftyMouseInputEvent inputEvent) {
    int mouseWheel = inputEvent.getMouseWheel();
    float currentValue = scrollbarImpl.getValue();
    if (mouseWheel < 0) {
      scrollbarImpl.setValue(currentValue - scrollbarImpl.getButtonStepSize() * mouseWheel);
    } else if (mouseWheel > 0) {
      scrollbarImpl.setValue(currentValue - scrollbarImpl.getButtonStepSize() * mouseWheel);
    }
    return true;
  }

  // Scrollbar implementation

  @Override
  public void setup(
      final float value,
      final float worldMax,
      final float worldPageSize,
      final float buttonStepSize,
      final float pageStepSize) {
    scrollbarImpl.setup(value, worldMax, worldPageSize, buttonStepSize, pageStepSize);
  }

  @Override
  public void setValue(final float value) {
    scrollbarImpl.setValue(value);
  }

  @Override
  public float getValue() {
    return scrollbarImpl.getValue();
  }

  @Override
  public void setWorldMax(final float worldMax) {
    scrollbarImpl.setWorldMax(worldMax);
  }

  @Override
  public float getWorldMax() {
    return scrollbarImpl.getWorldMax();
  }

  @Override
  public void setWorldPageSize(final float worldPageSize) {
    scrollbarImpl.setWorldPageSize(worldPageSize);
  }

  @Override
  public float getWorldPageSize() {
    return scrollbarImpl.getWorldPageSize();
  }

  @Override
  public void setButtonStepSize(final float stepSize) {
    scrollbarImpl.setButtonStepSize(stepSize);
  }

  @Override
  public float getButtonStepSize() {
    return scrollbarImpl.getButtonStepSize();
  }

  @Override
  public void setPageStepSize(final float stepSize) {
    scrollbarImpl.setPageStepSize(stepSize);
  }

  @Override
  public float getPageStepSize() {
    return scrollbarImpl.getPageStepSize();
  }

  // ScrollbarView implementations

  private static class ScrollbarViewVertical implements ScrollbarView {
    @Nonnull
    private final Nifty nifty;
    @Nonnull
    private final ScrollbarControl scrollbar;
    @Nonnull
    private final Element elementBackground;
    @Nonnull
    private final Element elementPosition;
    private int minHandleSize;

    public ScrollbarViewVertical(
        @Nonnull final Nifty nifty,
        @Nonnull final ScrollbarControl scrollbar,
        @Nonnull final Element elementBackground,
        @Nonnull final Element elementPosition,
        final int minHandleSize) {
      this.nifty = nifty;
      this.scrollbar = scrollbar;
      this.elementBackground = elementBackground;
      this.elementPosition = elementPosition;
      this.minHandleSize = minHandleSize;
    }

    @Override
    public int getAreaSize() {
      return elementBackground.getHeight();
    }

    @Override
    public int getMinHandleSize() {
      return minHandleSize;
    }

    @Override
    public void setHandle(final int pos, final int size) {
      Element scrollbarElement = scrollbar.getElement();
      if (scrollbarElement == null) {
        return;
      }

      if (getAreaSize() < minHandleSize) {
        if (elementPosition.isVisible()) {
          elementPosition.hide();
        }
      } else {
        if (!elementPosition.isVisible()) {
          elementPosition.show();
        }
        elementPosition.setConstraintY(SizeValue.px(pos));
        elementPosition.setConstraintHeight(SizeValue.px(size));
        elementBackground.layoutElements();
      }
    }

    @Override
    public void valueChanged(final float value) {
      String id = scrollbar.getId();
      if (id != null) {
        nifty.publishEvent(id, new ScrollbarChangedEvent(scrollbar, value));
      }
    }

    @Override
    public int filter(final int pixelX, final int pixelY) {
      return pixelY - elementBackground.getY();
    }
  }

  private static class ScrollbarViewHorizontal implements ScrollbarView {
    @Nonnull
    private final Nifty nifty;
    @Nonnull
    private final ScrollbarControl scrollbar;
    @Nonnull
    private final Element elementBackground;
    @Nonnull
    private final Element elementPosition;
    private int minHandleSize;

    public ScrollbarViewHorizontal(
        @Nonnull final Nifty nifty,
        @Nonnull final ScrollbarControl scrollbar,
        @Nonnull final Element elementBackground,
        @Nonnull final Element elementPosition,
        final int minHandleSize) {
      this.nifty = nifty;
      this.scrollbar = scrollbar;
      this.elementBackground = elementBackground;
      this.elementPosition = elementPosition;
      this.minHandleSize = minHandleSize;
    }

    @Override
    public int getAreaSize() {
      return elementBackground.getWidth();
    }

    @Override
    public int getMinHandleSize() {
      return minHandleSize;
    }

    @Override
    public void setHandle(final int pos, final int size) {
      Element scrollbarElement = scrollbar.getElement();
      if (scrollbarElement == null) {
        return;
      }

      if (elementBackground.getWidth() < minHandleSize) {
        if (scrollbarElement.isVisible()) {
          elementPosition.hide();
        }
      } else {
        if (scrollbarElement.isVisible()) {
          elementPosition.show();
        }
        elementPosition.setConstraintX(SizeValue.px(pos));
        elementPosition.setConstraintWidth(SizeValue.px(size));
        elementBackground.layoutElements();
      }
    }

    @Override
    public void valueChanged(final float value) {
      String id = scrollbar.getId();
      if (id != null) {
        nifty.publishEvent(id, new ScrollbarChangedEvent(scrollbar, value));
      }
    }

    @Override
    public int filter(final int pixelX, final int pixelY) {
      return pixelX - elementBackground.getX();
    }
  }
}
