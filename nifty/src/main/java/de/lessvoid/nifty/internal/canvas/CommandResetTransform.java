package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandResetTransform implements Command {

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.resetTransform();
  }

}
