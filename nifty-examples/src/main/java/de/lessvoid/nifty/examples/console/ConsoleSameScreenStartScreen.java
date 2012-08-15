package de.lessvoid.nifty.examples.console;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ConsoleDemoStartScreen.
 * @author void
 */
public class ConsoleSameScreenStartScreen implements ScreenController, KeyInputHandler, NiftyExample {
  private Nifty nifty;
  private Screen screen;
  private boolean consoleVisible = false;
  private boolean allowConsoleToggle = true;
  private Element oldFocusElement;
  private Element consoleElement;
  private Element consoleElementFocus;
  private Element consoleLayer;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    nifty = newNifty;
    screen = newScreen;
    screen.addKeyboardInputHandler(new DefaultInputMapping(), this);

    consoleElement = screen.findElementByName("console");
    consoleElementFocus = consoleElement.findElementByName("#textInput");
    consoleLayer = screen.findElementByName("consoleLayer");
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
    if (inputEvent == NiftyStandardInputEvent.ConsoleToggle) {
      toggleConsole();
      return true;
    } else {
      return false;
    }
  }

  @NiftyEventSubscriber(id="console")
  public void onConsoleCommand(final String id, final ConsoleExecuteCommandEvent command) {
    Console console = screen.findNiftyControl("console", Console.class);
    console.output("your input was: " + command.getCommandLine() + " [" + command.getArgumentCount() + " parameter(s)]");
    if ("exit".equals(command.getCommand())) {
      back();
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
        consoleElementFocus.setFocus();

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
    if (screen.getFocusHandler().findElement(consoleElementFocus.getId()) == null) {
      screen.getFocusHandler().addElement(consoleElementFocus);
    }
  }

  private void removeConsoleElementFromFocusHandler() {
    screen.getFocusHandler().remove(consoleElementFocus);
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "console/console-samescreen.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty Console Same Screen Demonstation";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
