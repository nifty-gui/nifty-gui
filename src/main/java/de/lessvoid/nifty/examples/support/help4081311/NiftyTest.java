package de.lessvoid.nifty.examples.support.help4081311;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.SlickCallable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.lwjglslick.input.LwjglKeyboardInputEventCreator;
import de.lessvoid.nifty.lwjglslick.render.RenderDeviceLwjgl;
import de.lessvoid.nifty.lwjglslick.sound.SlickSoundDevice;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.TimeProvider;

public class NiftyTest extends BasicGame {
  private Nifty nifty = null;
  private List < MouseInputEvent > mouseEvents = new ArrayList < MouseInputEvent >();
  private List < KeyboardInputEvent > keyEvents = new ArrayList < KeyboardInputEvent >();
  private LwjglKeyboardInputEventCreator inputEventCreator = new LwjglKeyboardInputEventCreator(); 
  private boolean mouseDown;
  private int height;

  public NiftyTest() {
    super("Nifty test");
  }

  @Override
  public void init(GameContainer gc) throws SlickException {
    nifty = new Nifty(
        new RenderDeviceLwjgl(),
        new SlickSoundDevice(),
        new InputSystem() {
          public void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
            for (MouseInputEvent event : mouseEvents) {
              inputEventConsumer.processMouseEvent(event);
            }
            mouseEvents.clear();
    
            for (KeyboardInputEvent event : keyEvents) {
              inputEventConsumer.processKeyboardEvent(event);
            }
            keyEvents.clear();
          }

          @Override
          public void setMousePosition(int x, int y) {
          }
        }, new TimeProvider());
    nifty.fromXml("de/lessvoid/nifty/examples/support/help4081311/nifty.xml", "screen");
  }

  @Override
  public void update(GameContainer gc, int delta) throws SlickException {
    nifty.update();
  }

  @Override
  public void render(GameContainer gc, Graphics g) throws SlickException {
    SlickCallable.enterSafeBlock();
    nifty.render(true);
    SlickCallable.leaveSafeBlock();
  }

  @Override
  public void keyPressed(final int key, final char c) {
    keyEvents.add(inputEventCreator.createEvent(key, c, true));
  }

  @Override
  public void keyReleased(final int key, final char c) {
    keyEvents.add(inputEventCreator.createEvent(key, c, false));
  }

  @Override
  public void mouseMoved(final int oldx, final int oldy, final int newx, final int newy) {
    forwardMouseEventToNifty(newx, newy, mouseDown);
  }

  @Override
  public void mousePressed(final int button, final int x, final int y) {
    mouseDown = true;
    forwardMouseEventToNifty(x, y, mouseDown);
  }

  @Override
  public void mouseReleased(final int button, final int x, final int y) {
    mouseDown = false;
    forwardMouseEventToNifty(x, y, mouseDown);
  }

  private void forwardMouseEventToNifty(final int mouseX, final int mouseY, final boolean mouseDown) {
    mouseEvents.add(new MouseInputEvent(mouseX, height - mouseY, mouseDown));
  }

  private void setHeight(final int height) {
    this.height = height;
  }

  /** * @param args */
  public static void main(String[] args) {
    try {
      NiftyTest niftyTest = new NiftyTest();
      AppGameContainer app = new AppGameContainer(niftyTest);
      app.setDisplayMode(800, 600, false);
      niftyTest.setHeight(app.getHeight());
      app.start();
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }
}
