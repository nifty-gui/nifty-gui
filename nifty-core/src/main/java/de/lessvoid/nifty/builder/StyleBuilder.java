package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.loaderv2.types.StyleType;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

public class StyleBuilder {
  @Nonnull
  private final Attributes styleAttributes = new Attributes();
  @Nonnull
  private final ControlAttributes attributes = new ControlAttributes();
  @Nonnull
  private final Collection<EffectBuilder> onStartScreen = new ArrayList<EffectBuilder>();
  @Nonnull
  private final Collection<EffectBuilder> onEndScreen = new ArrayList<EffectBuilder>();
  @Nonnull
  private final Collection<HoverEffectBuilder> onHover = new ArrayList<HoverEffectBuilder>();
  @Nonnull
  private final Collection<HoverEffectBuilder> onStartHover = new ArrayList<HoverEffectBuilder>();
  @Nonnull
  private final Collection<HoverEffectBuilder> onEndHover = new ArrayList<HoverEffectBuilder>();
  @Nonnull
  private final Collection<EffectBuilder> onClick = new ArrayList<EffectBuilder>();
  @Nonnull
  private final Collection<EffectBuilder> onFocus = new ArrayList<EffectBuilder>();
  @Nonnull
  private final Collection<EffectBuilder> onLostFocus = new ArrayList<EffectBuilder>();
  @Nonnull
  private final Collection<EffectBuilder> onGetFocus = new ArrayList<EffectBuilder>();
  @Nonnull
  private final Collection<EffectBuilder> onActive = new ArrayList<EffectBuilder>();
  @Nonnull
  private final Collection<EffectBuilder> onCustom = new ArrayList<EffectBuilder>();
  @Nonnull
  private final Collection<EffectBuilder> onShow = new ArrayList<EffectBuilder>();
  @Nonnull
  private final Collection<EffectBuilder> onHide = new ArrayList<EffectBuilder>();

  public enum ChildLayoutType {
    Vertical("vertical"), Horizontal("horizontal"), Center("center"), Absolute("absolute"),
    AbsoluteInside("absolute-inside"), Overlay("overlay");

    private final String layout;

    private ChildLayoutType(final String layout) {
      this.layout = layout;
    }

    public String getLayout() {
      return layout;
    }
  }

  public enum Align {
    Left("left"), Right("right"), Center("center");

    private final String align;

    private Align(final String align) {
      this.align = align;
    }

    public String getLayout() {
      return align;
    }
  }

  public enum VAlign {
    Top("top"), Bottom("bottom"), Center("center");

    private final String valign;

    private VAlign(final String valign) {
      this.valign = valign;
    }

    public String getLayout() {
      return valign;
    }
  }

  // these two attributes are applied directly to the style (<style id="this" base="another">)

  public StyleBuilder id(@Nonnull final String id) {
    styleAttributes.set("id", id);
    return this;
  }

  public StyleBuilder base(@Nonnull final String baseStyle) {
    styleAttributes.set("base", baseStyle);
    return this;
  }

  // all other attributes are applied to the attribute tag (<style><attributes .../></style>)

  public StyleBuilder name(@Nonnull final String name) {
    attributes.setName(name);
    return this;
  }

  public StyleBuilder backgroundColor(@Nonnull final String backgroundColor) {
    attributes.setBackgroundColor(backgroundColor);
    return this;
  }

  public StyleBuilder backgroundColor(@Nonnull final Color backgroundColor) {
    attributes.setBackgroundColor(backgroundColor.getColorString());
    return this;
  }

  public StyleBuilder color(@Nonnull final String color) {
    attributes.setColor(color);
    return this;
  }

  public StyleBuilder color(@Nonnull final Color color) {
    attributes.setColor(color.getColorString());
    return this;
  }

  public StyleBuilder selectionColor(@Nonnull final String color) {
    attributes.setSelectionColor(color);
    return this;
  }

  public StyleBuilder selectionColor(@Nonnull final Color color) {
    attributes.setSelectionColor(color.getColorString());
    return this;
  }

  public StyleBuilder text(@Nonnull final String text) {
    attributes.setText(text);
    return this;
  }

  public StyleBuilder backgroundImage(@Nonnull final String backgroundImage) {
    attributes.setBackgroundImage(backgroundImage);
    return this;
  }

  public StyleBuilder imageMode(@Nonnull final String imageMode) {
    attributes.setImageMode(imageMode);
    return this;
  }

  public StyleBuilder inset(@Nonnull final String inset) {
    attributes.setInset(inset);
    return this;
  }

  public StyleBuilder inputMapping(@Nonnull final String inputMapping) {
    attributes.setInputMapping(inputMapping);
    return this;
  }

  public StyleBuilder style(@Nonnull final String style) {
    attributes.setStyle(style);
    return this;
  }

  public StyleBuilder childLayout(@Nonnull final ChildLayoutType childLayout) {
    attributes.setChildLayout(childLayout.getLayout());
    return this;
  }

  public StyleBuilder childLayoutVertical() {
    return childLayout(ChildLayoutType.Vertical);
  }

  public StyleBuilder childLayoutHorizontal() {
    return childLayout(ChildLayoutType.Horizontal);
  }

  public StyleBuilder childLayoutCenter() {
    return childLayout(ChildLayoutType.Center);
  }

  public StyleBuilder childLayoutAbsolute() {
    return childLayout(ChildLayoutType.Absolute);
  }

  public StyleBuilder childLayoutOverlay() {
    return childLayout(ChildLayoutType.Overlay);
  }

  public StyleBuilder height(@Nonnull final String height) {
    attributes.setHeight(height);
    return this;
  }

  public StyleBuilder width(@Nonnull final String width) {
    attributes.setWidth(width);
    return this;
  }

  public StyleBuilder x(@Nonnull final String x) {
    attributes.setX(x);
    return this;
  }

  public StyleBuilder y(@Nonnull final String y) {
    attributes.setY(y);
    return this;
  }

  public StyleBuilder childClip(final boolean childClip) {
    attributes.setChildClip(String.valueOf(childClip));
    return this;
  }

  public StyleBuilder renderOrder(final int renderOrder) {
    attributes.setRenderOrder(renderOrder);
    return this;
  }

  public StyleBuilder visible(final boolean visible) {
    attributes.setVisible(String.valueOf(visible));
    return this;
  }

  public StyleBuilder focusable(final boolean focusable) {
    attributes.setFocusable(String.valueOf(focusable));
    return this;
  }

  public StyleBuilder textHAlign(@Nonnull final Align align) {
    attributes.set("textHAlign", align.getLayout());
    return this;
  }

  public StyleBuilder textHAlignLeft() {
    return textHAlign(Align.Left);
  }

  public StyleBuilder textHAlignRight() {
    return textHAlign(Align.Right);
  }

  public StyleBuilder textHAlignCenter() {
    return textHAlign(Align.Center);
  }

  public StyleBuilder textVAlign(@Nonnull final VAlign valign) {
    attributes.set("textVAlign", valign.getLayout());
    return this;
  }

  public StyleBuilder textVAlignTop() {
    return textVAlign(VAlign.Top);
  }

  public StyleBuilder textVAlignBottom() {
    return textVAlign(VAlign.Bottom);
  }

  public StyleBuilder textVAlignCenter() {
    return textVAlign(VAlign.Center);
  }

  public StyleBuilder align(@Nonnull final Align align) {
    attributes.setAlign(align.getLayout());
    return this;
  }

  public StyleBuilder alignLeft() {
    return align(Align.Left);
  }

  public StyleBuilder alignRight() {
    return align(Align.Right);
  }

  public StyleBuilder alignCenter() {
    return align(Align.Center);
  }

  public StyleBuilder valign(@Nonnull final VAlign valign) {
    attributes.setVAlign(valign.getLayout());
    return this;
  }

  public StyleBuilder valignTop() {
    return valign(VAlign.Top);
  }

  public StyleBuilder valignBottom() {
    return valign(VAlign.Bottom);
  }

  public StyleBuilder valignCenter() {
    return valign(VAlign.Center);
  }

  public StyleBuilder visibleToMouse(final boolean visibleToMouse) {
    attributes.setVisibleToMouse(String.valueOf(visibleToMouse));
    return this;
  }

  public StyleBuilder visibleToMouse() {
    return visibleToMouse(true);
  }

  public StyleBuilder invisibleToMouse() {
    return visibleToMouse(false);
  }

  public StyleBuilder font(@Nonnull final String font) {
    attributes.setFont(font);
    return this;
  }

  public StyleBuilder filename(@Nonnull final String filename) {
    attributes.setFilename(filename);
    return this;
  }

  public StyleBuilder padding(@Nonnull final String padding) {
    attributes.setPadding(padding);
    return this;
  }

  public StyleBuilder paddingLeft(@Nonnull final String padding) {
    attributes.setPaddingLeft(padding);
    return this;
  }

  public StyleBuilder paddingRight(@Nonnull final String padding) {
    attributes.setPaddingRight(padding);
    return this;
  }

  public StyleBuilder paddingTop(@Nonnull final String padding) {
    attributes.setPaddingTop(padding);
    return this;
  }

  public StyleBuilder paddingBottom(@Nonnull final String padding) {
    attributes.setPaddingBottom(padding);
    return this;
  }

  public StyleBuilder margin(@Nonnull final String margin) {
    attributes.setMargin(margin);
    return this;
  }

  public StyleBuilder marginLeft(@Nonnull final String margin) {
    attributes.setMarginLeft(margin);
    return this;
  }

  public StyleBuilder marginRight(@Nonnull final String margin) {
    attributes.setMarginRight(margin);
    return this;
  }

  public StyleBuilder marginTop(@Nonnull final String margin) {
    attributes.setMarginTop(margin);
    return this;
  }

  public StyleBuilder marginBottom(@Nonnull final String margin) {
    attributes.setMarginBottom(margin);
    return this;
  }

  public StyleBuilder set(@Nonnull final String key, @Nonnull final String value) {
    attributes.set(key, value);
    return this;
  }

  public StyleBuilder onStartScreenEffect(final EffectBuilder onStartScreenEffect) {
    onStartScreen.add(onStartScreenEffect);
    return this;
  }

  public StyleBuilder onEndScreenEffect(final EffectBuilder onEndScreenEffect) {
    onEndScreen.add(onEndScreenEffect);
    return this;
  }

  public StyleBuilder onHoverEffect(final HoverEffectBuilder onHoverEffect) {
    onHover.add(onHoverEffect);
    return this;
  }

  public StyleBuilder onStartHoverEffect(final HoverEffectBuilder onStartHoverEffect) {
    onStartHover.add(onStartHoverEffect);
    return this;
  }

  public StyleBuilder onEndHoverEffect(final HoverEffectBuilder onEndHoverEffect) {
    onEndHover.add(onEndHoverEffect);
    return this;
  }

  public StyleBuilder onClickEffect(final EffectBuilder onClickEffect) {
    onClick.add(onClickEffect);
    return this;
  }

  public StyleBuilder onFocusEffect(final EffectBuilder onFocusEffect) {
    onFocus.add(onFocusEffect);
    return this;
  }

  public StyleBuilder onLostFocusEffect(final EffectBuilder onLostFocusEffect) {
    onLostFocus.add(onLostFocusEffect);
    return this;
  }

  public StyleBuilder onGetFocusEffect(final EffectBuilder onGetFocusEffect) {
    onGetFocus.add(onGetFocusEffect);
    return this;
  }

  public StyleBuilder onActiveEffect(final EffectBuilder onActiveEffect) {
    onActive.add(onActiveEffect);
    return this;
  }

  public StyleBuilder onShowEffect(final EffectBuilder onShowEffect) {
    onShow.add(onShowEffect);
    return this;
  }

  public StyleBuilder onHideEffect(final EffectBuilder onHideEffect) {
    onHide.add(onHideEffect);
    return this;
  }

  public StyleBuilder onCustomEffect(final EffectBuilder onCustomEffect) {
    onCustom.add(onCustomEffect);
    return this;
  }

  public StyleBuilder interactOnClick(@Nonnull String method) {
    attributes.setInteractOnClick(method);
    return this;
  }
  public StyleBuilder interactOnMultiClick(@Nonnull String method){
    attributes.setIneractOnMultiClick(method);
    return this;
  }
  public StyleBuilder interactOnRelease(@Nonnull final String onRelease) {
    attributes.setInteractOnRelease(onRelease);
    return this;
  }

  public StyleBuilder interactOnMouseOver(@Nonnull final String onMouseOver) {
    attributes.setInteractOnMouseOver(onMouseOver);
    return this;
  }

  public StyleBuilder interactOnClickRepeat(@Nonnull final String onClickRepeat) {
    attributes.setInteractOnClickRepeat(onClickRepeat);
    return this;
  }

  public StyleBuilder interactOnClickMouseMove(@Nonnull final String onClickMouseMove) {
    attributes.setInteractOnClickMouseMove(onClickMouseMove);
    return this;
  }

  public StyleBuilder interactOnClickAlternateKey(@Nonnull final String onClickAlternateKey) {
    attributes.setInteractOnClickAlternateKey(onClickAlternateKey);
    return this;
  }

  @Nonnull
  public String percentage(final int percentage) {
    return Integer.toString(percentage) + "%";
  }

  @Nonnull
  public String pixels(final int px) {
    return Integer.toString(px) + "px";
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
    for (HoverEffectBuilder effectBuild : onStartHover) {
      attributes.addEffectsOnStartHover(effectBuild.getAttributes());
    }
    for (HoverEffectBuilder effectBuild : onEndHover) {
      attributes.addEffectsOnEndHover(effectBuild.getAttributes());
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

  @Nonnull
  private ControlEffectsAttributes createEffects() {
    return new ControlEffectsAttributes();
  }

  public void build(@Nonnull final Nifty nifty) {
    connectAttributes();

    StyleType style = attributes.createStyleType(styleAttributes);
    nifty.registerStyle(style);
  }
}
