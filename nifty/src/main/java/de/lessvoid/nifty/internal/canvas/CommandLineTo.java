package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandLineTo implements Command {
  private final double x;
  private final double y;

  public CommandLineTo(final double x, final double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.lineTo(x, y);
  }
}
