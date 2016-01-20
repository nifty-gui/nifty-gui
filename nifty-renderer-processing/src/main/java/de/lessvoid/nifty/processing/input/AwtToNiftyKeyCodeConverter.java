package de.lessvoid.nifty.processing.input;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

import java.awt.event.KeyEvent;

/**
 * Convert awt key codes to nifty key codes, based on lwjgl <a href="http://java-game-lib.svn.sourceforge
 * .net/viewvc/java-game-lib/trunk/LWJGL/src/java/org/lwjgl/opengl/KeyboardEventQueue
 * .java?revision=3116&content-type=text%2Fplain"
 * >code</a>.
 *
 * @author acoppes
 */
public class AwtToNiftyKeyCodeConverter {

  public int convertToNiftyKeyCode(int key_code, int location) {
    // manually map positioned keys
    switch (key_code) {
      case KeyEvent.VK_ALT: // fall through
        if (location == KeyEvent.KEY_LOCATION_RIGHT) {
          return KeyboardInputEvent.KEY_RMENU;
        } else {
          return KeyboardInputEvent.KEY_LMENU;
        }
      case KeyEvent.VK_WINDOWS:
      case KeyEvent.VK_META:
        if (location == KeyEvent.KEY_LOCATION_RIGHT) {
          return KeyboardInputEvent.KEY_RMETA;
        } else {
          return KeyboardInputEvent.KEY_LMETA;
        }
      case KeyEvent.VK_SHIFT:
        if (location == KeyEvent.KEY_LOCATION_RIGHT) {
          return KeyboardInputEvent.KEY_RSHIFT;
        } else {
          return KeyboardInputEvent.KEY_LSHIFT;
        }
      case KeyEvent.VK_CONTROL:
        if (location == KeyEvent.KEY_LOCATION_RIGHT) {
          return KeyboardInputEvent.KEY_RCONTROL;
        } else {
          return KeyboardInputEvent.KEY_LCONTROL;
        }
      default:
        return KEY_MAP[key_code];
    }
  }

  private static final int[] KEY_MAP = new int[0xffff];

  static {
    KEY_MAP[KeyEvent.VK_0] = KeyboardInputEvent.KEY_0;
    KEY_MAP[KeyEvent.VK_1] = KeyboardInputEvent.KEY_1;
    KEY_MAP[KeyEvent.VK_2] = KeyboardInputEvent.KEY_2;
    KEY_MAP[KeyEvent.VK_3] = KeyboardInputEvent.KEY_3;
    KEY_MAP[KeyEvent.VK_4] = KeyboardInputEvent.KEY_4;
    KEY_MAP[KeyEvent.VK_5] = KeyboardInputEvent.KEY_5;
    KEY_MAP[KeyEvent.VK_6] = KeyboardInputEvent.KEY_6;
    KEY_MAP[KeyEvent.VK_7] = KeyboardInputEvent.KEY_7;
    KEY_MAP[KeyEvent.VK_8] = KeyboardInputEvent.KEY_8;
    KEY_MAP[KeyEvent.VK_9] = KeyboardInputEvent.KEY_9;
    KEY_MAP[KeyEvent.VK_A] = KeyboardInputEvent.KEY_A;
    KEY_MAP[KeyEvent.VK_ADD] = KeyboardInputEvent.KEY_ADD;
    KEY_MAP[KeyEvent.VK_ALT_GRAPH] = KeyboardInputEvent.KEY_RMENU;
    KEY_MAP[KeyEvent.VK_AT] = KeyboardInputEvent.KEY_AT;
    KEY_MAP[KeyEvent.VK_B] = KeyboardInputEvent.KEY_B;
    KEY_MAP[KeyEvent.VK_BACK_SLASH] = KeyboardInputEvent.KEY_BACKSLASH;
    KEY_MAP[KeyEvent.VK_BACK_SPACE] = KeyboardInputEvent.KEY_BACK;
    KEY_MAP[KeyEvent.VK_C] = KeyboardInputEvent.KEY_C;
    KEY_MAP[KeyEvent.VK_CAPS_LOCK] = KeyboardInputEvent.KEY_CAPITAL;
    KEY_MAP[KeyEvent.VK_CIRCUMFLEX] = KeyboardInputEvent.KEY_CIRCUMFLEX;
    KEY_MAP[KeyEvent.VK_CLOSE_BRACKET] = KeyboardInputEvent.KEY_RBRACKET;
    KEY_MAP[KeyEvent.VK_COLON] = KeyboardInputEvent.KEY_COLON;
    KEY_MAP[KeyEvent.VK_COMMA] = KeyboardInputEvent.KEY_COMMA;
    KEY_MAP[KeyEvent.VK_CONVERT] = KeyboardInputEvent.KEY_CONVERT;
    KEY_MAP[KeyEvent.VK_D] = KeyboardInputEvent.KEY_D;
    KEY_MAP[KeyEvent.VK_DECIMAL] = KeyboardInputEvent.KEY_DECIMAL;
    KEY_MAP[KeyEvent.VK_DELETE] = KeyboardInputEvent.KEY_DELETE;
    KEY_MAP[KeyEvent.VK_DIVIDE] = KeyboardInputEvent.KEY_DIVIDE;
    KEY_MAP[KeyEvent.VK_DOWN] = KeyboardInputEvent.KEY_DOWN;
    KEY_MAP[KeyEvent.VK_E] = KeyboardInputEvent.KEY_E;
    KEY_MAP[KeyEvent.VK_END] = KeyboardInputEvent.KEY_END;
    KEY_MAP[KeyEvent.VK_ENTER] = KeyboardInputEvent.KEY_RETURN;
    KEY_MAP[KeyEvent.VK_EQUALS] = KeyboardInputEvent.KEY_EQUALS;
    KEY_MAP[KeyEvent.VK_ESCAPE] = KeyboardInputEvent.KEY_ESCAPE;
    KEY_MAP[KeyEvent.VK_F] = KeyboardInputEvent.KEY_F;
    KEY_MAP[KeyEvent.VK_F1] = KeyboardInputEvent.KEY_F1;
    KEY_MAP[KeyEvent.VK_F10] = KeyboardInputEvent.KEY_F10;
    KEY_MAP[KeyEvent.VK_F11] = KeyboardInputEvent.KEY_F11;
    KEY_MAP[KeyEvent.VK_F12] = KeyboardInputEvent.KEY_F12;
    KEY_MAP[KeyEvent.VK_F13] = KeyboardInputEvent.KEY_F13;
    KEY_MAP[KeyEvent.VK_F14] = KeyboardInputEvent.KEY_F14;
    KEY_MAP[KeyEvent.VK_F15] = KeyboardInputEvent.KEY_F15;
    KEY_MAP[KeyEvent.VK_F2] = KeyboardInputEvent.KEY_F2;
    KEY_MAP[KeyEvent.VK_F3] = KeyboardInputEvent.KEY_F3;
    KEY_MAP[KeyEvent.VK_F4] = KeyboardInputEvent.KEY_F4;
    KEY_MAP[KeyEvent.VK_F5] = KeyboardInputEvent.KEY_F5;
    KEY_MAP[KeyEvent.VK_F6] = KeyboardInputEvent.KEY_F6;
    KEY_MAP[KeyEvent.VK_F7] = KeyboardInputEvent.KEY_F7;
    KEY_MAP[KeyEvent.VK_F8] = KeyboardInputEvent.KEY_F8;
    KEY_MAP[KeyEvent.VK_F9] = KeyboardInputEvent.KEY_F9;
    KEY_MAP[KeyEvent.VK_G] = KeyboardInputEvent.KEY_G;
    KEY_MAP[KeyEvent.VK_H] = KeyboardInputEvent.KEY_H;
    KEY_MAP[KeyEvent.VK_HOME] = KeyboardInputEvent.KEY_HOME;
    KEY_MAP[KeyEvent.VK_I] = KeyboardInputEvent.KEY_I;
    KEY_MAP[KeyEvent.VK_INSERT] = KeyboardInputEvent.KEY_INSERT;
    KEY_MAP[KeyEvent.VK_J] = KeyboardInputEvent.KEY_J;
    KEY_MAP[KeyEvent.VK_K] = KeyboardInputEvent.KEY_K;
    KEY_MAP[KeyEvent.VK_KANA] = KeyboardInputEvent.KEY_KANA;
    KEY_MAP[KeyEvent.VK_KANJI] = KeyboardInputEvent.KEY_KANJI;
    KEY_MAP[KeyEvent.VK_L] = KeyboardInputEvent.KEY_L;
    KEY_MAP[KeyEvent.VK_LEFT] = KeyboardInputEvent.KEY_LEFT;
    KEY_MAP[KeyEvent.VK_M] = KeyboardInputEvent.KEY_M;
    KEY_MAP[KeyEvent.VK_MINUS] = KeyboardInputEvent.KEY_MINUS;
    KEY_MAP[KeyEvent.VK_MULTIPLY] = KeyboardInputEvent.KEY_MULTIPLY;
    KEY_MAP[KeyEvent.VK_N] = KeyboardInputEvent.KEY_N;
    KEY_MAP[KeyEvent.VK_NUM_LOCK] = KeyboardInputEvent.KEY_NUMLOCK;
    KEY_MAP[KeyEvent.VK_NUMPAD0] = KeyboardInputEvent.KEY_NUMPAD0;
    KEY_MAP[KeyEvent.VK_NUMPAD1] = KeyboardInputEvent.KEY_NUMPAD1;
    KEY_MAP[KeyEvent.VK_NUMPAD2] = KeyboardInputEvent.KEY_NUMPAD2;
    KEY_MAP[KeyEvent.VK_NUMPAD3] = KeyboardInputEvent.KEY_NUMPAD3;
    KEY_MAP[KeyEvent.VK_NUMPAD4] = KeyboardInputEvent.KEY_NUMPAD4;
    KEY_MAP[KeyEvent.VK_NUMPAD5] = KeyboardInputEvent.KEY_NUMPAD5;
    KEY_MAP[KeyEvent.VK_NUMPAD6] = KeyboardInputEvent.KEY_NUMPAD6;
    KEY_MAP[KeyEvent.VK_NUMPAD7] = KeyboardInputEvent.KEY_NUMPAD7;
    KEY_MAP[KeyEvent.VK_NUMPAD8] = KeyboardInputEvent.KEY_NUMPAD8;
    KEY_MAP[KeyEvent.VK_NUMPAD9] = KeyboardInputEvent.KEY_NUMPAD9;
    KEY_MAP[KeyEvent.VK_O] = KeyboardInputEvent.KEY_O;
    KEY_MAP[KeyEvent.VK_OPEN_BRACKET] = KeyboardInputEvent.KEY_LBRACKET;
    KEY_MAP[KeyEvent.VK_P] = KeyboardInputEvent.KEY_P;
    KEY_MAP[KeyEvent.VK_PAGE_DOWN] = KeyboardInputEvent.KEY_NEXT;
    KEY_MAP[KeyEvent.VK_PAGE_UP] = KeyboardInputEvent.KEY_PRIOR;
    KEY_MAP[KeyEvent.VK_PAUSE] = KeyboardInputEvent.KEY_PAUSE;
    KEY_MAP[KeyEvent.VK_PERIOD] = KeyboardInputEvent.KEY_PERIOD;
    KEY_MAP[KeyEvent.VK_Q] = KeyboardInputEvent.KEY_Q;
    KEY_MAP[KeyEvent.VK_R] = KeyboardInputEvent.KEY_R;
    KEY_MAP[KeyEvent.VK_RIGHT] = KeyboardInputEvent.KEY_RIGHT;
    KEY_MAP[KeyEvent.VK_S] = KeyboardInputEvent.KEY_S;
    KEY_MAP[KeyEvent.VK_SCROLL_LOCK] = KeyboardInputEvent.KEY_SCROLL;
    KEY_MAP[KeyEvent.VK_SEMICOLON] = KeyboardInputEvent.KEY_SEMICOLON;
    KEY_MAP[KeyEvent.VK_SEPARATOR] = KeyboardInputEvent.KEY_DECIMAL;
    KEY_MAP[KeyEvent.VK_SLASH] = KeyboardInputEvent.KEY_SLASH;
    KEY_MAP[KeyEvent.VK_SPACE] = KeyboardInputEvent.KEY_SPACE;
    KEY_MAP[KeyEvent.VK_STOP] = KeyboardInputEvent.KEY_STOP;
    KEY_MAP[KeyEvent.VK_SUBTRACT] = KeyboardInputEvent.KEY_SUBTRACT;
    KEY_MAP[KeyEvent.VK_T] = KeyboardInputEvent.KEY_T;
    KEY_MAP[KeyEvent.VK_TAB] = KeyboardInputEvent.KEY_TAB;
    KEY_MAP[KeyEvent.VK_U] = KeyboardInputEvent.KEY_U;
    KEY_MAP[KeyEvent.VK_UP] = KeyboardInputEvent.KEY_UP;
    KEY_MAP[KeyEvent.VK_V] = KeyboardInputEvent.KEY_V;
    KEY_MAP[KeyEvent.VK_W] = KeyboardInputEvent.KEY_W;
    KEY_MAP[KeyEvent.VK_X] = KeyboardInputEvent.KEY_X;
    KEY_MAP[KeyEvent.VK_Y] = KeyboardInputEvent.KEY_Y;
    KEY_MAP[KeyEvent.VK_Z] = KeyboardInputEvent.KEY_Z;
  }

}