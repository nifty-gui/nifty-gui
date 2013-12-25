package de.lessvoid.nifty.controls.slider;

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
 * @deprecated Please use {@link de.lessvoid.nifty.controls.Slider} when accessing NiftyControls.
 */
@Deprecated
public class SliderControl extends AbstractController implements Slider {
  @Nonnull
  private static final Logger log = Logger.getLogger(SliderControl.class.getName());
  @Nonnull
  private final SliderImpl sliderImpl;
  @Nullable
  private SliderView sliderView;
  @Nullable
  private Element elementPosition;
  @Nullable
  private Element elementBackground;
  @Nullable
  private NextPrevHelper nextPrevHelper;

  public SliderControl() {
    sliderImpl = new SliderImpl();
  }

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    bind(element);

    elementBackground = element.findElementById("#background");
    elementPosition = element.findElementById("#position");
    nextPrevHelper = new NextPrevHelper(element, screen.getFocusHandler());

    if (elementBackground == null) {
      log.severe("Background element of slider not found. Slider will not work properly. Looked for: #background");
    }
    if (elementPosition == null) {
      log.severe("Position element of slider not found. Slider will not work properly. Looked for: #position");
    } else if (elementBackground != null) {
      if ("verticalSlider".equals(parameter.get("name"))) {
        sliderView = new SliderViewVertical(nifty, this, elementBackground, elementPosition);
      } else {
        sliderView = new SliderViewHorizontal(nifty, this, elementBackground, elementPosition);
      }
    }

    float min = parameter.getAsFloat("min", 0.f);
    float max = parameter.getAsFloat("max", 100.f);
    float initial = parameter.getAsFloat("initial", 0.f);
    float stepSize = parameter.getAsFloat("stepSize", 1.f);
    float buttonStepSize = parameter.getAsFloat("buttonStepSize", 25.f);
    sliderImpl.bindToView(sliderView, min, max, stepSize, buttonStepSize);
    sliderImpl.setValue(initial);
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void layoutCallback() {
    sliderImpl.updateView();
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (nextPrevHelper != null && nextPrevHelper.handleNextPrev(inputEvent)) {
      return true;
    }
    if (inputEvent == NiftyStandardInputEvent.MoveCursorUp || inputEvent == NiftyStandardInputEvent.MoveCursorLeft) {
      sliderImpl.stepDown();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorDown || inputEvent == NiftyStandardInputEvent
        .MoveCursorRight) {
      sliderImpl.stepUp();
      return true;
    }
    return false;
  }

  public void upClick() {
    sliderImpl.stepDown();
  }

  public void downClick() {
    sliderImpl.stepUp();
  }

  public void mouseClick(final int mouseX, final int mouseY) {
    if (elementBackground != null && elementPosition != null) {
      sliderImpl.setValueFromPosition(
          mouseX - elementBackground.getX() - elementPosition.getWidth() / 2,
          mouseY - elementBackground.getY() - elementPosition.getHeight() / 2);
    }
  }

  public void mouseWheel(final Element element, @Nonnull final NiftyMouseInputEvent inputEvent) {
    int mouseWheel = inputEvent.getMouseWheel();
    float currentValue = sliderImpl.getValue();
    if (mouseWheel < 0) {
      sliderImpl.setValue(currentValue - sliderImpl.getButtonStepSize() * mouseWheel);
    } else if (mouseWheel > 0) {
      sliderImpl.setValue(currentValue - sliderImpl.getButtonStepSize() * mouseWheel);
    }
  }

  // Slider implementation

  @Override
  public void setValue(final float value) {
    sliderImpl.setValue(value);
  }

  @Override
  public float getValue() {
    return sliderImpl.getValue();
  }

  @Override
  public void setMin(final float min) {
    sliderImpl.setMin(min);
  }

  @Override
  public float getMin() {
    return sliderImpl.getMin();
  }

  @Override
  public void setMax(final float max) {
    sliderImpl.setMax(max);
  }

  @Override
  public float getMax() {
    return sliderImpl.getMax();
  }

  @Override
  public void setStepSize(final float stepSize) {
    sliderImpl.setStepSize(stepSize);
  }

  @Override
  public float getStepSize() {
    return sliderImpl.getStepSize();
  }

  @Override
  public void setButtonStepSize(final float buttonStepSize) {
    sliderImpl.setButtonStepSize(buttonStepSize);
  }

  @Override
  public float getButtonStepSize() {
    return sliderImpl.getButtonStepSize();
  }

  @Override
  public void setup(
      final float min,
      final float max,
      final float current,
      final float stepSize,
      final float buttonStepSize) {
    sliderImpl.setup(min, max, current, stepSize, buttonStepSize);
  }

  // SliderView implementation

  private static class SliderViewVertical implements SliderView {
    @Nonnull
    private final Nifty nifty;
    @Nonnull
    private final SliderControl slider;
    @Nonnull
    private final Element elementBackground;
    @Nonnull
    private final Element elementPosition;

    public SliderViewVertical(
        @Nonnull Nifty nifty,
        @Nonnull final SliderControl slider,
        @Nonnull Element elementBackground,
        @Nonnull Element elementPosition) {
      this.nifty = nifty;
      this.slider = slider;
      this.elementBackground = elementBackground;
      this.elementPosition = elementPosition;
    }

    @Override
    public int getSize() {
      return elementBackground.getHeight() - elementPosition.getHeight();
    }

    @Override
    public void update(final int position) {
      elementPosition.setConstraintY(SizeValue.px(position));
      elementBackground.layoutElements();
    }

    @Override
    public int filter(final int pixelX, final int pixelY) {
      return pixelY;
    }

    @Override
    public void valueChanged(final float value) {
      String id = slider.getId();
      if (id != null) {
        nifty.publishEvent(id, new SliderChangedEvent(slider, value));
      }
    }
  }

  private static class SliderViewHorizontal implements SliderView {
    @Nonnull
    private final Nifty nifty;
    @Nonnull
    private final SliderControl slider;
    @Nonnull
    private final Element elementBackground;
    @Nonnull
    private final Element elementPosition;

    public SliderViewHorizontal(
        @Nonnull Nifty nifty,
        @Nonnull final SliderControl slider,
        @Nonnull Element elementBackground,
        @Nonnull Element elementPosition) {
      this.nifty = nifty;
      this.slider = slider;
      this.elementBackground = elementBackground;
      this.elementPosition = elementPosition;
    }

    @Override
    public int getSize() {
      return elementBackground.getWidth() - elementPosition.getWidth();
    }

    @Override
    public void update(final int position) {
      elementPosition.setConstraintX(SizeValue.px(position));
      elementBackground.layoutElements();
    }

    @Override
    public int filter(final int pixelX, final int pixelY) {
      return pixelX;
    }

    @Override
    public void valueChanged(final float value) {
      String id = slider.getId();
      if (id != null) {
        nifty.publishEvent(id, new SliderChangedEvent(slider, value));
      }
    }
  }
}
