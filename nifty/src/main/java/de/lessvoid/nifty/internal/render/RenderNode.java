package de.lessvoid.nifty.internal.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lessvoid.nifty.api.BlendMode;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.canvas.Command;
import de.lessvoid.nifty.internal.canvas.Context;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.render.batch.BatchManager;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

public class RenderNode {
  private final int nodeId;
  private final List<Command> commands;
  private final List<RenderNode> children = new ArrayList<RenderNode>();
  private final Mat4 local;
  private int width;
  private int height;
  private boolean needsRender = true;
  private boolean needsContentUpdate = true;
  private boolean contentResized = false;
  private Context context;
  private final BlendMode blendMode;
  private final int renderOrder;
  private int indexInParent;

  public RenderNode(
      final int nodeId,
      final Mat4 local,
      final int w,
      final int h,
      final List<Command> commands,
      final NiftyTexture content,
      final BlendMode blendMode,
      final int renderOrder) {
    this.nodeId = nodeId;
    this.local = new Mat4(local);
    this.commands = commands;
    this.width = w;
    this.height = h;
    this.context = new Context(content);
    this.blendMode = blendMode;
    this.renderOrder = renderOrder;
  }

  public void setIndexInParent(final int indexInParent) {
    this.indexInParent = indexInParent;
  }

  public void render(final BatchManager batchManager, final NiftyRenderDevice renderDevice, final Mat4 parent) {
    if (contentResized) {
      context = new Context(renderDevice.createTexture(width, height, true));
      contentResized = false;
      needsContentUpdate = true;
    }
    if (needsContentUpdate) {
      BatchManager contentBatchManager = new BatchManager();
      contentBatchManager.begin();
      contentBatchManager.changeBlendMode(BlendMode.BLEND_SEP);
      context.prepare(renderDevice);
      for (int i=0; i<commands.size(); i++) {
        Command command = commands.get(i);
        command.execute(contentBatchManager, context);
      }
      contentBatchManager.end(renderDevice);
      context.flush(renderDevice);
      needsContentUpdate = false;
    }

    Mat4 current = Mat4.mul(parent, local);
    batchManager.changeBlendMode(blendMode);
    batchManager.addTextureQuad(context.getNiftyTexture(), current, NiftyColor.WHITE());

    for (int i=0; i<children.size(); i++) {
      children.get(i).render(batchManager, renderDevice, current);
    }
    needsRender = false;
  }

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
    this.width = width;
  }

  public void setHeight(final int height) {
    if (height != this.height) {
      contentResized = true;
    }
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
    RenderNodeStateLogger.stateInfo(this, new AABB(width, height), commands, needsContentUpdate, needsRender, result, offset);
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
}
