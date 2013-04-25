package de.lessvoid.nifty.internal;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.internal.canvas.InternalNiftyCanvas;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * Represents a Node that can be rendered. A InternalNiftyNode will be converted into an instance of this class.
 * @author void
 */
public class InternalRenderNode {
  // The actual content to render for this node.
  private final InternalNiftyCanvas canvas;

  // The combined model transformation for this node.
  private Mat4 mat = new Mat4();

  // The child RenderNodes of this node.
  private final List<InternalRenderNode> children = new ArrayList<InternalRenderNode>();

  public InternalRenderNode(final InternalNiftyCanvas canvas) {
    this.canvas = canvas;
  }

  public void updateContent(final NiftyRenderDevice renderDevice) {
    canvas.updateContent(renderDevice);
    InternalChildIterate.iterate(children, childUpdateContent, renderDevice);
  }

  public void render(final NiftyRenderDevice renderDevice) {
    canvas.render(renderDevice, canvas.getWidth(), canvas.getHeight(), mat);
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
