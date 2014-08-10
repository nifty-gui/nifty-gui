package de.lessvoid.nifty.internal.render.batch;

import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * This is not really a batch but a state change of the current custom Shader.
 */
public class CustomShaderBatch implements Batch<String> {
  private final String shaderId;

  public CustomShaderBatch(final String shaderId) {
    this.shaderId = shaderId;
  }

  @Override
  public void render(final NiftyRenderDevice renderDevice) {
    renderDevice.renderWithShader(shaderId);
  }

  @Override
  public boolean requiresNewBatch(final String param) {
    return true;
  }
}
