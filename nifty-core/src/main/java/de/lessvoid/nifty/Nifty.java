package de.lessvoid.nifty;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.EventTopicSubscriber;
import org.bushe.swing.event.ProxySubscriber;
import org.bushe.swing.event.ThreadSafeEventService;
import org.bushe.swing.event.annotation.ReferenceStrength;

import de.lessvoid.nifty.controls.StandardControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEventProcessor;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.layout.BoxConstraints;
import de.lessvoid.nifty.layout.LayoutPart;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.RootLayerFactory;
import de.lessvoid.nifty.loaderv2.types.ControlDefinitionType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.LayerType;
import de.lessvoid.nifty.loaderv2.types.NiftyType;
import de.lessvoid.nifty.loaderv2.types.PopupType;
import de.lessvoid.nifty.loaderv2.types.RegisterEffectType;
import de.lessvoid.nifty.loaderv2.types.RegisterMusicType;
import de.lessvoid.nifty.loaderv2.types.RegisterSoundType;
import de.lessvoid.nifty.loaderv2.types.ResourceBundleType;
import de.lessvoid.nifty.loaderv2.types.StyleType;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolver;
import de.lessvoid.nifty.loaderv2.types.resolver.style.StyleResolverDefault;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyMouseImpl;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.NiftyRenderEngineImpl;
import de.lessvoid.nifty.screen.NullScreen;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.tools.ObjectPool;
import de.lessvoid.nifty.tools.ObjectPool.Factory;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import de.lessvoid.xml.tools.SpecialValuesReplace;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The main Nifty class.
 * @author void
 */
public class Nifty {
  private Logger log = Logger.getLogger(Nifty.class.getName());
  private Logger inputEventLog = Logger.getLogger("NiftyInputEventHandlingLog");
  private NiftyRenderEngine renderEngine;
  private SoundSystem soundSystem;
  private Map < String, Screen > screens = new Hashtable < String, Screen >();
  private Map < String, PopupType > popupTypes = new Hashtable < String, PopupType >();
  private Map < String, Element > popups = new Hashtable < String, Element >();
  private Map < String, StyleType > styles = new Hashtable < String, StyleType >();
  private Map < String, ControlDefinitionType > controlDefintions = new Hashtable < String, ControlDefinitionType >();
  private Map < String, RegisterEffectType > registeredEffects = new Hashtable < String, RegisterEffectType >();
  private Screen currentScreen = new NullScreen();
  private String currentLoaded;
  private boolean exit;
  private boolean resolutionChanged;
  private TimeProvider timeProvider;
  private List < ClosePopUp > closePopupList = new ArrayList < ClosePopUp >();
  private NiftyLoader loader;
  private List < ControlToAdd > controlsToAdd = new ArrayList < ControlToAdd >();
  private List < EndOfFrameElementAction > endOfFrameElementActions = new ArrayList < EndOfFrameElementAction >();
  private MouseInputEventProcessor mouseInputEventProcessor;
  private Collection < ScreenController > registeredScreenControllers = new ArrayList < ScreenController >();
  private String alternateKeyForNextLoadXml;
  private long lastTime;
  private InputSystem inputSystem;
  private boolean gotoScreenInProgess;
  private String alternateKey;
  private Collection < DelayedMethodInvoke > delayedMethodInvokes = new ArrayList < DelayedMethodInvoke > ();
  private Map<String, String> resourceBundleSource = new Hashtable<String, String>();
  private Map<String, ResourceBundle> resourceBundles = new Hashtable<String, ResourceBundle>();
  private Locale locale = Locale.getDefault();
  private Properties globalProperties;
  private RootLayerFactory rootLayerFactory = new RootLayerFactory();
  private NiftyMouseImpl niftyMouse;
  private NiftyInputConsumerImpl niftyInputConsumer = new NiftyInputConsumerImpl();
  private SubscriberRegistry subscriberRegister = new SubscriberRegistry();
  private boolean debugOptionPanelColors;
  private Clipboard clipboard = null;
  private NiftyResourceLoader resourceLoader = new NiftyResourceLoader();

  /**
   * Create nifty with optional console parameter.
   * @param newRenderDevice the RenderDevice
   * @param newSoundSystem SoundSystem
   * @param newInputSystem InputSystem
   * @param newTimeProvider the TimeProvider
   */
  public Nifty(
      final RenderDevice newRenderDevice,
      final SoundDevice newSoundDevice,
      final InputSystem newInputSystem,
      final TimeProvider newTimeProvider) {
    newRenderDevice.setResourceLoader(resourceLoader);
    newSoundDevice.setResourceLoader(resourceLoader);
    newInputSystem.setResourceLoader(resourceLoader);
    initialize(new NiftyRenderEngineImpl(newRenderDevice), new SoundSystem(newSoundDevice), newInputSystem, newTimeProvider);
    initializeClipboard();
  }

  /**
   * Initialize this instance.
   * @param newRenderDevice RenderDevice
   * @param newSoundSystem SoundSystem
   * @param newInputSystem InputSystem
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
    this.resolutionChanged = false;
    this.currentLoaded = null;
    this.mouseInputEventProcessor = new MouseInputEventProcessor();
    this.lastTime = timeProvider.getMsTime();
    this.niftyMouse = new NiftyMouseImpl(newRenderDevice.getRenderDevice(), inputSystem, timeProvider);

    try {
      loader = new NiftyLoader(this, timeProvider);
      loader.registerSchema("nifty.nxs", getResourceAsStream("nifty.nxs"));
      loader.registerSchema("nifty-styles.nxs", getResourceAsStream("nifty-styles.nxs"));
      loader.registerSchema("nifty-controls.nxs", getResourceAsStream("nifty-controls.nxs"));
      NiftyDefaults.initDefaultEffects(this);

      initalizeEventBus();
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }

  private void initializeClipboard() {
    try {
      Class.forName("java.awt.datatransfer.Clipboard");
      clipboard = new ClipboardAWT();
    } catch (ClassNotFoundException e) {
      log.info("unable to access class 'java.awt.datatransfer.Clipboard'. clipboard will be disabled.");
      clipboard = new ClipboardNull();
    } catch (Throwable e) {
      log.info("unable to access class 'java.awt.datatransfer.Clipboard'. clipboard will be disabled.");
      clipboard = new ClipboardNull();
    }
  }

  private void initalizeEventBus() throws EventServiceExistsException {
    EventServiceLocator.setEventService("NiftyEventBus", new ThreadSafeEventService());
  }

  public EventService getEventService() {
    return EventServiceLocator.getEventService("NiftyEventBus");
  }

  @SuppressWarnings("rawtypes")
  public void publishEvent(final String id, final NiftyEvent event) {
    // we can't publish events for elements without an id
    if (id != null) {
      getEventService().publish(id, event);
    }
  }

  public void subscribeAnnotations(final Object object) {
    NiftyEventAnnotationProcessor.process(object);
  }

  public void unsubscribeAnnotations(final Object object) {
    NiftyEventAnnotationProcessor.unprocess(object);
  }

  public <T, S extends EventTopicSubscriber<? extends T>> void subscribe(final Screen screen, final String elementId, final Class<T> eventClass, final S subscriber) {
    if (elementId == null) {
      log.warning("trying to subscribe events for an element with elementId = null. this won't work. offending class \"" + eventClass + "\" and offending subscriber \"" + subscriber + "\". try to find the offending element/control and give it an id!");
      return;
    }
    ClassSaveEventTopicSubscriber theSubscriber = new ClassSaveEventTopicSubscriber(elementId, subscriber, eventClass);
    getEventService().subscribeStrongly(elementId, theSubscriber);
    NiftyDefaults.eventBusLog.info("-> subscribe [" + elementId + "] screen [" + screen + "] -> [" + theSubscriber + "(" + subscriber + "),(" + eventClass + ")]");

    subscriberRegister.register(screen, elementId, theSubscriber);
  }

  public void unsubscribe(final String elementId, final Object object) {
    // This handles direct subscription
    if (object instanceof EventTopicSubscriber<?>) {
      if (elementId == null) {
        log.warning("trying to unsubscribe events for an element with elementId = null. this won't work. offending object \"" + object + "\". try to find the offending element and give it an id!");
        return;
      }
      getEventService().unsubscribe(elementId, (EventTopicSubscriber<?>) object);
      NiftyDefaults.eventBusLog.info("<- unsubscribe [" + elementId + "] -> [" + object + "]");
    }
  }

  public void unsubscribeScreen(final Screen screen) {
    subscriberRegister.unsubscribeScreen(screen);
  }

  public void unsubscribeElement(final Screen screen, final String elementId) {
    subscriberRegister.unsubscribeElement(screen, elementId);
  }

  public void setAlternateKeyForNextLoadXml(final String alternateKeyForNextLoadXmlParam) {
    alternateKeyForNextLoadXml = alternateKeyForNextLoadXmlParam;
  }

  /**
   * Update Nifty.
   * @return true when nifty has finished processing the screen and false when rendering should continue.
   */
  public boolean update() {
    if (!currentScreen.isNull()) {
      mouseInputEventProcessor.begin();
      inputSystem.forwardEvents(niftyInputConsumer);
      if (mouseInputEventProcessor.hasLastMouseDownEvent()) {
        forwardMouseEventToScreen(mouseInputEventProcessor.getLastMouseDownEvent());
      }
    }
    handleDynamicElements();
    updateSoundSystem();
    if (log.isLoggable(Level.FINER)) {
      log.fine(currentScreen.debugOutput());
    }
    return exit;
  }

  private boolean forwardMouseEventToScreen(final NiftyMouseInputEvent mouseEvent) {
    // update the nifty mouse that keeps track of the current mouse position too 
    niftyMouse.updateMousePosition(mouseEvent.getMouseX(), mouseEvent.getMouseY());

    // and forward the event to the current screen
    return currentScreen.mouseEvent(mouseEvent);
  }

  /**
   * Render Nifty.
   * @param clearScreen true if nifty should clean the screen and false when you've done that already.
   */
  public void render(final boolean clearScreen) {
    renderEngine.beginFrame();
    if (clearScreen) {
      renderEngine.clear();
    }

    if (!currentScreen.isNull()) {
      currentScreen.renderLayers(renderEngine);
    }

    if (exit) {
      renderEngine.clear();
    }
    renderEngine.endFrame();

    // now that the frame is complete we can reset the renderdevice in case of the resolution change
    if (resolutionChanged) {
      resolutionChanged = false;
      displayResolutionChanged();
    }
  }

  private void updateSoundSystem() {
    long current = timeProvider.getMsTime();
    int delta = (int) (current - lastTime);
    soundSystem.update(delta);
    lastTime = current;
  }

  public void resetMouseInputEvents() {
    niftyInputConsumer.resetMouseDown();
    mouseInputEventProcessor.reset();
    if (!currentScreen.isNull()) {
      currentScreen.resetMouseDown();
    }
  }

  private void handleDynamicElements() {
    while (hasDynamics()) {
      invokeMethods();
      closePopUps();
      removeLayerElements();
      addControls();
      executeEndOfFrameElementActions();
    }
  }

  private boolean hasDynamics() {
    return hasInvokeMethods() || hasClosePopups() || hasRemoveLayerElements() || hasControlsToAdd() || hasEndOfFrameElementActions();
  }

  private boolean hasRemoveLayerElements() {
    if (currentScreen.isNull()) {
      return false;
    }
    return currentScreen.hasDynamicElements();
  }

  private void removeLayerElements() {
    if (!currentScreen.isNull()) {
      currentScreen.processAddAndRemoveLayerElements();
    }
  }

  private boolean hasClosePopups() {
    return !closePopupList.isEmpty();
  }

  private void closePopUps() {
    if (hasClosePopups()) {
      if (currentScreen.isNull()) {
        closePopupList.clear();
        return;
      }
      ArrayList <ClosePopUp> copy = new ArrayList <ClosePopUp>(closePopupList);
      closePopupList.clear();

      for (int i=0; i<copy.size(); i++) {
        ClosePopUp closePopup = copy.get(i);
        closePopup.close();
      }
    }
  }

  public void addControls() {
    if (hasControlsToAdd()) {
      List < ControlToAdd > copy = new ArrayList < ControlToAdd >(controlsToAdd);
      controlsToAdd.clear();

      for (int i=0; i<copy.size(); i++) {
        ControlToAdd controlToAdd = copy.get(i);
        try {
          controlToAdd.startControl(controlToAdd.createControl());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private boolean hasControlsToAdd() {
    return !controlsToAdd.isEmpty();
  }

  public void addControlsWithoutStartScreen() {
    if (hasControlsToAdd()) {
      for (int i=0; i<controlsToAdd.size(); i++) {
        ControlToAdd controlToAdd = controlsToAdd.get(i);
        try {
          controlToAdd.startControl(controlToAdd.createControl());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      controlsToAdd.clear();
    }
  }

  public void executeEndOfFrameElementActions() {
    if (hasEndOfFrameElementActions()) {
      ArrayList<EndOfFrameElementAction> listCopy = new ArrayList<EndOfFrameElementAction>(endOfFrameElementActions);
      endOfFrameElementActions.clear();
      for (int i=0; i<listCopy.size(); i++) {
        EndOfFrameElementAction elementAction = listCopy.get(i);
        elementAction.perform();
      }
    }
  }

  private boolean hasEndOfFrameElementActions() {
    return !endOfFrameElementActions.isEmpty();
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
   * Load an additional xml file without removing any of the data that might already been loaded.
   * @param filename the file to load
   */
  public void addXml(final String filename) {
    loadFromFile(filename);
  }

  /**
   * Load an additional xml from a stream without removing any of the data that might already been loaded.
   * @param stream the stream to load
   */
  public void addXml(final InputStream stream) {
    loadFromStream(stream);
  }

  /**
   * Load and validate the given filename. If the file is valid, nothing happens. If it
   * is invalid you'll get an exception explaining the error.
   * @param filename filename to check
   * @throws Exception exception describing the error
   */
  public void validateXml(final String filename) throws Exception {
    loader.validateNiftyXml(getResourceAsStream(filename));
  }

  /**
   * Load and validate the given stream. If the stream is valid, nothing happens. If it
   * is invalid you'll get an exception explaining the error.
   * @param filename filename to check
   * @throws Exception exception describing the error
   */
  public void validateXml(final InputStream stream) throws Exception {
    loader.validateNiftyXml(stream);
  }

  /**
   * load from the given file.
   * @param filename filename to load
   */
  void loadFromFile(final String filename) {
    log.info("loadFromFile [" + filename + "]");

    try {
      long start = timeProvider.getMsTime();
      NiftyType niftyType = loader.loadNiftyXml("nifty.nxs", getResourceAsStream(filename), this);
      niftyType.create(this, timeProvider);
      if (log.isLoggable(Level.INFO)) {
        log.info(niftyType.output());
      }
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
      NiftyType niftyType = loader.loadNiftyXml("nifty.nxs", stream, this);
      niftyType.create(this, timeProvider);
      if (log.isLoggable(Level.INFO)) {
        log.info(niftyType.output());
      }
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

  public void removeScreen(final String id) {
    if (!currentScreen.isNull()) {
      if (currentScreen.getScreenId().equals(id)) {
        currentScreen.endScreen(
          new EndNotify() {
            public void perform() {
              currentScreen = new NullScreen();
              removeScreenInternal(id);
            }
          });
        return;
      }
      removeScreenInternal(id);
    }
  }

  private void removeScreenInternal(final String id) {
    Screen screen = screens.remove(id);
    if (screen == null ||
        screen.getLayerElements() == null ||
        screen.getLayerElements().size() == 0) {
      return;
    }
    for (int i=0; i<screen.getLayerElements().size(); i++) {
      removeElement(screen, screen.getLayerElements().get(i));
    }
  }

  /**
   * This returns all the style names currently registed with nifty.
   * @return Collection of all style names
   */
  public Collection < String > getAllStylesName() {
    return styles.keySet();
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

  public void resolutionChanged() {
    resolutionChanged = true;
  }

  private void displayResolutionChanged() {
    getRenderEngine().displayResolutionChanged();

    int newWidth = getRenderEngine().getWidth();
    int newHeight = getRenderEngine().getHeight();
    updateLayoutPart(currentScreen.getRootElement().getLayoutPart(), newWidth, newHeight);

    for (Element e : currentScreen.getLayerElements()) {
      updateLayoutPart(e.getLayoutPart(), newWidth, newHeight);
    }

    for (Element e : popups.values()) {
      updateLayoutPart(e.getLayoutPart(), newWidth, newHeight);
    }

    resetMouseInputEvents();

    currentScreen.resetLayout();
    currentScreen.layoutLayers();
  }

  private void updateLayoutPart(final LayoutPart layoutPart, final int width, final int height) {
    Box box = layoutPart.getBox();
    box.setWidth(width);
    box.setHeight(height);
    BoxConstraints boxConstraints = layoutPart.getBoxConstraints();
    boxConstraints.setWidth(new SizeValue(width + "px"));
    boxConstraints.setHeight(new SizeValue(height + "px"));
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
    popupTypes.put(popup.getAttributes().get("id"), popup);
  }

  /**
   * show popup in the given screen.
   * @param screen screen
   * @param id id
   */
  public void showPopup(final Screen screen, final String id, final Element defaultFocusElement) {
    Element popup = popups.get(id);
    if (popup == null) {
      log.warning("missing popup [" + id + "] o_O");
    } else {
      screen.addPopup(popup, defaultFocusElement);
    }
  }

  private Element createPopupFromType(final PopupType popupTypeParam, final String id) {
    Screen screen = getCurrentScreen();
    LayoutPart layerLayout = rootLayerFactory.createRootLayerLayoutPart(this);
    PopupType popupType = new PopupType(popupTypeParam);
    popupType.prepare(this, screen, screen.getRootElement().getElementType());
    Element element = popupType.create(screen.getRootElement(), this, screen, layerLayout);
    element.setId(id);
    fixupSubIds(element, id);
    if (screen.isBound()) {
      element.layoutElements();
      element.bindControls(screen);
    }
    return element;
  }

  private void fixupSubIds(final Element element, final String parentId) {
    String currentId = element.getId();
    if (currentId != null && currentId.startsWith("#")) {
      currentId = parentId + element.getId();
      element.setId(currentId);
    }
    if (currentId == null) {
      currentId = parentId;
    }
    for (int i=0; i<element.getElements().size(); i++) {
      Element e = element.getElements().get(i);
      fixupSubIds(e, currentId);
    }
  }

  public Element createPopup(final String popupId) {
    return createAndAddPopup(NiftyIdCreator.generate(), popupTypes.get(popupId));
  }

  public Element createPopupWithId(final String popupId, final String id) {
    return createAndAddPopup(id, popupTypes.get(popupId));
  }

  public Element createPopupWithStyle(final String popupId, final String id, final String style) {
    PopupType popupType = popupTypes.get(popupId);
    popupType.getAttributes().set("style", style);
    return createAndAddPopup(id, popupType);
  }

  public Element createPopupWithStyle(final String popupId, final String style) {
    return createPopupWithStyle(popupId, style, new Attributes());
  }

  public Element createPopupWithStyle(final String popupId, final String style, final Attributes parameters) {
    PopupType popupType = new PopupType(popupTypes.get(popupId));
    popupType.getAttributes().set("style", style);
    popupType.getAttributes().merge(parameters);
    return createAndAddPopup(NiftyIdCreator.generate(), popupType);
  }

  private Element createAndAddPopup(final String id, PopupType popupType) {
    Element popupElement = createPopupFromType(popupType, id);
    popups.put(id, popupElement);
    return popupElement;
  }

  public Element findPopupByName(final String id) {
    return popups.get(id);
  }

  public Element getTopMostPopup() {
    if (currentScreen != null) {
      return currentScreen.getTopMostPopup();
    }
    return null;
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
    Element popup = popups.get(id);
    if (popup == null) {
      log.warning("missing popup [" + id + "] o_O");
      return;
    }
    popup.resetAllEffects();
    popup.startEffect(EffectEventId.onEndScreen, new EndNotify() {
      public void perform() {
        closePopupList.add(new ClosePopUp(id, closeNotify));
      }
    });
  }

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

    public Element createControl() throws Exception {
      return control.createControl(Nifty.this, screen, parent);
    }

    public void startControl(final Element newControl) {
      // if this startControl is called with a screen that is already running (which means
      // that the onStartScreen Event has already been called) we have to call onStartScreen
      // on the newControl here manually. It won't be called by the screen anymore.
      if (screen.isBound()) {
        newControl.bindControls(screen);
        newControl.initControls();
      }
      if (screen.isRunning()) {
        newControl.startEffect(EffectEventId.onStartScreen);
        newControl.startEffect(EffectEventId.onActive);
        newControl.onStartScreen();
      }
    }
  }

  private interface Action {
    void perform(Screen screen, Element element);
  }

  public class ElementRemoveAction implements Action {
    public void perform(final Screen screen, final Element element) {
      // we'll now resetAllEffects here when an element is removed. this was especially
      // introduced to reset all onHover effects that were used for changing the mouse cursor image.
      // without this reset the mouse cursor image might hang when elements are being removed
      // that changed the image.
      element.resetAllEffects();
      element.onEndScreen(screen);

      removeSingleElement(screen, element);
      Element parent = element.getParent();
      if (parent != null) {
        parent.getElements().remove(element);

        // when the parent is the root element then the element we're removing is a layer element
        if (parent == screen.getRootElement()) {
          screen.removeLayerElement(element);
        }
      }
      screen.layoutLayers();
    }

    private void removeSingleElement(final Screen screen, final Element element) {
      Iterator < Element > elementIt = element.getElements().iterator();
      while (elementIt.hasNext()) {
        Element el = elementIt.next();
        removeSingleElement(screen, el);
        elementIt.remove();
      }
    }
  }

  public class ElementMoveAction implements Action {
    private Element destinationElement;

    public ElementMoveAction(final Element destinationElement) {
      this.destinationElement = destinationElement;
    }

    public void perform(final Screen screen, final Element element) {
      Element parent = element.getParent();
      if (parent != null) {
        parent.getElements().remove(element);
      }
      element.setParent(destinationElement);
      destinationElement.add(element);
      screen.layoutLayers();
    }
  }

  private class EndOfFrameElementAction {
    private Screen screen;
    private Element element;
    private Action action;
    private EndNotify endNotify;

    public EndOfFrameElementAction(final Screen newScreen, final Element newElement, final Action action, final EndNotify endNotify) {
      this.screen = newScreen;
      this.element = newElement;
      this.action = action;
      this.endNotify = endNotify;
    }

    public void perform() {
      action.perform(screen, element);
      if (endNotify != null) {
        endNotify.perform();
      }
    }
  }

  public void removeElement(final Screen screen, final Element element) {
    removeElement(screen, element, null);
  }

  public void removeElement(final Screen screen, final Element element, final EndNotify endNotify) {
    element.removeFromFocusHandler();
    element.startEffect(EffectEventId.onEndScreen, new EndNotify() {
      public void perform() {
        endOfFrameElementActions.add(new EndOfFrameElementAction(screen, element, new ElementRemoveAction(), endNotify));
      }
    });
  }

  public void moveElement(final Screen screen, final Element elementToMove, final Element destination, final EndNotify endNotify) {
    elementToMove.removeFromFocusHandler();
    endOfFrameElementActions.add(new EndOfFrameElementAction(screen, elementToMove, new ElementMoveAction(destination), endNotify));
  }

  /**
   * @return the mouseInputEventQueue
   */
  public MouseInputEventProcessor getMouseInputEventQueue() {
    return mouseInputEventProcessor;
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

  public class ClosePopUp {
    private String removePopupId;
    private EndNotify closeNotify;

    public ClosePopUp(final String popupId, final EndNotify closeNotifyParam) {
      removePopupId = popupId;
      closeNotify = closeNotifyParam;
    }

    public void close() {
      currentScreen.closePopup(popups.get(removePopupId), closeNotify);
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
    if (hasInvokeMethods()) {
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
  }

  private boolean hasInvokeMethods() {
    return !delayedMethodInvokes.isEmpty();
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

  public void setLocale(final Locale locale) {
    this.locale = locale;
  }

  public Map<String, ResourceBundle> getResourceBundles() {
    return resourceBundles;
  }

  public void addResourceBundle(final String id, final String filename) {
    resourceBundleSource.put(id, filename);
    resourceBundles.put(id, ResourceBundle.getBundle(filename, locale));
  }

  public Properties getGlobalProperties() {
    return globalProperties;
  }

  public void setGlobalProperties(Properties globalProperties) {
    this.globalProperties = globalProperties;
  }

  public RootLayerFactory getRootLayerFactory() {
    return rootLayerFactory;
  }

  public void loadStyleFile(final String styleFile) {
    try {
      NiftyType niftyType = new NiftyType();
      loader.loadStyleFile("nifty-styles.nxs", styleFile, niftyType, this);
      niftyType.create(this, getTimeProvider());
      if (log.isLoggable(Level.INFO)) {
        log.info("loadStyleFile");
        log.info(niftyType.output());
      }
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }

  public void loadControlFile(final String controlFile) {
    try {
      NiftyType niftyType = new NiftyType();
      loader.loadControlFile("nifty-controls.nxs", controlFile, niftyType);
      niftyType.create(this, getTimeProvider());
      if (log.isLoggable(Level.INFO)) {
        log.info("loadControlFile");
        log.info(niftyType.output());
      }
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }

  public void registerResourceBundle(final String id, final String filename) {
    try {
      NiftyType niftyType = new NiftyType();
      ResourceBundleType resourceBundle = new ResourceBundleType();
      resourceBundle.getAttributes().set("id", id);
      resourceBundle.getAttributes().set("filename", filename);
      niftyType.addResourceBundle(resourceBundle);
      niftyType.create(this, getTimeProvider());
      if (log.isLoggable(Level.INFO)) {
        log.info("registerResourceBundle");
        log.info(niftyType.output());
      }
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }

  public void registerEffect(final String name, final String classParam) {
    try {
      NiftyType niftyType = new NiftyType();
      RegisterEffectType registerEffect = new RegisterEffectType(name, classParam);
      niftyType.addRegisterEffect(registerEffect);
      niftyType.create(this, getTimeProvider());
      if (log.isLoggable(Level.INFO)) {
        log.info("registerEffect");
        log.info(niftyType.output());
      }
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }

  public void registerSound(final String id, final String filename) {
    try {
      NiftyType niftyType = new NiftyType();
      RegisterSoundType registerSound = new RegisterSoundType();
      registerSound.getAttributes().set("id", id);
      registerSound.getAttributes().set("filename", filename);
      niftyType.addRegisterSound(registerSound);
      niftyType.create(this, getTimeProvider());
      if (log.isLoggable(Level.INFO)) {
        log.info("registerSound");
        log.info(niftyType.output());
      }
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }

  public void registerMusic(final String id, final String filename) {
    try {
      NiftyType niftyType = new NiftyType();
      RegisterMusicType registerMusic = new RegisterMusicType();
      registerMusic.getAttributes().set("id", id);
      registerMusic.getAttributes().set("filename", filename);
      niftyType.addRegisterMusic(registerMusic);
      niftyType.create(this, getTimeProvider());
      if (log.isLoggable(Level.INFO)) {
        log.info("registerMusic");
        log.info(niftyType.output());
      }
    } catch (Exception e) {
      log.warning(e.getMessage());
    }
  }

  public void registerMouseCursor(final String id, final String filename, final int hotspotX, final int hotspotY) {
    try {
      getNiftyMouse().registerMouseCursor(id, filename, hotspotX, hotspotY);
    } catch (IOException e) {
      log.warning(e.getMessage());
    }
  }

  public NiftyMouse getNiftyMouse() {
    return niftyMouse;
  }

  /**
   * This is now an inner class to make sure no one calls it from the outside directly.
   * All InputSystem processing should go through the InputSystem.
   * @author void
   */
  private class NiftyInputConsumerImpl implements NiftyInputConsumer {
    private ObjectPool<NiftyMouseInputEvent> pool = new ObjectPool<NiftyMouseInputEvent>(32, new Factory<NiftyMouseInputEvent>() {
      @Override
      public NiftyMouseInputEvent createNew() {
        return new NiftyMouseInputEvent();
      }
    });
    private boolean button0Down = false;
    private boolean button1Down = false;
    private boolean button2Down = false;

    @Override
    public boolean processMouseEvent(final int mouseX, final int mouseY, final int mouseWheel, final int button, final boolean buttonDown) {
      boolean processed = processEvent(createEvent(mouseX, mouseY, mouseWheel, button, buttonDown));
      if (inputEventLog.isLoggable(Level.INFO)) {
        inputEventLog.info("[processMouseEvent] [" +  mouseX + ", " + mouseY + ", " + mouseWheel + ", " + button + ", " + buttonDown + "] processed [" + processed + "]");
      }
      return processed;
    }

    @Override
    public boolean processKeyboardEvent(final KeyboardInputEvent keyEvent) {
      if (currentScreen.isNull()) {
        return false;
      }
      boolean result = currentScreen.keyEvent(keyEvent);
      if (inputEventLog.isLoggable(Level.INFO)) {
        inputEventLog.info("[processKeyboardEvent] " + keyEvent + " processed [" + result + "]");
      }
      return result;
    }

    void resetMouseDown() {
      button0Down = false;
      button1Down = false;
      button2Down = false;
    }

    private NiftyMouseInputEvent createEvent(final int mouseX, final int mouseY, final int mouseWheel, final int button, final boolean buttonDown) {
      switch (button) {
        case 0: button0Down = buttonDown; break;
        case 1: button1Down = buttonDown; break;
        case 2: button2Down = buttonDown; break;
      }

      NiftyMouseInputEvent result = pool.allocate();
      result.initialize(renderEngine.convertFromNativeX(mouseX), renderEngine.convertFromNativeY(mouseY), mouseWheel, button0Down, button1Down, button2Down);
      return result;
    }

    private boolean processEvent(final NiftyMouseInputEvent mouseInputEvent) {
      boolean handled = false;
      if (mouseInputEventProcessor.canProcess(mouseInputEvent)) {
        mouseInputEventProcessor.process(mouseInputEvent);
        handled = forwardMouseEventToScreen(mouseInputEvent);
        handleDynamicElements();
      }
      pool.free(mouseInputEvent);
      return handled;
    }
  }

  /**
   * Helper class to connect better to the eventbus.
   * @author void
   */
  @SuppressWarnings("rawtypes")
  private static class ClassSaveEventTopicSubscriber implements EventTopicSubscriber, ProxySubscriber {
    private String elementId;
    private EventTopicSubscriber target;
    private Class eventClass;

    private ClassSaveEventTopicSubscriber(final String elementId, final EventTopicSubscriber target, final Class eventClass) {
      this.elementId = elementId;
      this.target = target;
      this.eventClass = eventClass;
    }

    public String toString() {
      return super.toString() + "{" + elementId + "}{" + target + "}{" + eventClass + "}";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEvent(final String topic, final Object data) {
      if (eventClass.isInstance(data)) {
        target.onEvent(topic, data);
      }
    }

    public String getElementId() {
      return elementId;
    }

    @Override
    public Object getProxiedSubscriber() {
      return target;
    }

    @Override
    public void proxyUnsubscribed() {
      this.target = null;
    }

    @Override
    public ReferenceStrength getReferenceStrength() {
      return ReferenceStrength.STRONG;
    }
  }

  public Element createElementFromType(final Screen screen, final Element parent, final ElementType type) {
    if (type instanceof LayerType) {
      return createElementFromTypeInternal(screen, parent, type, getRootLayerFactory().createRootLayerLayoutPart(this));
    }
    return createElementFromTypeInternal(screen, parent, type, new LayoutPart());
  }

  private Element createElementFromTypeInternal(final Screen screen, final Element parent, final ElementType type, final LayoutPart layoutPart) {
    ElementType elementType = type.copy();
    elementType.prepare(this, screen, screen.getRootElement().getElementType());
    elementType.connectParentControls(parent);
    Element element = elementType.create(parent, this, screen, layoutPart);
    if (screen.isBound()) {
      screen.layoutLayers();
      element.bindControls(screen);
      element.initControls();
      element.startEffect(EffectEventId.onStartScreen);
      element.startEffect(EffectEventId.onActive);
      element.onStartScreen();
    }
    return element;
  }

  /**
   * Create a new Image. This is a helper method so that you don't need to get the RenderEngine.
   * @param name file name to use
   * @param filterLinear filter
   * @return RenderImage instance
   */
  public NiftyImage createImage(final String name, final boolean filterLinear) {
    return renderEngine.createImage(name, filterLinear);
  }

  /**
   * You can set this option to true to let Nifty automatically render all panels in random
   * background colors for debugging purposes.
   * @param option enable (true) or disable (false) this feature
   */
  public void setDebugOptionPanelColors(final boolean option) {
    this.debugOptionPanelColors = option;
  }

  /**
   * Returns true if the debug option to render panel colors in enabled.
   * @return true if the option is enabled and false if not
   */
  public boolean isDebugOptionPanelColors() {
    return debugOptionPanelColors;
  }

  /**
   * A helper method to call the special values replace method ${} syntax
   * @param value the value to perform the replace on
   * @return the value with stuff replaced
   */
  public String specialValuesReplace(final String value) {
    return SpecialValuesReplace.replace(value, getResourceBundles(), currentScreen == null ? null : currentScreen.getScreenController(), globalProperties);
  }

  private class SubscriberRegistry {
    private Map < Screen, Map < String, List < ClassSaveEventTopicSubscriber >>> screenBasedSubscribers = new Hashtable < Screen, Map < String, List < ClassSaveEventTopicSubscriber >>>();

    public void register(final Screen screen, final String elementId, final ClassSaveEventTopicSubscriber subscriber) {
      Map < String, List < ClassSaveEventTopicSubscriber >> elements = screenBasedSubscribers.get(screen);
      if (elements == null) {
        elements = new Hashtable < String, List < ClassSaveEventTopicSubscriber >>();
        screenBasedSubscribers.put(screen, elements);
      }
      List < ClassSaveEventTopicSubscriber > list = elements.get(elementId);
      if (list == null) {
        list = new ArrayList < ClassSaveEventTopicSubscriber >();
        elements.put(elementId, list);
      }
      list.add(subscriber);
    }

    public void unsubscribeScreen(final Screen screen) {
      if (screen == null) {
        return;
      }
      Map < String, List < ClassSaveEventTopicSubscriber >> elements = screenBasedSubscribers.get(screen);
      if (elements != null && !elements.isEmpty()) {
        for (Map.Entry < String, List < ClassSaveEventTopicSubscriber >> entry : elements.entrySet()) {
          List < ClassSaveEventTopicSubscriber > list = entry.getValue();
          for (int i=0; i<list.size(); i++) {
            ClassSaveEventTopicSubscriber subscriber = list.get(i);
            getEventService().unsubscribe(subscriber.getElementId(), subscriber);
            NiftyDefaults.eventBusLog.info("<- unsubscribe screen for [" + screen + "] [" + subscriber.getElementId() + "] -> [" + subscriber + "]");
          }
          list.clear();
        }
        elements.clear();
      }
      screenBasedSubscribers.remove(screen);
    }

    public void unsubscribeElement(final Screen screen, final String elementId) {
      if (screen == null || elementId == null) {
        return;
      }
      Map < String, List < ClassSaveEventTopicSubscriber >> elements = screenBasedSubscribers.get(screen);
      if (elements != null && !elements.isEmpty()) {
        List < ClassSaveEventTopicSubscriber > list = elements.get(elementId);
        if (list != null && !list.isEmpty()) {
          for (int i=0; i<list.size(); i++) {
            ClassSaveEventTopicSubscriber subscriber = list.get(i);
            getEventService().unsubscribe(subscriber.getElementId(), subscriber);
            NiftyDefaults.eventBusLog.info("<- unsubscribe element [" + elementId + "] [" + subscriber.getElementId() + "] -> [" + subscriber + "]");
          }
          list.clear();
        }
      }
    }
  }

  public Clipboard getClipboard() {
    return clipboard;
  }

  public void setClipboard(final Clipboard clipboard) {
    this.clipboard = clipboard;
  }

  public RenderFont createFont(final String name) {
    return getRenderEngine().createFont(name);
  }

  public String getFontname(final RenderFont font) {
    return getRenderEngine().getFontname(font);
  }

  /**
   * Enable automatic scaling of all GUI elements in relation to the given base resolution.
   * @param baseResultionX width, for instance 1024
   * @param baseResolutionY height, for instance 768
   */
  public void enableAutoScaling(final int baseResolutionX, final int baseResolutionY) {
    renderEngine.enableAutoScaling(baseResolutionX, baseResolutionY);
  }

  public void enableAutoScaling(final int baseResolutionX, final int baseResolutionY, final float scaleX, final float scaleY) {
    renderEngine.enableAutoScaling(baseResolutionX, baseResolutionY, scaleX, scaleY);
  }

  public void disableAutoScaling() {
    renderEngine.disableAutoScaling();
  }

  /**
   * Return an InputStream for the given resource name. This is resolved
   * using the currently registered ResourceLocations.
   * @param ref the name of the resource to load
   * @return the InputStream of the resource data
   */
  public InputStream getResourceAsStream(final String ref) {
    return resourceLoader.getResourceAsStream(ref);
  }

  /**
   * Return the ResourceLoader of this Nifty instance.
   * @return the ResourceLoader to load resources
   */
  public NiftyResourceLoader getResourceLoader() {
    return resourceLoader;
  }
}
