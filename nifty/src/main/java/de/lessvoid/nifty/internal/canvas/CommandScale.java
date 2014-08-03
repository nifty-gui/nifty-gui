package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandScale implements Command {
  private final float scaleWidth;
  private final float scaleHeight;

  public CommandScale(final float scaleWidth, final float scaleHeight) {
    this.scaleWidth = scaleWidth;
    this.scaleHeight = scaleHeight;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.addTransform(Mat4.createScale(scaleWidth, scaleHeight, 1.f));
  }
}
