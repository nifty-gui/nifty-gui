package de.lessvoid.nifty.loaderv2.types.resolver.style;

import de.lessvoid.nifty.loaderv2.types.StyleType;

public class StyleResolverControlDefinintion implements StyleResolver {
  private StyleResolver baseStyleResolver;
  private String baseStyleId;

  public StyleResolverControlDefinintion(
      final StyleResolver baseStyleResolverParam,
      final String baseStyleIdParam) {
    baseStyleResolver = baseStyleResolverParam;
    baseStyleId = baseStyleIdParam;
  }

  public StyleType resolve(final String styleId) {
    if (styleId == null) {
      return null;
    }
    int indexOf = styleId.indexOf("#");
    if (indexOf == -1) {
      return baseStyleResolver.resolve(styleId);
    } else if (indexOf == 0) {
      return baseStyleResolver.resolve(baseStyleId + styleId);
    } else {
      return baseStyleResolver.resolve(styleId);
    }
  }
}
