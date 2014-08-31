package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandLineTo implements Command {
  private final float x;
  private final float y;

  public CommandLineTo(final float x, final float y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.lineTo(x, y);
  }
}
