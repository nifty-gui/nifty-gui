package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandRotate implements Command {
  private final float angle;

  public CommandRotate(final float angleDegree) {
    this.angle = angleDegree;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.addTransform(Mat4.createRotate(angle, 0.f, 0.f, 1.f));
  }
}
