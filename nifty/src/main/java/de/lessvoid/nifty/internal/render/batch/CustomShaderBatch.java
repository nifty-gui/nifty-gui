package de.lessvoid.nifty.internal.render.batch;

import de.lessvoid.nifty.spi.NiftyRenderDevice;

public class CustomShaderBatch implements Batch {
  private final String shaderId;

  public CustomShaderBatch(final String shaderId) {
    this.shaderId = shaderId;
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    renderDevice.renderWithShader(shaderId);
  }
}
