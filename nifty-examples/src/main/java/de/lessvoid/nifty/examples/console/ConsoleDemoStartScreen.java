package de.lessvoid.nifty.examples.console;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;
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
 * @author void
 */
public class ConsoleDemoStartScreen implements ScreenController, KeyInputHandler, NiftyExample {
  private Nifty nifty;
  private Screen screen;
  @Nullable
  private Element consolePopup;
  private boolean consoleVisible = false;
  private boolean allowConsoleToggle = true;
  private boolean firstConsoleShow = true;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen newScreen) {
    nifty = newNifty;
    screen = newScreen;
    screen.addKeyboardInputHandler(new DefaultInputMapping(), this);
    consolePopup = nifty.createPopup("consolePopup");
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void back() {
    nifty.fromXml("all/intro.xml", "menu");
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
    String id = consolePopup != null ? consolePopup.getId() : null;
    if (id == null) {
      return;
    }
    nifty.showPopup(screen, id, consolePopup.findElementById("console#textInput"));
    screen.processAddAndRemoveLayerElements();

    if (firstConsoleShow) {
      firstConsoleShow = false;
      Console console = screen.findNiftyControl("console", Console.class);
      if (console != null) {
        console.output("Nifty Console Demo\nVersion: 2.0");
      }
    }

    consoleVisible = true;
    allowConsoleToggle = true;
  }

  private void closeConsole() {
    String id = consolePopup != null ? consolePopup.getId() : null;
    if (id == null) {
      return;
    }
    nifty.closePopup(id, new EndNotify() {
      @Override
      public void perform() {
        consoleVisible = false;
        allowConsoleToggle = true;
      }
    });
  }

  @NiftyEventSubscriber(id = "console")
  public void onConsoleCommand(final String id, @Nonnull final ConsoleExecuteCommandEvent command) {
    Console console = screen.findNiftyControl("console", Console.class);
    if (console != null) {
      console.output("your input was: " + command.getCommandLine() + " [" + command.getArgumentCount() + " parameter" +
          "(s)]");
    }
    if ("exit".equals(command.getCommand())) {
      back();
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
    return "console/console.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Console Demonstation";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
