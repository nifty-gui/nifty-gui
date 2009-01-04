package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * EffectsType.
 * @author void
 */
public class EffectsType {
  private static Logger log = Logger.getLogger(EffectsType.class.getName());

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
  private boolean overlay;

  /**
   * default contructor.
   * @param b 
   */
  public EffectsType(final boolean overlayParam) {
    overlay = overlayParam;
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
    this.onGetFocus = copyCollection(source.onGetFocus);
    this.onLostFocus = copyCollection(source.onLostFocus);
    this.onActive = copyCollection(source.onActive);
    this.onCustom = copyCollection(source.onCustom);
    this.onShow = copyCollection(source.onShow);
    this.onHide = copyCollection(source.onHide);
    this.overlay = source.overlay;
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

  public void addOnLostFocus(final EffectType effectParam) {
    onLostFocus.add(effectParam);
  }

  public void addOnGetFocus(final EffectType effectParam) {
    onGetFocus.add(effectParam);
  }

  /**
   * addOnActive.
   * @param effectParam onActive
   */
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
    initEffect(EffectEventId.onGetFocus, onGetFocus, element, registerEffects, nifty, time);
    initEffect(EffectEventId.onLostFocus, onLostFocus, element, registerEffects, nifty, time);
    initEffect(EffectEventId.onActive, onActive, element, registerEffects, nifty, time);
    initEffect(EffectEventId.onShow, onShow, element, registerEffects, nifty, time);
    initEffect(EffectEventId.onHide, onHide, element, registerEffects, nifty, time);
    initEffect(EffectEventId.onCustom, onCustom, element, registerEffects, nifty, time);
  }

  /**
   * initialize effect.
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
    for (EffectType effectTypeSource : effectCollection) {
      EffectType effectType = new EffectType(effectTypeSource);
      for (Entry < String, String > entry : effectType.getAny().getParameterAttributes().entrySet()) {
        String value = element.getElementType().getAttributes().getSrcAttributes().get(entry.getValue());
        if (value == null) {
          value = "'" + entry.getValue() + "' missing o_O";
        }
        log.fine(" initEffect for [" + element.getId() + "] setting [" + entry.getKey() + "] to [" + value + "]");
        Attributes attributes = effectType.getAny();
        attributes.overwriteAttribute(entry.getKey(), value);
        effectType.initFromAttributes(attributes);
      }

      effectType.create(element, nifty, eventId, registeredEffects, time, overlay);
    }
  }
}
