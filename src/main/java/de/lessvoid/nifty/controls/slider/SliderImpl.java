package de.lessvoid.nifty.controls.slider;

/**
 * A simple Slider mechanism which is used as the base for the Scrollbar.
 * @author void
 */
public class SliderImpl {
  private SliderView view;
  private float value;
  private float oldValue;
  private float min;
  private float max;
  private float stepSize;
  private float buttonStepSize;

  public void bindToView(final SliderView view, final float min, final float max, final float stepSize, final float buttonStepSize) {
    this.view = view;
    this.min = min;
    this.max = max;
    this.stepSize = stepSize;
    this.buttonStepSize = buttonStepSize;
    this.oldValue = -1.f;
    updateView();
    changeValue(0.f);
  }

  public void setValue(final float value) {
    changeValue(value);
    updateView();
  }

  public float getValue() {
    return value;
  }

  public void stepUp() {
    changeValue(value + buttonStepSize);
    updateView();
  }

  public void stepDown() {
    changeValue(value - buttonStepSize);
    updateView();
  }

  public void setValueFromPosition(final int pixelX, final int pixelY) {
    setValue(ensureStepSize(viewToWorld(view.filter(pixelX, pixelY))));
  }

  public float getMin() {
    return min;
  }

  public void setMin(final float min) {
    this.min = min;
    changeValue(value);
    updateView();
  }

  public float getMax() {
    return max;
  }

  public void setMax(final float max) {
    this.max = max;
    changeValue(value);
    updateView();
  }

  public float getStepSize() {
    return stepSize;
  }

  public void setStepSize(final float stepSize) {
    this.stepSize = stepSize;
    changeValue(value);
    updateView();
  }

  public float getButtonStepSize() {
    return buttonStepSize;
  }

  public void setButtonStepSize(final float buttonStepSize) {
    this.buttonStepSize = buttonStepSize;
    changeValue(value);
    updateView();
  }

  public void setup(final float min, final float max, final float current, final float stepSize, final float buttonStepSize) {
    this.min = min;
    this.max = max;
    this.value = current;
    this.stepSize = stepSize;
    this.buttonStepSize = buttonStepSize;
    changeValue(value);
    updateView();
  }

  private void changeValue(final float newValue) {
    value = newValue;    
    if (value > max) {
      value = max;
    } else if (newValue < min) {
      value = min;
    }
    value = ensureStepSize(value);
    if (value != oldValue) {
      oldValue = value;
      view.valueChanged(value);
    }
  }

  private void updateView() {
    view.update((int) worldToView(value));
  }

  private float ensureStepSize(final float value) {
    return Math.round(value / stepSize) * stepSize;
  }

  private float viewToWorld(final float viewValue) {
    return (viewValue / view.getSize() * (max - min)) + min;
  }

  private float worldToView(final float worldValue) {
    return (worldValue - min) / (max - min) * view.getSize();
  }
}
