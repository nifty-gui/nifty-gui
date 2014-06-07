package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandFilledRect implements Command {
  private final double x0;
  private final double y0;
  private final double x1;
  private final double y1;

  public CommandFilledRect(final double x0, final double y0, final double x1, final double y1) {
    this.x0 = x0;
    this.y0 = y0;
    this.x1 = x1;
    this.y1 = y1;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    if (context.getFillLinearGradient() != null) {
      batchManager.addLinearGradientQuad(x0, y0, x1, y1, context.getFillLinearGradient());
      return;
    }
    batchManager.addColorQuad(x0, y0, x1, y1, context.getFillColor());
  }
}
