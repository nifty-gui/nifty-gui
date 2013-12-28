package de.lessvoid.nifty.examples.helloworld;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyImageMode;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.time.TimeProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ScrollingPanel implements EffectImpl {
  private TimeProvider timeProvider;
  private long start;
  private float xspeed;
  private float yspeed;
  @Nullable
  private NiftyImage image;
  private float xoff;
  private float yoff;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    timeProvider = nifty.getTimeProvider();
    start = timeProvider.getMsTime();
    xspeed = Float.valueOf(parameter.getProperty("xspeed", "1000"));
    yspeed = Float.valueOf(parameter.getProperty("yspeed", "1000"));
    image = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(), parameter.getProperty("filename"), false);
    String imageMode = parameter.getProperty("imageMode", null);
    if (imageMode != null) {
      image.setImageMode(NiftyImageMode.valueOf(imageMode));
    }
    xoff = 0;
    yoff = 0;
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    long now = timeProvider.getMsTime();
    r.saveState(null);
    xoff = ((now - start) % xspeed / xspeed * image.getWidth()) % image.getWidth();
    yoff = ((now - start) % yspeed / yspeed * image.getHeight()) % image.getHeight();
    r.enableClip(element.getX(), element.getY(), element.getX() + element.getWidth(),
        element.getY() + element.getHeight());
    r.renderImage(
        image,
        element.getX() + (int) xoff - image.getWidth(),
        element.getY() + (int) yoff - image.getHeight(),
        element.getWidth() - (int) xoff + image.getWidth(),
        element.getHeight() - (int) yoff + image.getHeight());
    r.restoreState();
  }

  @Override
  public void deactivate() {
    image.dispose();
  }
}
