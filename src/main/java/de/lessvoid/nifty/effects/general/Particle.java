package de.lessvoid.nifty.effects.general;

import java.io.IOException;
import java.util.Properties;

import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderEngine;
import de.lessvoid.nifty.render.RenderState;

/**
 * Particle System effect. FIXME PROVE OF CONCEPT. NOT DONE!
 * @author void
 */
public class Particle implements EffectImpl {

  /**
   * the slick ParticleSystem to use.
   */
  private ParticleSystem system;

  /**
   * Initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Parameter
   */
  public void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    try {
      system = ParticleIO.loadConfiguredSystem("explosion.xml");
      system.setPosition(element.getX(), element.getY());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * execute the effect.
   * @param element Element
   * @param normalizedTime TimeInterpolator
   * @param r RenderDevice
   */
  public void execute(final Element element, final float normalizedTime, final RenderEngine r) {
    r.saveState(RenderState.allStates());
    r.enableBlend();
    system.render();
    r.restoreState();
  }
}
