package de.lessvoid.nifty.internal.render;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.internal.canvas.Command;
import de.lessvoid.nifty.internal.canvas.Context;
import de.lessvoid.nifty.internal.common.InternalChildIterate;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderTarget;

/**
 * Represents a Node that can be rendered. A InternalNiftyNode will be converted into an instance of this class.
 * @author void
 */
public class InternalRendererNode {
  // The combined model transformation for this node.
  private final Mat4 mat;

  // The child RenderNodes of this node.
  private final List<InternalRendererNode> children = new ArrayList<InternalRendererNode>();

  // render target for this node
  private NiftyRenderTarget renderTarget;

  private final int width;
  private final int height;
  private final Context context;
  private final List<Command> commands;

  public InternalRendererNode(final int width, final int height, final List<Command> commands, final Mat4 mat) {
    this.width = width;
    this.height = height;
    this.context = new Context();
    this.commands = commands;
    this.mat = mat;
  }

  public void updateContent(final NiftyRenderDevice renderDevice) {
    if (!commands.isEmpty()) {
      if (renderTarget == null) {
        renderTarget = renderDevice.createRenderTargets(width, height, 1, 1);
      }
      for (int i=0; i<commands.size(); i++) {
        Command command = commands.get(i);
        command.execute(renderTarget, context);
      }
      commands.clear();
    }

    InternalChildIterate.iterate(children, childUpdateContent, renderDevice);
  }

  public void render(final NiftyRenderDevice renderDevice) {
    if (renderTarget == null) {
      return;
    }
    renderDevice.render(renderTarget, 0, 0, width, height, mat);
    InternalChildIterate.iterate(children, childRender, renderDevice);
  }

  public void addChildNode(final InternalRendererNode childNode) {
    children.add(childNode);
  }

  private InternalChildIterate.Function<InternalRendererNode, NiftyRenderDevice> childRender =
      new InternalChildIterate.Function<InternalRendererNode, NiftyRenderDevice>() {
    @Override
    public void perform(final InternalRendererNode child, final NiftyRenderDevice parameter) {
      child.render(parameter);
    }
  };

  private InternalChildIterate.Function<InternalRendererNode, NiftyRenderDevice> childUpdateContent =
      new InternalChildIterate.Function<InternalRendererNode, NiftyRenderDevice>() {
    @Override
    public void perform(final InternalRendererNode child, final NiftyRenderDevice parameter) {
      child.updateContent(parameter);
    }
  };

  public Mat4 getTransformation() {
    return new Mat4(mat);
  }
}
