package de.lessvoid.nifty.controls.slider;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.NextPrevHelper;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * @deprecated Please use {@link de.lessvoid.nifty.controls.Slider} when accessing NiftyControls.
 */
@Deprecated
public class SliderControl extends AbstractController implements Slider {
  private SliderImpl sliderImpl = new SliderImpl();
  private SliderView sliderView;
  private Nifty nifty;
  private Element element;
  private Element elementPosition;
  private Element elementBackground;
  private NextPrevHelper nextPrevHelper;
  private float min;
  private float max;
  private float initial;
  private float stepSize;
  private float buttonStepSize;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    super.bind(element);

    this.nifty = nifty;
    this.element = element;
    elementBackground = element.findElementByName("#background");
    elementPosition = element.findElementByName("#position");
    nextPrevHelper = new NextPrevHelper(element, screen.getFocusHandler());

    if ("verticalSlider".equals(parameter.getProperty("name"))) {
      sliderView = new SliderViewVertical(this);
    } else if ("horizontalSlider".equals(parameter.getProperty("name"))) {
      sliderView = new SliderViewHorizontal(this);
    }

    min = Float.valueOf(parameter.getProperty("min", "0.0"));
    max = Float.valueOf(parameter.getProperty("max", "100.0"));
    initial = Float.valueOf(parameter.getProperty("initial", "0.0"));
    stepSize = Float.valueOf(parameter.getProperty("stepSize", "1.0"));
    buttonStepSize = Float.valueOf(parameter.getProperty("buttonStepSize", "25.0"));
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
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (nextPrevHelper.handleNextPrev(inputEvent)) {
      return true;
    }
    if (inputEvent == NiftyStandardInputEvent.MoveCursorUp || inputEvent == NiftyStandardInputEvent.MoveCursorLeft) {
      sliderImpl.stepDown();
      return true;
    } else if (inputEvent == NiftyStandardInputEvent.MoveCursorDown || inputEvent == NiftyStandardInputEvent.MoveCursorRight) {
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
    sliderImpl.setValueFromPosition(
        mouseX - elementBackground.getX() - elementPosition.getWidth() / 2,
        mouseY - elementBackground.getY() - elementPosition.getHeight() / 2);
  }

  public void mouseWheel(final Element element, final NiftyMouseInputEvent inputEvent) {
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
  public void setup(final float min, final float max, final float current, final float stepSize, final float buttonStepSize) {
    sliderImpl.setup(min, max, current, stepSize, buttonStepSize);
  }

  // SliderView implementation

  private class SliderViewVertical implements SliderView {
    private Slider slider;

    public SliderViewVertical(final Slider slider) {
      this.slider = slider;
    }

    @Override
    public int getSize() {
      return elementBackground.getHeight() - elementPosition.getHeight();
    }

    @Override
    public void update(final int position) {
      elementPosition.setConstraintY(new SizeValue(position + "px"));
      elementBackground.layoutElements();
    }

    @Override
    public int filter(final int pixelX, final int pixelY) {
      return pixelY;
    }

    @Override
    public void valueChanged(final float value) {
      if (element.getId() != null) {
        nifty.publishEvent(element.getId(), new SliderChangedEvent(slider, value));
      }
    }
  }

  private class SliderViewHorizontal implements SliderView {
    private Slider slider;

    public SliderViewHorizontal(final Slider slider) {
      this.slider = slider;
    }

    @Override
    public int getSize() {
      return elementBackground.getWidth() - elementPosition.getWidth();
    }

    @Override
    public void update(final int position) {
      elementPosition.setConstraintX(new SizeValue(position + "px"));
      elementBackground.layoutElements();
    }

    @Override
    public int filter(final int pixelX, final int pixelY) {
      return pixelX;
    }

    @Override
    public void valueChanged(final float value) {
      if (element.getId() != null) {
        nifty.publishEvent(element.getId(), new SliderChangedEvent(slider, value));
      }
    }
  }
}
