package de.lessvoid.nifty.examples.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.textfield.TextFieldControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * MainMenu.
 * @author void
 *
 */
public class TextFieldDemoStartScreen implements ScreenController {

  /**
   * the nifty.
   */
  private Nifty nifty;
  private Screen screen;

  /**
   * bind this ScreenController to a screen.
   * @param newRenderDevice RenderDevice
   * @param newNifty nifty
   * @param newScreen screen
   */
  public final void bind(
      final Nifty newNifty,
      final Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;
    screen.findElementByName("maxLengthTest").getControl(TextFieldControl.class).setMaxLength(5);
    screen.findElementByName("name").setFocus();
    screen.findControl("name", TextFieldControl.class).setCursorPosition(3);
    
    Element textElement = screen.findElementByName("my label");
    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    textRenderer.setText("new Text");
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
    System.out.println(screen.findElementByName("password").getControl(TextFieldControl.class).getText());
    nifty.exit();
  }

}
