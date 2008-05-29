package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Properties;

import de.lessvoid.nifty.effects.shared.Falloff;
import de.lessvoid.nifty.elements.Element;

/**
 * HoverType.
 * @author void
 */
public class HoverType {

  /**
   * width.
   */
  private String width;

  /**
   * height.
   */
  private String height;

  /**
   * falloffType.
   */
  private HoverFalloffType falloffType;

  /**
   * falloffConstraint.
   */
  private HoverFalloffConstraintType falloffConstraint;

  /**
   * setWidth.
   * @param widthParam width
   */
  public void setWidth(final String widthParam) {
    this.width = widthParam;
  }

  /**
   * setHeight.
   * @param heightParam height
   */
  public void setHeight(final String heightParam) {
    this.height = heightParam;
  }

  /**
   * setFalloffType.
   * @param falloffTypeParam falloffType
   */
  public void setFalloffType(final HoverFalloffType falloffTypeParam) {
    this.falloffType = falloffTypeParam;
  }

  /**
   * setFalloffConstraint.
   * @param falloffConstraintParam falloffConstraint
   */
  public void setFalloffConstraint(final HoverFalloffConstraintType falloffConstraintParam) {
    this.falloffConstraint = falloffConstraintParam;
  }

  /**
   * init element.
   * @param element element
   */
  public void initElement(final Element element) {
    Properties prop = new Properties();
    if (width != null) {
      prop.put(Falloff.HOVER_WIDTH, width);
    }
    if (height != null) {
      prop.put(Falloff.HOVER_HEIGHT, height);
    }
    if (falloffType != null) {
      prop.put(Falloff.HOVER_FALLOFF_TYPE, falloffType.toString());
    }
    if (falloffConstraint != null) {
      prop.put(Falloff.HOVER_FALLOFF_CONSTRAINT, falloffConstraint.toString());
    }
    if (!prop.isEmpty()) {
      Falloff falloff = element.getFalloff();
      if (falloff == null) {
        falloff = new Falloff(prop);
        element.setHotSpotFalloff(falloff);
      } else {
        falloff.applyProperties(prop);
      }
    }
  }
}
