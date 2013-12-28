package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.helper.PaddingAttributeParser;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

/**
 * Border - border overlay.
 *
 * @author void
 */
public class Border implements EffectImpl {
  private static final Logger log = Logger.getLogger(Border.class.getName());
  @Nonnull
  private Color colorLeft = Color.WHITE;
  @Nonnull
  private Color colorRight = Color.WHITE;
  @Nonnull
  private Color colorTop = Color.WHITE;
  @Nonnull
  private Color colorBottom = Color.WHITE;
  @Nonnull
  private SizeValue borderLeft = SizeValue.px(1);
  @Nonnull
  private SizeValue borderRight = SizeValue.px(1);
  @Nonnull
  private SizeValue borderTop = SizeValue.px(1);
  @Nonnull
  private SizeValue borderBottom = SizeValue.px(1);
  @Nonnull
  private SizeValue insetLeft = SizeValue.px(0);
  @Nonnull
  private SizeValue insetRight = SizeValue.px(0);
  @Nonnull
  private SizeValue insetTop = SizeValue.px(0);
  @Nonnull
  private SizeValue insetBottom = SizeValue.px(0);

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
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

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
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

  private void setAlphaSaveColor(@Nonnull final NiftyRenderEngine r, @Nonnull final Color color) {
    if (r.isColorAlphaChanged()) {
      r.setColorIgnoreAlpha(color);
    } else {
      r.setColor(color);
    }
  }

  private int getBorder(@Nonnull final Element element, @Nonnull final SizeValue sizeValue) {
    if (!element.hasParent()) {
      return 0;
    } else {
      final int parentWidth = sizeValue.getValueAsInt(element.getParent().getWidth());
      if (parentWidth < 0) {
        return 0;
      }
      return parentWidth;
    }
  }

  @Override
  public void deactivate() {
  }
}
