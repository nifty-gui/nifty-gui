package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandArc implements Command {
  private final static double TWO_PI = 2 * Math.PI;

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

  double normalize(double angle) {
    return angle - Math.floor(angle / TWO_PI) * TWO_PI;
  }
  
  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    double start = normalize(startAngle);
    double end = normalize(endAngle);
    context.arc(x, y, r, start, end);
  }
}
