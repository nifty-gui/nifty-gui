package de.lessvoid.nifty.internal.render;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.internal.canvas.Command;
import de.lessvoid.nifty.internal.canvas.Context;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

public class RenderNode {
  private final int nodeId;
  private final List<Command> commands;
  private final List<RenderNode> children = new ArrayList<RenderNode>();
  private final Mat4 local;
  private final AABB aabb;
  private int width;
  private int height;
  private boolean changed = true;
  private boolean rerender = true;
  private final NiftyTexture renderTarget;

  public RenderNode(final int nodeId, final Mat4 local, final int w, final int h, final List<Command> commands, final NiftyTexture renderTarget) {
    this.nodeId = nodeId;
    this.local = new Mat4(local);
    this.commands = commands;
    this.width = w;
    this.height = h;
    this.aabb = new AABB(width, height);
    this.renderTarget = renderTarget;
  }

  public void prepareRender(final NiftyTexture renderTarget) {
    if (changed) {
//      renderTarget.setMatrix(new Mat4());
      aabb.markStencil(renderTarget);
    }

    for (int i=0; i<children.size(); i++) {
      children.get(i).prepareRender(renderTarget);
    }
  }

  public void updateContent(final NiftyRenderDevice renderDevice, final NiftyTexture target, final Context context, final Mat4 mat, final boolean forceRender) {
    Mat4 my = Mat4.mul(mat, local);

    if (rerender || forceRender) {
//      renderTarget.setMatrix(new Mat4());
      for (int i=0; i<commands.size(); i++) {
        Command command = commands.get(i);
        command.execute(renderTarget, context);
      }
//      target.renderTexture(renderTarget, 0, 0);
    }

    if (changed) {
      aabb.update(my, width, height);
      changed = false;
    }

    for (int i=0; i<children.size(); i++) {
      children.get(i).updateContent(renderDevice, target, context, my, rerender);
    }
    rerender = false;
  }

  public void addChildNode(final RenderNode childNode) {
    children.add(childNode);
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
    this.width = width;
  }

  public void setHeight(final int height) {
    this.height = height;
  }

  public void setCommands(final List<Command> commands) {
    this.commands.clear();
    this.commands.addAll(commands);
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

  public void markChanged() {
    changed = true;
  }

  public void markNeedsReRender() {
    rerender = true;
  }

  public void outputStateInfo(final StringBuilder result, final String offset) {
    RenderNodeStateLogger.stateInfo(this, aabb, commands, changed, rerender, result, offset);
    for (int i=0; i<children.size(); i++) {
      children.get(i).outputStateInfo(result, offset + "  ");
    }
  }
}
