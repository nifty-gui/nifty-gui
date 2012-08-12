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
 * Color - color overlay.
 * @author void
 */
public class ColorBar implements EffectImpl {
  private static Logger log = Logger.getLogger(ColorBar.class.getName());
  private Color color;
  private Color tempColor = new Color("#000f");
  private SizeValue width;
  private SizeValue insetLeft = new SizeValue("0px");
  private SizeValue insetRight = new SizeValue("0px");
  private SizeValue insetTop = new SizeValue("0px");
  private SizeValue insetBottom = new SizeValue("0px");

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
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

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    r.saveState(null);
    if (r.isColorAlphaChanged()) {
      if (falloff == null) {
        r.setColorIgnoreAlpha(color);
      } else {
        tempColor.mutiply(color, falloff.getFalloffValue());
        r.setColorIgnoreAlpha(tempColor);
      }
    } else {
      if (falloff == null) {
        r.setColor(color);
      } else {
        tempColor.mutiply(color, falloff.getFalloffValue());
        r.setColor(tempColor);
      }
    }

    int insetOffsetLeft = insetLeft.getValueAsInt(element.getWidth());
    int insetOffsetRight = insetRight.getValueAsInt(element.getWidth());
    int insetOffsetTop = insetTop.getValueAsInt(element.getHeight());
    int insetOffsetBottom = insetBottom.getValueAsInt(element.getHeight());

    int size = (int) width.getValue(element.getParent().getWidth());
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

  public void deactivate() {
  }
}
