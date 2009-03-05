package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.resolver.parameter.ParameterResolver;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

public class StyleType extends XmlBaseType {
  private AttributesType attributesType;
  private EffectsType effectsType;
  private InteractType interactType;

  public void setAttributes(final AttributesType styleAttributesTypeParam) {
    attributesType = styleAttributesTypeParam;
  }

  public void setEffect(final EffectsType effectTypeParam) {
    effectsType = effectTypeParam;
  }

  public void setInteract(final InteractType interactTypeParam) {
    interactType = interactTypeParam;
  }

  public String output(final int offset) {
    String result = StringHelper.whitespace(offset) + "<style> (" + getAttributes().toString() + ")";
    if (attributesType != null) {
      result += "\n" + attributesType.output(offset + 1);
    }
    if (interactType != null) {
      result += "\n" + interactType.output(offset + 1);
    }
    if (effectsType != null) {
      result += "\n" + effectsType.output(offset + 1);
    }
    return result;
  }

  public String getStyleId() {
    return getAttributes().get("id");
  }

  public String getBaseStyleId() {
    return getAttributes().get("base");
  }

  /**
   * Apply the this style to the given result attributes using
   * the given nifty instance to resolve other styles.
   * @param styleResolver Nifty to resolve styles
   * @param result attributes to apply the style to
   */
  public void apply(
      final StyleResolver styleResolver,
      final Attributes result,
      final Nifty nifty,
      final Element element,
      final Screen screen,
      final ParameterResolver parameterResolver) {
    applyBaseStyle(styleResolver, result, nifty, element, screen, parameterResolver);
    applyStyle(result, nifty, element, screen, parameterResolver);
  }

  private void applyBaseStyle(
      final StyleResolver styleResolver,
      final Attributes result,
      final Nifty nifty,
      final Element element,
      final Screen screen,
      final ParameterResolver parameterResolver) {
    StyleType baseStyle = styleResolver.resolve(getBaseStyleId());
    if (baseStyle != null) {
      baseStyle.apply(styleResolver, result, nifty, element, screen, parameterResolver);
    }
  }

  private void applyStyle(
      final Attributes result,
      final Nifty nifty,
      final Element element,
      final Screen screen,
      final ParameterResolver parameterResolver) {
    if (attributesType != null) {
      attributesType.apply(result);
    }
    if (effectsType != null) {
      effectsType.materialize(nifty, element, screen, parameterResolver);
    }
  }
}
