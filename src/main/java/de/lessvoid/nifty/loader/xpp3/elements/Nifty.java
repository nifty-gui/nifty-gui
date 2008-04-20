package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Hashtable;
import java.util.Map;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.sound.SoundSystem;

/**
 * NiftyTag.
 * @author void
 */
public class Nifty implements XmlElementProcessor {

  /**
   * All registered effects.
   */
  private Map < String, Class < ? >> registerEffects = new Hashtable < String, Class < ? > >();

  /**
   * All screens.
   */
  private Map < String, Screen > screens;

  /**
   * SoundSystem.
   */
  private SoundSystem soundSystem;

  /**
   * nifty.
   */
  private de.lessvoid.nifty.Nifty nifty;

  /**
   * Create.
   * @param niftyParam nifty
   * @param screensParam screens
   */
  public Nifty(final de.lessvoid.nifty.Nifty niftyParam, final Map < String, Screen > screensParam) {
    nifty = niftyParam;
    soundSystem = nifty.getSoundSystem();
    screens = screensParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    xmlParser.nextTag();
    xmlParser.zeroOrMore("registerEffect", new RegisterEffect(registerEffects));
    xmlParser.zeroOrMore("registerSound", new RegisterSound(soundSystem));
    xmlParser.zeroOrMore("registerMusic", new RegisterMusic(soundSystem));
    xmlParser.zeroOrMore("controlDefinition", new RegisterControlDefinition());
    xmlParser.zeroOrMore("effectGroup", new RegisterEffectGroup());
    xmlParser.zeroOrMore("layerGroup", new RegisterLayoutGroup());
    xmlParser.oneOrMore("screen", new ScreenType(nifty, screens, registerEffects));
  }
}
