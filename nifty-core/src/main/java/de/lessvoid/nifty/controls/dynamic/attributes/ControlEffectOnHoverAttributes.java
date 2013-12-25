package de.lessvoid.nifty.controls.dynamic.attributes;

import de.lessvoid.nifty.loaderv2.types.EffectTypeOnHover;
import de.lessvoid.nifty.loaderv2.types.EffectValueType;
import de.lessvoid.nifty.loaderv2.types.HoverType;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlEffectOnHoverAttributes extends ControlEffectAttributes {
  private ControlHoverAttributes controlHoverAttributes;

  public ControlEffectOnHoverAttributes() {
  }

  public ControlEffectOnHoverAttributes(
      @Nonnull final Attributes attributes,
      @Nonnull final List<EffectValueType> effectValues,
      @Nonnull final HoverType hoverType) {
    this.attributes = new Attributes(attributes);
    this.effectValues = new ArrayList<EffectValueType>(effectValues);
    Collections.copy(this.effectValues, effectValues);
    this.controlHoverAttributes = new ControlHoverAttributes(hoverType);
  }

  public void setControlHoverAttributes(final ControlHoverAttributes controlHoverAttributesParam) {
    controlHoverAttributes = controlHoverAttributesParam;
  }

  @Override
  @Nonnull
  public EffectTypeOnHover create() {
    final EffectTypeOnHover effectTypeOnHover;
    if (controlHoverAttributes == null) {
      effectTypeOnHover = new EffectTypeOnHover(attributes);
    } else {
      effectTypeOnHover = new EffectTypeOnHover(attributes, controlHoverAttributes);
    }
    effectTypeOnHover.addValues(effectValues);

    return effectTypeOnHover;
  }
}
