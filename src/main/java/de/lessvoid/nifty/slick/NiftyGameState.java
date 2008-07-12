package de.lessvoid.nifty.slick;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.render.spi.lwjgl.RenderDeviceLwjgl;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.slick.SlickSoundLoader;
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
   * mouse down.
   */
  protected boolean mouseDown;

  /**
   * optional mouseImage.
   */
  private Image mouseImage;

  /**
   * create the nifty game state.
   * @param slickGameStateId the slick gamestate id for this state
   */
  public NiftyGameState(final int slickGameStateId) {
    this.id = slickGameStateId;

    SlickCallable.enterSafeBlock();
    this.nifty = new Nifty(
        new RenderDeviceLwjgl(),
        new SoundSystem(new SlickSoundLoader()),
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
    nifty.fromXml(filename, "start");
    SlickCallable.leaveSafeBlock();
  }

  /**
   * Enable overlay mouse cursor image.
   * @param newMouseImage image
   */
  public void enableMouseImage(final Image newMouseImage) {
    mouseImage = newMouseImage;
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
    nifty.render(false, mouseX, mouseY, mouseDown);
    SlickCallable.leaveSafeBlock();

    if (mouseImage != null) {
      g.drawImage(mouseImage, mouseX - mouseImage.getWidth() / 2, mouseY - mouseImage.getHeight() / 2);
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
  }

  /**
   * @see org.newdawn.slick.InputListener#keyPressed(int, char)
   */
  public void keyPressed(final int key, final char c) {
    nifty.keyEvent(key, c, true);
  }

  /**
   * @see org.newdawn.slick.InputListener#keyReleased(int, char)
   */
  public void keyReleased(final int key, final char c) {
    nifty.keyEvent(key, c, false);
  }

  /**
   * @see org.newdawn.slick.InputListener#mouseMoved(int, int, int, int)
   */
  public void mouseMoved(final int oldx, final int oldy, final int newx, final int newy) {
    mouseX = newx;
    mouseY = newy;
  }

  /**
   * @see org.newdawn.slick.InputListener#mousePressed(int, int, int)
   */
  public void mousePressed(final int button, final int x, final int y) {
    mouseX = x;
    mouseY = y;
    mouseDown = true;
  }

  /**
   * @see org.newdawn.slick.InputListener#mouseReleased(int, int, int)
   */
  public void mouseReleased(final int button, final int x, final int y) {
    mouseX = x;
    mouseY = y;
    mouseDown = false;
  }

  /**
   * enter state.
   * @param container container
   * @param game game
   */
  public void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
    SlickCallable.enterSafeBlock();
    nifty.getCurrentScreen().startScreen();
    mouseDown = false;
    SlickCallable.leaveSafeBlock();
  }
}
