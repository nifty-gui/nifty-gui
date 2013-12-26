package de.lessvoid.nifty.loaderv2.types.resolver.style;

import de.lessvoid.nifty.loaderv2.types.StyleType;

import javax.annotation.Nullable;

public interface StyleResolver {
  @Nullable
  StyleType resolve(String styleId);
}
