package de.lessvoid.nifty.gdx.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.gdx.input.events.GdxInputEvent;
import de.lessvoid.nifty.gdx.input.events.GdxKeyboardInputEvent;
import de.lessvoid.nifty.gdx.input.events.GdxMouseInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class GdxInputSystem implements InputSystem, InputProcessor {
  private final Input input;
  @Nonnull
  private final Queue<GdxInputEvent> eventQueue;

  public GdxInputSystem(final Input gdxInput) {
    eventQueue = new LinkedList<GdxInputEvent>();
    input = gdxInput;
    input.setInputProcessor(this);
  }

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader niftyResourceLoader) {
  }

  @Override
  public void forwardEvents(@Nonnull final NiftyInputConsumer inputEventConsumer) {
    while (true) {
      final GdxInputEvent event = eventQueue.poll();
      if (event == null) {
        return;
      }
      event.sendToNifty(inputEventConsumer);
      event.freeEvent();
    }
  }

  @Override
  public void setMousePosition(final int x, final int y) {
    input.setCursorPosition(x, y);
  }

  @Override
  public boolean keyDown(final int keyCode) {
    eventQueue.offer(GdxKeyboardInputEvent.getInstance(keyCode, (char) 0, true, false, isShiftDown(), isControlDown()));
    return true;
  }

  public Input getInput() {
    return input;
  }

  private boolean isShiftDown() {
    return input.isKeyPressed(Input.Keys.SHIFT_LEFT) || input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
  }

  private boolean isControlDown() {
    return input.isKeyPressed(Input.Keys.CONTROL_LEFT) || input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
  }

  @Override
  public boolean keyUp(final int keyCode) {
    eventQueue.offer(GdxKeyboardInputEvent.getInstance(keyCode, (char) 0, false, false, isShiftDown(),
        isControlDown()));
    return true;
  }

  @Override
  public boolean keyTyped(final char character) {
    eventQueue.offer(GdxKeyboardInputEvent.getInstance(0, character, true, true, isShiftDown(), isControlDown()));
    return true;
  }

  @Override
  public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
    eventQueue.offer(GdxMouseInputEvent.getMouseEvent(screenX, screenY, 0, pointer, button, true, false));
    return true;
  }

  @Override
  public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
    eventQueue.offer(GdxMouseInputEvent.getMouseEvent(screenX, screenY, 0, pointer, button, false, false));
    return true;
  }

  @Override
  public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
    if (input.isButtonPressed(Input.Buttons.LEFT)) {
      eventQueue.offer(GdxMouseInputEvent.getMouseEvent(screenX, screenY, 0, pointer, Input.Buttons.LEFT, true, true));
    }
    if (input.isButtonPressed(Input.Buttons.MIDDLE)) {
      eventQueue.offer(GdxMouseInputEvent.getMouseEvent(screenX, screenY, 0, pointer, Input.Buttons.MIDDLE, true,
          true));
    }
    if (input.isButtonPressed(Input.Buttons.RIGHT)) {
      eventQueue.offer(GdxMouseInputEvent.getMouseEvent(screenX, screenY, 0, pointer, Input.Buttons.RIGHT, true, true));
    }
    return true;
  }

  @Override
  public boolean mouseMoved(final int screenX, final int screenY) {
    eventQueue.offer(GdxMouseInputEvent.getMouseEvent(screenX, screenY, 0, 0, GdxMouseInputEvent.NO_BUTTON, false,
        false));
    return true;
  }

  @Override
  public boolean scrolled(final int amount) {
    eventQueue.offer(GdxMouseInputEvent.getMouseEvent(input.getX(), input.getY(), amount, 0,
        GdxMouseInputEvent.NO_BUTTON, false, false));
    return true;
  }
}
