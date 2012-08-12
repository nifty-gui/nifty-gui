package de.lessvoid.nifty.controls.dynamic;

import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.xml.tools.ClassHelper;

public class ScreenCreator {
  private static Logger log = Logger.getLogger(ScreenCreator.class.getName());

  private String id;
  private ScreenController screenController;
  private String defaultFocusElement;
  private String inputMapping;
  private String inputMappingPre;

  public ScreenCreator(final String id) {
    this.id = id;
  }

  public ScreenCreator(final String id, final ScreenController screenController) {
    this.id = id;
    this.screenController = screenController;
  }

  public void setScreenController(final ScreenController screenController) {
    this.screenController = screenController;
  }

  public void setDefaultFocusElement(final String defaultFocusElement) {
    this.defaultFocusElement = defaultFocusElement;
  }

  public void setInputMapping(final String inputMapping) {
    this.inputMapping = inputMapping;
  }

  public void setInputMappingPre(final String inputMappingPre) {
    this.inputMappingPre = inputMappingPre;
  }

  public Screen create(final Nifty nifty) {
    Screen screen = createScreen(nifty);

    addRootElement(nifty, screen);
    addDefaultFocusElement(screen);
    addInputMapping(screen, inputMapping);
    addPreInputMapping(screen, inputMappingPre);

    nifty.addScreen(id, screen);
    return screen;
  }

  private Screen createScreen(final Nifty nifty) {
    return new Screen(nifty, id, screenController, nifty.getTimeProvider());
  }

  private void addRootElement(final Nifty nifty, final Screen screen) {
    Element rootElement = nifty.getRootLayerFactory().createRootLayer("root", nifty, screen, nifty.getTimeProvider());
    screen.setRootElement(rootElement);
  }

  private void addDefaultFocusElement(final Screen screen) {
    screen.setDefaultFocusElement(defaultFocusElement);
  }

  private void addInputMapping(final Screen screen, final String inputMappingClass) {
    if (inputMappingClass != null) {
      NiftyInputMapping inputMapping = ClassHelper.getInstance(inputMappingClass, NiftyInputMapping.class);
      if (!(screenController instanceof KeyInputHandler)) {
        log.info("class [" + screenController + "] tries to use inputMapping [" + inputMappingClass + "] but does not implement [" + KeyInputHandler.class.getName() + "]");
      } else {
        screen.addKeyboardInputHandler(inputMapping, KeyInputHandler.class.cast(screenController));
      }
    }
  }

  private void addPreInputMapping(final Screen screen, final String inputMappingClass) {
    if (inputMappingClass != null) {
      NiftyInputMapping inputMapping = ClassHelper.getInstance(inputMappingClass, NiftyInputMapping.class);
      if (!(screenController instanceof KeyInputHandler)) {
        log.info("class [" + screenController + "] tries to use inputMapping [" + inputMappingClass + "] but does not implement [" + KeyInputHandler.class.getName() + "]");
      } else {
        screen.addPreKeyboardInputHandler(inputMapping, KeyInputHandler.class.cast(screenController));
      }
    }
  }
}
