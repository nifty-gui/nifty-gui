package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandMoveTo implements Command {
  private final double x;
  private final double y;

  public CommandMoveTo(final double x, final double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.moveTo(x, y);
  }

}
