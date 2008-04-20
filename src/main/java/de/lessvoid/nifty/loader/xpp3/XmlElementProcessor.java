package de.lessvoid.nifty.loader.xpp3;


/**
 * NodeProcessor helper.
 * @author void
 */
public interface XmlElementProcessor {

  /**
   * Process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  void process(XmlParser xmlParser, Attributes attributes) throws Exception;
}
