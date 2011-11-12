package de.lessvoid.xml.xpp3;

/**
 * XmlProcessor.
 * @author void
 */
public interface XmlProcessor {

  /**
   * Process.
   * @param xmlParser XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  void process(XmlParser xmlParser, Attributes attributes) throws Exception;
}
