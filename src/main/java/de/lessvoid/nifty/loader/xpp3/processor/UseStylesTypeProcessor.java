package de.lessvoid.nifty.loader.xpp3.processor;


import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.NiftyLoader;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.StyleType;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;

/**
 * UseStylesProcessor.
 * @author void
 */
public class UseStylesTypeProcessor implements XmlElementProcessor {
  /**
   * nifty loader.
   */
  private NiftyLoader niftyLoader;

  /**
   * style handler.
   */
  private StyleHandler styleHandler;

  /**
   * create the processor.
   * @param newNiftyLoader nifty loader
   * @param newStyleHandler the StyleHandler
   */
  public UseStylesTypeProcessor(final NiftyLoader newNiftyLoader, final StyleHandler newStyleHandler) {
    niftyLoader = newNiftyLoader;
    styleHandler = newStyleHandler;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes Attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    if (attributes.isSet("filename")) {
      niftyLoader.loadNiftyStyles(attributes.get("filename"));
    }
    xmlParser.nextTag();
  }
}
