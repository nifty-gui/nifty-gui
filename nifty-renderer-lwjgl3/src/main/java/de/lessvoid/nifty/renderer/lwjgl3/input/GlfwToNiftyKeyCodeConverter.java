package de.lessvoid.nifty.renderer.lwjgl3.input;

import static org.lwjgl.glfw.GLFW.*;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * Converts GLFW key codes to Nifty key codes. Borrowed and modified from
 * <code>de.lessvoid.nifty.renderer.jogl.input.AwtToNiftyKeyCodeConverter</code>
 * 
 * @author Brian Groenke
 */
class GlfwToNiftyKeyCodeConverter {

  public static int convertToNiftyKeyCode(int keyCode) {
    return KEY_MAP[keyCode];
  }

  private static final int[] KEY_MAP = new int[0xffff];

  static {
    KEY_MAP[GLFW_KEY_0] = KeyboardInputEvent.KEY_0;
    KEY_MAP[GLFW_KEY_1] = KeyboardInputEvent.KEY_1;
    KEY_MAP[GLFW_KEY_2] = KeyboardInputEvent.KEY_2;
    KEY_MAP[GLFW_KEY_3] = KeyboardInputEvent.KEY_3;
    KEY_MAP[GLFW_KEY_4] = KeyboardInputEvent.KEY_4;
    KEY_MAP[GLFW_KEY_5] = KeyboardInputEvent.KEY_5;
    KEY_MAP[GLFW_KEY_6] = KeyboardInputEvent.KEY_6;
    KEY_MAP[GLFW_KEY_7] = KeyboardInputEvent.KEY_7;
    KEY_MAP[GLFW_KEY_8] = KeyboardInputEvent.KEY_8;
    KEY_MAP[GLFW_KEY_9] = KeyboardInputEvent.KEY_9;
    KEY_MAP[GLFW_KEY_A] = KeyboardInputEvent.KEY_A;
    KEY_MAP[GLFW_KEY_KP_ADD] = KeyboardInputEvent.KEY_ADD;
    KEY_MAP[GLFW_KEY_MENU] = KeyboardInputEvent.KEY_RMENU;
    KEY_MAP[GLFW_KEY_B] = KeyboardInputEvent.KEY_B;
    KEY_MAP[GLFW_KEY_BACKSLASH] = KeyboardInputEvent.KEY_BACKSLASH;
    KEY_MAP[GLFW_KEY_BACKSPACE] = KeyboardInputEvent.KEY_BACK;
    KEY_MAP[GLFW_KEY_C] = KeyboardInputEvent.KEY_C;
    KEY_MAP[GLFW_KEY_CAPS_LOCK] = KeyboardInputEvent.KEY_CAPITAL;
    // KEY_MAP[GLFW_KEY_CIRCUMFLEX] = KeyboardInputEvent.KEY_CIRCUMFLEX;
    KEY_MAP[GLFW_KEY_RIGHT_BRACKET] = KeyboardInputEvent.KEY_RBRACKET;
    KEY_MAP[GLFW_KEY_COMMA] = KeyboardInputEvent.KEY_COMMA;
    // KEY_MAP[GLFW_KEY_CONVERT] = KeyboardInputEvent.KEY_CONVERT;
    KEY_MAP[GLFW_KEY_D] = KeyboardInputEvent.KEY_D;
    KEY_MAP[GLFW_KEY_KP_DECIMAL] = KeyboardInputEvent.KEY_DECIMAL;
    KEY_MAP[GLFW_KEY_DELETE] = KeyboardInputEvent.KEY_DELETE;
    KEY_MAP[GLFW_KEY_KP_DIVIDE] = KeyboardInputEvent.KEY_DIVIDE;
    KEY_MAP[GLFW_KEY_DOWN] = KeyboardInputEvent.KEY_DOWN;
    KEY_MAP[GLFW_KEY_E] = KeyboardInputEvent.KEY_E;
    KEY_MAP[GLFW_KEY_END] = KeyboardInputEvent.KEY_END;
    KEY_MAP[GLFW_KEY_ENTER] = KeyboardInputEvent.KEY_RETURN;
    KEY_MAP[GLFW_KEY_EQUAL] = KeyboardInputEvent.KEY_EQUALS;
    KEY_MAP[GLFW_KEY_ESCAPE] = KeyboardInputEvent.KEY_ESCAPE;
    KEY_MAP[GLFW_KEY_F] = KeyboardInputEvent.KEY_F;
    KEY_MAP[GLFW_KEY_F1] = KeyboardInputEvent.KEY_F1;
    KEY_MAP[GLFW_KEY_F10] = KeyboardInputEvent.KEY_F10;
    KEY_MAP[GLFW_KEY_F11] = KeyboardInputEvent.KEY_F11;
    KEY_MAP[GLFW_KEY_F12] = KeyboardInputEvent.KEY_F12;
    KEY_MAP[GLFW_KEY_F13] = KeyboardInputEvent.KEY_F13;
    KEY_MAP[GLFW_KEY_F14] = KeyboardInputEvent.KEY_F14;
    KEY_MAP[GLFW_KEY_F15] = KeyboardInputEvent.KEY_F15;
    KEY_MAP[GLFW_KEY_F2] = KeyboardInputEvent.KEY_F2;
    KEY_MAP[GLFW_KEY_F3] = KeyboardInputEvent.KEY_F3;
    KEY_MAP[GLFW_KEY_F4] = KeyboardInputEvent.KEY_F4;
    KEY_MAP[GLFW_KEY_F5] = KeyboardInputEvent.KEY_F5;
    KEY_MAP[GLFW_KEY_F6] = KeyboardInputEvent.KEY_F6;
    KEY_MAP[GLFW_KEY_F7] = KeyboardInputEvent.KEY_F7;
    KEY_MAP[GLFW_KEY_F8] = KeyboardInputEvent.KEY_F8;
    KEY_MAP[GLFW_KEY_F9] = KeyboardInputEvent.KEY_F9;
    KEY_MAP[GLFW_KEY_G] = KeyboardInputEvent.KEY_G;
    KEY_MAP[GLFW_KEY_H] = KeyboardInputEvent.KEY_H;
    KEY_MAP[GLFW_KEY_HOME] = KeyboardInputEvent.KEY_HOME;
    KEY_MAP[GLFW_KEY_I] = KeyboardInputEvent.KEY_I;
    KEY_MAP[GLFW_KEY_INSERT] = KeyboardInputEvent.KEY_INSERT;
    KEY_MAP[GLFW_KEY_J] = KeyboardInputEvent.KEY_J;
    KEY_MAP[GLFW_KEY_K] = KeyboardInputEvent.KEY_K;
    // KEY_MAP[GLFW_KEY_KATAKANA] = KeyboardInputEvent.KEY_KANA;
    // KEY_MAP[GLFW_KEY_KANA_LOCK] = KeyboardInputEvent.KEY_KANJI;
    KEY_MAP[GLFW_KEY_L] = KeyboardInputEvent.KEY_L;
    KEY_MAP[GLFW_KEY_LEFT] = KeyboardInputEvent.KEY_LEFT;
    KEY_MAP[GLFW_KEY_M] = KeyboardInputEvent.KEY_M;
    KEY_MAP[GLFW_KEY_MINUS] = KeyboardInputEvent.KEY_MINUS;
    KEY_MAP[GLFW_KEY_KP_MULTIPLY] = KeyboardInputEvent.KEY_MULTIPLY;
    KEY_MAP[GLFW_KEY_N] = KeyboardInputEvent.KEY_N;
    KEY_MAP[GLFW_KEY_NUM_LOCK] = KeyboardInputEvent.KEY_NUMLOCK;
    KEY_MAP[GLFW_KEY_KP_0] = KeyboardInputEvent.KEY_NUMPAD0;
    KEY_MAP[GLFW_KEY_KP_1] = KeyboardInputEvent.KEY_NUMPAD1;
    KEY_MAP[GLFW_KEY_KP_2] = KeyboardInputEvent.KEY_NUMPAD2;
    KEY_MAP[GLFW_KEY_KP_3] = KeyboardInputEvent.KEY_NUMPAD3;
    KEY_MAP[GLFW_KEY_KP_4] = KeyboardInputEvent.KEY_NUMPAD4;
    KEY_MAP[GLFW_KEY_KP_5] = KeyboardInputEvent.KEY_NUMPAD5;
    KEY_MAP[GLFW_KEY_KP_6] = KeyboardInputEvent.KEY_NUMPAD6;
    KEY_MAP[GLFW_KEY_KP_7] = KeyboardInputEvent.KEY_NUMPAD7;
    KEY_MAP[GLFW_KEY_KP_8] = KeyboardInputEvent.KEY_NUMPAD8;
    KEY_MAP[GLFW_KEY_KP_9] = KeyboardInputEvent.KEY_NUMPAD9;
    KEY_MAP[GLFW_KEY_O] = KeyboardInputEvent.KEY_O;
    KEY_MAP[GLFW_KEY_LEFT_BRACKET] = KeyboardInputEvent.KEY_LBRACKET;
    KEY_MAP[GLFW_KEY_P] = KeyboardInputEvent.KEY_P;
    KEY_MAP[GLFW_KEY_PAGE_DOWN] = KeyboardInputEvent.KEY_NEXT;
    KEY_MAP[GLFW_KEY_PAGE_UP] = KeyboardInputEvent.KEY_PRIOR;
    KEY_MAP[GLFW_KEY_PAUSE] = KeyboardInputEvent.KEY_PAUSE;
    KEY_MAP[GLFW_KEY_PERIOD] = KeyboardInputEvent.KEY_PERIOD;
    KEY_MAP[GLFW_KEY_Q] = KeyboardInputEvent.KEY_Q;
    KEY_MAP[GLFW_KEY_R] = KeyboardInputEvent.KEY_R;
    KEY_MAP[GLFW_KEY_RIGHT] = KeyboardInputEvent.KEY_RIGHT;
    KEY_MAP[GLFW_KEY_S] = KeyboardInputEvent.KEY_S;
    KEY_MAP[GLFW_KEY_SCROLL_LOCK] = KeyboardInputEvent.KEY_SCROLL;
    KEY_MAP[GLFW_KEY_SEMICOLON] = KeyboardInputEvent.KEY_SEMICOLON;
    // KEY_MAP[GLFW_KEY_SEPARATOR] = KeyboardInputEvent.KEY_DECIMAL;
    KEY_MAP[GLFW_KEY_SLASH] = KeyboardInputEvent.KEY_SLASH;
    KEY_MAP[GLFW_KEY_SPACE] = KeyboardInputEvent.KEY_SPACE;
    // KEY_MAP[GLFW_KEY_STOP] = KeyboardInputEvent.KEY_STOP;
    KEY_MAP[GLFW_KEY_KP_SUBTRACT] = KeyboardInputEvent.KEY_SUBTRACT;
    KEY_MAP[GLFW_KEY_T] = KeyboardInputEvent.KEY_T;
    KEY_MAP[GLFW_KEY_TAB] = KeyboardInputEvent.KEY_TAB;
    KEY_MAP[GLFW_KEY_U] = KeyboardInputEvent.KEY_U;
    KEY_MAP[GLFW_KEY_UP] = KeyboardInputEvent.KEY_UP;
    KEY_MAP[GLFW_KEY_V] = KeyboardInputEvent.KEY_V;
    KEY_MAP[GLFW_KEY_W] = KeyboardInputEvent.KEY_W;
    KEY_MAP[GLFW_KEY_X] = KeyboardInputEvent.KEY_X;
    KEY_MAP[GLFW_KEY_Y] = KeyboardInputEvent.KEY_Y;
    KEY_MAP[GLFW_KEY_Z] = KeyboardInputEvent.KEY_Z;
  }

}