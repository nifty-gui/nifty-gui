package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlHoverAttributes;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;

public class EffectTypeOnHover extends EffectType {
  @Nonnull
  private HoverType hoverType;

  public EffectTypeOnHover() {
    hoverType = new HoverType();
  }

  public EffectTypeOnHover(@Nonnull final ControlHoverAttributes hoverAttributes) {
    hoverType = hoverAttributes.create();
  }

  public EffectTypeOnHover(@Nonnull final Attributes attributes) {
    super(attributes);
    hoverType = new HoverType();
  }

  @Nonnull
  @Override
  public EffectTypeOnHover clone() throws CloneNotSupportedException {
    try {
      final EffectTypeOnHover newObject = (EffectTypeOnHover) super.clone();
      newObject.hoverType = new HoverType(hoverType);
      return newObject;
    } catch (ClassCastException e) {
      throw new CloneNotSupportedException("Cloning failed because the clone method created the wrong object.");
    }
  }

  public EffectTypeOnHover(
      @Nonnull final Attributes attributes,
      @Nonnull final ControlHoverAttributes hoverAttributes) {
    super(attributes);
    hoverType = hoverAttributes.create();
  }

  /**
   * This supports creating CustomControlCreator.
   *
   * @return
   */
  @Override
  @Nonnull
  public ControlEffectOnHoverAttributes convert() {
    return new ControlEffectOnHoverAttributes(getAttributes(), effectValues, hoverType);
  }

  @Override
  public String output(final int offset) {
    String result = super.output(offset);
    result += "\n" + hoverType.output(offset + 1);
    return result;
  }

  @Override
  protected void initializeEffect(@Nonnull final Effect effect, final EffectEventId effectEventId) {
    Falloff falloff = hoverType.materialize();
    effect.enableHover(falloff);
    if (!EffectEventId.onEndHover.equals(effectEventId)) {
      effect.enableInfinite();
    }
  }
}
