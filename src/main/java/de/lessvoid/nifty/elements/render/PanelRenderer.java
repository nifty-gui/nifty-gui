package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.tools.Color;

/**
 * The ElementRenderer for a Panel.
 * @author void
 */
public class PanelRenderer implements ElementRenderer {

  /**
   * the background color when used otherwise null.
   */
  private Color backgroundColor;

  /**
   * Default constructor.
   */
  public PanelRenderer() {
  }

  /**
   * render it.
   *
   * @param element
   *            the widget we're connected to
   * @param r
   *            the renderDevice we should use
   */
  public final void render(final Element element, final NiftyRenderEngine r) {

    if (backgroundColor != null) {
      r.saveState(RenderStateType.allStates());
      if (!r.isColorChanged()) {
        r.setColor(backgroundColor);
      } else {
        r.setColor(new Color(backgroundColor, r.getColorAlpha()));
      }
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
      r.restoreState();
    }
  }

  /**
   * set a background color.
   * @param newBackgroundColor background color
   */
  public final void setBackgroundColor(final Color newBackgroundColor) {
    this.backgroundColor = newBackgroundColor;
  }
}
