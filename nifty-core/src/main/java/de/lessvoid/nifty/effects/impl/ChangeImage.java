package de.lessvoid.nifty.effects.impl;

import java.util.logging.Logger;

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

/**
 * This can be applied to an image element. This will change the original image of the
 * element to the image given in the "active" attribute. When the effect gets deactivated
 * the image is being restored to the image given with the "inactive" attribute.
 * @author void
 */
public class ChangeImage implements EffectImpl {
  private static Logger log = Logger.getLogger(ChangeImage.class.getName());
  private Element element;
  private NiftyImage activeImage;
  private NiftyImage inactiveImage;

  @Override
  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    this.element = element;
    this.activeImage = loadImage("active", nifty, parameter);
    this.inactiveImage = loadImage("inactive", nifty, parameter);
  }

  @Override
  public void execute(final Element element, final float normalizedTime, final Falloff falloff, final NiftyRenderEngine r) {
    changeElementImage(activeImage);
  }

  @Override
  public void deactivate() {
    changeElementImage(inactiveImage);
    activeImage.dispose();
    inactiveImage.dispose();
  }

  private NiftyImage loadImage(final String name, final Nifty nifty, final EffectProperties parameter) {
    NiftyImage image = createImage(name, nifty, parameter);
    setImageMode(image, name, parameter);
    return image;
  }

  private NiftyImage createImage(final String name, final Nifty nifty, final EffectProperties parameter) {
    return nifty.createImage(parameter.getProperty(name), false);
  }

  private void setImageMode(final NiftyImage image, final String name, final EffectProperties parameter) {
    String areaProviderProperty = getAreaProviderProperty(name, parameter);
    String renderStrategyProperty = getRenderStrategyProperty(name, parameter);

    if ((areaProviderProperty != null) || (renderStrategyProperty != null)) {
      image.setImageMode(createImageMode(areaProviderProperty, renderStrategyProperty));
    }
  }

  private String getAreaProviderProperty(final String name, final EffectProperties parameter) {
    return ImageModeHelper.getAreaProviderProperty(getImageModeProperty(name, parameter));
  }

  private String getRenderStrategyProperty(final String name, final EffectProperties parameter) {
    return ImageModeHelper.getRenderStrategyProperty(getImageModeProperty(name, parameter));
  }

  private String getImageModeProperty(final String name, final EffectProperties parameter) {
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

  private ImageMode createImageMode (final String areaProviderProperty, final String renderStrategyProperty) {
    return ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty, renderStrategyProperty);
  }

  private void changeElementImage(final NiftyImage image) {
    ImageRenderer imageRenderer = element.getRenderer(ImageRenderer.class);
    if (imageRenderer == null) {
      log.warning("this effect can only be applied to images!");
      return;
    }
    imageRenderer.setImage(image);
  }
}
