package de.lessvoid.nifty.builder;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlInteractAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;

public abstract class ElementBuilder {
  private ControlAttributes attributes;
  private ControlEffectsAttributes effectsAttributes = new ControlEffectsAttributes();
  private ControlInteractAttributes interactAttributes = new ControlInteractAttributes();
  private List<ElementBuilder> elementBuilders = new ArrayList<ElementBuilder>();
  protected Screen screen = null;
  protected Element parent = null;

  protected void initialize(final ControlAttributes attributes) {
    this.attributes = attributes;
    this.attributes.setEffects(effectsAttributes);
    this.attributes.setInteract(interactAttributes);
  }

  public enum ChildLayoutType {
    Vertical("vertical"),
    Horizontal("horizontal"),
    Center("center"),
    Absolute("absolute"),
    Overlay("overlay");

    private String layout;
    private ChildLayoutType(final String layout) {
      this.layout = layout;
    }
    public String getLayout() {
      return layout;
    }
  }

  public enum Align {
    Left("left"),
    Right("right"),
    Center("center");

    private String align;
    private Align(final String align) {
      this.align = align;
    }
    public String getLayout() {
      return align;
    }
  }

  public enum VAlign {
    Top("top"),
    Bottom("bottom"),
    Center("center");

    private String valign;
    private VAlign(final String valign) {
      this.valign = valign;
    }
    public String getLayout() {
      return valign;
    }
  }

  public void screen(final Screen screen) {
    this.screen = screen;
  }

  public void parent(final Element parent) {
    this.parent = parent;
  }

  public void id(final String id) {
    attributes.setId(id);
  }

  public void name(final String name) {
    attributes.setName(name);
  }

  public void backgroundColor(final String backgroundColor) {
    attributes.setBackgroundColor(backgroundColor);
  }

  public void backgroundColor(final Color backgroundColor) {
    attributes.setBackgroundColor(backgroundColor.toString());
  }

  public void color(final String color) {
    attributes.setColor(color);
  }

  public void color(final Color color) {
    attributes.setColor(color.toString());
  }

  public void selectionColor(final String color) {
    attributes.setSelectionColor(color);
  }

  public void selectionColor(final Color color) {
    attributes.setSelectionColor(color.toString());
  }

  public void text(final String text) {
    attributes.setText(text);
  }

  public void backgroundImage(final String backgroundImage) {
    attributes.setBackgroundImage(backgroundImage);
  }

  public void imageMode(final String imageMode) {
    attributes.setImageMode(imageMode);
  }

  public void inset(final String inset) {
    attributes.setInset(inset);
  }

  public void inputMapping(final String inputMapping) {
    attributes.setInputMapping(inputMapping);
  }

  public void style(final String style) {
    attributes.setStyle(style);
  }

  public void childLayout(final ChildLayoutType childLayout) {
    attributes.setChildLayout(childLayout.getLayout());
  }

  public void childLayoutVertical() {
    childLayout(ChildLayoutType.Vertical);
  }

  public void childLayoutHorizontal() {
    childLayout(ChildLayoutType.Horizontal);
  }

  public void childLayoutCenter() {
    childLayout(ChildLayoutType.Center);
  }

  public void childLayoutAbsolute() {
    childLayout(ChildLayoutType.Absolute);
  }

  public void childLayoutOverlay() {
    childLayout(ChildLayoutType.Overlay);
  }

  public void height(final String height) {
    attributes.setHeight(height);
  }

  public void width(final String width) {
    attributes.setWidth(width);
  }

  public void x(final String x) {
    attributes.setX(x);
  }

  public void y(final String y) {
    attributes.setY(y);
  }

  public void childClip(final boolean childClip) {
    attributes.setChildClip(String.valueOf(childClip));
  }

  public void visible(final boolean visible) {
    attributes.setVisible(String.valueOf(visible));
  }

  public void focusable(final boolean focusable) {
    attributes.setFocusable(String.valueOf(focusable));
  }

  public void textHAlign(final Align align) {
    attributes.set("textHAlign", align.getLayout());
  }

  public void textHAlignLeft() {
    textHAlign(Align.Left);
  }

  public void textHAlignRight() {
    textHAlign(Align.Right);
  }

  public void textHAlignCenter() {
    textHAlign(Align.Center);
  }

  public void textVAlign(final VAlign valign) {
    attributes.set("textVAlign", valign.getLayout());
  }

  public void textVAlignTop() {
    textVAlign(VAlign.Top);
  }

  public void textVAlignBottom() {
    textVAlign(VAlign.Bottom);
  }

  public void textVAlignCenter() {
    textVAlign(VAlign.Center);
  }

  public void align(final Align align) {
    attributes.setAlign(align.getLayout());
  }

  public void alignLeft() {
    align(Align.Left);
  }

  public void alignRight() {
    align(Align.Right);
  }

  public void alignCenter() {
    align(Align.Center);
  }

  public void valign(final VAlign valign) {
    attributes.setVAlign(valign.getLayout());
  }

  public void valignTop() {
    valign(VAlign.Top);
  }

  public void valignBottom() {
    valign(VAlign.Bottom);
  }

  public void valignCenter() {
    valign(VAlign.Center);
  }

  public void visibleToMouse(final boolean visibleToMouse) {
    attributes.setVisibleToMouse(String.valueOf(visibleToMouse));
  }

  public void visibleToMouse() {
    visibleToMouse(true);
  }

  public void invisibleToMouse() {
    visibleToMouse(false);
  }

  public void font(final String font) {
    attributes.setFont(font);
  }

  public void filename(final String filename) {
    attributes.setFilename(filename);
  }

  public void padding(final String padding) {
    attributes.setPadding(padding);
  }

  public void paddingLeft(final String padding) {
    attributes.setPaddingLeft(padding);
  }

  public void paddingRight(final String padding) {
    attributes.setPaddingRight(padding);
  }

  public void paddingTop(final String padding) {
    attributes.setPaddingTop(padding);
  }

  public void paddingBottom(final String padding) {
    attributes.setPaddingBottom(padding);
  }

  public void set(final String key, final String value) {
    attributes.set(key, value);
  }

  public void panel(final PanelBuilder panelBuilder) {
    elementBuilders.add(panelBuilder);
  }

  public void text(final TextBuilder textBuilder) {
    elementBuilders.add(textBuilder);
  }

  public void image(final ImageBuilder imageBuilder) {
    elementBuilders.add(imageBuilder);
  }

  public void onStartScreenEffect(final ControlEffectAttributes onStartScreenEffect) {
    effectsAttributes.addOnStartScreen(onStartScreenEffect);
  }

  public void onEndScreenEffect(final ControlEffectAttributes onEndScreenEffect) {
    effectsAttributes.addOnEndScreen(onEndScreenEffect);
  }

  public void onHoverEffect(final ControlEffectOnHoverAttributes onHoverEffect) {
    effectsAttributes.addOnHover(onHoverEffect);
  }

  public void onEffectsOnClick(final ControlEffectAttributes onClickEffect) {
    effectsAttributes.addOnClick(onClickEffect);
  }

  public void onEffectsOnFocus(final ControlEffectAttributes onFocus) {
    effectsAttributes.addOnFocus(onFocus);
  }

  public void onEffectsOnLostFocus(final ControlEffectAttributes onLostFocus) {
    effectsAttributes.addOnLostFocus(onLostFocus);
  }

  public void onEffectsOnGetFocus(final ControlEffectAttributes onGetFocus) {
    effectsAttributes.addOnGetFocus(onGetFocus);
  }

  public void onEffectsOnActive(final ControlEffectAttributes onActive) {
    effectsAttributes.addOnActive(onActive);
  }

  public void onEffectsOnShow(final ControlEffectAttributes onShow) {
    effectsAttributes.addOnShow(onShow);
  }

  public void onEffectsOnHide(final ControlEffectAttributes onHide) {
    effectsAttributes.addOnShow(onHide);
  }

  public void onEffectsOnCustom(final ControlEffectAttributes onCustom) {
    effectsAttributes.addOnCustom(onCustom);
  }

  public void interactOnClick(String method) {
    interactAttributes.setOnClick(method);
  }

  public void interactOnRelease(final String onRelease) {
    interactAttributes.setOnRelease(onRelease);
  }

  public void interactOnMouseOver(final String onMouseOver) {
    interactAttributes.setOnMouseOver(onMouseOver);
  }

  public void interactOnClickRepeat(final String onClickRepeat) {
    interactAttributes.setOnClickRepeat(onClickRepeat);
  }

  public void interactOnClickMouseMove(final String onClickMouseMove) {
    interactAttributes.setOnClickMouseMove(onClickMouseMove);
  }

  public void interactOnClickAlternateKey(final String onClickAlternateKey) {
    interactAttributes.setOnClickAlternateKey(onClickAlternateKey);
  }

  public boolean hasParent() {
    return parent != null;
  }

  public String percentage(final int percentage) {
    return Integer.toString(percentage) + "%";
  }

  public String pixels(final int px) {
    return Integer.toString(px) + "px";
  }

  protected abstract Element buildInternal(Nifty nifty, Screen screen, Element parent);

  public Element build(final Nifty nifty, final Screen screen, final Element parent) {
    Element element = buildInternal(nifty, screen, parent);

    for (ElementBuilder elementBuilder : elementBuilders) {
      elementBuilder.parent(element);
      elementBuilder.screen(screen);

      Element childElement = elementBuilder.build(nifty, screen, parent);
      element.add(childElement);
    }

    return element;
  }

  protected void validate() {
    if (screen == null)
      throw new RuntimeException("screen is a required value for an element");

    if (!hasParent())
      throw new RuntimeException("parent is a required value for an element");
  }
}

