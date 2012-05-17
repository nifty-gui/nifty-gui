package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

public class StyleType extends XmlBaseType {
  private AttributesType attributesType;
  private EffectsType effectsType;
  private InteractType interactType;

  public StyleType() {
  }

  public StyleType(final Attributes attributes) {
    super(attributes);
  }

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

  public void applyTo(final ElementType elementType, final StyleResolver styleResolver) {
    applyToBaseStyleInternal(styleResolver, elementType);
    applyToInternal(elementType);
  }

  void applyToInternal(final ElementType elementType) {
    elementType.removeWithTag(getStyleId());
    if (attributesType != null) {
      attributesType.apply(elementType.getAttributes(), getStyleId());
    }
    if (effectsType != null) {
      effectsType.apply(elementType.getEffects(), getStyleId());
    }
    if (interactType != null) {
      interactType.apply(elementType.getInteract(), getStyleId());
    }
  }

  void applyToBaseStyleInternal(final StyleResolver styleResolver, final ElementType elementType) {
    StyleType baseStyle = styleResolver.resolve(getBaseStyleId());
    if (baseStyle != null) {
      baseStyle.applyTo(elementType, styleResolver);
    }
  }
}
