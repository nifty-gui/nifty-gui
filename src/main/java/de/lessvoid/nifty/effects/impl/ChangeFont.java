package de.lessvoid.nifty.effects.impl;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.spi.RenderFont;

/**
 * ChangeFont.
 * @author void
 */
public class ChangeFont implements EffectImpl {
  private RenderFont font;

  public void activate(final Nifty nifty, final Element element, final Properties parameter) {
    font = nifty.getRenderEngine().createFont(parameter.getProperty("font"));
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    r.setFont(font);
  }

  public void deactivate() {
  }
}
