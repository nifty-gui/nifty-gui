package de.lessvoid.nifty.loader.xpp3.processor;

import java.util.Hashtable;
import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterControlDefinitionType;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterEffectType;
import de.lessvoid.nifty.loader.xpp3.elements.ScreenType;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * HandleScreen.
 * @author void
 */
public class ScreenTypeProcessor implements XmlElementProcessor {

  /**
   * screens.
   */
  private Map < String, ScreenType > screens = new Hashtable < String, ScreenType >();

  /**
   * type context.
   */
  private TypeContext typeContext;

  /**
   * @param newTypeContext the TypeContext to use
   */
  public ScreenTypeProcessor(final TypeContext newTypeContext) {
    this.typeContext = newTypeContext;
  }

  /**
   * process.
   * @param parser parser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser parser, final Attributes attributes) throws Exception {
    String id = attributes.get("id");
    ScreenType screen = new ScreenType(id, attributes.get("controller"));
    if (attributes.isSet("effectGroup")) {
      screen.setEffectGroup(attributes.get("effectGroup"));
    }

    parser.nextTag();
    parser.zeroOrMore("layerGroup", new ScreenTypeLayerGroupTypeProcessor(screen));
    parser.oneOrMore("layer", new LayerTypeProcessor(typeContext, screen));

    screens.put(id, screen);
  }

  /**
   * actual create the objects.
   * @param nifty nifty
   * @param screenParam screen map to init
   * @param time time
   * @param registeredEffects effects
   * @param registeredControls registeredControls
   * @param styleHandler styleHandler
   */
  public void create(
      final Nifty nifty,
      final Map < String, Screen > screenParam,
      final TimeProvider time,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final StyleHandler styleHandler) {
    for (ScreenType screenType : screens.values()) {
      Screen screen = screenType.createScreen(
          nifty,
          time,
          registeredEffects,
          registeredControls,
          styleHandler);
      screenParam.put(screen.getScreenId(), screen);
    }
  }
}
