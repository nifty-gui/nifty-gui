package de.lessvoid.nifty.internal.render;

import java.util.List;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderTarget;

/**
 * Synchronize a list of NiftyNodes to a list if InternalRenderNodes.
 * @author void
 */
public class RendererNodeSync {
  private final NiftyNodeAccessor niftyNodeAccessor;
  private final NiftyRenderDevice renderDevice;

  public RendererNodeSync(final NiftyRenderDevice renderDevice) {
    this.niftyNodeAccessor = NiftyNodeAccessor.getDefault();
    this.renderDevice = renderDevice;
  }

  public boolean synchronize(final List<NiftyNode> srcNodes, final List<RootRenderNode> dstNodes) {
    boolean changed = false;

    for (int i=0; i<srcNodes.size(); i++) {
      InternalNiftyNode src = niftyNodeAccessor.getInternalNiftyNode(srcNodes.get(i));
      RootRenderNode dst = findNode(dstNodes, src.getId());
      if (dst == null) {
        dstNodes.add(createRenderNodeBufferParent(src, null, null));
        changed = true;
      } else {
        boolean syncChanged = syncRenderNodeBufferChildNodes(src, dst.getChild());
        changed = changed || syncChanged;
      }
    }

    return changed;
  }

  private RootRenderNode findNode(final List<RootRenderNode> nodes, final int id) {
    for (int i=0; i<nodes.size(); i++) {
      RootRenderNode node = nodes.get(i);
      if (node.getNodeId() == id) {
        return node;
      }
    }
    return null;
  }

  private RootRenderNode createRenderNodeBufferParent(
      final InternalNiftyNode node,
      final InternalNiftyNode parent,
      final NiftyRenderTarget parentRenderTarget) {
    return new RootRenderNode(
        createRenderNodeBufferChild(node),
        createRenderTarget(node, parent, parentRenderTarget));
  }

  private RenderNode createRenderNodeBufferChild(final InternalNiftyNode node) {
    RenderNode renderNode = new RenderNode(
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
    return Mat4.mul(Mat4.mul(Mat4.mul(Mat4.mul(
            Mat4.createTranslate(node.getX(), node.getY(), 0.f),
            Mat4.createRotate((float) node.getRotationX(), 1.f, 0.f, 0.f)),
            Mat4.createRotate((float) node.getRotationY(), 0.f, 1.f, 0.f)),
            Mat4.createRotate((float) node.getRotationZ(), 0.f, 0.f, 1.f)),
            Mat4.createScale((float) node.getScaleX(), (float) node.getScaleY(), (float) node.getScaleZ()));
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

  private boolean syncRenderNodeBufferChildNodes(final InternalNiftyNode src, final RenderNode dst) {
    boolean widthChanged = dst.getWidth() != src.getWidth();
    boolean heightChanged = dst.getHeight() != src.getHeight();
    boolean canvasChanged = src.getCanvas().isChanged();
    boolean thisNodeChanged = (widthChanged || heightChanged || canvasChanged || src.isTransformationChanged());

    if (thisNodeChanged) {
      dst.setLocal(buildLocalTransformation(src));
      dst.setWidth(src.getWidth());
      dst.setHeight(src.getHeight());
      dst.setCommands(src.getCanvas().getCommands());
      dst.markChanged();
      src.resetTransformationChanged();
    }

    boolean childChanged = false;
    for (int i=0; i<src.getChildren().size(); i++) {
      boolean currentChildChanged = syncRenderNodeBufferChild(src.getChildren().get(i), dst);
      childChanged = childChanged || currentChildChanged;
    }

    if (thisNodeChanged || childChanged) {
      dst.markNeedsReRender();
    }

    return thisNodeChanged || childChanged;
  }

  private boolean syncRenderNodeBufferChild(final InternalNiftyNode src, final RenderNode dstParent) {
    RenderNode dst = dstParent.findChildWithId(src.getId());
    if (dst == null) {
      dstParent.addChildNode(createRenderNodeBufferChild(src));
      return true;
    } else {
      return syncRenderNodeBufferChildNodes(src, dst);
    }
  }
}
