package de.lessvoid.nifty.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
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
  protected List<ElementBuilder> elementBuilders = new ArrayList<ElementBuilder>();
  private List<EffectBuilder> onStartScreen = new ArrayList<EffectBuilder>();
  private List<EffectBuilder> onEndScreen = new ArrayList<EffectBuilder>();
  private List<HoverEffectBuilder> onHover = new ArrayList<HoverEffectBuilder>();
  private List<HoverEffectBuilder> onStartHover = new ArrayList<HoverEffectBuilder>();
  private List<HoverEffectBuilder> onEndHover = new ArrayList<HoverEffectBuilder>();
  private List<EffectBuilder> onClick = new ArrayList<EffectBuilder>();
  private List<EffectBuilder> onFocus = new ArrayList<EffectBuilder>();
  private List<EffectBuilder> onLostFocus = new ArrayList<EffectBuilder>();
  private List<EffectBuilder> onGetFocus = new ArrayList<EffectBuilder>();
  private List<EffectBuilder> onActive = new ArrayList<EffectBuilder>();
  private List<EffectBuilder> onCustom = new ArrayList<EffectBuilder>();
  private List<EffectBuilder> onShow = new ArrayList<EffectBuilder>();
  private List<EffectBuilder> onHide = new ArrayList<EffectBuilder>();

  protected void initialize(final ControlAttributes attributes) {
    this.attributes = attributes;
    this.attributes.setInteract(interactAttributes);
  }

  public enum ChildLayoutType {
    Vertical("vertical"), Horizontal("horizontal"), Center("center"), Absolute("absolute"), AbsoluteInside("absolute-inside"), Overlay("overlay");
    
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
    attributes.setBackgroundColor(backgroundColor.getColorString());
  }

  public void controller(final Controller controller) {
    attributes.set("controller", controller.getClass().getName());
  }

  public void controller(final String controllerClass) {
    attributes.set("controller", controllerClass);
  }

  public void color(final String color) {
    attributes.setColor(color);
  }

  public void color(final Color color) {
    attributes.setColor(color.getColorString());
  }

  public void selectionColor(final String color) {
    attributes.setSelectionColor(color);
  }

  public void selectionColor(final Color color) {
    attributes.setSelectionColor(color.getColorString());
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

  public void childLayoutAbsoluteInside() {
    childLayout(ChildLayoutType.AbsoluteInside);
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

  public void renderOrder(final int renderOrder) {
    attributes.setRenderOrder(renderOrder);
  }

  public void visible(final boolean visible) {
    attributes.setVisible(String.valueOf(visible));
  }

  public void focusable(final boolean focusable) {
    attributes.setFocusable(String.valueOf(focusable));
  }

  public void focusableInsertBeforeElementId(final String focusableInsertBeforeElementId) {
    attributes.setFocusableInsertBeforeElementId(focusableInsertBeforeElementId);
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

  public void margin(final String margin) {
    attributes.setMargin(margin);
  }

  public void marginLeft(final String margin) {
    attributes.setMarginLeft(margin);
  }

  public void marginRight(final String margin) {
    attributes.setMarginRight(margin);
  }

  public void marginTop(final String margin) {
    attributes.setMarginTop(margin);
  }

  public void marginBottom(final String margin) {
    attributes.setMarginBottom(margin);
  }

  public void set(final String key, final String value) {
    attributes.set(key, value);
  }

  public String get(final String key) {
    return attributes.get(key);
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

  public void onStartHoverEffect(final HoverEffectBuilder onStartHoverEffect) {
    onStartHover.add(onStartHoverEffect);
  }

  public void onEndHoverEffect(final HoverEffectBuilder onEndHoverEffect) {
    onEndHover.add(onEndHoverEffect);
  }

  public void onClickEffect(final EffectBuilder onClickEffect) {
    onClick.add(onClickEffect);
  }

  public void onFocusEffect(final EffectBuilder onFocusEffect) {
    onFocus.add(onFocusEffect);
  }

  public void onLostFocusEffect(final EffectBuilder onLostFocusEffect) {
    onLostFocus.add(onLostFocusEffect);
  }

  public void onGetFocusEffect(final EffectBuilder onGetFocusEffect) {
    onGetFocus.add(onGetFocusEffect);
  }

  public void onActiveEffect(final EffectBuilder onActiveEffect) {
    onActive.add(onActiveEffect);
  }

  public void onShowEffect(final EffectBuilder onShowEffect) {
    onShow.add(onShowEffect);
  }

  public void onHideEffect(final EffectBuilder onHideEffect) {
    onHide.add(onHideEffect);
  }

  public void onCustomEffect(final EffectBuilder onCustomEffect) {
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

  public List<ElementBuilder> getElementBuilders() {
    return Collections.unmodifiableList(elementBuilders);
  }
  /**
   * Build an elment 
   * @param nifty
   * @param screen
   * @param parent
   * @param before
   * @return the Element created
   */
  public Element build(final Nifty nifty, final Screen screen, final Element parent) {
    ElementType type = buildElementType();
    Element result = nifty.createElementFromType(screen, parent, type);
    screen.layoutLayers();
    return result;
  }
  /**
   * Build an elment in a specified position in parent element list
   * @param nifty
   * @param screen
   * @param parent
   * @param before
   * @return the Element created
   */
  public Element build(final Nifty nifty, final Screen screen, final Element parent,int index) {
	  ElementType type = buildElementType();
	  Element result = nifty.createElementFromType(screen, parent, type,index);
	  screen.layoutLayers();
	 return result; 
  }
  /**
   * Build an elment after a elment in parent children list
   * @param nifty
   * @param screen
   * @param parent
   * @param before
   * @return the Element created
   */
  public Element build(final Nifty nifty, final Screen screen, final Element parent,Element before) {
	  int index = parent.getElements().size();
	  for(int i=0;i<parent.getElements().size();i++){
		  if(parent.getElements().get(i).equals(before)){
			  index=i;
			  break;
		  }
	  }
	  ElementType type = buildElementType();
	  Element result = nifty.createElementFromType(screen, parent, type,index);
	  screen.layoutLayers();
	 return result; 
  }
  
  /**
   * This method is called whenever we need the ElementType instead of a real
   * Element instance. This is currently used for ControlDefinition and Popup
   * registering dynamically from Java using the Builder pattern.
   *
   * It is not used for the general Java builder call that generates real instances.
   * 
   * @return the ElementType representation for this ElementBuilder
   */
  public ElementType buildElementType() {
    connectAttributes();
    ElementType thisType = attributes.createType();

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
    for (int i=0; i<elementBuilders.size(); i++) {
      thisType.addElementType(elementBuilders.get(i).buildElementType());
    }

    return thisType;
  }

  private void connectAttributes() {
    attributes.setEffects(createEffects());
    for (int i=0; i<onStartScreen.size(); i++) {
      attributes.addEffectsOnStartScreen(onStartScreen.get(i).getAttributes());
    }
    for (int i=0; i<onEndScreen.size(); i++) {
      attributes.addEffectsOnEndScreen(onEndScreen.get(i).getAttributes());
    }
    for (int i=0; i<onHover.size(); i++) {
      attributes.addEffectsOnHover(onHover.get(i).getAttributes());
    }
    for (int i=0; i<onStartHover.size(); i++) {
      attributes.addEffectsOnStartHover(onStartHover.get(i).getAttributes());
    }
    for (int i=0; i<onEndHover.size(); i++) {
      attributes.addEffectsOnEndHover(onEndHover.get(i).getAttributes());
    }
    for (int i=0; i<onClick.size(); i++) {
      attributes.addEffectsOnClick(onClick.get(i).getAttributes());
    }
    for (int i=0; i<onFocus.size(); i++) {
      attributes.addEffectsOnFocus(onFocus.get(i).getAttributes());
    }
    for (int i=0; i<onLostFocus.size(); i++) {
      attributes.addEffectsOnLostFocus(onLostFocus.get(i).getAttributes());
    }
    for (int i=0; i<onGetFocus.size(); i++) {
      attributes.addEffectsOnGetFocus(onGetFocus.get(i).getAttributes());
    }
    for (int i=0; i<onActive.size(); i++) {
      attributes.addEffectsOnActive(onActive.get(i).getAttributes());
    }
    for (int i=0; i<onCustom.size(); i++) {
      attributes.addEffectsOnCustom(onCustom.get(i).getAttributes());
    }
    for (int i=0; i<onShow.size(); i++) {
      attributes.addEffectsOnShow(onShow.get(i).getAttributes());
    }
    for (int i=0; i<onHide.size(); i++) {
      attributes.addEffectsOnHide(onHide.get(i).getAttributes());
    }
  }

  private ControlEffectsAttributes createEffects() {
    return new ControlEffectsAttributes();
  }
}
