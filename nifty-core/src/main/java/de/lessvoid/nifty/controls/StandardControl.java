package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;

public interface StandardControl {
  @Nonnull
  Element createControl(@Nonnull Nifty nifty, @Nonnull Screen screen, @Nonnull Element parent);
}
