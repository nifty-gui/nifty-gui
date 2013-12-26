package de.lessvoid.nifty.examples.tutorial;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.time.TimeProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.logging.Logger;

public class BubblesEffect implements EffectImpl {
  private static final Logger log = Logger.getLogger(BubblesEffect.class.getName());
  @Nonnull
  private final Bubble[] bubbles = new Bubble[32];
  private int screenWidth;
  private int screenHeight;
  @Nonnull
  private final Random random = new Random();
  @Nullable
  private TimeProvider timeProvider;
  @Nullable
  private NiftyImage niftyImage1;
  @Nullable
  private NiftyImage niftyImage2;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    final Screen currentScreen = nifty.getCurrentScreen();
    if (currentScreen == null) {
      log.severe("Can't active BubblesEffect without current screen!");
    } else {
      timeProvider = nifty.getTimeProvider();
      niftyImage1 = nifty.getRenderEngine().createImage(currentScreen, "tutorial/bubble-64x64.png", true);
      niftyImage2 = nifty.getRenderEngine().createImage(currentScreen, "tutorial/bubble-32x32.png", true);
      screenWidth = currentScreen.getRootElement().getWidth();
      screenHeight = currentScreen.getRootElement().getHeight();
      for (int i = 0; i < bubbles.length; i++) {
        bubbles[i] = new Bubble(timeProvider.getMsTime());
      }
    }

  }

  @Override
  public void deactivate() {
    if (niftyImage1 != null) {
      niftyImage1.dispose();
    }
    if (niftyImage2 != null) {
      niftyImage2.dispose();
    }
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float effectTime,
      final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (timeProvider != null) {
      for (@Nullable Bubble bubble : bubbles) {
        if (bubble != null) {
          bubble.update(timeProvider.getMsTime());
          bubble.render(r);
        }
      }
    }
  }

  private class Bubble {
    private static final int Y_MIN_1 = 100;
    private static final int Y_MIN_2 = 140;
    private static final int Y_MAX_1 = 768 - 140;
    private static final int Y_MAX_2 = 768 - 100;
    private int x;
    private int initialX;
    private int y;
    private int sizeX;
    private int sizeY;
    private long speed;
    private boolean enabled = true;
    private long startTime;
    private int speedX;
    private int dir;
    private int timeX;
    private int image;

    public Bubble(final long startTime) {
      initPosition(startTime);
      y = screenHeight;
      speed = getNewSpeed();
      this.startTime = startTime + random.nextInt((int) speed);
    }

    private int getNewSpeed() {
      return random.nextInt(5000) + 8000;
    }

    private void initPosition(final long currentTime) {
      initialX = x = random.nextInt(screenWidth);
      speed = getNewSpeed();
      initWind();
      startTime = currentTime;
      image = random.nextInt(2);
      if (image == 0) {
        sizeX = 64;
        sizeY = 64;
      } else {
        sizeX = 32;
        sizeY = 32;
      }
      y = screenHeight;
    }

    private void initWind() {
      dir = random.nextInt(2) * -1;
      speedX = random.nextInt(150) + 150;
      timeX = random.nextInt(5000) + 2500;
    }

    public void update(final long currentTime) {
      long time = currentTime - startTime;
      float t = getSinusValue(currentTime);
      x = (int) (t * speedX * dir + initialX);

      float value = (speed - time) / (float) speed;
      y = (int) (screenHeight * value);
      if (y < Y_MIN_1) {
        initPosition(currentTime);
        enabled = true;
      }
    }

    public void render(@Nonnull final NiftyRenderEngine r) {
      if (enabled) {
        if (y < Y_MIN_1) {
          return;
        } else if (y < Y_MIN_2) {
          r.setColorAlpha(1.0f - (140f - y) / 40f);
        } else if (y > Y_MAX_1 && y < Y_MAX_2) {
          float max = Y_MAX_1;
          r.setColorAlpha(1.0f - (y - max) / 40f);
        } else if (y >= Y_MAX_2) {
          return;
        } else {
          r.setColorAlpha(1.0f);
        }
        if (image == 0) {
          if (niftyImage1 != null) {
            r.renderImage(niftyImage1, x, y, sizeX, sizeY);
          }
        } else {
          if (niftyImage2 != null) {
            r.renderImage(niftyImage2, x, y, sizeX, sizeY);
          }
        }

      }
    }

    private float getSinusValue(final long x) {
      return (float) ((Math.sin(Math.PI * x / timeX) + 1.0f) / 2.0f);
    }
  }
}
