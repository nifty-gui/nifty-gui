package de.lessvoid.nifty.controls.scrollbar.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class ScrollbarBuilder extends ControlBuilder {
  public ScrollbarBuilder(final boolean vertical) {
    super(getName(vertical));
  }

  public ScrollbarBuilder(final String id, final boolean vertical) {
    super(id, getName(vertical));
  }

  private static String getName(final boolean vertical) {
    if (vertical) {
      return "verticalScrollbar";
    } else {
      return "horizontalScrollbar";
    }
  }
}
