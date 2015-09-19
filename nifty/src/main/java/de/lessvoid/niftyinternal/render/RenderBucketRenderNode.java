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
package de.lessvoid.niftyinternal.render;

import de.lessvoid.nifty.NiftyCanvas;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.node.NiftyNodeContentImpl;
import de.lessvoid.nifty.types.*;
import de.lessvoid.niftyinternal.accessor.NiftyCanvasAccessor;
import de.lessvoid.niftyinternal.canvas.Command;
import de.lessvoid.niftyinternal.canvas.Context;
import de.lessvoid.niftyinternal.canvas.InternalNiftyCanvas;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.math.Vec4;
import de.lessvoid.niftyinternal.render.batch.BatchManager;

import java.util.List;

/**
 * A RenderBucketRenderNode is the RenderNode representation of a NiftyContentNode. It stores the Context (texture) as
 * well as the NiftyCanvas for the NiftyContentNode.
 *
 * Created by void on 13.09.15.
 */
public class RenderBucketRenderNode {
  private final NiftyCanvas niftyCanvas = NiftyCanvasAccessor.getDefault().newNiftyCanvas();

  private int width;
  private int height;
  private Mat4 localToScreen;
  private Context context;

  public RenderBucketRenderNode(
      final int width,
      final int height,
      final Mat4 localToScreen,
      final NiftyRenderDevice renderDevice) {
    this.width = width;
    this.height = height;
    this.localToScreen = localToScreen;
    this.context = createContext(renderDevice);
  }

  public void updateContent(final int width, final int height, final Mat4 localToScreen, final NiftyRenderDevice renderDevice) {
    boolean sizeChanged = updateSize(width, height, localToScreen, renderDevice);

    InternalNiftyCanvas canvas = NiftyCanvasAccessor.getDefault().getInternalNiftyCanvas(niftyCanvas);
    if (sizeChanged || canvas.isChanged()) {
      context.bind(renderDevice, new BatchManager());
      context.prepare();

      List<Command> commands = canvas.getCommands();
      for (int i=0; i<commands.size(); i++) {
        Command command = commands.get(i);
        command.execute(context);
      }

      context.flush();
    }
  }

  public void render(final BatchManager batchManager, final Mat4 bucketTransformation) {
    Mat4 local = Mat4.mul(localToScreen, bucketTransformation);
    batchManager.addChangeCompositeOperation(NiftyCompositeOperation.SourceOver);
    batchManager.addTextureQuad(context.getNiftyTexture(), local, NiftyColor.white());
  }

  public NiftyRect getScreenRect() {
    Vec4 o = Mat4.transform(localToScreen, new Vec4(0.f, 0.f, 0.f, 1.f));
    Vec4 w = Mat4.transform(localToScreen, new Vec4(width, height, 0.f, 1.f));
    return NiftyRect.newNiftyRect(
        NiftyPoint.newNiftyPoint(o.x, o.y),
        NiftySize.newNiftySize(w.x, w.y));
  }

  private boolean updateSize(
      final int newWidth,
      final int newHeight,
      final Mat4 newLocalToScreen,
      final NiftyRenderDevice renderDevice) {
    if (newWidth == width && newHeight == height && newLocalToScreen.compare(localToScreen)) {
      return false;
    }

    width = newWidth;
    height = newHeight;
    localToScreen = newLocalToScreen;

    context.free();
    context = createContext(renderDevice);
    return true;
  }

  private Context createContext(final NiftyRenderDevice renderDevice) {
    return new Context(
        renderDevice.createTexture(width, height, NiftyRenderDevice.FilterMode.Linear),
        renderDevice.createTexture(width, height, NiftyRenderDevice.FilterMode.Linear));
  }

  public void updateCanvas(final NiftyNodeContentImpl child) {
    NiftyCanvasAccessor.getDefault().getInternalNiftyCanvas(niftyCanvas).reset();
    child.updateCanvas(niftyCanvas);
  }
}
