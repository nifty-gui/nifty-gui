package de.lessvoid.nifty.controls.scrollbar;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.NextPrevHelper;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.controls.Scrollbar;
import de.lessvoid.nifty.controls.ScrollbarChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * @deprecated Please use {@link de.lessvoid.nifty.controls.Scrollbar} when accessing NiftyControls.
 */
@Deprecated
public class ScrollbarControl extends AbstractController implements Scrollbar {
  private ScrollbarImpl scrollbarImpl = new ScrollbarImpl();
  private ScrollbarView scrollbarView;
  private Nifty nifty;
  private Element elementBackground;
  private Element elementPosition;
  private NextPrevHelper nextPrevHelper;
  private float worldMax;
  private float worldPageSize;
  private float initial;
  private float pageStepSize;
  private float buttonStepSize;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Parameters parameter) {
    super.bind(element);

    this.nifty = nifty;
    this.elementBackground = element.findElementByName("#background");
    this.elementPosition = element.findElementByName("#position");
    this.nextPrevHelper = new NextPrevHelper(element, screen.getFocusHandler());

    if ("verticalScrollbar".equals(parameter.getProperty("name"))) {
      this.scrollbarView = new ScrollbarViewVertical(this, elementPosition.getHeight());
    } else if ("horizontalScrollbar".equals(parameter.getProperty("name"))) {
      this.scrollbarView = new ScrollbarViewHorizontal(this, elementPosition.getWidth());
    }

    worldMax = Float.valueOf(parameter.getProperty("worldMax", "100.0"));
    worldPageSize = Float.valueOf(parameter.getProperty("worldPageSize", "100.0"));
    initial = Float.valueOf(parameter.getProperty("initial", "0.0"));
    buttonStepSize = Float.valueOf(parameter.getProperty("buttonStepSize", "1.0"));
    pageStepSize = Float.valueOf(parameter.getProperty("pageStepSize", "25.0"));
    scrollbarImpl.bindToView(scrollbarView, initial, worldMax, worldPageSize, buttonStepSize, pageStepSize);
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void layoutCallback() {
    scrollbarImpl.updateView();
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (nextPrevHelper.handleNextPrev(inputEvent)) {
      return true;
    }
    if (inputEvent == NiftyStandardInputEvent.MoveCursorUp || inputEvent == NiftyStandardInputEvent.MoveCursorLeft) {
      scrollbarImpl.stepDown();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorDown || inputEvent == NiftyStandardInputEvent.MoveCursorRight) {
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
    scrollbarImpl.interactionClick(scrollbarView.filter(mouseX, mouseY));
  }

  public void mouseMoveStart(final int mouseX, final int mouseY) {
    scrollbarImpl.interactionClick(scrollbarView.filter(mouseX, mouseY));
  }

  public void mouseMove(final int mouseX, final int mouseY) {
    scrollbarImpl.interactionMove(scrollbarView.filter(mouseX, mouseY));
  }

  public boolean consumeRelease() {
    return true;
  }

  public boolean mouseWheel(final Element element, final NiftyMouseInputEvent inputEvent) {
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
  public void setup(final float value, final float worldMax, final float worldPageSize, final float buttonStepSize, final float pageStepSize) {
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

  private class ScrollbarViewVertical implements ScrollbarView {
    private Scrollbar scrollbar;
    private int minHandleSize;

    public ScrollbarViewVertical(final Scrollbar scrollbar, final int minHandleSize) {
      this.scrollbar = scrollbar;
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
      if (elementBackground.getHeight() < minHandleSize) {
        if (getElement().isVisible()) {
          elementPosition.hide();
        }
      } else {
        if (getElement().isVisible()) {
          elementPosition.show();
        }
        elementPosition.setConstraintY(new SizeValue(pos + "px"));
        elementPosition.setConstraintHeight(new SizeValue(size + "px"));
        elementBackground.layoutElements();
      }
    }

    @Override
    public void valueChanged(final float value) {
      if (getElement().getId() != null) {
        nifty.publishEvent(getElement().getId(), new ScrollbarChangedEvent(scrollbar, value));
      }
    }

    @Override
    public int filter(final int pixelX, final int pixelY) {
      return pixelY - elementBackground.getY();
    }
  }

  private class ScrollbarViewHorizontal implements ScrollbarView {
    private Scrollbar scrollbar;
    private int minHandleSize;

    public ScrollbarViewHorizontal(final Scrollbar scrollbar, final int minHandleSize) {
      this.scrollbar = scrollbar;
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
      if (elementBackground.getWidth() < minHandleSize) {
        if (getElement().isVisible()) {
          elementPosition.hide();
        }
      } else {
        if (getElement().isVisible()) {
          elementPosition.show();
        }
        elementPosition.setConstraintX(new SizeValue(pos + "px"));
        elementPosition.setConstraintWidth(new SizeValue(size + "px"));
        elementBackground.layoutElements();
      }
    }

    @Override
    public void valueChanged(final float value) {
      if (getElement().getId() != null) {
        nifty.publishEvent(getElement().getId(), new ScrollbarChangedEvent(scrollbar, value));
      }
    }

    @Override
    public int filter(final int pixelX, final int pixelY) {
      return pixelX - elementBackground.getX();
    }
  }
}
