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


import java.nio.ByteBuffer;

/**
 * A NiftyTexture is a handle to a texture surface. A NiftyTexture can be rendered AND can be rendered to.
 * @author void
 */
public interface NiftyTexture {

  /**
   * Get the width of this texture.
   * @return the texture width
   */
  int getWidth();

  /**
   * Get the height of this texture.
   * @return the texture height
   */
  int getHeight();

  /**
   * Get the id of the atlas this texture is a part of. In case there is no support for Texture atlas in an SPI
   * implementation then this can be the texture id or anything else that identifies this texture.
   *
   * Please note: -1 is reserved and should not be returned by this method
   *
   * @return the texture id of the atlas this texture is a part of
   */
  int getAtlasId();

  /**
   * Get texture coordinate of top left corner of this texture in the texture atlas or 0 for non texture atlas texture.
   * @return texture coordinate U of this textures position in the texture atlas
   */
  double getU0();

  /**
   * Get texture coordinate of top left corner of this texture in the texture atlas or 0 for non texture atlas texture.
   * @return texture coordinate V of this textures position in the texture atlas
   */
  double getV0();

  /**
   * Get texture coordinate of bottom right corner of this texture in the texture atlas or 0 for non texture atlas
   * texture.
   * @return texture coordinate U of this textures position in the texture atlas
   */
  double getV1();

  /**
   * Get texture coordinate of bottom right corner of this texture in the texture atlas or 0 for non texture atlas
   * texture.
   * @return texture coordinate V of this textures position in the texture atlas
   */
  double getU1();

  /**
   * Save the content of the texture to a file with the given filename. This is very useful for debugging render to
   * texture issues.
   *
   * @param filename the filename to store the texture content to
   */
  void saveAsPng(String filename);

  /**
   * Free the texture memory. Call this if you don't plan to use this texture anymore to free any associated resources.
   */
  void dispose();

  /**
   * Update the content of this texture with the buffer data given as a parameter.
   * @param buffer ByteBuffer with new texture data. this should be exactly width * height * color format bytes size.
   */
  void update(ByteBuffer buffer);
}
