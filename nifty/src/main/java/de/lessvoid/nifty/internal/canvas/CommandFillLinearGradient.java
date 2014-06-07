package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandFillLinearGradient implements Command {
  private final NiftyLinearGradient gradient;

  public CommandFillLinearGradient(final NiftyLinearGradient gradient) {
    this.gradient = gradient;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.setFillLinearGradient(gradient);
  }
}
