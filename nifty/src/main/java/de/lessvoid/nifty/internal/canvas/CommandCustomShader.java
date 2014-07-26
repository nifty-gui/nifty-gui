package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandCustomShader implements Command {
  private final String shaderId;

  public CommandCustomShader(final String shaderId) {
    this.shaderId = shaderId;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    batchManager.addCustomShader(shaderId);
  }
}
