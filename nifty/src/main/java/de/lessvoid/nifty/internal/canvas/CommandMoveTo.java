package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandMoveTo implements Command {
  private final float x;
  private final float y;

  public CommandMoveTo(final float x, final float y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.moveTo(x, y);
  }

}
