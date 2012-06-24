package de.lessvoid.nifty.loaderv2.types.apply;

import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.layout.manager.AbsolutePositionLayout;
import de.lessvoid.nifty.layout.manager.CenterLayout;
import de.lessvoid.nifty.layout.manager.HorizontalLayout;
import de.lessvoid.nifty.layout.manager.LayoutManager;
import de.lessvoid.nifty.layout.manager.OverlayLayout;
import de.lessvoid.nifty.layout.manager.VerticalLayout;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.image.ImageMode;
import de.lessvoid.nifty.render.image.ImageModeFactory;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

public class Convert {
  public static final String DEFAULT_PADDING = "0px";
  public static final String DEFAULT_MARGIN = "0px";
  public static final HorizontalAlign DEFAULT_HORIZONTAL_ALIGN = HorizontalAlign.horizontalDefault;
  public static final VerticalAlign DEFAULT_VERTICAL_ALIGN = VerticalAlign.verticalDefault;
  public static final HorizontalAlign DEFAULT_TEXT_HORIZONTAL_ALIGN = HorizontalAlign.center;
  public static final VerticalAlign DEFAULT_TEXT_VERTICAL_ALIGN = VerticalAlign.center;
  public static final boolean DEFAULT_IMAGE_FILTER = false;
  public static final boolean DEFAULT_FOCUSABLE = false;
  public static final boolean DEFAULT_VISIBLE_TO_MOUSE = false;
  public static final boolean DEFAULT_VISIBLE = true;
  public static final boolean DEFAULT_CHILD_CLIP = false;
  public static final Color DEFAULT_COLOR = null;
  public static final int DEFAULT_RENDER_ORDER = 0;
  private static final VerticalLayout verticalLayout = new VerticalLayout();
  private static final CenterLayout centerLayout = new CenterLayout();
  private static final HorizontalLayout horizontalLayout = new HorizontalLayout();
  private static final OverlayLayout overlayLayout = new OverlayLayout();
  private static final AbsolutePositionLayout absolutePositionLayout = new AbsolutePositionLayout();
  private static final AbsolutePositionLayout absolutePositionLayoutKeepInside = new AbsolutePositionLayout(
      new AbsolutePositionLayout.KeepInsidePostProcess());

  public RenderFont font(final NiftyRenderEngine niftyRenderEngine, final String value) {
    if (value == null) {
      return null;
    }
    return niftyRenderEngine.createFont(value);
  }

  public SizeValue sizeValue(final String value) {
    if (value == null) {
      return null;
    }
    if (value.length() == 0) {
      return null;
    }
    return new SizeValue(value);
  }

  public SizeValue paddingSizeValue(final String value, final String defaultValue) {
    if (value == null) {
      return new SizeValue(defaultValue);
    }
    return new SizeValue(value);
  }

  public HorizontalAlign horizontalAlign(final String value) {
    if (value == null) {
      return DEFAULT_HORIZONTAL_ALIGN;
    }
    return HorizontalAlign.valueOf(value);
  }

  public HorizontalAlign textHorizontalAlign(final String value) {
    if (value == null) {
      return DEFAULT_TEXT_HORIZONTAL_ALIGN;
    }
    return HorizontalAlign.valueOf(value);
  }

  public VerticalAlign verticalAlign(final String value) {
    if (value == null) {
      return DEFAULT_VERTICAL_ALIGN;
    }
    return VerticalAlign.valueOf(value);
  }

  public VerticalAlign textVerticalAlign(final String value) {
    if (value == null) {
      return DEFAULT_TEXT_VERTICAL_ALIGN;
    }
    return VerticalAlign.valueOf(value);
  }

  public LayoutManager layoutManager(final String type) {
    if (type == null) {
      return null;
    }
    String typeCompare = type.toLowerCase();
    if (typeCompare.equals("vertical")) {
      return verticalLayout;
    } else if (typeCompare.equals("center")) {
      return centerLayout;
    } else if (typeCompare.equals("horizontal")) {
      return horizontalLayout;
    } else if (typeCompare.equals("overlay")) {
      return overlayLayout;
    } else if (typeCompare.equals("absolute")) {
      return absolutePositionLayout;
    } else if (typeCompare.equals("absolute-inside")) {
      return absolutePositionLayoutKeepInside;
    }
    
    return null;
  }

  public Color color(final String value) {
    if (value == null) {
      return DEFAULT_COLOR;
    }
    return new Color(value);
  }

  public ImageMode imageMode(final String areaProviderProperty, final String renderStrategyProperty) {
	return ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty, renderStrategyProperty);
  }

  public int insetSizeValue(final String value, final int imageHeight) {
    if (value == null) {
      return 0;
    }
    SizeValue sizeValue = new SizeValue(value);
    return sizeValue.getValueAsInt(imageHeight);
  }
}
