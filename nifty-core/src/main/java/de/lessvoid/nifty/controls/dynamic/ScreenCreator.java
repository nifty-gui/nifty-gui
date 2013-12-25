package de.lessvoid.nifty.controls.dynamic;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.xml.tools.ClassHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

public class ScreenCreator {
  @Nonnull
  private static final Logger log = Logger.getLogger(ScreenCreator.class.getName());

  @Nonnull
  private final String id;
  @Nullable
  private ScreenController screenController;
  @Nullable
  private String defaultFocusElement;
  @Nullable
  private String inputMapping;
  @Nullable
  private String inputMappingPre;

  public ScreenCreator(@Nonnull final String id) {
    this(id, null);
  }

  public ScreenCreator(@Nonnull final String id, @Nullable final ScreenController screenController) {
    this.id = id;
    this.screenController = screenController;
  }

  public void setScreenController(@Nullable final ScreenController screenController) {
    this.screenController = screenController;
  }

  public void setDefaultFocusElement(@Nullable final String defaultFocusElement) {
    this.defaultFocusElement = defaultFocusElement;
  }

  public void setInputMapping(@Nullable final String inputMapping) {
    this.inputMapping = inputMapping;
  }

  public void setInputMappingPre(@Nullable final String inputMappingPre) {
    this.inputMappingPre = inputMappingPre;
  }

  @Nonnull
  public Screen create(@Nonnull final Nifty nifty) {
    Screen screen = createScreen(nifty);

    addRootElement(nifty, screen);
    addDefaultFocusElement(screen);
    addInputMapping(screen, inputMapping);
    addPreInputMapping(screen, inputMappingPre);

    nifty.addScreen(id, screen);
    return screen;
  }

  @Nonnull
  private Screen createScreen(@Nonnull final Nifty nifty) {
    ScreenController usedController = screenController;
    if (usedController == null) {
      log.warning("Missing ScreenController for screen [" + id + "] using DefaultScreenController() instead but this " +
          "might not be what you want.");
      usedController = new DefaultScreenController();
    }
    return new Screen(nifty, id, usedController, nifty.getTimeProvider());
  }

  private void addRootElement(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
    Element rootElement = nifty.getRootLayerFactory().createRootLayer("root", nifty, screen, nifty.getTimeProvider());
    screen.setRootElement(rootElement);
  }

  private void addDefaultFocusElement(@Nonnull final Screen screen) {
    screen.setDefaultFocusElement(defaultFocusElement);
  }

  private void addInputMapping(@Nonnull final Screen screen, @Nullable final String inputMappingClass) {
    if (inputMappingClass != null) {
      NiftyInputMapping inputMapping = ClassHelper.getInstance(inputMappingClass, NiftyInputMapping.class);
      if (inputMapping != null) {
        if (!(screenController instanceof KeyInputHandler)) {
          log.warning("class [" + screenController + "] tries to use inputMapping [" + inputMappingClass + "] but " +
              "does not implement [" + KeyInputHandler.class.getName() + "]");
        } else {
          screen.addKeyboardInputHandler(inputMapping, KeyInputHandler.class.cast(screenController));
        }
      }
    }
  }

  private void addPreInputMapping(@Nonnull final Screen screen, @Nullable final String inputMappingClass) {
    if (inputMappingClass != null) {
      NiftyInputMapping inputMapping = ClassHelper.getInstance(inputMappingClass, NiftyInputMapping.class);
      if (inputMapping != null) {
        if (!(screenController instanceof KeyInputHandler)) {
          log.warning("class [" + screenController + "] tries to use inputMapping [" + inputMappingClass + "] but " +
              "does not implement [" + KeyInputHandler.class.getName() + "]");
        } else {
          screen.addPreKeyboardInputHandler(inputMapping, KeyInputHandler.class.cast(screenController));
        }
      }
    }
  }
}
