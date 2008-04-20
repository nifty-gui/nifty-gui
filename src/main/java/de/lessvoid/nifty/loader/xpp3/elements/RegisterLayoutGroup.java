package de.lessvoid.nifty.loader.xpp3.elements;


import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.ClassHelper;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;

/**
 * RegisterLayoutGroup.
 * @author void
 */
public class RegisterLayoutGroup implements XmlElementProcessor {

  /**
   * process.
   * @param parser parser
   */
  public void process(final XmlParser parser, Attributes attributes) throws Exception {
    String effectName = attributes.get("name");
    String className = attributes.get("class");
    Class < ? > cl = ClassHelper.loadClass(className);
    if (cl != null) {
    }
    
  }
}