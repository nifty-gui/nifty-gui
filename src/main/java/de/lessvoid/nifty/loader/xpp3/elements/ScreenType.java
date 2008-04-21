package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.ClassHelper;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * HandleScreen.
 * @author void
 */
public class ScreenType implements XmlElementProcessor {

  /**
   * logger.
   */
  private static Logger log = Logger.getLogger(ScreenType.class.getName());

  /**
   * Nifty instance we're connected to.
   */
  private Nifty nifty;

  /**
   * Screens we should fill.
   */
  private Map < String, Screen > screens;

  /**
   * effects.
   */
  private Map < String, Class < ? > > registeredEffects;

  /**
   * HandleScreen.
   * @param niftyParam niftyParam
   * @param screensParam screensParam
   * @param registeredEffectsParam registeredEffectsParam
   */
  public ScreenType(
      final Nifty niftyParam,
      final Map < String, Screen > screensParam,
      final Map < String, Class < ? > > registeredEffectsParam) {
    nifty = niftyParam;
    screens = screensParam;
    this.registeredEffects = registeredEffectsParam;
  }

  /**
   * process.
   * @param parser parser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser parser, final Attributes attributes) throws Exception {
    String id = attributes.get("id");
    String controllerClass = attributes.get("controller");
    String effectGroup = attributes.get("effectGroup");

    log.info("processing screen [" + id + "]");
    Screen screen = createScreeen(id, controllerClass, parser);
    screens.put(id, screen);
  }

  /**
   * Create Screen.
   * @param id the id
   * @param controllerClass controller class name
   * @param parser XmlParser
   * @return Screen
   * @throws Exception exception
   */
  private Screen createScreeen(final String id, final String controllerClass, final XmlParser parser) throws Exception {
    // create the ScreenController.
    ScreenController screenController = ClassHelper.getScreenController(controllerClass);

    // create the screen.
    Screen screen = new Screen(id, screenController, new TimeProvider());
    screenController.bind(nifty, screen);

    // process layer groups
    parser.nextTag();
    parser.zeroOrMore("layerGroup", new LayerGroupType());
    parser.oneOrMore("layer", new LayerType(nifty, screen, registeredEffects, screenController));
    return screen;
  }
}
