package de.lessvoid.nifty.effects;

import java.util.Set;

import de.lessvoid.nifty.effects.general.Effect;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;

/**
 * The EffectProcessor Interface for all EffectProcessors.
 * @author void
 */
public interface EffectProcessor {

  /**
   * register an effect as pre effect.
   * @param e the effect
   */
  void registerEffect(Effect e);

  /**
   * render pre effects.
   * @param renderDevice the renderDevice we should use.
   */
  void renderPre(final NiftyRenderEngine renderDevice);

  /**
   * render post effects.
   * @param renderDevice the renderDevice we should use.
   */
  void renderPost(final NiftyRenderEngine renderDevice);

  /**
   * Should return a set of all RenderStates that should be saved before doing
   * any rendering.
   * @return set of RenderState to save
   */
  Set < RenderStateType > getRenderStatesToSave();

  /**
   * checks if the effects are still active.
   * @return true, if active, false otherwise
   */
  boolean isActive();

  /**
   * reset this processor.
   */
  void reset();

  /**
   * get state string.
   * @return String with state information
   */
  String getStateString();
}
