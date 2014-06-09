package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandStrokeStyle implements Command {
  private final NiftyColor color;

  public CommandStrokeStyle(final NiftyColor color) {
    this.color = color;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.setStrokeStyle(color);
  }

}
