package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

public class CommandStrokeColor implements Command {
  private final NiftyColor color;

  public CommandStrokeColor(final NiftyColor color) {
    this.color = color;
  }

  @Override
  public void execute(final NiftyRenderDevice renderDevice, final Context context) {
  }

}
