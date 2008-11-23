package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.NiftyLoader;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;

/**
 * NiftyStyleTypeProcessor.
 * @author void
 */
public class NiftyStylesTypeProcessor implements XmlElementProcessor {

  private StyleHandler styleHandler;
  private UseStylesTypeProcessor useStylesTypeProcessor;

  public NiftyStylesTypeProcessor(final StyleHandler newStyleHandler, final NiftyLoader niftyLoader) {
    styleHandler = newStyleHandler;
    useStylesTypeProcessor = new UseStylesTypeProcessor(niftyLoader);
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
    xmlParser.zeroOrMore("useStyles", useStylesTypeProcessor);
    xmlParser.zeroOrMore("style", registerStyleTypeProcessor);
  }
}
