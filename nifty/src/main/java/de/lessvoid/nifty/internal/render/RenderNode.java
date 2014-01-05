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
  private int width;
  private int height;
  private boolean nodeChanged = true;
  private boolean contentChanged = true;
  private final Context context;

  public RenderNode(
      final int nodeId,
      final Mat4 local,
      final int w,
      final int h,
      final List<Command> commands,
      final NiftyTexture content,
      final NiftyRenderDevice renderDevice) {
    this.nodeId = nodeId;
    this.local = new Mat4(local);
    this.commands = commands;
    this.width = w;
    this.height = h;
    this.context = new Context(content);
  }

  public void render(final NiftyRenderDevice renderDevice, final Mat4 parent) {
    if (contentChanged) {
      context.prepare(renderDevice);
      for (int i=0; i<commands.size(); i++) {
        Command command = commands.get(i);
        command.execute(renderDevice, context);
      }
      context.flush(renderDevice);
      contentChanged = false;
    }
    Mat4 current = Mat4.mul(parent, local);
    renderDevice.render(context.getNiftyTexture(), current);

    for (int i=0; i<children.size(); i++) {
      children.get(i).render(renderDevice, current);
    }
    nodeChanged = false;
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

  public void nodeChanged() {
    nodeChanged = true;
  }

  public void contentChanged() {
    contentChanged = true;
  }

  public void outputStateInfo(final StringBuilder result, final String offset) {
    RenderNodeStateLogger.stateInfo(this, new AABB(width, height), commands, nodeChanged, contentChanged, result, offset);
    for (int i=0; i<children.size(); i++) {
      children.get(i).outputStateInfo(result, offset + "  ");
    }
  }
}
