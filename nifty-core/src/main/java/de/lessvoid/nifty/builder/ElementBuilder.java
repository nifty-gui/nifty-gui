package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlInteractAttributes;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.EnumStorage;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.factories.CollectionFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class ElementBuilder {
  @Nonnull
  private final ControlAttributes attributes;
  @Nonnull
  private final ControlInteractAttributes interactAttributes;
  @Nonnull
  protected final List<ElementBuilder> elementBuilders;

  @Nonnull
  private final EnumStorage<EffectEventId, Collection<EffectBuilder>> effectBuilders;

  @Nullable
  private Controller controller;

  protected ElementBuilder(@Nonnull final ControlAttributes attributes) {
    elementBuilders = new ArrayList<ElementBuilder>();
    interactAttributes = new ControlInteractAttributes();
    effectBuilders = new EnumStorage<EffectEventId, Collection<EffectBuilder>>(
        EffectEventId.class, CollectionFactory.<EffectBuilder>getArrayListInstance());

    this.attributes = attributes;
    attributes.setInteract(interactAttributes);
  }

  public enum ChildLayoutType {
    Vertical("vertical"),
    Horizontal("horizontal"),
    Center("center"),
    Absolute("absolute"),
    AbsoluteInside("absolute-inside"),
    Overlay("overlay");

    @Nonnull
    private final String layout;

    private ChildLayoutType(@Nonnull final String layout) {
      this.layout = layout;
    }

    @Nonnull
    public String getLayout() {
      return layout;
    }
  }

  public enum Align {
    Left("left"), Right("right"), Center("center");

    @Nonnull
    private final String align;

    private Align(@Nonnull final String align) {
      this.align = align;
    }

    @Nonnull
    public String getLayout() {
      return align;
    }
  }

  public enum VAlign {
    Top("top"), Bottom("bottom"), Center("center");

    @Nonnull
    private final String valign;

    private VAlign(@Nonnull final String valign) {
      this.valign = valign;
    }

    @Nonnull
    public String getLayout() {
      return valign;
    }
  }

  public void id(@Nonnull final String id) {
    attributes.setId(id);
  }

  @Nullable
  public String getId() {
    return attributes.getId();
  }

  public boolean isAutoId() {
    return attributes.isAutoId();
  }

  public ElementBuilder name(@Nonnull final String name) {
    attributes.setName(name);
    return this;
  }

  public ElementBuilder backgroundColor(@Nonnull final String backgroundColor) {
    attributes.setBackgroundColor(backgroundColor);
    return this;
  }

  public ElementBuilder backgroundColor(@Nonnull final Color backgroundColor) {
    attributes.setBackgroundColor(backgroundColor.getColorString());
    return this;
  }

  public ElementBuilder controller(@Nonnull final Controller controller) {
    attributes.set("controller", controller.getClass().getName());
    this.controller = controller;
    return this;
  }

  public ElementBuilder controller(@Nonnull final String controllerClass) {
    attributes.set("controller", controllerClass);
    return this;
  }

  public ElementBuilder color(@Nonnull final String color) {
    attributes.setColor(color);
    return this;
  }

  public ElementBuilder color(@Nonnull final Color color) {
    attributes.setColor(color.getColorString());
    return this;
  }

  public ElementBuilder selectionColor(@Nonnull final String color) {
    attributes.setSelectionColor(color);
    return this;
  }

  public ElementBuilder selectionColor(@Nonnull final Color color) {
    attributes.setSelectionColor(color.getColorString());
    return this;
  }

  public ElementBuilder text(@Nonnull final String text) {
    attributes.setText(text);
    return this;
  }

  public ElementBuilder backgroundImage(@Nonnull final String backgroundImage) {
    attributes.setBackgroundImage(backgroundImage);
    return this;
  }

  public ElementBuilder imageMode(@Nonnull final String imageMode) {
    attributes.setImageMode(imageMode);
    return this;
  }

  public ElementBuilder inset(@Nonnull final String inset) {
    attributes.setInset(inset);
    return this;
  }

  public ElementBuilder inputMapping(@Nonnull final String inputMapping) {
    attributes.setInputMapping(inputMapping);
    return this;
  }

  public ElementBuilder style(@Nonnull final String style) {
    attributes.setStyle(style);
    return this;
  }

  public ElementBuilder childLayout(@Nonnull final ChildLayoutType childLayout) {
    attributes.setChildLayout(childLayout.getLayout());
    return this;
  }

  public ElementBuilder childLayoutVertical() {
    return childLayout(ChildLayoutType.Vertical);
  }

  public ElementBuilder childLayoutHorizontal() {
    return childLayout(ChildLayoutType.Horizontal);
  }

  public ElementBuilder childLayoutCenter() {
    return childLayout(ChildLayoutType.Center);
  }

  public ElementBuilder childLayoutAbsolute() {
    return childLayout(ChildLayoutType.Absolute);
  }

  public ElementBuilder childLayoutAbsoluteInside() {
    return childLayout(ChildLayoutType.AbsoluteInside);
  }

  public ElementBuilder childLayoutOverlay() {
    return childLayout(ChildLayoutType.Overlay);
  }

  public ElementBuilder height(@Nonnull final String height) {
    attributes.setHeight(height);
    return this;
  }

  public ElementBuilder width(@Nonnull final String width) {
    attributes.setWidth(width);
    return this;
  }

  public ElementBuilder height(@Nonnull final SizeValue height) {
    attributes.setHeight(height.getValueAsString());
    return this;
  }

  public ElementBuilder width(@Nonnull final SizeValue width) {
    attributes.setWidth(width.getValueAsString());
    return this;
  }

  public ElementBuilder x(@Nonnull final String x) {
    attributes.setX(x);
    return this;
  }

  public ElementBuilder y(@Nonnull final String y) {
    attributes.setY(y);
    return this;
  }

  public ElementBuilder x(@Nonnull final SizeValue x) {
    attributes.setX(x.getValueAsString());
    return this;
  }

  public ElementBuilder y(@Nonnull final SizeValue y) {
    attributes.setY(y.getValueAsString());
    return this;
  }

  public ElementBuilder childClip(final boolean childClip) {
    attributes.setChildClip(String.valueOf(childClip));
    return this;
  }

  public ElementBuilder renderOrder(final int renderOrder) {
    attributes.setRenderOrder(renderOrder);
    return this;
  }

  public ElementBuilder visible(final boolean visible) {
    attributes.setVisible(String.valueOf(visible));
    return this;
  }

  public ElementBuilder focusable(final boolean focusable) {
    attributes.setFocusable(String.valueOf(focusable));
    return this;
  }

  public ElementBuilder focusableInsertBeforeElementId(@Nonnull final String focusableInsertBeforeElementId) {
    attributes.setFocusableInsertBeforeElementId(focusableInsertBeforeElementId);
    return this;
  }

  public ElementBuilder textHAlign(@Nonnull final Align align) {
    attributes.set("textHAlign", align.getLayout());
    return this;
  }

  public ElementBuilder textHAlignLeft() {
    return textHAlign(Align.Left);
  }

  public ElementBuilder textHAlignRight() {
    return textHAlign(Align.Right);
  }

  public ElementBuilder textHAlignCenter() {
    return textHAlign(Align.Center);
  }

  public ElementBuilder textVAlign(@Nonnull final VAlign valign) {
    attributes.set("textVAlign", valign.getLayout());
    return this;
  }

  public ElementBuilder textVAlignTop() {
    return textVAlign(VAlign.Top);
  }

  public ElementBuilder textVAlignBottom() {
    return textVAlign(VAlign.Bottom);
  }

  public ElementBuilder textVAlignCenter() {
    return textVAlign(VAlign.Center);
  }

  public ElementBuilder align(@Nonnull final Align align) {
    attributes.setAlign(align.getLayout());
    return this;
  }

  public ElementBuilder alignLeft() {
    return align(Align.Left);
  }

  public ElementBuilder alignRight() {
    return align(Align.Right);
  }

  public ElementBuilder alignCenter() {
    return align(Align.Center);
  }

  public ElementBuilder valign(@Nonnull final VAlign valign) {
    attributes.setVAlign(valign.getLayout());
    return this;
  }

  public ElementBuilder valignTop() {
    return valign(VAlign.Top);
  }

  public ElementBuilder valignBottom() {
    return valign(VAlign.Bottom);
  }

  public ElementBuilder valignCenter() {
    return valign(VAlign.Center);
  }

  public ElementBuilder visibleToMouse(final boolean visibleToMouse) {
    attributes.setVisibleToMouse(String.valueOf(visibleToMouse));
    return this;
  }

  public ElementBuilder visibleToMouse() {
    return visibleToMouse(true);
  }

  public ElementBuilder invisibleToMouse() {
    return visibleToMouse(false);
  }

  public ElementBuilder font(@Nonnull final String font) {
    attributes.setFont(font);
    return this;
  }

  public ElementBuilder filename(@Nonnull final String filename) {
    attributes.setFilename(filename);
    return this;
  }

  public ElementBuilder padding(@Nonnull final String padding) {
    attributes.setPadding(padding);
    return this;
  }

  public ElementBuilder paddingLeft(@Nonnull final String padding) {
    attributes.setPaddingLeft(padding);
    return this;
  }

  public ElementBuilder paddingRight(@Nonnull final String padding) {
    attributes.setPaddingRight(padding);
    return this;
  }

  public ElementBuilder paddingTop(@Nonnull final String padding) {
    attributes.setPaddingTop(padding);
    return this;
  }

  public ElementBuilder paddingBottom(@Nonnull final String padding) {
    attributes.setPaddingBottom(padding);
    return this;
  }

  public ElementBuilder margin(@Nonnull final String margin) {
    attributes.setMargin(margin);
    return this;
  }

  public ElementBuilder marginLeft(@Nonnull final String margin) {
    attributes.setMarginLeft(margin);
    return this;
  }

  public ElementBuilder marginRight(@Nonnull final String margin) {
    attributes.setMarginRight(margin);
    return this;
  }

  public ElementBuilder marginTop(@Nonnull final String margin) {
    attributes.setMarginTop(margin);
    return this;
  }

  public ElementBuilder marginBottom(@Nonnull final String margin) {
    attributes.setMarginBottom(margin);
    return this;
  }

  public ElementBuilder set(@Nonnull final String key, @Nonnull final String value) {
    attributes.set(key, value);
    return this;
  }

  @Nullable
  public String get(@Nonnull final String key) {
    return attributes.get(key);
  }

  public ElementBuilder panel(@Nonnull final PanelBuilder panelBuilder) {
    elementBuilders.add(panelBuilder);
    return this;
  }

  public ElementBuilder text(@Nonnull final TextBuilder textBuilder) {
    elementBuilders.add(textBuilder);
    return this;
  }

  public ElementBuilder image(@Nonnull final ImageBuilder imageBuilder) {
    elementBuilders.add(imageBuilder);
    return this;
  }

  public ElementBuilder control(@Nonnull final ControlBuilder controlBuilder) {
    elementBuilders.add(controlBuilder);
    return this;
  }

  public ElementBuilder onEffect(@Nonnull final EffectEventId eventId, @Nonnull final EffectBuilder builder) {
    effectBuilders.get(eventId).add(builder);
    return this;
  }

  public ElementBuilder onStartScreenEffect(@Nonnull final EffectBuilder onStartScreenEffect) {
    return onEffect(EffectEventId.onStartScreen, onStartScreenEffect);
  }

  public ElementBuilder onEndScreenEffect(@Nonnull final EffectBuilder onEndScreenEffect) {
    return onEffect(EffectEventId.onEndScreen, onEndScreenEffect);
  }

  public ElementBuilder onHoverEffect(@Nonnull final HoverEffectBuilder onHoverEffect) {
    return onEffect(EffectEventId.onHover, onHoverEffect);
  }

  public ElementBuilder onStartHoverEffect(@Nonnull final HoverEffectBuilder onStartHoverEffect) {
    return onEffect(EffectEventId.onStartHover, onStartHoverEffect);
  }

  public ElementBuilder onEndHoverEffect(@Nonnull final HoverEffectBuilder onEndHoverEffect) {
    return onEffect(EffectEventId.onEndHover, onEndHoverEffect);
  }

  public ElementBuilder onClickEffect(@Nonnull final EffectBuilder onClickEffect) {
    return onEffect(EffectEventId.onClick, onClickEffect);
  }

  public ElementBuilder onFocusEffect(@Nonnull final EffectBuilder onFocusEffect) {
    return onEffect(EffectEventId.onFocus, onFocusEffect);
  }

  public ElementBuilder onLostFocusEffect(@Nonnull final EffectBuilder onLostFocusEffect) {
    return onEffect(EffectEventId.onLostFocus, onLostFocusEffect);
  }

  public ElementBuilder onGetFocusEffect(@Nonnull final EffectBuilder onGetFocusEffect) {
    return onEffect(EffectEventId.onGetFocus, onGetFocusEffect);
  }

  public ElementBuilder onActiveEffect(@Nonnull final EffectBuilder onActiveEffect) {
    return onEffect(EffectEventId.onActive, onActiveEffect);
  }

  public ElementBuilder onShowEffect(@Nonnull final EffectBuilder onShowEffect) {
    return onEffect(EffectEventId.onShow, onShowEffect);
  }

  public ElementBuilder onHideEffect(@Nonnull final EffectBuilder onHideEffect) {
    return onEffect(EffectEventId.onHide, onHideEffect);
  }

  public ElementBuilder onCustomEffect(@Nonnull final EffectBuilder onCustomEffect) {
    return onEffect(EffectEventId.onCustom, onCustomEffect);
  }
  
  public ElementBuilder interactOnClick(@Nonnull String onClick) {
    interactAttributes.setOnClick(onClick);
    return this;
  }
  
  public ElementBuilder interactOnClickRepeat(@Nonnull final String onClickRepeat) {
    interactAttributes.setOnClickRepeat(onClickRepeat);
    return this;
  }
  
  public ElementBuilder interactOnRelease(@Nonnull final String onRelease) {
    interactAttributes.setOnRelease(onRelease);
    return this;
  }
  
  public ElementBuilder interactOnClickMouseMove(@Nonnull final String onClickMouseMove) {
    interactAttributes.setOnClickMouseMove(onClickMouseMove);
    return this;
  }
  
  public ElementBuilder interactOnMultiClick(@Nonnull String onMultiClick){
    interactAttributes.setOnMultiClick(onMultiClick);
    return this;
  }
  
  public ElementBuilder interactOnPrimaryClick(@Nonnull String onPrimaryClick) {
    interactAttributes.setOnPrimaryClick(onPrimaryClick);
    return this;
  }
  
  public ElementBuilder interactOnPrimaryClickRepeat(@Nonnull final String onPrimaryClickRepeat) {
    interactAttributes.setOnPrimaryClickRepeat(onPrimaryClickRepeat);
    return this;
  }
  
  public ElementBuilder interactOnPrimaryRelease(@Nonnull final String onPrimaryRelease) {
    interactAttributes.setOnPrimaryRelease(onPrimaryRelease);
    return this;
  }
  
  public ElementBuilder interactOnPrimaryClickMouseMove(@Nonnull final String onPrimaryClickMouseMove) {
    interactAttributes.setOnPrimaryClickMouseMove(onPrimaryClickMouseMove);
    return this;
  }
  
  public ElementBuilder interactOnPrimaryMultiClick(@Nonnull String onPrimaryMultiClick){
    interactAttributes.setOnPrimaryMultiClick(onPrimaryMultiClick);
    return this;
  }
  
  public ElementBuilder interactOnSecondaryClick(@Nonnull String onSecondaryClick) {
    interactAttributes.setOnSecondaryClick(onSecondaryClick);
    return this;
  }
  
  public ElementBuilder interactOnSecondaryClickRepeat(@Nonnull final String onSecondaryClickRepeat) {
    interactAttributes.setOnSecondaryClickRepeat(onSecondaryClickRepeat);
    return this;
  }
  
  public ElementBuilder interactOnSecondaryRelease(@Nonnull final String onSecondaryRelease) {
    interactAttributes.setOnSecondaryRelease(onSecondaryRelease);
    return this;
  }
  
  public ElementBuilder interactOnSecondaryClickMouseMove(@Nonnull final String onSecondaryClickMouseMove) {
    interactAttributes.setOnSecondaryClickMouseMove(onSecondaryClickMouseMove);
    return this;
  }
  
  public ElementBuilder interactOnSecondaryMultiClick(@Nonnull String onSecondaryMultiClick){
    interactAttributes.setOnSecondaryMultiClick(onSecondaryMultiClick);
    return this;
  }

  public ElementBuilder interactOnTertiaryClick(@Nonnull String onTertiaryClick) {
    interactAttributes.setOnTertiaryClick(onTertiaryClick);
    return this;
  }

  public ElementBuilder interactOnTertiaryClickRepeat(@Nonnull final String onTertiaryClickRepeat) {
    interactAttributes.setOnTertiaryClickRepeat(onTertiaryClickRepeat);
    return this;
  }
  
  public ElementBuilder interactOnTertiaryRelease(@Nonnull final String onTertiaryRelease) {
    interactAttributes.setOnTertiaryRelease(onTertiaryRelease);
    return this;
  }

  public ElementBuilder interactOnTertiaryClickMouseMove(@Nonnull final String onTertiaryClickMouseMove) {
    interactAttributes.setOnTertiaryClickMouseMove(onTertiaryClickMouseMove);
    return this;
  }

  public ElementBuilder interactOnTertiaryMultiClick(@Nonnull String onTertiaryMultiClick){
    interactAttributes.setOnTertiaryMultiClick(onTertiaryMultiClick);
    return this;
  }
  
  public ElementBuilder interactOnMouseOver(@Nonnull final String onMouseOver) {
    interactAttributes.setOnMouseOver(onMouseOver);
    return this;
  }

  public ElementBuilder interactOnMouseWheel(@Nonnull final String onMouseWheel) {
    interactAttributes.setOnMouseWheel(onMouseWheel);
    return this;
  }

  public ElementBuilder interactOnClickAlternateKey(@Nonnull final String onClickAlternateKey) {
    interactAttributes.setOnClickAlternateKey(onClickAlternateKey);
    return this;
  }

  public List<ElementBuilder> getElementBuilders() {
    return Collections.unmodifiableList(elementBuilders);
  }

  /**
   * Build a element
   *
   * @return the element created
   */
  public Element build(@Nonnull final Element parent) {
    ElementType type = buildElementType();
    Element result = parent.getNifty().createElementFromType(parent.getScreen(), parent, type);
    parent.layoutElements();
    return result;
  }

  /**
   * Build an element in a specified position in parent element list
   *
   * @return the Element created
   */
  @Nonnull
  public Element build(@Nonnull final Element parent, final int index) {
    Screen screen = parent.getScreen();
    ElementType type = buildElementType();
    Element result = parent.getNifty().createElementFromType(screen, parent, type, index);
    screen.layoutLayers();
    return result;
  }

  /**
   * Build an element after a element in parent children list
   *
   * @return the Element created
   */
  @Nonnull
  public Element build(@Nonnull final Element parent, @Nullable final Element before) {
    //find the index of 'before' element
    List<Element> parentList = parent.getChildren();
    int index = parentList.size();
    for (int i = 0; i < parentList.size(); i++) {
      if (parentList.get(i).equals(before)) {
        index = i;
        break;
      }
    }

    return this.build(parent, index);
  }

  /**
   * Build a element
   *
   * @return the element created
   */
  @Deprecated
  public Element build(@Nonnull final Nifty nifty, @Nonnull final Screen screen, @Nonnull final Element parent) {
    return build(parent);
  }

  /**
   * Build an element in a specified position in parent element list
   *
   * @return the Element created
   */
  @Deprecated
  @Nonnull
  public Element build(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent,
      final int index) {
    return build(parent, index);
  }

  /**
   * Build an element after a element in parent children list
   *
   * @return the Element created
   */
  @Deprecated
  @Nonnull
  public Element build(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent,
      @Nullable final Element before) {
    return build(parent, before);
  }

  /**
   * This method is called whenever we need the ElementType instead of a real
   * Element instance. This is currently used for ControlDefinition and Popup
   * registering dynamically from Java using the Builder pattern.
   * <p/>
   * It is not used for the general Java builder call that generates real instances.
   *
   * @return the ElementType representation for this ElementBuilder
   */
  @Nullable
  public ElementType buildElementType() {
    connectAttributes();
    ElementType thisType = attributes.createType();
    if (thisType == null) {
      return null;
    }

    // this is quite complicated: when we use the builder stuff then all of the
    // builders will create automatically an id for the element it builds. this
    // is a required feature when the builders are used to create actual elements.
    //
    // in this case here we're creating a type and not an actual element instance.
    // this is used for instance in controldefinitions. therefore we need to make
    // sure that the automatically generated id is being removed again. otherwise
    // we would end up with controldefinitions with ids. when we later use these
    // control definitions in multiple controls these ids will be reused which
    // could cause trouble when we have the same id multiple times.
    //
    // so here we make sure that when we have an automatically generated id
    // that we remove it again.
    if (attributes.isAutoId()) {
      thisType.getAttributes().remove("id");
    }

    attributes.connect(thisType);
    thisType.attachController(controller);
    for (int i = 0; i < elementBuilders.size(); i++) {
      ElementType newType = elementBuilders.get(i).buildElementType();
      if (newType != null) {
        thisType.addElementType(newType);
      }
    }

    return thisType;
  }

  private void connectAttributes() {
    attributes.setEffects(createEffects());
    for (EffectEventId eventId : EffectEventId.values()) {
      if (effectBuilders.isSet(eventId)) {
        Collection<EffectBuilder> builders = effectBuilders.get(eventId);
        for (EffectBuilder builder : builders) {
          attributes.addEffects(eventId, builder.getAttributes());
        }
      }
    }
  }

  @Nonnull
  private ControlEffectsAttributes createEffects() {
    return new ControlEffectsAttributes();
  }
}
