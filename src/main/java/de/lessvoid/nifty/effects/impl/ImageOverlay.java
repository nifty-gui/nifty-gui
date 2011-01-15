package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyImageMode;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Alpha;
import de.lessvoid.nifty.tools.SizeValue;

public class ImageOverlay implements EffectImpl {
  private NiftyImage image;
  private Alpha alpha;
  private SizeValue inset;
  private SizeValue width;
  private SizeValue height;
  private boolean center;
  private boolean hideIfNotEnoughSpace;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    image = nifty.getRenderEngine().createImage(parameter.getProperty("filename"), false);
    String imageMode = parameter.getProperty("imageMode", null);
    if (imageMode != null) {
      image.setImageMode(NiftyImageMode.valueOf(imageMode));
    }
    alpha = new Alpha(parameter.getProperty("alpha", "#f"));
    inset = new SizeValue(parameter.getProperty("inset", "0px"));
    width = new SizeValue(parameter.getProperty("width", element.getWidth() + "px"));
    height = new SizeValue(parameter.getProperty("height", element.getHeight() + "px"));
    center = Boolean.valueOf(parameter.getProperty("center", "false"));
    hideIfNotEnoughSpace = Boolean.valueOf(parameter.getProperty("hideIfNotEnoughSpace", "false"));
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
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
      r.setColorAlpha(alpha.mutiply(falloff.getFalloffValue()).getAlpha());
    } else {
      if (!r.isColorAlphaChanged()) {
        r.setColorAlpha(alpha.getAlpha());
      }
    }
    if (center) {
      r.renderImage(image, element.getX() + (element.getWidth() - imageWidth) / 2, element.getY() + (element.getHeight() - imageHeight) / 2, imageWidth, imageHeight);
    } else {
      r.renderImage(image, imageX, imageY, imageWidth, imageHeight);
    }
    r.restoreState();
  }

  public void deactivate() {
    image.dispose();
  }
}
