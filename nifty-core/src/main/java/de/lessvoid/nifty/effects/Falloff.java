package de.lessvoid.nifty.effects;

import java.util.Properties;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * The Falloff class.
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

  private HoverFalloffType falloffType = HoverFalloffType.none;
  private HoverFalloffConstraint falloffConstraint = HoverFalloffConstraint.none;
  private SizeValue hoverWidth;
  private SizeValue hoverHeight;
  private float falloffValue;

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

  public Falloff() {
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
      return hoverHeight.getValueAsInt( widget.getHeight() );
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
      return hoverWidth.getValueAsInt( widget.getWidth() );
    }
  }

  /**
   * calc falloff diszance.
   * @param element the element to check
   * @param mouseX the mouse position x
   * @param mouseY the mouse position y
   * @return the falloff distance as normalized value [0,1]
   */
  public void updateFalloffValue(final Element element, final int mouseX, final int mouseY) {
    if (falloffConstraint == HoverFalloffConstraint.none) {
      falloffValue = 1.0f;
      return;
    }

    int centerX = element.getX() + element.getWidth() / 2;
    int centerY = element.getY() + element.getHeight() / 2;

    float dx = mouseX - centerX;
    float dy = mouseY - centerY;

    //int falloff = 0;
    float falloff = 0; 
    if (falloffConstraint == HoverFalloffConstraint.vertical) {
      dx = 0;
      falloff = getVerticalHover(element) / 2;
    }

    if (falloffConstraint == HoverFalloffConstraint.horizontal) {
      dy = 0;
      falloff = getHorizontalHover(element) / 2;
    }

    if (falloffConstraint == HoverFalloffConstraint.both) {
      /* determine the angle from centre of element to current mouse position.
       * NOTE: if dy and dy are zero it is not possible to determine the angle.
       * Assume an angle of zero degrees if dy AND dx are 0 */
      double dA = 0;
      if (dy ==0 && dx == 0) {
        dA = 0;
      } else {
        dA = Math.abs(Math.atan((dy / dx)));
      }
      /*determine the angle from the centre of the object to one of its corners to find the flip / cutoff angle */
      float elA = (float)(getHorizontalHover(element) / 2);
      float elB = (float)(getVerticalHover(element) / 2);
      double dB = 0;
      dB = Math.abs(Math.atan( ( elB / elA ) ));
      if ((Math.abs(Math.toDegrees(dA)) >= 0) && ( Math.abs(Math.toDegrees(dA)) <= Math.abs(Math.toDegrees(dB)) ) ){
        //use cos(dA) = adj / hyp: so hyp = adj / cos(dA)
        falloff = (float)(elA / Math.cos(dA));
      }
      if ((Math.abs(Math.toDegrees(dA)) > Math.abs(Math.toDegrees(dB))) && ( Math.abs(Math.toDegrees(dA)) <= 90 ) ){
        //use sin(dA) = opp / hyp: so hyp = opp / sin(dA)
        falloff = (float)(elB / Math.sin(dA));
      }
    }

    float d = (float) Math.hypot(dx, dy);
    if (d > falloff) {
      falloffValue = 0.0f;
    }

    falloffValue = Math.abs(1.0f - (float) d / (float) falloff);
  }

  public float getFalloffValue() {
    return falloffValue;
  }

  public HoverFalloffConstraint getFalloffConstraint() {
    return falloffConstraint;
  }
}
