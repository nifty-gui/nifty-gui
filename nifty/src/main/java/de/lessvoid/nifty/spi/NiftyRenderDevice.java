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

  /**
   * Nifty will call this before it issues render*() calls.
   */
  void beginRender();

  /**
   * Render a textured Quad with the given NiftyTexture using vertex data given in the FloatBuffer containing:
   * - 2 floats x and y vertex position
   * - 2 floats u and v texture coordinates
   * - 4 floats red, green, blue, alpha color values
   *
   * @param texture the NiftyTexture to render
   * @param vertices the vertex data to render
   */
  void render(NiftyTexture texture, FloatBuffer vertices);

  /**
   * Render an untextured Quad with the vertex data given in the FloatBuffer containing:
   * - 2 floats x and y vertex position
   * - 4 floats red, green, blue, alpha color values for the vertex
   *
   * @param vertices the vertex data to render
   */
  void renderColorQuads(FloatBuffer vertices);

  /**
   * Render a linear gradient with the given parameters using vertex data given in the FloatBuffer containing:
   * - 2 floats x and y vertex position
   *
   * @param gradientParams the NiftyLinearGradient containing the linear gradient parameters to use
   * @param vertices the vertex data to render
   */
  void renderLinearGradientQuads(NiftyLinearGradient gradientParams, FloatBuffer vertices);

  /**
   * Called after all render*() calls are done to end rendering.
   */
  void endRender();

  /**
   * Redirect all subsequent render calls to the NiftyTexture given as a parameter. 
   * @param texture the texture to use
   */
  void beginRenderToTexture(NiftyTexture texture);

  /**
   * Stop rendering to the given texture.
   * @param texture the texture to use in the beginRenderToTexture() call to start rendering.
   */
  void endRenderToTexture(NiftyTexture texture);

  /**
   * Change the current BlendMode for subsequent render calls.
   * @param blendMode the BlendMode
   */
  void changeBlendMode(BlendMode blendMode);

  /**
   * Load a custom shader to be used for rendering later.
   * @param filename the filename of the shader code to load.
   * @return some kind of identification to activate this custom shader later for rendering
   */
  public String loadCustomShader(String filename);

  /**
   * Activate the custom shader with the given shaderId
   * @param shaderId the shaderId to activate
   */
  public void activateCustomShader(String shaderId);
}
