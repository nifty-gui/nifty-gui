package de.lessvoid.nifty.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlInteractAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public abstract class ElementBuilder {

  public static final class LayoutType {
    public static String Vertical = "vertical";
    public static String Horizontal = "horizontal";
    public static String Center = "center";
    public static String Absolute = "absolute";
    public static String Overlay = "overlay";
  }

  public static final class Align {
    public static String Left = "left";
    public static String Right = "right";
    public static String Center = "center";
  }

  public static final class VAlign {
    public static String Top = "top";
    public static String Bottom = "bottom";
    public static String Center = "center";
  }

  protected Screen screen = null;

  protected String id = null;

  protected Element parent = null;

  Map<String, String> elementAttributes = new HashMap<String, String>();

  public void id(String id) {
    this.id = id;
  }

  public void screen(Screen screen) {
    this.screen = screen;
  }

  public void parent(Element parent) {
    this.parent = parent;
  }

  public void backgroundColor(String backgroundColor) {
    elementAttributes.put("backgroundColor", backgroundColor);
  }

  public void color(String color) {
    elementAttributes.put("color", color);
  }

  // I can't ....
  // public void color(Color color) {
  // elementAttributes.put("color", color.toString());
  // }

  /**
   * @param childLayout
   *            use LayoutType.
   */
  public void childLayout(String childLayout) {
    elementAttributes.put("childLayout", childLayout);
  }

  public void height(String height) {
    elementAttributes.put("height", height);
  }

  public void width(String width) {
    elementAttributes.put("width", width);
  }

  /**
   * Use Align static members
   * 
   * @param align
   */
  public void align(String align) {
    elementAttributes.put("align", align);
  }

  /**
   * Use VAlign static members
   * 
   * @param valign
   */
  public void valign(String valign) {
    elementAttributes.put("valign", valign);
  }

  public void visibleToMouse(String visibleToMouse) {
    elementAttributes.put("visibleToMouse", visibleToMouse);
  }

  public void font(String font) {
    elementAttributes.put("font", font);
  }

  // children

  private List<ElementBuilder> elementBuilders = new ArrayList<ElementBuilder>();

  protected void addChildrenFor(Nifty nifty, Element element) {
    for (ElementBuilder elementBuilder : elementBuilders) {

      elementBuilder.parent(element);
      elementBuilder.screen(screen);

      Element childElement = elementBuilder.build(nifty);

      element.add(childElement);
    }
  }

  public void panel(PanelBuilder panelBuilder) {
    elementBuilders.add(panelBuilder);
  }

  public void text(TextBuilder textBuilder) {
    elementBuilders.add(textBuilder);
  }

  public abstract Element build(Nifty nifty);

  protected void validate() {
    if (id == null)
      throw new RuntimeException(
          "id is a required value for an element");

    if (screen == null)
      throw new RuntimeException(
          "screen is a required value for an element");

    if (!hasParent())
      throw new RuntimeException(
          "parent is a required value for an element");
  }

  // effects...

  ControlEffectsAttributes effectsAttributes = new ControlEffectsAttributes();

  public void onStartScreenEffect(
      ControlEffectAttributes onStartScreenEffect) {
    effectsAttributes.addOnStartScreen(onStartScreenEffect);
  }

  public void onHoverEffect(ControlEffectOnHoverAttributes onHoverEffect) {
    effectsAttributes.addOnHover(onHoverEffect);
  }

  public void onEndScreenEffect(ControlEffectAttributes onEndScreenEffect) {
    effectsAttributes.addOnEndScreen(onEndScreenEffect);
  }

  // interact

  ControlInteractAttributes interactAttributes = new ControlInteractAttributes();

  public void interactOnClick(String method) {
    interactAttributes.setOnClick(method);
  }

  public boolean hasParent() {
    return parent != null;
  }
}

