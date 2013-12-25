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
import de.lessvoid.nifty.tools.Alpha;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

public class ImageOverlay implements EffectImpl {
  @Nonnull
  private static final Logger log = Logger.getLogger(ImageOverlay.class.getName());
  @Nullable
  private NiftyImage image;
  @Nullable
  private Alpha alpha;
  @Nullable
  private SizeValue inset;
  @Nullable
  private SizeValue width;
  @Nullable
  private SizeValue height;
  private boolean center;
  private boolean hideIfNotEnoughSpace;
  private boolean activeBeforeStartDelay; // this will render the effect even when using a startDelay value so that
  // it will already render before the startDelay

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    Screen screen = nifty.getCurrentScreen();
    if (screen == null) {
      log.severe("Can't activate effect while there is no active current screen.");
      return;
    }
    image = nifty.getRenderEngine().createImage(screen, parameter.getProperty("filename"), false);
    if (image == null) {
      log.severe("Image not found, image overlay effect will not work.");
      return;
    }

    String areaProviderProperty = ImageModeHelper.getAreaProviderProperty(parameter);
    String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(parameter);
    if ((areaProviderProperty != null) || (renderStrategyProperty != null)) {
      image.setImageMode(ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty,
          renderStrategyProperty));
    }

    alpha = new Alpha(parameter.getProperty("alpha", "#f"));
    inset = new SizeValue(parameter.getProperty("inset", "0px"));
    width = new SizeValue(parameter.getProperty("width", element.getWidth() + "px"));
    height = new SizeValue(parameter.getProperty("height", element.getHeight() + "px"));
    center = Boolean.valueOf(parameter.getProperty("center", "false"));
    hideIfNotEnoughSpace = Boolean.valueOf(parameter.getProperty("hideIfNotEnoughSpace", "false"));
    activeBeforeStartDelay = Boolean.valueOf(parameter.getProperty("activeBeforeStartDelay", "false"));
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (image == null || inset == null || width == null || height == null || alpha == null) {
      return;
    }
    if (!activeBeforeStartDelay && normalizedTime <= 0.0) {
      return;
    }
    int insetOffset = inset.getValueAsInt(element.getWidth());
    int imageX = element.getX() + insetOffset;
    int imageY = element.getY() + insetOffset;
    int imageWidth = width.getValueAsInt(element.getWidth()) - insetOffset * 2;
    int imageHeight = height.getValueAsInt(element.getHeight()) - insetOffset * 2;
    if (hideIfNotEnoughSpace) {
      if (imageWidth > element.getWidth() || imageHeight > element.getHeight()) {
        return;
      }
    }
    r.saveState(null);
    if (falloff != null) {
      r.setColorAlpha(alpha.multiply(falloff.getFalloffValue()).getAlpha());
    } else {
      if (!r.isColorAlphaChanged()) {
        r.setColorAlpha(alpha.getAlpha());
      }
    }
    if (center) {
      r.renderImage(image, element.getX() + (element.getWidth() - imageWidth) / 2,
          element.getY() + (element.getHeight() - imageHeight) / 2, imageWidth, imageHeight);
    } else {
      r.renderImage(image, imageX, imageY, imageWidth, imageHeight);
    }
    r.restoreState();
  }

  @Override
  public void deactivate() {
    if (image != null) {
      image.dispose();
    }
  }
}
