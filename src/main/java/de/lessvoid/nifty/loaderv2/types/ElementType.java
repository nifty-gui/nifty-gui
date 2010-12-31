package de.lessvoid.nifty.loaderv2.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loaderv2.types.apply.ApplyAttributes;
import de.lessvoid.nifty.loaderv2.types.apply.Convert;
import de.lessvoid.nifty.loaderv2.types.helper.CollectionLogger;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolverControlDefinintion;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.tools.ClassHelper;
import de.lessvoid.xml.xpp3.Attributes;

public class ElementType extends XmlBaseType {
  protected String tagName;
  protected ElementRendererCreator elementRendererCreator;
  protected InteractType interact = new InteractType();
  protected EffectsType effects = new EffectsType();
  protected Collection < ElementType > elements = new ArrayList < ElementType >();
  protected LinkedList < Object > controllers = new LinkedList < Object >();
  protected Controller controller;

  public ElementType() {
    super();
  }

  public ElementType(final ElementType src) {
    super(src);
    tagName = src.tagName;
    elementRendererCreator = src.elementRendererCreator;
    interact = new InteractType(src.interact);
    effects = new EffectsType(src.effects);
    copyElements(src);
  }

  public ElementType(final Attributes attributes) {
    super(attributes);
  }

  void mergeFromElementType(final ElementType src) {
    tagName = src.tagName;
    elementRendererCreator = src.elementRendererCreator;
    mergeFromAttributes(src.getAttributes());
    interact.mergeFromInteractType(src.getInteract());
    effects.mergeFromEffectsType(src.getEffects());
    copyElements(src);
  }

  void copyElements(final ElementType src) {
    elements.clear();
    for (ElementType element : src.elements) {
      elements.add(element.copy());
    }
  }

  public ElementType copy() {
    return new ElementType(this);
  }

  void setElementRendererCreator(final ElementRendererCreator elementRendererCreatorParam) {
    elementRendererCreator = elementRendererCreatorParam;
  }

  void setTagName(final String tagNameParam) {
    tagName = tagNameParam;
  }

  protected void makeFlat() {
    for (ElementType element : elements) {
      element.makeFlat();
    }
  }

  public boolean hasElements() {
    return !elements.isEmpty();
  }

  public ElementType getFirstElement() {
    return elements.iterator().next();
  }

  public void addElementType(final ElementType type) {
    elements.add(type);
  }

  public void addPanel(final ElementType panel) {
    elements.add(panel);
  }

  public void addImage(final ElementType image) {
    elements.add(image);
  }

  public void addLabel(final ElementType label) {
    elements.add(label);
  }

  public void addText(final ElementType text) {
    elements.add(text);
  }

  public void addControl(final ElementType text) {
    elements.add(text);
  }

  public void setInteract(final InteractType interactParam) {
    interact = interactParam;
  }

  public void setEffect(final EffectsType effectsParam) {
    effects = effectsParam;
  }

  public String output(final int offset) {
    String result = StringHelper.whitespace(offset) + tagName + " [element] " + super.output(offset);
    if (interact != null) {
      result += "\n" + interact.output(offset + 1);
    }
    if (effects != null) {
      result += "\n" + effects.output(offset + 1);
    }
    result += "\n" + CollectionLogger.out(offset + 1, elements, "elements");
    return result;
  }

  public Element create(
      final Element parent,
      final Nifty nifty,
      final Screen screen,
      final LayoutPart layoutPart) {
    Element element = internalCreateElement(parent, nifty, screen, layoutPart, getAttributes());
    applyStandard(nifty, screen, element);
    return element;
  }

  private Element internalCreateElement(
      final Element parent,
      final Nifty nifty,
      final Screen screen,
      final LayoutPart layoutPart,
      final Attributes attrib) {
    Element element = new Element(
        nifty,
        this,
        attrib.get("id"),
        parent,
        layoutPart,
        screen.getFocusHandler(),
        false,
        nifty.getTimeProvider(),
        elementRendererCreator.createElementRenderer(nifty));
    parent.add(element);
    return element;
  }

  private void applyStandard(
      final Nifty nifty,
      final Screen screen,
      final Element element) {
    applyAttributes(element, getAttributes(), nifty.getRenderEngine());
    applyEffects(nifty, screen, element);
    applyInteract(nifty, screen, element);
    applyChildren(element, screen, nifty);
    enforceChildLayout(getAttributes(), elements.size());

    if (controller != null) {
      ControllerEventListener listener = createControllerEventListener(screen);

      controller.bind(
          nifty,
          screen,
          element,
          getAttributes().createProperties(),
          listener,
          getAttributes());

      NiftyInputControl niftyInputControl = createNiftyInputControl(element.getId(), getAttributes(), controller);
      element.attachInputControl(niftyInputControl);
    }
  }

  private void enforceChildLayout(final Attributes attributes, final int childCount) {
    if (!attributes.isSet("childLayout") && childCount > 0) {
      throw new RuntimeException("missing childLayout attribute for an element with [" + childCount + "] child elements. Attributes of error element [" + attributes.toString() + "]");
    }
  }

  LinkedList < Object > getControllersWithScreenController(final Screen screen) {
    LinkedList < Object > withScreenController = new LinkedList < Object > (controllers);
    withScreenController.addFirst(screen.getScreenController());
    return withScreenController;
  }

  private Controller createLocalController(final String controllerClassParam) {
    if (controllerClassParam == null) {
      return null;
    }
    return ClassHelper.getInstance(controllerClassParam, Controller.class);
  }

  private NiftyInputControl createNiftyInputControl(final String elementId, final Attributes controlDefinitionAttributes, final Controller controller) {
    String inputMappingClass = controlDefinitionAttributes.get("inputMapping");
    if (inputMappingClass == null) {
      inputMappingClass = DefaultInputMapping.class.getName();
    }

    NiftyInputMapping inputMapping = ClassHelper.getInstance(inputMappingClass, NiftyInputMapping.class);
    return new NiftyInputControl(elementId, controller, inputMapping);
  }

  private ControllerEventListener createControllerEventListener(final Screen screen) {
    LinkedList < Object > withScreenController = getControllersWithScreenController(screen);
    ControllerEventListener listener = null;

    String onChange = getAttributes().get("onChange");
    if (onChange != null) {
      /** FIXME
      Class screenControllerClass = screenController.getClass();
      final Method onChangeMethod = MethodResolver.findMethod(screenControllerClass, onChange);
      if (onChangeMethod == null) {
        log.warning("method [" + onChange + "] not found in class [" + screenControllerClass.getName() + "]");
      } else if (onChangeMethod != null) {
        listener = new ControllerEventListener() {
          public void onChangeNotify() {
            try {
              onChangeMethod.invoke(screenController, controllers);
            } catch (Exception e) {
              log.warning("ControllerEventListener with error: " + e.getMessage());
            }
          }
        };
      }
        */
    }

    return listener;
  }

  public void applyAttributes(
      final Element element,
      final Attributes work,
      final NiftyRenderEngine renderEngine) {
    ApplyAttributes apply = new ApplyAttributes(new Convert(), work);
    apply.perform(element, renderEngine);
  }

  public void applyEffects(
      final Nifty nifty,
      final Screen screen,
      final Element element) {
    if (effects != null) {
      effects.materialize(nifty, element, screen, getControllersWithScreenController(screen));
    }
  }

  public void applyInteract(
      final Nifty nifty,
      final Screen screen,
      final Element element) {
    if (interact != null) {
      interact.materialize(nifty, element, getControllersWithScreenController(screen).toArray());
    }
  }

  protected void applyChildren(
      final Element parent,
      final Screen screen,
      final Nifty nifty) {
    for (ElementType elementType : elements) {
      elementType.create(
          parent,
          nifty,
          screen,
          new LayoutPart());
    }
  }

  public void refreshAttributes(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final ControlAttributes attributes) {
    Attributes attrib = new Attributes(getAttributes());
    attributes.refreshAttributes(attrib);
    applyAttributes(element, attrib, nifty.getRenderEngine());
    attributes.refreshEffects(effects);
    screen.layoutLayers();
  }

  protected InteractType getInteract() {
    return interact;
  }

  protected EffectsType getEffects() {
    return effects;
  }

  public Collection < ElementType > getElements() {
    return elements;
  }

  public void prepare(final Nifty nifty, final Screen screen, final ElementType rootElementType) {
    translateSpecialValues(nifty, screen);
    makeFlat();
    applyControls(nifty);
    applyStyles(nifty.getDefaultStyleResolver());
    makeFlatControls();
    resolveParameters(rootElementType.getAttributes());
    resolveControllers(new LinkedList < Object >());
  }

  public void translateSpecialValues(final Nifty nifty, final Screen screen) {
    super.translateSpecialValues(nifty, screen);
    interact.translateSpecialValues(nifty, screen);
    effects.translateSpecialValues(nifty, screen);
    for (ElementType e : elements) {
      e.translateSpecialValues(nifty, screen);
    }
  }

  void resolveParameters(final Attributes parentAttributes) {
    getAttributes().resolveParameters(parentAttributes);

    Attributes newParent = new Attributes(parentAttributes);
    newParent.merge(getAttributes());

    interact.resolveParameters(newParent);
    effects.resolveParameters(newParent);

    for (ElementType elementType : elements) {
      elementType.resolveParameters(newParent);
    }
  }

  void applyControls(final Nifty nifty) {
    internalApplyControl(nifty);
    for (ElementType elementType : elements) {
      elementType.applyControls(nifty);
    }
  }

  void internalApplyControl(final Nifty nifty) {
  }

  void makeFlatControls() {
    for (ElementType elementType : elements) {
      elementType.makeFlatControls();
    }
    makeFlatControlsInternal();
  }

  void makeFlatControlsInternal() {
  }

  public void applyStyles(final StyleResolver styleResolver) {
    StyleResolver childStyleResolver = applyStyleInternal(styleResolver);
    for (ElementType elementType : elements) {
      elementType.applyStyles(childStyleResolver);
    }
  }

  StyleResolver applyStyleInternal(final StyleResolver styleResolver) {
    String style = getAttributes().get("style");
    if (style != null) {
      StyleType styleType = styleResolver.resolve(style);
      if (styleType != null) {
        styleType.applyTo(this, styleResolver);
      }
      if (!style.startsWith("#")) {
        return new StyleResolverControlDefinintion(styleResolver, style);
      }
    }
    return styleResolver;
  }

  void resolveControllers(final LinkedList < Object > controllerParam) {
    controllers = new LinkedList < Object > (controllerParam);
    controller = createLocalController(getAttributes().get("controller"));
    if (controller != null) {
      controllers.addFirst(controller);
    }
    for (ElementType elementType : elements) {
      elementType.resolveControllers(controllers);
    }
  }

  public void removeWithTag(final String styleId) {
    getAttributes().removeWithTag(styleId);
    effects.removeWithTag(styleId);
    interact.getAttributes().removeWithTag(styleId);
  }
}
