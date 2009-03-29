package de.lessvoid.nifty.loaderv2.types.helper;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;

public class NullElementRendererCreator implements ElementRendererCreator {
  public ElementRenderer[] createElementRenderer(Nifty nifty) {
    return null;
  } 
}
