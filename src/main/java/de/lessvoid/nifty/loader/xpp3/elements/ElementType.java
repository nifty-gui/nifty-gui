package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * ElementType.
 * @author void
 */
public class ElementType {
  /**
   * id.
   */
  private String id;

  /**
   * visible.
   */
  private Boolean visible;

  /**
   * width.
   */
  private String width;

  /**
   * height.
   */
  private String height;

  /**
   * align.
   */
  private AlignType align;

  /**
   * valign.
   */
  private ValignType valign;

  /**
   * childLayoutType.
   */
  private LayoutType childLayoutType;

  /**
   * childClip.
   */
  private Boolean childClip;

  /**
   * backgroundImage.
   */
  private String backgroundImage;

  /**
   * backgroundColor.
   */
  private ColorType backgroundColor = new ColorType(null);

  /**
   * visibleToMouse.
   */
  private Boolean visibleToMouse;

  /**
   * interact.
   * @optional
   */
  private InteractType interact;

  /**
   * hover.
   * @optional
   */
  private HoverType hover;

  /**
   * EffectsType.
   * @optional
   */
  private EffectsType effects;

  /**
   * elements.
   * @optional
   */
  private Collection < ElementType > elements = new ArrayList < ElementType >();

  /**
   * setId.
   * @param idParam id
   */
  public void setId(final String idParam) {
    this.id = idParam;
  }

  /**
   * setWidth.
   * @param widthParam width
   */
  public void setWidth(final String widthParam) {
    this.width = widthParam;
  }

  /**
   * setHeight.
   * @param heightParam height
   */
  public void setHeight(final String heightParam) {
    this.height = heightParam;
  }

  /**
   * setAlign.
   * @param alignParam align
   */
  public void setAlign(final AlignType alignParam) {
    this.align = alignParam;
  }

  /**
   * setValign.
   * @param valignParam valign
   */
  public void setValign(final ValignType valignParam) {
    this.valign = valignParam;
  }

  /**
   * setChildLayoutType.
   * @param layoutTypeParam layoutType
   */
  public void setChildLayoutType(final LayoutType layoutTypeParam) {
    this.childLayoutType = layoutTypeParam;
  }

  /**
   * setChildClip.
   * @param childClipParam childClip
   */
  public void setChildClip(final boolean childClipParam) {
    this.childClip = childClipParam;
  }

  /**
   * setBackgroundImage.
   * @param backgroundImageParam backgroundImage
   */
  public void setBackgroundImage(final String backgroundImageParam) {
    this.backgroundImage = backgroundImageParam;
  }

  /**
   * setBackgroundColor.
   * @param backgroundColorParam backgroundColor
   */
  public void setBackgroundColor(final ColorType backgroundColorParam) {
    this.backgroundColor = backgroundColorParam;
  }

  /**
   * setVisibleToMouse.
   * @param visibleToMouseParam visibleToMouse
   */
  public void setVisibleToMouse(final Boolean visibleToMouseParam) {
    this.visibleToMouse = visibleToMouseParam;
  }

  /**
   * set visible.
   * @param visibleParam visible
   */
  public void setVisible(final Boolean visibleParam) {
    this.visible = visibleParam;
  }

  /**
   * Create element.
   * @param parent parent element
   * @param nifty nifty
   * @param screen screen
   * @param controller ScreenController
   * @param registeredEffects registeredEffects
   * @param registeredControls registeredControls
   * @param time time
   * @return element
   */
  public Element createElement(
      final Element parent,
      final Nifty nifty,
      final Screen screen,
      final Object controller,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final TimeProvider time) {
    return null;
  }

  /**
   * get id.
   * @return id
   */
  protected String getId() {
    return id;
  }

  /**
   * get visible.
   * @return visible
   */
  protected Boolean getVisible() {
    return visible;
  }

  /**
   * get width.
   * @return width
   */
  protected String getWidth() {
    return width;
  }

  /**
   * get height.
   * @return height
   */
  protected String getHeight() {
    return height;
  }

  /**
   * get align.
   * @return align
   */
  protected AlignType getAlign() {
    return align;
  }

  /**
   * get valign.
   * @return valign
   */
  protected ValignType getValign() {
    return valign;
  }

  /**
   * Get childLayoutType.
   * @return childLayoutType
   */
  protected LayoutType getChildLayoutType() {
    return childLayoutType;
  }

  /**
   * is child clip.
   * @return child clip
   */
  protected boolean isChildClip() {
    return childClip;
  }

  /**
   * get background image.
   * @return background image
   */
  protected String getBackgroundImage() {
    return backgroundImage;
  }

  /**
   * get background color.
   * @return background color
   */
  protected ColorType getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * get visible to mouse flag.
   * @return visible to mouse
   */
  protected Boolean getVisibleToMouse() {
    return visibleToMouse;
  }

  /**
   * get interact.
   * @return interact.
   */
  protected InteractType getInteract() {
    return interact;
  }

  /**
   * get hover.
   * @return hover
   */
  protected HoverType getHover() {
    return hover;
  }

  /**
   * get effect.
   * @return effect
   */
  protected EffectsType getEffect() {
    return effects;
  }

  /**
   * add attributes to the element.
   * @param element element
   * @param screen screen
   * @param controller screenController
   * @param nifty nifty
   * @param registeredEffects effects
   * @param registeredControls registeredControls
   * @param time time
   */
  protected void addElementAttributes(
      final Element element,
      final Screen screen,
      final Object controller,
      final Nifty nifty,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final TimeProvider time) {
    element.bindToScreen(nifty);

    // height
    if (height != null) {
      SizeValue heightValue = new SizeValue(height);
      element.setConstraintHeight(heightValue);
    }
    // width
    if (width != null) {
      SizeValue widthValue = new SizeValue(width);
      element.setConstraintWidth(widthValue);
    }
    // horizontal align
    if (align != null) {
      element.setConstraintHorizontalAlign(HorizontalAlign.valueOf(align.getValue()));
    }
    // vertical align
    if (valign != null) {
      element.setConstraintVerticalAlign(VerticalAlign.valueOf(valign.getValue()));
    }
    // child clip
    if (childClip != null) {
      element.setClipChildren(childClip);
    }
    // visible
    if (visible != null) {
      if (visible) {
        element.show();
      } else {
        element.hide();
      }
    }
    // visibleToMouse
    if (visibleToMouse != null) {
      element.setVisibleToMouseEvents(visibleToMouse);
    }
    // childLayout
    if (childLayoutType != null) {
      element.setLayoutManager(childLayoutType.getLayoutManager());
    }
    // interact
    if (interact != null) {
      interact.initElement(element, controller);
    }
    // hover
    if (hover != null) {
      hover.initElement(element);
    }
    // effects
    if (effects != null) {
      effects.initElement(element, nifty, registeredEffects, time);
    }
    // children
    for (ElementType elementType : elements) {
      elementType.createElement(element, nifty, screen, controller, registeredEffects, registeredControls, time);
    }
  }

  /**
   * add element.
   * @param elementType elementType
   */
  public void addElementType(final ElementType elementType) {
    elements.add(elementType);
  }

  /**
   * set child clip.
   * @param childClipParam childClip
   */
  public void setChildClip(final Boolean childClipParam) {
    this.childClip = childClipParam;
  }

  /**
   * set interact.
   * @param interactParam interact
   */
  public void setInteract(final InteractType interactParam) {
    this.interact = interactParam;
  }

  /**
   * set hover.
   * @param hoverParam hover
   */
  public void setHover(final HoverType hoverParam) {
    this.hover = hoverParam;
  }

  /**
   * set effects.
   * @param effectsParam effects
   */
  public void setEffects(final EffectsType effectsParam) {
    this.effects = effectsParam;
  }
}
