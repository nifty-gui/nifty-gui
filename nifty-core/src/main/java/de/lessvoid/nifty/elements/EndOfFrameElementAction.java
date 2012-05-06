package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.screen.Screen;

public class EndOfFrameElementAction {
  private Screen screen;
  private Element element;
  private Action action;
  private EndNotify endNotify;

  public EndOfFrameElementAction(final Screen newScreen, final Element newElement, final Action action, final EndNotify endNotify) {
    this.screen = newScreen;
    this.element = newElement;
    this.action = action;
    this.endNotify = endNotify;
  }

  public void perform() {
    action.perform(screen, element);
    if (endNotify != null) {
      endNotify.perform();
    }
  }
}