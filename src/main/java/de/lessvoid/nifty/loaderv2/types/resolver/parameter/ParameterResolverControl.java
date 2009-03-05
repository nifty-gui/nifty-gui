package de.lessvoid.nifty.loaderv2.types.resolver.parameter;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import de.lessvoid.xml.xpp3.Attributes;

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

    Set < Map.Entry < Object, Object >> entrySet = getParameterSet(attributes);
    for (Map.Entry < Object, Object > entry : entrySet) {
      String key = (String) entry.getKey();
      String value = (String) entry.getValue();
      if (controlAttributes.isSet(key)) {
        result.set(value, controlAttributes.get(key));
      }
    }

    Attributes res = parent.resolve(result);
    entrySet = getParameterSet(attributes);
    for (Map.Entry < Object, Object > entry : entrySet) {
      String value = (String) entry.getValue();
      result.set(value, "");
    }
    return res;
  }

  private Set < Map.Entry < Object, Object >> getParameterSet(final Attributes attributes) {
    Properties parameters = attributes.extractParameters();
    return parameters.entrySet();
  }
}
