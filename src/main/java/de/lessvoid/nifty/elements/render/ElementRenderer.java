package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 * The element renderer.
 * @author void
 */
public interface ElementRenderer {

  /**
   * render the element.
   * @param w the Widget
   * @param r the RenderDevice for output.
   */
  void render(Element w, NiftyRenderEngine r);
}
