package de.lessvoid.nifty.slick2d;

import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public abstract class NiftyBasicGameState extends NiftyOverlayBasicGameState {
  /**
   * The screen that is called upon entering the game state.
   */
  private final String startScreen;

  /**
   * Create this game state.
   */
  protected NiftyBasicGameState() {
    this("start");
  }

  /**
   * Create this game state and set the screen that is called upon entering this state.
   *
   * @param niftyStartScreen the name of the screen Nifty is supposed to goto once the game state is entered
   */
  protected NiftyBasicGameState(final String niftyStartScreen) {
    startScreen = niftyStartScreen;
  }

  /**
   * Enter this game state.
   */
  @SuppressWarnings("PublicMethodNotExposedInInterface")
  @Override
  public void enterState(final GameContainer container, final StateBasedGame game) throws SlickException {
    if (startScreen != null) {
      getNifty().gotoScreen(startScreen);
    }
  }

  /**
   * When initializing the game its only needed to prepare the GUI for display.
   */
  @Override
  protected final void initGameAndGUI(final GameContainer container, final StateBasedGame game) {
    initNifty(container, game, new PlainSlickInputSystem());
    getNifty().gotoScreen(startScreen);
  }

  /**
   * Leave this game state.
   */
  @SuppressWarnings("PublicMethodNotExposedInInterface")
  @Override
  public void leaveState(final GameContainer container, final StateBasedGame game) throws SlickException {
    // nothing
  }

  /**
   * Rendering the GUI only requires that the display is cleared before rendering the screen.
   */
  @Override
  protected void renderGame(
      final GameContainer container, final StateBasedGame game, final Graphics g) throws SlickException {
    g.clear();
  }

  /**
   * Updating the game is not needed in this implementation as only the GUI is displayed.
   */
  @Override
  protected final void updateGame(final GameContainer container, final StateBasedGame game, final int delta) {
    // nothing to do
  }
}
