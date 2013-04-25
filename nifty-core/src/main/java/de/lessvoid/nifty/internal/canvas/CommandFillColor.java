package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderTarget;

public class CommandFillColor implements Command {
  private final NiftyColor color;

  public CommandFillColor(final NiftyColor color) {
    this.color = color;
  }

  @Override
  public void execute(final NiftyRenderTarget renderTarget, final Context context) {
    context.setFillColor(color);
  }

}
