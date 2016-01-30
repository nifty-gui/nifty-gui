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
import de.lessvoid.niftyinternal.math.Vec2;
import de.lessvoid.niftyinternal.math.Vec4;
import de.lessvoid.niftyinternal.render.batch.BatchManager;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A RenderBucketRenderNode is the RenderNode representation of a NiftyContentNode. It stores the Context (texture) as
 * well as the NiftyCanvas for the NiftyContentNode.
 *
 * Created by void on 13.09.15.
 */
public class RenderBucketRenderNode implements Comparable<RenderBucketRenderNode> {
  private final NiftyCanvas niftyCanvas = NiftyCanvasAccessor.getDefault().newNiftyCanvas();

  private int width;
  private int height;
  private Mat4 local;
  private Mat4 localToScreen;

  private Context context;
  private int renderOrder;
  @Nullable
  private NiftyRect screenSpace;
  private boolean canvasChanged;
  private boolean sizeChanged;
  private boolean transformationChanged;
  private boolean disableRender;
  private Vec2 layoutPos;

  public RenderBucketRenderNode(
      final int width,
      final int height,
      final Mat4 local,
      final Mat4 localToScreen,
      final Vec2 layoutPos,
      final NiftyRenderDevice renderDevice) {
    this.width = width;
    this.height = height;
    this.local = local;
    this.localToScreen = localToScreen;
    this.layoutPos = layoutPos;
    this.context = createContext(renderDevice);
  }

  public void prepare(
      final NiftyNodeContentImpl child,
      final int renderOrderValue,
      final NiftyRenderDevice renderDevice) {
    InternalNiftyCanvas canvas = NiftyCanvasAccessor.getDefault().getInternalNiftyCanvas(niftyCanvas);
    canvas.reset();
    child.updateCanvas(niftyCanvas);

    renderOrder = renderOrderValue;
    canvasChanged = canvas.isChanged();
    sizeChanged = updateSize(child.getContentWidth(), child.getContentHeight(), renderDevice);
    transformationChanged = updateTransformation(child.getLocalToScreen(), child.getLocal());
    layoutPos = child.getLayoutPos();
  }

  public void update(final NiftyRenderDevice renderDevice, final List<RenderBucketRenderNode> childRenderNodes) {
    InternalNiftyCanvas canvas = NiftyCanvasAccessor.getDefault().getInternalNiftyCanvas(niftyCanvas);
    if (canvasChanged || sizeChanged || transformationChanged || !childRenderNodes.isEmpty()) {
      BatchManager batchManager = new BatchManager();
      context.bind(renderDevice, batchManager);
      context.prepare();

      List<Command> commands = canvas.getCommands();
      for (int i=0; i<commands.size(); i++) {
        Command command = commands.get(i);
        command.execute(context);
      }

      for (RenderBucketRenderNode childRenderNode : childRenderNodes) {
        // FIXME talk with mkaring if layout can provide the layoutPos relative to the parent node too
        Vec2 layoutPosRel = Vec2.sub(childRenderNode.layoutPos, layoutPos, null);
        Mat4 childLayoutTranslate = Mat4.createTranslate(layoutPosRel.getX(), layoutPosRel.getY(), 0.f);
        Mat4 childLocalWithLayout = Mat4.mul(childLayoutTranslate, childRenderNode.local);
        batchManager.addTextureQuad(childRenderNode.context.getNiftyTexture(), childLocalWithLayout, NiftyColor.white());
        childRenderNode.disableRender();
      }

      context.flush();
    }
  }

  private void disableRender() {
    disableRender = true;
  }

  public void render(final BatchManager batchManager, final Mat4 bucketTransformation) {
    if (disableRender) {
      return;
    }
    Mat4 local = Mat4.mul(bucketTransformation, localToScreen);
    batchManager.addChangeCompositeOperation(NiftyCompositeOperation.SourceOver);
    batchManager.addTextureQuad(context.getNiftyTexture(), local, NiftyColor.white());
  }

  public NiftyRect getScreenSpaceAABB() {
    NiftyRect screenSpace = this.screenSpace;
    if (screenSpace == null) {
      Vec4 p0 = Mat4.transform(localToScreen, new Vec4(0.f, 0.f, 0.f, 1.f));
      Vec4 p1 = Mat4.transform(localToScreen, new Vec4(width, 0.f, 0.f, 1.f));
      Vec4 p2 = Mat4.transform(localToScreen, new Vec4(width, height, 0.f, 1.f));
      Vec4 p3 = Mat4.transform(localToScreen, new Vec4(0.f, height, 0.f, 1.f));
      float minX = Math.min(Math.min(p0.getX(), p1.getX()), Math.min(p2.getX(), p3.getX()));
      float maxX = Math.max(Math.max(p0.getX(), p1.getX()), Math.max(p2.getX(), p3.getX()));
      float minY = Math.min(Math.min(p0.getY(), p1.getY()), Math.min(p2.getY(), p3.getY()));
      float maxY = Math.max(Math.max(p0.getY(), p1.getY()), Math.max(p2.getY(), p3.getY()));
      screenSpace = NiftyRect.newNiftyRect(
          NiftyPoint.newNiftyPoint(minX, minY),
          NiftySize.newNiftySize(maxX - minX, maxY - minY));
      this.screenSpace = screenSpace;
    }
    return screenSpace;
  }

  @Override
  public int compareTo(final RenderBucketRenderNode o) {
    return Integer.valueOf(this.renderOrder).compareTo(o.renderOrder);
  }

  private boolean updateSize(
      final int newWidth,
      final int newHeight,
      final NiftyRenderDevice renderDevice) {
    if (newWidth == width && newHeight == height) {
      return false;
    }

    width = newWidth;
    height = newHeight;
    this.screenSpace = null;

    context.free();
    context = createContext(renderDevice);
    return true;
  }

  private boolean updateTransformation(final Mat4 newLocalToScreen, final Mat4 newLocal) {
    if (newLocalToScreen.compare(localToScreen)) {
      return false;
    }
    localToScreen = newLocalToScreen;
    this.screenSpace = null;
    local = newLocal;
    return true;
  }

  private Context createContext(final NiftyRenderDevice renderDevice) {
    return new Context(
        renderDevice.createTexture(width, height, NiftyRenderDevice.FilterMode.Linear),
        renderDevice.createTexture(width, height, NiftyRenderDevice.FilterMode.Linear));
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("width: ").append(width).append(", ");
    result.append("height: ").append(height).append("\n");
    result.append("localToScreen:\n").append(localToScreen);
    result.append("local:\n").append(local);
    result.append("layoutPos:\n").append(layoutPos);
    result.append("renderOrder: ").append(renderOrder);
    return result.toString();
  }
}
