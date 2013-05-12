package de.lessvoid.nifty.internal.render;

import java.io.IOException;

import de.lessvoid.nifty.internal.canvas.Context;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderTarget;

/**
 * 
 * @author void
 */
public class RenderNodeContent {
  private final RenderNodeContentChild child;
  private final NiftyRenderTarget renderTarget;
  private final Context context;

  public RenderNodeContent(final RenderNodeContentChild child, final NiftyRenderTarget renderTarget) {
    this.child = child;
    this.renderTarget = renderTarget;
    this.context = new Context();
  }

  public void render(final NiftyRenderDevice renderDevice) {
    renderTarget.beginStencil();
    child.prepareRender(renderTarget);
    renderTarget.endStencil();

    renderTarget.enableStencil();
    child.updateContent(renderTarget, context, child.getLocal().invert());
    renderTarget.disableStencil();
    
    renderDevice.render(renderTarget, child.getLocal());
  }

  public int getNodeId() {
    return child.getNodeId();
  }

  public RenderNodeContentChild getChild() {
    return child;
  }
}
