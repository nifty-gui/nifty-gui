package de.lessvoid.nifty;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.NiftyLoader;
import de.lessvoid.nifty.loader.xpp3.elements.PopupType;
import de.lessvoid.nifty.render.RenderDevice;
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
  private RenderDevice renderDevice;

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
  private String currentFile;

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
   * Create nifty for the given RenderDevice and TimeProvider.
   * @param newRenderDevice the RenderDevice
   * @param newSoundSystem SoundSystem
   * @param newTimeProvider the TimeProvider
   */
  public Nifty(
      final RenderDevice newRenderDevice,
      final SoundSystem newSoundSystem,
      final TimeProvider newTimeProvider) {
    initialize(newRenderDevice, newSoundSystem, newTimeProvider);
  }

  /**
   * Create nifty with optional console parameter.
   * @param newRenderDevice the RenderDevice
   * @param newSoundSystem SoundSystem
   * @param newTimeProvider the TimeProvider
   * @param useConsoleParam use debug console or not
   */
  public Nifty(
      final RenderDevice newRenderDevice,
      final SoundSystem newSoundSystem,
      final TimeProvider newTimeProvider,
      final boolean useConsoleParam) {
    initialize(newRenderDevice, newSoundSystem, newTimeProvider);

    if (useConsoleParam) {
      this.console = new NiftyDebugConsole();
    }
  }

  /**
   * Initialize this instance.
   * @param newRenderDevice RenderDevice
   * @param newSoundSystem SoundSystem
   * @param newTimeProvider TimeProvider
   */
  private void initialize(
      final RenderDevice newRenderDevice,
      final SoundSystem newSoundSystem,
      final TimeProvider newTimeProvider) {
    this.renderDevice = newRenderDevice;
    this.soundSystem = newSoundSystem;
    this.timeProvider = newTimeProvider;
    this.exit = false;
    this.console = null;
    this.currentFile = null;
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
    if (clearScreen) {
      renderDevice.clear();
    }

    if (currentScreen != null) {
      currentScreen.mouseEvent(mouseX, mouseY, mouseDown);
      currentScreen.renderLayers(renderDevice);

      if (console != null) {
        console.render(currentScreen, renderDevice);
      }
    }

    if (exit) {
      renderDevice.clear();
    }
    return exit;
  }

  /**
   * Initialize this Nifty instance from the given xml file.
   * @param filename filename to nifty xml
   */
  public void fromXml(final String filename) {
    start(filename, "start");
  }

  /**
   * Initialize this Nifty instance from the given xml file.
   * @param filename filename to nifty xml
   * @param startScreen screen to start exec
   */
  public void fromXml(final String filename, final String startScreen) {
    start(filename, startScreen);
  }

  /**
   * internal start method.
   * @param filename filename to load
   * @param startScreen screen to start exec
   */
  private void start(final String filename, final String startScreen) {
    try {
      screens.clear();

      this.currentScreen = null;
      this.currentFile = filename;
      this.exit = false;

      NiftyLoader loader = new NiftyLoader();
      loader.loadXml(this, screens, filename, timeProvider);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // start with first screen
    gotoScreen(startScreen);
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
   * @param keyDown TODO
   */
  public void keyEvent(final int eventKey, final char eventCharacter, final boolean keyDown) {
    if (currentScreen != null) {
      currentScreen.keyEvent(eventKey, eventCharacter, keyDown);
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
  public RenderDevice getRenderDevice() {
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
    if (currentFile != null && currentFile.equals(filename)) {
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
}
