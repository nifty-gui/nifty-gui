package de.lessvoid.nifty.examples.slick.niftyoverlay;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.lwjglslick.render.RenderDeviceLwjgl;
import de.lessvoid.nifty.lwjglslick.sound.SlickSoundDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * This is the original TestState1 from slick tests extended to a nifty gui
 * overlay. This only implements from ScreenController because we have a
 * quit() onClick action definied in the nifty xml file that is handled in here
 * to quit the demo.
 * @author void
 */
public class TestState1 extends BasicGameState implements ScreenController {
  public static final int ID = 1;
  private GameContainer container;
  private Font font;
  private Color currentColor;
  private Nifty nifty;
  private List<MouseInputEvent> mouseEvents = new ArrayList<MouseInputEvent>();
  private int mouseX;
  private int mouseY;
  private boolean mouseDown;
  
  public int getID() {
    return ID;
  }

  public void init(GameContainer container, StateBasedGame game) throws SlickException {
    this.container = container;
    font = new AngelCodeFont("menu.fnt", "menu.png");
    currentColor = Color.white;

    // create nifty
    nifty = new Nifty(new RenderDeviceLwjgl(), new SoundSystem(new SlickSoundDevice()), new InputSystem() {
      public List<MouseInputEvent> getMouseEvents() {
        ArrayList<MouseInputEvent> result = new ArrayList<MouseInputEvent>(mouseEvents);
        mouseEvents.clear();
        return result;
      }
    }, new TimeProvider());
    nifty.fromXml("slick/niftyoverlay/overlay.xml", "start", this);
  }

  public void render(GameContainer container, StateBasedGame game, Graphics g) {
    g.setFont(font);
    g.setColor(currentColor);
    g.drawString("State Based Game Test", 100, 100);
    g.drawString("1-3 will switch between colors", 100, 300);
    g.drawString("(this is all slick rendering!)", 100, 400);
    g.drawString("and this is more slick text", 360, 650);
    g.drawString("below (!) a nifty-gui overlay", 360, 700);

    SlickCallable.enterSafeBlock();
    nifty.render(false);
    SlickCallable.leaveSafeBlock();
  }

  public void update(GameContainer container, StateBasedGame game, int delta) {
  }

  public void keyReleased(int key, char c) {
    if (key == Input.KEY_1) {
      currentColor = Color.red;
      getElement("red").startEffect(EffectEventId.onCustom);
    }
    if (key == Input.KEY_2) {
      currentColor = Color.green;
      getElement("green").startEffect(EffectEventId.onCustom);
    }
    if (key == Input.KEY_3) {
      currentColor = Color.blue;
      getElement("blue").startEffect(EffectEventId.onCustom);
    }
  }

  private Element getElement(final String id) {
    return nifty.getCurrentScreen().findElementByName(id);
  }

  public void mouseMoved(final int oldx, final int oldy, final int newx, final int newy) {
    mouseX = newx;
    mouseY = newy;
    forwardMouseEventToNifty(mouseX, mouseY, mouseDown);
  }

  public void mousePressed(final int button, final int x, final int y) {
    mouseX = x;
    mouseY = y;
    mouseDown = true;
    forwardMouseEventToNifty(mouseX, mouseY, mouseDown);
  }

  public void mouseReleased(final int button, final int x, final int y) {
    mouseX = x;
    mouseY = y;
    mouseDown = false;
    forwardMouseEventToNifty(mouseX, mouseY, mouseDown);
  }

  private void forwardMouseEventToNifty(final int mouseX, final int mouseY, final boolean mouseDown) {
    mouseEvents.add(new MouseInputEvent(mouseX, container.getHeight() - mouseY, mouseDown));
  }

  public void bind(Nifty nifty, Screen screen) {
  }

  public void onEndScreen() {
  }

  public void onStartScreen() {
  }

  public void quit() {
    nifty.getCurrentScreen().endScreen(new EndNotify() {
      public void perform() {
        container.exit();
      }
    });
  }
}