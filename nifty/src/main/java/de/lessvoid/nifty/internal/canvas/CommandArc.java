package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandArc implements Command {
  private final double x;
  private final double y;
  private final double r;
  private final double startAngle;
  private final double endAngle;

  public CommandArc(final double x, final double y, final double r, final double startAngle, final double endAngle) {
    this.x = x;
    this.y = y;
    this.r = r;
    this.startAngle = startAngle;
    this.endAngle = endAngle;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.arc(x, y, r, startAngle, endAngle);
  }
}
