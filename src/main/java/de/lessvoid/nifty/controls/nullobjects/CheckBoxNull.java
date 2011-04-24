package de.lessvoid.nifty.controls.nullobjects;

import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

public class CheckBoxNull implements CheckBox {

  @Override
  public Element getElement() {
    return null;
  }

  @Override
  public String getId() {
    return "CheckBoxNull";
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
  public void check() {
  }

  @Override
  public void uncheck() {
  }

  @Override
  public void setChecked(final boolean state) {
  }

  @Override
  public boolean isChecked() {
    return false;
  }

  @Override
  public void toggle() {
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
