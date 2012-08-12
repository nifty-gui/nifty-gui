package de.lessvoid.nifty.loaderv2.types;

import java.util.logging.Logger;

import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.tools.ClassHelper;
import de.lessvoid.xml.xpp3.Attributes;

public class RegisterEffectType extends XmlBaseType {
  private static Logger logger = Logger.getLogger(RegisterEffectType.class.getName());

  public RegisterEffectType() {
  }

  public RegisterEffectType(final String nameParam, final String classParam) {
    Attributes attributes = new Attributes();
    attributes.set("name", nameParam);
    attributes.set("class", classParam);
    try {
      initFromAttributes(attributes);
    } catch (Exception e) {
      logger.warning(
          "unable to register effect [" + nameParam + "] for class [" + classParam + "] (" + e.getMessage() + "]");
    }
  }

  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<registerEffect> " + super.output(offset);
  }

  public Class < ? > getEffectClass() {
    String className = getClassName();
    if (className == null) {
      return null;
    }
    return ClassHelper.loadClass(className);
  }

  public String getName() {
    return getAttributes().get("name");
  }

  private String getClassName() {
    return getAttributes().get("class");
  }
}
