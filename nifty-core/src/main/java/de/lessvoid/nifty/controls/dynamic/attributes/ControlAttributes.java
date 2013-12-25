package de.lessvoid.nifty.controls.dynamic.attributes;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loaderv2.types.*;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ControlAttributes {
  @Nonnull
  private final Attributes attributes;
  @Nullable
  private ControlInteractAttributes interact;
  @Nullable
  private ControlEffectsAttributes effects;

  private boolean isAutoId = false;

  public ControlAttributes() {
    attributes = new Attributes();
  }

  public ControlAttributes(@Nonnull final ElementType type) {
    attributes = new Attributes(type.getAttributes());
    final InteractType interactType = type.getInteract();
    interact = new ControlInteractAttributes(interactType);
    final EffectsType effectsType = type.getEffects();
    effects = new ControlEffectsAttributes(effectsType);
  }

  @Nonnull
  public Attributes getAttributes() {
    return attributes;
  }

  @Nonnull
  protected ControlInteractAttributes getInteract() {
    if (interact == null) {
      interact = new ControlInteractAttributes();
    }
    return interact;
  }

  @Nonnull
  protected ControlEffectsAttributes getEffects() {
    if (effects == null) {
      effects = new ControlEffectsAttributes();
    }
    return effects;
  }

  public void setInteract(@Nullable final ControlInteractAttributes controlInteract) {
    interact = controlInteract;
  }

  public void setEffects(@Nullable final ControlEffectsAttributes controlEffects) {
    effects = controlEffects;
  }

  public void set(@Nonnull final String key, @Nonnull final String value) {
    attributes.set(key, value);
  }

  @Nullable
  public String get(@Nonnull final String key) {
    return attributes.get(key);
  }

  public void setId(@Nonnull final String id) {
    isAutoId = false;
    set("id", id);
  }

  @Nullable
  public String getId() {
    return get("id");
  }

  public void setAutoId() {
    setAutoId(NiftyIdCreator.generate());
  }

  public void setAutoId(@Nonnull final String id) {
    isAutoId = true;
    set("id", id);
  }

  public boolean isAutoId() {
    return isAutoId;
  }

  public void setName(@Nonnull final String name) {
    set("name", name);
  }

  public void setHeight(@Nonnull final String height) {
    set("height", height);
  }

  public void setWidth(@Nonnull final String width) {
    set("width", width);
  }

  public void setX(@Nonnull final String x) {
    set("x", x);
  }

  public void setY(@Nonnull final String y) {
    set("y", y);
  }

  public void setAlign(@Nonnull final String align) {
    set("align", align);
  }

  public void setVAlign(@Nonnull final String valign) {
    set("valign", valign);
  }

  public void setPadding(@Nonnull final String padding) {
    set("padding", padding);
  }

  public void setPaddingLeft(@Nonnull final String paddingLeft) {
    set("paddingLeft", paddingLeft);
  }

  public void setPaddingRight(@Nonnull final String paddingRight) {
    set("paddingRight", paddingRight);
  }

  public void setPaddingTop(@Nonnull final String paddingTop) {
    set("paddingTop", paddingTop);
  }

  public void setPaddingBottom(@Nonnull final String paddingBottom) {
    set("paddingBottom", paddingBottom);
  }

  public void setMargin(@Nonnull final String margin) {
    set("margin", margin);
  }

  public void setMarginLeft(@Nonnull final String marginLeft) {
    set("marginLeft", marginLeft);
  }

  public void setMarginRight(@Nonnull final String marginRight) {
    set("marginRight", marginRight);
  }

  public void setMarginTop(@Nonnull final String marginTop) {
    set("marginTop", marginTop);
  }

  public void setMarginBottom(@Nonnull final String marginBottom) {
    set("marginBottom", marginBottom);
  }

  public void setChildClip(@Nonnull final String childClip) {
    set("childClip", childClip);
  }

  public void setRenderOrder(final int renderOrder) {
    set("renderOrder", String.valueOf(renderOrder));
  }

  public void setVisible(@Nonnull final String visible) {
    set("visible", visible);
  }

  public void setVisibleToMouse(@Nonnull final String visibleToMouse) {
    set("visibleToMouse", visibleToMouse);
  }

  public void setChildLayout(@Nonnull final String childLayout) {
    set("childLayout", childLayout);
  }

  public void setFocusable(@Nonnull final String focusable) {
    set("focusable", focusable);
  }

  public void setFocusableInsertBeforeElementId(@Nonnull final String focusableInsertBeforeElementId) {
    set("focusableInsertBeforeElementId", focusableInsertBeforeElementId);
  }

  public void setFont(@Nonnull final String font) {
    set("font", font);
  }

  public void setTextHAlign(@Nonnull final String textHAlign) {
    set("textHAlign", textHAlign);
  }

  public void setTextVAlign(@Nonnull final String textVAlign) {
    set("textVAlign", textVAlign);
  }

  public void setColor(@Nonnull final String color) {
    set("color", color);
  }

  public void setSelectionColor(@Nonnull final String selectionColor) {
    set("selectionColor", selectionColor);
  }

  public void setText(@Nonnull final String text) {
    set("text", text);
  }

  public void setBackgroundColor(@Nonnull final String backgroundColor) {
    set("backgroundColor", backgroundColor);
  }

  public void setBackgroundImage(@Nonnull final String backgroundImage) {
    set("backgroundImage", backgroundImage);
  }

  public void setImageMode(@Nonnull final String imageMode) {
    set("imageMode", imageMode);
  }

  public void setFilename(@Nonnull final String filename) {
    set("filename", filename);
  }

  public void setInset(@Nonnull final String inset) {
    set("inset", inset);
  }

  public void setController(@Nonnull final String controller) {
    set("controller", controller);
  }

  public void setInputMapping(@Nonnull final String inputMapping) {
    set("inputMapping", inputMapping);
  }

  public void setStyle(@Nonnull final String style) {
    set("style", style);
  }

  public void setInteractAttribute(@Nonnull final String name, @Nonnull final String value) {
    getInteract().setAttribute(name, value);
  }

  public void setInteractOnClick(@Nonnull final String onClick) {
    getInteract().setOnClick(onClick);
  }

  public void setInteractOnRelease(@Nonnull final String onRelease) {
    getInteract().setOnRelease(onRelease);
  }

  public void setInteractOnMouseOver(@Nonnull final String onMouseOver) {
    getInteract().setOnMouseOver(onMouseOver);
  }

  public void setInteractOnClickRepeat(@Nonnull final String onClickRepeat) {
    getInteract().setOnClickRepeat(onClickRepeat);
  }

  public void setInteractOnClickMouseMove(@Nonnull final String onClickMouseMove) {
    getInteract().setOnClickMouseMove(onClickMouseMove);
  }

  public void setInteractOnClickAlternateKey(@Nonnull final String onClickAlternateKey) {
    getInteract().setOnClickAlternateKey(onClickAlternateKey);
  }

  public void setEffectsAttribute(@Nonnull final String name, @Nonnull final String value) {
    getEffects().setAttribute(name, value);
  }

  public void setEffectsOverlay(@Nonnull final String overlay) {
    getEffects().setOverlay(overlay);
  }

  public void addEffects(@Nonnull final EffectEventId eventId, @Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addEffectAttribute(eventId, effectParam);
  }

  public void addEffectsOnStartScreen(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnStartScreen(effectParam);
  }

  public void addEffectsOnEndScreen(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnEndScreen(effectParam);
  }

  public void addEffectsOnHover(@Nonnull final ControlEffectOnHoverAttributes effectParam) {
    getEffects().addOnHover(effectParam);
  }

  public void addEffectsOnStartHover(@Nonnull final ControlEffectOnHoverAttributes effectParam) {
    getEffects().addOnStartHover(effectParam);
  }

  public void addEffectsOnEndHover(@Nonnull final ControlEffectOnHoverAttributes effectParam) {
    getEffects().addOnEndHover(effectParam);
  }

  public void addEffectsOnClick(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnClick(effectParam);
  }

  public void addEffectsOnFocus(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnFocus(effectParam);
  }

  public void addEffectsOnLostFocus(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnLostFocus(effectParam);
  }

  public void addEffectsOnGetFocus(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnGetFocus(effectParam);
  }

  public void addEffectsOnActive(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnActive(effectParam);
  }

  public void addEffectsOnShow(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnShow(effectParam);
  }

  public void addEffectsOnHide(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnHide(effectParam);
  }

  public void addEffectsOnCustom(@Nonnull final ControlEffectAttributes effectParam) {
    getEffects().addOnCustom(effectParam);
  }

  @Nonnull
  protected Element createControlInternal(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    ControlType controlType = new ControlType(attributes);
    return buildControl(nifty, screen, parent, controlType, new LayoutPart());
  }

  @Nonnull
  protected Element createText(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    TextType textType = new TextType(attributes);
    return buildControl(nifty, screen, parent, textType, new LayoutPart());
  }

  @Nonnull
  protected Element createPanel(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    PanelType panelType = new PanelType(attributes);
    return buildControl(nifty, screen, parent, panelType, new LayoutPart());
  }

  protected void registerPopup(@Nonnull final Nifty nifty) {
    PopupType popupType = new PopupType(attributes);
    popupType.translateSpecialValues(nifty, null);
    nifty.registerPopup(popupType);
  }

  @Nonnull
  protected Element createLayer(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    LayerType layerType = new LayerType(attributes);
    return buildControl(nifty, screen, parent, layerType, nifty.getRootLayerFactory().createRootLayerLayoutPart(nifty));
  }

  @Nonnull
  protected Element createImage(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent) {
    ImageType imageType = new ImageType(attributes);
    return buildControl(nifty, screen, parent, imageType, new LayoutPart());
  }

  @Nonnull
  private Element buildControl(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element parent,
      @Nonnull final ElementType elementType,
      @Nonnull final LayoutPart layoutPart) {
    if (effects != null) {
      elementType.setEffect(effects.create());
    }
    if (interact != null) {
      elementType.setInteract(interact.create());
    }
    elementType.prepare(nifty, screen, parent.getElementType());
    elementType.connectParentControls(parent);
    Element element = elementType.create(
        parent,
        nifty,
        screen,
        layoutPart);

    parent.layoutElements();
    return element;
  }

  @Nonnull
  protected StandardControl getStandardControl() {
    return new StandardControl() {
      @Nonnull
      @Override
      public Element createControl(
          @Nonnull final Nifty nifty,
          @Nonnull final Screen screen,
          @Nonnull final Element parent) {
        return createControlInternal(nifty, screen, parent);
      }
    };
  }

  public void refreshAttributes(@Nonnull final Attributes attrib) {
    attrib.refreshFromAttributes(attributes);
  }

  public void refreshEffects(@Nonnull final EffectsType effects) {
    if (this.effects != null) {
      effects.refreshFromAttributes(this.effects);
    }
  }

  @Nullable
  public ElementType createType() {
    // you'll need to implement this in a sub class
    return null;
  }

  public void connect(@Nonnull final ElementType e) {
    if (effects != null) {
      e.setEffect(effects.create());
    }
    if (interact != null) {
      e.setInteract(interact.create());
    }
  }

  @Nonnull
  public StyleType createStyleType(@Nonnull final Attributes styleAttributes) {
    StyleType styleType = new StyleType(styleAttributes);
    styleType.setAttributes(new AttributesType(attributes));
    if (effects != null) {
      styleType.setEffect(effects.create());
    }
    if (interact != null) {
      styleType.setInteract(interact.create());
    }
    return styleType;
  }
}
