package de.lessvoid.nifty.effects.shared;

import java.util.Properties;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * The Fallof class.
 * @author void
 */
public class Falloff {

  /**
   * fallofftype.
   */
  public static final String HOVER_FALLOFF_TYPE = "hoverFalloffType";

  /**
   * falloff constraint.
   */
  public static final String HOVER_FALLOFF_CONSTRAINT = "hoverFalloffConstraint";

  /**
   * hover width.
   */
  public static final String HOVER_WIDTH = "hoverWidth";

  /**
   * hover height.
   */
  public static final String HOVER_HEIGHT = "hoverHeight";

  /**
   * falloff type.
   * @author void
   */
  public enum HoverFalloffType {
    /**
     * none, the default.
     */
    none,

    /**
     * linear.
     */
    linear;
  }

  /**
   * falloff constraint.
   * @author void
   *
   */
  public enum HoverFalloffConstraint {
    /**
     * none, the default.
     */
    none,

    /**
     * vertical.
     */
    vertical,

    /**
     * horizontal.
     */
    horizontal,

    /**
     * both.
     */
    both;
  }

  /**
   * falloftype.
   */
  private HoverFalloffType falloffType = HoverFalloffType.none;

  /**
   * fallofconstraint.
   */
  private HoverFalloffConstraint falloffConstraint = HoverFalloffConstraint.none;

  /**
   * hover width.
   */
  private SizeValue hoverWidth;

  /**
   * hover height.
   */
  private SizeValue hoverHeight;

  /**
   * Falloff constructor.
   * @param parameter parameter properties
   */
  public Falloff(final Properties parameter) {
    String falloffTypeString = parameter.getProperty(Falloff.HOVER_FALLOFF_TYPE);
    if (falloffTypeString != null) {
      falloffType = HoverFalloffType.valueOf(falloffTypeString);
    }

    String falloffConstraintString = parameter.getProperty(Falloff.HOVER_FALLOFF_CONSTRAINT);
    if (falloffConstraintString != null) {
      falloffConstraint = HoverFalloffConstraint.valueOf(falloffConstraintString);
    }

    String hoverWidthString = parameter.getProperty(Falloff.HOVER_WIDTH);
    if (hoverWidthString != null) {
      hoverWidth = new SizeValue(hoverWidthString);
    }

    String hoverHeightString = parameter.getProperty(Falloff.HOVER_HEIGHT);
    if (hoverHeightString != null) {
      hoverHeight = new SizeValue(hoverHeightString);
    }
  }

  /**
   * apply properties.
   * @param parameter parameter
   */
  public void applyProperties(final Properties parameter) {
    
  }

  /**
   * @param widget
   * @param x
   * @param y
   * @return
   */
  public final boolean isInside(final Element widget, final int x, final int y) {
    int centerX = widget.getX() + widget.getWidth() / 2;
    int centerY = widget.getY() + widget.getHeight() / 2;

    int horizontalHover = getHorizontalHover(widget);
    int verticalHover = getVerticalHover(widget);

    return
        x > (centerX - horizontalHover / 2)
        &&
        x <= (centerX + horizontalHover / 2)
        &&
        y > (centerY - verticalHover / 2)
        &&
        y <= (centerY + verticalHover / 2);
  }

  /**
   * 
   * @param widget
   * @return
   */
  private int getVerticalHover( Element widget ) {
    if( hoverHeight == null ) {
      return widget.getHeight();
    } else {
      return hoverHeight.getValueAsInt( widget.getParent().getHeight() );
    }
  }

  /**
   * 
   * @param widget
   * @return
   */
  private int getHorizontalHover( Element widget ) {
    if( hoverWidth == null ) {
      return widget.getWidth();
    } else {
      return hoverWidth.getValueAsInt( widget.getParent().getWidth() );
    }
  }

  /**
   * calc falloff diszance.
   * @param element the element to check
   * @param mouseX the mouse position x
   * @param mouseY the mouse position y
   * @return the falloff distance as normalized value [0,1]
   */
  public float getFalloffDistance(final Element element, final int mouseX, final int mouseY) {
    if (falloffConstraint == falloffConstraint.none) {
      return 1.0f;
    }

    int centerX = element.getX() + element.getWidth() / 2;
    int centerY = element.getY() + element.getHeight() / 2;

    float dx = mouseX - centerX;
    float dy = mouseY - centerY;

    int falloff = 0;
    if (falloffConstraint == falloffConstraint.vertical) {
      dx = 0;
      falloff = getVerticalHover(element) / 2;
    }

    if (falloffConstraint == falloffConstraint.horizontal) {
      dy = 0;
      falloff = getHorizontalHover(element) / 2;
    }

    if (falloffConstraint == falloffConstraint.both) {
      falloff = Math.max(getHorizontalHover(element) / 2, getVerticalHover(element) / 2);
    }

    float d = (float) Math.hypot(dx, dy);
    if (d > falloff) {
      return 0;
    }

    return 1.0f - (float) d / (float) falloff;
  }
}
