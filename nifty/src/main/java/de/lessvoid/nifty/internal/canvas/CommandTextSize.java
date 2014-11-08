package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandTextSize implements Command {
  private final double textSize;

  public CommandTextSize(final double textSize) {
    this.textSize = textSize;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.setTextSize(textSize);
  }
}
