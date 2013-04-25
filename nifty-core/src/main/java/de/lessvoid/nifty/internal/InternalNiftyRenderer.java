package de.lessvoid.nifty.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.canvas.InternalNiftyCanvas;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

public class InternalNiftyRenderer {
  private final Logger log = Logger.getLogger(InternalNiftyRenderer.class.getName());

  // The list of render nodes.
  private final List<InternalRenderNode> rootRenderNodes = new ArrayList<InternalRenderNode>();

  // The accessor to access the InternalNiftyNode of a NiftyNode without making any of it public.
  private final NiftyNodeAccessor niftyNodeAccessor;

  // The render device.
  private final NiftyRenderDevice renderDevice;

  public InternalNiftyRenderer(final NiftyRenderDevice renderDevice) {
    this.niftyNodeAccessor = NiftyNodeAccessor.getDefault();
    this.renderDevice = renderDevice;
  }

  public void synchronize(final List<NiftyNode> rootNodes) {
    rootRenderNodes.clear();
    for (int i=0; i<rootNodes.size(); i++) {
      rootRenderNodes.add(toRenderNode(niftyNodeAccessor.getInternalNiftyNode(rootNodes.get(i))));
    }
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

  private InternalRenderNode toRenderNode(final InternalNiftyNode node) {
    InternalRenderNode renderNode = new InternalRenderNode(node.getCanvas());
    InternalChildIterate.iterate(node.getChildren(), convertToRenderNode, renderNode);
    return renderNode;
  }

  private InternalChildIterate.Function<InternalNiftyNode, InternalRenderNode> convertToRenderNode =
      new InternalChildIterate.Function<InternalNiftyNode, InternalRenderNode>() {
    @Override
    public void perform(final InternalNiftyNode node, final InternalRenderNode parent) {
      parent.addChildNode(toRenderNode(node));
    }
  };

}
