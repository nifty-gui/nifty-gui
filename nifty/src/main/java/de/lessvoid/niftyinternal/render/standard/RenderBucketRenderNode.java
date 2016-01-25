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
package de.lessvoid.niftyinternal.render.standard;

import de.lessvoid.nifty.NiftyCanvas;
import de.lessvoid.nifty.node.TransformationParameters;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeContentImpl;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.types.*;
import de.lessvoid.niftyinternal.accessor.NiftyCanvasAccessor;
import de.lessvoid.niftyinternal.canvas.Command;
import de.lessvoid.niftyinternal.canvas.Context;
import de.lessvoid.niftyinternal.canvas.InternalNiftyCanvas;
import de.lessvoid.niftyinternal.common.Statistics;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.math.Vec4;
import de.lessvoid.niftyinternal.render.batch.BatchManager;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A RenderBucketRenderNode is the RenderNode representation of a NiftyContentNode. It stores the Context (texture) as
 * well as the NiftyCanvas for the NiftyContentNode.
 * <p/>
 * Created by void on 13.09.15.
 */
public class RenderBucketRenderNode {
  private final NiftyCanvas niftyCanvas = NiftyCanvasAccessor.getDefault().newNiftyCanvas();
  private final BatchManager batchManager = new BatchManager();
  private final Mat4 local = Mat4.createIdentity();

  // we'll set this to true when the content of this render node has been rendered
  private boolean changed;

  private int width;
  private int height;
  private Mat4 localToScreen = Mat4.createIdentity();
  private Context context;
  @Nullable
  private NiftyRect screenSpace;
  private Statistics stats;

  public RenderBucketRenderNode(
      final int width,
      final int height,
      final NiftyRenderDevice renderDevice,
      final Statistics stats) {
    this.width = width;
    this.height = height;
    this.stats = stats;
    this.context = createContext(renderDevice);
    this.changed = false;
  }

  public void redrawCanvas(final NiftyNodeContentImpl node) {
    InternalNiftyCanvas canvas = NiftyCanvasAccessor.getDefault().getInternalNiftyCanvas(niftyCanvas);
    canvas.reset();
    node.updateCanvas(niftyCanvas);
  }

  public void updateCanvas(
      final NiftyNodeContentImpl child,
      final NiftyRenderDevice renderDevice,
      final NiftyNodeImpl<? extends NiftyNode> parent,
      final RenderBucketRenderNode parentRenderNode) {
    float layoutPosX;
    float layoutPosY;
    if (parent instanceof NiftyNodeContentImpl) {
      layoutPosX = child.getLayoutPosX() - ((NiftyNodeContentImpl) parent).getLayoutPosX();
      layoutPosY = child.getLayoutPosY() - ((NiftyNodeContentImpl) parent).getLayoutPosY();
    } else {
      layoutPosX = child.getLayoutPosX();
      layoutPosY = child.getLayoutPosY();
    }

    InternalNiftyCanvas canvas = NiftyCanvasAccessor.getDefault().getInternalNiftyCanvas(niftyCanvas);
    boolean canvasChanged = canvas.isChanged();
    boolean sizeChanged = updateSize(child.getContentWidth(), child.getContentHeight(), renderDevice);
    boolean transformationChanged = updateTransformation(child.getTransformations(), layoutPosX, layoutPosY, parentRenderNode);
    if (canvasChanged || sizeChanged) {
      context.bind(renderDevice, batchManager);
      context.prepare();

      List<Command> commands = canvas.getCommands();
      for (int i=0; i<commands.size(); i++) {
        Command command = commands.get(i);
        command.execute(context);
      }

      context.flush();
      changed = true;
    } else {
      changed = false;
    }
    changed = changed || transformationChanged;
  }

  public void render(final BatchManager batchManager, final Mat4 bucketTransformation) {
    Mat4.mul(bucketTransformation, localToScreen, local);
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

  public boolean isChanged() {
    return changed;
  }

  public Mat4 getLocalToScreen() {
    return localToScreen;
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

  private boolean updateTransformation(
      final List<TransformationParameters> transformations,
      final float layoutPosX,
      final float layoutPosY,
      final RenderBucketRenderNode parentRenderNode) {
    Mat4 parentLocalToScreen = new Mat4();
    if (parentRenderNode != null) {
      parentLocalToScreen = parentRenderNode.getLocalToScreen();
    }

    Mat4 a = new Mat4(parentLocalToScreen);
    Mat4 b = new Mat4();
    Mat4 c = new Mat4();
    Mat4 s;
    if (transformations.isEmpty()) {
      b = Mat4.createTranslate(layoutPosX, layoutPosY, 0.f);
      Mat4.mul(a, b, c);
      a = c;
    } else {
      for (int i = transformations.size() - 1; i >= 0; i--) {
        TransformationParameters param = transformations.get(i);
        param.buildTransformationMatrix(b, layoutPosX, layoutPosY, width, height);
        Mat4.mul(a, b, c);
        s = a;
        a = c;
        c = s;
      }
    }
    Mat4 newLocalToScreen = a;
    if (newLocalToScreen.compare(localToScreen)) {
      return false;
    }
    this.localToScreen = newLocalToScreen;
    this.screenSpace = null;
    return true;
  }

  private Context createContext(final NiftyRenderDevice renderDevice) {
    return new Context(
        renderDevice.createTexture(width, height, NiftyRenderDevice.FilterMode.Linear),
        renderDevice.createTexture(width, height, NiftyRenderDevice.FilterMode.Linear),
        stats);
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("width: ").append(width).append(", ");
    result.append("height: ").append(height).append("\n");
    result.append("localToScreen:\n").append(localToScreen);
    result.append("local:\n").append(local);
    return result.toString();
  }
}
