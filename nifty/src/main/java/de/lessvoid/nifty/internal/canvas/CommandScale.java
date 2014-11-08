package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandScale implements Command {
  private final double scaleWidth;
  private final double scaleHeight;

  public CommandScale(final double scaleWidth, final double scaleHeight) {
    this.scaleWidth = scaleWidth;
    this.scaleHeight = scaleHeight;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.addTransform(Mat4.createScale((float) scaleWidth, (float) scaleHeight, 1.f));
  }
}
