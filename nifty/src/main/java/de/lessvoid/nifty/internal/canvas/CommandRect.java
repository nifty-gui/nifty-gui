package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.spi.NiftyRenderDevice;

public class CommandRect implements Command {
  private final double x0;
  private final double y0;
  private final double x1;
  private final double y1;

  public CommandRect(final double x0, final double y0, final double x1, final double y1) {
    this.x0 = x0;
    this.y0 = y0;
    this.x1 = x1;
    this.y1 = y1;
  }

  @Override
  public void execute(final NiftyRenderDevice renderDevice, final Context context) {
  }

}
