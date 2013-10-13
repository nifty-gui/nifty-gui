package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.spi.NiftyRenderTarget;

public class CommandLineWidth implements Command {
  private double lineWidth;

  public CommandLineWidth(final double lineWidth) {
    this.lineWidth = lineWidth;
  }

  @Override
  public void execute(final NiftyRenderTarget renderTarget, final Context context) {
    context.setLineWidth((float) lineWidth);
  }
}
