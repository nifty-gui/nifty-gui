package de.lessvoid.nifty.loader.xpp3;

import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

import org.xmlpull.mxp1.MXParser;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterControlDefinitionType;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterEffectType;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.loader.xpp3.processor.NiftyControlsTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.NiftyStylesTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.NiftyTypeProcessor;
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
    logBlockBegin("processing");

    // create processors
    niftyTypeProcessor = new NiftyTypeProcessor(nifty, this, timeProvider);
    niftyStylesTypeProcessor = new NiftyStylesTypeProcessor(niftyTypeProcessor.getStyleHandler());
    niftyControlsTypeProcessor =
      new NiftyControlsTypeProcessor(niftyTypeProcessor.getRegisterControlDefinitionTypeProcessor());

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
    parser.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(filename));

    // start parsing
    parser.nextTag();
    parser.required("nifty-styles", niftyStylesTypeProcessor);
    logBlockEnd();
  }

  /**
   * loadNiftyControls.
   * @param filename filename
   */
  public void loadNiftyControls(final String filename) throws Exception {
    logBlockBegin("loadNiftyControls: " + filename);

    // create parser
    XmlParser parser = new XmlParser(new MXParser());
    parser.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(filename));

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
    log.info("========================================================");
    log.info(msg);
    log.info("========================================================");
  }

  /**
   * log some nice header thing into the log.
   */
  private void logBlockEnd() {
    log.info("--------------------------------------------------------");
  }
}
