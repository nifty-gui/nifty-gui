package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * EffectsType.
 * @author void
 */
public class EffectsType {

  /**
   * onStartScreen.
   */
  private Collection < EffectType > onStartScreen = new ArrayList < EffectType >();

  /**
   * onEndScreen.
   */
  private Collection < EffectType > onEndScreen = new ArrayList < EffectType >();

  /**
   * onHover.
   */
  private Collection < EffectType > onHover = new ArrayList < EffectType >();

  /**
   * onClick.
   */
  private Collection < EffectType > onClick = new ArrayList < EffectType >();

  /**
   * onFocus.
   */
  private Collection < EffectType > onFocus = new ArrayList < EffectType >();

  /**
   * onActive.
   */
  private Collection < EffectType > onActive = new ArrayList < EffectType >();

  /**
   * default contructor.
   */
  public EffectsType() {
  }

  /**
   * copy constructor.
   * @param source source
   */
  public EffectsType(final EffectsType source) {
    this.onStartScreen = copyCollection(source.onStartScreen);
    this.onEndScreen = copyCollection(source.onEndScreen);
    this.onHover = copyCollection(source.onHover);
    this.onClick = copyCollection(source.onClick);
    this.onFocus = copyCollection(source.onFocus);
    this.onActive = copyCollection(source.onActive);
  }

  /**
   * copy collection.
   * @param source source
   * @return new collection that is a copy
   */
  private Collection < EffectType > copyCollection(final Collection < EffectType > source) {
    Collection < EffectType > copy = new ArrayList < EffectType >();
    for (EffectType effect : source) {
      copy.add(new EffectType(effect));
    }
    return copy;
  }

  /**
   * addOnStartScreen.
   * @param effectParam onStartScreen
   */
  public void addOnStartScreen(final EffectType effectParam) {
    onStartScreen.add(effectParam);
  }

  /**
   * addOnEndScreen.
   * @param effectParam onEndScreen
   */
  public void addOnEndScreen(final EffectType effectParam) {
    onEndScreen.add(effectParam);
  }

  /**
   * addOnHover.
   * @param effectParam onHover
   */
  public void addOnHover(final EffectType effectParam) {
    onHover.add(effectParam);
  }

  /**
   * addOnClick.
   * @param effectParam onClick
   */
  public void addOnClick(final EffectType effectParam) {
    onClick.add(effectParam);
  }

  /**
   * addOnFocus.
   * @param effectParam onFocus
   */
  public void addOnFocus(final EffectType effectParam) {
    onFocus.add(effectParam);
  }

  /**
   * addOnActive.
   * @param effectParam onActive
   */
  public void addOnActive(final EffectType effectParam) {
    onActive.add(effectParam);
  }

  /**
   * bind to element.
   * @param element element
   * @param nifty nifty
   * @param registerEffects effect map
   * @param time time
   */
  public void initElement(
      final Element element,
      final Nifty nifty,
      final Map < String, RegisterEffectType > registerEffects,
      final TimeProvider time) {
    initEffect(EffectEventId.onStartScreen, onStartScreen, element, registerEffects, nifty, time);
    initEffect(EffectEventId.onEndScreen, onEndScreen, element, registerEffects, nifty, time);
    initEffect(EffectEventId.onHover, onHover, element, registerEffects, nifty, time);
    initEffect(EffectEventId.onClick, onClick, element, registerEffects, nifty, time);
    initEffect(EffectEventId.onFocus, onFocus, element, registerEffects, nifty, time);
    initEffect(EffectEventId.onActive, onActive, element, registerEffects, nifty, time);
  }

  /**
   * init effect.
   * @param eventId eventId
   * @param effectCollection effectCollection
   * @param element element
   * @param registeredEffects registeredEffects
   * @param nifty nifty
   * @param time time
   */
  private void initEffect(
      final EffectEventId eventId,
      final Collection < EffectType > effectCollection,
      final Element element,
      final Map < String, RegisterEffectType > registeredEffects,
      final Nifty nifty,
      final TimeProvider time) {
    for (EffectType effectType : effectCollection) {
      effectType.create(element, nifty, eventId, registeredEffects, time);
    }
  }
}
