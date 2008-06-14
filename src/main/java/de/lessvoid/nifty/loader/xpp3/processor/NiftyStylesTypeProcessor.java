package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;

/**
 * NiftyStyleTypeProcessor.
 * @author void
 */
public class NiftyStylesTypeProcessor implements XmlElementProcessor {

  /**
   * the style handler.
   */
  private StyleHandler styleHandler;

  /**
   * create the NiftyStyleTypeProcessor.
   * @param newStyleHandler connect to this StyleHandler instance
   */
  public NiftyStylesTypeProcessor(final StyleHandler newStyleHandler) {
    styleHandler = newStyleHandler;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    RegisterStyleProcessor registerStyleTypeProcessor = new RegisterStyleProcessor(styleHandler);

    xmlParser.nextTag();
    xmlParser.zeroOrMore("style", registerStyleTypeProcessor);
  }
}
