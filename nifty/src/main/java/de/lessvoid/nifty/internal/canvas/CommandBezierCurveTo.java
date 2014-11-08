package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandBezierCurveTo implements Command {
  private final double cp1x;
  private final double cp1y;
  private final double cp2x;
  private final double cp2y;
  private final double x;
  private final double y;

  public CommandBezierCurveTo(final double cp1x, final double cp1y, final double cp2x, final double cp2y, final double x, final double y) {
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
