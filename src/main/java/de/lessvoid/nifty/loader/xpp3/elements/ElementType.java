package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyImageMode;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * ElementType.
 * @author void
 */
public class ElementType {

  /**
   * attributes.
   */
  private AttributesType attributes;

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
   * Create element.
   * @param parent parent element
   * @param nifty nifty
   * @param screen screen
   * @param registeredEffects registeredEffects
   * @param registeredControls registeredControls
   * @param styleHandler style handler
   * @param time time
   * @param inputControl inputControl we should attach to the element (can be null)
   * @param screenController ScreenController
   * @return element
   */
  public Element createElement(
      final Element parent,
      final Nifty nifty,
      final Screen screen,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final StyleHandler styleHandler,
      final TimeProvider time,
      final NiftyInputControl inputControl,
      final ScreenController screenController) {
    return null;
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
   * @param nifty nifty
   * @param registeredEffects effects
   * @param registeredControls registeredControls
   * @param styleHandler style handler
   * @param time time
   * @param control attached control (might be null)
   * @param screenController screenController
   */
  protected void addElementAttributes(
      final Element element,
      final Screen screen,
      final Nifty nifty,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final StyleHandler styleHandler,
      final TimeProvider time,
      final NiftyInputControl control,
      final ScreenController screenController) {
    element.bindToScreen(nifty);

    // if the element we process has a style set, we try to apply
    // the style attributes first
    String styleId = attributes.getStyle();
    if (styleId != null) {
      StyleType style = styleHandler.getStyle(styleId);
      if (style != null) {
        style.applyStyle(element, nifty, registeredEffects, time);
      }
    }

    // now apply our own attributes
    applyAttributes(attributes, element, nifty.getRenderDevice());

    // interact
    if (interact != null) {
      // control given?
      if (control != null) {
        interact.initWithControl(element, control, screenController);
      } else {
        interact.initWithScreenController(element, screenController);
      }
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
      elementType.createElement(
          element,
          nifty,
          screen,
          registeredEffects,
          registeredControls,
          styleHandler,
          time,
          control,
          screenController);
    }
  }

  /**
   * apply given attributes to the element.
   * @param attrib attributes
   * @param element the element to apply attributes
   * @param renderDevice RenderDevice
   */
  public static void applyAttributes(
      final AttributesType attrib,
      final Element element,
      final NiftyRenderEngine renderDevice) {
    if (attrib == null) {
      return;
    }
    // height
    if (attrib.getHeight() != null) {
      SizeValue heightValue = new SizeValue(attrib.getHeight());
      element.setConstraintHeight(heightValue);
    }
    // width
    if (attrib.getWidth() != null) {
      SizeValue widthValue = new SizeValue(attrib.getWidth());
      element.setConstraintWidth(widthValue);
    }
    // set absolute x position when given
    if (attrib.getX() != null) {
      element.setConstraintX(new SizeValue(attrib.getX()));
    }
    // set absolute y position when given
    if (attrib.getY() != null) {
      element.setConstraintY(new SizeValue(attrib.getY()));
    }
    // horizontal align
    if (attrib.getAlign() != null) {
      element.setConstraintHorizontalAlign(HorizontalAlign.valueOf(attrib.getAlign().getValue()));
    }
    // vertical align
    if (attrib.getValign() != null) {
      element.setConstraintVerticalAlign(VerticalAlign.valueOf(attrib.getValign().getValue()));
    }
    // child clip
    if (attrib.getChildClip() != null) {
      element.setClipChildren(attrib.getChildClip());
    }
    // visible
    if (attrib.getVisible() != null) {
      if (attrib.getVisible()) {
        element.show();
      } else {
        element.hide();
      }
    }
    // visibleToMouse
    if (attrib.getVisibleToMouse() != null) {
      element.setVisibleToMouseEvents(attrib.getVisibleToMouse());
    }
    // childLayout
    if (attrib.getChildLayoutType() != null) {
      element.setLayoutManager(attrib.getChildLayoutType().getLayoutManager());
    }
    // textRenderer
    TextRenderer textRenderer = element.getRenderer(TextRenderer.class);
    if (textRenderer != null) {
      // font
      if (attrib.getFont() != null) {
        textRenderer.setFont(renderDevice.createFont(attrib.getFont()));
      }
      // text horizontal align
      if (attrib.getTextHAlign() != null) {
        textRenderer.setTextHAlign(HorizontalAlign.valueOf(attrib.getTextHAlign().getValue()));
      }
      // text vertical align
      if (attrib.getTextVAlign() != null) {
        textRenderer.setTextVAlign(VerticalAlign.valueOf(attrib.getTextVAlign().getValue()));
      }
      // text color
      if (attrib.getColor() != null) {
        textRenderer.setColor(attrib.getColor().createColor());
      }
    }
    // panelRenderer
    PanelRenderer panelRenderer = element.getRenderer(PanelRenderer.class);
    if (panelRenderer != null) {
      // background color
      if (attrib.getBackgroundColor() != null) {
        panelRenderer.setBackgroundColor(attrib.getBackgroundColor().createColor());
      }
    }
    // imageRenderer
    ImageRenderer imageRenderer = element.getRenderer(ImageRenderer.class);
    if (imageRenderer != null) {
      // filename
      if (attrib.getFilename() != null) {
        imageRenderer.setImage(renderDevice.createImage(attrib.getFilename(), attrib.getFilter()));
      }
      if (attrib.getBackgroundImage() != null) {
        imageRenderer.setImage(renderDevice.createImage(attrib.getBackgroundImage(), attrib.getFilter()));
      }
      // set imageMode?
      NiftyImage image = imageRenderer.getImage();
      String imageMode = attrib.getImageMode();
      if (image != null && imageMode != null) {
          image.setImageMode(NiftyImageMode.valueOf(imageMode));
      }
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

  /**
   * get attributes.
   * @return attributes
   */
  public AttributesType getAttributes() {
    return attributes;
  }

  /**
   * set attributes.
   * @param attributesTypeParam attributes type to set
   */
  public void setAttributes(final AttributesType attributesTypeParam) {
    attributes = attributesTypeParam;
  }
}
