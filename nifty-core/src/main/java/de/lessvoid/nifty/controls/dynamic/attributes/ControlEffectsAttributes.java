package de.lessvoid.nifty.controls.dynamic.attributes;

import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.loaderv2.types.EffectsType;
import de.lessvoid.nifty.tools.EnumStorage;
import de.lessvoid.nifty.tools.factories.CollectionFactory;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import java.util.Collection;

public class ControlEffectsAttributes {
  @Nonnull
  private final Attributes attributes;

  @Nonnull
  private final EnumStorage<EffectEventId, Collection<ControlEffectAttributes>> effectAttributes;

  public ControlEffectsAttributes() {
    attributes = new Attributes();
    effectAttributes = new EnumStorage<EffectEventId, Collection<ControlEffectAttributes>>(
        EffectEventId.class, CollectionFactory.<ControlEffectAttributes>getArrayListInstance());
  }

  /**
   * Support for CustomControlCreator
   */
  public ControlEffectsAttributes(@Nonnull final EffectsType source) {
    attributes = new Attributes(source.getAttributes());
    effectAttributes = new EnumStorage<EffectEventId, Collection<ControlEffectAttributes>>(
        EffectEventId.class, CollectionFactory.<ControlEffectAttributes>getArrayListInstance());

    for (EffectEventId id : EffectEventId.values()) {
      if (source.hasEffectTypes(id)) {
        source.convertCopy(id, effectAttributes.get(id));
      }
    }
  }

  @Nonnull
  public Attributes getAttributes() {
    return attributes;
  }

  public void setAttribute(@Nonnull final String name, @Nonnull final String value) {
    attributes.set(name, value);
  }

  public void setOverlay(@Nonnull final String overlay) {
    attributes.set("overlay", overlay);
  }

  public void addEffectAttribute(@Nonnull final EffectEventId id, @Nonnull final ControlEffectAttributes attribute) {
    effectAttributes.get(id).add(attribute);
  }

  public void addOnStartScreen(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onStartScreen, effectParam);
  }

  public void addOnEndScreen(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onEndScreen, effectParam);
  }

  public void addOnHover(@Nonnull final ControlEffectOnHoverAttributes effectParam) {
    addEffectAttribute(EffectEventId.onHover, effectParam);
  }

  public void addOnStartHover(@Nonnull final ControlEffectOnHoverAttributes effectParam) {
    addEffectAttribute(EffectEventId.onStartHover, effectParam);
  }

  public void addOnEndHover(@Nonnull final ControlEffectOnHoverAttributes effectParam) {
    addEffectAttribute(EffectEventId.onEndHover, effectParam);
  }

  public void addOnClick(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onClick, effectParam);
  }

  public void addOnFocus(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onFocus, effectParam);
  }

  public void addOnLostFocus(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onLostFocus, effectParam);
  }

  public void addOnGetFocus(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onGetFocus, effectParam);
  }

  public void addOnActive(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onActive, effectParam);
  }

  public void addOnShow(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onShow, effectParam);
  }

  public void addOnHide(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onHide, effectParam);
  }

  public void addOnCustom(@Nonnull final ControlEffectAttributes effectParam) {
    addEffectAttribute(EffectEventId.onCustom, effectParam);
  }

  @Nonnull
  public EffectsType create() {
    EffectsType effectsType = new EffectsType(attributes);

    for (EffectEventId id : EffectEventId.values()) {
      if (effectAttributes.isSet(id)) {
        for (ControlEffectAttributes effectParam : effectAttributes.get(id)) {
          effectsType.addEventEffect(id, effectParam.create());
        }
      }
    }
    return effectsType;
  }

  public void refreshEffectsType(@Nonnull final EffectsType effectsType) {
    effectsType.getAttributes().refreshFromAttributes(getAttributes());
  }
}
