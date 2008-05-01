package de.lessvoid.nifty.loader.xpp3.elements;

import de.lessvoid.nifty.layout.manager.AbsolutePositionLayout;
import de.lessvoid.nifty.layout.manager.CenterLayout;
import de.lessvoid.nifty.layout.manager.HorizontalLayout;
import de.lessvoid.nifty.layout.manager.LayoutManager;
import de.lessvoid.nifty.layout.manager.OverlayLayout;
import de.lessvoid.nifty.layout.manager.VerticalLayout;

/**
 * LayoutType.
 * @author void
 */
public enum LayoutType {
  /**
   * vertical.
   */
  vertical("vertical"),

  /**
   * center.
   */
  center("center"),

  /**
   * absolute.
   */
  absolute("absolute"),

  /**
   * horizontal.
   */
  horizontal("horizontal"),

  /**
   * overlay.
   */
  overlay("overlay");

  /**
   * the actual value.
   */
  private String value;

  /**
   * LayoutType.
   * @param valueParam value
   */
  private LayoutType(final String valueParam) {
    this.value = valueParam;
  }

  /**
   * Get LayoutManager.
   * @return layoutManager
   */
  public LayoutManager getLayoutManager() {
    switch (this) {
    case vertical:
      return new VerticalLayout();
    case center:
      return new CenterLayout();
    case horizontal:
      return new HorizontalLayout();
    case overlay:
      return new OverlayLayout();
    case absolute:
      return new AbsolutePositionLayout();
    default:
        return null;
    }
  }
}
