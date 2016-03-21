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

  public void name(@Nonnull final String name) {
    attributes.setName(name);
  }

  public void backgroundColor(@Nonnull final String backgroundColor) {
    attributes.setBackgroundColor(backgroundColor);
  }

  public void backgroundColor(@Nonnull final Color backgroundColor) {
    attributes.setBackgroundColor(backgroundColor.getColorString());
  }

  public void controller(@Nonnull final Controller controller) {
    attributes.set("controller", controller.getClass().getName());
  }

  public void controller(@Nonnull final String controllerClass) {
    attributes.set("controller", controllerClass);
  }

  public void color(@Nonnull final String color) {
    attributes.setColor(color);
  }

  public void color(@Nonnull final Color color) {
    attributes.setColor(color.getColorString());
  }

  public void selectionColor(@Nonnull final String color) {
    attributes.setSelectionColor(color);
  }

  public void selectionColor(@Nonnull final Color color) {
    attributes.setSelectionColor(color.getColorString());
  }

  public void text(@Nonnull final String text) {
    attributes.setText(text);
  }

  public void backgroundImage(@Nonnull final String backgroundImage) {
    attributes.setBackgroundImage(backgroundImage);
  }

  public void imageMode(@Nonnull final String imageMode) {
    attributes.setImageMode(imageMode);
  }

  public void inset(@Nonnull final String inset) {
    attributes.setInset(inset);
  }

  public void inputMapping(@Nonnull final String inputMapping) {
    attributes.setInputMapping(inputMapping);
  }

  public void style(@Nonnull final String style) {
    attributes.setStyle(style);
  }

  public void childLayout(@Nonnull final ChildLayoutType childLayout) {
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

  public void childLayoutAbsoluteInside() {
    childLayout(ChildLayoutType.AbsoluteInside);
  }

  public void childLayoutOverlay() {
    childLayout(ChildLayoutType.Overlay);
  }

  public void height(@Nonnull final String height) {
    attributes.setHeight(height);
  }

  public void width(@Nonnull final String width) {
    attributes.setWidth(width);
  }

  public void height(@Nonnull final SizeValue height) {
    attributes.setHeight(height.getValueAsString());
  }

  public void width(@Nonnull final SizeValue width) {
    attributes.setWidth(width.getValueAsString());
  }

  public void x(@Nonnull final String x) {
    attributes.setX(x);
  }

  public void y(@Nonnull final String y) {
    attributes.setY(y);
  }

  public void x(@Nonnull final SizeValue x) {
    attributes.setX(x.getValueAsString());
  }

  public void y(@Nonnull final SizeValue y) {
    attributes.setY(y.getValueAsString());
  }

  public void childClip(final boolean childClip) {
    attributes.setChildClip(String.valueOf(childClip));
  }

  public void renderOrder(final int renderOrder) {
    attributes.setRenderOrder(renderOrder);
  }

  public void visible(final boolean visible) {
    attributes.setVisible(String.valueOf(visible));
  }

  public void focusable(final boolean focusable) {
    attributes.setFocusable(String.valueOf(focusable));
  }

  public void focusableInsertBeforeElementId(@Nonnull final String focusableInsertBeforeElementId) {
    attributes.setFocusableInsertBeforeElementId(focusableInsertBeforeElementId);
  }

  public void textHAlign(@Nonnull final Align align) {
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

  public void textVAlign(@Nonnull final VAlign valign) {
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

  public void align(@Nonnull final Align align) {
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

  public void valign(@Nonnull final VAlign valign) {
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

  public void font(@Nonnull final String font) {
    attributes.setFont(font);
  }

  public void filename(@Nonnull final String filename) {
    attributes.setFilename(filename);
  }

  public void padding(@Nonnull final String padding) {
    attributes.setPadding(padding);
  }

  public void paddingLeft(@Nonnull final String padding) {
    attributes.setPaddingLeft(padding);
  }

  public void paddingRight(@Nonnull final String padding) {
    attributes.setPaddingRight(padding);
  }

  public void paddingTop(@Nonnull final String padding) {
    attributes.setPaddingTop(padding);
  }

  public void paddingBottom(@Nonnull final String padding) {
    attributes.setPaddingBottom(padding);
  }

  public void margin(@Nonnull final String margin) {
    attributes.setMargin(margin);
  }

  public void marginLeft(@Nonnull final String margin) {
    attributes.setMarginLeft(margin);
  }

  public void marginRight(@Nonnull final String margin) {
    attributes.setMarginRight(margin);
  }

  public void marginTop(@Nonnull final String margin) {
    attributes.setMarginTop(margin);
  }

  public void marginBottom(@Nonnull final String margin) {
    attributes.setMarginBottom(margin);
  }

  public void set(@Nonnull final String key, @Nonnull final String value) {
    attributes.set(key, value);
  }

  @Nullable
  public String get(@Nonnull final String key) {
    return attributes.get(key);
  }

  public void panel(@Nonnull final PanelBuilder panelBuilder) {
    elementBuilders.add(panelBuilder);
  }

  public void text(@Nonnull final TextBuilder textBuilder) {
    elementBuilders.add(textBuilder);
  }

  public void image(@Nonnull final ImageBuilder imageBuilder) {
    elementBuilders.add(imageBuilder);
  }

  public void control(@Nonnull final ControlBuilder controlBuilder) {
    elementBuilders.add(controlBuilder);
  }

  public void onEffect(@Nonnull final EffectEventId eventId, @Nonnull final EffectBuilder builder) {
    effectBuilders.get(eventId).add(builder);
  }

  public void onStartScreenEffect(@Nonnull final EffectBuilder onStartScreenEffect) {
    onEffect(EffectEventId.onStartScreen, onStartScreenEffect);
  }

  public void onEndScreenEffect(@Nonnull final EffectBuilder onEndScreenEffect) {
    onEffect(EffectEventId.onEndScreen, onEndScreenEffect);
  }

  public void onHoverEffect(@Nonnull final HoverEffectBuilder onHoverEffect) {
    onEffect(EffectEventId.onHover, onHoverEffect);
  }

  public void onStartHoverEffect(@Nonnull final HoverEffectBuilder onStartHoverEffect) {
    onEffect(EffectEventId.onStartHover, onStartHoverEffect);
  }

  public void onEndHoverEffect(@Nonnull final HoverEffectBuilder onEndHoverEffect) {
    onEffect(EffectEventId.onEndHover, onEndHoverEffect);
  }

  public void onClickEffect(@Nonnull final EffectBuilder onClickEffect) {
    onEffect(EffectEventId.onClick, onClickEffect);
  }

  public void onFocusEffect(@Nonnull final EffectBuilder onFocusEffect) {
    onEffect(EffectEventId.onFocus, onFocusEffect);
  }

  public void onLostFocusEffect(@Nonnull final EffectBuilder onLostFocusEffect) {
    onEffect(EffectEventId.onLostFocus, onLostFocusEffect);
  }

  public void onGetFocusEffect(@Nonnull final EffectBuilder onGetFocusEffect) {
    onEffect(EffectEventId.onGetFocus, onGetFocusEffect);
  }

  public void onActiveEffect(@Nonnull final EffectBuilder onActiveEffect) {
    onEffect(EffectEventId.onActive, onActiveEffect);
  }

  public void onShowEffect(@Nonnull final EffectBuilder onShowEffect) {
    onEffect(EffectEventId.onShow, onShowEffect);
  }

  public void onHideEffect(@Nonnull final EffectBuilder onHideEffect) {
    onEffect(EffectEventId.onHide, onHideEffect);
  }

  public void onCustomEffect(@Nonnull final EffectBuilder onCustomEffect) {
    onEffect(EffectEventId.onCustom, onCustomEffect);
  }

  public void interactOnClick(@Nonnull String method) {
    interactAttributes.setOnClick(method);
  }

  public void interactOnMultiClick(@Nonnull String method){
    interactAttributes.setOnMultiClick(method);
  }
  public void interactOnRelease(@Nonnull final String onRelease) {
    interactAttributes.setOnRelease(onRelease);
  }

  public void interactOnMouseOver(@Nonnull final String onMouseOver) {
    interactAttributes.setOnMouseOver(onMouseOver);
  }

  public void interactOnClickRepeat(@Nonnull final String onClickRepeat) {
    interactAttributes.setOnClickRepeat(onClickRepeat);
  }

  public void interactOnClickMouseMove(@Nonnull final String onClickMouseMove) {
    interactAttributes.setOnClickMouseMove(onClickMouseMove);
  }

  public void interactOnClickAlternateKey(@Nonnull final String onClickAlternateKey) {
    interactAttributes.setOnClickAlternateKey(onClickAlternateKey);
  }

  public List<ElementBuilder> getElementBuilders() {
    return Collections.unmodifiableList(elementBuilders);
  }

  /**
   * Build a element
   *
   * @return the element created
   */
  public Element build(@Nonnull final Nifty nifty, @Nonnull final Screen screen, @Nonnull final Element parent) {
    ElementType type = buildElementType();
    Element result = nifty.createElementFromType(screen, parent, type);
    parent.layoutElements();
    return result;
  }

  /**
   * Build an element in a specified position in parent element list
   *
   * @return the Element created
   */
  @Nonnull
  public Element build(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent,
      final int index) {
    ElementType type = buildElementType();
    Element result = nifty.createElementFromType(screen, parent, type, index);
    screen.layoutLayers();
    return result;
  }

  /**
   * Build an element after a element in parent children list
   *
   * @return the Element created
   */
  @Nonnull
  public Element build(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent,
      @Nullable final Element before) {
    List<Element> parentList = parent.getChildren();
    int index = parentList.size();
    for (int i = 0; i < parentList.size(); i++) {
      if (parentList.get(i).equals(before)) {
        index = i;
        break;
      }
    }
    ElementType type = buildElementType();
    Element result = nifty.createElementFromType(screen, parent, type, index);
    screen.layoutLayers();
    return result;
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
