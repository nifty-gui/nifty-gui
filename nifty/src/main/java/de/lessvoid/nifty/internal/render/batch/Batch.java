package de.lessvoid.nifty.internal.render.batch;

import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * A Batch stores pre-transformed vertices that share the same rendering state and that can be rendered.
 */
public interface Batch {
  void render(NiftyRenderDevice renderDevice);
}
