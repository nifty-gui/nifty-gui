package de.lessvoid.nifty.loader.xpp3.processor;

import java.util.Hashtable;
import java.util.Map;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterEffectType;

/**
 * RegisterEffectTypeProcessor.
 * @author void
 */
public class RegisterEffectTypeProcessor implements XmlElementProcessor {

  /**
   * registered effects.
   */
  private Map < String, RegisterEffectType > registeredEffects = new Hashtable < String, RegisterEffectType >();

  /**
   * process.
   * @param xmlParser parser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    String name = attributes.get("name");
    String clazz = attributes.get("class");
    registeredEffects.put(name, new RegisterEffectType(clazz));
    xmlParser.nextTag();
  }

  /**
   * Get a RegisterEffectType instance.
   * @return registered effects
   */
  public Map < String, RegisterEffectType > getRegisterEffects() {
    return registeredEffects;
  }
}
