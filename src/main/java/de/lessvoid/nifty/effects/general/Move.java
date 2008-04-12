package de.lessvoid.nifty.effects.general;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderDevice;

/**
 * Move - move stuff around.
 * @author void
 */
public class Move implements EffectImpl {

  /**
   * left.
   */
  private static final String LEFT = "left";

  /**
   * right.
   */
  private static final String RIGHT = "right";

  /**
   * top.
   */
  private static final String TOP = "top";

  /**
   * bottom.
   */
  private static final String BOTTOM = "bottom";

  /**
   * direction param.
   */
  private String direction;

  /**
   * offset helper.
   */
  private long offset = 0;

  /**
   * start offset helper.
   */
  private long startOffset = 0;

  /**
   * direction: in or out movement.
   */
  private int offsetDir = 0;

  /**
   * Initialize.
   * @param nifty Nifty
   * @param element Element
   * @param parameter Properties
   */
  public final void initialize(final Nifty nifty, final Element element, final Properties parameter) {
    direction = parameter.getProperty("direction");
    if (LEFT.equals(direction)) {
      offset = element.getX() + element.getWidth();
    } else if (RIGHT.equals(direction)) {
      offset = element.getX() + element.getWidth();
    } else if (TOP.equals(direction)) {
      offset = element.getY() + element.getHeight();
    } else if (BOTTOM.equals(direction)) {
      offset = element.getY() + element.getHeight();
    } else {
      offset = 0;
    }

    String type = parameter.getProperty("directionType");
    if ("out".equals(type)) {
      startOffset = 0;
      offsetDir = -1;
    } else if ("in".equals(type)) {
      startOffset = offset;
      offsetDir = 1;
    }
  }

  /**
   * execute the effect.
   * @param element Element
   * @param normalizedTime TimeInterpolator
   * @param r RenderDevice
   */
  public void execute(final Element element, final float normalizedTime, final RenderDevice r) {
    if (LEFT.equals(direction)) {
      r.moveTo(-startOffset + offsetDir * normalizedTime * offset, 0);
    } else if (RIGHT.equals(direction)) {
      r.moveTo(startOffset - offsetDir * normalizedTime * offset, 0);
    } else if (TOP.equals(direction)) {
      r.moveTo(0, -startOffset + offsetDir * normalizedTime * offset);
    } else if (BOTTOM.equals(direction)) {
      r.moveTo(0, startOffset - offsetDir * normalizedTime * offset);
    }
  }
}
