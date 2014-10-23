package de.lessvoid.nifty.spi;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.api.NiftyResourceLoader;
import de.lessvoid.nifty.api.input.NiftyInputConsumer;

/**
 * Interface for Niftys InputSystem.
 * @author void
 */
public interface NiftyInputDevice {

  /**
   * Gives this NiftyInputDevice access to the NiftyResourceLoader.
   *
   * @param niftyResourceLoader NiftyResourceLoader
   */
  void setResourceLoader(@Nonnull NiftyResourceLoader niftyResourceLoader);

  /**
   * This method is called by Nifty when it's ready to process input events. The InputSystem implementation should
   * call the methods on the given NiftyInputConsumer to forward events to Nifty.
   *
   * @param inputEventConsumer the NiftyInputConsumer to forward input events to
   */
  void forwardEvents(@Nonnull NiftyInputConsumer inputEventConsumer);

  /**
   * This allows Nifty to set the position of the mouse to the given coordinate with {@code (0, 0)} being the
   * upper left corner of the screen.
   *
   * @param x x coordinate of mouse
   * @param y y coordinate of mouse
   */
  void setMousePosition(int x, int y);
}
