package de.lessvoid.xml.xpp3;

import javax.annotation.Nonnull;

/**
 * XmlProcessor.
 *
 * @author void
 */
public interface XmlProcessor {

  /**
   * Process.
   *
   * @param xmlParser  XmlParser
   * @param attributes attributes
   * @throws Exception exception
   */
  void process(@Nonnull XmlParser xmlParser, @Nonnull Attributes attributes) throws Exception;
}
