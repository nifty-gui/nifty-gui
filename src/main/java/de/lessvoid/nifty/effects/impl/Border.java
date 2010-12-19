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
  private Logger log = Logger.getLogger(Border.class.getName());
  private Color colorLeft = Color.WHITE;
  private Color colorRight = Color.WHITE;
  private Color colorTop = Color.WHITE;
  private Color colorBottom = Color.WHITE;
  private SizeValue borderLeft = new SizeValue("1px");
  private SizeValue borderRight = new SizeValue("1px");
  private SizeValue borderTop = new SizeValue("1px");
  private SizeValue borderBottom = new SizeValue("1px");

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    try {
      PaddingAttributeParser parser = new PaddingAttributeParser(parameter.getProperty("border"));
      borderLeft = new SizeValue(parser.getLeft());
      borderRight = new SizeValue(parser.getRight());
      borderTop = new SizeValue(parser.getTop());
      borderBottom = new SizeValue(parser.getBottom());

      parser = new PaddingAttributeParser(parameter.getProperty("color", "#ffff"));
      colorLeft = new Color(parser.getLeft());
      colorRight = new Color(parser.getRight());
      colorTop = new Color(parser.getTop());
      colorBottom = new Color(parser.getBottom());
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

    if (left > 0) {
      r.setColor(colorLeft);
      r.renderQuad(element.getX() - left, element.getY() - top, left, element.getHeight() + top + bottom);
    }
    if (right > 0) {
      r.setColor(colorRight);
      r.renderQuad(element.getX() + element.getWidth(), element.getY() - top, right, element.getHeight() + top + bottom);
    }
    if (top > 0) {
      r.setColor(colorTop);
      r.renderQuad(element.getX() - left, element.getY() - top, element.getWidth() + left + right, top);
    }
    if (bottom > 0) {
      r.setColor(colorBottom);
      r.renderQuad(element.getX() - left, element.getY() + element.getHeight(), element.getWidth() + left + right, bottom);
    }
    r.restoreState();
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
