package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class PathElementLine implements PathElement {
  private double x;
  private double y;

  public PathElementLine(final double x, final double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void render(
      final Context context,
      final BatchManager batchManager,
      final boolean first,
      final boolean last) {
    batchManager.addLineVertex((float) x, (float) y, context.getTransform(), context.getLineParameters(), first, last);
  }
}