package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.LayerType;
import de.lessvoid.nifty.loader.xpp3.elements.ScreenType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;


/**
 * LayerType.
 * @author void
 */
public class LayerTypeProcessor implements XmlElementProcessor {

  /**
   * screen.
   */
  private ScreenType screen;
  private TypeContext typeContext;

  /**
   * LayerTypeProcessor.
   * @param screenParam screen
   */
  public LayerTypeProcessor(final TypeContext newTypeContext, final ScreenType screenParam) {
    this.typeContext = newTypeContext;
    this.screen = screenParam;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    LayerType layer = typeContext.createLayerType(attributes);
    ProcessorHelper.processElement(xmlParser, layer, typeContext);
    screen.addLayer(layer);
  }
}
