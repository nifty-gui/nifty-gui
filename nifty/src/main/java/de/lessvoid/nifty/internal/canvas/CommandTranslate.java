package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandTranslate implements Command {
  private final double x;
  private final double y;

  public CommandTranslate(final double x, final double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.addTransform(Mat4.createTranslate((float) x, (float) y, 0.f));
  }
}
