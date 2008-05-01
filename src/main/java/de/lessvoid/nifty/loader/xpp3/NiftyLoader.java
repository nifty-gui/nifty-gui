package de.lessvoid.nifty.loader.xpp3;

import java.util.Map;
import java.util.logging.Logger;

import org.xmlpull.mxp1.MXParser;

import de.lessvoid.nifty.Nifty;
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
   * load xml.
   * @param nifty nifty
   * @param screens screens
   * @param filename filename
   * @param timeProvider timeProvider
   * @throws Exception exception
   */
  public void loadXml(
      final Nifty nifty,
      final Map < String, Screen > screens,
      final String filename,
      final TimeProvider timeProvider) throws Exception {

    log.info("loadXml: " + filename);

    // create parser
    XmlParser parser = new XmlParser(new MXParser());
    parser.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(filename));

    // start parsing
    NiftyTypeProcessor niftyTypeProcessor = new NiftyTypeProcessor();
    parser.nextTag();
    parser.required("nifty", niftyTypeProcessor);

    // create actual nifty objects
    niftyTypeProcessor.create(nifty, screens, timeProvider);
  }
}
