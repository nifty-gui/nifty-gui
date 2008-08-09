package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;
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
   * logger.
   */
  private static Logger log = Logger.getLogger(ElementType.class.getName());

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

  protected TypeContext typeContext;

  protected ElementType elementTypeParent;

  public ElementType(final TypeContext newTypeContext) {
    typeContext = newTypeContext;
  }

  /**
   * Create element.
   * @param parent parent element
   * @param screen screen
   * @param inputControl inputControl we should attach to the element (can be null)
   * @param screenController ScreenController
   * @return element
   */
  public Element createElement(
      final Element parent,
      final Screen screen,
      final NiftyInputControl inputControl,
      final ScreenController screenController) {
    return null;
  }

  /**
   * add attributes to the element.
   * @param element element
   * @param screen screen
   * @param screenController screenController
   * @param control attached control (might be null)
   */
  protected void addElementAttributes(
      final Element element,
      final Screen screen,
      final ScreenController screenController,
      final NiftyInputControl ... control) {
    // if the element we process has a style set, we try to apply
    // the style attributes first
    String styleId = attributes.getStyle();
    applyStyle(
        element,
        typeContext.nifty,
        typeContext.registeredEffects,
        typeContext.styleHandler,
        typeContext.time,
        styleId,
        screen);

    // now apply our own attributes
    applyAttributes(attributes, screen, element, typeContext.nifty.getRenderDevice());

    // interact
    if (interact != null) {
      // control given?
      if (control != null) {
        element.attachInputControl(control[control.length - 1]);
        interact.initWithControl(element, getControllerArray(control, screenController));
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
      effects.initElement(element, typeContext.nifty, typeContext.registeredEffects, typeContext.time);
    }
    // children
    for (ElementType elementType : elements) {
      elementType.createElement(
          element,
          screen,
          control[control.length - 1],
          screenController);
    }
  }

  /**
   * Get controller array.
   * @param control input controls
   * @param screenController screen controller
   * @return object array
   */
  private Object[] getControllerArray(final NiftyInputControl[] control, final ScreenController screenController) {
    ArrayList < Object > controlList = new ArrayList < Object >();
    for (NiftyInputControl c : control) {
      if (c != null) {
        controlList.add(c.getController());
      }
    }
    if (screenController != null) {
      controlList.add(screenController);
    }
    return controlList.toArray(new Object[0]);
  }

  /**
   * apply style.
   * @param element element
   * @param nifty nifty
   * @param registeredEffects effects
   * @param styleHandler style handler
   * @param time time provider
   * @param styleId style id
   * @param screen screen
   */
  private void applyStyle(
      final Element element,
      final Nifty nifty,
      final Map < String, RegisterEffectType > registeredEffects,
      final StyleHandler styleHandler,
      final TimeProvider time,
      final String styleId,
      final Screen screen) {
    if (styleId != null) {
      StyleType style = styleHandler.getStyle(styleId);
      if (style != null) {
        style.applyStyle(element, nifty, registeredEffects, time, screen);
      }
    }
  }

  /**
   * apply given attributes to the element.
   * @param attrib attributes
   * @param screen screen
   * @param element the element to apply attributes
   * @param renderDevice RenderDevice
   */
  public static void applyAttributes(
      final AttributesType attrib,
      final Screen screen,
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
    // focusable
    if (attrib.getFocusable() != null) {
      element.setFocusable(attrib.getFocusable());
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
      // text
      if (attrib.getText() != null) {
        textRenderer.setText(attrib.getText());
//        element.setConstraintHeight(new SizeValue(textRenderer.getTextHeight() + "px"));
//        element.setConstraintWidth(new SizeValue(textRenderer.getTextWidth() + "px"));
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
      // set width and height to image width and height (for now)
      image = imageRenderer.getImage();
      if (image != null) {
        if (element.getConstraintWidth() == null) {
          element.setConstraintWidth(new SizeValue(image.getWidth() + "px"));
        }
        if (element.getConstraintHeight() == null) {
          element.setConstraintHeight(new SizeValue(image.getHeight() + "px"));
        }
      }
    }
  }

  /**
   * add element.
   * @param elementType elementType
   */
  public void addElementType(final ElementType elementType) {
    elements.add(elementType);
    elementType.setParent(this);
  }

  private void setParent(final ElementType newElementTypeParent) {
    elementTypeParent = newElementTypeParent;
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

  public void applyStyle(
      final Element element,
      final Screen screen,
      final String newStyle) {

    applyStyle(
        element,
        typeContext.nifty,
        typeContext.registeredEffects,
        typeContext.styleHandler,
        typeContext.time,
        newStyle,
        screen);

    controlProcessStyleAttribute(
        element,
        typeContext.styleHandler,
        null,
        newStyle,
        typeContext.nifty,
        typeContext.registeredEffects,
        typeContext.time,
        screen);
    for (Element child : element.getElements()) {
      applyControlStyle(
          child,
          typeContext.styleHandler,
          null,
          newStyle,
          typeContext.nifty,
          typeContext.registeredEffects,
          typeContext.time,
          screen);
    }
  }

  /**
   * process this elements styleId. this is used for controls and
   * changes the given styleId and the elements style id to a new
   * combined one.
   * @param element element
   * @param styleHandler style handler
   * @param controlDefinitionAttributes controlDefinitionAttributes
   * @param controlAttributes controlAttributes
   * @param nifty nifty
   * @param registeredEffects effects
   * @param time time
   * @param screen screen
   */
  public static void applyControlStyle(
      final Element element,
      final StyleHandler styleHandler,
      final String controlDefinitionStyle,
      final String controlStyle,
      final Nifty nifty,
      final Map < String, RegisterEffectType > registeredEffects,
      final TimeProvider time,
      final Screen screen) {
    controlProcessStyleAttribute(
        element,
        styleHandler,
        controlDefinitionStyle,
        controlStyle,
        nifty,
        registeredEffects,
        time,
        screen);
    for (Element child : element.getElements()) {
      applyControlStyle(
          child,
          styleHandler,
          controlDefinitionStyle,
          controlStyle,
          nifty,
          registeredEffects,
          time,
          screen);
    }
  }

  /**
   * apply control parameters.
   * @param element element
   * @param controlAttributes control attributes
   * @param nifty nifty instance
   * @param screen screen
   */
  public static void applyControlParameters(
      final Element element,
      final Attributes controlAttributes,
      final Nifty nifty,
      final Screen screen) {
    controlProcessParameters(
        element, controlAttributes, nifty.getRenderDevice(), screen);
    for (Element child : element.getElements()) {
      applyControlParameters(child, controlAttributes, nifty, screen);
    }
  }

  /**
   * process style attribute.
   * @param element element
   * @param styleHandler style handler
   * @param controlDefinitionAttributes control definition attributes
   * @param controlAttributes control attributes
   * @param nifty nifty
   * @param registeredEffects effects
   * @param time time
   * @param screen screen
   */
  private static void controlProcessStyleAttribute(
      final Element element,
      final StyleHandler styleHandler,
      final String controlDefinitionStyle,
      final String controlStyle,
      final Nifty nifty,
      final Map < String, RegisterEffectType > registeredEffects,
      final TimeProvider time,
      final Screen screen) {
    String myStyleId = element.getElementType().getAttributes().getStyle();
    if (myStyleId != null) {
      // this element has a style id set. is a special substyle?
      int indexOfSep = myStyleId.indexOf("#");
      if (indexOfSep != -1) {
        StyleType style = resolveStyle(styleHandler, myStyleId, controlStyle);
        if (style == null) {
          style = resolveStyle(styleHandler, myStyleId, controlDefinitionStyle);
        }
        if (style != null) {
          style.applyStyle(element, nifty, registeredEffects, time, screen);
        }
      }
    }
  }

  /**
   * try to resolve style.
   * @param styleHandler style handler
   * @param myStyleId my style
   * @param newStyle new style
   * @return StyleType when resolved or null on error.
   */
  private static StyleType resolveStyle(
      final StyleHandler styleHandler,
      final String myStyleId,
      final String newStyle) {
    if (myStyleId.startsWith("#")) {
      String resolvedStyle = newStyle + myStyleId;

      // check if newStyleId exists.
      return styleHandler.getStyle(resolvedStyle);
    } else {
      return null;
    }
  }

  /**
   * process parameters.
   * @param element element
   * @param controlAttributes control attributes
   * @param niftyRenderEngine render engine
   * @param screen screen
   */
  private static void controlProcessParameters(
      final Element element,
      final Attributes controlAttributes,
      final NiftyRenderEngine niftyRenderEngine,
      final Screen screen) {
    for (Entry < String, String > entry
        : element.getElementType().getAttributes().findParameterAttributes().entrySet()) {
      String value = controlAttributes.get(entry.getValue());
      if (value == null) {
        value = "'" + entry.getValue() + "' missing o_O";
      }
      log.info("[" + element.getId() + "] setting [" + entry.getKey() + "] to [" + value + "]");
      Attributes attributes = new Attributes();
      attributes.overwriteAttribute(entry.getKey(), value);
      ElementType.applyAttributes(new AttributesType(attributes), screen, element, niftyRenderEngine);
    }
  }
}
