package de.lessvoid.nifty.slick2d;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.input.ForwardingInputSystem;
import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickInputSystem;
import de.lessvoid.nifty.slick2d.render.SlickRenderDevice;
import de.lessvoid.nifty.slick2d.sound.SlickSoundDevice;
import de.lessvoid.nifty.slick2d.time.LWJGLTimeProvider;
import de.lessvoid.nifty.spi.time.TimeProvider;
import org.newdawn.slick.*;

/**
 * This game implementation creates a Slick Game that is able to Overlay the game itself with a Nifty GUI.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class NiftyOverlayGame implements Game, NiftyInputForwarding, NiftyOrderControl {
  /**
   * The one and only Nifty GUI.
   */
  private Nifty niftyGUI = null;

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
  protected NiftyOverlayGame() {
    setRenderOrder(NiftyRenderOrder.NiftyOverlay);
    setUpdateOrder(NiftyUpdateOrder.NiftyLast);
  }

  /**
   * Get the instance of the NiftyGUI that is used to render this screen.
   *
   * @return the instance of the NiftyGUI
   */
  public final Nifty getNifty() {
    return niftyGUI;
  }

  @Override
  public final ForwardingInputSystem getInputForwardingControl() {
    return inputForwardingControl;
  }

  /**
   * Initialize the game and the GUI.
   */
  @Override
  public final void init(final GameContainer container) throws SlickException {
    initGameAndGUI(container);

    if (niftyGUI == null) {
      throw new IllegalStateException("NiftyGUI was not initialized.");
    }
  }

  /**
   * Initialize the game. This function is called during {@link #init(GameContainer)}. During this call its needed to
   * initialize the Nifty GUI with own options by calling {@link #initNifty(GameContainer, SlickRenderDevice,
   * SlickSoundDevice, SlickInputSystem, TimeProvider)} .
   *
   * @param container the game container that displays the game
   * @throws SlickException in case initializing the game goes wrong
   */
  protected abstract void initGameAndGUI(GameContainer container) throws SlickException;

  /**
   * Initialize the Nifty GUI for this game.
   *
   * @param container the container used to display the game
   * @param renderDevice the render device that is supposed to be used to render the GUI
   * @param soundDevice the sound device that is supposed to be used
   * @param inputSystem the input system that is supposed to be used
   * @param timeProvider the time provider that is supposed to be used
   * @throws IllegalStateException in case this function was called before
   */
  protected final void initNifty(
      @SuppressWarnings("TypeMayBeWeakened") final GameContainer container,
      final SlickRenderDevice renderDevice,
      final SlickSoundDevice soundDevice,
      final SlickInputSystem inputSystem,
      final TimeProvider timeProvider) {
    if (niftyGUI != null) {
      throw new IllegalStateException("The NiftyGUI was already initialized. Its illegal to do so twice.");
    }

    final Input input = container.getInput();
    inputSystem.setInput(input);

    niftyGUI = new Nifty(renderDevice, soundDevice, inputSystem, timeProvider);

    if (inputSystem instanceof ForwardingInputSystem) {
      inputForwardingControl = (ForwardingInputSystem) inputSystem;
    }

    input.removeListener(inputSystem);
    input.addListener(inputSystem);

    prepareNifty(niftyGUI);
  }

  /**
   * Initialize the Nifty GUI for this game. This function will use the default {@link TimeProvider}.
   *
   * @param container the container used to display the game
   * @param renderDevice the render device that is supposed to be used to render the GUI
   * @param soundDevice the sound device that is supposed to be used
   * @param inputSystem the input system that is supposed to be used
   * @throws IllegalStateException in case this function was called before
   */
  protected final void initNifty(
      final GameContainer container,
      final SlickRenderDevice renderDevice,
      final SlickSoundDevice soundDevice,
      final SlickInputSystem inputSystem) {
    initNifty(container, renderDevice, soundDevice, inputSystem, new LWJGLTimeProvider());
  }

  /**
   * Initialize the Nifty GUI for this game. This function will use the default {@link TimeProvider}. Also it will use
   * the render and sound devices that are provided with this library.
   *
   * @param container the container used to display the game
   * @param inputSystem the input system that is supposed to be used
   * @throws IllegalStateException in case this function was called before
   * @see SlickRenderDevice
   * @see SlickSoundDevice
   */
  protected final void initNifty(final GameContainer container, final SlickInputSystem inputSystem) {
    initNifty(container, new SlickRenderDevice(container), new SlickSoundDevice(), inputSystem);
  }

  /**
   * Initialize the Nifty GUI for this game. This function will use the default {@link TimeProvider}. Also it will use
   * the render and sound devices that are provided with this library. As for the input the a system will be
   * implemented
   * that forwards the commands <i>only</i> to the Nifty-GUI.
   *
   * @param container the container used to display the game
   * @throws IllegalStateException in case this function was called before
   * @see SlickRenderDevice
   * @see SlickSoundDevice
   * @see PlainSlickInputSystem
   */
  protected final void initNifty(final GameContainer container) {
    initNifty(container, new PlainSlickInputSystem());
  }

  @Override
  public final boolean isInputForwardingSupported() {
    return inputForwardingControl != null;
  }

  /**
   * This function should be used to prepare the actual GUI and the controllers of the Nifty GUI. It is called right
   * after the Nifty GUI got initialized.
   *
   * @param nifty the Nifty GUI that got initialized
   */
  protected abstract void prepareNifty(Nifty nifty);

  /**
   * Render the game.
   */
  @Override
  public final void render(final GameContainer container, final Graphics g) throws SlickException {
    if (niftyGUI == null) {
      renderGame(container, g);
    } else {
      switch (renderOrder) {
        case NiftyOverlay:
          renderGame(container, g);
          niftyGUI.render(false);
          break;
        case NiftyBackground:
          niftyGUI.render(true);
          renderGame(container, g);
          break;
      }
    }
  }

  /**
   * This function is supposed to be used to render the game. It is called during the call of the {@link
   * #render(GameContainer, Graphics)} function.
   *
   * @param container the container that displays the game
   * @param g the graphics instance that is used to draw the game
   * @throws SlickException in case anything goes wrong during the rendering
   */
  protected abstract void renderGame(GameContainer container, Graphics g) throws SlickException;

  /**
   * Update the game.
   */
  @Override
  public final void update(final GameContainer container, final int delta) throws SlickException {
    if (niftyGUI == null) {
      updateGame(container, delta);
    } else {
      switch (updateOrder) {
        case NiftyLast:
          updateGame(container, delta);
          niftyGUI.update();
          break;
        case NiftyFirst:
          niftyGUI.update();
          updateGame(container, delta);
          break;
      }
    }
  }

  /**
   * This function is supposed to be used to update the state of the game. It called during the call of the {@link
   * #update(GameContainer, int)} function.
   *
   * @param container the container that displays the game
   * @param delta the time since the last update
   * @throws SlickException in case anything goes wrong during the update
   */
  protected abstract void updateGame(GameContainer container, int delta) throws SlickException;

  @Override
  public final NiftyRenderOrder getRenderOrder() {
    return renderOrder;
  }

  @Override
  public final NiftyUpdateOrder getUpdateOrder() {
    return updateOrder;
  }

  @Override
  public final void setRenderOrder(NiftyRenderOrder order) {
    renderOrder = order;
  }

  @Override
  public final void setUpdateOrder(NiftyUpdateOrder order) {
    updateOrder = order;
  }
}
