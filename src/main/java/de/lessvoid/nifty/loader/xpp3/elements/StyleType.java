package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderEngine;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * StyleType.
 * @author void
 */
public class StyleType {

  /**
   * the id.
   */
  private String id;

  /**
   * style this style is based upon.
   */
  private StyleType baseStyle;

  /**
   * attributes.
   */
  private AttributesType attributes;

  /**
   * hover.
   * @optional
   */
  private HoverType hover;

  /**
   * EffectsType.
   * @optional
   */
  private EffectsType effects;

  /**
   * create style type.
   * @param idParam id
   */
  public StyleType(final String idParam) {
    id = idParam;
  }

  /**
   * create style type from some other style type.
   * @param idParam id
   * @param baseStyleParam base style
   */
  public StyleType(final String idParam, final StyleType baseStyleParam) {
    id = idParam;
    baseStyle = baseStyleParam;
  }

  /**
   * get id.
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * set new attributes or merge with existing.
   * @param attributesParam attributes
   */
  public void setAttributes(final AttributesType attributesParam) {
    attributes = attributesParam;
  }

  /**
   * Set Hover.
   * @param hoverTypeParam HoverType
   */
  public void setHover(final HoverType hoverTypeParam) {
    hover = hoverTypeParam;
  }

  /**
   * Set Effects.
   * @param effectsTypeParam EffectsType
   */
  public void setEffects(final EffectsType effectsTypeParam) {
    effects = effectsTypeParam;
  }

  /**
   * Apply Style to the element.
   * @param element Element to apply Style to
   * @param nifty nifty
   * @param registeredEffects registered effects
   * @param time time
   */
  public void applyStyle(
      final Element element,
      final Nifty nifty,
      final Map < String, RegisterEffectType > registeredEffects,
      final TimeProvider time) {
    // apply base style if given
    if (baseStyle != null) {
      baseStyle.applyStyle(element, nifty, registeredEffects, time);
    }

    // attributes
    if (attributes != null) {
      ElementType.applyAttributes(attributes, element, nifty.getRenderDevice());
    }

    // hover
    if (hover != null) {
      hover.initElement(element);
    }

    // effects
    if (effects != null) {
      effects.initElement(element, nifty, registeredEffects, time);
    }
  }
}
