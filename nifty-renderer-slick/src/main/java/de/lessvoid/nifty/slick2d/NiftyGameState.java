package de.lessvoid.nifty.slick2d;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.slick2d.input.LwjglKeyboardInputEventCreator;
import de.lessvoid.nifty.slick2d.render.SlickRenderDevice;
import de.lessvoid.nifty.slick2d.sound.SlickSoundDevice;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * A Slick Nifty GameState.
 * @author void
 */
public class NiftyGameState extends BasicGameState {

  /**
   * nifty instance to use.
   */
  protected Nifty nifty;

  /**
   * the slick game state id.
   */
  protected int id;

  /**
   * mouse x.
   */
  protected int mouseX;

  /**
   * mouse y.
   */
  protected int mouseY;

  /**
   * mouse offset x.
   */
  protected int cursorOffsetX;
  
  /**
   * mouse offset y.
   */
  protected int cursorOffsetY;

  /**
   * mouse down.
   */
  protected boolean mouseDown;

  /**
   * optional mouseImage.
   */
  private Image mouseImage;

  private List < MouseEvent > mouseEvents = new ArrayList < MouseEvent >();
  private List < KeyboardInputEvent > keyEvents = new ArrayList < KeyboardInputEvent >();

  private GameContainer container;
  private LwjglKeyboardInputEventCreator inputEventCreator = new LwjglKeyboardInputEventCreator(); 

  /**
   * create the nifty game state.
   * @param slickGameStateId the slick gamestate id for this state
   */
  public NiftyGameState(final int slickGameStateId) {
    this.id = slickGameStateId;

    SlickCallable.enterSafeBlock();
    this.nifty = new Nifty(
        new SlickRenderDevice(container),
        new SlickSoundDevice(),
        new InputSystem() {
          public void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
            for (MouseEvent event : mouseEvents) {
              event.processMouseEvents(inputEventConsumer);
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
        },
        new TimeProvider());
    SlickCallable.leaveSafeBlock();
  }

  /**
   * load xml.
   * @param filename file to load
   * @param controllers controllers to use
   */
  public void fromXml(final String filename, final ScreenController ... controllers) {
    SlickCallable.enterSafeBlock();
    nifty.registerScreenController(controllers);
    nifty.fromXmlWithoutStartScreen(filename);
    SlickCallable.leaveSafeBlock();
  }

  /**
   * load xml.
   * @param filename file to load
   * @param controllers controllers to use
   */
  public void fromXml(final String fileId, final InputStream xmlData, final ScreenController ... controllers) {
    SlickCallable.enterSafeBlock();
    nifty.registerScreenController(controllers);
    nifty.fromXmlWithoutStartScreen(fileId, xmlData);
    SlickCallable.leaveSafeBlock();
  }
  
  /**
   * Enable overlay mouse cursor image.
   * @param newMouseImage image
   */
  public void enableMouseImage(final Image newMouseImage) {
    mouseImage = newMouseImage;
    cursorOffsetX = mouseImage.getWidth() / 2;
    cursorOffsetY = mouseImage.getHeight() / 2;
  }

  /**
   * Enable overlay mouse cursor image.
   * @param newMouseImage image
   * @param cursorOffsetX Offset of the cursor position
   * @param cursorOffsetY Offset of the cursor position
   */
  public void enableMouseImage(final Image newMouseImage, int cursorOffsetX, int cursorOffsetY) {
    mouseImage = newMouseImage;
    this.cursorOffsetX = cursorOffsetX;
    this.cursorOffsetY = cursorOffsetY;
  }

  /**
   * get slick game state id.
   * @return slick game state id
   */
  public int getID() {
    return id;
  }

  /**
   * initialize.
   * @param container GameContainer
   * @param game StateBasedGame
   * @throws SlickException exception
   */
  public void init(final GameContainer container, final StateBasedGame game) throws SlickException {
    this.container = container;
  }

  /**
   * render.
   * @param container GameContainer
   * @param game StateBasedGame
   * @param g Graphics
   * @throws SlickException exception
   */
  public void render(final GameContainer container, final StateBasedGame game, final Graphics g) throws SlickException {
    SlickCallable.enterSafeBlock();
    nifty.render(false);
    SlickCallable.leaveSafeBlock();

    if (mouseImage != null) {
      g.drawImage(mouseImage, mouseX - cursorOffsetX, mouseY - cursorOffsetX);
    }
  }

  /**
   * update.
   * @param container GameContainer
   * @param game StateBasedGame
   * @param d delta thing
   * @throws SlickException exception
   */
  public void update(final GameContainer container, final StateBasedGame game, final int d) throws SlickException {
    nifty.update();
  }

  /**
   * @see org.newdawn.slick.InputListener#keyPressed(int, char)
   */
  public void keyPressed(final int key, final char c) {
    keyEvents.add(inputEventCreator.createEvent(key, c, true));
  }

  /**
   * @see org.newdawn.slick.InputListener#keyReleased(int, char)
   */
  public void keyReleased(final int key, final char c) {
    keyEvents.add(inputEventCreator.createEvent(key, c, false));
  }

  /**
   * @see org.newdawn.slick.InputListener#mouseMoved(int, int, int, int)
   */
  public void mouseMoved(final int oldx, final int oldy, final int newx, final int newy) {
    mouseX = newx;
    mouseY = newy;
    forwardMouseEventToNifty(mouseX, mouseY, mouseDown);
  }

  /**
   * @see org.newdawn.slick.InputListener#mousePressed(int, int, int)
   */
  public void mousePressed(final int button, final int x, final int y) {
    mouseX = x;
    mouseY = y;
    mouseDown = true;
    forwardMouseEventToNifty(mouseX, mouseY, mouseDown);
  }

  /**
   * @see org.newdawn.slick.InputListener#mouseReleased(int, int, int)
   */
  public void mouseReleased(final int button, final int x, final int y) {
    mouseX = x;
    mouseY = y;
    mouseDown = false;
    forwardMouseEventToNifty(mouseX, mouseY, mouseDown);
  }

  /**
   * enter state.
   * @param container container
   * @param game game
   */
  public void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
    SlickCallable.enterSafeBlock();
    if (nifty.getCurrentScreen().isNull()) {
      nifty.gotoScreen("start");
    } else {
      nifty.getCurrentScreen().startScreen();
    }
    mouseDown = false;
    SlickCallable.leaveSafeBlock();
  }

  /**
   * Activate the given ScreenId.
   * @param screenId
   */
  public void gotoScreen(final String screenId) {
    nifty.gotoScreen(screenId);
  }

  /**
   * 
   * @param mouseX
   * @param mouseY
   * @param mouseDown
   */
  private void forwardMouseEventToNifty(final int mouseX, final int mouseY, final boolean mouseDown) {
    // FIXME: add support for more mouse buttons (this assumes left mouse button click currently)
    mouseEvents.add(new MouseEvent(mouseX, container.getHeight() - mouseY, mouseDown, 0));
  }

  public Nifty getNifty() {
    return nifty;
  }
}
