package de.lessvoid.nifty.controls.scrollbar.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class ScrollbarBuilder extends ControlBuilder {
  public ScrollbarBuilder(final boolean vertical) {
    super(getName(vertical));
  }

  public ScrollbarBuilder(@Nonnull final String id, final boolean vertical) {
    super(id, getName(vertical));
  }

  @Nonnull
  private static String getName(final boolean vertical) {
    if (vertical) {
      return "verticalScrollbar";
    } else {
      return "horizontalScrollbar";
    }
  }
}
