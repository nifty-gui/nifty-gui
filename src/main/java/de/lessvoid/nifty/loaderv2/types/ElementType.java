package de.lessvoid.nifty.loaderv2.types;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.DefaultController;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loaderv2.types.apply.ApplyAttributes;
import de.lessvoid.nifty.loaderv2.types.apply.Convert;
import de.lessvoid.nifty.loaderv2.types.helper.CollectionLogger;
import de.lessvoid.nifty.loaderv2.types.resolver.parameter.ParameterResolver;
import de.lessvoid.nifty.loaderv2.types.resolver.parameter.ParameterResolverControl;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolverControlDefinintion;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.xml.tools.ClassHelper;
import de.lessvoid.xml.tools.MethodResolver;
import de.lessvoid.xml.xpp3.Attributes;

public abstract class ElementType extends XmlBaseType {
  private Logger log = Logger.getLogger(ElementType.class.getName());
  private InteractType interact;
  private EffectsType effects;
  private Collection < ElementType > elements = new ArrayList < ElementType >();

  public ElementType() {
    super();
  }

  public ElementType(final Attributes attributes) {
    super(attributes);
  }

  protected abstract ElementRenderer[] createElementRenderer(final Nifty nifty);

  public boolean hasElements() {
    return !elements.isEmpty();
  }

  public ElementType getFirstElement() {
    return elements.iterator().next();
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
    String result = "[element] " + super.output(offset);
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
      final LayoutPart layoutPart,
      final StyleResolver styleResolver,
      final ParameterResolver parameterResolver,
      final Attributes attrib,
      final Controller controller) {
    return internalCreate(
        parent,
        nifty,
        screen,
        layoutPart,
        styleResolver,
        attrib,
        parameterResolver,
        controller);
  }

  public Element createControl(
      final Element parent,
      final Nifty nifty,
      final Screen screen,
      final LayoutPart layoutPart,
      final StyleResolver styleResolver,
      final ParameterResolver parameterResolver,
      final Attributes attrib,
      final Attributes attribControlDefinition,
      final Attributes attribControl) {
    Attributes merge = new Attributes(attribControl);
    merge.merge(attrib);

    Element element = internalCreateElement(parent, nifty, screen, layoutPart, merge);
    StyleResolver controlStyleResolve = getControlStyleResolver(styleResolver, attribControlDefinition, attribControl);
    ParameterResolver controlParaResolv = new ParameterResolverControl(parameterResolver, attribControl);
    applyStyle(element, merge, screen, nifty, controlStyleResolve, controlParaResolv);
    applyAttributes(element, merge, nifty.getRenderEngine(), controlParaResolv);
    applyEffects(element, screen, nifty, controlParaResolv);

    Controller controller = createController(attribControlDefinition.get("controller"));
    applyInteract(nifty, element, screen.getScreenController(), controller);
    applyChildren(element, screen, nifty, controlStyleResolve, controlParaResolv, controller);

    ControllerEventListener listener = createControllerEventListener(
        attribControl,
        screen.getScreenController(),
        controller);
    controller.bind(
        nifty,
        element,
        attribControl.createProperties(),
        listener,
        attribControlDefinition);

    NiftyInputControl niftyInputControl = createNiftyInputControl(attribControlDefinition, controller);
    element.attachInputControl(niftyInputControl);

    return element;
  }

  private StyleResolver getControlStyleResolver(
      final StyleResolver styleResolver,
      final Attributes controlDefinitionAttributes,
      final Attributes controlAttributes) {
    String baseStyleId = controlAttributes.get("style");
    if (baseStyleId == null) {
      baseStyleId = controlDefinitionAttributes.get("style");
    }
    StyleResolver styleResolverDef = new StyleResolverControlDefinintion(styleResolver, baseStyleId);
    return styleResolverDef;
  }

  private Element internalCreate(
      final Element parent,
      final Nifty nifty,
      final Screen screen,
      final LayoutPart layoutPart,
      final StyleResolver styleResolver,
      final Attributes attrib,
      final ParameterResolver parameterResolverParam,
      final Controller controller) {
    ParameterResolver parameterResolver = new ParameterResolverControl(parameterResolverParam, attrib);
    Element element = internalCreateElement(parent, nifty, screen, layoutPart, attrib);
    applyStandard(nifty, screen, styleResolver, parameterResolver, attrib, element, controller);
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
        createElementRenderer(nifty));
    parent.add(element);
    return element;
  }

  private void applyStandard(
      final Nifty nifty,
      final Screen screen,
      final StyleResolver styleResolver,
      final ParameterResolver parameterResolver,
      final Attributes attrib,
      final Element element,
      Controller controller) {
    applyStyle(element, attrib, screen, nifty, styleResolver, parameterResolver);
    applyAttributes(element, attrib, nifty.getRenderEngine(), parameterResolver);
    applyEffects(element, screen, nifty, parameterResolver);

    Controller localController = createLocalController(attrib.get("inputController"));
    if (localController != null) {
      controller = localController;
    }

    applyInteract(nifty, element, screen.getScreenController(), controller);
    applyChildren(element, screen, nifty, styleResolver, parameterResolver, controller);

    if (localController != null) {
      ControllerEventListener listener = createControllerEventListener(
          attrib,
          screen.getScreenController(),
          controller);
      controller.bind(
          nifty,
          element,
          attrib.createProperties(),
          listener,
          attrib);

      NiftyInputControl niftyInputControl = createNiftyInputControl(attrib, controller);
      element.attachInputControl(niftyInputControl);
    }
  }

  private Controller createController(final String controllerClassParam) {
    Controller controller = ClassHelper.getInstance(controllerClassParam, Controller.class);
    if (controller == null) {
      log.warning("creating DefaultController instance");
      controller = new DefaultController();
    }

    return controller;
  }

  private Controller createLocalController(final String controllerClassParam) {
    if (controllerClassParam == null) {
      return null;
    }
    return ClassHelper.getInstance(controllerClassParam, Controller.class);
  }

  private NiftyInputControl createNiftyInputControl(
      final Attributes controlDefinitionAttributes,
      final Controller controller) {
  String inputMappingClass = controlDefinitionAttributes.get("inputMapping");
    if (inputMappingClass == null) {
      inputMappingClass = DefaultInputMapping.class.getName();
    }

    NiftyInputMapping inputMapping = ClassHelper.getInstance(inputMappingClass, NiftyInputMapping.class);
    return new NiftyInputControl(controller, inputMapping);
  }

  private ControllerEventListener createControllerEventListener(
      final Attributes attribControl,
      final ScreenController screenController,
      final Controller controller) {
    ControllerEventListener listener = null;

    String onChange = attribControl.get("onChange");
    if (onChange != null) {
      Class screenControllerClass = screenController.getClass();
      final Method onChangeMethod = MethodResolver.findMethod(screenControllerClass, onChange);
      if (onChangeMethod == null) {
        log.warning("method [" + onChange + "] not found in class [" + screenControllerClass.getName() + "]");
      } else if (onChangeMethod != null) {
        listener = new ControllerEventListener() {
          public void onChangeNotify() {
            try {
              onChangeMethod.invoke(screenController, controller);
            } catch (Exception e) {
              log.warning("ControllerEventListener with error: " + e.getMessage());
            }
          }
        };
      }
    }

    return listener;
  }

  private void applyStyle(
      final Element element,
      final Attributes attributes,
      final Screen screen,
      final Nifty nifty,
      final StyleResolver styleResolver,
      final ParameterResolver parameterResolver) {
    StyleType styleType = styleResolver.resolve(attributes.get("style"));
    if (styleType != null) {
      styleType.apply(styleResolver, attributes, nifty, element, screen, parameterResolver);
    }
  }

  private void applyAttributes(
      final Element element,
      final Attributes work,
      final NiftyRenderEngine renderEngine,
      final ParameterResolver parameterResolver) {
    ApplyAttributes apply = new ApplyAttributes(new Convert(), work);
    apply.perform(element, renderEngine, parameterResolver);
  }

  protected void applyEffects(
      final Element element,
      final Screen screen,
      final Nifty nifty,
      final ParameterResolver parameterResolver) {
    if (effects != null) {
      effects.materialize(nifty, element, screen, parameterResolver);
    }
  }

  protected void applyInteract(
      final Nifty nifty,
      final Element element,
      final Object ... controller) {
    if (interact != null) {
      interact.materialize(nifty, element, controller);
    }
  }

  protected void applyChildren(
      final Element parent,
      final Screen screen,
      final Nifty nifty,
      final StyleResolver styleResolver,
      final ParameterResolver parameterResolver,
      final Controller controller) {
    for (ElementType elementType : elements) {
      elementType.create(
          parent,
          nifty,
          screen,
          new LayoutPart(),
          styleResolver,
          parameterResolver,
          elementType.getAttributes(),
          controller);
    }
  }
}
