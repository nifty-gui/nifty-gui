package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.EndNotify;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EndOfFrameElementAction {
  @Nonnull
  private final Action action;
  @Nullable
  private final EndNotify endNotify;

  public EndOfFrameElementAction(
      @Nonnull final Action action,
      @Nullable final EndNotify endNotify) {
    this.action = action;
    this.endNotify = endNotify;
  }

  public void perform() {
    action.perform();
    if (endNotify != null) {
      endNotify.perform();
    }
  }
}