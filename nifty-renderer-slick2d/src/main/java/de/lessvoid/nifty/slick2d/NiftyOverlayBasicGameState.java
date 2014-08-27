package de.lessvoid.nifty.slick2d;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.render.batch.BatchRenderDevice;
import de.lessvoid.nifty.slick2d.input.ForwardingInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickSlickInputSystem;
import de.lessvoid.nifty.slick2d.render.batch.SlickBatchRenderBackendFactory;
import de.lessvoid.nifty.slick2d.sound.SlickSoundDevice;
import de.lessvoid.nifty.slick2d.time.LWJGLTimeProvider;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.time.TimeProvider;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

public abstract class NiftyOverlayBasicGameState extends BasicGameState implements NiftyCarrierUser,
    NiftyInputForwarding,
    NiftyOrderControl {
  /**
   * The logger for this class.
   */
  @Nonnull
  private static final Logger LOGGER = Logger.getLogger(NiftyOverlayBasicGameState.class.getName());
  /**
   * The used input system.
   */
  @Nullable
  private SlickInputSystem inSystem = null;

  /**
   * The carrier of the Nifty-GUI.
   */
  @Nullable
  private NiftyCarrier niftyCarrier;

  /**
   * This variable is switched to {@code true} once the GUI is initialized for this game state.
   */
  private boolean guiPrepared;

  /**
   * This variable provides the control over the forwarding implementations.
   */
  @Nullable
  private ForwardingInputSystem inputForwardingControl;

  /**
   * The render order that is used in this game.
   */
  @Nonnull
  private NiftyRenderOrder renderOrder;

  /**
   * The update order that is used in this game.
   */
  @Nonnull
  private NiftyUpdateOrder updateOrder;

  /**
   * The last reported screen height.
   */
  private int lastHeight;

  /**
   * The last reported screen width.
   */
  private int lastWidth;

  /**
   * Default constructor.
   */
  protected NiftyOverlayBasicGameState() {
    setRenderOrder(NiftyRenderOrder.NiftyOverlay);
    setUpdateOrder(NiftyUpdateOrder.NiftyLast);
  }

  @Override
  public final void setRenderOrder(@Nonnull final NiftyRenderOrder order) {
    renderOrder = order;
  }

  @Override
  public final void setUpdateOrder(@Nonnull final NiftyUpdateOrder order) {
    updateOrder = order;
  }

  /**
   * Enter the game state.
   */
  @Override
  public final void enter(@Nonnull final GameContainer container, @Nonnull final StateBasedGame game)
      throws SlickException {
    final Input input = container.getInput();
    input.removeListener(inSystem);
    input.addListener(inSystem);

    if (niftyCarrier != null && niftyCarrier.isUsingRelayInputSystem()) {
      niftyCarrier.setInputSystem(inSystem);
    }

    enterState(container, game);
  }

  /**
   * Enter the game state. This function is called during the default {@link #enter(GameContainer, StateBasedGame)}
   * function call.
   *
   * @param container the container that displays the game
   * @param game      the state based game this state is a part of
   */
  protected abstract void enterState(@Nonnull GameContainer container, @Nonnull StateBasedGame game);

  @Nullable
  @Override
  public final ForwardingInputSystem getInputForwardingControl() {
    return inputForwardingControl;
  }

  @Nonnull
  @Override
  public final NiftyRenderOrder getRenderOrder() {
    return renderOrder;
  }

  @Nonnull
  @Override
  public final NiftyUpdateOrder getUpdateOrder() {
    return updateOrder;
  }

  /**
   * Initialize the game and the GUI.
   */
  @Override
  public final void init(@Nonnull final GameContainer container, @Nonnull final StateBasedGame game)
      throws SlickException {
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
   * RenderDevice, SoundDevice, SlickInputSystem, TimeProvider)} .
   *
   * @param container the game container that displays the game
   * @param game      the state based game this state is part of
   */
  protected abstract void initGameAndGUI(@Nonnull GameContainer container, @Nonnull StateBasedGame game);

  @Override
  public final boolean isInputForwardingSupported() {
    return inputForwardingControl != null;
  }

  /**
   * Leave this game state.
   */
  @Override
  public final void leave(@Nonnull final GameContainer container, @Nonnull final StateBasedGame game)
      throws SlickException {
    final Input input = container.getInput();
    input.removeListener(inSystem);

    leaveState(container, game);
  }

  /**
   * Leave the game state. This function is called during the default {@link #leave(GameContainer, StateBasedGame)}
   * function call.
   *
   * @param container the container that displays the game
   * @param game      the state based game this state is a part of
   */
  protected abstract void leaveState(@Nonnull GameContainer container, @Nonnull StateBasedGame game);

  /**
   * Render the game.
   */
  @Override
  public final void render(
      @Nonnull final GameContainer container,
      @Nonnull final StateBasedGame game,
      @Nonnull final Graphics g) throws SlickException {
    if (niftyCarrier != null && niftyCarrier.isInitialized()) {
      Nifty nifty = niftyCarrier.getNifty();
      assert nifty != null; // checked by isInitialized
      switch (renderOrder) {
        case NiftyOverlay:
          renderGame(container, game, g);
          nifty.render(false);
          break;
        case NiftyBackground:
          nifty.render(true);
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
   * @param game      the state based game this state is part of
   * @param g         the graphics instance that is used to draw the game
   */
  protected abstract void renderGame(
      @Nonnull GameContainer container,
      @Nonnull StateBasedGame game,
      @Nonnull Graphics g);

  /**
   * Update the game.
   */
  @Override
  public final void update(
      @Nonnull final GameContainer container,
      @Nonnull final StateBasedGame game,
      final int delta) throws SlickException {
    if (niftyCarrier != null && niftyCarrier.isInitialized()) {
      Nifty nifty = niftyCarrier.getNifty();
      assert nifty != null; // checked by isInitialized()
      final int currentHeight = container.getHeight();
      final int currentWidth = container.getWidth();
      if ((currentHeight != lastHeight) || (currentWidth != lastWidth)) {
        lastHeight = currentHeight;
        lastWidth = currentWidth;
        nifty.resolutionChanged();
      }
      switch (updateOrder) {
        case NiftyLast:
          updateGame(container, game, delta);
          nifty.update();
          break;
        case NiftyFirst:
          nifty.update();
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
   * @param game      the state based game this state is part of
   * @param delta     the time since the last update
   */
  protected abstract void updateGame(@Nonnull GameContainer container, @Nonnull StateBasedGame game, int delta);

  @Override
  public void setCarrier(@Nonnull final NiftyCarrier carrier) {
    niftyCarrier = carrier;
  }

  /**
   * Initialize the Nifty GUI for this game. This function will use the default {@link TimeProvider}. Also it will use
   * the render and sound devices that are provided with this library. As for the input it will forward all input to the
   * Slick {@link InputListener} that is implemented in this class.
   *
   * @param container the container used to display the game
   * @param game      the state based game this state is part of
   * @throws IllegalStateException in case this function was called before
   * @see SlickSoundDevice
   * @see SlickSlickInputSystem
   */
  protected final void initNifty(@Nonnull final GameContainer container, @Nonnull final StateBasedGame game) {
    initNifty(container, game, new SlickSlickInputSystem(this));
  }

  /**
   * Initialize the Nifty GUI for this game. This function will use the default {@link TimeProvider}. Also it will use
   * the render and sound devices that are provided with this library.
   *
   * @param container   the container used to display the game
   * @param game        the state based game this state is part of
   * @param inputSystem the input system that is supposed to be used
   * @throws IllegalStateException in case this function was called before
   * @see SlickSoundDevice
   */
  protected final void initNifty(
      @Nonnull final GameContainer container,
      @Nonnull final StateBasedGame game,
      @Nonnull final SlickInputSystem inputSystem) {
    initNifty(container, game, new BatchRenderDevice(SlickBatchRenderBackendFactory.create()), new SlickSoundDevice(), inputSystem);
  }

  /**
   * Initialize the Nifty GUI for this game. This function will use the default {@link TimeProvider}.
   *
   * @param container    the container used to display the game
   * @param game         the state based game this state is part of
   * @param renderDevice the render device that is supposed to be used to render the GUI
   * @param soundDevice  the sound device that is supposed to be used
   * @param inputSystem  the input system that is supposed to be used
   * @throws IllegalStateException in case this function was called before
   */
  protected final void initNifty(
      @Nonnull final GameContainer container,
      @Nonnull final StateBasedGame game,
      @Nonnull final RenderDevice renderDevice,
      @Nonnull final SoundDevice soundDevice,
      @Nonnull final SlickInputSystem inputSystem) {
    initNifty(container, game, renderDevice, soundDevice, inputSystem, new LWJGLTimeProvider());
  }

  /**
   * Initialize the Nifty GUI for this game.
   *
   * @param container    the container used to display the game
   * @param game         the state based game this state is part of
   * @param renderDevice the render device that is supposed to be used to render the GUI
   * @param soundDevice  the sound device that is supposed to be used
   * @param inputSystem  the input system that is supposed to be used
   * @param timeProvider the time provider that is supposed to be used
   * @throws IllegalStateException in case this function was called before
   */
  protected final void initNifty(
      @Nonnull @SuppressWarnings("TypeMayBeWeakened") final GameContainer container,
      @Nonnull final StateBasedGame game,
      @Nonnull final RenderDevice renderDevice,
      @Nonnull final SoundDevice soundDevice,
      @Nonnull final SlickInputSystem inputSystem,
      @Nonnull final TimeProvider timeProvider) {
    if (guiPrepared) {
      throw new IllegalStateException("The NiftyGUI was already initialized. Its illegal to do so twice.");
    }
    guiPrepared = true;

    inputSystem.setInput(container.getInput());

    if (niftyCarrier != null) {
      if (niftyCarrier.isInitialized()) {
        if (!niftyCarrier.isUsingRelayInputSystem()) {
          throw new IllegalStateException("Detected carrier that was already initialized without relay.");
        }
      } else {
        niftyCarrier.initNifty(renderDevice, soundDevice, inputSystem, timeProvider);
      }
    } else {
      throw new IllegalStateException("Nifty carrier is not set yet.");
    }

    if (inputSystem instanceof ForwardingInputSystem) {
      inputForwardingControl = (ForwardingInputSystem) inputSystem;
    }

    inSystem = inputSystem;

    Nifty nifty = niftyCarrier.getNifty();
    if (nifty == null) {
      throw new IllegalStateException("Nifty is not properly initialized.");
    }
    prepareNifty(nifty, game);
  }

  /**
   * This function should be used to prepare the actual GUI and the controllers of the Nifty GUI. It is called right
   * after the Nifty GUI got initialized.
   *
   * @param nifty the Nifty GUI that got initialized
   * @param game  the state based game this state is part of
   */
  protected abstract void prepareNifty(@Nonnull Nifty nifty, @Nonnull StateBasedGame game);

  /**
   * Get the instance of the NiftyGUI that is used to render this screen.
   *
   * @return the instance of the NiftyGUI
   */
  @Nullable
  public final Nifty getNifty() {
    if (niftyCarrier == null) {
      return null;
    }
    return niftyCarrier.getNifty();
  }
}
