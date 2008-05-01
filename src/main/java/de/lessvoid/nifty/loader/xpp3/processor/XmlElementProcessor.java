package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;


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
