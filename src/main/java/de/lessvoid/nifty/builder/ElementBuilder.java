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

  protected void initialize(final ControlAttributes attributes) {
    this.attributes = attributes;
  }

  public enum LayoutType {
    Vertical("vertical"),
    Horizontal("horizontal"),
    Center("center"),
    Absolute("absolute"),
    Overlay("overlay");

    private String layout;
    private LayoutType(final String layout) {
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

  protected Screen screen = null;
  protected Element parent = null;

  public void screen(final Screen screen) {
    this.screen = screen;
  }

  public void parent(final Element parent) {
    this.parent = parent;
  }

  public void id(final String id) {
    attributes.setId(id);
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

  public void childLayout(final LayoutType childLayout) {
    attributes.setChildLayout(childLayout.getLayout());
  }

  public void height(final String height) {
    attributes.setHeight(height);
  }

  public void width(final String width) {
    attributes.setWidth(width);
  }

  public void align(final Align align) {
    attributes.setAlign(align.getLayout());
  }

  public void valign(final VAlign valign) {
    attributes.setVAlign(valign.getLayout());
  }

  public void visibleToMouse(final boolean visibleToMouse) {
    attributes.setVisibleToMouse(String.valueOf(visibleToMouse));
  }

  public void font(final String font) {
    attributes.setFont(font);
  }

  // children

  private List<ElementBuilder> elementBuilders = new ArrayList<ElementBuilder>();

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

  public void panel(final PanelBuilder panelBuilder) {
    elementBuilders.add(panelBuilder);
  }

  public void text(final TextBuilder textBuilder) {
    elementBuilders.add(textBuilder);
  }

  protected void validate() {
    if (screen == null)
      throw new RuntimeException("screen is a required value for an element");

    if (!hasParent())
      throw new RuntimeException("parent is a required value for an element");
  }

  // effects...

  ControlEffectsAttributes effectsAttributes = new ControlEffectsAttributes();

  public void onStartScreenEffect(ControlEffectAttributes onStartScreenEffect) {
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
  
  // helpers
  
  public String percentage(final int percentage) {
    return Integer.toString(percentage) + "%";
  }
	
  public String pixels(final int px) {
    return Integer.toString(px) + "px";
  }
}

