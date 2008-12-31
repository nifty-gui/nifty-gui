package de.lessvoid.nifty.loader.xpp3.processor;


import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.StyleType;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;

/**
 * RegisterStyleProcessor.
 * @author void
 */
public class RegisterStyleProcessor implements XmlElementProcessor {

  /**
   * style handler.
   */
  private StyleHandler styleHandler;

  /**
   * create the processor.
   * @param newStyleHandler the StyleHandler
   */
  public RegisterStyleProcessor(final StyleHandler newStyleHandler) {
    styleHandler = newStyleHandler;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes Attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    StyleType styleType = null;
    if (attributes.isSet("base")) {
      styleType = new StyleType(attributes.get("id"), styleHandler.getStyle(attributes.get("base")));
    } else {
      styleType = new StyleType(attributes.get("id"));
    }
    xmlParser.nextTag();
    xmlParser.optional("attributes", new StyleAttributesTypeProcessor(styleType));
    xmlParser.optional("effect", new EffectsTypeProcessor(styleType));
    styleHandler.register(styleType);
  }

  /**
   * get style handler.
   * @return style handler
   */
  public StyleHandler getStyleHandler() {
    return styleHandler;
  }
}
