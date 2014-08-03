package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandTranslate implements Command {
  private final float x;
  private final float y;

  public CommandTranslate(final float x, final float y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.addTransform(Mat4.createTranslate(x, y, 0.f));
  }
}
