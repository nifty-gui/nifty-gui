package de.lessvoid.nifty.loaderv2.types.apply;

import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.layout.manager.AbsolutePositionLayout;
import de.lessvoid.nifty.layout.manager.CenterLayout;
import de.lessvoid.nifty.layout.manager.HorizontalLayout;
import de.lessvoid.nifty.layout.manager.LayoutManager;
import de.lessvoid.nifty.layout.manager.OverlayLayout;
import de.lessvoid.nifty.layout.manager.VerticalLayout;
import de.lessvoid.nifty.render.NiftyImageMode;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

public class Convert {
  public static final SizeValue DEFAULT_PADDING = new SizeValue("0px");
  public static final HorizontalAlign DEFAULT_HORIZONTAL_ALIGN = HorizontalAlign.left;
  public static final VerticalAlign DEFAULT_VERTICAL_ALIGN = VerticalAlign.top;
  public static final HorizontalAlign DEFAULT_TEXT_HORIZONTAL_ALIGN = HorizontalAlign.center;
  public static final VerticalAlign DEFAULT_TEXT_VERTICAL_ALIGN = VerticalAlign.center;
  public static final NiftyImageMode DEFAULT_IMAGE_MODE = NiftyImageMode.normal();
  public static final boolean DEFAULT_IMAGE_FILTER = false;
  public static final boolean DEFAULT_FOCUSABLE = false;
  public static final boolean DEFAULT_VISIBLE_TO_MOUSE = false;
  public static final boolean DEFAULT_VISIBLE = true;
  public static final boolean DEFAULT_CHILD_CLIP = false;
  public static final Color DEFAULT_COLOR = null;

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

  public SizeValue paddingSizeValue(final String value) {
    if (value == null) {
      return DEFAULT_PADDING;
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
      return new VerticalLayout();
    } else if (typeCompare.equals("center")) {
      return new CenterLayout();
    } else if (typeCompare.equals("horizontal")) {
      return new HorizontalLayout();
    } else if (typeCompare.equals("overlay")) {
      return new OverlayLayout();
    } else if (typeCompare.equals("absolute")) {
      return new AbsolutePositionLayout();
    }
    return null;
  }

  public Color color(final String value) {
    if (value == null) {
      return DEFAULT_COLOR;
    }
    return new Color(value);
  }

  public NiftyImageMode imageMode(final String value) {
    if (value == null) {
      return DEFAULT_IMAGE_MODE;
    }
    return NiftyImageMode.valueOf(value);
  }

  public int insetSizeValue(final String value, final int imageHeight) {
    if (value == null) {
      return 0;
    }
    SizeValue sizeValue = new SizeValue(value);
    return sizeValue.getValueAsInt(imageHeight);
  }
}
