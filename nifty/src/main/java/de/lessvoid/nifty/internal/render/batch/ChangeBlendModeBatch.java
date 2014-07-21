package de.lessvoid.nifty.internal.render.batch;

import de.lessvoid.nifty.api.BlendMode;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

public class ChangeBlendModeBatch implements Batch {
  private final BlendMode blendMode;

  public ChangeBlendModeBatch(final BlendMode blendMode) {
    this.blendMode = blendMode;
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    renderDevice.changeBlendMode(blendMode);
  }
}
