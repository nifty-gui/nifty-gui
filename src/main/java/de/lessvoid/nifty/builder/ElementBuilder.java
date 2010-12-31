package de.lessvoid.nifty.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlInteractAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;

public abstract class ElementBuilder {
  private ControlAttributes attributes;
  private ControlInteractAttributes interactAttributes = new ControlInteractAttributes();
  private List<ElementBuilder> elementBuilders = new ArrayList<ElementBuilder>();
  private Collection<EffectBuilder> onStartScreen = new ArrayList<EffectBuilder>();
  private Collection<EffectBuilder> onEndScreen = new ArrayList<EffectBuilder>();
  private Collection<HoverEffectBuilder> onHover = new ArrayList<HoverEffectBuilder>();
  private Collection<EffectBuilder> onClick = new ArrayList<EffectBuilder>();
  private Collection<EffectBuilder> onFocus = new ArrayList<EffectBuilder>();
  private Collection<EffectBuilder> onLostFocus = new ArrayList<EffectBuilder>();
  private Collection<EffectBuilder> onGetFocus = new ArrayList<EffectBuilder>();
  private Collection<EffectBuilder> onActive = new ArrayList<EffectBuilder>();
  private Collection<EffectBuilder> onCustom = new ArrayList<EffectBuilder>();
  private Collection<EffectBuilder> onShow = new ArrayList<EffectBuilder>();
  private Collection<EffectBuilder> onHide = new ArrayList<EffectBuilder>();

  protected void initialize(final ControlAttributes attributes) {
    this.attributes = attributes;
    this.attributes.setInteract(interactAttributes);
  }

  public enum ChildLayoutType {
    Vertical("vertical"), Horizontal("horizontal"), Center("center"), Absolute("absolute"), Overlay("overlay");
    
    private String layout;
    
    private ChildLayoutType(final String layout) {
      this.layout = layout;
    }
    
    public String getLayout() {
      return layout;
    }
  }

  public enum Align {
    Left("left"), Right("right"), Center("center");
    
    private String align;
    
    private Align(final String align) {
      this.align = align;
    }
    
    public String getLayout() {
      return align;
    }
  }

  public enum VAlign {
    Top("top"), Bottom("bottom"), Center("center");
    
    private String valign;
    
    private VAlign(final String valign) {
      this.valign = valign;
    }
    
    public String getLayout() {
      return valign;
    }
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

  public void label(final LabelBuilder labelBuilder) {
    elementBuilders.add(labelBuilder);
  }

  public void text(final TextBuilder textBuilder) {
    elementBuilders.add(textBuilder);
  }

  public void image(final ImageBuilder imageBuilder) {
    elementBuilders.add(imageBuilder);
  }
  
  public void control(ControlBuilder controlBuilder) {
    elementBuilders.add(controlBuilder);
  }

  public void onStartScreenEffect(final EffectBuilder onStartScreenEffect) {
    onStartScreen.add(onStartScreenEffect);
  }

  public void onEndScreenEffect(final EffectBuilder onEndScreenEffect) {
    onEndScreen.add(onEndScreenEffect);
  }

  public void onHoverEffect(final HoverEffectBuilder onHoverEffect) {
    onHover.add(onHoverEffect);
  }

  public void onEffectsOnClick(final EffectBuilder onClickEffect) {
    onClick.add(onClickEffect);
  }

  public void onEffectsOnFocus(final EffectBuilder onFocusEffect) {
    onFocus.add(onFocusEffect);
  }

  public void onEffectsOnLostFocus(final EffectBuilder onLostFocusEffect) {
    onLostFocus.add(onLostFocusEffect);
  }

  public void onEffectsOnGetFocus(final EffectBuilder onGetFocusEffect) {
    onGetFocus.add(onGetFocusEffect);
  }

  public void onEffectsOnActive(final EffectBuilder onActiveEffect) {
    onActive.add(onActiveEffect);
  }

  public void onEffectsOnShow(final EffectBuilder onShowEffect) {
    onShow.add(onShowEffect);
  }

  public void onEffectsOnHide(final EffectBuilder onHideEffect) {
    onHide.add(onHideEffect);
  }

  public void onEffectsOnCustom(final EffectBuilder onCustomEffect) {
    onCustom.add(onCustomEffect);
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

  public String percentage(final int percentage) {
    return Integer.toString(percentage) + "%";
  }

  public String pixels(final int px) {
    return Integer.toString(px) + "px";
  }

  protected abstract Element buildInternal(Nifty nifty, Screen screen, Element parent);

  public Element build(final Nifty nifty, final Screen screen, final Element parent) {
    connectAttributes();
    Element element = buildInternal(nifty, screen, parent);
    for (ElementBuilder elementBuilder : elementBuilders) {
      elementBuilder.build(nifty, screen, element);
    }
    return element;
  }

  protected ElementType buildElementType() {
    connectAttributes();
    ElementType thisType = attributes.createType();
    attributes.connect(thisType);
    for (ElementBuilder elementBuilder : elementBuilders) {
      thisType.addElementType(elementBuilder.buildElementType());
    }
    return thisType;
  }

  private void connectAttributes() {
    attributes.setEffects(createEffects());
    for (EffectBuilder effectBuild : onStartScreen) {
      attributes.addEffectsOnStartScreen(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onEndScreen) {
      attributes.addEffectsOnEndScreen(effectBuild.getAttributes());
    }
    for (HoverEffectBuilder effectBuild : onHover) {
      attributes.addEffectsOnHover(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onClick) {
      attributes.addEffectsOnClick(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onFocus) {
      attributes.addEffectsOnFocus(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onLostFocus) {
      attributes.addEffectsOnLostFocus(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onGetFocus) {
      attributes.addEffectsOnGetFocus(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onActive) {
      attributes.addEffectsOnActive(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onCustom) {
      attributes.addEffectsOnCustom(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onShow) {
      attributes.addEffectsOnShow(effectBuild.getAttributes());
    }
    for (EffectBuilder effectBuild : onHide) {
      attributes.addEffectsOnHide(effectBuild.getAttributes());
    }
  }

  private ControlEffectsAttributes createEffects() {
    return new ControlEffectsAttributes();
  }
}
