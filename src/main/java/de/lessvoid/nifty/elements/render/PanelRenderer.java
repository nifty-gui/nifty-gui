package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderEngine;
import de.lessvoid.nifty.render.RenderImage;
import de.lessvoid.nifty.render.RenderState;
import de.lessvoid.nifty.tools.Color;

/**
 * The ElementRenderer for a Panel.
 * @author void
 */
public class PanelRenderer implements ElementRenderer {

  /**
   * the background image when used otherwise null.
   */
  private RenderImage backgroundImage;

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
  public final void render(final Element element, final RenderEngine r) {

    // use background image?
    if (backgroundImage != null) {
      r.renderImage(backgroundImage, element.getX(), element.getY(), element.getWidth(), element.getHeight());
    }

    if (backgroundColor != null) {
      r.saveState(RenderState.allStates());
      if (!r.isColorChanged()) {
        r.setColor(backgroundColor);
      }
      r.enableBlend();
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
      r.restoreState();
    }
  }

  /**
   * set a background image.
   * @param newBackgroundImage background image
   */
  public final void setBackgroundImage(final RenderImage newBackgroundImage) {
    this.backgroundImage = newBackgroundImage;
  }

  /**
   * set a background color.
   * @param newBackgroundColor background color
   */
  public final void setBackgroundColor(final Color newBackgroundColor) {
    this.backgroundColor = newBackgroundColor;
  }
}
