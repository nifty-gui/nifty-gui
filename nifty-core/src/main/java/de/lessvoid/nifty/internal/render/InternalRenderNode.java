package de.lessvoid.nifty.internal.render;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.internal.canvas.Command;
import de.lessvoid.nifty.internal.canvas.Context;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderTarget;

/**
 * Represents a Node that can be rendered. A InternalNiftyNode will be converted into an instance of this class.
 * @author void
 */
public class InternalRenderNode {
  // track back to the original InternalNiftyNode
  private final int originalNodeId;

  // The local model transformation for this node.
  private final Mat4 local;

  // The child RenderNodes of this node.
  private final List<InternalRenderNode> children = new ArrayList<InternalRenderNode>();

  // render target for this node
  private NiftyRenderTarget renderTarget;

  private int width;
  private int height;
  private final Context context;
  private final List<Command> commands;

  public InternalRenderNode(
      final int originalNodeId,
      final int width,
      final int height,
      final List<Command> commands,
      final Mat4 local,
      final NiftyRenderTarget renderTarget) {
    this.originalNodeId = originalNodeId;
    this.width = width;
    this.height = height;
    this.context = new Context();
    this.commands = commands;
    this.local = local;
    this.renderTarget = renderTarget;
  }

  public void updateContent(final NiftyRenderDevice renderDevice, final Mat4 mat) {
    renderTarget.setMatrix(mat);
    if (!commands.isEmpty()) {
      for (int i=0; i<commands.size(); i++) {
        Command command = commands.get(i);
        command.execute(renderTarget, context);
      }
      commands.clear();
    }

    for (int i=0; i<children.size(); i++) {
      children.get(i).updateContent(renderDevice, children.get(i).local);
    }
  }

  public void render(final NiftyRenderDevice renderDevice) {
    renderDevice.render(renderTarget, 0, 0, width, height, local);
/*
    for (int i=0; i<children.size(); i++) {
      children.get(i).render(renderDevice);
    }
*/
  }

  public void addChildNode(final InternalRenderNode childNode) {
    children.add(childNode);
  }

  public int getOriginalNodeId() {
    return originalNodeId;
  }

  public Mat4 getLocal() {
    return new Mat4(local);
  }

  public void setLocal(final Mat4 src) {
    this.local.load(src);
  }

  public NiftyRenderTarget getRendertarget() {
    return renderTarget;
  }

  public void setWidth(final int width) {
    this.width = width;
  }

  public int getWidth() {
    return width;
  }

  public void setHeight(final int height) {
    this.height = height;
  }

  public int getHeight() {
    return height;
  }

  public void setCommands(final List<Command> commands) {
    this.commands.clear();
    this.commands.addAll(commands);
  }

  public List<InternalRenderNode> getChildren() {
    return children;
  }
}
