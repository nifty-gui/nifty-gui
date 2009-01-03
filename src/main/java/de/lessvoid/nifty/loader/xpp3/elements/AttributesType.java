package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import org.newdawn.slick.util.Log;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.elements.helper.PaddingAttributeParser;

/**
 * AttributesType.
 * @author void
 */
public class AttributesType {

  /**
   * source attributes this attributes instance is based upon.
   */
  private Attributes srcAttributes;

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
   * text attribute.
   */
  private String text;

  /**
   * focusable.
   */
  private Boolean focusable;

  private String paddingLeft;
  private String paddingRight;
  private String paddingTop;
  private String paddingBottom;
  private String inset;

  private String inputController;
  private String inputMapping;

  /**
   * default constructor.
   */
  public AttributesType() {
    srcAttributes = null;
  }

  /**
   * constructor from attributes.
   * @param attributes attributes
   */
  public AttributesType(final Attributes attributes) {
    srcAttributes = attributes;
    processAttributes();
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
    text = other.text;
    focusable = other.focusable;
    srcAttributes = other.srcAttributes;
    paddingLeft = other.paddingLeft;
    paddingRight = other.paddingRight;
    paddingTop = other.paddingTop;
    paddingBottom = other.paddingBottom;
    inset = other.inset;
    inputController = other.inputController;
    inputMapping = other.inputMapping;
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
   * process element attributes.
   */
  public void processAttributes() {
    // id
    if (srcAttributes.isSet("id")) {
      setId(srcAttributes.get("id"));
    }
    // style
    if (srcAttributes.isSet("style")) {
      setStyle(srcAttributes.get("style"));
    }
    // visible
    if (srcAttributes.isSet("visible")) {
      setVisible(srcAttributes.getAsBoolean("visible"));
    }
    // height
    if (srcAttributes.isSet("height")) {
      setHeight(srcAttributes.get("height"));
    }
    // width
    if (srcAttributes.isSet("width")) {
      setWidth(srcAttributes.get("width"));
    }
    // x
    if (srcAttributes.isSet("x")) {
      setX(srcAttributes.get("x"));
    }
    // y
    if (srcAttributes.isSet("y")) {
      setY(srcAttributes.get("y"));
    }
    // horizontal align
    if (srcAttributes.isSet("align")) {
      setAlign(srcAttributes.getAsAlignType("align"));
    }
    // vertical align
    if (srcAttributes.isSet("valign")) {
      setValign(srcAttributes.getAsVAlignType("valign"));
    }
    // child clip
    if (srcAttributes.isSet("childClip")) {
      setChildClip(srcAttributes.getAsBoolean("childClip"));
    }
    // visibleToMouse
    if (srcAttributes.isSet("visibleToMouse")) {
      setVisibleToMouse(srcAttributes.getAsBoolean("visibleToMouse"));
    }
    // childLayout
    if (srcAttributes.isSet("childLayout")) {
      setChildLayoutType(srcAttributes.getAsLayoutType("childLayout"));
    }
    // backgroundImage
    if (srcAttributes.isSet("backgroundImage")) {
      setBackgroundImage(srcAttributes.get("backgroundImage"));
    }
    // imageMode
    if (srcAttributes.isSet("imageMode")) {
      setImageMode(srcAttributes.get("imageMode"));
    }
    // backgroundColor
    if (srcAttributes.isSet("backgroundColor")) {
      setBackgroundColor(new ColorType(srcAttributes.get("backgroundColor")));
    }
    // font
    if (srcAttributes.isSet("font")) {
      setFont(srcAttributes.get("font"));
    }
    // color
    if (srcAttributes.isSet("color")) {
      setColor(new ColorType(srcAttributes.get("color")));
    }
    // filename
    if (srcAttributes.isSet("filename")) {
      setFilename(srcAttributes.get("filename"));
    }
    // filter
    if (srcAttributes.isSet("filter")) {
      setFilter(srcAttributes.getAsBoolean("filter"));
    }
    // text horizontal align
    if (srcAttributes.isSet("textHAlign")) {
      setTextHAlign(srcAttributes.getAsAlignType("textHAlign"));
    }
    // text vertical align
    if (srcAttributes.isSet("textVAlign")) {
      setTextVAlign(srcAttributes.getAsVAlignType("textVAlign"));
    }
    // text
    if (srcAttributes.isSet("text")) {
      text = srcAttributes.get("text");
    }
    // focusable
    if (srcAttributes.isSet("focusable")) {
      focusable = srcAttributes.getAsBoolean("focusable");
    }
    // paddingLeft
    if (srcAttributes.isSet("paddingLeft")) {
      paddingLeft = srcAttributes.get("paddingLeft");
    }
    // paddingRight
    if (srcAttributes.isSet("paddingRight")) {
      paddingRight = srcAttributes.get("paddingRight");
    }
    // paddingTop
    if (srcAttributes.isSet("paddingTop")) {
      paddingTop = srcAttributes.get("paddingTop");
    }
    // paddingBottom
    if (srcAttributes.isSet("paddingBottom")) {
      paddingBottom = srcAttributes.get("paddingBottom");
    }
    // padding
    if (srcAttributes.isSet("padding")) {
      parsePadding(srcAttributes.get("padding"));
    }
    // inset
    if (srcAttributes.isSet("inset")) {
      setInset(srcAttributes.get("inset"));
    }
    if (srcAttributes.isSet("inputController")) {
      setInputController(srcAttributes.get("inputController"));
    }
    if (srcAttributes.isSet("inputMapping")) {
      setInputMapping(srcAttributes.get("inputMapping"));
    }
  }

  private void parsePadding(final String paddingValue) {
    try {
      PaddingAttributeParser parser = new PaddingAttributeParser(paddingValue);
      paddingLeft = parser.getPaddingLeft();
      paddingRight = parser.getPaddingRight();
      paddingTop = parser.getPaddingTop();
      paddingBottom = parser.getPaddingBottom();
    } catch (Exception e) {
      Log.error(e);
    }
  }

  /**
   * get source attributes.
   * @return source attributes
   */
  public Attributes getSrcAttributes() {
    return srcAttributes;
  }

  /**
   * find parameter attributes.
   * @return set of parameter attributes
   */
  public Map < String, String > findParameterAttributes() {
    return srcAttributes.getParameterAttributes();
  }

  /**
   * get text.
   * @return text
   */
  public String getText() {
    return text;
  }

  /**
   * get focusable.
   * @return focusable
   */
  public Boolean getFocusable() {
    return focusable;
  }

  public String getPaddingLeft() {
    return paddingLeft;
  }

  public String getPaddingRight() {
    return paddingRight;
  }

  public String getPaddingTop() {
    return paddingTop;
  }

  public String getPaddingBottom() {
    return paddingBottom;
  }

  public String getInset() {
    return inset;
  }

  public void setInset(final String insetParam) {
    this.inset = insetParam;
  }

  public String getInputController() {
    return inputController;
  }

  private void setInputController(final String controllerParam) {
    inputController = controllerParam;
  }

  public String getInputMapping() {
    return inputMapping;
  }

  public void setInputMapping(final String inputMappingParam) {
    this.inputMapping = inputMappingParam;
  }
}
