package de.lessvoid.nifty.loaderv2.types.resolver.parameter;

import java.util.List;

import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.xml.xpp3.Attributes.Entry;

public class ParameterResolverControl implements ParameterResolver {
  private ParameterResolver parent;
  private Attributes controlAttributes;

  public ParameterResolverControl(
      final ParameterResolver parentParam,
      final Attributes controlAttributesParam) {
    parent = parentParam;
    controlAttributes = controlAttributesParam;
  }

  public Attributes resolve(final Attributes attributes) {
    Attributes result = new Attributes(attributes);

    for (Entry entry : getParameterSet(attributes)) {
      String key = (String) entry.getKey();
      String value = (String) entry.getValue();
      if (controlAttributes.isSet(key)) {
        result.set(value, controlAttributes.get(key));
      }
    }

    Attributes res = parent.resolve(result);
    for (Entry entry : getParameterSet(attributes)) {
      String value = (String) entry.getValue();
      result.set(value, "");
    }
    return res;
  }

  private List<Entry> getParameterSet(final Attributes attributes) {
    return attributes.extractParameters();
  }
}
