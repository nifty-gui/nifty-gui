package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.Falloff;

public class EffectTypeOnHover extends EffectType {
  private HoverType hoverType = new HoverType();

  public EffectTypeOnHover(final EffectTypeOnHover src) {
    super(src);
    hoverType = new HoverType(src.hoverType);
  }

  public EffectTypeOnHover() {
  }

  public EffectTypeOnHover clone() {
    return new EffectTypeOnHover(this);
  }

  /**
   * This supports creating CustomControlCreator.
   * @return
   */
  public ControlEffectOnHoverAttributes convert() {
    return new ControlEffectOnHoverAttributes(getAttributes(), effectValues, hoverType);
  }

  public void setHover(final HoverType hoverTypeParam) {
    this.hoverType = hoverTypeParam;
  }

  public String output(final int offset) {
    String result = super.output(offset);
    if (hoverType != null) {
      result += "\n" + hoverType.output(offset + 1);
    }
    return result;
  }

  protected void initializeEffect(final Effect effect, final EffectEventId effectEventId) {
    Falloff falloff = new Falloff();
    if (hoverType != null) {
      falloff = hoverType.materialize();
    }
    effect.enableHover(falloff);
    if (!EffectEventId.onEndHover.equals(effectEventId)) {
      effect.enableInfinite();
    }
  }
}
