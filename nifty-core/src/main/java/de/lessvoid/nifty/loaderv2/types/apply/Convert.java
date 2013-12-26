package de.lessvoid.nifty.loaderv2.types.apply;

import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.layout.manager.*;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.image.ImageMode;
import de.lessvoid.nifty.render.image.ImageModeFactory;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

public class Convert {
  private static final Logger log = Logger.getLogger(Convert.class.getName());
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
  public static final int DEFAULT_RENDER_ORDER = 0;
  private static final VerticalLayout verticalLayout = new VerticalLayout();
  private static final CenterLayout centerLayout = new CenterLayout();
  private static final HorizontalLayout horizontalLayout = new HorizontalLayout();
  private static final OverlayLayout overlayLayout = new OverlayLayout();
  private static final AbsolutePositionLayout absolutePositionLayout = new AbsolutePositionLayout();
  private static final AbsolutePositionLayout absolutePositionLayoutKeepInside = new AbsolutePositionLayout(
      new AbsolutePositionLayout.KeepInsidePostProcess());

  @Nullable
  public RenderFont font(@Nonnull final NiftyRenderEngine niftyRenderEngine, @Nullable final String value) {
    if (value == null) {
      return null;
    }
    return niftyRenderEngine.createFont(value);
  }

  @Nonnull
  public SizeValue sizeValue(@Nullable final String value) {
    return new SizeValue(value);
  }

  @Nonnull
  public SizeValue paddingSizeValue(@Nullable final String value, @Nonnull final String defaultValue) {
    if (value == null) {
      return new SizeValue(defaultValue);
    }
    return new SizeValue(value);
  }

  @Nonnull
  public HorizontalAlign horizontalAlign(@Nullable final String value) {
    if (value == null) {
      return DEFAULT_HORIZONTAL_ALIGN;
    }
    try {
      return HorizontalAlign.valueOf(value);
    } catch (IllegalArgumentException e) {
      log.warning("Illegal value for horizontal align: \"" + value + "\"");
      return DEFAULT_TEXT_HORIZONTAL_ALIGN;
    }
  }

  @Nonnull
  public HorizontalAlign textHorizontalAlign(@Nullable final String value) {
    if (value == null) {
      return DEFAULT_TEXT_HORIZONTAL_ALIGN;
    }
    try {
      return HorizontalAlign.valueOf(value);
    } catch (IllegalArgumentException e) {
      log.warning("Illegal value for horizontal text align: \"" + value + "\"");
      return DEFAULT_TEXT_HORIZONTAL_ALIGN;
    }
  }

  @Nonnull
  public VerticalAlign verticalAlign(@Nullable final String value) {
    if (value == null) {
      return DEFAULT_VERTICAL_ALIGN;
    }
    try {
      return VerticalAlign.valueOf(value);
    } catch (IllegalArgumentException e) {
      log.warning("Illegal value for vertical align: \"" + value + "\"");
      return DEFAULT_VERTICAL_ALIGN;
    }
  }

  @Nonnull
  public VerticalAlign textVerticalAlign(@Nullable final String value) {
    if (value == null) {
      return DEFAULT_TEXT_VERTICAL_ALIGN;
    }
    try {
      return VerticalAlign.valueOf(value);
    } catch (IllegalArgumentException e) {
      log.warning("Illegal value for vertical text align: \"" + value + "\"");
      return DEFAULT_TEXT_VERTICAL_ALIGN;
    }
  }

  @Nullable
  public LayoutManager layoutManager(@Nullable final String type) {
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

  @Nullable
  public Color color(@Nullable final String value) {
    if (value == null) {
      return null;
    }
    return new Color(value);
  }

  @Nonnull
  public Color color(@Nullable final String value, @Nonnull final Color defaultColor) {
    if (value == null) {
      return defaultColor;
    }
    return new Color(value);
  }

  @Nonnull
  public ImageMode imageMode(
      @Nullable final String areaProviderProperty,
      @Nullable final String renderStrategyProperty) {
    return ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty, renderStrategyProperty);
  }

  public int insetSizeValue(@Nullable final String value, final int imageHeight) {
    if (value == null) {
      return 0;
    }
    SizeValue sizeValue = new SizeValue(value);
    return sizeValue.getValueAsInt(imageHeight);
  }
}
