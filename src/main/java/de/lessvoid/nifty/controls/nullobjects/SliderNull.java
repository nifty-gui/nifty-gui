package de.lessvoid.nifty.controls.nullobjects;

import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

public class SliderNull implements Slider {

  @Override
  public Element getElement() {
    return null;
  }

  @Override
  public String getId() {
    return "SliderNull";
  }

  @Override
  public void setId(final String id) {
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public void setWidth(final SizeValue width) {
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public void setHeight(final SizeValue height) {
  }

  @Override
  public String getStyle() {
    return null;
  }

  @Override
  public void setStyle(final String style) {
  }

  @Override
  public void enable() {
  }

  @Override
  public void disable() {
  }

  @Override
  public void setEnabled(final boolean enabled) {
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public void setup(final float min, final float max, final float current, final float stepSize, final float buttonStepSize) {
  }

  @Override
  public void setValue(final float value) {
  }

  @Override
  public float getValue() {
    return 0;
  }

  @Override
  public void setMin(final float min) {
  }

  @Override
  public float getMin() {
    return 0;
  }

  @Override
  public void setMax(final float max) {
  }

  @Override
  public float getMax() {
    return 0;
  }

  @Override
  public void setStepSize(final float stepSize) {
  }

  @Override
  public float getStepSize() {
    return 0;
  }

  @Override
  public void setButtonStepSize(final float buttonStepSize) {
  }

  @Override
  public float getButtonStepSize() {
    return 0;
  }

  @Override
  public void setFocus() {
  }

  @Override
  public void setFocusable(final boolean focusable) {
  }

  @Override
  public boolean hasFocus() {
    return false;
  }
}
