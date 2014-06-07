package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandStrokeColor implements Command {
  private final NiftyColor color;

  public CommandStrokeColor(final NiftyColor color) {
    this.color = color;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
  }

}
