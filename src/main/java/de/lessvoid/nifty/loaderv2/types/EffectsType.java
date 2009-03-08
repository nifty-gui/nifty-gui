package de.lessvoid.nifty.loaderv2.types;

import java.util.ArrayList;
import java.util.Collection;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.resolver.parameter.ParameterResolver;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * EffectsType.
 * @author void
 */
public class EffectsType extends XmlBaseType {
  private Collection < EffectType > onStartScreen = new ArrayList < EffectType >();
  private Collection < EffectType > onEndScreen = new ArrayList < EffectType >();
  private Collection < EffectType > onHover = new ArrayList < EffectType >();
  private Collection < EffectType > onClick = new ArrayList < EffectType >();
  private Collection < EffectType > onFocus = new ArrayList < EffectType >();
  private Collection < EffectType > onLostFocus = new ArrayList < EffectType >();
  private Collection < EffectType > onGetFocus = new ArrayList < EffectType >();
  private Collection < EffectType > onActive = new ArrayList < EffectType >();
  private Collection < EffectType > onCustom = new ArrayList < EffectType >();
  private Collection < EffectType > onShow = new ArrayList < EffectType >();
  private Collection < EffectType > onHide = new ArrayList < EffectType >();

  public void addOnStartScreen(final EffectType effectParam) {
    onStartScreen.add(effectParam);
  }

  public void addOnEndScreen(final EffectType effectParam) {
    onEndScreen.add(effectParam);
  }

  public void addOnHover(final EffectTypeOnHover effectParam) {
    onHover.add(effectParam);
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

  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<effects> (" + getAttributes().toString() + ")"
      + getCollectionString("onStartScreen", onStartScreen, offset + 1)
      + getCollectionString("onEndScreen", onEndScreen, offset + 1)
      + getCollectionString("onHover", onHover, offset + 1)
      + getCollectionString("onClick", onClick, offset + 1)
      + getCollectionString("onFocus", onFocus, offset + 1)
      + getCollectionString("onLostFocus", onLostFocus, offset + 1)
      + getCollectionString("onGetFocus", onGetFocus, offset + 1)
      + getCollectionString("onActive", onActive, offset + 1)
      + getCollectionString("onCustom", onCustom, offset + 1)
      + getCollectionString("onShow", onShow, offset + 1)
      + getCollectionString("onHide", onHide, offset + 1);
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
    }
    return result;
  }

  public void materialize(
      final Nifty nifty,
      final Element element,
      final Screen screen,
      final ParameterResolver parameterResolver) {
    Attributes attributes = parameterResolver.resolve(getAttributes());
    initEffect(EffectEventId.onStartScreen, onStartScreen, element, nifty, screen, attributes, parameterResolver);
    initEffect(EffectEventId.onEndScreen, onEndScreen, element, nifty, screen, attributes, parameterResolver);
    initEffect(EffectEventId.onHover, onHover, element, nifty, screen, attributes, parameterResolver);
    initEffect(EffectEventId.onClick, onClick, element, nifty, screen, attributes, parameterResolver);
    initEffect(EffectEventId.onFocus, onFocus, element, nifty, screen, attributes, parameterResolver);
    initEffect(EffectEventId.onLostFocus, onLostFocus, element, nifty, screen, attributes, parameterResolver);
    initEffect(EffectEventId.onGetFocus, onGetFocus, element, nifty, screen, attributes, parameterResolver);
    initEffect(EffectEventId.onActive, onActive, element, nifty, screen, attributes, parameterResolver);
    initEffect(EffectEventId.onCustom, onCustom, element, nifty, screen, attributes, parameterResolver);
    initEffect(EffectEventId.onShow, onShow, element, nifty, screen, attributes, parameterResolver);
    initEffect(EffectEventId.onHide, onHide, element, nifty, screen, attributes, parameterResolver);
  }

  private void initEffect(
      final EffectEventId eventId,
      final Collection < EffectType > effectCollection,
      final Element element,
      final Nifty nifty,
      final Screen screen,
      final Attributes effectsTypeAttributes,
      final ParameterResolver parameterResolver) {
    for (EffectType effectType : effectCollection) {
      effectType.materialize(
          nifty,
          screen.getScreenController(),
          element,
          eventId,
          effectsTypeAttributes,
          parameterResolver);
    }
  }

  public void refreshFromAttributes(final ControlEffectsAttributes effects) {
    effects.refreshEffectsType(this);

//    refreshEffect(EffectEventId.onStartScreen, onStartScreen);
//    refreshEffect(EffectEventId.onEndScreen, onEndScreen);
//    refreshEffect(EffectEventId.onHover, onHover);
//    refreshEffect(EffectEventId.onClick, onClick);
//    refreshEffect(EffectEventId.onFocus, onFocus);
//    refreshEffect(EffectEventId.onLostFocus, onLostFocus);
//    refreshEffect(EffectEventId.onGetFocus, onGetFocus);
//    refreshEffect(EffectEventId.onActive, onActive);
//    refreshEffect(EffectEventId.onCustom, onCustom);
//    refreshEffect(EffectEventId.onShow, onShow);
//    refreshEffect(EffectEventId.onHide, onHide);
  }

//  private void refreshEffect(final EffectEventId effectEventId, final Collection < EffectType > effectCollection) {
//    for (EffectType effectType : effectCollection) {
//      effectType.r
//    }
//  }
}
