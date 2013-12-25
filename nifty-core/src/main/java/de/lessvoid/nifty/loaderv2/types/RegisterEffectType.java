package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.tools.ClassHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

public class RegisterEffectType extends XmlBaseType {
  private static final Logger logger = Logger.getLogger(RegisterEffectType.class.getName());

  public RegisterEffectType() {
  }

  public RegisterEffectType(@Nonnull final String nameParam, @Nonnull final String classParam) {
    getAttributes().set("name", nameParam);
    getAttributes().set("class", classParam);
  }

  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<registerEffect> " + super.output(offset);
  }

  @Nullable
  public Class<?> getEffectClass() {
    String className = getClassName();
    if (className == null) {
      return null;
    }
    return ClassHelper.loadClass(className);
  }

  @Nullable
  public String getName() {
    return getAttributes().get("name");
  }

  @Nullable
  private String getClassName() {
    return getAttributes().get("class");
  }
}
