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
package de.lessvoid.niftyinternal.render.batch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.math.Vec4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * A triangle fan batch.
 */
public class TriangleFanBatch implements Batch<Void> {
  // the actual vertex data this batch stores
  private final List<Float> vertices = new ArrayList<>();

  // Vec4 buffer data so that we don't allocate new Vec4 instances all the time (these are being reused)
  private final Vec4 vsrc = new Vec4();
  private final Vec4 vdst = new Vec4();

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    FloatBuffer buffer = createBuffer(vertices.size());
    for (int i=0; i<vertices.size(); i++) {
      buffer.put(vertices.get(i));
    }
    renderDevice.maskRenderFill(buffer);
  }

  @Override
  public boolean requiresNewBatch(final Void params) {
    return false;
  }

  public boolean add(final float x, final float y, final Mat4 mat) {
    addTransformed(x, y, mat);
    return true;
  }

  private void addTransformed(final float x, final float y, final Mat4 mat) {
    vsrc.x = x;
    vsrc.y = y;
    vsrc.z = 0.0f;
    Mat4.transform(mat, vsrc, vdst);
    vertices.add(vdst.x);
    vertices.add(vdst.y);
  }

  private FloatBuffer createBuffer(final int size) {
    return ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  }
}
