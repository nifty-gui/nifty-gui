package de.lessvoid.nifty.elements.render;

import java.util.Random;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
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

  private Color debugColor;

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
  public void render(final Element element, final NiftyRenderEngine r) {
    if (element.getNifty().isDebugOptionPanelColors()) {
      r.saveState(null);
      r.setColor(getDebugColor());
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
      r.restoreState();
      return;
    }

    if (backgroundColor != null) {
      r.saveState(null);
      if (!r.isColorChanged()) {
        if (r.isColorAlphaChanged()) {
          r.setColorIgnoreAlpha(backgroundColor);
        } else {
          r.setColor(backgroundColor);
        }
      }
      r.renderQuad(element.getX(), element.getY(), element.getWidth(), element.getHeight());
      r.restoreState();
    }
  }

  private Color getDebugColor() {
    if (debugColor != null) {
      return debugColor;
    }
    debugColor =  new Color(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), .5f);
    return debugColor;
  }

  /**
   * set a background color.
   * @param newBackgroundColor background color
   */
  public void setBackgroundColor(final Color newBackgroundColor) {
    this.backgroundColor = newBackgroundColor;
  }
}
