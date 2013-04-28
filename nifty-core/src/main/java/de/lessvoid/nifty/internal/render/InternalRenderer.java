package de.lessvoid.nifty.internal.render;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;
import de.lessvoid.nifty.internal.common.InternalChildIterate;
import de.lessvoid.nifty.internal.common.InternalNiftyStatistics;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

public class InternalRenderer {
  private final List<InternalRendererNode> rootRenderNodes = new ArrayList<InternalRendererNode>();
  private final NiftyNodeAccessor niftyNodeAccessor;
  private final NiftyRenderDevice renderDevice;
  private final InternalNiftyStatistics statistics;

  public InternalRenderer(final InternalNiftyStatistics statistics, final NiftyRenderDevice renderDevice) {
    this.statistics = statistics;
    this.niftyNodeAccessor = NiftyNodeAccessor.getDefault();
    this.renderDevice = renderDevice;
  }

  private boolean synchron = false;
  public void synchronize(final List<NiftyNode> rootNodes) {
    if (synchron) {
      return;
    }

    rootRenderNodes.clear();
    for (int i=0; i<rootNodes.size(); i++) {
      NiftyNode rootNode = rootNodes.get(i);
      rootRenderNodes.add(toRenderNode(
          niftyNodeAccessor.getInternalNiftyNode(rootNode),
          null,
          Mat4.createTranslate(rootNode.getX(), rootNode.getY(), 0.f)));
    }
    statistics.renderTreeSynchronisation();
    synchron = true;
  }

  public void render() {
    for (int i=0; i<rootRenderNodes.size(); i++) {
      rootRenderNodes.get(i).updateContent(renderDevice);
    }

    renderDevice.begin();
    for (int i=0; i<rootRenderNodes.size(); i++) {
      rootRenderNodes.get(i).render(renderDevice);
    }
    renderDevice.end();
  }

  private InternalRendererNode toRenderNode(final InternalNiftyNode node, final InternalNiftyNode parent, final Mat4 parentTransform) {
    Mat4 transformation = parentTransform;
    if (parent != null) {
      Mat4 move = Mat4.createTranslate(node.getX() - parent.getX(), node.getY() - parent.getY(), 0.f);
      System.out.println("* " + node.toString() + "\n" + move);
      Mat4.mul(parentTransform, move, transformation);
    }
    System.out.println(node.toString() + "\n" + transformation);
    InternalRendererNode renderNode = new InternalRendererNode(node.getWidth(), node.getHeight(), node.getCanvas().getCommands(), transformation);
    InternalChildIterate.iterate(node.getChildren(), convertToRenderNode, renderNode);
    return renderNode;
  }

  private InternalChildIterate.Function<InternalNiftyNode, InternalRendererNode> convertToRenderNode =
      new InternalChildIterate.Function<InternalNiftyNode, InternalRendererNode>() {
    @Override
    public void perform(final InternalNiftyNode node, final InternalRendererNode parentRenderNode) {
      Mat4 parentTransform = parentRenderNode.getTransformation();
      parentRenderNode.addChildNode(toRenderNode(node, node.getParent(), parentTransform));
    }
  };
}
