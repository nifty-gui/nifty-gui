package de.lessvoid.nifty.spi;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.api.BlendMode;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.api.NiftyResourceLoader;

/**
 * NiftyRenderDevice is part of the SPI that allows Nifty to use different graphics backends. Everything that can
 * be rendered will go through implementations of this interface.
 *
 * @author void
 */
public interface NiftyRenderDevice {

  /**
   * Gives this RenderDevice access to the NiftyResourceLoader.
   *
   * @param niftyResourceLoader NiftyResourceLoader
   */
  void setResourceLoader(@Nonnull NiftyResourceLoader niftyResourceLoader);

  /**
   * Get the width of the display.
   * @return width of display mode
   */
  int getDisplayWidth();

  /**
   * Get the height of the display.
   * @return height of display mode
   */
  int getDisplayHeight();

  /**
   * Enable clearing the screen when rendering.
   */
  void clearScreenBeforeRender(boolean clearScreenBeforeRender);

  /**
   * Create a texture of the given width and height.
   *
   * @param width the width of the texture
   * @param height the height of the texture
   * @return NiftyTexture
   */
  NiftyTexture createTexture(int width, int height, boolean filterLinear);

  /**
   * Create a texture of the given width and height and initialize it with the given pixel data.
   *
   * @param width the width of the texture
   * @param height the height of the texture
   * @param data the pixel data of the texture
   * @return NiftyTexture
   */
  NiftyTexture createTexture(int width, int height, ByteBuffer data, boolean filterLinear);

  /**
   * Load an image and return a NiftyTexture from the image.
   *
   * @param filename the image filename to load
   * @return NiftyTexture
   */
  NiftyTexture loadTexture(String filename, boolean filterLinear);

  void render(NiftyTexture renderTarget, FloatBuffer vertices);
  void renderColorQuads(FloatBuffer vertices);
  void renderLinearGradientQuads(NiftyLinearGradient gradientParams, FloatBuffer vertices);

  void begin();
  void end();

  void beginRenderToTexture(NiftyTexture texture);
  void endRenderToTexture(NiftyTexture texture);

  void changeBlendMode(BlendMode blendMode);


}
