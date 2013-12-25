package de.lessvoid.nifty.effects.impl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.image.ImageMode;
import de.lessvoid.nifty.render.image.ImageModeFactory;
import de.lessvoid.nifty.render.image.ImageModeHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

/**
 * This can be applied to an image element. This will change the original image of the
 * element to the image given in the "active" attribute. When the effect gets deactivated
 * the image is being restored to the image given with the "inactive" attribute.
 *
 * @author void
 */
public class ChangeImage implements EffectImpl {
  @Nonnull
  private static final Logger log = Logger.getLogger(ChangeImage.class.getName());
  @Nonnull
  private Element element;
  @Nullable
  private NiftyImage activeImage;
  @Nullable
  private NiftyImage inactiveImage;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    this.element = element;
    this.activeImage = loadImage("active", nifty, parameter);
    this.inactiveImage = loadImage("inactive", nifty, parameter);
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    changeElementImage(activeImage);
  }

  @Override
  public void deactivate() {
    changeElementImage(inactiveImage);
    if (activeImage != null) {
      activeImage.dispose();
    }
    if (inactiveImage != null) {
      inactiveImage.dispose();
    }
  }

  @Nullable
  private NiftyImage loadImage(
      @Nonnull final String name,
      @Nonnull final Nifty nifty,
      @Nonnull final EffectProperties parameter) {
    NiftyImage image = createImage(name, nifty, parameter);
    if (image == null) {
      return null;
    }
    setImageMode(image, name, parameter);
    return image;
  }

  @Nullable
  private NiftyImage createImage(
      @Nonnull final String name,
      @Nonnull final Nifty nifty,
      @Nonnull final EffectProperties parameter) {
    return nifty.createImage(parameter.getProperty(name), false);
  }

  private void setImageMode(
      @Nonnull final NiftyImage image,
      @Nonnull final String name,
      @Nonnull final EffectProperties parameter) {
    String areaProviderProperty = getAreaProviderProperty(name, parameter);
    String renderStrategyProperty = getRenderStrategyProperty(name, parameter);

    if ((areaProviderProperty != null) || (renderStrategyProperty != null)) {
      image.setImageMode(createImageMode(areaProviderProperty, renderStrategyProperty));
    }
  }

  @Nullable
  private String getAreaProviderProperty(@Nonnull final String name, @Nonnull final EffectProperties parameter) {
    return ImageModeHelper.getAreaProviderProperty(getImageModeProperty(name, parameter));
  }

  @Nullable
  private String getRenderStrategyProperty(@Nonnull final String name, @Nonnull final EffectProperties parameter) {
    return ImageModeHelper.getRenderStrategyProperty(getImageModeProperty(name, parameter));
  }

  @Nullable
  private String getImageModeProperty(@Nonnull final String name, @Nonnull final EffectProperties parameter) {
    String imageModeProperty = null;

    if ("active".equals(name)) {
      imageModeProperty = parameter.getProperty("imageModeActive", null);
    } else if ("inactive".equals(name)) {
      imageModeProperty = parameter.getProperty("imageModeInactive", null);
    }

    if (imageModeProperty == null) {
      imageModeProperty = parameter.getProperty("imageMode", null);
    }

    return imageModeProperty;
  }

  @Nonnull
  private ImageMode createImageMode(
      @Nullable final String areaProviderProperty,
      @Nullable final String renderStrategyProperty) {
    return ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty, renderStrategyProperty);
  }

  private void changeElementImage(@Nullable final NiftyImage image) {
    ImageRenderer imageRenderer = element.getRenderer(ImageRenderer.class);
    if (imageRenderer == null) {
      log.warning("this effect can only be applied to images!");
      return;
    }
    imageRenderer.setImage(image);
  }
}
