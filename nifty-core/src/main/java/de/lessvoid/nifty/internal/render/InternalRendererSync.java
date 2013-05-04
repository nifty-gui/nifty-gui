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
public class InternalRendererSync {
  private final InternalNiftyStatistics statistics;
  private final NiftyNodeAccessor niftyNodeAccessor;
  private final NiftyRenderDevice renderDevice;

  public InternalRendererSync(final InternalNiftyStatistics statistics, final NiftyRenderDevice renderDevice) {
    this.statistics = statistics;
    this.niftyNodeAccessor = NiftyNodeAccessor.getDefault();
    this.renderDevice = renderDevice;
  }

  public boolean synchronize(final List<NiftyNode> srcNodes, final List<InternalRendererNode> dstNodes) {
    statistics.start(Type.Synchronize);
    boolean changed = false;

    for (int i=0; i<srcNodes.size(); i++) {
      InternalNiftyNode src = niftyNodeAccessor.getInternalNiftyNode(srcNodes.get(i));
      InternalRendererNode dst = findNode(dstNodes, src.getId());
      if (dst == null) {
        dstNodes.add(createRenderNode(src, null, Mat4.createTranslate(src.getX(), src.getY(), 0.f), null));
        changed = true;
      } else {
        boolean syncChanged = syncRenderNode(src, Mat4.createTranslate(src.getX(), src.getY(), 0.f), null, dst);
        changed = changed || syncChanged;
      }
    }

    statistics.stop(Type.Synchronize);
    return changed;
  }

  private InternalRendererNode findNode(final List<InternalRendererNode> nodes, final int id) {
    for (int i=0; i<nodes.size(); i++) {
      InternalRendererNode node = nodes.get(i);
      if (node.getOriginalNodeId() == id) {
        return node;
      }
    }
    return null;
  }

  private InternalRendererNode createRenderNode(
      final InternalNiftyNode node,
      final InternalNiftyNode parent,
      final Mat4 parentTransform,
      final NiftyRenderTarget parentRenderTarget) {
    Mat4 transformation = parentTransform;
    NiftyRenderTarget rt;
    Mat4 move;
    if (parent != null) {
      move = Mat4.createTranslate(node.getX() - parent.getX(), node.getY() - parent.getY(), 0.f);
      Mat4.mul(parentTransform, move, transformation);
      rt = parentRenderTarget;
    } else {
      rt = renderDevice.createRenderTargets(node.getWidth(), node.getHeight(), 1, 1);
      move = new Mat4();
    }
    Mat4.mul(transformation, Mat4.createRotate((float) node.getRotationZ(), 0.0f, 0.0f, 1.f), transformation);
    Mat4.mul(move, Mat4.createRotate((float) node.getRotationZ(), 0.0f, 0.0f, 1.f), move);
    System.out.println(move);
    InternalRendererNode renderNode = new InternalRendererNode(
        node.getId(),
        node.getWidth(),
        node.getHeight(),
        node.getCanvas().getCommands(),
        transformation,
        move,
        rt);

    for (int i=0; i<node.getChildren().size(); i++) {
      InternalNiftyNode n = node.getChildren().get(i);
      renderNode.addChildNode(createRenderNode(n, n.getParent(), renderNode.getTransformation(), renderNode.getRendertarget()));
    }

    return renderNode;
  }

  private boolean syncRenderNode(
      final InternalNiftyNode src,
      final Mat4 parentTransform,
      final NiftyRenderTarget parentRenderTarget,
      final InternalRendererNode dst) {
    Mat4 transformation = parentTransform;
    InternalNiftyNode parent = src.getParent();
    Mat4 move = new Mat4();
    if (parent != null) {
      move = Mat4.createTranslate(src.getX() - parent.getX(), src.getY() - parent.getY(), 0.f);
      Mat4.mul(parentTransform, move, transformation);
    }
    Mat4.mul(transformation, Mat4.createRotate((float) src.getRotationZ(), 0.0f, 0.0f, 1.f), transformation);
    Mat4.mul(move, Mat4.createRotate((float) src.getRotationZ(), 0.0f, 0.0f, 1.f), move);

    boolean thisNodeChanged = (!dst.getTransformation().compare(transformation) ||
                               !dst.getMove().compare(move) ||
                                dst.getWidth() != src.getWidth() ||
                                dst.getHeight() != src.getHeight() ||
                                src.getCanvas().isChanged());

    dst.setTransformation(transformation);
    dst.setWidth(src.getWidth());
    dst.setHeight(src.getHeight());
    dst.setCommands(src.getCanvas().getCommands());
    dst.setMove(move);

    boolean childChanged = false;
    for (int i=0; i<src.getChildren().size(); i++) {
      boolean currentChildChanged = sync(src.getChildren().get(i), dst);
      childChanged = childChanged || currentChildChanged;
    }

    return thisNodeChanged || childChanged;
  }

  private boolean sync(final InternalNiftyNode src, final InternalRendererNode dstParent) {
    InternalRendererNode dst = findNode(dstParent.getChildren(), src.getId());
    if (dst == null) {
      dstParent.addChildNode(createRenderNode(src, src.getParent(), Mat4.createTranslate(src.getX(), src.getY(), 0.f), dstParent.getRendertarget()));
      return true;
    } else {
      return syncRenderNode(src, dstParent.getTransformation(), dstParent.getRendertarget(), dst);
    }
  }
}
