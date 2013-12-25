package de.lessvoid.nifty.examples.tutorial;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyImageMode;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class SnowEffect implements EffectImpl {
  @Nonnull
  private final Snowflake[] snow = new Snowflake[256];
  private int screenWidth;
  private int screenHeight;
  @Nonnull
  private final Random random = new Random();
  @Nonnull
  private final TimeProvider timeProvider = new AccurateTimeProvider();
  @Nullable
  private NiftyImage niftyImage;
  @Nonnull
  private final NiftyImageMode image0 = NiftyImageMode.subImage(0, 0, 1, 1);
  @Nonnull
  private final NiftyImageMode image1 = NiftyImageMode.subImage(2, 0, 3, 3);
  @Nonnull
  private final NiftyImageMode image2 = NiftyImageMode.subImage(6, 0, 4, 4);
  @Nonnull
  private final NiftyImageMode image3 = NiftyImageMode.subImage(0, 4, 4, 4);
  @Nonnull
  private final NiftyImageMode image4 = NiftyImageMode.subImage(5, 5, 5, 5);

  @Override
  public void activate(@Nonnull final Nifty nifty, @Nonnull final Element element, @Nonnull final EffectProperties parameter) {
    niftyImage = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(), "tutorial/snow.png", true);
    screenWidth = nifty.getCurrentScreen().getRootElement().getWidth();
    screenHeight = nifty.getCurrentScreen().getRootElement().getHeight();
    for (int i=0; i<snow.length; i++) {
      snow[i] = new Snowflake(timeProvider.getMsTime());
    }
  }

  @Override
  public void deactivate() {
    niftyImage.dispose();
  }

  @Override
  public void execute(@Nonnull final Element element, final float effectTime, final Falloff falloff, @Nonnull final NiftyRenderEngine r) {
    for (Snowflake snowflake : snow) {
      snowflake.update(timeProvider.getMsTime());
      snowflake.render(r);
    }
  }

  private class Snowflake {
    private int x;
    private int initialX;
    private int y;
    private NiftyImageMode imageMode;
    private int sizeX;
    private int sizeY;
    private long speed;
    private boolean enabled = true;
    private long startTime;
    private int speedX;
    private int dir;
    private int timeX;

    public Snowflake(final long startTime) {
      initPosition(startTime);
      y = random.nextInt(screenHeight);
      speed = getNewSpeed();
      this.startTime = startTime + random.nextInt((int)speed);
    }

    private int getNewSpeed() {
      return random.nextInt(5000) + 8000;
    }

    private void initPosition(final long currentTime) {
      initialX = x = random.nextInt(screenWidth);
      switch(random.nextInt(5)) {
        case 0:
          imageMode = image0;
          sizeX = 1;
          sizeY = 1;
          break;
        case 1:
          imageMode = image1;
          sizeX = 3;
          sizeY = 3;
          break;
        case 2:
          imageMode = image2;
          sizeX = 4;
          sizeY = 4;
          break;
        case 3:
          imageMode = image3;
          sizeX = 4;
          sizeY = 4;
          break;
        case 4:
          imageMode = image4;
          sizeX = 5;
          sizeY = 5;
          break;
      }
      speed = getNewSpeed();
      initWind();
      startTime = currentTime;
    }

    private void initWind() {
      dir = random.nextInt(2) * -1;
      speedX = random.nextInt(50) + 50;
      timeX = random.nextInt(5000) + 2500;
    }

    public void update(final long currentTime) {
      long time = currentTime - startTime;
      float t = getSinusValue(currentTime);
      x = (int)(t * speedX * dir + initialX);
      float value = (float)(1.0 - (speed - time) / (float)speed);
      y = (int)(screenHeight * value);
      if (y > screenHeight) {
        initPosition(currentTime);
        enabled = true;
      }
    }

    public void render(@Nonnull final NiftyRenderEngine r) {
      if (enabled) {
        niftyImage.setImageMode(imageMode);
        if (y < 100) {
          return;
        } else if (y < 140) {
          r.setColorAlpha(1.0f - (140f - y) / 40f);
        } else if (y > (768 - 140) && y < (768 - 100)) {
          float max = 768 - 140;
          r.setColorAlpha(1.0f - (y - max) / 40f);
        } else if (y >= (768 - 100)) {
          return;
        } else {
          r.setColorAlpha(1.0f);
        }
        r.renderImage(niftyImage, x, y, sizeX, sizeY);
      }
    }

    private float getSinusValue(final long x) {
      return (float) ((Math.sin(Math.PI * x / timeX) + 1.0f) / 2.0f);
    }
  }
}
