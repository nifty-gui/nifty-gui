package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandLineWidth implements Command {
  private double lineWidth;

  public CommandLineWidth(final double lineWidth) {
    this.lineWidth = lineWidth;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.setLineWidth((float) lineWidth);
  }
}
