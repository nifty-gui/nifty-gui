package de.lessvoid.nifty;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEventQueue;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loaderv2.NiftyFactory;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.ControlDefinitionType;
import de.lessvoid.nifty.loaderv2.types.NiftyType;
import de.lessvoid.nifty.loaderv2.types.PopupType;
import de.lessvoid.nifty.loaderv2.types.RegisterEffectType;
import de.lessvoid.nifty.loaderv2.types.StyleType;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolverDefault;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.NiftyRenderEngineImpl;
import de.lessvoid.nifty.screen.NullScreen;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.resourceloader.ResourceLoader;

/**
 * The main Nifty class.
 * @author void
 */
public class Nifty {
  private Logger log = Logger.getLogger(Nifty.class.getName());
  private NiftyRenderEngine renderEngine;
  private SoundSystem soundSystem;
  private Map < String, Screen > screens = new Hashtable < String, Screen >();
  private Map < String, PopupType > popups = new Hashtable < String, PopupType >();
  private Map < String, Element > activePopups = new Hashtable < String, Element >();
  private Map < String, StyleType > styles = new Hashtable < String, StyleType >();
  private Map < String, ControlDefinitionType > controlDefintions = new Hashtable < String, ControlDefinitionType >();
  private Map < String, RegisterEffectType > registeredEffects = new Hashtable < String, RegisterEffectType >();
  private Screen currentScreen = new NullScreen();
  private String currentLoaded;
  private boolean exit;
  private NiftyDebugConsole console;
  private TimeProvider timeProvider;
  private List < RemovePopUp > removePopupList = new ArrayList < RemovePopUp >();
  private NiftyLoader loader;
  private List < ControlToAdd > controlsToAdd = new ArrayList < ControlToAdd >();
  private List < ElementToRemove > elementsToRemove = new ArrayList < ElementToRemove >();
  private boolean useDebugConsole;
  private MouseInputEventQueue mouseInputEventQueue;
  private Collection < ScreenController > registeredScreenControllers = new ArrayList < ScreenController >();
  private String alternateKeyForNextLoadXml;
  private long lastTime;
  private InputSystem inputSystem;
  private boolean gotoScreenInProgess;
  private String alternateKey;
  private Collection < DelayedMethodInvoke > delayedMethodInvokes = new ArrayList < DelayedMethodInvoke > (); 

  /**
   * Create nifty for the given RenderDevice and TimeProvider.
   * @param newRenderDevice the RenderDevice
   * @param newSoundSystem SoundSystem
   * @param newInputSystem TODO
   * @param newTimeProvider the TimeProvider
   */
  public Nifty(
      final NiftyRenderEngine newRenderDevice,
      final SoundSystem newSoundSystem,
      final InputSystem newInputSystem,
      final TimeProvider newTimeProvider) {
    initialize(newRenderDevice, newSoundSystem, newInputSystem, newTimeProvider);
    console = new NiftyDebugConsole(null); // this will cause trouble i'm sure, but i don't care at this point
  }

  /**
   * Create nifty with optional console parameter.
   * @param newRenderDevice the RenderDevice
   * @param newSoundSystem SoundSystem
   * @param inputSystem TODO
   * @param newTimeProvider the TimeProvider
   */
  public Nifty(
      final RenderDevice newRenderDevice,
      final SoundSystem newSoundSystem,
      final InputSystem newInputSystem,
      final TimeProvider newTimeProvider) {
    initialize(new NiftyRenderEngineImpl(newRenderDevice), newSoundSystem, newInputSystem, newTimeProvider);
    console = new NiftyDebugConsole(newRenderDevice);
  }

  /**
   * Initialize this instance.
   * @param newRenderDevice RenderDevice
   * @param newSoundSystem SoundSystem
   * @param newInputSystem TODO
   * @param newTimeProvider TimeProvider
   */
  private void initialize(
      final NiftyRenderEngine newRenderDevice,
      final SoundSystem newSoundSystem,
      final InputSystem newInputSystem,
      final TimeProvider newTimeProvider) {
    this.renderEngine = newRenderDevice;
    this.soundSystem = newSoundSystem;
    this.inputSystem = newInputSystem;
    this.timeProvider = newTimeProvider;
    this.exit = false;
    this.currentLoaded = null;
    this.mouseInputEventQueue = new MouseInputEventQueue(renderEngine.getHeight());
    this.lastTime = timeProvider.getMsTime();

    try {
      loader = new NiftyLoader();
      loader.registerSchema("nifty.nxs", ResourceLoader.getResourceAsStream("nifty.nxs"));
      loader.registerSchema("nifty-styles.nxs", ResourceLoader.getResourceAsStream("nifty-styles.nxs"));
      loader.registerSchema("nifty-controls.nxs", ResourceLoader.getResourceAsStream("nifty-controls.nxs"));
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }

  public void setAlternateKeyForNextLoadXml(final String alternateKeyForNextLoadXmlParam) {
    alternateKeyForNextLoadXml = alternateKeyForNextLoadXmlParam;
  }

  /**
   * Render all stuff in the current Screen.
   * @param clearScreen true if nifty should clean the screen and false when you've done that already.
   * @return true when nifty has finished processing the screen and false when rendering should continue.
   */
  public boolean render(final boolean clearScreen) {
    renderEngine.beginFrame();
    if (clearScreen) {
      renderEngine.clear();
    }

    if (!currentScreen.isNull()) {
        mouseInputEventQueue.process(inputSystem.getMouseEvents());

        MouseInputEvent inputEvent = mouseInputEventQueue.poll();
        while (inputEvent != null) {
          currentScreen.mouseEvent(inputEvent);
          handleDynamicElements();
          inputEvent = mouseInputEventQueue.poll();
        }

        for (KeyboardInputEvent keyEvent : inputSystem.getKeyboardEvents()) {
          if (!currentScreen.isNull()) {
            currentScreen.keyEvent(keyEvent);
          }
        }

        if (!currentScreen.isNull()) {
          currentScreen.renderLayers(renderEngine);
        }

        if (useDebugConsole) {
          console.render(this, currentScreen, renderEngine);
        }
    }

    if (exit) {
      renderEngine.clear();
    }

    handleDynamicElements();
    renderEngine.endFrame();

    long current = timeProvider.getMsTime();
    int delta = (int) (current - lastTime);
    soundSystem.update(delta);
    lastTime = current;

    return exit;
  }

  public void resetEvents() {
    mouseInputEventQueue.reset();
  }

  private void handleDynamicElements() {
    invokeMethods();
    removePopUps();
    removeLayerElements();
    addControls();
    removeElements();
  }

  private void removeLayerElements() {
    if (!currentScreen.isNull()) {
      currentScreen.processAddAndRemoveLayerElements();
    }
  }

  private void removePopUps() {
    if (!removePopupList.isEmpty()) {
      if (!currentScreen.isNull()) {
        for (RemovePopUp removePopup : removePopupList) {
          removePopup.close();
        }
      }
      removePopupList.clear();
    }
  }

  public void addControls() {
    if (!controlsToAdd.isEmpty()) {
      for (ControlToAdd controlToAdd : controlsToAdd) {
        try {
          controlToAdd.startControl(controlToAdd.createControl());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      controlsToAdd.clear();
    }
  }

  public void addControlsWithoutStartScreen() {
    if (!controlsToAdd.isEmpty()) {
      for (ControlToAdd controlToAdd : controlsToAdd) {
        try {
          controlToAdd.startControlWithCheck(controlToAdd.createControl());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      controlsToAdd.clear();
    }
  }

  /**
   * remove elements.
   */
  public void removeElements() {
    if (!elementsToRemove.isEmpty()) {
      for (ElementToRemove elementToRemove : elementsToRemove) {
        elementToRemove.remove();
      }
      elementsToRemove.clear();
    }
  }

  /**
   * Initialize this Nifty instance from the given xml file.
   * @param filename filename to nifty xml
   * @param startScreen screen to start exec
   */
  public void fromXml(final String filename, final String startScreen) {
    prepareScreens(filename);
    loadFromFile(filename);
    gotoScreen(startScreen);
  }

  /**
   * Initialize this Nifty instance from the given xml file.
   * @param filename filename to nifty xml
   */
  public void fromXmlWithoutStartScreen(final String filename) {
    prepareScreens(filename);
    loadFromFile(filename);
  }

  /**
   * Initialize this Nifty instance from the given xml file.
   * @param filename filename to nifty xml
   * @param startScreen screen to start exec
   * @param controllers controllers to use
   */
  public void fromXml(
      final String filename,
      final String startScreen,
      final ScreenController ... controllers) {
    registerScreenController(controllers);
    prepareScreens(filename);
    loadFromFile(filename);
    gotoScreen(startScreen);
  }

  /**
   * fromXml.
   * @param fileId fileId
   * @param input inputStream
   * @param startScreen screen to start
   */
  public void fromXml(final String fileId, final InputStream input, final String startScreen) {
    prepareScreens(fileId);
    loadFromStream(input);
    gotoScreen(startScreen);
  }

  /**
   * fromXmlWithoutStartScreen.
   * @param fileId fileId
   * @param input inputStream
   */
  public void fromXmlWithoutStartScreen(final String fileId, final InputStream input) {
    prepareScreens(fileId);
    loadFromStream(input);
  }

  /**
   * fromXml with ScreenControllers.
   * @param fileId fileId
   * @param input inputStream
   * @param startScreen screen to start
   * @param controllers controllers to use
   */
  public void fromXml(
      final String fileId,
      final InputStream input,
      final String startScreen,
      final ScreenController ... controllers) {
    registerScreenController(controllers);
    prepareScreens(fileId);
    loadFromStream(input);
    gotoScreen(startScreen);
  }

  /**
   * load from the given file.
   * @param filename filename to load
   */
  void loadFromFile(final String filename) {
    log.info("loadFromFile [" + filename + "]");

    try {
      long start = timeProvider.getMsTime();
      NiftyType niftyType = loader.loadNiftyXml(
          "nifty.nxs",
          ResourceLoader.getResourceAsStream(filename),
          this,
          timeProvider);
      niftyType.create(this, timeProvider);
//      log.info(niftyType.output());
      long end = timeProvider.getMsTime();
      log.info("loadFromFile took [" + (end - start) + "]");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * load from the given file.
   * @param stream stream to load
   */
  void loadFromStream(final InputStream stream) {
    log.info("loadFromStream []");

    try {
      long start = timeProvider.getMsTime();
      NiftyType niftyType = loader.loadNiftyXml(
          "nifty.nxs",
          stream,
          this,
          timeProvider);
      niftyType.create(this, timeProvider);
//      log.info(niftyType.output());
      long end = timeProvider.getMsTime();
      log.info("loadFromStream took [" + (end - start) + "]");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * prepare/reset screens.
   * @param xmlId xml id
   */
  void prepareScreens(final String xmlId) {
    screens.clear();

    // this.currentScreen = null;
    this.currentLoaded = xmlId;
    this.exit = false;
  }

  /**
   * goto screen command. this will send first an endScreen event to the current screen.
   * @param id the new screen id we should go to.
   */
  public void gotoScreen(final String id) {
    if (gotoScreenInProgess) {
      log.info("gotoScreen [" + id + "] aborted because still in gotoScreenInProgress phase");
      return;
    }

    log.info("gotoScreen [" + id + "]");
    gotoScreenInProgess = true;

    if (currentScreen.isNull()) {
      gotoScreenInternal(id);
    } else {
      // end current screen
      currentScreen.endScreen(
          new EndNotify() {
            public void perform() {
              gotoScreenInternal(id);
            }
          });
    }
  }

  /**
   * goto new screen.
   * @param id the new screen id we should go to.
   */
  private void gotoScreenInternal(final String id) {
    log.info("gotoScreenInternal [" + id + "]");

    currentScreen = screens.get(id);
    if (currentScreen == null) {
      currentScreen = new NullScreen();
      log.warning("screen [" + id + "] not found");
      gotoScreenInProgess = false;
      return;
    }

    // start the new screen
    if (alternateKeyForNextLoadXml != null) {
      currentScreen.setAlternateKey(alternateKeyForNextLoadXml);
      alternateKeyForNextLoadXml = null;
    }
    currentScreen.startScreen(new EndNotify() {
      public void perform() {
        gotoScreenInProgess = false;
      }
    });
  }

  /**
   * Set alternate key for all screen. This could be used to change behavior on all screens.
   * @param alternateKey the new alternate key to use
   */
  public void setAlternateKey(final String alternateKey) {
    this.alternateKey = alternateKey;
    for (Screen screen : screens.values()) {
      screen.setAlternateKey(alternateKey);
    }
  }

  /**
   * Returns a collection of the name of all screens
   * @return sn The collection containing the name of all screens
   */
  public Collection < String > getAllScreensName() {
    Collection < String > sn = new LinkedList < String >();
    for (Screen screen : screens.values()) {
      sn.add(screen.getScreenId());
    }
    return sn;
  }

  /**
   * exit.
   */
  public void exit() {
    currentScreen.endScreen(
        new EndNotify() {
          public final void perform() {
            exit = true;
            currentScreen = new NullScreen();
          }
        });
  }

  /**
   * get a specific screen.
   * @param id the id of the screen to retrieve.
   * @return the screen
   */
  public Screen getScreen(final String id) {
    Screen screen = screens.get(id);
    if (screen == null) {
      log.warning("screen [" + id + "] not found");
      return null;
    }

    return screen;
  }

  /**
   * Get the SoundSystem.
   * @return SoundSystem
   */
  public SoundSystem getSoundSystem() {
    return soundSystem;
  }

  /**
   * Return the RenderDevice.
   * @return RenderDevice
   */
  public NiftyRenderEngine getRenderEngine() {
    return renderEngine;
  }

  /**
   * Get current screen.
   * @return current screen
   */
  public Screen getCurrentScreen() {
    return currentScreen;
  }

  /**
   * Check if nifty displays the file with the given filename and is at a screen with the given screenId.
   * @param filename filename
   * @param screenId screenId
   * @return true if the given screen is active and false when not
   */
  public boolean isActive(final String filename, final String screenId) {
    if (currentLoaded != null && currentLoaded.equals(filename)) {
      if (!currentScreen.isNull() && currentScreen.getScreenId().equals(screenId)) {
        return true;
      }
    }
    return false;
  }

  /**
   * popup.
   * @param popup popup
   */
  public void registerPopup(final PopupType popup) {
    popups.put(popup.getAttributes().get("id"), popup);
  }

  /**
   * show popup in the given screen.
   * @param screen screen
   * @param id id
   */
  public void showPopup(final Screen screen, final String id, final Element defaultFocusElement) {
    Element popup = activePopups.get(id);
    if (popup == null) {
      log.warning("missing popup [" + id + "] o_O");
    } else {
      screen.addPopup(popup, defaultFocusElement);
    }
  }

  private Element createPopupFromType(final PopupType popupTypeParam) {
    Screen screen = getCurrentScreen();
    LayoutPart layerLayout = NiftyFactory.createRootLayerLayoutPart(this);
    PopupType popupType = new PopupType(popupTypeParam);
    popupType.prepare(this, screen.getRootElement().getElementType());
    return popupType.create(
        screen.getRootElement(),
        this,
        screen,
        layerLayout);
  }

  public Element createPopup(final String id) {
    return createAndAddPopup(id, popups.get(id));
  }

  public Element createPopupWithStyle(final String id, final String style) {
    PopupType popupType = popups.get(id);
    popupType.getAttributes().set("style", style);
    return createAndAddPopup(id, popupType);
  }

  private Element createAndAddPopup(final String id, PopupType popupType) {
    Element popupElement = createPopupFromType(popupType);
    activePopups.put(id, popupElement);
    return popupElement;
  }

  public Element findActivePopupByName(final String id) {
    return activePopups.get(id);
  }

  /**
   * Close the Popup with the given id.
   * @param id id of popup to close
   */
  public void closePopup(final String id) {
    closePopupInternal(id, null);
  }

  /**
   * Close the Popup with the given id. This calls the given EndNotify when the onEndScreen of the popup ends.
   * @param id id of popup to close
   * @param closeNotify EndNotify callback
   */
  public void closePopup(final String id, final EndNotify closeNotify) {
    closePopupInternal(id, closeNotify);
  }

  private void closePopupInternal(final String id, final EndNotify closeNotify) {
    Element popup = activePopups.get(id);
    if (popup == null) {
      log.warning("missing popup [" + id + "] o_O");
      return;
    }
    popup.resetAllEffects();
    popup.startEffect(EffectEventId.onEndScreen, new EndNotify() {
      public void perform() {
        removePopupList.add(new RemovePopUp(id, closeNotify));
      }
    });
  }

  /**
   * Add a control to this screen and the given parent element.
   * @param screen screen
   * @param parent parent element
   * @param controlName control name to add
   * @param id id of control
   * @param style style
   * @param focusable focusable
  public void addControl(
      final Screen screen,
      final Element parent,
      final String controlName,
      final String id,
      final String style,
      final Boolean focusable,
      final Attributes attributes) {
    controlsToAdd.add(
        new ControlToAdd(
            screen, parent, controlName, id, style, focusable, attributes));
  }
   */

  public void addControl(
      final Screen screen,
      final Element element,
      final StandardControl standardControl) {
    controlsToAdd.add(new ControlToAdd(screen, element, standardControl));
  }

  /**
   * ControlToAdd helper class.
   * @author void
   */
  private class ControlToAdd {
    private Screen screen;
    private Element parent;
    private StandardControl control;

    public ControlToAdd(
        final Screen screenParam,
        final Element parentParam,
        final StandardControl standardControl) {
      screen = screenParam;
      parent = parentParam;
      control = standardControl;
    }

    public void startControlWithCheck(final Element element) {
      if (screen.isRunning()) {
//        element.startEffect(EffectEventId.onStartScreen);
//        element.startEffect(EffectEventId.onActive);
      }
    }

    public Element createControl() throws Exception {
      return control.createControl(Nifty.this, screen, parent);
    }

    public void startControl(final Element newControl) {
      newControl.startEffect(EffectEventId.onStartScreen);
      newControl.startEffect(EffectEventId.onActive);
      newControl.onStartScreen(screen);
    }
  }

  /**
   * ElementToRemove helper.
   * @author void
   */
  private class ElementToRemove {
    private Screen screen;
    private Element element;
    private EndNotify endNotify;

    /**
     * create it.
     * @param newScreen screen
     * @param newElement element
     * @param endNotify 
     */
    public ElementToRemove(final Screen newScreen, final Element newElement, final EndNotify endNotify) {
      this.screen = newScreen;
      this.element = newElement;
      this.endNotify = endNotify;
    }

    /**
     * do the actual remove.
     */
    public void remove() {
      removeSingleElement(element);
      Element parent = element.getParent();
      if (parent != null) {
        parent.getElements().remove(element);
      }
      screen.layoutLayers();
      if (endNotify != null) {
        endNotify.perform();
      }
    }

    private void removeSingleElement(final Element element) {
      Iterator < Element > elementIt = element.getElements().iterator();
      while (elementIt.hasNext()) {
        Element el = elementIt.next();
        removeSingleElement(el);

        elementIt.remove();
      }
    }
  }

  /**
   * Remove the given element from the given screen.
   * @param screen screen
   * @param element element to remove
   */
  public void removeElement(final Screen screen, final Element element) {
    element.removeFromFocusHandler();
    element.startEffect(EffectEventId.onEndScreen, new EndNotify() {
      public void perform() {
        elementsToRemove.add(new ElementToRemove(screen, element, null));
      }
    });
  }

  public void removeElement(final Screen screen, final Element element, final EndNotify endNotify) {
    element.removeFromFocusHandler();
    element.startEffect(EffectEventId.onEndScreen, new EndNotify() {
      public void perform() {
        elementsToRemove.add(new ElementToRemove(screen, element, endNotify));
      }
    });
  }

  public void toggleElementsDebugConsole() {
    useDebugConsole = !useDebugConsole;
    console.setOutputElements(true);
  }

  public void toggleEffectsDebugConsole() {
    useDebugConsole = !useDebugConsole;
    console.setOutputElements(false);
  }

  /**
   * @return the mouseInputEventQueue
   */
  public MouseInputEventQueue getMouseInputEventQueue() {
    return mouseInputEventQueue;
  }

  /**
   * Register a ScreenController instance.
   * @param controllers ScreenController
   */
  public void registerScreenController(final ScreenController ... controllers) {
    for (ScreenController c : controllers) {
      registeredScreenControllers.add(c);
    }
  }

  /**
   * find a ScreenController instance that matches the given controllerClass name.
   * @param controllerClass controller class name
   * @return ScreenController instance
   */
  public ScreenController findScreenController(final String controllerClass) {
    for (ScreenController controller : registeredScreenControllers) {
      if (controller.getClass().getName().equals(controllerClass)) {
        return controller;
      }
    }
    return null;
  }

  public NiftyLoader getLoader() {
    return loader;
  }

  public TimeProvider getTimeProvider() {
    return timeProvider;
  }

  public class RemovePopUp {
    private String removePopupId;
    private EndNotify closeNotify;

    public RemovePopUp(final String popupId, final EndNotify closeNotifyParam) {
      removePopupId = popupId;
      closeNotify = closeNotifyParam;
    }

    public void close() {
      currentScreen.closePopup(activePopups.get(removePopupId));
      if (closeNotify != null) {
        closeNotify.perform();
      }
    }
  }

  public void addScreen(final String id, final Screen screen) {
    screens.put(id, screen);
  }

  public void registerStyle(final StyleType style) {
    log.fine("registerStyle " + style.getStyleId());
    styles.put(style.getStyleId(), style);
  }

  public void registerControlDefintion(final ControlDefinitionType controlDefintion) {
    controlDefintions.put(controlDefintion.getName(), controlDefintion);
  }

  public void registerEffect(final RegisterEffectType registerEffectType) {
    registeredEffects.put(registerEffectType.getName(), registerEffectType);
  }

  public ControlDefinitionType resolveControlDefinition(final String name) {
    if (name == null) {
      return null;
    }
    return controlDefintions.get(name);
  }

  public RegisterEffectType resolveRegisteredEffect(final String name) {
    if (name == null) {
      return null;
    }
    return registeredEffects.get(name);
  }

  public StyleResolver getDefaultStyleResolver() {
    return new StyleResolverDefault(styles);
  }

  public String getAlternateKey() {
    return alternateKey;
  }

  public void delayedMethodInvoke(final NiftyDelayedMethodInvoke method, final Object[] params) {
    delayedMethodInvokes.add(new DelayedMethodInvoke(method, params));
  }

  public void invokeMethods() {
    // make working copy in case a method invoke will create addtional method calls
    Collection < DelayedMethodInvoke > workingCopy = new ArrayList < DelayedMethodInvoke > (delayedMethodInvokes);

    // clean current List
    delayedMethodInvokes.clear();

    // process the working copy
    for (DelayedMethodInvoke method : workingCopy) {
      method.perform();
    }

    // the delayedMethodInvokes list is empty now or it has new entries that resulted from method.perform calls
    // in that case these methods will be processed next frame
  }

  private class DelayedMethodInvoke {
    private NiftyDelayedMethodInvoke method;
    private Object[] params;

    public DelayedMethodInvoke(final NiftyDelayedMethodInvoke method, final Object[] params) {
      this.method = method;
      this.params = params;
    }

    public void perform() {
      method.performInvoke(params);
    }
  }
}
