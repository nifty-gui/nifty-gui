package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

public class CommandFillColor implements Command {
  private final NiftyColor color;

  public CommandFillColor(final NiftyColor color) {
    this.color = color;
  }

  @Override
  public void execute(final NiftyTexture renderTarget, final Context context) {
    context.setFillColor(color);
  }

}
