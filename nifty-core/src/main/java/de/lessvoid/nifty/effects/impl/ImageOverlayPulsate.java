package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.image.ImageModeFactory;
import de.lessvoid.nifty.render.image.ImageModeHelper;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.pulsate.Pulsator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * ImagePulsate - image color pulsate.
 *
 * @author void
 */
public class ImageOverlayPulsate implements EffectImpl {
  @Nullable
  private NiftyImage image;
  @Nullable
  private Pulsator pulsater;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    Screen screen = nifty.getCurrentScreen();
    if (screen == null) {
      return;
    }
    image = nifty.getRenderEngine().createImage(screen, parameter.getProperty("filename"), true);
    if (image == null) {
      return;
    }

    String areaProviderProperty = ImageModeHelper.getAreaProviderProperty(parameter);
    String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(parameter);
    if ((areaProviderProperty != null) || (renderStrategyProperty != null)) {
      image.setImageMode(ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty,
          renderStrategyProperty));
    }

    this.pulsater = new Pulsator(parameter, nifty.getTimeProvider());
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (image != null) {
      r.saveState(null);
      if (pulsater != null) {
        r.setColorAlpha(pulsater.update());
      }
      r.renderImage(image, element.getX(), element.getY(), element.getWidth(), element.getHeight());
      r.restoreState();
    }
  }

  @Override
  public void deactivate() {
    if (image != null) {
      image.dispose();
    }
  }
}
