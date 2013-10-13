package de.lessvoid.nifty.gdx.input.events;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Pool;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * This is the keyboard input event that is buffered from the libGDX data before its send to the Nifty-GUI.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class GdxKeyboardInputEvent extends KeyboardInputEvent implements GdxInputEvent, Pool.Poolable {
  /**
   * The internal object pool for this the {@link GdxKeyboardInputEvent} class.
   */
  private static final Pool<GdxKeyboardInputEvent> POOL = new Pool<GdxKeyboardInputEvent>() {
    @Override
    protected GdxKeyboardInputEvent newObject() {
      return new GdxKeyboardInputEvent();
    }
  };

  /**
   * Get a instance of the keyboard input event.
   *
   * @param key the key that is pressed (has to be in the libGDX key value space)
   * @param character the character of the key that is pressed
   * @param keyDown {@code true} in case the key was pressed
   * @param keyTyped {@code true} in case the key was typed
   * @param shiftDown {@code true} in case shift is down right now
   * @param controlDown {@code true} in case control is down right now
   * @return the new event instance
   */
  public static GdxKeyboardInputEvent getInstance(
      final int key,
      final char character,
      final boolean keyDown,
      final boolean keyTyped,
      final boolean shiftDown,
      final boolean controlDown) {
    final GdxKeyboardInputEvent newEvent = POOL.obtain();
    newEvent.setData(key, character, keyDown, keyTyped, shiftDown, controlDown);
    return newEvent;
  }

  /**
   * This function frees a instanced of the keyboard event again and sends it back into the object pool.
   *
   * @param event the object to free
   */
  public static void free(final GdxKeyboardInputEvent event) {
    POOL.free(event);
  }

  /**
   * This value is {@code true} in case the key was typed.
   */
  private boolean typed;

  /**
   * The ID of the key in the value space of libGDX.
   */
  private int gdxKey;

  /**
   * Create a new keyboard input event.
   */
  private GdxKeyboardInputEvent() {
    super();
    // nothing to do
  }

  /**
   * Set the data of this input event.
   *
   * @param key the key that was pressed
   * @param character the character of the key
   * @param keyDown {@code true} in case the key was pressed down
   * @param keyTyped {@code true} in case the key was typed
   * @param shiftDown {@code true} in case the shift key is down
   * @param controlDown {@code true} in case the control key is down
   */
  private void setData(
      final int key,
      final char character,
      final boolean keyDown,
      final boolean keyTyped,
      final boolean shiftDown,
      final boolean controlDown) {
      super.setData(getNiftyKeyCode(key), character, keyDown, shiftDown, controlDown);
    typed = keyTyped;
    gdxKey = key;
  }

  /**
   * This function converts a libGDX key code to a Nifty key code.
   *
   * @param gdxKeyCode the libGDX button key code
   * @return the nifty button key code
   */
  private static int getNiftyKeyCode(final int gdxKeyCode) {
    switch (gdxKeyCode) {
      case Input.Keys.NUM_0:
        return KeyboardInputEvent.KEY_0;
      case Input.Keys.NUM_1:
        return KeyboardInputEvent.KEY_1;
      case Input.Keys.NUM_2:
        return KeyboardInputEvent.KEY_2;
      case Input.Keys.NUM_3:
        return KeyboardInputEvent.KEY_3;
      case Input.Keys.NUM_4:
        return KeyboardInputEvent.KEY_4;
      case Input.Keys.NUM_5:
        return KeyboardInputEvent.KEY_5;
      case Input.Keys.NUM_6:
        return KeyboardInputEvent.KEY_6;
      case Input.Keys.NUM_7:
        return KeyboardInputEvent.KEY_7;
      case Input.Keys.NUM_8:
        return KeyboardInputEvent.KEY_8;
      case Input.Keys.NUM_9:
        return KeyboardInputEvent.KEY_9;
      case Input.Keys.A:
        return KeyboardInputEvent.KEY_A;
      case Input.Keys.ALT_LEFT:
        return KeyboardInputEvent.KEY_LMENU;
      case Input.Keys.ALT_RIGHT:
        return KeyboardInputEvent.KEY_RMENU;
      case Input.Keys.APOSTROPHE:
        return KeyboardInputEvent.KEY_APOSTROPHE;
      case Input.Keys.AT:
        return KeyboardInputEvent.KEY_AT;
      case Input.Keys.B:
        return KeyboardInputEvent.KEY_B;
      case Input.Keys.BACK:
        return KeyboardInputEvent.KEY_BACK;
      case Input.Keys.BACKSLASH:
        return KeyboardInputEvent.KEY_BACKSLASH;
      case Input.Keys.C:
        return KeyboardInputEvent.KEY_C;
      case Input.Keys.CALL:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.CAMERA:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.CLEAR:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.COMMA:
        return KeyboardInputEvent.KEY_COMMA;
      case Input.Keys.D:
        return KeyboardInputEvent.KEY_D;
      case Input.Keys.BACKSPACE:
        return KeyboardInputEvent.KEY_BACK;
      case Input.Keys.FORWARD_DEL:
        return KeyboardInputEvent.KEY_DELETE;
      case Input.Keys.CENTER:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.DOWN:
        return KeyboardInputEvent.KEY_DOWN;
      case Input.Keys.LEFT:
        return KeyboardInputEvent.KEY_LEFT;
      case Input.Keys.RIGHT:
        return KeyboardInputEvent.KEY_RIGHT;
      case Input.Keys.UP:
        return KeyboardInputEvent.KEY_UP;
      case Input.Keys.E:
        return KeyboardInputEvent.KEY_E;
      case Input.Keys.ENDCALL:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.ENTER:
        return KeyboardInputEvent.KEY_RETURN;
      case Input.Keys.ENVELOPE:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.EQUALS:
        return KeyboardInputEvent.KEY_EQUALS;
      case Input.Keys.EXPLORER:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.F:
        return KeyboardInputEvent.KEY_F;
      case Input.Keys.FOCUS:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.G:
        return KeyboardInputEvent.KEY_G;
      case Input.Keys.GRAVE:
        return KeyboardInputEvent.KEY_GRAVE;
      case Input.Keys.H:
        return KeyboardInputEvent.KEY_H;
      case Input.Keys.HEADSETHOOK:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.HOME:
        return KeyboardInputEvent.KEY_HOME;
      case Input.Keys.I:
        return KeyboardInputEvent.KEY_I;
      case Input.Keys.J:
        return KeyboardInputEvent.KEY_J;
      case Input.Keys.K:
        return KeyboardInputEvent.KEY_K;
      case Input.Keys.L:
        return KeyboardInputEvent.KEY_L;
      case Input.Keys.LEFT_BRACKET:
        return KeyboardInputEvent.KEY_LBRACKET;
      case Input.Keys.M:
        return KeyboardInputEvent.KEY_M;
      case Input.Keys.MEDIA_FAST_FORWARD:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.MEDIA_NEXT:
        return KeyboardInputEvent.KEY_NEXT;
      case Input.Keys.MEDIA_PLAY_PAUSE:
        return KeyboardInputEvent.KEY_PAUSE;
      case Input.Keys.MEDIA_PREVIOUS:
        return KeyboardInputEvent.KEY_PRIOR;
      case Input.Keys.MEDIA_REWIND:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.MEDIA_STOP:
        return KeyboardInputEvent.KEY_STOP;
      case Input.Keys.MENU:
        return KeyboardInputEvent.KEY_LMENU;
      case Input.Keys.MINUS:
        return KeyboardInputEvent.KEY_MINUS;
      case Input.Keys.MUTE:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.N:
        return KeyboardInputEvent.KEY_N;
      case Input.Keys.NOTIFICATION:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.NUM:
        return KeyboardInputEvent.KEY_NUMLOCK;
      case Input.Keys.O:
        return KeyboardInputEvent.KEY_O;
      case Input.Keys.P:
        return KeyboardInputEvent.KEY_P;
      case Input.Keys.PERIOD:
        return KeyboardInputEvent.KEY_PERIOD;
      case Input.Keys.PLUS:
        return KeyboardInputEvent.KEY_ADD;
      case Input.Keys.POUND:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.POWER:
        return KeyboardInputEvent.KEY_POWER;
      case Input.Keys.Q:
        return KeyboardInputEvent.KEY_Q;
      case Input.Keys.R:
        return KeyboardInputEvent.KEY_R;
      case Input.Keys.RIGHT_BRACKET:
        return KeyboardInputEvent.KEY_RBRACKET;
      case Input.Keys.S:
        return KeyboardInputEvent.KEY_S;
      case Input.Keys.SEARCH:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.SEMICOLON:
        return KeyboardInputEvent.KEY_SEMICOLON;
      case Input.Keys.SHIFT_LEFT:
        return KeyboardInputEvent.KEY_LSHIFT;
      case Input.Keys.SHIFT_RIGHT:
        return KeyboardInputEvent.KEY_RSHIFT;
      case Input.Keys.SLASH:
        return KeyboardInputEvent.KEY_SLASH;
      case Input.Keys.SOFT_LEFT:
        return KeyboardInputEvent.KEY_LMETA;
      case Input.Keys.SOFT_RIGHT:
        return KeyboardInputEvent.KEY_RMETA;
      case Input.Keys.SPACE:
        return KeyboardInputEvent.KEY_SPACE;
      case Input.Keys.STAR:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.SYM:
        return KeyboardInputEvent.KEY_SYSRQ;
      case Input.Keys.T:
        return KeyboardInputEvent.KEY_T;
      case Input.Keys.TAB:
        return KeyboardInputEvent.KEY_TAB;
      case Input.Keys.U:
        return KeyboardInputEvent.KEY_U;
      case Input.Keys.UNKNOWN:
        return KeyboardInputEvent.KEY_UNLABELED;
      case Input.Keys.V:
        return KeyboardInputEvent.KEY_V;
      case Input.Keys.VOLUME_DOWN:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.VOLUME_UP:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.W:
        return KeyboardInputEvent.KEY_W;
      case Input.Keys.X:
        return KeyboardInputEvent.KEY_X;
      case Input.Keys.Y:
        return KeyboardInputEvent.KEY_Y;
      case Input.Keys.Z:
        return KeyboardInputEvent.KEY_Z;
      case Input.Keys.CONTROL_LEFT:
        return KeyboardInputEvent.KEY_LCONTROL;
      case Input.Keys.CONTROL_RIGHT:
        return KeyboardInputEvent.KEY_RCONTROL;
      case Input.Keys.ESCAPE:
        return KeyboardInputEvent.KEY_ESCAPE;
      case Input.Keys.END:
        return KeyboardInputEvent.KEY_END;
      case Input.Keys.INSERT:
        return KeyboardInputEvent.KEY_INSERT;
      case Input.Keys.PAGE_UP:
        return KeyboardInputEvent.KEY_PRIOR;
      case Input.Keys.PAGE_DOWN:
        return KeyboardInputEvent.KEY_NEXT;
      case Input.Keys.PICTSYMBOLS:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.SWITCH_CHARSET:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_A:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_B:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_C:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_X:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_Y:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_Z:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_L1:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_R1:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_L2:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_R2:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_THUMBL:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_THUMBR:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_START:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_SELECT:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.BUTTON_MODE:
        return KeyboardInputEvent.KEY_NONE;
      case Input.Keys.NUMPAD_0:
        return KeyboardInputEvent.KEY_NUMPAD0;
      case Input.Keys.NUMPAD_1:
        return KeyboardInputEvent.KEY_NUMPAD1;
      case Input.Keys.NUMPAD_2:
        return KeyboardInputEvent.KEY_NUMPAD2;
      case Input.Keys.NUMPAD_3:
        return KeyboardInputEvent.KEY_NUMPAD3;
      case Input.Keys.NUMPAD_4:
        return KeyboardInputEvent.KEY_NUMPAD4;
      case Input.Keys.NUMPAD_5:
        return KeyboardInputEvent.KEY_NUMPAD5;
      case Input.Keys.NUMPAD_6:
        return KeyboardInputEvent.KEY_NUMPAD6;
      case Input.Keys.NUMPAD_7:
        return KeyboardInputEvent.KEY_NUMPAD7;
      case Input.Keys.NUMPAD_8:
        return KeyboardInputEvent.KEY_NUMPAD8;
      case Input.Keys.NUMPAD_9:
        return KeyboardInputEvent.KEY_NUMPAD9;
      case Input.Keys.COLON:
        return KeyboardInputEvent.KEY_COLON;
      case Input.Keys.F1:
        return KeyboardInputEvent.KEY_F1;
      case Input.Keys.F2:
        return KeyboardInputEvent.KEY_F2;
      case Input.Keys.F3:
        return KeyboardInputEvent.KEY_F3;
      case Input.Keys.F4:
        return KeyboardInputEvent.KEY_F4;
      case Input.Keys.F5:
        return KeyboardInputEvent.KEY_F5;
      case Input.Keys.F6:
        return KeyboardInputEvent.KEY_F6;
      case Input.Keys.F7:
        return KeyboardInputEvent.KEY_F7;
      case Input.Keys.F8:
        return KeyboardInputEvent.KEY_F8;
      case Input.Keys.F9:
        return KeyboardInputEvent.KEY_F9;
      case Input.Keys.F10:
        return KeyboardInputEvent.KEY_F10;
      case Input.Keys.F11:
        return KeyboardInputEvent.KEY_F11;
      case Input.Keys.F12:
        return KeyboardInputEvent.KEY_F12;
      default:
        return KeyboardInputEvent.KEY_NONE;
    }
  }

  @Override
  public boolean sendToNifty(final NiftyInputConsumer consumer) {
    return consumer.processKeyboardEvent(this);
  }

  @Override
  public void sendToGdx(final InputProcessor processor) {
    if (typed) {
      processor.keyTyped(getCharacter());
    } else if (isKeyDown()) {
      processor.keyDown(gdxKey);
    } else {
      processor.keyUp(gdxKey);
    }
  }

  @Override
  public void freeEvent() {
    free(this);
  }

  @Override
  public void reset() {
    // no action required
  }
}
