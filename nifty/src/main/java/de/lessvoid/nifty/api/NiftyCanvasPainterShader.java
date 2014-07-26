package de.lessvoid.nifty.api;

import de.lessvoid.nifty.internal.canvas.InternalNiftyCanvas;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * The NiftyCanvasPainterShader will use a custom fragment shader.
 *
 * @author void
 */
public class NiftyCanvasPainterShader implements NiftyCanvasPainter {
  private final String shaderId;

  /* protected */ NiftyCanvasPainterShader(final NiftyRenderDevice renderDevice, final String shaderName) {
    shaderId = renderDevice.loadCustomShader(shaderName);
  }

  @Override
  public void paint(final NiftyNode node, final NiftyCanvas canvas) {
    InternalNiftyCanvas internalCanvas = canvas.getImpl();
    internalCanvas.customerShader(shaderId);
  }
}
