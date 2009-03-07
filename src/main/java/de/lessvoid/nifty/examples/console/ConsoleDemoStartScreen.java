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
public class ConsoleDemoStartScreen implements ScreenController, KeyInputHandler {
  private Nifty nifty;
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    nifty = newNifty;

    screen = newScreen;
    screen.addKeyboardInputHandler(new DefaultInputMapping(), this);

    final Element element = screen.findElementByName("console");
    element.hide();

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
  }

  public void onEndScreen() {
  }

  public void back() {
    nifty.fromXml("all/intro.xml", "menu");
  }

  public boolean keyEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.ConsoleToggle) {
      final Element console = screen.findElementByName("console");
      if (!console.isVisible()) {
        console.show();
        console.setAlternateKey("show");
        console.startEffect(
            EffectEventId.onCustom,
            new EndNotify() {
              public void perform() {
                console.setFocus();
              }
            });
      } else {
        console.setAlternateKey("hide");
        console.startEffect(
            EffectEventId.onCustom,
            new EndNotify() {
              public void perform() {
                console.hide();
              }
            });
      }
      return true;
    } else {
      return false;
    }
  }
}
