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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * ConsoleDemoStartScreen.
 *
 * @author void
 */
public class ConsoleSameScreenStartScreen implements ScreenController, KeyInputHandler, NiftyExample {
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;
  private boolean consoleVisible = false;
  private boolean allowConsoleToggle = true;
  @Nullable
  private Element oldFocusElement;
  @Nullable
  private Element consoleElementFocus;
  @Nullable
  private Element consoleLayer;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen newScreen) {
    nifty = newNifty;
    screen = newScreen;
    screen.addKeyboardInputHandler(new DefaultInputMapping(), this);

    Element consoleElement = screen.findElementById("console");
    if (consoleElement != null) {
      consoleElementFocus = consoleElement.findElementById("#textInput");
    }
    consoleLayer = screen.findElementById("consoleLayer");
  }

  @Override
  public void onStartScreen() {
    removeConsoleElementFromFocusHandler();
  }

  @Override
  public void onEndScreen() {
  }

  public void back() {
    if (nifty != null) {
      nifty.fromXml("all/intro.xml", "menu");
    }
  }

  @Override
  public boolean keyEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.ConsoleToggle) {
      toggleConsole();
      return true;
    } else {
      return false;
    }
  }

  @NiftyEventSubscriber(id = "console")
  public void onConsoleCommand(final String id, @Nonnull final ConsoleExecuteCommandEvent command) {
    if (screen == null) {
      return;
    }
    Console console = screen.findNiftyControl("console", Console.class);
    if (console != null) {
      console.output("your input was: " + command.getCommandLine() + " [" + command.getArgumentCount() + " parameter" +
          "(s)]");
    }
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
    if (consoleLayer != null && screen != null && consoleElementFocus != null) {
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
  }

  private void closeConsole() {
    if (consoleLayer != null) {
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
  }

  private void addConsoleElementToFocusHandler() {
    String id = consoleElementFocus == null ? null : consoleElementFocus.getId();
    if (id == null) {
      return;
    }
    if (screen != null && screen.getFocusHandler().findElement(id) == null) {
      screen.getFocusHandler().addElement(consoleElementFocus);
    }
  }

  private void removeConsoleElementFromFocusHandler() {
    if (screen != null) {
      screen.getFocusHandler().remove(consoleElementFocus);
    }
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "console/console-samescreen.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Console Same Screen Demonstation";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
