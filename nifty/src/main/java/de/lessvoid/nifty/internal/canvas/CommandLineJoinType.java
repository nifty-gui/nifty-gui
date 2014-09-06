package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyLineJoinType;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandLineJoinType implements Command {
  private NiftyLineJoinType lineJoinType;

  public CommandLineJoinType(final NiftyLineJoinType lineJoinType) {
    this.lineJoinType = lineJoinType;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.setLineJoinType(lineJoinType);
  }
}
