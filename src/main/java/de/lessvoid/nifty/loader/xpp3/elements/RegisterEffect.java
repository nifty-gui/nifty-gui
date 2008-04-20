package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.ClassHelper;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;

/**
 * RegisterEffect.
 * @author void
 */
public class RegisterEffect implements XmlElementProcessor {

  /**
   * logger.
   */
  private Logger log = Logger.getLogger(RegisterEffect.class.getName());

  /**
   * All registered effects.
   */
  private Map < String, Class < ? >> registerEffects = new Hashtable < String, Class < ? > >();

  /**
   * @param registerEffectsParam registerEffectsParam
   */
  public RegisterEffect(final Map < String, Class < ? > > registerEffectsParam) {
    this.registerEffects = registerEffectsParam;
  }

  /**
   * process.
   * @param xmlParser parser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    String effectName = attributes.get("name");
    String className = attributes.get("class");
    Class < ? > cl = ClassHelper.loadClass(className);
    if (cl != null) {
      log.info("register effect [" + effectName + "]->[" + className + "]");
      registerEffects.put(effectName, cl);
    }
    xmlParser.nextTag();
  }
}
