package de.lessvoid.nifty.loaderv2.types.helper;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;

import javax.annotation.Nullable;

public class NullElementRendererCreator implements ElementRendererCreator {
  @Override
  @Nullable
  public ElementRenderer[] createElementRenderer(Nifty nifty) {
    return null;
  }
}
