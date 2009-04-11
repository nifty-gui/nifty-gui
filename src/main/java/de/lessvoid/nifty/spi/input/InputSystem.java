package de.lessvoid.nifty.spi.input;

import java.util.List;

import de.lessvoid.nifty.input.mouse.MouseInputEvent;

/**
 * Interface for InputSystem for Nifty.
 * @author void
 */
public interface InputSystem {

  /**
   * Initialize the InputSystem.
   * @throws Exception
   */
  public void startup() throws Exception;

  /**
   * Shutdown the InputSystem.
   * @throws Exception
   */
  public void shutdown();

  /**
   * Get all available MouseEvents into a List.
   * @return List of MouseInputEvent for Nifty to process.
   */
  public List < MouseInputEvent > getMouseEvents();
}
