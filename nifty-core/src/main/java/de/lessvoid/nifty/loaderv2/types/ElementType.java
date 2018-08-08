package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyDefaults;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loaderv2.types.helper.CollectionLogger;
import de.lessvoid.nifty.loaderv2.types.helper.ElementRendererCreator;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolverControlDefinintion;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.tools.ClassHelper;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ElementType extends XmlBaseType {
  @Nullable
  protected String tagName;
  @Nullable
  protected ElementRendererCreator elementRendererCreator;
  @Nonnull
  protected InteractType interact = new InteractType();
  @Nonnull
  protected EffectsType effects = new EffectsType();
  @Nonnull
  protected final List<ElementType> elements = new ArrayList<ElementType>(0);
  @Nonnull
  protected Deque<Object> controllers = new LinkedList<Object>();
  @Nullable
  protected Controller controller;

  private boolean prepared;

  public ElementType() {
    super();
  }

  public ElementType(@Nonnull final ElementType src) {
    super(src);
    tagName = src.tagName;
    elementRendererCreator = src.elementRendererCreator;
    interact = new InteractType(src.interact);
    effects = new EffectsType(src.effects);
    copyElements(src);
  }

  public ElementType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  void mergeFromElementType(@Nonnull final ElementType src) {
    tagName = src.tagName;
    elementRendererCreator = src.elementRendererCreator;
    mergeFromAttributes(src.getAttributes());
    interact.mergeFromInteractType(src.getInteract());
    effects.mergeFromEffectsType(src.getEffects());
    copyElements(src);
  }

  void copyElements(@Nonnull final ElementType src) {
    elements.clear();
    for (ElementType element : src.elements) {
      elements.add(element.copy());
    }
  }

  @Nonnull
  public ElementType copy() {
    return new ElementType(this);
  }

  void setElementRendererCreator(@Nullable final ElementRendererCreator elementRendererCreatorParam) {
    elementRendererCreator = elementRendererCreatorParam;
  }

  void setTagName(@Nullable final String tagNameParam) {
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

  public void addElementType(@Nonnull final ElementType type) {
    elements.add(type);
  }

  public void addPanel(@Nonnull final ElementType panel) {
    elements.add(panel);
  }

  public void addImage(@Nonnull final ElementType image) {
    elements.add(image);
  }

  public void addLabel(@Nonnull final ElementType label) {
    elements.add(label);
  }

  public void addText(@Nonnull final ElementType text) {
    elements.add(text);
  }

  public void addControl(@Nonnull final ElementType text) {
    elements.add(text);
  }

  public void setInteract(@Nonnull final InteractType interactParam) {
    interact = interactParam;
  }

  public void setEffect(@Nonnull final EffectsType effectsParam) {
    effects = effectsParam;
  }

  @Override
  @Nonnull
  public String output(final int offset) {
    String result = StringHelper.whitespace(offset) + tagName + " [element] " + super.output(offset);
    result += "\n" + interact.output(offset + 1);
    result += "\n" + effects.output(offset + 1);
    result += "\n" + CollectionLogger.out(offset + 1, elements, "elements");
    return result;
  }

  @Nonnull
  public Element create(
      @Nonnull final Element parent,
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final LayoutPart layoutPart) {
    return create(parent, nifty, screen, layoutPart, parent.getChildrenCount());
  }

  @Nonnull
  public Element create(
      @Nonnull final Element parent,
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final LayoutPart layoutPart,
      final int index) {
    Element element = internalCreateElement(parent, nifty, screen, layoutPart, getAttributes(), index);
    applyStandard(nifty, screen, element);
    return element;
  }

  @Nonnull
  private Element internalCreateElement(
      @Nonnull final Element parent,
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final LayoutPart layoutPart,
      @Nonnull final Attributes attrib,
      final int index) {
    ElementRenderer[] renderer = null;
    if (elementRendererCreator != null) {
      renderer = elementRendererCreator.createElementRenderer(nifty);
    }
    if (renderer == null) {
      renderer = new ElementRenderer[0];
    }
    Element element = new Element(
        nifty,
        this,
        attrib.get("id"),
        parent,
        layoutPart,
        screen.getFocusHandler(),
        false,
        nifty.getTimeProvider(),
        renderer);
    parent.insertChild(element, index);
    return element;
  }

  private void applyStandard(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element) {
    applyAttributes(screen, element, getAttributes(), nifty.getRenderEngine());
    applyEffects(nifty, screen, element);
    applyInteract(nifty, screen, element);
    applyChildren(element, screen, nifty);
    enforceChildLayout(getAttributes(), elements.size());
    applyPostAttributes(element, getAttributes(), nifty.getRenderEngine());

    if (controller != null) {
      NiftyInputControl niftyInputControl = createNiftyInputControl(getAttributes(), controller);
      element.attachInputControl(niftyInputControl);
    }
  }

  private void enforceChildLayout(@Nonnull final Attributes attributes, final int childCount) {
    if (!attributes.isSet("childLayout") && childCount > 0) {
      throw new RuntimeException("missing childLayout attribute for an element with [" + childCount + "] child " +
          "elements. Attributes of error element [" + attributes.toString() + "]");
    }
  }

  @Nonnull
  List<Object> getControllersWithScreenController(@Nonnull final Screen screen) {
    List<Object> withScreenController = new LinkedList<Object>(controllers);
    withScreenController.add(screen.getScreenController());
    return withScreenController;
  }

  @Nullable
  private NiftyInputControl createNiftyInputControl(
      @Nonnull final Attributes controlDefinitionAttributes,
      @Nonnull final Controller controller) {
    String inputMappingClass = controlDefinitionAttributes.get("inputMapping");
    NiftyInputMapping inputMapping = null;
    if (inputMappingClass != null) {
      inputMapping = ClassHelper.getInstance(inputMappingClass, NiftyInputMapping.class);
    }
    if (inputMapping == null) {
      inputMapping = NiftyDefaults.getDefaultInputMapping();
    }

    return new NiftyInputControl(controller, inputMapping);
  }

  public void applyAttributes(
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nullable final Attributes work,
      @Nonnull final NiftyRenderEngine renderEngine) {
    if (work == null) {
      return;
    }
    element.initializeFromAttributes(screen, work, renderEngine);
  }

  public void applyPostAttributes(
      @Nonnull final Element element,
      @Nullable final Attributes work,
      @Nonnull final NiftyRenderEngine renderEngine) {
    if (work == null) {
      return;
    }
    element.initializeFromPostAttributes(work);
  }

  public void applyEffects(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element) {
    effects.materialize(nifty, element, screen, getControllersWithScreenController(screen));
  }

  public void applyInteract(
      final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element) {
    interact.materialize(nifty, element, getControllersWithScreenController(screen).toArray());
  }

  protected void applyChildren(
      @Nonnull final Element parent,
      @Nonnull final Screen screen,
      @Nonnull final Nifty nifty) {
    for (ElementType elementType : elements) {
      elementType.create(
          parent,
          nifty,
          screen,
          new LayoutPart());
    }
  }

  public void refreshAttributes(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final ControlAttributes attributes) {
    Attributes attrib = new Attributes(getAttributes());
    attributes.refreshAttributes(attrib);
    applyAttributes(screen, element, attrib, nifty.getRenderEngine());
    applyPostAttributes(element, attrib, nifty.getRenderEngine());
    attributes.refreshEffects(effects);

    element.getParent().layoutElements();
  }

  @Nonnull
  public InteractType getInteract() {
    return interact;
  }

  @Nonnull
  public EffectsType getEffects() {
    return effects;
  }

  @Nonnull
  public Collection<ElementType> getElements() {
    return elements;
  }

  public void prepare(
      @Nonnull final Nifty nifty,
      @Nullable final Screen screen,
      @Nonnull final ElementType rootElementType) {
    prepared = true;

    translateSpecialValues(nifty, screen);
    makeFlat();
    applyControls(nifty);
    applyStyles(nifty.getDefaultStyleResolver());

    // github issue #109: https://github.com/void256/nifty-gui/issues/109
    // resolveParameters() needs to be called before makeFlatControls() in case someone tries to change the id of
    // elements with some $parameter. Since makeFlatControls() resolves the ids it's necessary that this parameter
    // replacement already happend when makeFlatControls() is called.
    resolveParameters(rootElementType.getAttributes());

    makeFlatControls();

    // in case we have surviving special values (f.i. from applied controlDefinitions) we need to translate them too
    translateSpecialValues(nifty, screen);

    resolveControllers(nifty, new LinkedList<Object>());
  }

  public boolean isPrepared() {
    return this.prepared;
  }

  @Override
  public void translateSpecialValues(@Nonnull final Nifty nifty, @Nullable final Screen screen) {
    super.translateSpecialValues(nifty, screen);
    interact.translateSpecialValues(nifty, screen);
    effects.translateSpecialValues(nifty, screen);
    for (ElementType e : elements) {
      e.translateSpecialValues(nifty, screen);
    }
  }

  void resolveParameters(@Nonnull final Attributes parentAttributes) {
    getAttributes().resolveParameters(parentAttributes);

    Attributes newParent = new Attributes(parentAttributes);
    newParent.merge(getAttributes());

    interact.resolveParameters(newParent);
    effects.resolveParameters(newParent);

    for (ElementType elementType : elements) {
      elementType.resolveParameters(newParent);
    }
  }

  void applyControls(@Nonnull final Nifty nifty) {
    internalApplyControl(nifty);
    for (int i = 0; i < elements.size(); i++) {
      elements.get(i).applyControls(nifty);
    }
  }

  void internalApplyControl(@Nonnull final Nifty nifty) {
  }

  void makeFlatControls() {
    for (ElementType elementType : elements) {
      elementType.makeFlatControls();
    }
    makeFlatControlsInternal();
  }

  void makeFlatControlsInternal() {
  }

  public void applyStyles(@Nonnull final StyleResolver styleResolver) {
    StyleResolver childStyleResolver = applyStyleInternal(styleResolver);
    for (ElementType elementType : elements) {
      elementType.applyStyles(childStyleResolver);
    }
  }

  @Nonnull
  StyleResolver applyStyleInternal(@Nonnull final StyleResolver styleResolver) {
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

  public void attachController(@Nullable final Controller controller) {
    this.controller = controller;
  }

  void resolveControllers(@Nonnull final Nifty nifty, @Nonnull final Collection<Object> controllerParam) {
    controllers = new LinkedList<Object>(controllerParam);
    if(controller == null) {
      controller = nifty.getControllerFactory().create(getAttributes().get("controller"));
    }
    if (controller != null) {
      controllers.addFirst(controller);
    }
    for (ElementType elementType : elements) {
      elementType.resolveControllers(nifty, controllers);
    }
  }

  public void removeWithTag(@Nonnull final String styleId) {
    getAttributes().removeWithTag(styleId);
    effects.removeWithTag(styleId);
    interact.getAttributes().removeWithTag(styleId);
  }

  /**
   * usually when elements (incl. controls) are loaded they are all present when the xml
   * is being transformed into the runtime element tree. during this process each interact
   * method is being resolved, tracing all controllers from top to bottom leading a list
   * of controller instances for each method.
   * <p/>
   * when we're creating elements dynamically then every element below in the hierachry is
   * resolved the same way but everything above us (the parent and parent.parent and so on)
   * is not being linked, which leads to controllers missing.
   * <p/>
   * this call will now travel up the hierachry and collect all controllers and add them
   * to the element we're currently processing.
   */
  public void connectParentControls(@Nonnull final Element parent) {
    NiftyInputControl control = parent.getAttachedInputControl();
    if (control != null) {
      Controller controller = control.getController();
      controllers.addLast(controller);
    }
    if (parent.hasParent()) {
      connectParentControls(parent.getParent());
    }
  }
}
