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
package de.lessvoid.nifty.internal.render.batch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import de.lessvoid.nifty.api.NiftyLineParameters;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.Vec4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * A line batch is a number of lines rendered with the same cap and join styles. Changing cap or join style will
 * create a new batch.
 */
public class LineBatch implements Batch<NiftyLineParameters> {
  private final static int NUM_PRIMITIVES = 100;
  public final static int PRIMITIVE_SIZE = 2;

  private final FloatBuffer b;
  private final NiftyLineParameters lineParameters;

  // Vec4 buffer data
  private final Vec4 vsrc = new Vec4();
  private final Vec4 vdst = new Vec4();
  private boolean isStartPathBatch;
  private boolean isEndPathBatch;

  public LineBatch(final NiftyLineParameters lineParameters) {
    this.b = createBuffer(NUM_PRIMITIVES * PRIMITIVE_SIZE);
    this.lineParameters = new NiftyLineParameters(lineParameters);
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    if (isStartPathBatch) {
      renderDevice.pathBegin(lineParameters);
    }

    renderDevice.pathLines(b, lineParameters);

    if (isEndPathBatch) {
      renderDevice.pathEnd(lineParameters);
    }
  }

  @Override
  public boolean requiresNewBatch(final NiftyLineParameters params) {
    return !lineParameters.equals(params) || (b.remaining() < PRIMITIVE_SIZE);
  }

  public boolean add(final float x, final float y, final Mat4 mat) {
    addTransformed(x, y, mat);
    return true;
  }

  private void addTransformed(final float x, final float y, final Mat4 mat) {
    vsrc.x = (float) x;
    vsrc.y = (float) y;
    vsrc.z = 0.0f;
    Mat4.transform(mat, vsrc, vdst);
    b.put(vdst.x);
    b.put(vdst.y);
  }

  private FloatBuffer createBuffer(final int size) {
    return ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  }

  public void enableStartPathBatch() {
    isStartPathBatch = true;
  }

  public void enableEndPathBatch() {
    isEndPathBatch = true;
  }
}
