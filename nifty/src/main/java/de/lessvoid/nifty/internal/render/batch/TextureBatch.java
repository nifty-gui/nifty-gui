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
package de.lessvoid.nifty.internal.render.batch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.Vec4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

/**
 * A TextureBatch stores textured Quads with vertex colors.
 */
public class TextureBatch implements Batch<NiftyTexture> {
  private final static int NUM_PRIMITIVES = 1000;
  public final static int PRIMITIVE_SIZE = 8 * 6;

  private final FloatBuffer b;
  private final NiftyTexture texture;
  private final int atlasId;

  // Vec4 buffer data
  private final Vec4 vsrc = new Vec4();
  private final Vec4 vdst = new Vec4();

  public TextureBatch(final NiftyTexture texture) {
    this.atlasId = texture.getAtlasId();
    this.texture = texture;
    this.b = createBuffer(NUM_PRIMITIVES * PRIMITIVE_SIZE);
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    renderDevice.render(texture, b);
  }

  @Override
  public boolean requiresNewBatch(final NiftyTexture niftyTexture) {
    return niftyTexture.getAtlasId() != atlasId || (b.remaining() < PRIMITIVE_SIZE);
  }

  public void reset() {
    this.b.clear();
  }

  public boolean add(
      final double x,
      final double y,
      final int width,
      final int height,
      final double u0,
      final double v0,
      final double u1,
      final double v1,
      final Mat4 mat,
      final NiftyColor color) {
    addVertex(x,         y,          mat, u0, v0, color);
    addVertex(x,         y + height, mat, u0, v1, color);
    addVertex(x + width, y,          mat, u1, v0, color);
    addVertex(x + width, y,          mat, u1, v0, color);
    addVertex(x,         y + height, mat, u0, v1, color);
    addVertex(x + width, y + height, mat, u1, v1, color);
    return true;
  }

  private void addVertex(
      final double x,
      final double y,
      final Mat4 mat,
      final double u,
      final double v,
      final NiftyColor color) {
    vsrc.x = (float) x;
    vsrc.y = (float) y;
    vsrc.z = 0.0f;
    Mat4.transform(mat, vsrc, vdst);
    b.put(vdst.x);
    b.put(vdst.y);
    b.put((float) u);
    b.put((float) v);
    b.put((float) color.getRed());
    b.put((float) color.getGreen());
    b.put((float) color.getBlue());
    b.put((float) color.getAlpha());
  }

  private FloatBuffer createBuffer(final int size) {
    return ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  }
}
