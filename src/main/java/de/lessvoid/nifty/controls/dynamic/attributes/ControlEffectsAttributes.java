package de.lessvoid.nifty.controls.dynamic.attributes;

import de.lessvoid.nifty.loaderv2.types.EffectsType;
import de.lessvoid.xml.xpp3.Attributes;

public class ControlEffectsAttributes {
  private EffectsType effectsType = new EffectsType();
  private Attributes attributes = new Attributes();

  public void setAttribute(final String name, final String value) {
    attributes.set(name, value);
  }

  public void setOverlay(final String overlay) {
    attributes.set("overlay", overlay);
  }

  public void addOnStartScreen(final ControlEffectAttributes effectParam) {
    effectsType.addOnStartScreen(effectParam.create());
  }

  public void addOnEndScreen(final ControlEffectAttributes effectParam) {
    effectsType.addOnEndScreen(effectParam.create());
  }

  public void addOnHover(final ControlEffectOnHoverAttributes effectParam) {
    effectsType.addOnHover(effectParam.create());
  }

  public void addOnClick(final ControlEffectAttributes effectParam) {
    effectsType.addOnClick(effectParam.create());
  }

  public void addOnFocus(final ControlEffectAttributes effectParam) {
    effectsType.addOnFocus(effectParam.create());
  }

  public void addOnLostFocus(final ControlEffectAttributes effectParam) {
    effectsType.addOnLostFocus(effectParam.create());
  }

  public void addOnGetFocus(final ControlEffectAttributes effectParam) {
    effectsType.addOnGetFocus(effectParam.create());
  }

  public void addOnActive(final ControlEffectAttributes effectParam) {
    effectsType.addOnActive(effectParam.create());
  }

  public void addOnShow(final ControlEffectAttributes effectParam) {
    effectsType.addOnShow(effectParam.create());
  }

  public void addOnHide(final ControlEffectAttributes effectParam) {
    effectsType.addOnHide(effectParam.create());
  }

  public void addOnCustom(final ControlEffectAttributes effectParam) {
    effectsType.addOnCustom(effectParam.create());
  }

  public EffectsType create() {
    effectsType.initFromAttributes(attributes);
    return effectsType;
  }
}
