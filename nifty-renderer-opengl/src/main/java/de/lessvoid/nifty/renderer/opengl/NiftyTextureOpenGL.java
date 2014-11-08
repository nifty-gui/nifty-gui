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
package de.lessvoid.nifty.renderer.opengl;

import java.nio.ByteBuffer;

import de.lessvoid.coregl.ColorFormat;
import de.lessvoid.coregl.CoreTexture2D;
import de.lessvoid.coregl.ResizeFilter;
import de.lessvoid.coregl.Type;
import de.lessvoid.coregl.spi.CoreGL;
import de.lessvoid.nifty.api.NiftyResourceLoader;
import de.lessvoid.nifty.internal.render.io.ImageLoader;
import de.lessvoid.nifty.internal.render.io.ImageLoaderFactory;
import de.lessvoid.nifty.spi.NiftyTexture;

public class NiftyTextureOpenGL implements NiftyTexture {
  final CoreTexture2D texture;

  private NiftyTextureOpenGL(final CoreTexture2D texture) {
    this.texture = texture;
  }

  public static NiftyTextureOpenGL newTextureRGBA(
      final CoreGL gl,
      final int width,
      final int height,
      final boolean linear) {
    return new NiftyTextureOpenGL(CoreTexture2D.createEmptyTexture(gl, ColorFormat.RGBA, Type.UNSIGNED_BYTE, width, height, resizeFilter(linear)));
  }

  public static NiftyTextureOpenGL newTextureRed(
      final CoreGL gl,
      final int width,
      final int height,
      final boolean linear) {
    return new NiftyTextureOpenGL(CoreTexture2D.createEmptyTexture(gl, ColorFormat.Red, Type.UNSIGNED_BYTE, width, height, resizeFilter(linear)));
  }

  public NiftyTextureOpenGL(
      final CoreGL gl,
      final int width,
      final int height,
      final ByteBuffer data,
      final boolean linear) {
    texture = CoreTexture2D.createCoreTexture(gl, ColorFormat.RGBA, width, height, data, resizeFilter(linear));
  }

  public NiftyTextureOpenGL(
      final CoreGL gl,
      final NiftyResourceLoader resourceLoader,
      final String filename,
      final boolean linear) {
    try {
      ImageLoader imageLoader = ImageLoaderFactory.createImageLoader(filename);
      ByteBuffer data = imageLoader.loadAsByteBufferRGBA(resourceLoader.getResourceAsStream(filename));
      texture = CoreTexture2D.createCoreTexture(
          gl,
          ColorFormat.RGBA,
          imageLoader.getImageWidth(),
          imageLoader.getImageHeight(),
          data,
          resizeFilter(linear));
    } catch (Exception e) {
      throw new RuntimeException("Could not load image from file: [" + filename + "]", e);
    }
  }

  public void bind() {
    texture.bind();
  }

  public int getWidth() {
    return texture.getWidth();
  }

  public int getHeight() {
    return texture.getHeight();
  }

  @Override
  public int getAtlasId() {
    // FIXME add texture atlas support
    return texture.getTextureId();
  }

  @Override
  public double getU0() {
    return 0;
  }

  @Override
  public double getV0() {
    return 0;
  }

  @Override
  public double getV1() {
    return 1.0;
  }

  @Override
  public double getU1() {
    return 1.0;
  }

  private static ResizeFilter resizeFilter(final boolean linear) {
    if (linear) {
      return ResizeFilter.Linear;
    }
    return ResizeFilter.Nearest;
  }
}
