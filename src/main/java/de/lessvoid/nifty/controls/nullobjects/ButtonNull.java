package de.lessvoid.nifty.controls.nullobjects;

import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

public class ButtonNull implements Button {

  @Override
  public Element getElement() {
    return null;
  }

  @Override
  public String getId() {
    return "ButtonNull";
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
  public void activate() {
  }

  @Override
  public String getText() {
    return null;
  }

  @Override
  public void setText(final String text) {
  }

  @Override
  public int getTextWidth() {
    return 0;
  }

  @Override
  public int getTextHeight() {
    return 0;
  }

  @Override
  public RenderFont getFont() {
    return null;
  }

  @Override
  public void setFont(final RenderFont fontParam) {
  }

  @Override
  public VerticalAlign getTextVAlign() {
    return null;
  }

  @Override
  public void setTextVAlign(final VerticalAlign newTextVAlign) {
  }

  @Override
  public HorizontalAlign getTextHAlign() {
    return null;
  }

  @Override
  public void setTextHAlign(final HorizontalAlign newTextHAlign) {
  }

  @Override
  public Color getTextColor() {
    return null;
  }

  @Override
  public void setTextColor(final Color newColor) {
  }

  @Override
  public void setFocus() {
  }

  @Override
  public void setFocusable(final boolean focusable) {
  }
}
