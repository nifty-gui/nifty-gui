package de.lessvoid.nifty.slick2d;

import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.input.ForwardingInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickSlickInputSystem;
import de.lessvoid.nifty.slick2d.render.SlickRenderDevice;
import de.lessvoid.nifty.slick2d.sound.SlickSoundDevice;
import de.lessvoid.nifty.slick2d.time.LWJGLTimeProvider;
import de.lessvoid.nifty.spi.time.TimeProvider;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class NiftyOverlayBasicGameState extends BasicGameState implements NiftyCarrierUser,
    NiftyInputForwarding,
    NiftyOrderControl {
  /**
   * The logger for this class.
   */
  private static final Logger LOGGER = Logger.getLogger(NiftyOverlayBasicGameState.class.getName());
  /**
   * The used input system.
   */
  private SlickInputSystem inSystem = null;

  /**
   * The carrier of the Nifty-GUI.
   */
  private NiftyCarrier niftyCarrier;

  /**
   * This variable is switched to {@code true} once the GUI is initialized for this game state.
   */
  private boolean guiPrepared;

  /**
   * This variable provides the control over the forwarding implementations.
   */
  private ForwardingInputSystem inputForwardingControl;

  /**
   * The render order that is used in this game.
   */
  private NiftyRenderOrder renderOrder;

  /**
   * The update order that is used in this game.
   */
  private NiftyUpdateOrder updateOrder;

  /**
   * Default constructor.
   */
  protected NiftyOverlayBasicGameState() {
    setRenderOrder(NiftyRenderOrder.NiftyOverlay);
    setUpdateOrder(NiftyUpdateOrder.NiftyLast);
  }

  @Override
  public final void setRenderOrder(final NiftyRenderOrder order) {
    renderOrder = order;
  }

  @Override
  public final void setUpdateOrder(final NiftyUpdateOrder order) {
    updateOrder = order;
  }

  /**
   * Enter the game state.
   */
  @Override
  public final void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
    final Input input = container.getInput();
    input.removeListener(inSystem);
    input.addListener(inSystem);

    if (niftyCarrier.isUsingRelayInputSystem()) {
      niftyCarrier.setInputSystem(inSystem);
    }

    enterState(container, game);
  }

  /**
   * Enter the game state. This function is called during the default {@link #enter(GameContainer, StateBasedGame)}
   * function call.
   *
   * @param container the container that displays the game
   * @param game the state based game this state is a part of
   * @throws SlickException in case entering the state fails
   */
  protected abstract void enterState(GameContainer container, StateBasedGame game) throws SlickException;

  @Override
  public final ForwardingInputSystem getInputForwardingControl() {
    return inputForwardingControl;
  }

  @Override
  public final NiftyRenderOrder getRenderOrder() {
    return renderOrder;
  }

  @Override
  public final NiftyUpdateOrder getUpdateOrder() {
    return updateOrder;
  }

  /**
   * Initialize the game and the GUI.
   */
  @Override
  public final void init(final GameContainer container, final StateBasedGame game) throws SlickException {
    if (niftyCarrier == null) {
      //noinspection HardCodedStringLiteral
      LOGGER.warning("Better use the Nifty-GameState implementations with the NiftyStateBasedGame.");
      niftyCarrier = new NiftyCarrier(false);
    }

    initGameAndGUI(container, game);

    if (!niftyCarrier.isInitialized()) {
      throw new IllegalStateException("NiftyGUI was not initialized.");
    }

    container.getInput().removeListener(game);
  }

  /**
   * Initialize the game. This function is called during {@link #init(GameContainer, StateBasedGame)}. During this call
   * its needed to initialize the Nifty GUI with own options by calling {@link #initNifty(GameContainer, StateBasedGame,
   * SlickRenderDevice, SlickSoundDevice, SlickInputSystem, TimeProvider)} .
   *
   * @param container the game container that displays the game
   * @param game the state based game this state is part of
   * @throws SlickException in case initializing the game goes wrong
   */
  protected abstract void initGameAndGUI(GameContainer container, StateBasedGame game) throws SlickException;

  @Override
  public final boolean isInputForwardingSupported() {
    return inputForwardingControl != null;
  }

  /**
   * Leave this game state.
   */
  @Override
  public final void leave(final GameContainer container, final StateBasedGame game) throws SlickException {
    final Input input = container.getInput();
    input.removeListener(inSystem);

    leaveState(container, game);
  }

  /**
   * Leave the game state. This function is called during the default {@link #leave(GameContainer, StateBasedGame)}
   * function call.
   *
   * @param container the container that displays the game
   * @param game the state based game this state is a part of
   * @throws SlickException in case entering the state fails
   */
  protected abstract void leaveState(GameContainer container, StateBasedGame game) throws SlickException;

  /**
   * Render the game.
   */
  @Override
  public final void render(
      final GameContainer container, final StateBasedGame game, final Graphics g) throws SlickException {
    if (niftyCarrier.isInitialized()) {
      switch (renderOrder) {
        case NiftyOverlay:
          renderGame(container, game, g);
          niftyCarrier.getNifty().render(false);
          break;
        case NiftyBackground:
          niftyCarrier.getNifty().render(true);
          renderGame(container, game, g);
          break;
      }
    } else {
      renderGame(container, game, g);
    }
  }

  /**
   * This function is supposed to be used to render the game. It is called during the call of the {@link
   * #render(GameContainer, StateBasedGame, Graphics)} function.
   *
   * @param container the container that displays the game
   * @param game the state based game this state is part of
   * @param g the graphics instance that is used to draw the game
   * @throws SlickException in case anything goes wrong during the rendering
   */
  protected abstract void renderGame(GameContainer container, StateBasedGame game, Graphics g) throws SlickException;

  /**
   * Update the game.
   */
  @Override
  public final void update(
      final GameContainer container, final StateBasedGame game, final int delta) throws SlickException {
    if (niftyCarrier.isInitialized()) {
      switch (updateOrder) {
        case NiftyLast:
          updateGame(container, game, delta);
          niftyCarrier.getNifty().update();
          break;
        case NiftyFirst:
          niftyCarrier.getNifty().update();
          updateGame(container, game, delta);
          break;
      }
    } else {
      updateGame(container, game, delta);
    }
  }

  /**
   * This function is supposed to be used to update the state of the game. It called during the call of the {@link
   * #update(GameContainer, StateBasedGame, int)} function.
   *
   * @param container the container that displays the game
   * @param game the state based game this state is part of
   * @param delta the time since the last update
   * @throws SlickException in case anything goes wrong during the update
   */
  protected abstract void updateGame(GameContainer container, StateBasedGame game, int delta) throws SlickException;

  @Override
  public void setCarrier(final NiftyCarrier carrier) {
    niftyCarrier = carrier;
  }

  /**
   * Initialize the Nifty GUI for this game. This function will use the default {@link TimeProvider}. Also it will use
   * the render and sound devices that are provided with this library. As for the input it will forward all input to the
   * Slick {@link InputListener} that is implemented in this class.
   *
   * @param container the container used to display the game
   * @param game the state based game this state is part of
   * @throws IllegalStateException in case this function was called before
   * @see SlickRenderDevice
   * @see SlickSoundDevice
   * @see SlickSlickInputSystem
   */
  protected final void initNifty(final GameContainer container, final StateBasedGame game) {
    initNifty(container, game, new SlickSlickInputSystem(this));
  }

  /**
   * Initialize the Nifty GUI for this game. This function will use the default {@link TimeProvider}. Also it will use
   * the render and sound devices that are provided with this library.
   *
   * @param container the container used to display the game
   * @param game the state based game this state is part of
   * @param inputSystem the input system that is supposed to be used
   * @throws IllegalStateException in case this function was called before
   * @see SlickRenderDevice
   * @see SlickSoundDevice
   */
  protected final void initNifty(
      final GameContainer container, final StateBasedGame game, final SlickInputSystem inputSystem) {
    initNifty(container, game, new SlickRenderDevice(container), new SlickSoundDevice(), inputSystem);
  }

  /**
   * Initialize the Nifty GUI for this game. This function will use the default {@link TimeProvider}.
   *
   * @param container the container used to display the game
   * @param game the state based game this state is part of
   * @param renderDevice the render device that is supposed to be used to render the GUI
   * @param soundDevice the sound device that is supposed to be used
   * @param inputSystem the input system that is supposed to be used
   * @throws IllegalStateException in case this function was called before
   */
  protected final void initNifty(
      final GameContainer container,
      final StateBasedGame game,
      final SlickRenderDevice renderDevice,
      final SlickSoundDevice soundDevice,
      final SlickInputSystem inputSystem) {
    initNifty(container, game, renderDevice, soundDevice, inputSystem, new LWJGLTimeProvider());
  }

  /**
   * Initialize the Nifty GUI for this game.
   *
   * @param container the container used to display the game
   * @param game the state based game this state is part of
   * @param renderDevice the render device that is supposed to be used to render the GUI
   * @param soundDevice the sound device that is supposed to be used
   * @param inputSystem the input system that is supposed to be used
   * @param timeProvider the time provider that is supposed to be used
   * @throws IllegalStateException in case this function was called before
   */
  protected final void initNifty(
      @SuppressWarnings("TypeMayBeWeakened") final GameContainer container,
      final StateBasedGame game,
      final SlickRenderDevice renderDevice,
      final SlickSoundDevice soundDevice,
      final SlickInputSystem inputSystem,
      final TimeProvider timeProvider) {
    if (guiPrepared) {
      throw new IllegalStateException("The NiftyGUI was already initialized. Its illegal to do so twice.");
    }
    guiPrepared = true;

    inputSystem.setInput(container.getInput());

    if (niftyCarrier.isInitialized()) {
      if (!niftyCarrier.isUsingRelayInputSystem()) {
        throw new IllegalStateException("Detected carrier that was already initialized without relay.");
      }
    } else {
      niftyCarrier.initNifty(renderDevice, soundDevice, inputSystem, timeProvider);
    }

    if (inputSystem instanceof ForwardingInputSystem) {
      inputForwardingControl = (ForwardingInputSystem) inputSystem;
    }

    inSystem = inputSystem;

    prepareNifty(niftyCarrier.getNifty(), game);
  }

  /**
   * This function should be used to prepare the actual GUI and the controllers of the Nifty GUI. It is called right
   * after the Nifty GUI got initialized.
   *
   * @param nifty the Nifty GUI that got initialized
   * @param game the state based game this state is part of
   */
  protected abstract void prepareNifty(Nifty nifty, StateBasedGame game);

  /**
   * Get the instance of the NiftyGUI that is used to render this screen.
   *
   * @return the instance of the NiftyGUI
   */
  public final Nifty getNifty() {
    return niftyCarrier.getNifty();
  }
}
