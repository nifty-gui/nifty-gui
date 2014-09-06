package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyLineCapType;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandLineCapType implements Command {
  private NiftyLineCapType lineCapType;

  public CommandLineCapType(final NiftyLineCapType lineCapType) {
    this.lineCapType = lineCapType;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.setLineCapType(lineCapType);
  }
}
