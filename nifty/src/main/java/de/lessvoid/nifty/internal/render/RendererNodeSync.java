package de.lessvoid.nifty.internal.render;

import java.util.List;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * Synchronize a list of NiftyNodes to a list of RootRenderNode.
 *
 * @author void
 */
public class RendererNodeSync {
  // since we want to access the nodes private API we need a NiftyNodeAccessor
  private final NiftyNodeAccessor niftyNodeAccessor;

  // and since we need to create render resources like textures we'll keep an NiftyRenderDevice around as well
  private final NiftyRenderDevice renderDevice;

  /**
   * Create the RendererNodeSync using the NiftyRenderDevice given.
   * @param renderDevice the NiftyRenderDevice
   */
  public RendererNodeSync(final NiftyRenderDevice renderDevice) {
    this.niftyNodeAccessor = NiftyNodeAccessor.getDefault();
    this.renderDevice = renderDevice;
  }

  /**
   * Synchronize the list of given NiftyNode srcNodes into the list of RootRenderNode in dstNodes.
   * @param srcNodes the source nodes
   * @param dstNodes the destination nodes
   * @return true if anything changed and false if it is exactly the same as in the last frame
   */
  public boolean synchronize(final List<NiftyNode> srcNodes, final List<RenderNode> dstNodes) {
    boolean changed = false;

    for (int i=0; i<srcNodes.size(); i++) {
      InternalNiftyNode src = niftyNodeAccessor.getInternalNiftyNode(srcNodes.get(i));
      RenderNode dst = findNode(dstNodes, src.getId());
      if (dst == null) {
        dstNodes.add(createRenderNode(src));
        changed = true;
        continue;
      }
      boolean syncChanged = syncRenderNodeBufferChildNodes(src, dst);
      changed = changed || syncChanged;
    }

    return changed;
  }

  private RenderNode findNode(final List<RenderNode> nodes, final int id) {
    for (int i=0; i<nodes.size(); i++) {
      RenderNode node = nodes.get(i);
      if (node.getNodeId() == id) {
        return node;
      }
    }
    return null;
  }

  private RenderNode createRenderNode(final InternalNiftyNode node) {
    RenderNode renderNode = new RenderNode(
        node.getId(),
        buildLocalTransformation(node),
        node.getWidth(),
        node.getHeight(),
        node.getCanvas().getCommands(),
        renderDevice.createTexture(node.getWidth(), node.getHeight()),
        renderDevice);

    for (int i=0; i<node.getChildren().size(); i++) {
      renderNode.addChildNode(createRenderNode(node.getChildren().get(i)));
    }

    return renderNode;
  }

  private Mat4 buildLocalTransformation(final InternalNiftyNode node) {
    float pivotX = (float) node.getPivotX() * node.getWidth();
    float pivotY = (float) node.getPivotY() * node.getHeight();
    return Mat4.mul(Mat4.mul(Mat4.mul(Mat4.mul(Mat4.mul(Mat4.mul(
        Mat4.createTranslate(node.getX(), node.getY(), 0.f),
        Mat4.createTranslate(pivotX, pivotY, 0.0f)),
        Mat4.createRotate((float) node.getRotationX(), 1.f, 0.f, 0.f)),
        Mat4.createRotate((float) node.getRotationY(), 0.f, 1.f, 0.f)),
        Mat4.createRotate((float) node.getRotationZ(), 0.f, 0.f, 1.f)),
        Mat4.createScale((float) node.getScaleX(), (float) node.getScaleY(), (float) node.getScaleZ())),
        Mat4.createTranslate(-pivotX, -pivotY, 0.0f));
  }

  private boolean syncRenderNodeBufferChildNodes(final InternalNiftyNode src, final RenderNode dst) {
    boolean thisNodeChanged = nodeChanged(src, dst);
    if (thisNodeChanged) {
      dst.setLocal(buildLocalTransformation(src));
      dst.setWidth(src.getWidth());
      dst.setHeight(src.getHeight());
      dst.setCommands(src.getCanvas().getCommands());
      dst.nodeChanged();
      src.resetTransformationChanged();
    }

    boolean childChanged = false;
    for (int i=0; i<src.getChildren().size(); i++) {
      boolean currentChildChanged = syncRenderNodeBufferChild(src.getChildren().get(i), dst);
      childChanged = childChanged || currentChildChanged;
    }

    if (thisNodeChanged || childChanged) {
      dst.contentChanged();
    }

    return thisNodeChanged || childChanged;
  }

  private boolean nodeChanged(final InternalNiftyNode src, final RenderNode dst) {
    boolean widthChanged = dst.getWidth() != src.getWidth();
    boolean heightChanged = dst.getHeight() != src.getHeight();
    boolean canvasChanged = src.getCanvas().isChanged();
    return (widthChanged || heightChanged || canvasChanged || src.isTransformationChanged());
  }

  private boolean syncRenderNodeBufferChild(final InternalNiftyNode src, final RenderNode dstParent) {
    RenderNode dst = dstParent.findChildWithId(src.getId());
    if (dst == null) {
      dstParent.addChildNode(createRenderNode(src));
      return true;
    }
    return syncRenderNodeBufferChildNodes(src, dst);
  }
}
