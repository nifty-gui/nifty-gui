/*
 * Copyright (c) 2015, Nifty GUI Community 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.spi;

import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyCompositeOperation;
import de.lessvoid.nifty.types.NiftyLineCapType;
import de.lessvoid.nifty.types.NiftyLineJoinType;
import de.lessvoid.niftyinternal.NiftyResourceLoader;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

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

  ////////////////////////////////////////////////////////////////////////////
  // Provide Display Properties
  ////////////////////////////////////////////////////////////////////////////

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

  ////////////////////////////////////////////////////////////////////////////
  // NiftyTexture Creation
  ////////////////////////////////////////////////////////////////////////////

  /**
   * Create a texture of the given width and height.
   *
   * @param width the width of the texture
   * @param height the height of the texture
   * @param filterMode the FilterMode to use
   * @return NiftyTexture
   */
  NiftyTexture createTexture(int width, int height, FilterMode filterMode);

  /**
   * Create a texture of the given width and height and initialize it with the given pixel data.
   *
   * @param width the width of the texture
   * @param height the height of the texture
   * @param data the pixel data of the texture
   * @param filterMode the FilterMode to use
   * @return NiftyTexture
   */
  NiftyTexture createTexture(int width, int height, ByteBuffer data, FilterMode filterMode);

  /**
   * Load an image and return a NiftyTexture from the image.
   *
   * @param filename the image filename to load
   * @param filterMode the filter mode to use for the loaded texture
   * @param preMultipliedAlphaMode the pre-multiplied alpha mode to use
   * @return NiftyTexture
   */
  NiftyTexture loadTexture(String filename, FilterMode filterMode, PreMultipliedAlphaMode preMultipliedAlphaMode);

  ////////////////////////////////////////////////////////////////////////////
  // Render
  ////////////////////////////////////////////////////////////////////////////

  /**
   * Nifty will call this before it issues render*() calls.
   */
  void beginRender();

  /**
   * Called after all render*() calls are done to end rendering.
   */
  void endRender();

  /**
   * Render a textured Quad with the given NiftyTexture using vertex data given in the FloatBuffer containing:
   * - 2 floats x and y vertex position
   * - 2 floats u and v texture coordinates
   * - 4 floats red, green, blue, alpha color values
   *
   * @param texture the NiftyTexture to render
   * @param vertices the vertex data to render
   */
  void renderTexturedQuads(NiftyTexture texture, FloatBuffer vertices);

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
   * @param x0 the x position of the first edge point
   * @param y0 the y position of the first edge point
   * @param x1 the x position of the second edge point
   * @param y1 the y position of the second edge point
   * @param colorStops the list of colorstops
   * @param vertices the vertex data to render
   */
  void renderLinearGradientQuads(double x0, double y0, double x1, double y1, List<ColorStop> colorStops, FloatBuffer vertices);

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

  ////////////////////////////////////////////////////////////////////////////
  // Mask or Matte Rendering
  ////////////////////////////////////////////////////////////////////////////

  /**
   * This call enables rendering to the internal hold full screen alpha texture.
   */
  void maskBegin();

  /**
   * This call stops rendering to the internal hold full screen alpha texture.
   */
  void maskEnd();

  /**
   * This call should clear the mask alpha texture.
   */
  void maskClear();

  /**
   * Render lines from the given FloatBuffer containing the line vertices into the internal alpha mask texture.
   *
   * @param b the FloatBuffer containing the vertex data
   * @param lineWidth the width of the line to render
   * @param lineCapType the line cap type to use
   * @param lineJoinType the line join type
   */
  void maskRenderLines(FloatBuffer b, float lineWidth, NiftyLineCapType lineCapType, NiftyLineJoinType lineJoinType);

  /**
   * Render the given outline given as the vertices on the outline and fill it. The vertex data consists of:
   * - 2 floats x and y vertex position
   *
   * @param vertices the vertex data to render
   */
  void maskRenderFill(final FloatBuffer vertices);

  ////////////////////////////////////////////////////////////////////////////
  // Composition related
  ////////////////////////////////////////////////////////////////////////////

  /**
   * Change the current composite operation (blend mode in GL) for subsequent render calls.
   * @param compositeOperation the new composite operation
   */
  void changeCompositeOperation(NiftyCompositeOperation compositeOperation);

  ////////////////////////////////////////////////////////////////////////////
  // Custom Shaders
  ////////////////////////////////////////////////////////////////////////////

  /**
   * Load a custom shader to be used for rendering later.
   * @param filename the filename of the shader code to load.
   * @return some kind of identification to activate this custom shader later for rendering
   */
  String loadCustomShader(String filename);

  /**
   * Activate the custom shader with the given shaderId
   * @param shaderId the shaderId to activate
   */
  void activateCustomShader(String shaderId);

  ////////////////////////////////////////////////////////////////////////////
  // Method specific to Text rendering and Font loading
  ////////////////////////////////////////////////////////////////////////////

  NiftyFont createFont(String name);

  ////////////////////////////////////////////////////////////////////////////
  // enums and types used in the NiftyRenderDevice
  ////////////////////////////////////////////////////////////////////////////

  /**
   * The type of filtering to use when loading a texture (Actually this is the filtering mode of the texture after
   * it has been loaded. It's named LoadTextureFilterMode because it can currently only be applied when loading a
   * texture).
   *
   * @author void
   */
  enum FilterMode {
    Nearest,
    Linear
  }

  /**
   * Specifies if loaded texture data already is pre-multiplied or if Nifty should pre-multiply it when loading.
   *
   * For best results texture data in Nifty should use pre-multiplied alpha!
   *
   * @author void
   *
   */
  enum PreMultipliedAlphaMode {
    UseAsIs,
    PreMultiplyAlpha
  }

  /**
   * When rendering renderLinearGradientQuads() the NiftyRenderDevice implementation gets a list of ColorStop instances.
   */
  interface ColorStop {
    NiftyColor getColor();
    float getStop();
  }
}
