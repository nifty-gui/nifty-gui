package de.lessvoid.nifty.gdx.input.events;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Pool;
import de.lessvoid.nifty.NiftyInputConsumer;

import javax.annotation.Nonnull;

/**
 * This mouse input event is used to buffer the events send by libGDX before they are send to the Nifty-GUI.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class GdxMouseInputEvent implements GdxInputEvent, Pool.Poolable {
  private int mouseX;
  private int mouseY;
  private int wheelDelta;
  private int button;
  private boolean buttonDown;
  private boolean dragging;
  private int pointer;

  /**
   * The internal object pool for this the {@link GdxMouseInputEvent} class.
   */
  private static final Pool<GdxMouseInputEvent> POOL = new Pool<GdxMouseInputEvent>() {
    @Nonnull
    @Override
    protected GdxMouseInputEvent newObject() {
      return new GdxMouseInputEvent();
    }
  };

  public static GdxMouseInputEvent getMouseEvent(
      final int mouseX,
      final int mouseY,
      final int wheelDelta,
      final int pointer,
      final int button,
      final boolean buttonDown,
      final boolean dragged) {
    final GdxMouseInputEvent event = POOL.obtain();
    event.mouseX = mouseX;
    event.mouseY = mouseY;
    event.wheelDelta = wheelDelta;
    event.pointer = pointer;
    event.button = button;
    event.buttonDown = buttonDown;
    event.dragging = dragged;

    return event;
  }

  private GdxMouseInputEvent() {
  }

  public static final int NO_BUTTON = -1;

  @Override
  public boolean sendToNifty(@Nonnull final NiftyInputConsumer consumer) {
    return consumer.processMouseEvent(mouseX, mouseY, -wheelDelta, button, buttonDown);
  }

  @Override
  public boolean sendToGdx(@Nonnull final InputProcessor processor) {
    if (dragging) {
      return processor.touchDragged(mouseX, mouseY, pointer);
    } else if (buttonDown) {
      return processor.touchDown(mouseX, mouseY, pointer, button);
    } else if (button != NO_BUTTON) {
      return processor.touchUp(mouseX, mouseY, pointer, button);
    } else if (wheelDelta != 0) {
      return processor.scrolled(wheelDelta);
    } else {
      return processor.mouseMoved(mouseX, mouseY);
    }
  }

  @Override
  public void freeEvent() {
    POOL.free(this);
  }

  @Override
  public void reset() {
    // nothing to do
  }
}
