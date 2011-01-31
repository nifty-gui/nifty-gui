package de.lessvoid.nifty.slick2d;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.SlickCallable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.slick2d.input.LwjglKeyboardInputEventCreator;
import de.lessvoid.nifty.slick2d.render.SlickRenderDevice;
import de.lessvoid.nifty.slick2d.sound.SlickSoundDevice;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.TimeProvider;

public abstract class NiftyBasicGame implements Game, InputListener {
  /** The maximum number of controllers supported by the basic game */
  private static final int MAX_CONTROLLERS = 20;
  /** The maximum number of controller buttons supported by the basic game */
  private static final int MAX_CONTROLLER_BUTTONS = 100;
  /** The title of the game */
  private String title;
  /** The state of the left control */
  protected boolean[] controllerLeft = new boolean[MAX_CONTROLLERS];
  /** The state of the right control */
  protected boolean[] controllerRight = new boolean[MAX_CONTROLLERS];
  /** The state of the up control */
  protected boolean[] controllerUp = new boolean[MAX_CONTROLLERS];
  /** The state of the down control */
  protected boolean[] controllerDown = new boolean[MAX_CONTROLLERS];
  /** The state of the button controlls */
  protected boolean[][] controllerButton = new boolean[MAX_CONTROLLERS][MAX_CONTROLLER_BUTTONS];
  protected Nifty nifty = null;
  private List<MouseInputEvent> mouseEvents = new ArrayList<MouseInputEvent>();
  private List<KeyboardInputEvent> keyEvents = new ArrayList<KeyboardInputEvent>();
  private LwjglKeyboardInputEventCreator inputEventCreator = new LwjglKeyboardInputEventCreator();
  private boolean mouseDown;
  private int height;

  /**
   * Create a new basic game
   * @param title The title for the game
   */
  public NiftyBasicGame(String title) {
    this.title = title;
  }

  /**
   * @see org.newdawn.slick.InputListener#setInput(org.newdawn.slick.Input)
   */
  public void setInput(Input input) {
  }

  /** 
   * @see org.newdawn.slick.Game#closeRequested()
   */
  public boolean closeRequested() {
    return true;
  }

  /**
   * @see org.newdawn.slick.Game#getTitle()
   */
  public String getTitle() {
    return title;
  }

  @Override
  public void init(GameContainer container) throws SlickException {
    nifty = new Nifty(new SlickRenderDevice(container), new SlickSoundDevice(), new InputSystem() {
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

  protected void setHeight(final int height) {
    this.height = height;
  }

  /** 
   * @see org.newdawn.slick.InputListener#mouseDragged(int, int, int, int)
   */
  public void mouseDragged(int oldx, int oldy, int newx, int newy) {
  }

  /**
   * @see org.newdawn.slick.InputListener#mouseClicked(int, int, int, int)
   */
  public void mouseClicked(int button, int x, int y, int clickCount) {
  }

  /**
   * @see org.newdawn.slick.InputListener#controllerButtonPressed(int, int)
   */
  public void controllerButtonPressed(int controller, int button) {
    controllerButton[controller][button] = true;
  }

  /**
   * @see org.newdawn.slick.InputListener#controllerButtonReleased(int, int)
   */
  public void controllerButtonReleased(int controller, int button) {
    controllerButton[controller][button] = false;
  }

  /**
   * @see org.newdawn.slick.InputListener#controllerDownPressed(int)
   */
  public void controllerDownPressed(int controller) {
    controllerDown[controller] = true;
  }

  /**
   * @see org.newdawn.slick.InputListener#controllerDownReleased(int)
   */
  public void controllerDownReleased(int controller) {
    controllerDown[controller] = false;
  }

  /**
   * @see org.newdawn.slick.InputListener#controllerLeftPressed(int)
   */
  public void controllerLeftPressed(int controller) {
    controllerLeft[controller] = true;
  }

  /** 
   * @see org.newdawn.slick.InputListener#controllerLeftReleased(int)
   */
  public void controllerLeftReleased(int controller) {
    controllerLeft[controller] = false;
  }

  /** 
   * @see org.newdawn.slick.InputListener#controllerRightPressed(int)
   */
  public void controllerRightPressed(int controller) {
    controllerRight[controller] = true;
  }

  /**
   * @see org.newdawn.slick.InputListener#controllerRightReleased(int)
   */
  public void controllerRightReleased(int controller) {
    controllerRight[controller] = false;
  }

  /** 
   * @see org.newdawn.slick.InputListener#controllerUpPressed(int)
   */
  public void controllerUpPressed(int controller) {
    controllerUp[controller] = true;
  }

  /**
   * @see org.newdawn.slick.InputListener#controllerUpReleased(int)
   */
  public void controllerUpReleased(int controller) {
    controllerUp[controller] = false;
  }

  /** 
   * @see org.newdawn.slick.InputListener#mouseWheelMoved(int)
   */
  public void mouseWheelMoved(int change) {
  }

  /**
   * @see org.newdawn.slick.InputListener#isAcceptingInput()
   */
  public boolean isAcceptingInput() {
    return true;
  }

  /**
   * @see org.newdawn.slick.InputListener#inputEnded()
   */
  public void inputEnded() {
  }

  /**
   * @see org.newdawn.slick.ControlledInputReciever#inputStarted()
   */
  public void inputStarted() {
  }
}
