package de.lessvoid.nifty.slick2d;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This is the game state implementation that supports the Nifty-GUI. Its used for game states that display only the
 * Nifty-GUI and nothing other then that.
 *
 * @author Martin Karing &gt;nitram@illarion.org&lt;
 */
public abstract class NiftyGameState extends NiftyOverlayGameState {
  /**
   * The screen that is called upon entering the game state.
   */
  @Nullable
  private final String startScreen;

  /**
   * Create this game state.
   */
  protected NiftyGameState() {
    this("start");
  }

  /**
   * Create this game state and set the screen that is called upon entering this state.
   *
   * @param niftyStartScreen the name of the screen Nifty is supposed to goto once the game state is entered
   */
  protected NiftyGameState(@Nullable final String niftyStartScreen) {
    startScreen = niftyStartScreen;
  }

  /**
   * Enter this game state.
   */
  @Override
  public void enterState(final GameContainer container, final StateBasedGame game) {
    if (startScreen != null) {
      Nifty nifty = getNifty();
      if (nifty == null) {
        throw new IllegalStateException("Nifty is not initialized, but it should be.");
      }
      nifty.gotoScreen(startScreen);
    }
  }

  /**
   * When initializing the game its only needed to prepare the GUI for display.
   */
  @Override
  protected final void initGameAndGUI(@Nonnull final GameContainer container, @Nonnull final StateBasedGame game) {
    initNifty(container, game, new PlainSlickInputSystem());
    if (startScreen != null) {
      Nifty nifty = getNifty();
      if (nifty == null) {
        throw new IllegalStateException("Nifty is not initialized, but it should be.");
      }
      nifty.gotoScreen(startScreen);
    }
  }

  /**
   * Leave this game state.
   */
  @Override
  public void leaveState(@Nonnull final GameContainer container, @Nonnull final StateBasedGame game) {
    // nothing
  }

  /**
   * Rendering the GUI only requires that the display is cleared before rendering the screen.
   */
  @Override
  protected final void renderGame(
      @Nonnull final GameContainer container,
      @Nonnull final StateBasedGame game,
      @Nonnull final Graphics g) {
    g.clear();
  }

  /**
   * Updating the game is not needed in this implementation as only the GUI is displayed.
   */
  @Override
  protected final void updateGame(
      @Nonnull final GameContainer container,
      @Nonnull final StateBasedGame game,
      final int delta) {
    // nothing to do
  }
}
