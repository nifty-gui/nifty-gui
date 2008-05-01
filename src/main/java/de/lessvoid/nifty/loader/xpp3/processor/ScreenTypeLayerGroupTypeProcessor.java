package de.lessvoid.nifty.loader.xpp3.processor;


import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ScreenType;
import de.lessvoid.nifty.loader.xpp3.elements.ScreenTypeLayerGroupType;

/**
 * ScreenTypeLayerGroupTypeProcessor.
 * @author void
 */
public class ScreenTypeLayerGroupTypeProcessor implements XmlElementProcessor {

  /**
   * screen.
   */
  private ScreenType screen;

  /**
   * create instance.
   * @param screenParam screen
   */
  public ScreenTypeLayerGroupTypeProcessor(final ScreenType screenParam) {
    this.screen = screenParam;
  }

  /**
   * process.
   * @param xmlParser xmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    ScreenTypeLayerGroupType layerGroup = new ScreenTypeLayerGroupType(attributes.get("id"));
    if (attributes.isSet("alternate")) {
      layerGroup.setAlternate(attributes.get("alternate"));
    }
    screen.addLayerGroup(layerGroup);
    xmlParser.nextTag();
  }
}
