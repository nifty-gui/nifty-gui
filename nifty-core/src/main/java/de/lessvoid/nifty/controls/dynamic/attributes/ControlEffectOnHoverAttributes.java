package de.lessvoid.nifty.controls.dynamic.attributes;

import java.util.ArrayList;
import java.util.Collections;

import de.lessvoid.nifty.loaderv2.types.EffectTypeOnHover;
import de.lessvoid.nifty.loaderv2.types.EffectValueType;
import de.lessvoid.nifty.loaderv2.types.HoverType;
import de.lessvoid.xml.xpp3.Attributes;

public class ControlEffectOnHoverAttributes extends ControlEffectAttributes {
  private ControlHoverAttributes controlHoverAttributes;

  public ControlEffectOnHoverAttributes() {
  }

  public ControlEffectOnHoverAttributes(final Attributes attributes, final ArrayList<EffectValueType> effectValues, final HoverType hoverType) {
    this.attributes = new Attributes(attributes);
    this.effectValues = new ArrayList<EffectValueType>(effectValues);
    Collections.copy(this.effectValues, effectValues);
    this.controlHoverAttributes = new ControlHoverAttributes(hoverType);
  }

  public void setControlHoverAttributes(final ControlHoverAttributes controlHoverAttributesParam) {
    controlHoverAttributes = controlHoverAttributesParam;
  }

  public EffectTypeOnHover create() {
    EffectTypeOnHover effectTypeOnHover = new EffectTypeOnHover();
    effectTypeOnHover.initFromAttributes(attributes);
    for (int i=0; i<effectValues.size(); i++) {
      effectTypeOnHover.addValue(effectValues.get(i));
    }
    if (controlHoverAttributes != null) {
      effectTypeOnHover.setHover(controlHoverAttributes.create());
    }
    return effectTypeOnHover;
  }
}
