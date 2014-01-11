package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

public class CommandFillColor implements Command {
  private final NiftyColor color;

  public CommandFillColor(final NiftyColor color) {
    this.color = new NiftyColor(color);
  }

  @Override
  public void execute(final NiftyRenderDevice renderDevice, final Context context) {
    context.setFillColor(color);
  }

}
