package de.lessvoid.nifty.internal.render;

import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * A RenderNode can be rendered to a NiftyRenderDevice.
 * @author void
 */
public interface RenderNode {

  /**
   * Render this RenderNode to the NiftyRenderDevice.
   * @param renderDevice the NiftyRenderDevice to render to
   */
  void render(NiftyRenderDevice renderDevice);
}
