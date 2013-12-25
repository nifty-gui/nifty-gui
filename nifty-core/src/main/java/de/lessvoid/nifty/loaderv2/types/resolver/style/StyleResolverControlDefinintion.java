package de.lessvoid.nifty.loaderv2.types.resolver.style;

import de.lessvoid.nifty.loaderv2.types.StyleType;

import javax.annotation.Nullable;

public class StyleResolverControlDefinintion implements StyleResolver {
  private final StyleResolver baseStyleResolver;
  private final String baseStyleId;

  public StyleResolverControlDefinintion(
      final StyleResolver baseStyleResolverParam,
      final String baseStyleIdParam) {
    baseStyleResolver = baseStyleResolverParam;
    baseStyleId = baseStyleIdParam;
  }

  @Override
  @Nullable
  public StyleType resolve(@Nullable final String styleId) {
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
