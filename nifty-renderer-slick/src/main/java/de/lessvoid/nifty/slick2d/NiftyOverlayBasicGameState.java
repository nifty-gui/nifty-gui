package de.lessvoid.nifty.slick2d;

import org.lwjgl.util.Timer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.input.SlickInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickSlickInputSystem;
import de.lessvoid.nifty.slick2d.render.SlickRenderDevice;
import de.lessvoid.nifty.slick2d.sound.SlickSoundDevice;
import de.lessvoid.nifty.slick2d.time.LWJGLTimeProvider;
import de.lessvoid.nifty.spi.time.TimeProvider;

public abstract class NiftyOverlayBasicGameState extends BasicGameState {
  /**
   * The used input system.
   */
  private SlickInputSystem inputSystem;

  /**
   * The one and only Nifty GUI.
   */
  private Nifty niftyGUI;

  /**
   * Enter the game state.
   */
  @Override
  public final void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
    final Input input = container.getInput();
    input.removeListener(inputSystem);
    input.addListener(inputSystem);

    enterState(container, game);
  }

  /**
   * Enter the game state. This function is called during the default
   * {@link #enter(GameContainer, StateBasedGame)} function call.
   * 
   * @param container
   *          the container that displays the game
   * @param game
   *          the state based game this state is a part of
   * @throws SlickException
   *           in case entering the state fails
   */
  protected abstract void enterState(final GameContainer container, final StateBasedGame game) throws SlickException;

  /**
   * Get the instance of the NiftyGUI that is used to render this screen.
   * 
   * @return the instance of the NiftyGUI
   */
  public final Nifty getNifty() {
    return niftyGUI;
  }

  /**
   * Initialize the game and the GUI.
   */
  @Override
  public final void init(final GameContainer container, final StateBasedGame game) throws SlickException {
    initGameAndGUI(container, game);

    if (niftyGUI == null) {
      throw new IllegalStateException("NiftyGUI was not initialized.");
    }

    container.getInput().removeListener(game);
  }

  /**
   * Initialize the game. This function is called during
   * {@link #init(GameContainer)}. During this call its needed to initialize the
   * Nifty GUI with own options by calling
   * {@link #initNifty(GameContainer, SlickRenderDevice, SlickSoundDevice, TimeProvider)}
   * .
   * 
   * @param container
   *          the game container that displays the game
   * @throws SlickException
   *           in case initializing the game goes wrong
   */
  protected abstract void initGameAndGUI(final GameContainer container, StateBasedGame game) throws SlickException;

  /**
   * Initialize the Nifty GUI for this game.
   * 
   * @param container
   *          the container used to display the game
   * @param game
   *          the state based game this state is part of
   * @param renderDevice
   *          the render device that is supposed to be used to render the GUI
   * @param soundDevice
   *          the sound device that is supposed to be used
   * @param inputSystem
   *          the input system that is supposed to be used
   * @param timeProvider
   *          the time provider that is supposed to be used
   * @throws IllegalStateException
   *           in case this function was called before
   */
  protected final void initNifty(
      final GameContainer container,
      final StateBasedGame game,
      final SlickRenderDevice renderDevice,
      final SlickSoundDevice soundDevice,
      final SlickInputSystem inputSystem,
      final TimeProvider timeProvider) {
    if (niftyGUI != null) {
      throw new IllegalStateException("The NiftyGUI was already initialized. Its illegal to do so twice.");
    }

    inputSystem.setInput(container.getInput());

    niftyGUI = new Nifty(renderDevice, soundDevice, inputSystem, timeProvider);

    this.inputSystem = inputSystem;

    prepareNifty(niftyGUI, game);
  }

  /**
   * Initialize the Nifty GUI for this game. This function will use the default
   * {@link de.lessvoid.nifty.tools.TimeProvider}.
   * 
   * @param container
   *          the container used to display the game
   * @param game
   *          the state based game this state is part of
   * @param renderDevice
   *          the render device that is supposed to be used to render the GUI
   * @param soundDevice
   *          the sound device that is supposed to be used
   * @param inputSystem
   *          the input system that is supposed to be used
   * @throws IllegalStateException
   *           in case this function was called before
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
   * Initialize the Nifty GUI for this game. This function will use the default
   * {@link de.lessvoid.nifty.tools.TimeProvider}. Also it will use the render
   * and sound devices that are provided with this library.
   * 
   * @param container
   *          the container used to display the game
   * @param game
   *          the state based game this state is part of
   * @param inputSystem
   *          the input system that is supposed to be used
   * @throws IllegalStateException
   *           in case this function was called before
   * @see de.lessvoid.nifty.slick2d.render.SlickRenderDevice
   * @see de.lessvoid.nifty.slick2d.sound.SlickSoundDevice
   */
  protected final void initNifty(
      final GameContainer container,
      final StateBasedGame game,
      final SlickInputSystem inputSystem) {
    initNifty(container, game, new SlickRenderDevice(container), new SlickSoundDevice(), inputSystem);
  }

  /**
   * Initialize the Nifty GUI for this game. This function will use the default
   * {@link de.lessvoid.nifty.tools.TimeProvider}. Also it will use the render
   * and sound devices that are provided with this library. As for the input it
   * will forward all input to the Slick {@link org.newdawn.slick.InputListener}
   * that is implemented in this class.
   * 
   * @param container
   *          the container used to display the game
   * @param game
   *          the state based game this state is part of
   * @throws IllegalStateException
   *           in case this function was called before
   * @see de.lessvoid.nifty.slick2d.render.SlickRenderDevice
   * @see de.lessvoid.nifty.slick2d.sound.SlickSoundDevice
   * @see de.lessvoid.nifty.slick2d.input.SlickSlickInputSystem
   */
  protected final void initNifty(final GameContainer container, final StateBasedGame game) {
    initNifty(container, game, new SlickSlickInputSystem(this));
  }

  /**
   * Leave this game state.
   */
  @Override
  public final void leave(final GameContainer container, final StateBasedGame game) throws SlickException {
    final Input input = container.getInput();
    input.removeListener(inputSystem);

    leaveState(container, game);
  }

  /**
   * Leave the game state. This function is called during the default
   * {@link #leave(GameContainer, StateBasedGame)} function call.
   * 
   * @param container
   *          the container that displays the game
   * @param game
   *          the state based game this state is a part of
   * @throws SlickException
   *           in case entering the state fails
   */
  protected abstract void leaveState(final GameContainer container, final StateBasedGame game) throws SlickException;

  /**
   * This function should be used to prepare the actual GUI and the controllers
   * of the Nifty GUI. It is called right after the Nifty GUI got initialized.
   * 
   * @param nifty
   *          the Nifty GUI that got initialized
   * @param game
   *          the state based game this state is part of
   */
  protected abstract void prepareNifty(Nifty nifty, StateBasedGame game);

  /**
   * Render the game.
   */
  @Override
  public final void render(final GameContainer container, final StateBasedGame game, final Graphics g)
      throws SlickException {
    renderGame(container, game, g);

    if (niftyGUI != null) {
      niftyGUI.render(false);
    }
  }

  /**
   * This function is supposed to be used to render the game. It is called
   * during the call of the {@link #render(GameContainer, Graphics)} function.
   * 
   * @param container
   *          the container that displays the game
   * @param g
   *          the graphics instance that is used to draw the game
   * @throws SlickException
   *           in case anything goes wrong during the rendering
   */
  protected abstract void renderGame(GameContainer container, StateBasedGame game, Graphics g) throws SlickException;

  /**
   * Update the game.
   */
  @Override
  public final void update(final GameContainer container, final StateBasedGame game, final int delta)
      throws SlickException {
    Timer.tick();
    updateGame(container, game, delta);

    if (niftyGUI != null) {
      niftyGUI.update();
    }
  }

  /**
   * This function is supposed to be used to update the state of the game. It
   * called during the call of the {@link #update(GameContainer, int)} function.
   * 
   * @param container
   *          the container that displays the game
   * @param delta
   *          the time since the last update
   * @throws SlickException
   *           in case anything goes wrong during the update
   */
  protected abstract void updateGame(GameContainer container, StateBasedGame game, int delta) throws SlickException;
}
