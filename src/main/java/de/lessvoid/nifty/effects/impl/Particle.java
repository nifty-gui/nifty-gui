package de.lessvoid.nifty.effects.impl;

import java.io.IOException;

import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;

/**
 * Particle System effect. FIXME PROVE OF CONCEPT. NOT DONE!
 * @author void
 */
public class Particle implements EffectImpl {

  private ParticleSystem system;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    try {
      system = ParticleIO.loadConfiguredSystem("explosion.xml");
      system.setPosition(element.getX(), element.getY());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    r.saveState(RenderStateType.allStates());
    system.render();
    r.restoreState();
  }

  public void deactivate() {
  }
}
