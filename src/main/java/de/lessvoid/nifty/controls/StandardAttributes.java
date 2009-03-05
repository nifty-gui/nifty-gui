package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.LabelType;
import de.lessvoid.nifty.loaderv2.types.resolver.parameter.ParameterResolverDefault;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class StandardAttributes {
  private Attributes attributes = new Attributes();

  public void setAttribute(final String name, final String value) {
    attributes.set(name, value);
  }

  public void setId(final String id) {
    attributes.set("id", id);
  }

  public void setName(final String name) {
    attributes.set("name", name);
  }

  public void setHeight(final String height) {
    attributes.set("height", height);
  }

  public void setWidth(final String width) {
    attributes.set("width", width);
  }

  public void setX(final String x) {
    attributes.set("x", x);
  }

  public void setY(final String y) {
    attributes.set("y", y);
  }

  public void setAlign(final String align) {
    attributes.set("align", align);
  }

  public void setVAlign(final String valign) {
    attributes.set("valign", valign);
  }

  public void setPaddingLeft(final String paddingLeft) {
    attributes.set("paddingLeft", paddingLeft);
  }

  public void setPaddingRight(final String paddingRight) {
    attributes.set("paddingRight", paddingRight);
  }

  public void setPaddingTop(final String paddingTop) {
    attributes.set("paddingTop", paddingTop);
  }

  public void setPaddingBottom(final String paddingBottom) {
    attributes.set("paddingBottom", paddingBottom);
  }

  public void setChildClip(final String childClip) {
    attributes.set("childClip", childClip);
  }

  public void setVisible(final String visible) {
    attributes.set("visible", visible);
  }

  public void setVisibleToMouse(final String visibleToMouse) {
    attributes.set("visibleToMouse", visibleToMouse);
  }

  public void setChildLayout(final String childLayout) {
    attributes.set("childLayout", childLayout);
  }

  public void setFocusable(final String focusable) {
    attributes.set("focusable", focusable);
  }

  public void setFont(final String font) {
    attributes.set("font", font);
  }

  public void setTextHAlign(final String textHAlign) {
    attributes.set("textHAlign", textHAlign);
  }

  public void setTextVAlign(final String textVAlign) {
    attributes.set("textVAlign", textVAlign);
  }

  public void setColor(final String color) {
    attributes.set("color", color);
  }

  public void setSelectionColor(final String selectionColor) {
    attributes.set("selectionColor", selectionColor);
  }

  public void setText(final String text) {
    attributes.set("text", text);
  }

  public void setBackgroundColor(final String backgroundColor) {
    attributes.set("backgroundColor", backgroundColor);
  }

  public void setBackgroundImage(final String backgroundImage) {
    attributes.set("backgroundImage", backgroundImage);
  }

  public void setImageMode(final String imageMode) {
    attributes.set("imageMode", imageMode);
  }

  public void setFilename(final String filename) {
    attributes.set("filename", filename);
  }

  public void setInset(final String inset) {
    attributes.set("inset", inset);
  }

  public void setInputController(final String inputController) {
    attributes.set("inputController", inputController);
  }

  public void setInputMapping(final String inputMapping) {
    attributes.set("inputMapping", inputMapping);
  }

  public Element createControl(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    ControlType controlType = new ControlType(attributes);
    Element control = controlType.create(
      parent,
      nifty,
      screen,
      new LayoutPart(),
      nifty.getDefaultStyleResolver(),
      new ParameterResolverDefault(),
      attributes,
      null);
    screen.layoutLayers();
    return control;
  }

  public Element createLabel(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    LabelType controlType = new LabelType(attributes);
    Element control = controlType.create(
      parent,
      nifty,
      screen,
      new LayoutPart(),
      nifty.getDefaultStyleResolver(),
      new ParameterResolverDefault(),
      attributes,
      null);
    screen.layoutLayers();
    return control;
  }
}
