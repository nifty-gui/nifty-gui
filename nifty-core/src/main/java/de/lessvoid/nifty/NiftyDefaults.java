package de.lessvoid.nifty;

import de.lessvoid.nifty.loaderv2.types.RegisterEffectType;

public class NiftyDefaults {
  private NiftyDefaults() {
  }

  public static void initDefaultEffects(final Nifty nifty) {
    nifty.registerEffect(new RegisterEffectType("alphaHide", "de.lessvoid.nifty.effects.impl.AlphaHide"));
    nifty.registerEffect(new RegisterEffectType("autoScroll", "de.lessvoid.nifty.effects.impl.AutoScroll"));
    nifty.registerEffect(new RegisterEffectType("border", "de.lessvoid.nifty.effects.impl.Border"));
    nifty.registerEffect(new RegisterEffectType("blendMode", "de.lessvoid.nifty.effects.impl.BlendMode"));
    nifty.registerEffect(new RegisterEffectType("changeColor", "de.lessvoid.nifty.effects.impl.ChangeColor"));
    nifty.registerEffect(new RegisterEffectType("changeFont", "de.lessvoid.nifty.effects.impl.ChangeFont"));
    nifty.registerEffect(new RegisterEffectType("changeImage", "de.lessvoid.nifty.effects.impl.ChangeImage"));
    nifty.registerEffect(new RegisterEffectType("changeMouseCursor", "de.lessvoid.nifty.effects.impl.ChangeMouseCursor"));
    nifty.registerEffect(new RegisterEffectType("clip", "de.lessvoid.nifty.effects.impl.Clip"));
    nifty.registerEffect(new RegisterEffectType("colorBar", "de.lessvoid.nifty.effects.impl.ColorBar"));
    nifty.registerEffect(new RegisterEffectType("colorPulsate", "de.lessvoid.nifty.effects.impl.ColorPulsate"));
    nifty.registerEffect(new RegisterEffectType("fade", "de.lessvoid.nifty.effects.impl.Fade"));
    nifty.registerEffect(new RegisterEffectType("fadeSound", "de.lessvoid.nifty.effects.impl.FadeSound"));
    nifty.registerEffect(new RegisterEffectType("fadeMusic", "de.lessvoid.nifty.effects.impl.FadeMusic"));
    nifty.registerEffect(new RegisterEffectType("focus", "de.lessvoid.nifty.effects.impl.Focus"));
    nifty.registerEffect(new RegisterEffectType("followMouse", "de.lessvoid.nifty.effects.impl.FollowMouse"));
    nifty.registerEffect(new RegisterEffectType("gradient", "de.lessvoid.nifty.effects.impl.Gradient"));
    nifty.registerEffect(new RegisterEffectType("hide", "de.lessvoid.nifty.effects.impl.Hide"));
    nifty.registerEffect(new RegisterEffectType("hint", "de.lessvoid.nifty.effects.impl.Hint"));
    nifty.registerEffect(new RegisterEffectType("imageOverlay", "de.lessvoid.nifty.effects.impl.ImageOverlay"));
    nifty.registerEffect(new RegisterEffectType("imageOverlayPulsate", "de.lessvoid.nifty.effects.impl.ImageOverlayPulsate"));
    nifty.registerEffect(new RegisterEffectType("imageSize", "de.lessvoid.nifty.effects.impl.ImageSize"));
    nifty.registerEffect(new RegisterEffectType("imageSizePulsate", "de.lessvoid.nifty.effects.impl.ImageSizePulsate"));
    nifty.registerEffect(new RegisterEffectType("move", "de.lessvoid.nifty.effects.impl.Move"));
    nifty.registerEffect(new RegisterEffectType("nop", "de.lessvoid.nifty.effects.impl.Nop"));
    nifty.registerEffect(new RegisterEffectType("particle", "de.lessvoid.nifty.effects.impl.Particle"));
    nifty.registerEffect(new RegisterEffectType("playMusic", "de.lessvoid.nifty.effects.impl.PlayMusic"));
    nifty.registerEffect(new RegisterEffectType("playSound", "de.lessvoid.nifty.effects.impl.PlaySound"));
    nifty.registerEffect(new RegisterEffectType("pulsate", "de.lessvoid.nifty.effects.impl.Pulsate"));
    nifty.registerEffect(new RegisterEffectType("remote", "de.lessvoid.nifty.effects.impl.Remote"));
    nifty.registerEffect(new RegisterEffectType("renderElement", "de.lessvoid.nifty.effects.impl.RenderElement"));
    nifty.registerEffect(new RegisterEffectType("renderQuad", "de.lessvoid.nifty.effects.impl.RenderQuad"));
    nifty.registerEffect(new RegisterEffectType("restoreState", "de.lessvoid.nifty.effects.impl.RestoreState"));
    nifty.registerEffect(new RegisterEffectType("saveState", "de.lessvoid.nifty.effects.impl.SaveState"));
    nifty.registerEffect(new RegisterEffectType("simpleHint", "de.lessvoid.nifty.effects.impl.SimpleHint"));
    nifty.registerEffect(new RegisterEffectType("shake", "de.lessvoid.nifty.effects.impl.Shake"));
    nifty.registerEffect(new RegisterEffectType("show", "de.lessvoid.nifty.effects.impl.Show"));
    nifty.registerEffect(new RegisterEffectType("textColor", "de.lessvoid.nifty.effects.impl.TextColor"));
    nifty.registerEffect(new RegisterEffectType("textColorAnimated", "de.lessvoid.nifty.effects.impl.TextColorAnimated"));
    nifty.registerEffect(new RegisterEffectType("textSize", "de.lessvoid.nifty.effects.impl.TextSize"));
    nifty.registerEffect(new RegisterEffectType("textSizePulsate", "de.lessvoid.nifty.effects.impl.TextSizePulsate"));
    nifty.registerEffect(new RegisterEffectType("updateScrollpanelPositionToDisplayElement", "de.lessvoid.nifty.controls.scrollbar.UpdateScrollpanelPositionToDisplayElement"));    
  }
}
