package de.lessvoid.nifty.elements.render;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderDevice;
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
   * @param w
   *            the widget we're connected to
   * @param r
   *            the renderDevice we should use
   */
  public final void render(final Element w, final RenderDevice r) {

    // use background image?
    if (backgroundImage != null) {
      r.renderImage(backgroundImage, w.getX(), w.getY(), w.getWidth(), w.getHeight());
    }

    if (backgroundColor != null) {
      r.saveState(RenderState.allStates());
      if (!r.isColorChanged()) {
        r.setColor(
          backgroundColor.getRed(),
          backgroundColor.getGreen(),
          backgroundColor.getBlue(),
          backgroundColor.getAlpha());
      }
      r.enableBlend();
      r.disableTexture();
      r.renderQuad(w.getX(), w.getY(), w.getWidth(), w.getHeight());
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
