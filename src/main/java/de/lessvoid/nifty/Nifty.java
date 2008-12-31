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

import org.newdawn.slick.util.ResourceLoader;

import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEventCreator;
import de.lessvoid.nifty.input.mapping.Default;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEventQueue;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.NiftyLoader;
import de.lessvoid.nifty.loader.xpp3.elements.ControlType;
import de.lessvoid.nifty.loader.xpp3.elements.PopupType;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.NiftyRenderEngineImpl;
import de.lessvoid.nifty.render.spi.RenderDevice;
import de.lessvoid.nifty.screen.NullScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * The main Nifty class.
 * @author void
 */
public class Nifty {

  /**
   * The logger.
   */
  private Logger log = Logger.getLogger(Nifty.class.getName());

  /**
   * RenderDevice.
   */
  private NiftyRenderEngine renderDevice;

  /**
   * SoundSystem.
   */
  private SoundSystem soundSystem;

  /**
   * All screens with Id of screen as the key.
   */
  private Map < String, Screen > screens = new Hashtable < String, Screen >();

  /**
   * All popups with Id as the key.
   */
  private Map < String, PopupType > popups = new Hashtable < String, PopupType >();
  private Map < String, Element > activePopups = new Hashtable < String, Element >();

  /**
   * The current screen.
   */
  private Screen currentScreen;

  /**
   * The current xml file loaded.
   */
  private String currentLoaded;

  /**
   * When everything is done exit is true.
   */
  private boolean exit;

  /**
   * console for debugging purpose.
   * @param newRenderDevice RenderDevice
   * @param options options
   */
  private NiftyDebugConsole console;

  /**
   * The TimeProvider to use.
   */
  private TimeProvider timeProvider;

  /**
   * store the RemovePopUp id we need to remove.
   */
  private List < RemovePopUp > removePopupList = new ArrayList < RemovePopUp >();

  /**
   * nifty loader.
   */
  private NiftyLoader loader = new NiftyLoader();

  /**
   * these controls need to be added.
   */
  private List < ControlToAdd > controlsToAdd = new ArrayList < ControlToAdd >();

  /**
   * these elements need to be removed.
   */
  private List < ElementToRemove > elementsToRemove = new ArrayList < ElementToRemove >();

  /**
   * use debug console.
   */
  private boolean useDebugConsole;

  /**
   * KeyboardInputEventCreator.
   */
  private KeyboardInputEventCreator inputEventCreator;

  /**
   * MouseInputEventQueue.
   */
  private MouseInputEventQueue mouseInputEventQueue;

  /**
   * collection of registered ScreenController instances.
   */
  private Collection < ScreenController > registeredScreenControllers = new ArrayList < ScreenController >();

  /**
   * last x position of mouse.
   */
  private int lastMouseX;

  /**
   * last y position of mouse.
   */
  private int lastMouseY;

  private String alternateKeyForNextLoadXml;

  public void setAlternateKeyForNextLoadXml(final String alternateKeyForNextLoadXmlParam) {
    alternateKeyForNextLoadXml = alternateKeyForNextLoadXmlParam;
  }

  /**
   * Create nifty for the given RenderDevice and TimeProvider.
   * @param newRenderDevice the RenderDevice
   * @param newSoundSystem SoundSystem
   * @param newTimeProvider the TimeProvider
   */
  public Nifty(
      final NiftyRenderEngine newRenderDevice,
      final SoundSystem newSoundSystem,
      final TimeProvider newTimeProvider) {
    initialize(newRenderDevice, newSoundSystem, newTimeProvider);
    console = new NiftyDebugConsole(null); // this will cause trouble i'm sure, but i don't care at this point
  }

  /**
   * Create nifty with optional console parameter.
   * @param newRenderDevice the RenderDevice
   * @param newSoundSystem SoundSystem
   * @param newTimeProvider the TimeProvider
   */
  public Nifty(
      final RenderDevice newRenderDevice,
      final SoundSystem newSoundSystem,
      final TimeProvider newTimeProvider) {
    initialize(new NiftyRenderEngineImpl(newRenderDevice), newSoundSystem, newTimeProvider);
    console = new NiftyDebugConsole(newRenderDevice);
  }

  /**
   * Initialize this instance.
   * @param newRenderDevice RenderDevice
   * @param newSoundSystem SoundSystem
   * @param newTimeProvider TimeProvider
   */
  private void initialize(
      final NiftyRenderEngine newRenderDevice,
      final SoundSystem newSoundSystem,
      final TimeProvider newTimeProvider) {
    this.renderDevice = newRenderDevice;
    this.soundSystem = newSoundSystem;
    this.timeProvider = newTimeProvider;
    this.exit = false;
    this.currentLoaded = null;
    this.inputEventCreator = new KeyboardInputEventCreator();
    this.mouseInputEventQueue = new MouseInputEventQueue();
  }

  /**
   * Render all stuff in the current Screen.
   * @param clearScreen TODO
   * @param mouseX TODO
   * @param mouseY TODO
   * @param mouseDown TODO
   * @return true when nifty has finished processing the screen and false when rendering should continue.
   */
  public boolean render(
      final boolean clearScreen,
      final int mouseX,
      final int mouseY,
      final boolean mouseDown) {
    if (currentScreen != null) {
        mouseInputEventQueue.process(mouseX, mouseY, mouseDown);
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }
    return render(clearScreen);
  }

  /**
   * This is a replacement render that does not take a mouse event.
   * @param clearScreen TODO
   * @return true when nifty has finished processing the screen and false when rendering should continue.
   */
  public boolean render(final boolean clearScreen) {
    if (clearScreen) {
      renderDevice.clear();
    }

    if (currentScreen != null) {
      MouseInputEvent inputEvent = mouseInputEventQueue.peek();
      if (inputEvent != null) {
        currentScreen.mouseEvent(inputEvent);
        mouseInputEventQueue.remove();
      }
      currentScreen.renderLayers(renderDevice);

      if (useDebugConsole) {
        console.render(this, currentScreen, renderDevice);
      }
    }

    if (exit) {
      renderDevice.clear();
    }

    if (!removePopupList.isEmpty()) {
      if (currentScreen != null) {
        for (RemovePopUp removePopup : removePopupList) {
          removePopup.close();
        }
      }
      removePopupList.clear();
    }

    if (currentScreen != null) {
      currentScreen.processAddAndRemoveLayerElements();
    }

    addControls();
    removeElements();
    return exit;
  }

  public void addControls() {
    if (!controlsToAdd.isEmpty()) {
      for (ControlToAdd controlToAdd : controlsToAdd) {
        Element newControl = controlToAdd.createControl();
        controlToAdd.startControl(newControl);
      }
      controlsToAdd.clear();
    }
  }

  public void addControlsWithoutStartScreen() {
    if (!controlsToAdd.isEmpty()) {
      for (ControlToAdd controlToAdd : controlsToAdd) {
        controlToAdd.createControl();
      }
      controlsToAdd.clear();
    }
  }

  /**
   * remove elements.
   */
  private void removeElements() {
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
    try {
      loader.loadXml(ResourceLoader.getResourceAsStream(filename), this, screens, timeProvider);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * load from the given file.
   * @param stream stream to load
   */
  private void loadFromStream(final InputStream stream) {
    try {
      loader.loadXml(stream, this, screens, timeProvider);
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
    if (currentScreen == null) {
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
    currentScreen = screens.get(id);
    if (currentScreen == null) {
      log.warning("screen [" + id + "] not found");
      return;
    }

    // start the new screen
    if (alternateKeyForNextLoadXml != null) {
      currentScreen.setAlternateKey(alternateKeyForNextLoadXml);
      alternateKeyForNextLoadXml = null;
    }
    currentScreen.startScreen();
  }

  /**
   * Set alternate key for all screen. This could be used to change behavior on all screens.
   * @param alternateKey the new alternate key to use
   */
  public void setAlternateKey(final String alternateKey) {
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
            currentScreen = null;
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
   * keyboard event.
   * @param eventKey eventKey
   * @param eventCharacter the character
   * @param keyDown key down
   */
  public void keyEvent(final int eventKey, final char eventCharacter, final boolean keyDown) {
//    System.out.println("key " + eventKey + ", character " + eventCharacter + ", keyDown: " + keyDown);
    if (currentScreen != null) {
      currentScreen.keyEvent(inputEventCreator.createEvent(eventKey, eventCharacter, keyDown));
    }
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
  public NiftyRenderEngine getRenderDevice() {
    return renderDevice;
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
      if (currentScreen != null && currentScreen.getScreenId().equals(screenId)) {
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
    popups.put(popup.getAttributes().getId(), popup);
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

  private Element createPopupFromType(final PopupType popupType) {
    NiftyInputControl niftyInputControl = null;
    Controller controllerInstance = null;
    if (popupType.hasController()) {
      controllerInstance = popupType.getControllerInstance(this);
      if (controllerInstance != null) {
        niftyInputControl = new NiftyInputControl(controllerInstance, new Default());
      }
    }
    log.fine("createPopupFromType: " + controllerInstance + ", " + niftyInputControl);
    Element popupElement = popupType.createElement(
        this,
        getCurrentScreen(),
        getLoader().getRegisteredEffects(),
        getLoader().getRegisteredControls(),
        getStyleHandler(),
        timeProvider,
        niftyInputControl,
        new NullScreenController());
    if (controllerInstance != null) {
      controllerInstance.bind(
          this,
          popupElement,
          null,
          null,
          new Attributes());
    }
    return popupElement;
  }

  /**
   * Get Popup.
   * @param id get id
   * @return popup element
   */
  public Element createPopup(final String id) {
    Element popupElement = createPopupFromType(popups.get(id));
    activePopups.put(id, popupElement);
    return popupElement;
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
    popup.resetEffects();
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
   */
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

  /**
   * ControlToAdd helper class.
   * @author void
   */
  private class ControlToAdd {
    /**
     * screen.
     */
    private Screen screen;

    /**
     * parent element.
     */
    private Element parent;

    /**
     * control name.
     */
    private String controlName;

    /**
     * control id.
     */
    private String controlId;

    /**
     * current style.
     */
    private String style;

    /**
     * focusable.
     */
    private Boolean focusable;

    private Attributes attr;

    public ControlToAdd(
        final Screen newScreen,
        final Element newParent,
        final String newControlName,
        final String newId,
        final String newStyle,
        final Boolean newFocusable,
        final Attributes newAttributes) {
      this.screen = newScreen;
      this.parent = newParent;
      this.controlName = newControlName;
      this.controlId = newId;
      this.style = newStyle;
      this.focusable = newFocusable;
      this.attr = newAttributes;
      if (attr == null) {
        attr = new Attributes();
      }
    }

    public void startControl(final Element newControl) {
      newControl.startEffect(EffectEventId.onStartScreen);
      newControl.startEffect(EffectEventId.onActive);
      newControl.onStartScreen(screen);
    }

    /**
     * create the control.
     */
    public Element createControl() {
      TypeContext typeContext = createTypeContext();

      attr.overwriteAttribute("id", controlId);
      if (style != null) {
        attr.overwriteAttribute("style", style);
      }

      attr.set("name", controlName);
      ControlType controlType = typeContext.createControlType(attr);
      Element newControl = controlType.createElement(
          parent,
          screen,
          null,
          screen.getScreenController());
      if (focusable != null) {
        newControl.setFocusable(focusable);
      }
      screen.layoutLayers();
      return newControl;
    }
  }

  /**
   * ElementToRemove helper.
   * @author void
   */
  private class ElementToRemove {
    /**
     * screen.
     */
    private Screen screen;

    /**
     * element.
     */
    private Element element;

    /**
     * create it.
     * @param newScreen screen
     * @param newElement element
     */
    public ElementToRemove(final Screen newScreen, final Element newElement) {
      this.screen = newScreen;
      this.element = newElement;
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
        elementsToRemove.add(new ElementToRemove(screen, element));
      }
    });
  }

  /**
   * toggle debug console on/off.
   * @param outputEffects outputEffects
   */
  public void toggleDebugConsole(final boolean outputEffects) {
    useDebugConsole = !useDebugConsole;
    console.setOutputElements(outputEffects);
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

  /**
   * get last mouse x.
   * @return mouse x
   */
  public int getLastMouseX() {
    return lastMouseX;
  }

  /**
   * get last mouse y.
   * @return mouse x
   */
  public int getLastMouseY() {
    return lastMouseY;
  }

  /**
   * Get StyleHandler.
   * @return StyleHandler
   */
  public StyleHandler getStyleHandler() {
    return loader.getStyleHandler();
  }

  /**
   * Get Loader.
   * @return NiftyLoader
   */
  public NiftyLoader getLoader() {
    return loader;
  }

  public TimeProvider getTimeProvider() {
    return timeProvider;
  }

  public TypeContext createTypeContext() {
    TypeContext typeContext = new TypeContext(
        loader.getStyleHandler(),
        this,
        loader.getRegisteredEffects(),
        loader.getRegisteredControls(),
        timeProvider
        );
    return typeContext;
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
}
