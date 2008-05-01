package de.lessvoid.nifty.loader.xpp3.processor;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * NiftyType.
 * @author void
 */
public class NiftyTypeProcessor implements XmlElementProcessor {

  /**
   * registerEffectTypeProcessor.
   */
  private RegisterEffectTypeProcessor registerEffectTypeProcessor;

  /**
   * registerSoundTypeProcessor.
   */
  private RegisterSoundTypeProcessor registerSoundTypeProcessor;

  /**
   * registerMusicTypeProcessor.
   */
  private RegisterMusicTypeProcessor registerMusicTypeProcessor;

  /**
   * registerControlDefinitionTypeProcessor.
   */
  private RegisterControlDefinitionTypeProcessor registerControlDefinitionTypeProcessor;

  /**
   * registerEffectGroupProcessor.
   */
  private RegisterEffectGroupProcessor registerEffectGroupProcessor;

  /**
   * registerLayoutGroupProcessor.
   */
  private RegisterLayoutGroupProcessor registerLayoutGroupProcessor;

  /**
   * screenTypeProcessor.
   */
  private ScreenTypeProcessor screenTypeProcessor;

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    registerEffectTypeProcessor = new RegisterEffectTypeProcessor();
    registerSoundTypeProcessor = new RegisterSoundTypeProcessor();
    registerMusicTypeProcessor = new RegisterMusicTypeProcessor();
    registerControlDefinitionTypeProcessor = new RegisterControlDefinitionTypeProcessor();
    registerEffectGroupProcessor = new RegisterEffectGroupProcessor();
    registerLayoutGroupProcessor = new RegisterLayoutGroupProcessor();
    screenTypeProcessor = new ScreenTypeProcessor();

    xmlParser.nextTag();
    xmlParser.zeroOrMore("registerEffect", registerEffectTypeProcessor);
    xmlParser.zeroOrMore("registerSound", registerSoundTypeProcessor);
    xmlParser.zeroOrMore("registerMusic", registerMusicTypeProcessor);
    xmlParser.zeroOrMore("controlDefinition", registerControlDefinitionTypeProcessor);
    xmlParser.zeroOrMore("effectGroup", registerEffectGroupProcessor);
    xmlParser.zeroOrMore("layerGroup", registerLayoutGroupProcessor);
    xmlParser.oneOrMore("screen", screenTypeProcessor);
  }

  /**
   * create objects.
   * @param nifty nifty
   * @param screens screens screens
   * @param time time
   */
  public void create(final Nifty nifty, final Map < String, Screen > screens, final TimeProvider time) {
    registerSoundTypeProcessor.register(nifty.getSoundSystem());
    registerMusicTypeProcessor.register(nifty.getSoundSystem());
    screenTypeProcessor.create(
        nifty,
        screens,
        time,
        registerEffectTypeProcessor.getRegisterEffects(),
        registerControlDefinitionTypeProcessor.getRegisteredControls());
  }
}
