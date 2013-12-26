package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StyleType extends XmlBaseType {
  @Nullable
  private AttributesType attributesType;
  @Nullable
  private EffectsType effectsType;
  @Nullable
  private InteractType interactType;

  public StyleType() {
  }

  public StyleType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  public void setAttributes(@Nonnull final AttributesType styleAttributesTypeParam) {
    attributesType = styleAttributesTypeParam;
  }

  public void setEffect(@Nonnull final EffectsType effectTypeParam) {
    effectsType = effectTypeParam;
  }

  public void setInteract(@Nonnull final InteractType interactTypeParam) {
    interactType = interactTypeParam;
  }

  @Override
  @Nonnull
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

  @Nullable
  public String getStyleId() {
    return getAttributes().get("id");
  }

  @Nullable
  public String getBaseStyleId() {
    return getAttributes().get("base");
  }

  public void applyTo(@Nonnull final ElementType elementType, @Nonnull final StyleResolver styleResolver) {
    applyToBaseStyleInternal(styleResolver, elementType);
    applyToInternal(elementType);
  }

  void applyToInternal(@Nonnull final ElementType elementType) {
    String styleId = getStyleId();
    if (styleId == null) {
      return;
    }
    elementType.removeWithTag(styleId);
    if (attributesType != null) {
      attributesType.apply(elementType.getAttributes(), styleId);
    }
    if (effectsType != null) {
      effectsType.apply(elementType.getEffects(), styleId);
    }
    if (interactType != null) {
      interactType.apply(elementType.getInteract(), styleId);
    }
  }

  void applyToBaseStyleInternal(@Nonnull final StyleResolver styleResolver, @Nonnull final ElementType elementType) {
    StyleType baseStyle = styleResolver.resolve(getBaseStyleId());
    if (baseStyle != null) {
      baseStyle.applyTo(elementType, styleResolver);
    }
  }
}
