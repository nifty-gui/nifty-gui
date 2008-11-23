package de.lessvoid.nifty.loader.xpp3.processor;


import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.NiftyLoader;
import de.lessvoid.nifty.loader.xpp3.XmlParser;

/**
 * UseControlsTypeProcessor.
 * @author void
 */
public class UseControlsTypeProcessor implements XmlElementProcessor {
  /**
   * nifty loader.
   */
  private NiftyLoader niftyLoader;
  private PopupTypeProcessor popupTypeProcessor;

  /**
   * create the processor.
   * @param newNiftyLoader nifty loader
   * @param newPopupTypeProcessor 
   */
  public UseControlsTypeProcessor(final NiftyLoader newNiftyLoader, final PopupTypeProcessor newPopupTypeProcessor) {
    niftyLoader = newNiftyLoader;
    popupTypeProcessor = newPopupTypeProcessor;
  }

  /**
   * process.
   * @param xmlParser XmlParser
   * @param attributes Attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    if (attributes.isSet("filename")) {
      niftyLoader.loadNiftyControls(attributes.get("filename"), popupTypeProcessor);
    }
    xmlParser.nextTag();
  }
}
