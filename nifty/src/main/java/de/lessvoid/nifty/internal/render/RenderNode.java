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
package de.lessvoid.nifty.internal.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyCompositeOperation;
import de.lessvoid.nifty.internal.canvas.Command;
import de.lessvoid.nifty.internal.canvas.Context;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.render.batch.BatchManager;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;
import de.lessvoid.nifty.spi.NiftyRenderDevice.FilterMode;

public class RenderNode {
  private final int nodeId;
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

  public RenderNode(
      final int nodeId,
      final Mat4 local,
      final int w,
      final int h,
      final List<Command> commands,
      final NiftyTexture contentTexture,
      final NiftyTexture workingTexture,
      final NiftyCompositeOperation compositeOperation,
      final int renderOrder) {
    this.nodeId = nodeId;
    this.local = new Mat4(local);
    this.commands = commands;
    this.width = this.oldWidth = w;
    this.height = this.oldHeight = h;
    this.context = new Context(contentTexture, workingTexture);
    this.compositeOperation = compositeOperation;
    this.renderOrder = renderOrder;
  }

  public void setIndexInParent(final int indexInParent) {
    this.indexInParent = indexInParent;
  }

  public void render(final BatchManager batchManager, final NiftyRenderDevice renderDevice, final Mat4 parent) {
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

    Mat4 current = Mat4.mul(parent, local);
    batchManager.addChangeCompositeOperation(compositeOperation);
    batchManager.addTextureQuad(context.getNiftyTexture(), current, NiftyColor.white());

    for (int i=0; i<children.size(); i++) {
      children.get(i).render(batchManager, renderDevice, current);
    }
    needsRender = false;
  }

  @Deprecated
  public void addChildNode(final RenderNode childNode) {
    children.add(childNode);
    childNode.setIndexInParent(children.size() - 1);
  }

  public int getNodeId() {
    return nodeId;
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

  public void setLocal(final Mat4 src) {
    this.local.load(src);
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

  public RenderNode findChildWithId(final int id) {
    for (int i=0; i<children.size(); i++) {
      RenderNode node = children.get(i);
      if (node.getNodeId() == id) {
        return node;
      }
    }
    return null;
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
}
