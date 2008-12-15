package de.lessvoid.nifty;

import java.util.Map;

import de.lessvoid.nifty.loader.xpp3.elements.RegisterEffectType;

/**
 * NiftyDefaults.
 * @author void
 */
public final class NiftyDefaults {

  /**
   * you can't instantiate this class.
   */
  private NiftyDefaults() {
  }

  /**
   * register default effects.
   * @param effects map to register effects.
   */
  public static void initDefaultEffects(final Map < String, RegisterEffectType > effects) {
    effects.put("border", new RegisterEffectType("de.lessvoid.nifty.effects.impl.Border"));
    effects.put("changeFont", new RegisterEffectType("de.lessvoid.nifty.effects.impl.ChangeFont"));
    effects.put("clip", new RegisterEffectType("de.lessvoid.nifty.effects.impl.Clip"));
    effects.put("colorBar", new RegisterEffectType("de.lessvoid.nifty.effects.impl.ColorBar"));
    effects.put("colorPulsate", new RegisterEffectType("de.lessvoid.nifty.effects.impl.ColorPulsate"));
    effects.put("fade", new RegisterEffectType("de.lessvoid.nifty.effects.impl.Fade"));
    effects.put("focus", new RegisterEffectType("de.lessvoid.nifty.effects.impl.Focus"));
    effects.put("hide", new RegisterEffectType("de.lessvoid.nifty.effects.impl.Hide"));
    effects.put("hint", new RegisterEffectType("de.lessvoid.nifty.effects.impl.Hint"));
    effects.put("imageOverlay", new RegisterEffectType("de.lessvoid.nifty.effects.impl.ImageOverlay"));
    effects.put("imageOverlayPulsate", new RegisterEffectType("de.lessvoid.nifty.effects.impl.ImageOverlayPulsate"));
    effects.put("imageSize", new RegisterEffectType("de.lessvoid.nifty.effects.impl.ImageSize"));
    effects.put("imageSizePulsate", new RegisterEffectType("de.lessvoid.nifty.effects.impl.ImageSizePulsate"));
    effects.put("move", new RegisterEffectType("de.lessvoid.nifty.effects.impl.Move"));
    effects.put("nop", new RegisterEffectType("de.lessvoid.nifty.effects.impl.Nop"));
    effects.put("particle", new RegisterEffectType("de.lessvoid.nifty.effects.impl.Particle"));
    effects.put("playSound", new RegisterEffectType("de.lessvoid.nifty.effects.impl.PlaySound"));
    effects.put("pulsate", new RegisterEffectType("de.lessvoid.nifty.effects.impl.Pulsate"));
    effects.put("remote", new RegisterEffectType("de.lessvoid.nifty.effects.impl.Remote"));
    effects.put("renderElement", new RegisterEffectType("de.lessvoid.nifty.effects.impl.RenderElement"));
    effects.put("restoreState", new RegisterEffectType("de.lessvoid.nifty.effects.impl.RestoreState"));
    effects.put("saveState", new RegisterEffectType("de.lessvoid.nifty.effects.impl.SaveState"));
    effects.put("shake", new RegisterEffectType("de.lessvoid.nifty.effects.impl.Shake"));
    effects.put("textColor", new RegisterEffectType("de.lessvoid.nifty.effects.impl.TextColor"));
    effects.put("textSize", new RegisterEffectType("de.lessvoid.nifty.effects.impl.TextSize"));
    effects.put("textSizePulsate", new RegisterEffectType("de.lessvoid.nifty.effects.impl.TextSizePulsate"));
  }
}
