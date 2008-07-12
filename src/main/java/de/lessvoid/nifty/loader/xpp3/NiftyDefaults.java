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
    effects.put("changeFont", new RegisterEffectType("de.lessvoid.nifty.effects.general.ChangeFont"));
    effects.put("colorBar", new RegisterEffectType("de.lessvoid.nifty.effects.general.ColorBar"));
    effects.put("colorPulsate", new RegisterEffectType("de.lessvoid.nifty.effects.general.ColorPulsate"));
    effects.put("fade", new RegisterEffectType("de.lessvoid.nifty.effects.general.Fade"));
    effects.put("hide", new RegisterEffectType("de.lessvoid.nifty.effects.general.Hide"));
    effects.put("ImageOverlay", new RegisterEffectType("de.lessvoid.nifty.effects.general.ImageOverlay"));
    effects.put("ImageOverlayPulsate", new RegisterEffectType("de.lessvoid.nifty.effects.general.ImageOverlayPulsate"));
    effects.put("imageSize", new RegisterEffectType("de.lessvoid.nifty.effects.general.ImageSize"));
    effects.put("move", new RegisterEffectType("de.lessvoid.nifty.effects.general.Move"));
    effects.put("nop", new RegisterEffectType("de.lessvoid.nifty.effects.general.Nop"));
    effects.put("particle", new RegisterEffectType("de.lessvoid.nifty.effects.general.Particle"));
    effects.put("playSound", new RegisterEffectType("de.lessvoid.nifty.effects.general.PlaySound"));
    effects.put("pulsate", new RegisterEffectType("de.lessvoid.nifty.effects.general.Pulsate"));
    effects.put("renderElement", new RegisterEffectType("de.lessvoid.nifty.effects.general.RenderElement"));
    effects.put("restoreState", new RegisterEffectType("de.lessvoid.nifty.effects.general.RestoreState"));
    effects.put("saveState", new RegisterEffectType("de.lessvoid.nifty.effects.general.SaveState"));
    effects.put("shake", new RegisterEffectType("de.lessvoid.nifty.effects.general.Shake"));
    effects.put("textSize", new RegisterEffectType("de.lessvoid.nifty.effects.general.TextSize"));

    // hover
    effects.put("hoverChangeFont", new RegisterEffectType("de.lessvoid.nifty.effects.hover.ChangeFont"));
    effects.put("hoverColorBar", new RegisterEffectType("de.lessvoid.nifty.effects.hover.ColorBar"));
    effects.put("hoverColorPulsate", new RegisterEffectType("de.lessvoid.nifty.effects.hover.ColorPulsate"));
    effects.put("hoverFocus", new RegisterEffectType("de.lessvoid.nifty.effects.hover.Focus"));
    effects.put("hoverHint", new RegisterEffectType("de.lessvoid.nifty.effects.hover.Hint"));
    effects.put("hoverImageOverlay", new RegisterEffectType("de.lessvoid.nifty.effects.hover.ImageOverlay"));
    effects.put("hoverImageSize", new RegisterEffectType("de.lessvoid.nifty.effects.hover.ImageSize"));
    effects.put("hoverImageSizePulsate", new RegisterEffectType("de.lessvoid.nifty.effects.hover.ImageSizePulsate"));
    effects.put("hoverPulsate", new RegisterEffectType("de.lessvoid.nifty.effects.hover.Pulsate"));
    effects.put("hoverRenderElement", new RegisterEffectType("de.lessvoid.nifty.effects.hover.RenderElement"));
    effects.put("hoverRestoreState", new RegisterEffectType("de.lessvoid.nifty.effects.hover.RestoreState"));
    effects.put("hoverSaveState", new RegisterEffectType("de.lessvoid.nifty.effects.hover.SaveState"));
    effects.put("hoverTextColor", new RegisterEffectType("de.lessvoid.nifty.effects.hover.TextColor"));
    effects.put("hoverTextSize", new RegisterEffectType("de.lessvoid.nifty.effects.hover.TextSize"));
    effects.put("hoverTextSizePulsate", new RegisterEffectType("de.lessvoid.nifty.effects.hover.TextSizePulsate"));
  }
}
