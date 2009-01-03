package de.lessvoid.nifty.examples.console;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.console.ConsoleCommandHandler;
import de.lessvoid.nifty.controls.console.ConsoleControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * MainMenu.
 * @author void
 *
 */
public class ConsoleDemoStartScreen implements ScreenController, KeyInputHandler {

  /**
   * the nifty.
   */
  private Nifty nifty;

  /**
   * screen.
   */
  private Screen screen;

  /**
   * bind this ScreenController to a screen.
   * @param newNifty nifty
   * @param newScreen screen
   */
  public final void bind(
      final Nifty newNifty,
      final Screen newScreen) {
    nifty = newNifty;

    screen = newScreen;
    screen.addKeyboardInputHandler(new DefaultInputMapping(), this);

    final Element element = nifty.getCurrentScreen().findElementByName("console");
    element.hide();

    final ConsoleControl control = (ConsoleControl) element.getAttachedInputControl().getController();
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

  /**
   * just goto the next screen.
   */
  public final void onStartScreen() {
  }

  /**
   * on end screen.
   */
  public final void onEndScreen() {
  }

  /**
   * back.
   */
  public final void back() {
    nifty.fromXml("all/intro.xml", "menu");
  }

  /**
   * process a keyEvent for the whole screen.
   * @param inputEvent the input event
   * @return true when consumen and false when not
   */
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
