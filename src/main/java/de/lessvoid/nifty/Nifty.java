package de.lessvoid.nifty;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEventCreator;
import de.lessvoid.nifty.loader.xpp3.NiftyLoader;
import de.lessvoid.nifty.loader.xpp3.elements.AttributesType;
import de.lessvoid.nifty.loader.xpp3.elements.ControlType;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.NiftyRenderEngineImpl;
import de.lessvoid.nifty.render.spi.RenderDevice;
import de.lessvoid.nifty.screen.Screen;
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
  private Map < String, Element > popups = new Hashtable < String, Element >();

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
  private NiftyDebugConsole console = new NiftyDebugConsole();

  /**
   * The TimeProvider to use.
   */
  private TimeProvider timeProvider;

  /**
   * store the popup id we need to remove.
   */
  private String removePopupId = null;

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
    addControls();
    removeElements();

    if (clearScreen) {
      renderDevice.clear();
    }

    if (currentScreen != null) {
      currentScreen.mouseEvent(mouseX, mouseY, mouseDown);
      currentScreen.renderLayers(renderDevice);

      if (useDebugConsole) {
        console.render(currentScreen, renderDevice);
      }
    }

    if (exit) {
      renderDevice.clear();
    }

    if (removePopupId != null) {
      currentScreen.closePopup(popups.get(removePopupId));
      removePopupId = null;
    }

    return exit;
  }

  /**
   * add controls.
   */
  private void addControls() {
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
   * load from the given file.
   * @param filename filename to load
   */
  private void loadFromFile(final String filename) {
    try {
      loader.loadXml(
          Thread.currentThread().getContextClassLoader().getResourceAsStream(filename), this, screens, timeProvider);
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
  private void prepareScreens(final String xmlId) {
    screens.clear();

    this.currentScreen = null;
    this.currentLoaded = xmlId;
    this.exit = false;
  }

  /**
   * goto screen command. this will send first an endScreen event to the current screen.
   * @param id the new screen id we should go to.
   */
  public final void gotoScreen(final String id) {
    if (currentScreen == null) {
      gotoScreenInternal(id);
    } else {
      // end current screen
      currentScreen.endScreen(
          new EndNotify() {
            public final void perform() {
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
   * exit.
   */
  public void exit() {
    currentScreen.endScreen(
        new EndNotify() {
          public final void perform() {
            exit = true;
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
  public void registerPopup(final Element popup) {
    popups.put(popup.getId(), popup);
  }

  /**
   * show popup in the given screen.
   * @param screen screen
   * @param id id
   */
  public void showPopup(final Screen screen, final String id) {
    Element popup = popups.get(id);
    if (popup == null) {
      log.warning("missing popup [" + id + "] o_O");
    } else {
      screen.addPopup(popup);
    }
  }

  /**
   * close the given popup on the given screen.
   * @param id the popup id
   */
  public void closePopup(final String id) {
    Element popup = popups.get(id);
    if (popup == null) {
      log.warning("missing popup [" + id + "] o_O");
    } else {
      popup.startEffect(EffectEventId.onEndScreen, timeProvider, new EndNotify() {
        public void perform() {
          removePopupId = id;
        }
      });
    }
  }

  /**
   * Add a control to this screen and the given parent element.
   * @param screen screen
   * @param parent parent element
   * @param controlName control name to add
   * @param id id of control
   */
  public void addControl(final Screen screen, final Element parent, final String controlName, final String id) {
    controlsToAdd.add(new ControlToAdd(screen, parent, controlName, id));
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
     * create new.
     * @param newScreen screen
     * @param newParent parent
     * @param newControlName control name
     * @param newId id if control
     */
    public ControlToAdd(
        final Screen newScreen,
        final Element newParent,
        final String newControlName,
        final String newId) {
      this.screen = newScreen;
      this.parent = newParent;
      this.controlName = newControlName;
      this.controlId = newId;
    }

    /**
     * create the control.
     */
    public void createControl() {
      ControlType controlType = new ControlType(controlName);
      AttributesType attributeType = new AttributesType();
      attributeType.setId(controlId);
      controlType.setAttributes(attributeType);
      Element newControl = controlType.createElement(
          parent,
          Nifty.this,
          screen,
          loader.getRegisteredEffects(),
          loader.getRegisteredControls(),
          loader.getStyleHandler(),
          timeProvider,
          null,
          screen.getScreenController());
      screen.layoutLayers();

      newControl.onStartScreen(Nifty.this, screen);
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
      element.remove();
      screen.layoutLayers();
    }
  }

  /**
   * Remove the given element from the given screen.
   * @param screen screen
   * @param element element to remove
   */
  public void removeElement(final Screen screen, final Element element) {
    elementsToRemove.add(new ElementToRemove(screen, element));
  }

  /**
   * toggle debug console on/off.
   */
  public void toggleDebugConsole() {
    useDebugConsole = !useDebugConsole;
  }
}
