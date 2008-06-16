package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;

/**
 * NiftyControlsTypeProcessor.
 * @author void
 */
public class NiftyControlsTypeProcessor implements XmlElementProcessor {

  /**
   * the registerControlTypeProcessor.
   */
  private RegisterControlDefinitionTypeProcessor registerControlTypeProcessor;

  /**
   * create the RegisterControlDefinitionTypeProcessor.
   * @param newRegisterControlTypeProcessor connect to this registerControlTypeProcessor instance
   */
  public NiftyControlsTypeProcessor(final RegisterControlDefinitionTypeProcessor newRegisterControlTypeProcessor) {
    registerControlTypeProcessor = newRegisterControlTypeProcessor;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    xmlParser.nextTag();
    xmlParser.zeroOrMore("controlDefinition", registerControlTypeProcessor);
  }
}
