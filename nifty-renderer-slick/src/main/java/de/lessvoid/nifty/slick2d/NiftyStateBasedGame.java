package de.lessvoid.nifty.slick2d;

import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * This is a implementation of Slicks {@link StateBasedGame} that is able to handle the Nifty game state implementations
 * in a better way.
 * <p/>
 * It provides the capability to use a single Nifty-GUI instance over all game states. This is the preferred mode to use
 * the Nifty-GUI in a application, yet not the default one.
 *
 * @author Martin Karing &gt;nitram@illarion.org&lt;
 */
public abstract class NiftyStateBasedGame extends StateBasedGame {
  /**
   * This value is used to set of the state based game is supposed to use only a single Nifty-GUI instance in all game
   * states.
   */
  private final boolean singleNiftyInstance;

  /**
   * This variable holds the reference to the global Nifty-GUI carrier in case one is used.
   */
  private NiftyCarrier globalCarrier;

  /**
   * Create a new state based game. This will set the game to use multiple Nifty-GUI instances. One for each game state.
   * Please use the constructor that allows changing this behavior in case you want to switch to the proposed mode of
   * using a single Nifty-GUI instance.
   *
   * @param name The name of the game
   */
  protected NiftyStateBasedGame(final String name) {
    this(name, false);
  }

  /**
   * Create a new state based game.
   * <p/>
   * This constructor allows to set if the game states should use the same instance of the Nifty-GUI or separated ones.
   * In matters of performance and memory usage, its highly recommend to use a single Nifty-GUI instance. How ever in
   * this case you have to ensure that the GUI of all game states use unique screen names.
   *
   * @param name The name of the game
   * @param singleNiftyGuiInstance This flag will, in case its set to {@code true}, apply the same instance of the
   * Nifty-GUI to all game states added to this state based game. In case its set to {@code false} every game state will
   * handle its own Nifty-GUI instance.
   */
  protected NiftyStateBasedGame(final String name, final boolean singleNiftyGuiInstance) {
    super(name);
    singleNiftyInstance = singleNiftyGuiInstance;
  }

  /**
   * Check if this state based game is set to use a single Nifty-GUI instance.
   *
   * @return {@code true} in case a single Nifty-GUI instance is used for all game states
   */
  public boolean isSingleNiftyInstance() {
    return singleNiftyInstance;
  }

  @Override
  @SuppressWarnings("ChainOfInstanceofChecks")
  public void addState(final GameState gameState) {
    if (gameState instanceof NiftyOverlayBasicGameState) {
      addState((NiftyOverlayBasicGameState) gameState);
    } else if (gameState instanceof NiftyOverlayGameState) {
      addState((NiftyOverlayGameState) gameState);
    } else {
      super.addState(gameState);
    }
  }

  /**
   * Special implementation of the {@link #addState(GameState)} function for {@link NiftyOverlayBasicGameState}. This
   * function has to be used in order to ensure proper exchange of the Nifty-GUI instance if enabled.
   *
   * @param gameState the game state that is supposed to be added
   */
  public void addState(final NiftyOverlayBasicGameState gameState) {
    setCarrier(gameState);
    super.addState(gameState);
  }

  /**
   * Special implementation of the {@link #addState(GameState)} function for {@link NiftyOverlayGameState}. This
   * function has to be used in order to ensure proper exchange of the Nifty-GUI instance if enabled.
   *
   * @param gameState the game state that is supposed to be added
   */
  public void addState(final NiftyOverlayGameState gameState) {
    setCarrier(gameState);
    super.addState(gameState);
  }

  /**
   * This function sets the carrier the game state is supposed to use in order to handle the GUI.
   *
   * @param carrierUser the instance that requires a carrier
   */
  private void setCarrier(final NiftyCarrierUser carrierUser) {
    if (singleNiftyInstance) {
      if (globalCarrier == null) {
        globalCarrier = new NiftyCarrier(true);
      }
      carrierUser.setCarrier(globalCarrier);
    } else {
      carrierUser.setCarrier(new NiftyCarrier(false));
    }
  }
}
