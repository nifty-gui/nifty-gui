package de.lessvoid.nifty.effects;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Effect Implementation ... here is the actual fun :>
 *
 * @author void
 */
public interface EffectImpl {

  /**
   * initialize effect.
   *
   * @param nifty     Nifty
   * @param element   Element
   * @param parameter parameters
   */
  void activate(@Nonnull Nifty nifty, @Nonnull Element element, @Nonnull EffectProperties parameter);

  /**
   * execute the effect.
   *
   * @param element    the Element
   * @param effectTime current effect time
   * @param falloff    the Falloff class for hover effects. This is supposed to be null for none hover effects.
   * @param r          RenderDevice to use
   */
  void execute(@Nonnull Element element, float effectTime, @Nullable Falloff falloff, @Nonnull NiftyRenderEngine r);

  /**
   * deactivate the effect.
   */
  void deactivate();
}
