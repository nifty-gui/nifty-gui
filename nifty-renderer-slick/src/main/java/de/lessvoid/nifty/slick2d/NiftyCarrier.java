package de.lessvoid.nifty.slick2d;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.input.RelaySlickInputSystem;
import de.lessvoid.nifty.slick2d.input.SlickInputSystem;
import de.lessvoid.nifty.slick2d.render.SlickRenderDevice;
import de.lessvoid.nifty.slick2d.sound.SlickSoundDevice;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.time.TimeProvider;

/**
 * As the name suggest, this class carries the instance of the Nifty-GUI that is used around.
 *
 * @author Martin Karing &gt;nitram@illarion.org&lt;
 */
public final class NiftyCarrier {
  /**
   * The instance of the Nifty-GUI that is carried by this carrier.
   */
  private Nifty nifty;

  /**
   * The relay input system that is used in case the target input system is switched.
   */
  private final RelaySlickInputSystem relayInputSystem;

  /**
   * Constructor that allows to set if the relay input system is supposed to be enabled or not.
   *
   * @param useRelay {@code true} to enable the relay input system
   */
  NiftyCarrier(final boolean useRelay) {
    if (useRelay) {
      relayInputSystem = new RelaySlickInputSystem();
    } else {
      relayInputSystem = null;
    }
  }

  /**
   * Check if the carrier is using the relay input system. In case it is, its possible to switch the current target
   * input system.
   *
   * @return {@code true} in case the relay input system is used
   */
  public boolean isUsingRelayInputSystem() {
    return relayInputSystem != null;
  }

  /**
   * Get the instance of the Nifty-GUI that is carried by this carrier.
   *
   * @return the instance of the Nifty-GUI
   */
  public Nifty getNifty() {
    return nifty;
  }

  /**
   * Check if the Nifty-GUI in this carrier was already initialized.
   *
   * @return {@code true} if the GUI is initialized
   */
  public boolean isInitialized() {
    return nifty != null;
  }

  /**
   * Initialize the Nifty-GUI.
   *
   * @param renderDevice the render device to use for the GUI
   * @param soundDevice the sound device to use for the GUI
   * @param inputSystem the input system to use for the GUI
   * @param timeProvider the time provider to use for the GUI
   * @throws IllegalStateException in case this function was already called
   */
  public void initNifty(
      final SlickRenderDevice renderDevice,
      final SlickSoundDevice soundDevice,
      final SlickInputSystem inputSystem,
      final TimeProvider timeProvider) {
    if (isInitialized()) {
      throw new IllegalStateException("The Nifty-GUI was already initialized. Its illegal to do so twice.");
    }

    final InputSystem activeInputSystem;
    if (relayInputSystem == null) {
      activeInputSystem = inputSystem;
    } else {
      activeInputSystem = relayInputSystem;
      relayInputSystem.setTargetInputSystem(inputSystem);
    }

    nifty = new Nifty(renderDevice, soundDevice, activeInputSystem, timeProvider);
  }

  /**
   * Change the input system that is supposed to handle the input.
   *
   * @param inputSystem the new input system
   * @throws IllegalStateException in case this instance of the carrier does not use the relay input system
   */
  public void setInputSystem(final SlickInputSystem inputSystem) {
    if (relayInputSystem == null) {
      throw new IllegalStateException(
          "Changing the input system is only allowed for carriers that use the relay input system.");
    }
    relayInputSystem.setTargetInputSystem(inputSystem);
  }
}
