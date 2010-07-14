package de.lessvoid.nifty.examples.console;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.console.controller.ConsoleCommandHandler;
import de.lessvoid.nifty.controls.console.controller.ConsoleControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ConsoleDemoStartScreen.
 * @author void
 */
public class ConsoleSameScreenStartScreen implements ScreenController, KeyInputHandler {
  private Nifty nifty;
  private Screen screen;
  private boolean consoleVisible = false;
  private boolean allowConsoleToggle = true;
  private Element oldFocusElement;
  private Element consoleElement;
  private Element consoleLayer;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    nifty = newNifty;
    screen = newScreen;
    screen.addKeyboardInputHandler(new DefaultInputMapping(), this);

    consoleElement = screen.findElementByName("console");
    consoleLayer = screen.findElementByName("consoleLayer");

    final ConsoleControl control = screen.findControl("console", ConsoleControl.class);
    control.output("Nifty Console Demo\nVersion: 1.0");
    control.addCommandHandler(new ConsoleCommandHandler() {
      public void execute(final String line) {
        // just echo to the console
        control.output("your input was: " + line);
        if ("exit".equals(line.toLowerCase())) {
          back();
        }
      }
    });
  }

  public void onStartScreen() {
    removeConsoleElementFromFocusHandler();
  }

  public void onEndScreen() {
  }

  public void back() {
    nifty.fromXml("all/intro.xml", "menu");
  }

  public boolean keyEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.ConsoleToggle) {
      toggleConsole();
      return true;
    } else {
      return false;
    }
  }

  private void toggleConsole() {
    if (allowConsoleToggle) {
      allowConsoleToggle = false;
      if (consoleVisible) {
        closeConsole();
      } else {
        openConsole();
      }
    }
  }

  private void openConsole() {
    consoleLayer.showWithoutEffects();
    consoleLayer.startEffect(EffectEventId.onStartScreen, new EndNotify() {
      @Override
      public void perform() {
        oldFocusElement = screen.getFocusHandler().getKeyboardFocusElement();
        // add the consoleElement to the focushandler, when it's not yet added already
        addConsoleElementToFocusHandler();
        consoleElement.setFocus();

        consoleVisible = true;
        allowConsoleToggle = true;
      }
    });
  }

  private void closeConsole() {
    consoleLayer.startEffect(EffectEventId.onEndScreen, new EndNotify() {
      @Override
      public void perform() {
        consoleLayer.hideWithoutEffect();

        consoleVisible = false;
        allowConsoleToggle = true;

        if (oldFocusElement != null) {
          oldFocusElement.setFocus();
        }

        removeConsoleElementFromFocusHandler();
      }
    });
  }

  private void addConsoleElementToFocusHandler() {
    if (screen.getFocusHandler().findElement(consoleElement.getId()) == null) {
      screen.getFocusHandler().addElement(consoleElement);
    }
  }

  private void removeConsoleElementFromFocusHandler() {
    screen.getFocusHandler().remove(consoleElement);
  }
}
