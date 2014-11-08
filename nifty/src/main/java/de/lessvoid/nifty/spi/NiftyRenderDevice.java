/*
 * Copyright (c) 2014, Jens Hohmuth 
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

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.api.BlendMode;
import de.lessvoid.nifty.api.NiftyArcParameters;
import de.lessvoid.nifty.api.NiftyLineParameters;
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

  /**
   * @param lineParameters 
   */
  void pathBegin(NiftyLineParameters lineParameters);

  /**
   * Render lines from the given FloatBuffer containing the line vertices.
   * @param b the FloatBuffer containing the vertex data
   * @param lineParameters the LineParameters to render ALL lines in the FloatBuffer with
   */
  void pathLines(FloatBuffer b, NiftyLineParameters lineParameters);

  /**
   * Render arcs using the information given. You'll get a quad the size of the full arc with the following coordinates
   *
   *  (-1, -1)        ( 1, -1)
   *     +---------------+
   *     |               |
   *     |               |
   *     |       +       |
   *     |               |
   *     |               |
   *     +---------------+
   *  (-1, 1)         ( 1,  1)
   *
   * The quad will be the the size of the circle centered around the center of the circle. You'll get the following
   * per vertex informations for each circle / quad (4 vertices per quad): 
   * - 2 floats x and y vertex position
   * - 2 floats u and v "texture" coordinates with the coordinates given in the diagram above
   *
   * @param vertices the vertex data to render
   * @param arcParameters the parameters for all the arcs to be rendered
   */
  void pathArcs(FloatBuffer vertices, NiftyArcParameters arcParameters);

  /**
   * @param lineParameters 
   */
  void pathEnd(NiftyLineParameters lineParameters);
}
