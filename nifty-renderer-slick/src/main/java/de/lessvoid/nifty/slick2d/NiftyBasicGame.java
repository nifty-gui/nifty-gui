package de.lessvoid.nifty.slick2d;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * This "game" implements all the features of a Slick BasicGame with the sole purpose of displaying a NiftyGUI on top of
 * it.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class NiftyBasicGame extends NiftyOverlayBasicGame {
  /**
   * The screen that is called when preparing the GUI.
   */
  private final String startScreen;

  /**
   * Create a new game that displays the Nifty GUI and set the title that is shown.
   *
   * @param gameTitle the title of the game
   */
  protected NiftyBasicGame(final String gameTitle) {
    this(gameTitle, "start");
  }

  /**
   * Create a new game that displays the Nifty GUI and set the title and the start screen for this game.
   *
   * @param gameTitle the title of the game
   * @param niftyStartScreen the name of the screen that should be called first
   */
  protected NiftyBasicGame(final String gameTitle, final String niftyStartScreen) {
    super(gameTitle);
    startScreen = niftyStartScreen;
  }

  /**
   * When initializing the game its only needed to prepare the GUI for display.
   */
  @Override
  protected void initGameAndGUI(final GameContainer container) {
    initNifty(container);
    getNifty().gotoScreen(startScreen);
  }

  /**
   * Rendering the GUI only requires that the display is cleared before rendering the screen.
   */
  @Override
  protected void renderGame(final GameContainer container, final Graphics g) throws SlickException {
    g.clear();
  }

  /**
   * Updating the game is not needed in this implementation as only the GUI is displayed.
   */
  @Override
  protected void updateGame(final GameContainer container, final int delta) throws SlickException {
    // nothing to do
  }
}
