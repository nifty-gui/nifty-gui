package de.lessvoid.nifty.render;

import java.io.IOException;

import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;

public class NiftyMouseImpl implements NiftyMouse {
  private RenderDevice renderDevice;
  private InputSystem inputSystem;

  public NiftyMouseImpl(final RenderDevice renderDevice, final InputSystem inputSystem) {
    this.renderDevice = renderDevice;
    this.inputSystem = inputSystem;
  }

  @Override
  public MouseCursor registerMouseCursor(final String filename, final int hotspotX, final int hotspotY) throws IOException {
    return renderDevice.createMouseCursor(filename, hotspotX, hotspotY);
  }

  @Override
  public void resetMouseCursor() {
    renderDevice.disableMouseCursor();
  }

  @Override
  public void enableMouseCursor(final MouseCursor cursor) {
    renderDevice.enableMouseCursor(cursor);
  }

  @Override
  public void setMousePosition(final int x, final int y) {
    inputSystem.setMousePosition(x, y);
  }
}
