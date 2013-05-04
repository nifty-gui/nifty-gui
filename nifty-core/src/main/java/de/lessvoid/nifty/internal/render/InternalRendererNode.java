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
public class InternalRendererNode {
  // track back to the original InternalNiftyNode
  private final int originalNodeId;

  // The combined model transformation for this node.
  private final Mat4 mat;

  // The child RenderNodes of this node.
  private final List<InternalRendererNode> children = new ArrayList<InternalRendererNode>();

  // render target for this node
  private NiftyRenderTarget renderTarget;

  private int width;
  private int height;
  private final Context context;
  private final List<Command> commands;

  private Mat4 move;

  public InternalRendererNode(
      final int originalNodeId,
      final int width,
      final int height,
      final List<Command> commands,
      final Mat4 mat,
      final Mat4 move,
      final NiftyRenderTarget renderTarget) {
    this.originalNodeId = originalNodeId;
    this.width = width;
    this.height = height;
    this.context = new Context();
    this.commands = commands;
    this.mat = mat;
    this.move = move;
    this.renderTarget = renderTarget;
  }

  public void updateContent(final NiftyRenderDevice renderDevice) {
    renderTarget.setMatrix(move);
    if (!commands.isEmpty()) {
      for (int i=0; i<commands.size(); i++) {
        Command command = commands.get(i);
        command.execute(renderTarget, context);
      }
      commands.clear();
    }

    for (int i=0; i<children.size(); i++) {
      children.get(i).updateContent(renderDevice);
    }
  }

  public void render(final NiftyRenderDevice renderDevice) {
    renderDevice.render(renderTarget, 0, 0, width, height, mat);
//    InternalChildIterate.iterate(children, childRender, renderDevice);
  }

  public void addChildNode(final InternalRendererNode childNode) {
    children.add(childNode);
  }

  public int getOriginalNodeId() {
    return originalNodeId;
  }

  public Mat4 getTransformation() {
    return new Mat4(mat);
  }

  public Mat4 getMove() {
    return new Mat4(move);
  }

  public void setTransformation(final Mat4 src) {
    this.mat.load(src);
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

  public List<InternalRendererNode> getChildren() {
    return children;
  }

  public void setMove(Mat4 move2) {
    move = new Mat4(move2);
  }
}
