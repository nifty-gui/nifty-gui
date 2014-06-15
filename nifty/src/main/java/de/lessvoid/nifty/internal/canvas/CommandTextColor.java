package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandTextColor implements Command {
  private final NiftyColor textColor;

  public CommandTextColor(final NiftyColor textColor) {
    this.textColor = textColor;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    context.setTextColor(textColor);
  }
}
