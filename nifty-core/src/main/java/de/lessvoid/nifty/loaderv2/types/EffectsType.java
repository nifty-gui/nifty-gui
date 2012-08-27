package de.lessvoid.nifty.loaderv2.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * EffectsType.
 * @author void
 */
public class EffectsType extends XmlBaseType {
  protected Collection < EffectType > onStartScreen = new ArrayList < EffectType >();
  protected Collection < EffectType > onEndScreen = new ArrayList < EffectType >();
  protected Collection < EffectType > onHover = new ArrayList < EffectType >();
  protected Collection < EffectType > onStartHover = new ArrayList < EffectType >();
  protected Collection < EffectType > onEndHover = new ArrayList < EffectType >();
  protected Collection < EffectType > onClick = new ArrayList < EffectType >();
  protected Collection < EffectType > onFocus = new ArrayList < EffectType >();
  protected Collection < EffectType > onLostFocus = new ArrayList < EffectType >();
  protected Collection < EffectType > onGetFocus = new ArrayList < EffectType >();
  protected Collection < EffectType > onActive = new ArrayList < EffectType >();
  protected Collection < EffectType > onCustom = new ArrayList < EffectType >();
  protected Collection < EffectType > onShow = new ArrayList < EffectType >();
  protected Collection < EffectType > onHide = new ArrayList < EffectType >();
  protected Collection < EffectType > onEnabled = new ArrayList < EffectType >();
  protected Collection < EffectType > onDisabled = new ArrayList < EffectType >();

  public EffectsType() {
  }

  public EffectsType(final EffectsType src) {
    super(src);
    copyEffects(src);
  }

  public void mergeFromEffectsType(final EffectsType src) {
    mergeFromAttributes(src.getAttributes());
    mergeEffects(src);
  }

  void copyEffects(final EffectsType src) {
    copyCollection(onStartScreen, src.onStartScreen);
    copyCollection(onEndScreen, src.onEndScreen);
    copyCollection(onHover, src.onHover);
    copyCollection(onStartHover, src.onStartHover);
    copyCollection(onEndHover, src.onEndHover);
    copyCollection(onClick, src.onClick);
    copyCollection(onFocus, src.onFocus);
    copyCollection(onLostFocus, src.onLostFocus);
    copyCollection(onGetFocus, src.onGetFocus);
    copyCollection(onActive, src.onActive);
    copyCollection(onCustom, src.onCustom);
    copyCollection(onShow, src.onShow);
    copyCollection(onHide, src.onHide);
    copyCollection(onEnabled, src.onEnabled);
    copyCollection(onDisabled, src.onDisabled);
  }

  void mergeEffects(final EffectsType src) {
    mergeCollection(onStartScreen, src.onStartScreen);
    mergeCollection(onEndScreen, src.onEndScreen);
    mergeCollection(onHover, src.onHover);
    mergeCollection(onStartHover, src.onStartHover);
    mergeCollection(onEndHover, src.onEndHover);
    mergeCollection(onClick, src.onClick);
    mergeCollection(onFocus, src.onFocus);
    mergeCollection(onLostFocus, src.onLostFocus);
    mergeCollection(onGetFocus, src.onGetFocus);
    mergeCollection(onActive, src.onActive);
    mergeCollection(onCustom, src.onCustom);
    mergeCollection(onShow, src.onShow);
    mergeCollection(onHide, src.onHide);
    mergeCollection(onEnabled, src.onEnabled);
    mergeCollection(onDisabled, src.onDisabled);
  }

  Collection < EffectType > copyCollection(
      final Collection < EffectType > dst,
      final Collection < EffectType > src) {
    dst.clear();
    copyEffects(dst, src);
    return dst;
  }

  Collection < EffectType > mergeCollection(
      final Collection < EffectType > dst,
      final Collection < EffectType > src) {
    copyEffects(dst, src);
    return dst;
  }

  void copyEffects(
      final Collection < EffectType > dst,
      final Collection < EffectType > src) {
    for (EffectType e : src) {
      dst.add(e.clone());
    }
  }

  @SuppressWarnings("unchecked")
  public void translateSpecialValues(final Nifty nifty, final Screen screen) {
    super.translateSpecialValues(nifty, screen);
    
    for (Collection<EffectType> col : new Collection[]{
        onStartScreen, onEndScreen, onHover, onStartHover, onEndHover, onClick, onFocus,
        onLostFocus, onGetFocus, onActive, onCustom, onShow, onHide, onEnabled, onDisabled}) {
      for (EffectType e : col) {
        e.translateSpecialValues(nifty, screen);
      }
    }
  }

  public void addOnStartScreen(final EffectType effectParam) {
    onStartScreen.add(effectParam);
  }

  public void addOnEndScreen(final EffectType effectParam) {
    onEndScreen.add(effectParam);
  }

  public void addOnHover(final EffectTypeOnHover effectParam) {
    onHover.add(effectParam);
  }

  public void addOnStartHover(final EffectType effectParam) {
    onStartHover.add(effectParam);
  }

  public void addOnEndHover(final EffectType effectParam) {
    onEndHover.add(effectParam);
  }

  public void addOnClick(final EffectType effectParam) {
    onClick.add(effectParam);
  }

  public void addOnFocus(final EffectType effectParam) {
    onFocus.add(effectParam);
  }

  public void addOnLostFocus(final EffectType effectParam) {
    onLostFocus.add(effectParam);
  }

  public void addOnGetFocus(final EffectType effectParam) {
    onGetFocus.add(effectParam);
  }

  public void addOnActive(final EffectType effectParam) {
    onActive.add(effectParam);
  }

  public void addOnShow(final EffectType effectParam) {
    onShow.add(effectParam);
  }

  public void addOnHide(final EffectType effectParam) {
    onHide.add(effectParam);
  }

  public void addOnCustom(final EffectType effectParam) {
    onCustom.add(effectParam);
  }

  public void addOnDisabled(final EffectType effectParam) {
    onDisabled.add(effectParam);
  }

  public void addOnEnabled(final EffectType effectParam) {
    onEnabled.add(effectParam);
  }

  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<effects> (" + getAttributes().toString() + ")"
      + getCollectionString("onStartScreen", onStartScreen, offset + 1)
      + getCollectionString("onEndScreen", onEndScreen, offset + 1)
      + getCollectionString("onHover", onHover, offset + 1)
      + getCollectionString("onStartHover", onStartHover, offset + 1)
      + getCollectionString("onEndHover", onEndHover, offset + 1)
      + getCollectionString("onClick", onClick, offset + 1)
      + getCollectionString("onFocus", onFocus, offset + 1)
      + getCollectionString("onLostFocus", onLostFocus, offset + 1)
      + getCollectionString("onGetFocus", onGetFocus, offset + 1)
      + getCollectionString("onActive", onActive, offset + 1)
      + getCollectionString("onCustom", onCustom, offset + 1)
      + getCollectionString("onShow", onShow, offset + 1)
      + getCollectionString("onHide", onHide, offset + 1)
      + getCollectionString("onEnabled", onEnabled, offset + 1)
      + getCollectionString("onDisabled", onDisabled, offset + 1);
  }

  private String getCollectionString(
      final String name,
      final Collection < EffectType > effectCollection,
      final int offset) {
    if (effectCollection.isEmpty()) {
      return "";
    }
    String result = "";
    for (EffectType effect : effectCollection) {
      result += "\n" + StringHelper.whitespace(offset) + "<" + name + "> " + effect.output(offset);
      if (effect.getStyleId() != null) {
        result += " {" + effect.getStyleId() + "}";
      }
    }
    return result;
  }

  public void materialize(
      final Nifty nifty,
      final Element element,
      final Screen screen,
      final LinkedList < Object > controllers) {
    Attributes attributes = getAttributes();
    initEffect(EffectEventId.onStartScreen, onStartScreen, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onEndScreen, onEndScreen, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onHover, onHover, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onStartHover, onStartHover, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onEndHover, onEndHover, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onClick, onClick, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onFocus, onFocus, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onLostFocus, onLostFocus, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onGetFocus, onGetFocus, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onActive, onActive, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onCustom, onCustom, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onShow, onShow, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onHide, onHide, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onEnabled, onEnabled, element, nifty, screen, attributes, controllers);
    initEffect(EffectEventId.onDisabled, onDisabled, element, nifty, screen, attributes, controllers);
  }

  private void initEffect(
      final EffectEventId eventId,
      final Collection < EffectType > effectCollection,
      final Element element,
      final Nifty nifty,
      final Screen screen,
      final Attributes effectsTypeAttributes,
      final LinkedList < Object > controllers) {
    for (EffectType effectType : effectCollection) {
      effectType.materialize(
          nifty,
          element,
          eventId,
          effectsTypeAttributes,
          controllers);
    }
  }

  public void refreshFromAttributes(final ControlEffectsAttributes effects) {
    effects.refreshEffectsType(this);
  }

  public void apply(final EffectsType effects, final String styleId) {
    applyEffectCollection(onStartScreen, effects.onStartScreen, styleId);
    applyEffectCollection(onEndScreen, effects.onEndScreen, styleId);
    applyEffectCollection(onHover, effects.onHover, styleId);
    applyEffectCollection(onStartHover, effects.onStartHover, styleId);
    applyEffectCollection(onEndHover, effects.onEndHover, styleId);
    applyEffectCollection(onClick, effects.onClick, styleId);
    applyEffectCollection(onFocus, effects.onFocus, styleId);
    applyEffectCollection(onLostFocus, effects.onLostFocus, styleId);
    applyEffectCollection(onGetFocus, effects.onGetFocus, styleId);
    applyEffectCollection(onActive, effects.onActive, styleId);
    applyEffectCollection(onCustom, effects.onCustom, styleId);
    applyEffectCollection(onShow, effects.onShow, styleId);
    applyEffectCollection(onHide, effects.onHide, styleId);
    applyEffectCollection(onEnabled, effects.onEnabled, styleId);
    applyEffectCollection(onDisabled, effects.onDisabled, styleId);
  }

  void applyEffectCollection(final Collection < EffectType > src, final Collection < EffectType > dst, final String styleId) {
    for (EffectType effectType : src) {
      EffectType copy = effectType.clone();
      copy.setStyleId(styleId);
      dst.add(copy);
    }
  }

  public void resolveParameters(final Attributes src) {
    resolveParameterCollection(onStartScreen, src);
    resolveParameterCollection(onEndScreen, src);
    resolveParameterCollection(onHover, src);
    resolveParameterCollection(onStartHover, src);
    resolveParameterCollection(onEndHover, src);
    resolveParameterCollection(onClick, src);
    resolveParameterCollection(onFocus, src);
    resolveParameterCollection(onLostFocus, src);
    resolveParameterCollection(onGetFocus, src);
    resolveParameterCollection(onActive, src);
    resolveParameterCollection(onCustom, src);
    resolveParameterCollection(onShow, src);
    resolveParameterCollection(onHide, src);
    resolveParameterCollection(onEnabled, src);
    resolveParameterCollection(onDisabled, src);
  }

  void resolveParameterCollection(
      final Collection < EffectType > dst,
      final Attributes src) {
    for (EffectType e : dst) {
      e.resolveParameters(src);
    }
  }

  public void removeWithTag(final String styleId) {
    getAttributes().removeWithTag(styleId);
    removeAllEffectsWithStyleId(onStartScreen, styleId);
    removeAllEffectsWithStyleId(onEndScreen, styleId);
    removeAllEffectsWithStyleId(onHover, styleId);
    removeAllEffectsWithStyleId(onStartHover, styleId);
    removeAllEffectsWithStyleId(onEndHover, styleId);
    removeAllEffectsWithStyleId(onClick, styleId);
    removeAllEffectsWithStyleId(onFocus, styleId);
    removeAllEffectsWithStyleId(onLostFocus, styleId);
    removeAllEffectsWithStyleId(onGetFocus, styleId);
    removeAllEffectsWithStyleId(onActive, styleId);
    removeAllEffectsWithStyleId(onCustom, styleId);
    removeAllEffectsWithStyleId(onShow, styleId);
    removeAllEffectsWithStyleId(onHide, styleId);
    removeAllEffectsWithStyleId(onEnabled, styleId);
    removeAllEffectsWithStyleId(onDisabled, styleId);
  }

  private void removeAllEffectsWithStyleId(final Collection < EffectType > source, final String styleId) {
    Iterator < EffectType > iter = source.iterator();
    while (iter.hasNext()) {
      EffectType current = iter.next();
      if (styleId.equals(current.getStyleId())) {
        iter.remove();
      }
    }
  }

  Collection<ControlEffectAttributes> convertCopy(final Collection < EffectType > src) {
    Collection<ControlEffectAttributes> result = new ArrayList<ControlEffectAttributes>();
    for (EffectType e : src) {
      result.add(e.convert());
    }
    return result;
  }

  Collection<ControlEffectOnHoverAttributes> convertCopyHover(final Collection < EffectType > src) {
    Collection<ControlEffectOnHoverAttributes> result = new ArrayList<ControlEffectOnHoverAttributes>();
    for (EffectType e : src) {
      result.add(((EffectTypeOnHover) e).convert());
    }
    return result;
  }

  public Collection<ControlEffectAttributes> getOnStartScreen() {
    return convertCopy(onStartScreen);
  }

  public Collection<ControlEffectAttributes> getOnEndScreen() {
    return convertCopy(onEndScreen);
  }

  public Collection<ControlEffectOnHoverAttributes> getOnHover() {
    return convertCopyHover(onHover);
  }

  public Collection<ControlEffectOnHoverAttributes> getOnStartHover() {
    return convertCopyHover(onStartHover);
  }

  public Collection<ControlEffectOnHoverAttributes> getOnEndHover() {
    return convertCopyHover(onEndHover);
  }

  public Collection<ControlEffectAttributes> getOnClick() {
    return convertCopy(onClick);
  }

  public Collection<ControlEffectAttributes> getOnFocus() {
    return convertCopy(onFocus);
  }

  public Collection<ControlEffectAttributes> getLostFocus() {
    return convertCopy(onLostFocus);
  }

  public Collection<ControlEffectAttributes> getOnGetFocus() {
    return convertCopy(onGetFocus);
  }

  public Collection<ControlEffectAttributes> getOnActive() {
    return convertCopy(onActive);
  }

  public Collection<ControlEffectAttributes> getOnCustom() {
    return convertCopy(onCustom);
  }

  public Collection<ControlEffectAttributes> getOnShow() {
    return convertCopy(onShow);
  }

  public Collection<ControlEffectAttributes> getOnHide() {
    return convertCopy(onHide);
  }
}
