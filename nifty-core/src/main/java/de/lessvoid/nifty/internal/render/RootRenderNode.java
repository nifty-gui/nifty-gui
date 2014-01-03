package de.lessvoid.nifty.internal.render;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.internal.canvas.Context;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

public class RootRenderNode {
  private final Logger log = Logger.getLogger(RootRenderNode.class.getName());
  private final RenderNode child;
  private final NiftyTexture renderTarget;
  private final Context context;
  private final StringBuilder getStateInfo = new StringBuilder();

  public RootRenderNode(final RenderNode child, final NiftyTexture renderTarget) {
    this.child = child;
    this.renderTarget = renderTarget;
    this.context = new Context();
  }

  public void render(final NiftyRenderDevice renderDevice) {
    if (log.isLoggable(Level.FINE)) {
      log.fine(getStateInfo());
    }
    renderDevice.beginStencil();
    child.prepareRender(renderTarget);
    renderDevice.endStencil();

    renderDevice.enableStencil();
    child.updateContent(renderDevice, renderTarget, context, child.getLocal().invert(), false);
    renderDevice.disableStencil();

    renderDevice.render(renderTarget, child.getLocal());
  }

  public int getNodeId() {
    return child.getNodeId();
  }

  public RenderNode getChild() {
    return child;
  }

  public String getStateInfo() {
    getStateInfo.setLength(0);
    getStateInfo.append("# Nifty render tree dump\n");
    child.outputStateInfo(getStateInfo, "");
    return getStateInfo.toString();
  }
}
