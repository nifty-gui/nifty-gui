package de.lessvoid.nifty.loader.xpp3.processor;


import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.ClassHelper;
import de.lessvoid.nifty.loader.xpp3.XmlParser;

/**
 * RegisterLayoutGroup.
 * @author void
 */
public class RegisterLayoutGroupProcessor implements XmlElementProcessor {

  /**
   * process.
   * @param xmlParser parser
   * @param attributes attributes
   * @throws Exception exception
   */
  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    String effectName = attributes.get("name");
    String className = attributes.get("class");
    Class < ? > cl = ClassHelper.loadClass(className);
  }
}
