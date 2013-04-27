package de.lessvoid.nifty.internal;

import java.util.ArrayList;
import java.util.Collections;
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
  // The combined model transformation for this node.
  private Mat4 mat = new Mat4();

  // The child RenderNodes of this node.
  private final List<InternalRenderNode> children = new ArrayList<InternalRenderNode>();

  // render target for this node
  private NiftyRenderTarget renderTarget;

  private final int width;
  private final int height;
  private final Context context;
  private final List<Command> commands;

  public InternalRenderNode(final int width, final int height, final List<Command> commands) {
    this.width = width;
    this.height = height;
    this.context = new Context();
    this.commands = commands;
  }

  public void updateContent(final NiftyRenderDevice renderDevice) {
    if (renderTarget == null) {
      renderTarget = renderDevice.createRenderTargets(width, height, 1, 1);
    }

    if (!commands.isEmpty()) {
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

  public void addChildNode(final InternalRenderNode childNode) {
    children.add(childNode);
  }

  private InternalChildIterate.Function<InternalRenderNode, NiftyRenderDevice> childRender =
      new InternalChildIterate.Function<InternalRenderNode, NiftyRenderDevice>() {
    @Override
    public void perform(final InternalRenderNode child, final NiftyRenderDevice parameter) {
      child.render(parameter);
    }
  };

  private InternalChildIterate.Function<InternalRenderNode, NiftyRenderDevice> childUpdateContent =
      new InternalChildIterate.Function<InternalRenderNode, NiftyRenderDevice>() {
    @Override
    public void perform(final InternalRenderNode child, final NiftyRenderDevice parameter) {
      child.updateContent(parameter);
    }
  };

/*
 *     float cx = parentX + width / 2;
    float cy = parentY + height / 2;
    InternalMatrix t1 = InternalMatrix.createTranslate(-cx, -cy, 0.f);
    InternalMatrix mat = InternalMatrix.createRotate((float) node.getRotationZ(), 0.f, 0.f, 1.f);
    InternalMatrix t2 = InternalMatrix.createTranslate(cx, cy, 0.f);

 */

}
