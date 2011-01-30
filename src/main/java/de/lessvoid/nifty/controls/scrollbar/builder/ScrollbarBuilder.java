package de.lessvoid.nifty.controls.scrollbar.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class ScrollbarBuilder extends ControlBuilder {
  public ScrollbarBuilder(final boolean vertical) {
    super(vertical ? "verticalScrollbar" : "horizontalScrollbar");
  }

  public ScrollbarBuilder(final String id, final boolean vertical) {
    super(id, vertical ? "verticalScrollbar" : "horizontalScrollbar");
  }
}
