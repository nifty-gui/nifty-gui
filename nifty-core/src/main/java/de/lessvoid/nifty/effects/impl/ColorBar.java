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
 * Color - color overlay.
 *
 * @author void
 */
public class ColorBar implements EffectImpl {
  @Nonnull
  private static final Logger log = Logger.getLogger(ColorBar.class.getName());
  @Nullable
  private Color color;
  @Nonnull
  private final Color tempColor = new Color("#000f");
  @Nonnull
  private SizeValue width = SizeValue.def();
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
    color = new Color(parameter.getProperty("color", "#ffffffff"));
    width = new SizeValue(parameter.getProperty("width"));
    try {
      PaddingAttributeParser parser = new PaddingAttributeParser(parameter.getProperty("inset", "0px"));
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
    if (color != null) {
      if (r.isColorAlphaChanged()) {
        if (falloff == null) {
          r.setColorIgnoreAlpha(color);
        } else {
          tempColor.multiply(color, falloff.getFalloffValue());
          r.setColorIgnoreAlpha(tempColor);
        }
      } else {
        if (falloff == null) {
          r.setColor(color);
        } else {
          tempColor.multiply(color, falloff.getFalloffValue());
          r.setColor(tempColor);
        }
      }
    }

    int insetOffsetLeft = insetLeft.getValueAsInt(element.getWidth());
    int insetOffsetRight = insetRight.getValueAsInt(element.getWidth());
    int insetOffsetTop = insetTop.getValueAsInt(element.getHeight());
    int insetOffsetBottom = insetBottom.getValueAsInt(element.getHeight());

    final int size;
    if (!element.hasParent()) {
      size = -1;
    } else {
      size = width.getValueAsInt(element.getParent().getWidth());
    }
    if (size == -1) {
      r.renderQuad(
          element.getX() + insetOffsetLeft,
          element.getY() + insetOffsetTop,
          element.getWidth() - insetOffsetLeft - insetOffsetRight,
          element.getHeight() - insetOffsetTop - insetOffsetBottom);
    } else {
      r.renderQuad(
          (element.getX() + element.getWidth() / 2) - size / 2 + insetOffsetLeft,
          element.getY() + insetOffsetTop,
          size - insetOffsetLeft - insetOffsetRight,
          element.getHeight() - insetOffsetTop - insetOffsetBottom);
    }
    r.restoreState();
  }

  @Override
  public void deactivate() {
  }
}
