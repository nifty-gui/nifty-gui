package de.lessvoid.nifty.slick2d;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * This "game" does nothing but showing the Nifty GUI on the screen. For real games the Nifty Overlay Game should be
 * used.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class NiftyGame extends NiftyOverlayGame {
  /**
   * The screen that is called when preparing the GUI.
   */
  private final String startScreen;

  /**
   * The title of the game.
   */
  private final String title;

  /**
   * Create a new game that displays the Nifty GUI and set the title that is shown.
   *
   * @param gameTitle the title of the game
   */
  protected NiftyGame(final String gameTitle) {
    this(gameTitle, "start");
  }

  /**
   * Create a new game that displays the Nifty GUI and set the title and the start screen for this game.
   *
   * @param gameTitle the title of the game
   * @param niftyStartScreen the name of the screen that should be called first
   */
  protected NiftyGame(final String gameTitle, final String niftyStartScreen) {
    title = gameTitle;
    startScreen = niftyStartScreen;
  }

  /**
   * Was a close requested?
   */
  @Override
  public boolean closeRequested() {
    return false;
  }

  /**
   * Get the title of the game.
   */
  @Override
  public final String getTitle() {
    return title;
  }

  /**
   * When initializing the game its only needed to prepare the GUI for display.
   */
  @Override
  protected final void initGameAndGUI(final GameContainer container) {
    initNifty(container);

    if (startScreen != null) {
      getNifty().gotoScreen(startScreen);
    }
  }

  /**
   * Rendering the GUI only requires that the display is cleared before rendering the screen.
   */
  @Override
  protected final void renderGame(final GameContainer container, final Graphics g) {
    g.clear();
  }

  /**
   * Updating the game is not needed in this implementation as only the GUI is displayed.
   */
  @Override
  protected final void updateGame(final GameContainer container, final int delta) {
    // nothing to do
  }
}
