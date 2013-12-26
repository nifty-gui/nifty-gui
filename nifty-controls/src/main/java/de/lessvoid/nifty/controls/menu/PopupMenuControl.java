package de.lessvoid.nifty.controls.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PopupMenuControl extends AbstractController {
  @Nullable
  private Nifty nifty;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters properties) {
    bind(element);
    this.nifty = nifty;
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  public void closePopup() {
    if (nifty != null) {
      String id = getId();
      if (id != null) {
        nifty.closePopup(id);
      }
    }
  }
}
