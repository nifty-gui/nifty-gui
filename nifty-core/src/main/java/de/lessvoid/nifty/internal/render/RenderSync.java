package de.lessvoid.nifty.internal.render;

import java.util.List;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;
import de.lessvoid.nifty.internal.common.InternalNiftyStatistics;
import de.lessvoid.nifty.internal.common.InternalNiftyStatistics.Type;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderTarget;

/**
 * Synchronize a list of NiftyNodes to a list if InternalRenderNodes.
 * @author void
 */
public class RenderSync {
  private final InternalNiftyStatistics statistics;
  private final NiftyNodeAccessor niftyNodeAccessor;
  private final NiftyRenderDevice renderDevice;

  public RenderSync(final InternalNiftyStatistics statistics, final NiftyRenderDevice renderDevice) {
    this.statistics = statistics;
    this.niftyNodeAccessor = NiftyNodeAccessor.getDefault();
    this.renderDevice = renderDevice;
  }

  public boolean synchronize(final List<NiftyNode> srcNodes, final List<RenderNodeContent> dstNodes) {
    statistics.start(Type.Synchronize);
    boolean changed = false;

    for (int i=0; i<srcNodes.size(); i++) {
      InternalNiftyNode src = niftyNodeAccessor.getInternalNiftyNode(srcNodes.get(i));
      RenderNodeContent dst = findNode(dstNodes, src.getId());
      if (dst == null) {
        dstNodes.add(createRenderNodeBufferParent(src, null, null));
        changed = true;
      } else {
        boolean syncChanged = syncRenderNodeBufferChildNodes(src, dst.getChild());
        changed = changed || syncChanged;
      }
    }

    statistics.stop(Type.Synchronize);
    return changed;
  }

  private RenderNodeContent findNode(final List<RenderNodeContent> nodes, final int id) {
    for (int i=0; i<nodes.size(); i++) {
      RenderNodeContent node = nodes.get(i);
      if (node.getNodeId() == id) {
        return node;
      }
    }
    return null;
  }

  private RenderNodeContent createRenderNodeBufferParent(
      final InternalNiftyNode node,
      final InternalNiftyNode parent,
      final NiftyRenderTarget parentRenderTarget) {
    return new RenderNodeContent(
        createRenderNodeBufferChild(node),
        createRenderTarget(node, parent, parentRenderTarget));
  }

  private RenderNodeContentChild createRenderNodeBufferChild(final InternalNiftyNode node) {
    RenderNodeContentChild renderNode = new RenderNodeContentChild(
        node.getId(),
        buildLocalTransformation(node),
        node.getWidth(),
        node.getHeight(),
        node.getCanvas().getCommands());

    for (int i=0; i<node.getChildren().size(); i++) {
      InternalNiftyNode n = node.getChildren().get(i);
      renderNode.addChildNode(createRenderNodeBufferChild(n));
    }

    return renderNode;
  }

  private Mat4 buildLocalTransformation(final InternalNiftyNode node) {
    return Mat4.mul(
        Mat4.createTranslate(node.getX(), node.getY(), 0.f),
        Mat4.createRotate((float) node.getRotationZ(), 0.0f, 0.0f, 1.f));
  }

  private NiftyRenderTarget createRenderTarget(
      final InternalNiftyNode node,
      final InternalNiftyNode parent,
      final NiftyRenderTarget parentRenderTarget) {
    if (parent == null || node.isCache()) {
      return renderDevice.createRenderTargets(node.getWidth(), node.getHeight(), 1, 1);
    }
    return parentRenderTarget;
  }

  private boolean syncRenderNodeBufferChildNodes(final InternalNiftyNode src, final RenderNodeContentChild dst) {
    Mat4 local = buildLocalTransformation(src);
    boolean localChanged = !dst.getLocal().compare(local);
    boolean widthChanged = dst.getWidth() != src.getWidth();
    boolean heightChanged = dst.getHeight() != src.getHeight();
    boolean canvasChanged = src.getCanvas().isChanged();
    boolean thisNodeChanged = (localChanged || widthChanged || heightChanged || canvasChanged);

    if (thisNodeChanged) {
      dst.setLocal(local);
      dst.setWidth(src.getWidth());
      dst.setHeight(src.getHeight());
      dst.setCommands(src.getCanvas().getCommands());
      dst.markChanged();
    }

    boolean childChanged = false;
    for (int i=0; i<src.getChildren().size(); i++) {
      boolean currentChildChanged = syncRenderNodeBufferChild(src.getChildren().get(i), dst);
      childChanged = childChanged || currentChildChanged;
    }

    return thisNodeChanged || childChanged;
  }

  private boolean syncRenderNodeBufferChild(final InternalNiftyNode src, final RenderNodeContentChild dstParent) {
    RenderNodeContentChild dst = dstParent.findChildWithId(src.getId());
    if (dst == null) {
      dstParent.addChildNode(createRenderNodeBufferChild(src));
      return true;
    } else {
      return syncRenderNodeBufferChildNodes(src, dst);
    }
  }
}
