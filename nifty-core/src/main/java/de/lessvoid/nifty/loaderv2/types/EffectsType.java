package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.EnumStorage;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.nifty.tools.factories.CollectionFactory;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * EffectsType.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class EffectsType extends XmlBaseType {
  @Nonnull
  private final EnumStorage<EffectEventId, Collection<EffectType>> effects;

  public EffectsType() {
    effects = new EnumStorage<EffectEventId, Collection<EffectType>>(
        EffectEventId.class, CollectionFactory.<EffectType>getArrayListInstance());
  }

  public EffectsType(@Nonnull final EffectsType src) {
    super(src);
    effects = new EnumStorage<EffectEventId, Collection<EffectType>>(
        EffectEventId.class, CollectionFactory.<EffectType>getArrayListInstance());
    copyEffects(src);
  }

  public EffectsType(@Nonnull Attributes attributes) {
    super(attributes);
    effects = new EnumStorage<EffectEventId, Collection<EffectType>>(
        EffectEventId.class, CollectionFactory.<EffectType>getArrayListInstance());
  }

  public void mergeFromEffectsType(@Nonnull final EffectsType src) {
    mergeFromAttributes(src.getAttributes());
    mergeEffects(src);
  }

  private void copyEffects(@Nonnull final EffectsType src) {
    for (final EffectEventId event : EffectEventId.values()) {
      if (src.effects.isSet(event)) {
        copyCollection(effects.get(event), src.effects.get(event));
      }
    }
  }

  private void mergeEffects(@Nonnull final EffectsType src) {
    for (final EffectEventId event : EffectEventId.values()) {
      if (src.effects.isSet(event)) {
        mergeCollection(effects.get(event), src.effects.get(event));
      }
    }
  }

  @Nonnull
  private Collection<EffectType> copyCollection(
      @Nonnull final Collection<EffectType> dst,
      @Nonnull final Collection<EffectType> src) {
    dst.clear();
    copyEffects(dst, src);
    return dst;
  }

  @Nonnull
  private Collection<EffectType> mergeCollection(
      @Nonnull final Collection<EffectType> dst,
      @Nonnull final Collection<EffectType> src) {
    copyEffects(dst, src);
    return dst;
  }

  void copyEffects(@Nonnull final Collection<EffectType> dst, @Nonnull final Collection<EffectType> src) {
    try {
      for (EffectType e : src) {
        dst.add(e.clone());
      }
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException("Creating copy of effect data failed!", e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void translateSpecialValues(@Nonnull final Nifty nifty, @Nullable final Screen screen) {
    super.translateSpecialValues(nifty, screen);

    for (final EffectEventId event : EffectEventId.values()) {
      if (effects.isSet(event)) {
        for (EffectType effectType : effects.get(event)) {
          effectType.translateSpecialValues(nifty, screen);
        }
      }
    }
  }

  public void addEventEffect(@Nonnull final EffectEventId eventId, @Nonnull final EffectType effectParam) {
    effects.get(eventId).add(effectParam);
  }

  public void addOnStartScreen(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onStartScreen, effectParam);
  }

  public void addOnEndScreen(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onEndScreen, effectParam);
  }

  public void addOnHover(@Nonnull final EffectTypeOnHover effectParam) {
    addEventEffect(EffectEventId.onHover, effectParam);
  }

  public void addOnStartHover(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onStartHover, effectParam);
  }

  public void addOnEndHover(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onEndHover, effectParam);
  }

  public void addOnClick(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onClick, effectParam);
  }

  public void addOnFocus(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onFocus, effectParam);
  }

  public void addOnLostFocus(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onLostFocus, effectParam);
  }

  public void addOnGetFocus(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onGetFocus, effectParam);
  }

  public void addOnActive(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onActive, effectParam);
  }

  public void addOnShow(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onShow, effectParam);
  }

  public void addOnHide(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onHide, effectParam);
  }

  public void addOnCustom(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onCustom, effectParam);
  }

  public void addOnDisabled(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onDisabled, effectParam);
  }

  public void addOnEnabled(@Nonnull final EffectType effectParam) {
    addEventEffect(EffectEventId.onEnabled, effectParam);
  }

  public Collection<EffectType> getEventEffectTypes(@Nonnull final EffectEventId id) {
    if (effects.isSet(id)) {
      return Collections.unmodifiableCollection(effects.get(id));
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  @Nonnull
  public String output(final int offset) {
    final StringBuilder builder = new StringBuilder();
    builder.append(StringHelper.whitespace(offset));
    builder.append("<effects> (").append(getAttributes().toString()).append(')');
    for (EffectEventId id : EffectEventId.values()) {
      builder.append(getCollectionString(id, offset + 1));
    }
    return builder.toString();
  }

  @Nonnull
  private String getCollectionString(@Nonnull final EffectEventId effectId, final int offset) {
    if (!effects.isSet(effectId)) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (EffectType effect : effects.get(effectId)) {
      if (builder.length() > 0) {
        builder.append('\n');
      }
      builder.append(StringHelper.whitespace(offset)).append('<').append(effectId.name()).append("> ");
      builder.append(effect.output(offset));
      if (effect.getStyleId() != null) {
        builder.append(" [").append(effect.getStyleId()).append(']');
      }
    }
    return builder.toString();
  }

  public void materialize(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final Screen screen,
      @Nonnull final List<Object> controllers) {
    for (EffectEventId id : EffectEventId.values()) {
      initEffect(id, element, nifty, controllers);
    }
  }

  private void initEffect(
      @Nonnull final EffectEventId eventId,
      @Nonnull final Element element,
      @Nonnull final Nifty nifty,
      @Nonnull final List<Object> controllers) {
    final Collection<EffectType> effectCollection = effects.get(eventId);
    final Attributes effectsTypeAttributes = getAttributes();

    for (EffectType effectType : effectCollection) {
      effectType.materialize(nifty, element, eventId, effectsTypeAttributes, controllers);
    }
  }

  public void refreshFromAttributes(@Nonnull final ControlEffectsAttributes effects) {
    effects.refreshEffectsType(this);
  }

  public void apply(@Nonnull final EffectsType dstEffectType, @Nullable final String styleId) {
    for (EffectEventId id : EffectEventId.values()) {
      if (effects.isSet(id)) {
        applyEffectCollection(effects.get(id), dstEffectType.effects.get(id), styleId);
      }
    }
  }

  void applyEffectCollection(
      @Nonnull final Collection<EffectType> src,
      @Nonnull final Collection<EffectType> dst,
      @Nullable final String styleId) {
    try {
      for (EffectType effectType : src) {
        EffectType copy = effectType.clone();
        copy.setStyleId(styleId);
        dst.add(copy);
      }
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException("Failure to apply a effect collection!", e);
    }
  }

  public void resolveParameters(@Nonnull final Attributes src) {
    for (EffectEventId id : EffectEventId.values()) {
      if (effects.isSet(id)) {
        resolveParameterCollection(effects.get(id), src);
      }
    }
  }

  void resolveParameterCollection(@Nonnull final Collection<EffectType> dst, @Nonnull final Attributes src) {
    for (EffectType e : dst) {
      e.resolveParameters(src);
    }
  }

  public void removeWithTag(@Nonnull final String styleId) {
    getAttributes().removeWithTag(styleId);
    for (EffectEventId id : EffectEventId.values()) {
      if (effects.isSet(id)) {
        removeAllEffectsWithStyleId(effects.get(id), styleId);
      }
    }
  }

  private void removeAllEffectsWithStyleId(
      @Nonnull final Collection<EffectType> source,
      @Nonnull final String styleId) {
    Iterator<EffectType> itr = source.iterator();
    while (itr.hasNext()) {
      EffectType current = itr.next();
      if (styleId.equals(current.getStyleId())) {
        itr.remove();
      }
    }
  }

  public boolean hasEffectTypes(@Nonnull final EffectEventId id) {
    return effects.isSet(id);
  }

  @SuppressWarnings("unchecked")
  public <T extends ControlEffectAttributes> void convertCopy(
      @Nonnull final EffectEventId effectId,
      @Nonnull final Collection<T> storage) {
    if (effects.isSet(effectId)) {
      for (EffectType e : effects.get(effectId)) {
        storage.add((T) e.convert());
      }
    }
  }
}
