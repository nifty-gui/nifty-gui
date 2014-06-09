package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandText implements Command {
  private final int x;
  private final int y;
  private final String text;

  public CommandText(final int x, final int y, final String text) {
    this.x = x;
    this.y = y;
    this.text = text;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    
  }
}
