package de.lessvoid.nifty.loader.xpp3.elements;

/**
 * AttributesType.
 * @author void
 */
public class AttributesType {

  /**
   * id.
   */
  private String id;

  /**
   * style.
   */
  private String style;

  /**
   * visible.
   */
  private Boolean visible;

  /**
   * width.
   */
  private String width;

  /**
   * height.
   */
  private String height;

  /**
   * x pos.
   */
  private String x;

  /**
   * y pos.
   */
  private String y;

  /**
   * align.
   */
  private AlignType align;

  /**
   * valign.
   */
  private ValignType valign;

  /**
   * text horizontal alignment.
   */
  private AlignType textHAlign;

  /**
   * text vertical alignment.
   */
  private ValignType textVAlign;

  /**
   * childLayoutType.
   */
  private LayoutType childLayoutType;

  /**
   * childClip.
   */
  private Boolean childClip;

  /**
   * backgroundImage.
   */
  private String backgroundImage;

  /**
   * backgroundColor.
   */
  private ColorType backgroundColor = null;

  /**
   * visibleToMouse.
   */
  private Boolean visibleToMouse;

  /**
   * font.
   */
  private String font;

  /**
   * color.
   */
  private ColorType color = null;

  /**
   * filename.
   */
  private String filename;

  /**
   * image filter.
   */
  private Boolean filter = false;

  /**
   * image mode.
   */
  private String imageMode;

  /**
   * default constructor.
   */
  public AttributesType() {
  }

  /**
   * copy constructor.
   * @param other object to copy from
   */
  public AttributesType(final AttributesType other) {
    visible = other.getVisible();
    width = other.getWidth();
    height = other.getHeight();
    x = other.getX();
    y = other.getY();
    align = other.getAlign();
    valign = other.getValign();
    childLayoutType = other.getChildLayoutType();
    childClip = other.getChildClip();
    backgroundImage = other.getBackgroundImage();
    backgroundColor = other.getBackgroundColor();
    visibleToMouse = other.getVisibleToMouse();
    font = other.getFont();
    color = other.getColor();
    filename = other.getFilename();
    filter = other.getFilter();
    textHAlign = other.getTextHAlign();
    textVAlign = other.getTextVAlign();
    imageMode = other.getImageMode();
  }

  /**
   * setId.
   * @param idParam id
   */
  public void setId(final String idParam) {
    this.id = idParam;
  }

  /**
   * setWidth.
   * @param widthParam width
   */
  public void setWidth(final String widthParam) {
    this.width = widthParam;
  }

  /**
   * setHeight.
   * @param heightParam height
   */
  public void setHeight(final String heightParam) {
    this.height = heightParam;
  }

  /**
   * setAlign.
   * @param alignParam align
   */
  public void setAlign(final AlignType alignParam) {
    this.align = alignParam;
  }

  /**
   * setValign.
   * @param valignParam valign
   */
  public void setValign(final ValignType valignParam) {
    this.valign = valignParam;
  }

  /**
   * setChildLayoutType.
   * @param layoutTypeParam layoutType
   */
  public void setChildLayoutType(final LayoutType layoutTypeParam) {
    this.childLayoutType = layoutTypeParam;
  }

  /**
   * setChildClip.
   * @param childClipParam childClip
   */
  public void setChildClip(final boolean childClipParam) {
    this.childClip = childClipParam;
  }

  /**
   * setBackgroundImage.
   * @param backgroundImageParam backgroundImage
   */
  public void setBackgroundImage(final String backgroundImageParam) {
    this.backgroundImage = backgroundImageParam;
  }

  /**
   * setBackgroundColor.
   * @param backgroundColorParam backgroundColor
   */
  public void setBackgroundColor(final ColorType backgroundColorParam) {
    this.backgroundColor = backgroundColorParam;
  }

  /**
   * setVisibleToMouse.
   * @param visibleToMouseParam visibleToMouse
   */
  public void setVisibleToMouse(final Boolean visibleToMouseParam) {
    this.visibleToMouse = visibleToMouseParam;
  }

  /**
   * set visible.
   * @param visibleParam visible
   */
  public void setVisible(final Boolean visibleParam) {
    this.visible = visibleParam;
  }

  /**
   * get id.
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * get visible.
   * @return visible
   */
  public Boolean getVisible() {
    return visible;
  }

  /**
   * get width.
   * @return width
   */
  public String getWidth() {
    return width;
  }

  /**
   * get height.
   * @return height
   */
  public String getHeight() {
    return height;
  }

  /**
   * set x.
   * @param xParam x
   */
  public void setX(final String xParam) {
    this.x = xParam;
  }

  /**
   * set y.
   * @param yParam y
   */
  public void setY(final String yParam) {
    this.y = yParam;
  }

  /**
   * get align.
   * @return align
   */
  public AlignType getAlign() {
    return align;
  }

  /**
   * get valign.
   * @return valign
   */
  public ValignType getValign() {
    return valign;
  }

  /**
   * Get childLayoutType.
   * @return childLayoutType
   */
  public LayoutType getChildLayoutType() {
    return childLayoutType;
  }

  /**
   * is child clip.
   * @return child clip
   */
  public boolean isChildClip() {
    return childClip;
  }

  /**
   * get background image.
   * @return background image
   */
  public String getBackgroundImage() {
    return backgroundImage;
  }

  /**
   * get background color.
   * @return background color
   */
  public ColorType getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * get visible to mouse flag.
   * @return visible to mouse
   */
  public Boolean getVisibleToMouse() {
    return visibleToMouse;
  }

  /**
   * get child clip.
   * @return child clip
   */
  public Boolean getChildClip() {
    return childClip;
  }

  /**
   * set child clip.
   * @param childClipParam childClip param
   */
  public void setChildClip(final Boolean childClipParam) {
    this.childClip = childClipParam;
  }

  /**
   * get x.
   * @return x
   */
  public String getX() {
    return x;
  }

  /**
   * get y.
   * @return y
   */
  public String getY() {
    return y;
  }

  /**
   * set style.
   * @param styleParam style
   */
  public void setStyle(final String styleParam) {
    style = styleParam;
  }

  /**
   * get style.
   * @return style.
   */
  public String getStyle() {
    return style;
  }

  /**
   * get font.
   * @return font
   */
  public String getFont() {
    return font;
  }

  /**
   * get filename.
   * @return filename
   */
  public String getFilename() {
    return filename;
  }

  /**
   * set font.
   * @param fontParam font
   */
  public void setFont(final String fontParam) {
    this.font = fontParam;
  }

  /**
   * get color.
   * @return color
   */
  public ColorType getColor() {
    return color;
  }

  /**
   * set color.
   * @param colorParam color
   */
  public void setColor(final ColorType colorParam) {
    this.color = colorParam;
  }

  /**
   * set filename.
   * @param filenameParam filename
   */
  public void setFilename(final String filenameParam) {
    this.filename = filenameParam;
  }

  /**
   * get filter.
   * @return filter
   */
  public Boolean getFilter() {
    return filter;
  }

  /**
   * set filter.
   * @param filterParam filter
   */
  public void setFilter(final Boolean filterParam) {
    this.filter = filterParam;
  }

  /**
   * set text horizontal align.
   * @param newTextHAlignType new horizontal align type
   */
  public void setTextHAlign(final AlignType newTextHAlignType) {
    textHAlign = newTextHAlignType;
  }

  /**
   * set text vertical align.
   * @param newTextVAlignType new vertical align type
   */
  public void setTextVAlign(final ValignType newTextVAlignType) {
    textVAlign = newTextVAlignType;
  }

  /**
   * get text horizontal alignment.
   * @return text horizontal alignment
   */
  public AlignType getTextHAlign() {
    return textHAlign;
  }

  /**
   * get text vertical alignment.
   * @return text vertical alignment
   */
  public ValignType getTextVAlign() {
    return textVAlign;
  }

  /**
   * set image mode.
   * @param newImageMode new mode
   */
  public void setImageMode(final String newImageMode) {
    imageMode = newImageMode;
  }

  /**
   * get image mode.
   * @return image mode.
   */
  public String getImageMode() {
    return imageMode;
  }

  /**
   * return a new AttributesType with is this + attributes from the param.
   * @param attributesParam attributes we should merge
   * @return new AttributesType
   */
  public AttributesType merge(final AttributesType attributesParam) {
    AttributesType result = new AttributesType(this);
    if (attributesParam.getVisible() != null) {
      result.setVisible(attributesParam.getVisible());
    }
    if (attributesParam.getWidth() != null) {
      result.setWidth(attributesParam.getWidth());
    }
    if (attributesParam.getHeight() != null) {
      result.setHeight(attributesParam.getHeight());
    }
    if (attributesParam.getX() != null) {
      result.setX(attributesParam.getX());
    }
    if (attributesParam.getY() != null) {
      result.setY(attributesParam.getY());
    }
    if (attributesParam.getAlign() != null) {
      result.setAlign(attributesParam.getAlign());
    }
    if (attributesParam.getValign() != null) {
      result.setValign(attributesParam.getValign());
    }
    if (attributesParam.getTextHAlign() != null) {
      result.setTextHAlign(attributesParam.getTextHAlign());
    }
    if (attributesParam.getTextVAlign() != null) {
      result.setTextVAlign(attributesParam.getTextVAlign());
    }
    if (attributesParam.getChildLayoutType() != null) {
      result.setChildLayoutType(attributesParam.getChildLayoutType());
    }
    if (attributesParam.getChildClip() != null) {
      result.setChildClip(attributesParam.getChildClip());
    }
    if (attributesParam.getBackgroundColor() != null) {
      result.setBackgroundColor(attributesParam.getBackgroundColor());
    }
    if (attributesParam.getBackgroundImage() != null) {
      result.setBackgroundImage(attributesParam.getBackgroundImage());
    }
    if (attributesParam.getImageMode() != null) {
      result.setImageMode(attributesParam.getImageMode());
    }
    if (attributesParam.getVisibleToMouse() != null) {
      result.setVisibleToMouse(attributesParam.getVisibleToMouse());
    }
    if (attributesParam.getFont() != null) {
      result.setFont(attributesParam.getFont());
    }
    if (attributesParam.getColor() != null) {
      result.setColor(attributesParam.getColor());
    }
    if (attributesParam.getFilename() != null) {
      result.setFilename(attributesParam.getFilename());
    }
    if (attributesParam.getFilter() != null) {
      result.setFilter(attributesParam.getFilter());
    }
    return result;
  }

}
