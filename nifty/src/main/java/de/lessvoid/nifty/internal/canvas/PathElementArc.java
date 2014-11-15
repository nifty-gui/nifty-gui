package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyArcParameters;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class PathElementArc implements PathElement {
  private double x;
  private double y;
  private double r;
  private double startAngle;
  private double endAngle;

  public PathElementArc(final double x, final double y, final double r, final double startAngle, final double endAngle) {
    this.x = x;
    this.y = y;
    this.r = r;
    this.startAngle = startAngle;
    this.endAngle = endAngle;
  }

  @Override
  public void render(
      final Context context,
      final BatchManager batchManager,
      final boolean first,
      final boolean last) {
    batchManager.addArc(
        x, y, r,
        startAngle, endAngle,
        context.getTransform(),
        new NiftyArcParameters(context.getLineParameters(), (float) startAngle, (float) endAngle, (float) r),
        first, last);
  }
}