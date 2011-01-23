package de.lessvoid.nifty.controls.slider;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.NextPrevHelper;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

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
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    super.bind(element);

    this.nifty = nifty;
    this.element = element;
    elementBackground = element.findElementByName("nifty-internal-slider-background");
    elementPosition = element.findElementByName("nifty-internal-slider-position");
    nextPrevHelper = new NextPrevHelper(element, screen.getFocusHandler());

    if ("verticalSlider".equals(parameter.getProperty("name"))) {
      sliderView = new SliderViewVertical();
    } else if ("horizontalSlider".equals(parameter.getProperty("name"))) {
      sliderView = new SliderViewHorizontal();
    }

    min = Float.valueOf(parameter.getProperty("min", "0.0"));
    max = Float.valueOf(parameter.getProperty("max", "100.0"));
    initial = Float.valueOf(parameter.getProperty("initial", "0.0"));
    stepSize = Float.valueOf(parameter.getProperty("stepSize", "1.0"));
    buttonStepSize = Float.valueOf(parameter.getProperty("buttonStepSize", "25.0"));
  }

  @Override
  public void onStartScreen() {
    sliderImpl.bindToView(sliderView, min, max, stepSize, buttonStepSize);
    sliderImpl.setValue(initial);
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (nextPrevHelper.handleNextPrev(inputEvent)) {
      return true;
    }
    if (inputEvent == NiftyInputEvent.MoveCursorUp || inputEvent == NiftyInputEvent.MoveCursorLeft) {
      sliderImpl.stepDown();
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorDown || inputEvent == NiftyInputEvent.MoveCursorRight) {
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

  // Slider implementation

  @Override
  public void setValue(final float value) {
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

  // SliderView implementation

  private class SliderViewVertical implements SliderView {
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
      nifty.publishEvent(element.getId(), new SliderChangedEvent(value));
    }
  }

  private class SliderViewHorizontal implements SliderView {
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
      nifty.publishEvent(element.getId(), new SliderChangedEvent(value));
    }
  }
}
