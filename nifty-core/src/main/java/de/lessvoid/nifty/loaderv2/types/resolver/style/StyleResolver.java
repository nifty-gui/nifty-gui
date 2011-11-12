package de.lessvoid.nifty.loaderv2.types.resolver.style;

import de.lessvoid.nifty.loaderv2.types.StyleType;

public interface StyleResolver {
  StyleType resolve(String styleId);
}
