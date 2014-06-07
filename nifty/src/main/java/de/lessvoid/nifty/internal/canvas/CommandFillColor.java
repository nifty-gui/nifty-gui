package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandFillColor implements Command {
  private final NiftyColor color;

  public CommandFillColor(final NiftyColor color) {
    this.color = new NiftyColor(color);
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.setFillColor(color);
  }

}
