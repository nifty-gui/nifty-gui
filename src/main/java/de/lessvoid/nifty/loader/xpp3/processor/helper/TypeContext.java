package de.lessvoid.nifty.loader.xpp3.processor.helper;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterControlDefinitionType;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterEffectType;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.tools.TimeProvider;

public class TypeContext {
  public StyleHandler styleHandler;
  public Nifty nifty;
  public Map < String, RegisterEffectType > registeredEffects;
  public Map < String, RegisterControlDefinitionType > registeredControls;
  public TimeProvider time;

  /**
   * @param styleHandler
   * @param nifty
   * @param registeredEffects
   * @param time
   */
  public TypeContext(
      final StyleHandler styleHandler,
      final Nifty nifty,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final TimeProvider time) {
    this.styleHandler = styleHandler;
    this.nifty = nifty;
    this.registeredEffects = registeredEffects;
    this.registeredControls = registeredControls;
    this.time = time;
  }
}
