package de.lessvoid.nifty.loader.xpp3.processor;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.NiftyLoader;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterControlDefinitionType;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterEffectType;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
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
  private RegisterEffectTypeProcessor registerEffectTypeProcessor = new RegisterEffectTypeProcessor();

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
   * screenTypeProcessor.
   */
  private ScreenTypeProcessor screenTypeProcessor;

  /**
   * popupTypeProcessor.
   */
  private PopupTypeProcessor popupTypeProcessor;

  /**
   * the style handler.
   */
  private StyleHandler styleHandler = new StyleHandler();

  /**
   * register styles processor.
   */
  private RegisterStyleProcessor registerStyleTypeProcessor;

  /**
   * use styles processor to load style files.
   */
  private UseStylesTypeProcessor useStylesTypeProcessor;

  /**
   * new nifty loader.
   * @param niftyLoader nifty loader to use
   */
  public NiftyTypeProcessor(final NiftyLoader niftyLoader) {
    useStylesTypeProcessor = new UseStylesTypeProcessor(niftyLoader, styleHandler);
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    registerSoundTypeProcessor = new RegisterSoundTypeProcessor();
    registerMusicTypeProcessor = new RegisterMusicTypeProcessor();
    registerControlDefinitionTypeProcessor = new RegisterControlDefinitionTypeProcessor();
    screenTypeProcessor = new ScreenTypeProcessor();
    popupTypeProcessor = new PopupTypeProcessor();

    xmlParser.nextTag();
    xmlParser.zeroOrMore("useStyles", useStylesTypeProcessor);
    xmlParser.zeroOrMore("registerEffect", registerEffectTypeProcessor);
    xmlParser.zeroOrMore("registerSound", registerSoundTypeProcessor);
    xmlParser.zeroOrMore("registerMusic", registerMusicTypeProcessor);
    xmlParser.zeroOrMore("style", new RegisterStyleProcessor(styleHandler));
    xmlParser.zeroOrMore("controlDefinition", registerControlDefinitionTypeProcessor);
    xmlParser.oneOrMore("screen", screenTypeProcessor);
    xmlParser.zeroOrMore("popup", popupTypeProcessor);
  }

  /**
   * create objects.
   * @param nifty nifty
   * @param screens screens screens
   * @param time time
   */
  public void create(
      final Nifty nifty,
      final Map < String, Screen > screens,
      final TimeProvider time) {
    registerSoundTypeProcessor.register(nifty.getSoundSystem());
    registerMusicTypeProcessor.register(nifty.getSoundSystem());
    screenTypeProcessor.create(
        nifty,
        screens,
        time,
        registerEffectTypeProcessor.getRegisterEffects(),
        registerControlDefinitionTypeProcessor.getRegisteredControls(),
        styleHandler);
    popupTypeProcessor.registerPopups(
        nifty,
        registerEffectTypeProcessor.getRegisterEffects(),
        registerControlDefinitionTypeProcessor.getRegisteredControls(),
        styleHandler,
        time);
  }

  /**
   * get registered effects.
   * @return map with all registered effects
   */
  public Map < String, RegisterEffectType > getRegisteredEffects() {
    return registerEffectTypeProcessor.getRegisterEffects();
  }

  /**
   * get registered controls.
   * @return map with all registered controls
   */
  public Map < String, RegisterControlDefinitionType > getRegisteredControls() {
    return registerControlDefinitionTypeProcessor.getRegisteredControls();
  }

  /**
   * get the StyleHandler.
   * @return the style handler
   */
  public StyleHandler getStyleHandler() {
    return styleHandler;
  }
}
