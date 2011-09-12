package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * This Nifty Effect will change the position of the element the effect is attached to to
 * the current mouse pointer position. Make sure that the element you apply this effect to
 * is a child of an element that uses childLayout="absolute" or this effect will not work correctly.
 *
 * @author void
 */
public class FollowMouse implements EffectImpl {
  private Nifty nifty;
  private int offsetX;
  private int offsetY;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    this.nifty = nifty;
    this.offsetX = Integer.valueOf(parameter.getProperty("offsetX", "20"));
    this.offsetY = Integer.valueOf(parameter.getProperty("offsetY", "20"));
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    NiftyMouse niftyMouse = nifty.getNiftyMouse();

    int newPosX = borderCheck(niftyMouse.getX() + offsetX, element.getWidth(), r.getWidth());
    element.setConstraintX(new SizeValue(newPosX + "px"));

    int newPosY = borderCheck(niftyMouse.getY() + offsetY, element.getHeight(), r.getHeight());
    element.setConstraintY(new SizeValue(newPosY + "px"));

    element.getParent().layoutElements();
  }

  private int borderCheck(final int pos, final int size, final int max) {
    if (pos + size > max) {
      return max - size;
    }
    return pos;
  }

  public void deactivate() {
  }
}


