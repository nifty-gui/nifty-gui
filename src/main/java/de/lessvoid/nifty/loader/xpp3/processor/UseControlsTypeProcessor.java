package de.lessvoid.nifty.loader.xpp3.processor;


import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.NiftyLoader;
import de.lessvoid.nifty.loader.xpp3.XmlParser;

/**
 * UseControlsTypeProcessor.
 * @author void
 */
public class UseControlsTypeProcessor implements XmlElementProcessor {
  private NiftyLoader niftyLoader;
  private PopupTypeProcessor popupTypeProcessor;

  public UseControlsTypeProcessor(final NiftyLoader newNiftyLoader, final PopupTypeProcessor newPopupTypeProcessor) {
    niftyLoader = newNiftyLoader;
    popupTypeProcessor = newPopupTypeProcessor;
  }

  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    if (attributes.isSet("filename")) {
      niftyLoader.loadNiftyControls(attributes.get("filename"), popupTypeProcessor);
    }
    xmlParser.nextTag();
  }
}
