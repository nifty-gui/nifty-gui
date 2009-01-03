package de.lessvoid.nifty.loader.xpp3;

import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

import org.newdawn.slick.util.ResourceLoader;
import org.xmlpull.mxp1.MXParser;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyDefaults;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterControlDefinitionType;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterEffectType;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.loader.xpp3.processor.NiftyControlsTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.NiftyStylesTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.NiftyTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.PopupTypeProcessor;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * Loader.
 * @author void
 */
public class NiftyLoader {

  /**
   * logger.
   */
  private Logger log = Logger.getLogger(NiftyLoader.class.getName());

  /**
   * nifty type processor.
   */
  private NiftyTypeProcessor niftyTypeProcessor;

  /**
   * nifty style type processor.
   */
  private NiftyStylesTypeProcessor niftyStylesTypeProcessor;

  /**
   * nifty controls type processor.
   */
  private NiftyControlsTypeProcessor niftyControlsTypeProcessor;

  /**
   * load xml.
   * @param inputStream TODO
   * @param nifty nifty
   * @param screens screens
   * @param timeProvider timeProvider
   * @throws Exception exception
   */
  public void loadXml(
      final InputStream inputStream,
      final Nifty nifty,
      final Map < String, Screen > screens,
      final TimeProvider timeProvider) throws Exception {
    log.info("loading new xml file...");

    // create processors
    niftyTypeProcessor = new NiftyTypeProcessor(nifty, this, timeProvider);
    niftyStylesTypeProcessor = new NiftyStylesTypeProcessor(nifty.createTypeContext(), niftyTypeProcessor.getStyleHandler(), this);
    niftyControlsTypeProcessor = new NiftyControlsTypeProcessor(
          niftyTypeProcessor.getRegisterControlDefinitionTypeProcessor(),
          niftyTypeProcessor.getUseControlsTypeProcessor()
          );

    // initialize defaults
    NiftyDefaults.initDefaultEffects(niftyTypeProcessor.getRegisteredEffects());

    // create parser
    XmlParser parser = new XmlParser(new MXParser());
    parser.read(inputStream);

    // start parsing
    parser.nextTag();
    parser.required("nifty", niftyTypeProcessor);

    // create actual nifty objects
    niftyTypeProcessor.create(nifty, screens, timeProvider);
    logBlockEnd();
  }

  /**
   * Load a Nifty style xml.
   * @param filename filename to load
   * @throws Exception exception in case of any errors
   */
  public void loadNiftyStyles(final String filename) throws Exception {
    logBlockBegin("loadNiftyStyles: " + filename);

    // create parser
    XmlParser parser = new XmlParser(new MXParser());
    parser.read(ResourceLoader.getResourceAsStream(filename));

    // start parsing
    parser.nextTag();
    parser.required("nifty-styles", niftyStylesTypeProcessor);
    logBlockEnd();
  }

  /**
   * loadNiftyControls.
   * @param filename filename
   * @param popupTypeProcessor 
   * @throws Exception exception
   */
  public void loadNiftyControls(final String filename, final PopupTypeProcessor popupTypeProcessor) throws Exception {
    logBlockBegin("loadNiftyControls: " + filename);

    // create parser
    XmlParser parser = new XmlParser(new MXParser());
    parser.read(ResourceLoader.getResourceAsStream(filename));

    niftyControlsTypeProcessor.setPopupTypeProcessor(popupTypeProcessor);

    // start parsing
    parser.nextTag();
    parser.required("nifty-controls", niftyControlsTypeProcessor);
    logBlockEnd();
  }

  /**
   * get registered effects.
   * @return registered effects map
   */
  public Map < String, RegisterEffectType > getRegisteredEffects() {
    return niftyTypeProcessor.getRegisteredEffects();
  }

  /**
   * get registered controls.
   * @return registered control map
   */
  public Map < String, RegisterControlDefinitionType > getRegisteredControls() {
    return niftyTypeProcessor.getRegisteredControls();
  }

  /**
   * get style handler.
   * @return style handler
   */
  public StyleHandler getStyleHandler() {
    return niftyTypeProcessor.getStyleHandler();
  }

  /**
   * log some nice header thing into the log.
   * @param msg message to log
   */
  private void logBlockBegin(final String msg) {
    log.fine("========================================================");
    log.info(msg);
    log.fine("========================================================");
  }

  /**
   * log some nice header thing into the log.
   */
  private void logBlockEnd() {
    log.fine("--------------------------------------------------------");
  }
}
