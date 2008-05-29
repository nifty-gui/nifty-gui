package de.lessvoid.nifty.loader.xpp3.processor;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.PopupType;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterControlDefinitionType;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterEffectType;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;
import de.lessvoid.nifty.screen.NullScreenController;
import de.lessvoid.nifty.tools.TimeProvider;


/**
 * LayerType.
 * @author void
 */
public class PopupTypeProcessor implements XmlElementProcessor {

  /**
   * the logger.
   */
  private static Logger logger = Logger.getLogger(PopupTypeProcessor.class.getName());

  /**
   * registeredPopups.
   */
  private Map < String, PopupType > registeredPopups = new Hashtable < String, PopupType >();

  /**
   * LayerTypeProcessor.
   */
  public PopupTypeProcessor() {
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    PopupType popupType = new PopupType();
    ProcessorHelper.processElement(xmlParser, popupType, attributes);

    String name = attributes.get("id");
    registeredPopups.put(name, popupType);
    logger.info("registering popup [" + name + "]");
  }

  /**
   * register popups with nifty.
   * @param nifty nifty instance
   * @param registeredEffects registeredEffects
   * @param registeredControls registeredControls
   * @param styleHandler styleHandler
   * @param time time
   */
  public void registerPopups(
      final Nifty nifty,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final StyleHandler styleHandler,
      final TimeProvider time) {
    for (PopupType popup : registeredPopups.values()) {
      nifty.registerPopup(
          popup.createElement(
              nifty,
              null,
              registeredEffects,
              registeredControls,
              styleHandler,
              time,
              null,
              new NullScreenController()));
    }
  }
}
