package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

/**
 * This Nifty Effect will change the position of the element the effect is attached to to
 * the current mouse pointer position. Make sure that the element you apply this effect to
 * is a child of an element that uses childLayout="absolute" or this effect will not work correctly.
 *
 * @author void
 */
public class FollowMouse implements EffectImpl {
  @Nonnull
  private static final Logger log = Logger.getLogger(FollowMouse.class.getName());
  @Nullable
  private Nifty nifty;
  private int offsetX;
  private int offsetY;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    this.nifty = nifty;
    this.offsetX = Integer.valueOf(parameter.getProperty("offsetX", "20"));
    this.offsetY = Integer.valueOf(parameter.getProperty("offsetY", "20"));
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (nifty == null) {
      log.warning("FadeMusic effect is executed before it was activated.");
      return;
    }
    NiftyMouse niftyMouse = nifty.getNiftyMouse();

    int newPosX = borderCheck(niftyMouse.getX() + offsetX, element.getWidth(), r.getWidth());
    element.setConstraintX(SizeValue.px(newPosX));

    int newPosY = borderCheck(niftyMouse.getY() + offsetY, element.getHeight(), r.getHeight());
    element.setConstraintY(SizeValue.px(newPosY));

    element.getParent().layoutElements();
  }

  private int borderCheck(final int pos, final int size, final int max) {
    if (pos + size > max) {
      return max - size;
    }
    return pos;
  }

  @Override
  public void deactivate() {
  }
}


