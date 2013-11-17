package de.lessvoid.nifty.effects;

import java.util.Properties;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * @author void
 */
public class Falloff {

  public static final String HOVER_FALLOFF_TYPE = "hoverFalloffType";
  public static final String HOVER_FALLOFF_CONSTRAINT = "hoverFalloffConstraint";
  public static final String HOVER_WIDTH = "hoverWidth";
  public static final String HOVER_HEIGHT = "hoverHeight";

  /**
   * @author void
   */
  public enum HoverFalloffType {
    none, // default
    linear;
  }

  /**
   * @author void
   */
  public enum HoverFalloffConstraint {
    none, // default
    vertical,
    horizontal,
    both;
  }

  private HoverFalloffType falloffType = HoverFalloffType.none;
  private HoverFalloffConstraint falloffConstraint = HoverFalloffConstraint.none;
  private SizeValue hoverWidth;
  private SizeValue hoverHeight;
  private float falloffValue;

  public Falloff(final Properties properties) {
    String falloffTypeString = properties.getProperty(Falloff.HOVER_FALLOFF_TYPE);
    if (falloffTypeString != null) {
      falloffType = HoverFalloffType.valueOf(falloffTypeString);
    }

    String falloffConstraintString = properties.getProperty(Falloff.HOVER_FALLOFF_CONSTRAINT);
    if (falloffConstraintString != null) {
      falloffConstraint = HoverFalloffConstraint.valueOf(falloffConstraintString);
    }

    
    hoverWidth = new SizeValue(properties.getProperty(Falloff.HOVER_WIDTH));
    hoverHeight = new SizeValue(properties.getProperty(Falloff.HOVER_HEIGHT));
    
  }

  public Falloff() {
  }

  public void applyProperties(final Properties properties) {
  }

  public final boolean isInside(final Element element, final int x, final int y) {
    int centerX = element.getX() + element.getWidth() / 2;
    int centerY = element.getY() + element.getHeight() / 2;

    int horizontalHover = getHorizontalHover(element);
    int verticalHover = getVerticalHover(element);

    return x > (centerX - horizontalHover / 2) &&
           x <= (centerX + horizontalHover / 2) &&
           y > (centerY - verticalHover / 2) &&
           y <= (centerY + verticalHover / 2);
  }

  private int getVerticalHover(Element element) {
    return hoverHeight.hasValue() ? hoverHeight.getValueAsInt(element.getHeight()) : element.getHeight();
  }

  private int getHorizontalHover(Element element) {
    return hoverWidth.hasValue() ? hoverWidth.getValueAsInt(element.getWidth()) : element.getWidth();
  }

  public void updateFalloffValue(final Element element, final int mouseX, final int mouseY) {
    if (falloffConstraint == HoverFalloffConstraint.none) {
      falloffValue = 1.0f;
      return;
    }

    int centerX = element.getX() + element.getWidth() / 2;
    int centerY = element.getY() + element.getHeight() / 2;
    float dx = mouseX - centerX;
    float dy = mouseY - centerY;
    float falloff = 0.0f;

    if (falloffConstraint == HoverFalloffConstraint.vertical) {
      dx = 0.0f;
      falloff = getVerticalHover(element) / 2;
    }

    if (falloffConstraint == HoverFalloffConstraint.horizontal) {
      dy = 0.0f;
      falloff = getHorizontalHover(element) / 2;
    }

    if (falloffConstraint == HoverFalloffConstraint.both) {
      /* determine the angle from center of element to current mouse position.
         NOTE: if dy and dy are zero it is not possible to determine the angle.
         Assume an angle of zero degrees if dy AND dx are 0 */
      double dA = 0.0;
      if (dy == 0.0 && dx == 0.0) {
        dA = 0.0;
      } else {
        dA = Math.abs(Math.atan((dy / dx)));
      }
      // determine the angle from the center of the object to one of its corners to find the flip / cutoff angle
      float elA = (float) (getHorizontalHover(element) / 2);
      float elB = (float) (getVerticalHover(element) / 2);
      double dB = 0.0;
      dB = Math.abs(Math.atan((elB / elA)));
      if ((Math.abs(Math.toDegrees(dA)) >= 0.0) && (Math.abs(Math.toDegrees(dA)) <= Math.abs(Math.toDegrees(dB)))) {
        //use cos(dA) = adj / hyp: so hyp = adj / cos(dA)
        falloff = (float) (elA / Math.cos(dA));
      }
      if ((Math.abs(Math.toDegrees(dA)) > Math.abs(Math.toDegrees(dB))) && (Math.abs(Math.toDegrees(dA)) <= 90.0)) {
        //use sin(dA) = opp / hyp: so hyp = opp / sin(dA)
        falloff = (float) (elB / Math.sin(dA));
      }
    }

    float d = (float) Math.hypot(dx, dy);
    if (d > falloff) {
      falloffValue = 0.0f;
    }

    falloffValue = Math.abs(1.0f - d / falloff);
  }

  public float getFalloffValue() {
    return falloffValue;
  }

  public HoverFalloffConstraint getFalloffConstraint() {
    return falloffConstraint;
  }
}
