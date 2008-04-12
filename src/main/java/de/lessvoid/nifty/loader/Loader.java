package de.lessvoid.nifty.loader;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import net.sourceforge.niftyGui.nifty.ControlDefinition;
import net.sourceforge.niftyGui.nifty.ControlType;
import net.sourceforge.niftyGui.nifty.EffectGroupType;
import net.sourceforge.niftyGui.nifty.EffectType;
import net.sourceforge.niftyGui.nifty.EffectsType;
import net.sourceforge.niftyGui.nifty.ElementType;
import net.sourceforge.niftyGui.nifty.ElementTypeBase;
import net.sourceforge.niftyGui.nifty.ImageType;
import net.sourceforge.niftyGui.nifty.InteractType;
import net.sourceforge.niftyGui.nifty.LayerGroupType;
import net.sourceforge.niftyGui.nifty.LayerType;
import net.sourceforge.niftyGui.nifty.LayoutType;
import net.sourceforge.niftyGui.nifty.MenuItemType;
import net.sourceforge.niftyGui.nifty.MenuType;
import net.sourceforge.niftyGui.nifty.NiftyDocument;
import net.sourceforge.niftyGui.nifty.NiftyType;
import net.sourceforge.niftyGui.nifty.PanelType;
import net.sourceforge.niftyGui.nifty.RegisterEffectType;
import net.sourceforge.niftyGui.nifty.RegisterMusicType;
import net.sourceforge.niftyGui.nifty.RegisterSoundType;
import net.sourceforge.niftyGui.nifty.ScreenType;
import net.sourceforge.niftyGui.nifty.TextType;
import net.sourceforge.niftyGui.nifty.ScreenType.LayerGroup;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.general.Effect;
import de.lessvoid.nifty.effects.shared.Falloff;
import de.lessvoid.nifty.elements.Controller;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.elements.tools.MethodResolver;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.layout.manager.AbsolutePositionLayout;
import de.lessvoid.nifty.layout.manager.CenterLayout;
import de.lessvoid.nifty.layout.manager.HorizontalLayout;
import de.lessvoid.nifty.layout.manager.OverlayLayout;
import de.lessvoid.nifty.layout.manager.VerticalLayout;
import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.render.RenderImage;
import de.lessvoid.nifty.render.RenderImage.SubImageMode;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * loader.
 * @author void
 */
public class Loader {

  /**
   * logger.
   */
  private static Logger log = Logger.getLogger(Loader.class.getName());

  /**
   * effect groups.
   */
  private static Hashtable < String, EffectGroupType > effectGroups =
    new Hashtable < String, EffectGroupType > ();

  /**
   * layer groups.
   */
  private static Hashtable < String, LayerGroupType > layerGroups =
    new Hashtable < String, LayerGroupType > ();

  /**
   * registered effect classes.
   */
  private static Hashtable < String, RegisterEffectType > registerEffects =
    new Hashtable < String, RegisterEffectType > ();

  /**
   * registered controls.
   */
  private static Hashtable < String, ControlDefinition > registeredControls =
    new Hashtable < String, ControlDefinition > ();

  /**
   * load xml.
   * @param niftyParent
   * @param screens
   * @param r
   * @param filename
   * @param timeProvider TODO
   * @throws Exception
   */
  public static void loadXml(
      final Nifty niftyParent,
      final Map < String, Screen > screens,
      final RenderDevice r,
      final String filename,
      final TimeProvider timeProvider) throws Exception {

    log.info("loadXml: " + filename);
    NiftyDocument niftyDoc =
      NiftyDocument.Factory.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(filename));

    log.info("parse done");
    if (XmlBeansHelper.validate(niftyDoc)) {
      log.info("is valid");
      NiftyType nifty = niftyDoc.getNifty();

      // register effects
      for (int i = 0; i < nifty.sizeOfRegisterEffectArray(); i++) {
        RegisterEffectType registerEffectType = nifty.getRegisterEffectArray(i);
        registerEffect(registerEffectType);
      }

      // register sound
      for (int i = 0; i < nifty.sizeOfRegisterSoundArray(); i++) {
        RegisterSoundType registerSoundType = nifty.getRegisterSoundArray(i);
        registerSound(niftyParent.getSoundSystem(), registerSoundType);
      }

      // register music
      for (int i = 0; i < nifty.sizeOfRegisterMusicArray(); i++) {
        RegisterMusicType registerMusicType = nifty.getRegisterMusicArray(i);
        registerMusic(niftyParent.getSoundSystem(), registerMusicType);
      }

      // register effect groups
      for (int i = 0; i < nifty.sizeOfEffectGroupArray(); i++) {
        EffectGroupType effectGroupType = nifty.getEffectGroupArray(i);
        registerEffectGroup(effectGroupType);
      }

      // register controls groups
      for (int i = 0; i < nifty.sizeOfControlDefinitionArray(); i++) {
        ControlDefinition controlDefinitionType = nifty.getControlDefinitionArray(i);
        registerControlDefinition(controlDefinitionType);
      }

      // register layert groups
      for (int i = 0; i < nifty.sizeOfLayerGroupArray(); i++) {
        LayerGroupType layerGroupType = nifty.getLayerGroupArray(i);
        registerLayerGroup(layerGroupType);
      }

      // load screen definitions
      for (int i = 0; i < nifty.sizeOfScreenArray(); i++) {
        ScreenType screenType = nifty.getScreenArray(i);
        screens.put(screenType.getId(), process(niftyParent, screenType, r, timeProvider));
      }
    }
  }

  /**
   * register control definition.
   * @param controlDefinitionType TODO
   */
  private static void registerControlDefinition(
      final ControlDefinition controlDefinitionType) {
    log.info("register control: " + controlDefinitionType.getName());
    registeredControls.put(controlDefinitionType.getName(), controlDefinitionType);
  }

  /**
   * register layer group.
   * @param layerGroupType layerGroupType
   */
  private static void registerLayerGroup(final LayerGroupType layerGroupType) {
    log.info("register layer group: " + layerGroupType.getId());
    layerGroups.put(layerGroupType.getId(), layerGroupType);
  }

  /**
   * register sound.
   * @param soundSystem SoundSystem
   * @param registerSoundType RegisterSoundType to register
   */
  private static void registerSound(final SoundSystem soundSystem, final RegisterSoundType registerSoundType) {
    String name = registerSoundType.getId();
    String file = registerSoundType.getFilename();
    log.info("register sound: " + name + " -> " + file);
    soundSystem.addSound(name, file);
  }

  /**
   * register music.
   * @param soundSystem SoundSystem
   * @param registerMusicType RegisterMusicType to register
   */
  private static void registerMusic(final SoundSystem soundSystem, final RegisterMusicType registerMusicType) {
    String name = registerMusicType.getId();
    String file = registerMusicType.getFilename();
    log.info("register music: " + name + " -> " + file);
    soundSystem.addMusic(name, file);
  }

  /**
   * register effect.
   * @param registerEffectType register effect type to register
   */
  private static void registerEffect(final RegisterEffectType registerEffectType) {
    log.info("register effect: " + registerEffectType.getName());
    registerEffects.put(registerEffectType.getName(), registerEffectType);
  }

  /**
   * register effect group.
   * @param effectGroupType the effect group to register
   */
  private static void registerEffectGroup(final EffectGroupType effectGroupType) {
    log.info("register effect group: " + effectGroupType.getId());
    effectGroups.put(effectGroupType.getId(), effectGroupType);
  }

  /**
   * process a screen.
   * @param niftyParent
   * @param screenType
   * @param r
   * @param timeProvider TODO
   * @return
   * @throws Exception
   */
  private static Screen process(
      final Nifty niftyParent,
      final ScreenType screenType,
      final RenderDevice r,
      final TimeProvider timeProvider) throws Exception {
    if (!screenType.isSetController()) {
      log.warning("required screencontroler for id: " + screenType.getId());
    }
    ScreenController controller = getController(r, niftyParent, screenType.getController());
    Screen screen = new Screen(screenType.getId(), controller, timeProvider);
    controller.bind(niftyParent, screen);

    // process layer groups
    for (int i = 0; i < screenType.sizeOfLayerGroupArray(); i++) {
      LayerGroup layerGroup = screenType.getLayerGroupArray(i);
      LayerGroupType layerGroupType = layerGroups.get(layerGroup.getId());

      for (int j = 0; j < layerGroupType.sizeOfLayerArray(); j++) {
        LayerType current = layerGroupType.getLayerArray(j);
        Element layer = processLayer(niftyParent, r, controller, screen, current);

        if (layerGroup.isSetAlternate()) {
          layer.setAlternateKey(layerGroup.getAlternate());
        }
      }
    }

    // process normal layers
    for (int i = 0; i < screenType.sizeOfLayerArray(); i++) {
      processLayer(niftyParent, r, controller, screen, screenType.getLayerArray(i));
    }

    // effect group?
    if (screenType.isSetEffectGroup()) {
      String effectId = screenType.getEffectGroup();
    }

    return screen;
  }

  /**
   * @param niftyParent
   * @param renderDevice
   * @param controller
   * @param screen
   * @param layerType
   * @return
   */
  private static Element processLayer(
      final Nifty niftyParent,
      final RenderDevice renderDevice,
      final ScreenController controller,
      final Screen screen,
      final LayerType layerType) {

    // create root panel
    Element rootPanel = createLayer(layerType, niftyParent, screen);
    screen.addLayerElement(rootPanel);

    // process children
    for (int panelIdx = 0; panelIdx < layerType.sizeOfElementBaseArray(); panelIdx++) {
      ElementTypeBase w = layerType.getElementBaseArray(panelIdx);
      processElement(niftyParent, screen, w, rootPanel, renderDevice, controller);
    }

    return rootPanel;
  }

  /**
   * dynamically load the given class, create and return a new instance.
   * @param renderDevice TODO
   * @param controllerClass
   * @return new ScreenController instance or null
   */
  private static ScreenController getController(
      final RenderDevice renderDevice,
      final Nifty niftyParent,
      final String controllerClass) {
    try {
      Class<?> cls= Loader.class.getClassLoader().loadClass( controllerClass );
      if( ScreenController.class.isAssignableFrom( cls )) {
        return (ScreenController)cls.newInstance();
      } else {
        log.warning( "given screenController class [" + controllerClass + "] does not implement [" + ScreenController.class.getName() + "]" );
      }
    } catch ( Exception e ) {
      log.warning( "class [" + controllerClass + "] could not be instanziated" );
    }
    return null;
  }

  /**
   * Get Controller by name of class.
   * @param controllerClass the name of the Controller implementation.
   * @return new instance of the Controller
   */
  private static Controller getController(final String controllerClass) {
    try {
      Class < ? > cls = Loader.class.getClassLoader().loadClass(controllerClass);
      if (Controller.class.isAssignableFrom(cls)) {
        return (Controller) cls.newInstance();
      } else {
        log.warning("given class [" + controllerClass + "] does not implement [" + Controller.class.getName() + "]");
      }
    } catch (Exception e) {
      log.warning("class [" + controllerClass + "] could not be instanziated");
    }
    return null;
  }

  /**
   * create a new layer.
   * @param layerType LayerType
   * @param nifty RenderDevice
   * @param parentScreen TODO
   * @return new Element representing the Layer
   */
  private static Element createLayer(
      final LayerType layerType,
      final Nifty nifty,
      final Screen parentScreen) {
    LayoutPart layerLayout = new LayoutPart();
    layerLayout.getBox().setX(0);
    layerLayout.getBox().setY(0);
    layerLayout.getBox().setWidth(nifty.getRenderDevice().getWidth());
    layerLayout.getBox().setHeight(nifty.getRenderDevice().getHeight());
    Element layer = new Element( layerType.getId(), null, layerLayout, parentScreen, false, createPanelRenderer(layerType, nifty.getRenderDevice()));
    processElement(nifty, layerType, layer);
    return layer;
  }

  private static Element createPanel(PanelType panelType, Element parent, Nifty nifty, Screen parentScreen) {
    Element layer = new Element( panelType.getId(), parent, parentScreen, false, createPanelRenderer(panelType, nifty.getRenderDevice()));
    processElement(nifty, panelType, layer);
    return layer;
  }

  /**
   * @param nifty Nifty
   * @param panelType PanelType
   * @param layer Element
   */
  private static void processElement(
      final Nifty nifty,
      final PanelType panelType,
      final Element layer) {
    // effects
    processEffects(panelType, layer, nifty);
    handleLayout(panelType, layer);
  }

  /**
   * @param panelType
   * @param r
   * @return
   */
  private static PanelRenderer createPanelRenderer(
      final ElementType panelType,
      final RenderDevice r) {
    PanelRenderer panelRenderer = new PanelRenderer();

    if (panelType.isSetBackgroundColor()) {
      panelRenderer.setBackgroundColor(new Color(panelType.getBackgroundColor()));
    }

    if (panelType.isSetBackgroundImage()) {
      panelRenderer.setBackgroundImage(r.createImage(panelType.getBackgroundImage(), true));
    }

    return panelRenderer;
  }

  private static Element processElement(
      final Nifty niftyParent,
      final Screen parentScreen,
      final ElementTypeBase elementType,
      final Element parentWidget,
      final RenderDevice r,
      final Object controller) {
    Element current = null;

    if (elementType instanceof ControlType) {
      final ControlType controlType = (ControlType) elementType;
      ControlDefinition controlDefinition = registeredControls.get(controlType.getName());

      final Controller c = getController(controlDefinition.getController());
      ControllerEventListener listener = null;

      // onClick action
      if (controlType.isSetOnChange()) {
        final Method onChangeMethod = MethodResolver.findMethod(controller.getClass(), controlType.getOnChange());
        if (onChangeMethod == null) {
          log.warning("method [" + controlType.getOnChange() + "] "
                   + "not found in class [" + controller.getClass().getName() + "]");
        } else {
          if (onChangeMethod != null) {
            listener = new ControllerEventListener() {
              public void onChangeNotify() {
                try {
                  onChangeMethod.invoke(controller, c);
                } catch (Exception e) {
                  log.warning("ControllerEventListener with error: " + e);
                }
              }
            };
          }
        }
      }

      // get very first child if available
      if (controlDefinition.sizeOfElementBaseArray() == 1) {
        ElementTypeBase w = controlDefinition.getElementBaseArray(0);
        current = processElement(niftyParent, parentScreen, w, parentWidget, r, c);
        processElementAttributes(niftyParent, controlType, current, c);
        c.bind(parentScreen, current, XmlBeansHelper.createProperties(controlType), listener);
      }
    } else if (elementType instanceof ImageType) {
      current = processImage(niftyParent, elementType, parentWidget, parentScreen);
      finishProcess(niftyParent, parentScreen, (ElementType)elementType, parentWidget, current, controller);
      processChildren(niftyParent, parentScreen, (ElementType)elementType, r, current, controller);
    } else if (elementType instanceof PanelType) {
      Element newPanel = createPanel((PanelType) elementType, parentWidget, niftyParent, parentScreen);
      current = newPanel;
      finishProcess(niftyParent, parentScreen, (ElementType)elementType, parentWidget, current, controller);
      processChildren(niftyParent, parentScreen, (ElementType)elementType, r, current, controller);
    } else if (elementType instanceof TextType) {
      TextType textType = (TextType) elementType;

      TextRenderer textRenderer;
      if (textType.isSetColor()) {
        textRenderer = new TextRenderer(
            r.createFont(textType.getFont()), textType.getText(), new Color(textType.getColor()));
      } else {
        textRenderer = new TextRenderer(
            r.createFont(textType.getFont()), textType.getText());
      }

      current = new Element(((ElementType)elementType).getId(), parentWidget, parentScreen, true, createPanelRenderer(textType, r), textRenderer);
      current.setConstraintHeight(new SizeValue(textRenderer.getTextHeight() + "px"));
      current.setConstraintWidth(new SizeValue(textRenderer.getTextWidth() + "px"));
      processEffects(textType, current, niftyParent);
      finishProcess(niftyParent, parentScreen, (ElementType)elementType, parentWidget, current, controller);
      processChildren(niftyParent, parentScreen, (ElementType)elementType, r, current, controller);
    } else if (elementType instanceof MenuType) {
      MenuType menuType = (MenuType) elementType;

      Element menu = new Element(((ElementType)elementType).getId(), parentWidget, parentScreen, true, createPanelRenderer(menuType, r));
      current = menu;

      processEffects(menuType, menu, niftyParent);
      finishProcess(niftyParent, parentScreen, (ElementType)elementType, parentWidget, current, controller);

      for (int panelIdx=0; panelIdx<((ElementType)elementType).sizeOfElementBaseArray(); panelIdx++) {
        ElementTypeBase w = ((ElementType)elementType).getElementBaseArray( panelIdx );
        processMenuItem(menu, menuType, niftyParent, parentScreen, (ElementType)w, r, controller);
      }
    }

    return current;
  }

  private static void processMenuItem(
      final Element menu,
      final MenuType menuType,
      final Nifty niftyParent,
      final Screen parentScreen,
      final ElementType elementType,
      final RenderDevice r,
      final Object controller) {
    if (elementType instanceof MenuItemType) {
      MenuItemType menuItemType = (MenuItemType) elementType;

      TextRenderer textRenderer;
      if (menuItemType.isSetColor()) {
        textRenderer = new TextRenderer(
            r.createFont(menuType.getFont()), menuItemType.getText(), new Color(menuItemType.getColor()));
      } else {
        textRenderer = new TextRenderer(
            r.createFont(menuType.getFont()), menuItemType.getText());
      }

      Element current = new Element(elementType.getId(), menu, parentScreen, true, textRenderer);
      current.setConstraintWidth(new SizeValue(textRenderer.getTextWidth() + "px"));
      current.setConstraintHeight(new SizeValue(textRenderer.getTextHeight() + "px"));

      if (menuType.isSetAlign()) {
        current.setConstraintHorizontalAlign(HorizontalAlign.valueOf(menuType.getAlign().toString()));
      }

      if (menuType.isSetValign()) {
        current.setConstraintVerticalAlign(VerticalAlign.valueOf(menuType.getValign().toString()));
      }

      processEffects(menuItemType, current, niftyParent);
      finishProcess(niftyParent, parentScreen, elementType, menu, current, controller);
      processChildren(niftyParent, parentScreen, elementType, r, current, controller);

      menu.setConstraintWidth(getMenuMaxWidth(menu.getElements()));
      menu.setConstraintHeight(getMenuMaxHeight(menu.getElements()));
    }
  }

  /**
   * get max height of all elements.
   * @param elements the elements to check
   * @return max height
   */
  private static SizeValue getMenuMaxHeight(final List < Element > elements) {
    if (elements == null || elements.isEmpty()) {
      return null;
    }

    int sum = 0;
    for (Element e : elements) {
      SizeValue current = e.getConstraintHeight();
      if (current.isPercentOrPixel()) {
        sum += current.getValueAsInt(0);
      }
    }
    return new SizeValue(sum + "px");
  }

  /**
   * get max width of all elements.
   * @param elements the elements to check
   * @return max width
   */
  private static SizeValue getMenuMaxWidth(final List < Element > elements) {
    if (elements == null || elements.isEmpty()) {
      return null;
    }

    int max = -1;
    for (Element e : elements) {
      SizeValue current = e.getConstraintWidth();
      if (current.isPercentOrPixel()) {
        int value = current.getValueAsInt(0);
        if (value > max) {
          max = value;
        }
      }
    }
    return new SizeValue(max + "px");
  }

  /**
   * @param niftyParent
   * @param parentScreen
   * @param ElementType
   * @param r
   * @param current
   * @param controller TODO
   */
  private static void processChildren(
      final Nifty niftyParent,
      final Screen parentScreen,
      final ElementType ElementType,
      final RenderDevice r,
      final Element current,
      final Object controller) {
    // process children
    for (int panelIdx=0; panelIdx<ElementType.sizeOfElementBaseArray(); panelIdx++) {
      ElementTypeBase w = ElementType.getElementBaseArray(panelIdx);
      processElement(niftyParent, parentScreen, w, current, r, controller);
    }
  }

  /**
   * @param nifty TODO
   * @param parentScreen
   * @param elementType
   * @param parentWidget
   * @param current
   * @param controller TODO
   */
  private static void finishProcess(
      final Nifty nifty,
      final Screen parentScreen,
      final ElementType elementType,
      final Element parentWidget,
      final Element current,
      final Object controller) {
    // process widget attributes
    processElementAttributes(nifty, elementType, current, controller);
    handleLayout(elementType, current);

    // add to current parent
    parentWidget.add(current);
  }

  /**
   * @param nifty TODO
   * @param elementType ElementType
   * @param parentWidget parent Widget
   * @param parentScreen TODO
   * @return the new Image Element
   */
  private static Element processImage(
      final Nifty nifty,
      final ElementTypeBase elementType,
      final Element parentWidget,
      final Screen parentScreen) {
    Element current;
    ImageType imageType = (ImageType) elementType;

    // create the actual image
    boolean filter = true;
    if (imageType.isSetFilter()) {
      filter = imageType.getFilter();
    }

    RenderDevice r = nifty.getRenderDevice();
    RenderImage image = r.createImage(imageType.getFilename(), filter);

    // create the imager renderer
    ImageRenderer imageRenderer =
      new ImageRenderer(image);

    // create a new element with the given renderer
    current = new Element(((ElementType)elementType).getId(), parentWidget, parentScreen, true, imageRenderer);

    // set absolute x position when given
    if (imageType.isSetX()) {
      current.setConstraintX(new SizeValue(imageType.getX() + "px"));
    }

    // set absolute y position when given
    if (imageType.isSetY()) {
      current.setConstraintY(new SizeValue(imageType.getY() + "px"));
    }

    // sub image enable?
    if (imageType.isSetSubImageSizeMode()) {
      if (imageType.getSubImageSizeMode().equals(ImageType.SubImageSizeMode.SCALE)) {
        image.setSubImageMode(SubImageMode.Scale);
      } else if (imageType.getSubImageSizeMode().equals(ImageType.SubImageSizeMode.RESIZE_HINT)) {
        image.setSubImageMode(SubImageMode.ResizeHint);
      }
    }

    // resize hint available?
    if (imageType.isSetResizeHint()) {
      image.setResizeHint(imageType.getResizeHint());
      image.setSubImageMode(SubImageMode.ResizeHint);
    }

    // set width and height to image width and height (for now)
    current.setConstraintWidth(new SizeValue(image.getWidth() + "px"));
    current.setConstraintHeight(new SizeValue(image.getHeight() + "px"));

    // process element effects
    processEffects(imageType, current, nifty);

    // return the newly created element
    return current;
  }

  private static void processElementAttributes(
      final Nifty nifty, ElementType elementType, Element element, Object controller ) {

    // attach to the given screen
    element.bindToScreen(nifty);

    // height
    if (elementType.isSetHeight()) {
      SizeValue height = new SizeValue(elementType.getHeight());
      element.setConstraintHeight(height);
    }

    // width
    if (elementType.isSetWidth()) {
      SizeValue width = new SizeValue(elementType.getWidth());
      element.setConstraintWidth(width);
    }

    // horizontal align
    if (elementType.isSetAlign()) {
      element.setConstraintHorizontalAlign(HorizontalAlign.valueOf(elementType.getAlign().toString()));
    }

    // vertical align
    if (elementType.isSetValign()) {
      element.setConstraintVerticalAlign(VerticalAlign.valueOf(elementType.getValign().toString()));
    }

    // child clip
    if (elementType.isSetChildClip()) {
      element.setClipChildren(elementType.getChildClip());
    }

    // onClick action
    if (elementType.getInteract() != null) {
      InteractType interactType = elementType.getInteract();
      if (interactType.isSetOnClick()) {
        String methodName = interactType.getOnClick();
        Method onClickMethod = MethodResolver.findMethod(controller.getClass(), methodName);
        if (onClickMethod == null) {
          log.warning("method [" + methodName + "] not found in class [" + controller.getClass().getName() + "]");
        } else {
          element.setOnClickMethod(onClickMethod, controller, false);
        }
      }

      // onClick action
      if (interactType.isSetOnClickRepeat()) {
        String methodName = interactType.getOnClickRepeat();
        Method onClickMethod = MethodResolver.findMethod(controller.getClass(), methodName);
        if (onClickMethod == null) {
          log.warning("method [" + methodName + "] not found in class [" + controller.getClass().getName() + "]");
        } else {
          element.setOnClickMethod(onClickMethod, controller, true);
        }
      }

      // onClickMouseMove action
      if (interactType.isSetOnClickMouseMove()) {
        String methodName = interactType.getOnClickMouseMove();
        Method onClickMouseMoveMethod = MethodResolver.findMethod(controller.getClass(), methodName);
        if (onClickMouseMoveMethod == null) {
          log.warning("method [" + methodName + "] not found in class [" + controller.getClass().getName() + "]");
        } else {
          element.setOnClickMouseMoveMethod(onClickMouseMoveMethod, controller);
        }
      }

      // on click alternate
      if (interactType.isSetOnClickAlternateKey()) {
        element.setOnClickAlternateKey(interactType.getOnClickAlternateKey());
      }
    }

    //
    if (elementType.isSetVisibleToMouse()) {
      element.setVisibleToMouseEvents(elementType.getVisibleToMouse());
    }

    // falloff
    Properties prop = new Properties();

    if (elementType.getHover() != null && elementType.getHover().isSetWidth()) {
      prop.put( Falloff.HOVER_WIDTH, elementType.getHover().getWidth());
    }

    if (elementType.getHover() != null && elementType.getHover().isSetHeight()) {
      prop.put( Falloff.HOVER_HEIGHT, elementType.getHover().getHeight());
    }

    if (elementType.getHover() != null && elementType.getHover().isSetFalloffType()) {
      prop.put( Falloff.HOVER_FALLOFF_TYPE, elementType.getHover().getFalloffType().toString());
    }

    if (elementType.getHover() != null && elementType.getHover().isSetFalloffConstraint()) {
      prop.put( Falloff.HOVER_FALLOFF_CONSTRAINT, elementType.getHover().getFalloffConstraint().toString());
    }

    //
    if( !prop.isEmpty()) {
      Falloff falloff= new Falloff( prop );
      element.setHotSpotFalloff( falloff );
    }
  }

  /**
   * Handle layout.
   * @param panelType the panel type
   * @param newPanel the panel
   */
  private static void handleLayout(final ElementType panelType, final Element newPanel) {
    // layout
    if (panelType.getChildLayout() != null) {
      LayoutType.Enum layout = panelType.getChildLayout();
      if (layout != null) {
        if (layout.equals(LayoutType.ABSOLUTE)) {
          newPanel.setLayoutManager(new AbsolutePositionLayout());
        } else if (layout.equals(LayoutType.VERTICAL)) {
          newPanel.setLayoutManager(new VerticalLayout());
        } else if (layout.equals(LayoutType.CENTER)) {
          newPanel.setLayoutManager(new CenterLayout());
        } else if (layout.equals(LayoutType.HORIZONTAL)) {
          newPanel.setLayoutManager(new HorizontalLayout());
        } else if (layout.equals(LayoutType.OVERLAY)) {
          newPanel.setLayoutManager(new OverlayLayout());
        } else {
          log.warning("unsupported layout: " + layout.toString());
        }
      }
    }
  }

  /**
   * process effects read from the given elementType for the given element.
   * @param elementType the elementType
   * @param element the element
   * @param nifty the renderDevice
   */
  private static void processEffects(
      final ElementType elementType,
      final Element element,
      final Nifty nifty) {
    if (elementType.getEffect() != null && elementType.getEffect().isSetEffectGroup()) {
      String effectGroupId = elementType.getEffect().getEffectGroup();
      if (effectGroupId.indexOf(",") != -1) {
        for (String effectGroup : effectGroupId.split(",")) {
          handleEffectGroup(effectGroup, element, nifty);
        }
      } else {
        handleEffectGroup(effectGroupId, element, nifty);
      }
    }

    EffectsType effects = elementType.getEffect();
    if (effects != null) {
      processEffect(EffectEventId.onClick, effects.getOnClickArray(), element, nifty);
      processEffect(EffectEventId.onEndScreen, effects.getOnEndScreenArray(), element, nifty);
      processEffect(EffectEventId.onFocus, effects.getOnFocusArray(), element, nifty);
      processEffect(EffectEventId.onHover, effects.getOnHoverArray(), element, nifty);
      processEffect(EffectEventId.onStartScreen, effects.getOnStartScreenArray(), element, nifty);
      processEffect(EffectEventId.onActive, effects.getOnActiveArray(), element, nifty);
    }
  }

  /**
   * apply effect group to an element.
   * @param effectGroupId the effect group to apply
   * @param element the element
   * @param nifty the renderDevice
   */
  private static void handleEffectGroup(
      final String effectGroupId,
      final Element element,
      final Nifty nifty) {
    if (effectGroups.containsKey(effectGroupId)) {
      EffectGroupType effectGroupType = effectGroups.get(effectGroupId);
      processEffect(EffectEventId.onClick, effectGroupType.getOnClickArray(), element, nifty);
      processEffect(EffectEventId.onEndScreen, effectGroupType.getOnEndScreenArray(), element, nifty);
      processEffect(EffectEventId.onFocus, effectGroupType.getOnFocusArray(), element, nifty);
      processEffect(EffectEventId.onStartScreen, effectGroupType.getOnStartScreenArray(), element, nifty);
      processEffect(EffectEventId.onHover, effectGroupType.getOnHoverArray(), element, nifty);
      processEffect(EffectEventId.onActive, effectGroupType.getOnActiveArray(), element, nifty);
    } else {
      log.warning("missing effectGroup: " + effectGroupId);
    }
  }

  /**
   * process a single effect.
   * @param effectEventId the effect type id
   * @param effects the effectType array
   * @param e the element
   * @param nifty the RenderDevice
   */
  private static void processEffect(
      final EffectEventId effectEventId,
      final EffectType[] effects,
      final Element e,
      final Nifty nifty) {
    for (int i = 0; i < effects.length; i++) {
      Effect effect = EffectConvert.convertEffect(nifty, effectEventId, e, registerEffects, effects[ i ]);
      e.registerEffect(effectEventId, effect);
    }
  }
}
