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
public class InternalRenderSync {
  private final InternalNiftyStatistics statistics;
  private final NiftyNodeAccessor niftyNodeAccessor;
  private final NiftyRenderDevice renderDevice;

  public InternalRenderSync(final InternalNiftyStatistics statistics, final NiftyRenderDevice renderDevice) {
    this.statistics = statistics;
    this.niftyNodeAccessor = NiftyNodeAccessor.getDefault();
    this.renderDevice = renderDevice;
  }

  public boolean synchronize(final List<NiftyNode> srcNodes, final List<InternalRenderNode> dstNodes) {
    statistics.start(Type.Synchronize);
    boolean changed = false;

    for (int i=0; i<srcNodes.size(); i++) {
      InternalNiftyNode src = niftyNodeAccessor.getInternalNiftyNode(srcNodes.get(i));
      InternalRenderNode dst = findNode(dstNodes, src.getId());
      if (dst == null) {
        dstNodes.add(createRenderNode(src, null, new Mat4(), null));
        changed = true;
      } else {
        boolean syncChanged = syncRenderNode(src, new Mat4(), null, dst);
        changed = changed || syncChanged;
      }
    }

    statistics.stop(Type.Synchronize);
    return changed;
  }

  private InternalRenderNode findNode(final List<InternalRenderNode> nodes, final int id) {
    for (int i=0; i<nodes.size(); i++) {
      InternalRenderNode node = nodes.get(i);
      if (node.getOriginalNodeId() == id) {
        return node;
      }
    }
    return null;
  }

  private InternalRenderNode createRenderNode(
      final InternalNiftyNode node,
      final InternalNiftyNode parent,
      final Mat4 parentTransform,
      final NiftyRenderTarget parentRenderTarget) {
    InternalRenderNode renderNode = new InternalRenderNode(
        node.getId(),
        node.getWidth(),
        node.getHeight(),
        node.getCanvas().getCommands(),
        buildLocalTransformation(node),
        createRenderTarget(node, parent, parentRenderTarget));

    for (int i=0; i<node.getChildren().size(); i++) {
      InternalNiftyNode n = node.getChildren().get(i);
      renderNode.addChildNode(createRenderNode(n, n.getParent(), renderNode.getLocal(), renderNode.getRendertarget()));
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

  private boolean syncRenderNode(
      final InternalNiftyNode src,
      final Mat4 parentTransform,
      final NiftyRenderTarget parentRenderTarget,
      final InternalRenderNode dst) {
    // FIXME only build a new matrix when transformation related properties of the nodes have changed!
    Mat4 local = buildLocalTransformation(src);
    boolean thisNodeChanged = (!dst.getLocal().compare(local) ||
                                dst.getWidth() != src.getWidth() ||
                                dst.getHeight() != src.getHeight() ||
                                src.getCanvas().isChanged());
    dst.setLocal(local);
    dst.setWidth(src.getWidth());
    dst.setHeight(src.getHeight());
    if (thisNodeChanged) {
      dst.setCommands(src.getCanvas().getCommands());
    }

    boolean childChanged = false;
    for (int i=0; i<src.getChildren().size(); i++) {
      boolean currentChildChanged = sync(src.getChildren().get(i), dst);
      childChanged = childChanged || currentChildChanged;
    }

    return thisNodeChanged || childChanged;
  }

  private boolean sync(final InternalNiftyNode src, final InternalRenderNode dstParent) {
    InternalRenderNode dst = findNode(dstParent.getChildren(), src.getId());
    if (dst == null) {
      dstParent.addChildNode(createRenderNode(src, src.getParent(), Mat4.createTranslate(src.getX(), src.getY(), 0.f), dstParent.getRendertarget()));
      return true;
    } else {
      return syncRenderNode(src, dstParent.getLocal(), dstParent.getRendertarget(), dst);
    }
  }
}
