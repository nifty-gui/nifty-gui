package de.lessvoid.nifty.slick2d.input;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import org.newdawn.slick.Input;

/**
 * This is a input system that is <b>not</b> supposed to be used in any application directly. This input system allows
 * the on-the-fly replacement of the used input system.
 *
 * @author Martin Karing &gt;nitram@illarion.org&lt;
 */
public final class RelaySlickInputSystem implements SlickInputSystem {
  /**
   * The input system that is supposed to receive all input data.
   */
  private SlickInputSystem targetInputSystem;

  @Override
  public void controllerButtonPressed(final int controller, final int button) {
    if (targetInputSystem != null) {
      targetInputSystem.controllerButtonPressed(controller, button);
    }
  }

  @Override
  public void controllerButtonReleased(final int controller, final int button) {
    if (targetInputSystem != null) {
      targetInputSystem.controllerButtonReleased(controller, button);
    }
  }

  @Override
  public void controllerDownPressed(final int controller) {
    if (targetInputSystem != null) {
      targetInputSystem.controllerDownPressed(controller);
    }
  }

  @Override
  public void controllerDownReleased(final int controller) {
    if (targetInputSystem != null) {
      targetInputSystem.controllerDownReleased(controller);
    }
  }

  @Override
  public void controllerLeftPressed(final int controller) {
    if (targetInputSystem != null) {
      targetInputSystem.controllerLeftPressed(controller);
    }
  }

  @Override
  public void controllerLeftReleased(final int controller) {
    if (targetInputSystem != null) {
      targetInputSystem.controllerLeftReleased(controller);
    }
  }

  @Override
  public void controllerRightPressed(final int controller) {
    if (targetInputSystem != null) {
      targetInputSystem.controllerRightPressed(controller);
    }
  }

  @Override
  public void controllerRightReleased(final int controller) {
    if (targetInputSystem != null) {
      targetInputSystem.controllerRightReleased(controller);
    }
  }

  @Override
  public void controllerUpPressed(final int controller) {
    if (targetInputSystem != null) {
      targetInputSystem.controllerUpPressed(controller);
    }
  }

  @Override
  public void controllerUpReleased(final int controller) {
    if (targetInputSystem != null) {
      targetInputSystem.controllerUpReleased(controller);
    }
  }

  @Override
  public void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
    if (targetInputSystem != null) {
      targetInputSystem.forwardEvents(inputEventConsumer);
    }
  }

  @Override
  public void inputEnded() {
    if (targetInputSystem != null) {
      targetInputSystem.inputEnded();
    }
  }

  @Override
  public void inputStarted() {
    if (targetInputSystem != null) {
      targetInputSystem.inputStarted();
    }
  }

  @Override
  public boolean isAcceptingInput() {
    if (targetInputSystem != null) {
      return targetInputSystem.isAcceptingInput();
    }
    return false;
  }

  @Override
  public void keyPressed(final int key, final char c) {
    if (targetInputSystem != null) {
      targetInputSystem.keyPressed(key, c);
    }
  }

  @Override
  public void keyReleased(final int key, final char c) {
    if (targetInputSystem != null) {
      targetInputSystem.keyReleased(key, c);
    }
  }

  @Override
  public void mouseClicked(final int button, final int x, final int y, final int clickCount) {
    if (targetInputSystem != null) {
      targetInputSystem.mouseClicked(button, x, y, clickCount);
    }
  }

  @Override
  public void mouseDragged(final int oldx, final int oldy, final int newx, final int newy) {
    if (targetInputSystem != null) {
      targetInputSystem.mouseDragged(oldx, oldy, newx, newy);
    }
  }

  @Override
  public void mouseMoved(final int oldx, final int oldy, final int newx, final int newy) {
    if (targetInputSystem != null) {
      targetInputSystem.mouseMoved(oldx, oldy, newx, newy);
    }
  }

  @Override
  public void mousePressed(final int button, final int x, final int y) {
    if (targetInputSystem != null) {
      targetInputSystem.mousePressed(button, x, y);
    }
  }

  @Override
  public void mouseReleased(final int button, final int x, final int y) {
    if (targetInputSystem != null) {
      targetInputSystem.mouseReleased(button, x, y);
    }
  }

  @Override
  public void mouseWheelMoved(final int change) {
    if (targetInputSystem != null) {
      targetInputSystem.mouseWheelMoved(change);
    }
  }

  @Override
  public void setInput(final Input input) {
    if (targetInputSystem != null) {
      targetInputSystem.setInput(input);
    }
  }

  @Override
  public void setMousePosition(final int x, final int y) {
    if (targetInputSystem != null) {
      targetInputSystem.setMousePosition(x, y);
    }
  }

  @Override
  public void setResourceLoader(final NiftyResourceLoader niftyResourceLoader) {
    if (targetInputSystem != null) {
      targetInputSystem.setResourceLoader(niftyResourceLoader);
    }
  }

  /**
   * Set the input system that is supposed to receive all input data.
   *
   * @param system the target input system
   */
  public void setTargetInputSystem(final SlickInputSystem system) {
    targetInputSystem = system;
  }
}
