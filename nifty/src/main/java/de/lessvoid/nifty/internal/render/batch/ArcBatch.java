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

import de.lessvoid.nifty.api.NiftyArcParameters;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.Vec4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * An arc batch is a number of arc rendered with the same line style. Changing line styles will create a new batch.
 */
public class ArcBatch implements Batch<NiftyArcParameters> {
  private final static int NUM_PRIMITIVES = 100;
  public final static int PRIMITIVE_SIZE = 4;

  private final FloatBuffer b;
  private final NiftyArcParameters arcParameters;

  // Vec4 buffer data
  private final Vec4 vsrc = new Vec4();
  private final Vec4 vdst = new Vec4();
  private boolean isStartPathBatch;
  private boolean isEndPathBatch;

  public ArcBatch(final NiftyArcParameters arcParameters) {
    this.b = createBuffer(NUM_PRIMITIVES * PRIMITIVE_SIZE);
    this.arcParameters = new NiftyArcParameters(arcParameters);
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    if (isStartPathBatch) {
      renderDevice.pathBegin(arcParameters.getLineParameters());
    }

    renderDevice.pathArcs(b, arcParameters);

    if (isEndPathBatch) {
      renderDevice.pathEnd(arcParameters.getLineParameters());
    }
  }

  @Override
  public boolean requiresNewBatch(final NiftyArcParameters params) {
    return !arcParameters.equals(params) || (b.remaining() < PRIMITIVE_SIZE);
  }

  public boolean add(final double centerX, final double centerY, final double radius, final Mat4 mat) {
    float lw = arcParameters.getLineParameters().getLineWidth() / 2.f;
    float uv = (float) ((((arcParameters.getLineParameters().getLineWidth()) / 2.f) + 2) / radius) + 1.f;
    addTransformed((float) (centerX - radius - lw), (float) (centerY + radius + lw), -uv, -uv, mat);
    addTransformed((float) (centerX - radius - lw), (float) (centerY - radius - lw), -uv,  uv, mat);
    addTransformed((float) (centerX + radius + lw), (float) (centerY + radius + lw),  uv, -uv, mat);
    addTransformed((float) (centerX + radius + lw), (float) (centerY - radius - lw),  uv,  uv, mat);
    return true;
  }

  public void enableStartPathBatch() {
    isStartPathBatch = true;
  }

  public void enableEndPathBatch() {
    isEndPathBatch = true;
  }

  private void addTransformed(final float x, final float y, final float u, final float v, final Mat4 mat) {
    vsrc.x = (float) x;
    vsrc.y = (float) y;
    vsrc.z = 0.0f;
    Mat4.transform(mat, vsrc, vdst);
    b.put(vdst.x);
    b.put(vdst.y);
    b.put(u);
    b.put(v);
  }

  private FloatBuffer createBuffer(final int size) {
    return ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
  }
}
