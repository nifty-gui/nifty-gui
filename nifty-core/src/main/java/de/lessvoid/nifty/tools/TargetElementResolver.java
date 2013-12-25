package de.lessvoid.nifty.tools;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TargetElementResolver {
  @Nonnull
  private final static String PARENT = "#parent";

  @Nonnull
  private final Screen screen;
  @Nonnull
  private final Element base;

  public TargetElementResolver(@Nonnull final Screen screen, @Nonnull final Element baseElement) {
    this.screen = screen;
    this.base = baseElement;
  }

  @Nullable
  public Element resolve(@Nullable final String id) {
    if (id == null) {
      return null;
    }
    if (id.startsWith(PARENT)) {
      return resolveParents(id, base.getParent());
    }
    if (id.startsWith("#")) {
      return base.findElementById(id);
    }
    return screen.findElementById(id);
  }

  @Nullable
  private Element resolveParents(@Nonnull final String id, @Nonnull final Element parent) {
    String subParentId = id.replaceFirst(PARENT, "");
    if (!subParentId.startsWith(PARENT)) {
      if (subParentId.startsWith("#")) {
        return parent.findElementById(subParentId.replaceFirst("#", ""));
      }
      return parent;
    }
    return resolveParents(subParentId, parent.getParent());
  }
}
