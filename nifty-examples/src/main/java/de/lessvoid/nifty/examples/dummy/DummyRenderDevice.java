package de.lessvoid.nifty.examples.dummy;

import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;
import de.lessvoid.nifty.types.*;
import de.lessvoid.niftyinternal.NiftyResourceLoader;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

/**
 * This is the dummy render device implementation. It actually does nothing.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class DummyRenderDevice implements NiftyRenderDevice {
  private final int height;
  private final int width;
  private final int fpsLimit;
  private long lastRenderTime;

  private NiftyResourceLoader loader;

  public DummyRenderDevice(int width, int height) {
    this(width, height, -1);
  }

  public DummyRenderDevice(int width, int height, int fpsLimit) {
    this.height = height;
    this.width = width;
    this.fpsLimit = fpsLimit;

    lastRenderTime = -1L;
  }

  @Override
  public void setResourceLoader(@Nonnull NiftyResourceLoader niftyResourceLoader) {
    loader = niftyResourceLoader;
  }

  @Override
  public int getDisplayWidth() {
    return width;
  }

  @Override
  public int getDisplayHeight() {
    return height;
  }

  @Override
  public void clearScreenBeforeRender(boolean clearScreenBeforeRender) {
  }

  @Override
  public NiftyTexture createTexture(int width, int height, FilterMode filterMode) {
    return new DummyTexture(width, height);
  }

  @Override
  public NiftyTexture createTexture(int width, int height, ByteBuffer data, FilterMode filterMode) {
    return createTexture(width, height, filterMode);
  }

  @Override
  public NiftyTexture loadTexture(String filename, FilterMode filterMode, PreMultipliedAlphaMode preMultipliedAlphaMode) {
    if (loader == null) throw new IllegalStateException("loader is not set yet");
    try (InputStream stream = loader.getResourceAsStream(filename)) {
      if (stream != null) {
        BufferedImage image = ImageIO.read(stream);
        return createTexture(image.getWidth() , image.getHeight(), filterMode);
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not load image from file: [" + filename + "]", e);
    }
    throw new RuntimeException("Could not load image from file: [" + filename + "]");
  }

  @Override
  public void beginRender() {
    if (fpsLimit > -1) {
      if (lastRenderTime != -1) {
        long diff = System.currentTimeMillis() - lastRenderTime;
        double expected = 1000.0 / fpsLimit;
        if (diff < expected) {
          try {
            Thread.sleep((long) (expected - diff));
          } catch (InterruptedException ignored) {
          }
        }
      }

      lastRenderTime = System.currentTimeMillis();
    }
  }

  @Override
  public void renderTexturedQuads(NiftyTexture texture, FloatBuffer vertices) {
  }

  @Override
  public void renderColorQuads(FloatBuffer vertices) {
  }

  @Override
  public void renderLinearGradientQuads(double x0, double y0, double x1, double y1, List<NiftyColorStop> colorStops, FloatBuffer vertices) {
  }

  @Override
  public void endRender() {
  }

  @Override
  public void beginRenderToTexture(NiftyTexture texture) {
  }

  @Override
  public void endRenderToTexture(NiftyTexture texture) {
  }

  @Override
  public void maskBegin() {

  }

  @Override
  public void maskEnd() {

  }

  @Override
  public void maskClear() {
  }

  @Override
  public void changeCompositeOperation(NiftyCompositeOperation compositeOperation) {
  }

  @Override
  public String loadCustomShader(String filename) {
    return null;
  }

  @Override
  public void activateCustomShader(String shaderId) {
  }

  @Override
  public void maskRenderLines(FloatBuffer b, float lineWidth, NiftyLineCapType lineCapType, NiftyLineJoinType lineJoinType) {
  }

  @Override
  public void maskRenderFill(FloatBuffer vertices) {
  }

}
