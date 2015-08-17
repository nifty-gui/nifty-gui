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

import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderDevice.FilterMode;
import de.lessvoid.nifty.spi.NiftyTexture;
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyCompositeOperation;
import de.lessvoid.niftyinternal.canvas.Command;
import de.lessvoid.niftyinternal.canvas.Context;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.render.batch.BatchManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderNode {
  private final List<Command> commands;
  private final List<RenderNode> children = new ArrayList<RenderNode>();
  private final Mat4 local;
  private int width;
  private int height;
  private int oldWidth;
  private int oldHeight;
  private boolean needsRender = true;
  private boolean needsContentUpdate = true;
  private boolean contentResized = false;
  private Context context;
  private final NiftyCompositeOperation compositeOperation;
  private final int renderOrder;
  private int indexInParent;
  private Integer nodeId;

  public RenderNode(
      final Mat4 local,
      final int w,
      final int h,
      final List<Command> commands,
      final NiftyTexture contentTexture,
      final NiftyTexture workingTexture,
      final NiftyCompositeOperation compositeOperation,
      final int renderOrder) {
    this.local = new Mat4(local);
    this.commands = commands;
    this.width = this.oldWidth = w;
    this.height = this.oldHeight = h;
    this.context = new Context(contentTexture, workingTexture);
    this.compositeOperation = compositeOperation;
    this.renderOrder = renderOrder;
    this.nodeId = this.hashCode();
  }

  public void setIndexInParent(final int indexInParent) {
    this.indexInParent = indexInParent;
  }

  public void render(final BatchManager batchManager, final NiftyRenderDevice renderDevice) {
    if (contentResized) {
      // only actually allocate new data when the new size is greater than the old size
      // if they are the same size or lower then we can keep the current data
      if (width > oldWidth || height > oldHeight) {
        context.free();
        context = new Context(
            renderDevice.createTexture(width, height, FilterMode.Linear),
            renderDevice.createTexture(width, height, FilterMode.Linear));
      }
      contentResized = false;
      needsContentUpdate = true;
    }
    if (needsContentUpdate) {
      context.bind(renderDevice, new BatchManager());
      context.prepare();

      for (int i=0; i<commands.size(); i++) {
        Command command = commands.get(i);
        command.execute(context);
      }

      context.flush();
      needsContentUpdate = false;
    }

    batchManager.addChangeCompositeOperation(compositeOperation);
    batchManager.addTextureQuad(context.getNiftyTexture(), local, NiftyColor.white());

    for (int i=0; i<children.size(); i++) {
      children.get(i).render(batchManager, renderDevice);
    }
    needsRender = false;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Mat4 getLocal() {
    return new Mat4(local);
  }

  public void setWidth(final int width) {
    if (width != this.width) {
      contentResized = true;
    }
    this.oldWidth = this.width;
    this.width = width;
  }

  public void setHeight(final int height) {
    if (height != this.height) {
      contentResized = true;
    }
    this.oldHeight = this.height;
    this.height = height;
  }

  public void needsRender() {
    needsRender = true;
  }

  public void needsContentUpdate(final List<Command> list) {
    commands.clear();
    commands.addAll(list);

    needsContentUpdate = true;
    needsRender = true;
  }

  public void outputStateInfo(final StringBuilder result, final String offset) {
    RenderNodeStateLogger.stateInfo(
        this,
        new AABB(width, height),
        commands,
        needsContentUpdate,
        needsRender,
        result,
        offset);
    for (int i=0; i<children.size(); i++) {
      children.get(i).outputStateInfo(result, offset + "  ");
    }
  }

  public int getRenderOrder() {
    return renderOrder;
  }

  public int getIndexInParent() {
    return indexInParent;
  }

  public void sortChildren() {
    Collections.sort(children, new RenderNodeComparator());
    for (int i=0; i<children.size(); i++) {
      children.get(i).sortChildren();
    }
  }

  public List<RenderNode> getChildren() {
    return children;
  }

  public Integer getNodeId() {
    return nodeId;
  }
}
