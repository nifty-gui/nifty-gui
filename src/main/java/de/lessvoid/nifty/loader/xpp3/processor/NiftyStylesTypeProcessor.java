package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.NiftyLoader;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;

/**
 * NiftyStyleTypeProcessor.
 * @author void
 */
public class NiftyStylesTypeProcessor implements XmlElementProcessor {

  private StyleHandler styleHandler;
  private UseStylesTypeProcessor useStylesTypeProcessor;
  private TypeContext typeContext;

  public NiftyStylesTypeProcessor(final TypeContext typeContextParam, final StyleHandler newStyleHandler, final NiftyLoader niftyLoader) {
    styleHandler = newStyleHandler;
    useStylesTypeProcessor = new UseStylesTypeProcessor(niftyLoader);
    typeContext = typeContextParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    RegisterStyleProcessor registerStyleTypeProcessor = new RegisterStyleProcessor(typeContext, styleHandler);

    xmlParser.nextTag();
    xmlParser.zeroOrMore("useStyles", useStylesTypeProcessor);
    xmlParser.zeroOrMore("style", registerStyleTypeProcessor);
  }
}
