package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

public class CommandFillLinearGradient implements Command {
  private final NiftyLinearGradient gradient;

  public CommandFillLinearGradient(final NiftyLinearGradient gradient) {
    this.gradient = gradient;
  }

  @Override
  public void execute(final NiftyRenderDevice renderDevice, final Context context) {
    context.setFillLinearGradient(gradient);
  }
}
