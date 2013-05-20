package de.lessvoid.nifty.internal.render;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.internal.canvas.Context;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderTarget;

/**
 * 
 * @author void
 */
public class RenderNodeContent {
  private final Logger log = Logger.getLogger(RenderNodeContent.class.getName());
  private final RenderNodeContentChild child;
  private final NiftyRenderTarget renderTarget;
  private final Context context;
  private final StringBuilder getStateInfo = new StringBuilder();

  public RenderNodeContent(final RenderNodeContentChild child, final NiftyRenderTarget renderTarget) {
    this.child = child;
    this.renderTarget = renderTarget;
    this.context = new Context();
  }

  public void render(final NiftyRenderDevice renderDevice) {
    if (log.isLoggable(Level.FINE)) {
      log.fine(getStateInfo());
    }

    renderTarget.beginStencil();
    child.prepareRender(renderTarget);
    renderTarget.endStencil();

    renderTarget.enableStencil();
    child.updateContent(renderTarget, context, child.getLocal().invert(), false);
    renderTarget.disableStencil();

    renderDevice.render(renderTarget, child.getLocal());
  }

  public int getNodeId() {
    return child.getNodeId();
  }

  public RenderNodeContentChild getChild() {
    return child;
  }

  public String getStateInfo() {
    getStateInfo.setLength(0);
    getStateInfo.append("# Nfty render tree dump\n");
    child.getStateInfo(getStateInfo, "");
    return getStateInfo.toString();
  }
}
