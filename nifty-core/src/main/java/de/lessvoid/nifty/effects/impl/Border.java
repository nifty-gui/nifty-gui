package de.lessvoid.nifty.effects.impl;


import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.helper.PaddingAttributeParser;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Border - border overlay.
 * @author void
 */
public class Border implements EffectImpl {
  private static Logger log = Logger.getLogger(Border.class.getName());
  private Color colorLeft = Color.WHITE;
  private Color colorRight = Color.WHITE;
  private Color colorTop = Color.WHITE;
  private Color colorBottom = Color.WHITE;
  private SizeValue borderLeft = new SizeValue("1px");
  private SizeValue borderRight = new SizeValue("1px");
  private SizeValue borderTop = new SizeValue("1px");
  private SizeValue borderBottom = new SizeValue("1px");
  private SizeValue insetLeft = new SizeValue("0px");
  private SizeValue insetRight = new SizeValue("0px");
  private SizeValue insetTop = new SizeValue("0px");
  private SizeValue insetBottom = new SizeValue("0px");

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    try {
      PaddingAttributeParser parser = new PaddingAttributeParser(parameter.getProperty("border", "1px"));
      borderLeft = new SizeValue(parser.getLeft());
      borderRight = new SizeValue(parser.getRight());
      borderTop = new SizeValue(parser.getTop());
      borderBottom = new SizeValue(parser.getBottom());

      parser = new PaddingAttributeParser(parameter.getProperty("color", "#ffff"));
      colorLeft = new Color(parser.getLeft());
      colorRight = new Color(parser.getRight());
      colorTop = new Color(parser.getTop());
      colorBottom = new Color(parser.getBottom());

      parser = new PaddingAttributeParser(parameter.getProperty("inset", "0px"));
      insetLeft = new SizeValue(parser.getLeft());
      insetRight = new SizeValue(parser.getRight());
      insetTop = new SizeValue(parser.getTop());
      insetBottom = new SizeValue(parser.getBottom());
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    r.saveState(null);
    int left = getBorder(element, borderLeft);
    int right = getBorder(element, borderRight);
    int top = getBorder(element, borderTop);
    int bottom = getBorder(element, borderBottom);
    int insetOffsetLeft = insetLeft.getValueAsInt(element.getWidth());
    int insetOffsetRight = insetRight.getValueAsInt(element.getWidth());
    int insetOffsetTop = insetTop.getValueAsInt(element.getHeight());
    int insetOffsetBottom = insetBottom.getValueAsInt(element.getHeight());

    if (left > 0) {
      setAlphaSaveColor(r, colorLeft);
      r.renderQuad(
          element.getX() - left + insetOffsetLeft,
          element.getY() - top + insetOffsetTop,
          left,
          element.getHeight() + top + bottom - insetOffsetTop - insetOffsetBottom);
    }
    if (right > 0) {
      setAlphaSaveColor(r, colorRight);
      r.renderQuad(
          element.getX() + element.getWidth() - insetOffsetRight,
          element.getY() - top + insetOffsetTop,
          right,
          element.getHeight() + top + bottom - insetOffsetTop - insetOffsetBottom);
    }
    if (top > 0) {
      setAlphaSaveColor(r, colorTop);
      r.renderQuad(
          element.getX() - left + insetOffsetLeft,
          element.getY() - top + insetOffsetTop,
          element.getWidth() + left + right - insetOffsetLeft - insetOffsetRight,
          top);
    }
    if (bottom > 0) {
      setAlphaSaveColor(r, colorBottom);
      r.renderQuad(
          element.getX() - left + insetOffsetLeft,
          element.getY() + element.getHeight() - insetOffsetBottom,
          element.getWidth() + left + right - insetOffsetLeft - insetOffsetRight,
          bottom);
    }
    r.restoreState();
  }

  private void setAlphaSaveColor(final NiftyRenderEngine r, final Color color) {
    if (r.isColorAlphaChanged()) {
      r.setColorIgnoreAlpha(color);
    } else {
      r.setColor(color);
    }
  }

  private int getBorder(final Element element, final SizeValue sizeValue) {
    int left = (int) sizeValue.getValue(element.getParent().getWidth());
    if (left == -1) {
      left = 0;
    }
    return left;
  }

  public void deactivate() {
  }
}
