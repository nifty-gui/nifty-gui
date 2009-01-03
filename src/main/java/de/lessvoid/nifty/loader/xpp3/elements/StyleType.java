package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;
import de.lessvoid.nifty.screen.Screen;
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
   * EffectsType.
   * @optional
   */
  private EffectsType effects;

  private TypeContext typeContext;

  /**
   * create style type.
   * @param idParam id
   */
  public StyleType(final TypeContext typeContextParam, final String idParam) {
    typeContext = typeContextParam;
    id = idParam;
  }

  /**
   * create style type from some other style type.
   * @param idParam id
   * @param baseStyleParam base style
   */
  public StyleType(final TypeContext typeContextParam, final String idParam, final StyleType baseStyleParam) {
    typeContext = typeContextParam;
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
   * @param screen screen
   */
  public void applyStyle(
      final Element element,
      final Nifty nifty,
      final Map < String, RegisterEffectType > registeredEffects,
      final TimeProvider time,
      final Screen screen) {
    // apply base style if given
    if (baseStyle != null) {
      baseStyle.applyStyle(element, nifty, registeredEffects, time, screen);
    }
    // attributes
    if (attributes != null) {
      ElementType.applyAttributes(typeContext, attributes, screen, element, nifty.getRenderDevice());
    }
    // effects
    if (effects != null) {
      effects.initElement(element, nifty, registeredEffects, time);
    }
  }
}
