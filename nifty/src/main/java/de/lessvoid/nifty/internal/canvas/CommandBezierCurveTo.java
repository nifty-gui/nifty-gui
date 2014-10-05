package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandBezierCurveTo implements Command {
  private final float cp1x;
  private final float cp1y;
  private final float cp2x;
  private final float cp2y;
  private final float x;
  private final float y;

  public CommandBezierCurveTo(final float cp1x, final float cp1y, final float cp2x, final float cp2y, final float x, final float y) {
    this.cp1x = cp1x;
    this.cp1y = cp1y;
    this.cp2x = cp2x;
    this.cp2y = cp2y;
    this.x = x;
    this.y = y;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y);
  }
}
