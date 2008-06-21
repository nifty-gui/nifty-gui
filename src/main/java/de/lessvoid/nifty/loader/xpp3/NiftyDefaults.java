package de.lessvoid.nifty.loader.xpp3;

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
    // general
    effects.put("imageSize", new RegisterEffectType("de.lessvoid.nifty.effects.general.ImageSize"));
    effects.put("colorBar", new RegisterEffectType("de.lessvoid.nifty.effects.general.ColorBar"));
    effects.put("pulsate", new RegisterEffectType("de.lessvoid.nifty.effects.general.Pulsate"));
    effects.put("ImageOverlay", new RegisterEffectType("de.lessvoid.nifty.effects.general.ImageOverlay"));
    effects.put("ImageOverlayPulsate", new RegisterEffectType("de.lessvoid.nifty.effects.general.ImageOverlayPulsate"));
    effects.put("fade", new RegisterEffectType("de.lessvoid.nifty.effects.general.Fade"));
    effects.put("nop", new RegisterEffectType("de.lessvoid.nifty.effects.general.Nop"));
    effects.put("fade", new RegisterEffectType("de.lessvoid.nifty.effects.general.Fade"));
    effects.put("hide", new RegisterEffectType("de.lessvoid.nifty.effects.general.Hide"));
    effects.put("shake", new RegisterEffectType("de.lessvoid.nifty.effects.general.Shake"));
    effects.put("playSound", new RegisterEffectType("de.lessvoid.nifty.effects.general.PlaySound"));
    effects.put("move", new RegisterEffectType("de.lessvoid.nifty.effects.general.Move"));
    effects.put("changeFont", new RegisterEffectType("de.lessvoid.nifty.effects.general.ChangeFont"));
    effects.put("colorPulsate", new RegisterEffectType("de.lessvoid.nifty.effects.general.ColorPulsate"));
    effects.put("saveState", new RegisterEffectType("de.lessvoid.nifty.effects.general.SaveState"));
    effects.put("restoreState", new RegisterEffectType("de.lessvoid.nifty.effects.general.RestoreState"));
    effects.put("renderElement", new RegisterEffectType("de.lessvoid.nifty.effects.general.RenderElement"));

    // hover
    effects.put("hoverColorBar", new RegisterEffectType("de.lessvoid.nifty.effects.hover.ColorBar"));
    effects.put("hoverPulsate", new RegisterEffectType("de.lessvoid.nifty.effects.hover.Pulsate"));
    effects.put("hoverHint", new RegisterEffectType("de.lessvoid.nifty.effects.hover.Hint"));
    effects.put("hoverImageOverlay", new RegisterEffectType("de.lessvoid.nifty.effects.hover.ImageOverlay"));
    effects.put("hoverColorPulsate", new RegisterEffectType("de.lessvoid.nifty.effects.hover.ColorPulsate"));
    effects.put("hoverChangeFont", new RegisterEffectType("de.lessvoid.nifty.effects.hover.ChangeFont"));
    effects.put("hoverSaveState", new RegisterEffectType("de.lessvoid.nifty.effects.hover.SaveState"));
    effects.put("hoverRestoreState", new RegisterEffectType("de.lessvoid.nifty.effects.hover.RestoreState"));
    effects.put("hoverRenderElement", new RegisterEffectType("de.lessvoid.nifty.effects.hover.RenderElement"));
  }
}
