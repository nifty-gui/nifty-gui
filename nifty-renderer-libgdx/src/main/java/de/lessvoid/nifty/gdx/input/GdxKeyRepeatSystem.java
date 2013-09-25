package de.lessvoid.nifty.gdx.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntFloatMap;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Timer;

/**
 * Enables repeating of NON-PRINTABLE keys, which is not supported in LibGDX. Only keys whose character would return
 * true for {@link Character.isISOControl()} are allowed to be used. The reason why is that LibGDX already repeats
 * printable characters for you automatically.
 *
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class GdxKeyRepeatSystem {
  public static final int DEFAULT_KEY_REPEAT_RATE_MILLIS = 100; // For reference by users of this class.
  public static final int DEFAULT_KEY_REPEAT_START_DELAY_MILLIS = 500; // For reference by users of this class.
  private static final float DEFAULT_KEY_REPEAT_START_DELAY_SECONDS = millisToSeconds(DEFAULT_KEY_REPEAT_START_DELAY_MILLIS);
  private static final float DEFAULT_KEY_REPEAT_RATE_SECONDS = millisToSeconds(DEFAULT_KEY_REPEAT_RATE_MILLIS);
  private Input input;
  private GdxInputSystem gdxInputSystem;
  private Timer keyRepeatTimer;
  private IntMap<Timer.Task> keyRepeatTasks;
  private IntArray repeatingKeys;
  private IntFloatMap keyRepeatRates;
  private IntFloatMap keyRepeatStartDelays;
  private Timer.Task currentRepeatingKeyTask;
  private int currentRepeatingGdxKeyCode;
  private int currentRepeatingKeyIndex;
  private char temp;

  public GdxKeyRepeatSystem(final GdxInputSystem gdxInputSystem) {
    input = gdxInputSystem.getInput();
    this.gdxInputSystem = gdxInputSystem;
    keyRepeatTimer = new Timer();
    keyRepeatTasks = new IntMap<Timer.Task>();
    repeatingKeys = new IntArray();
    keyRepeatRates = new IntFloatMap();
    keyRepeatStartDelays = new IntFloatMap();
    currentRepeatingKeyTask = null;
    currentRepeatingGdxKeyCode = 0;
    currentRepeatingKeyIndex = 0;
  }

  /**
   * Get the key repeat rate of the specified NON-PRINTABLE key. Only keys whose character would return true for
   * {@link Character.isISOControl()} are allowed. The reason why is that LibGDX already repeats printable characters
   * for you automatically.
   *
   * @param gdxKeyCode The {@link com.badlogic.gdx.Input.Keys} keyCode of the key to get the repeat rate of. Must be a NON-PRINTABLE key (See above).
   *
   * @return The key repeat rate of the specified key in milliseconds. If no repeat rate was set for that specific key,
   * then the default value will be returned, see {@link #DEFAULT_KEY_REPEAT_RATE_MILLIS}. The value will always be
   * greater than 0.
   */
  public int getKeyRepeatRateMillis(final int gdxKeyCode) {
    checkKeyCode(gdxKeyCode);
    return secondsToMillis(keyRepeatRates.get(gdxKeyCode, DEFAULT_KEY_REPEAT_RATE_SECONDS));
  }

  /**
   * Get the key repeat start delay of the specified NON-PRINTABLE key. Only keys whose character would return true
   * for {@link Character.isISOControl()} are allowed. The reason why is that LibGDX already repeats printable
   * characters for you automatically.
   *
   * @param gdxKeyCode The {@link com.badlogic.gdx.Input.Keys} keyCode of the key to get the repeat start delay of. Must be a NON-PRINTABLE key (See above).
   *
   * @return The key repeat start delay of the specified key in milliseconds. If no repeat start delay was set for that
   * specific key, then the default value will be returned, see {@link #DEFAULT_KEY_REPEAT_START_DELAY_MILLIS}. The
   * value will always be non-negative (may be 0).
   */
  public int getKeyRepeatStartDelayMillis(final int gdxKeyCode) {
    checkKeyCode(gdxKeyCode);
    return secondsToMillis(keyRepeatStartDelays.get(gdxKeyCode, DEFAULT_KEY_REPEAT_START_DELAY_SECONDS));
  }

  /**
   * Check whether the specified NON-PRINTABLE key is set to repeat. Only keys whose character would return true for
   * {@link Character.isISOControl()} are allowed. The reason why is that LibGDX already repeats printable characters
   * for you automatically.
   *
   * @param gdxKeyCode The {@link com.badlogic.gdx.Input.Keys} keyCode of the key to check. Must be a NON-PRINTABLE key (See above).
   *
   * @return The repeat status of the specified key.
   */
  public boolean isRepeatingKey(final int gdxKeyCode) {
    checkKeyCode(gdxKeyCode);
    return repeatingKeys.contains(gdxKeyCode);
  }

  /**
   * Enable or disable key repeat for the specified NON-PRINTABLE key. Only keys whose character would return true for
   * {@link Character.isISOControl()} are allowed. The reason why is that LibGDX already repeats printable characters
   * for you automatically.
   *
   * @param gdxKeyCode The {@link com.badlogic.gdx.Input.Keys} keyCode of the key to repeat. Must be a NON-PRINTABLE key (See above).
   * @param isEnabled Whether to enable or disable key repeat for the specified key.
   */
  public void setKeyRepeat(final int gdxKeyCode, final boolean isEnabled) {
    checkKeyCode(gdxKeyCode);
    if (isEnabled && ! repeatingKeys.contains(gdxKeyCode)) {
      repeatingKeys.add(gdxKeyCode);
      registerKeyRepeatTask(gdxKeyCode);
    } else if (! isEnabled && repeatingKeys.contains(gdxKeyCode)) {
      repeatingKeys.removeValue(gdxKeyCode);
    }
  }

  /**
   * Set the key repeat rate for the specified NON-PRINTABLE key. Only keys whose character would return true for
   * {@link Character.isISOControl()} are allowed. The reason why is that LibGDX already repeats printable characters
   * for you automatically.
   *
   * @param gdxKeyCode The {@link com.badlogic.gdx.Input.Keys} keyCode of the key to set the repeat rate of. Must be a NON-PRINTABLE key (See above).
   * @param repeatRateMillis The repeat rate in milliseconds, must be greater than 0.
   */
  public void setKeyRepeatRate(final int gdxKeyCode, final int repeatRateMillis) {
    checkRepeatRate(repeatRateMillis);
    checkKeyCode(gdxKeyCode);
    keyRepeatRates.put(gdxKeyCode, millisToSeconds(repeatRateMillis));
  }

  /**
   * Set the key repeat start delay for the specified NON-PRINTABLE key. Only keys whose character would return true
   * for {@link Character.isISOControl()} are allowed. The reason why is that LibGDX already repeats printable
   * characters for you automatically.
   *
   * @param gdxKeyCode The {@link com.badlogic.gdx.Input.Keys} keyCode of the key to set the repeat start delay of. Must be a NON-PRINTABLE key (See above).
   * @param repeatStartDelayMillis The repeat start delay in milliseconds, must be non-negative.
   */
  public void setKeyRepeatStartDelay(final int gdxKeyCode, final int repeatStartDelayMillis) {
    checkKeyCode(gdxKeyCode);
    checkRepeatRateStartDelay(gdxKeyCode);
    keyRepeatStartDelays.put(gdxKeyCode, millisToSeconds(repeatStartDelayMillis));
  }

  /**
   * Process any key repeats. If a repeating key is being held down, this method will ensure that it gets repeated. If
   * a repeating key is released, this method will ensure that it stops. It should be called once per frame, i.e., in
   * {@link com.badlogic.gdx.ApplicationListener#render()}
   */
  public void update() {
    for (currentRepeatingKeyIndex = 0; currentRepeatingKeyIndex < repeatingKeys.size; ++currentRepeatingKeyIndex) {
      updateCurrentRepeatingKey();
      updateCurrentRepeatingKeyTask();
      updateCurrentRepeatingKeySchedule();
    }
  }

  // internal implementations

  private void updateCurrentRepeatingKey() {
    currentRepeatingGdxKeyCode = repeatingKeys.get(currentRepeatingKeyIndex);
  }

  private void updateCurrentRepeatingKeyTask() {
    currentRepeatingKeyTask = keyRepeatTasks.get(currentRepeatingGdxKeyCode);
  }

  private void updateCurrentRepeatingKeySchedule() {
    if (input.isKeyPressed(currentRepeatingGdxKeyCode)) {
      scheduleCurrentRepeatingKeyTask();
    } else {
      cancelCurrentRepeatingKeyTask();
    }
  }

  private void scheduleCurrentRepeatingKeyTask() {
    if (! currentRepeatingKeyTask.isScheduled()) {
      keyRepeatTimer.scheduleTask(
              currentRepeatingKeyTask,
              keyRepeatStartDelays.get(currentRepeatingGdxKeyCode, DEFAULT_KEY_REPEAT_START_DELAY_SECONDS),
              keyRepeatRates.get(currentRepeatingGdxKeyCode, DEFAULT_KEY_REPEAT_RATE_SECONDS));
    }
  }

  private void cancelCurrentRepeatingKeyTask() {
    if (currentRepeatingKeyTask.isScheduled()) {
      currentRepeatingKeyTask.cancel();
    }
  }

  private void registerKeyRepeatTask(final int gdxKeyCode) {
    if (! keyRepeatTasks.containsKey(gdxKeyCode)) {
      keyRepeatTasks.put(gdxKeyCode, new Timer.Task() {
        @Override
        public void run() {
          if (input.isKeyPressed(gdxKeyCode)) gdxInputSystem.keyDown(gdxKeyCode);
        }
      });
    }
  }

  private void checkKeyCode(final int gdxKeyCode) throws GdxRuntimeException {
    switch (gdxKeyCode) {
        case Input.Keys.NUM_0:
        case Input.Keys.NUMPAD_0: {
          temp = '0';
          break;
      } case Input.Keys.NUM_1:
        case Input.Keys.NUMPAD_1: {
          temp = '1';
          break;
      } case Input.Keys.NUM_2:
        case Input.Keys.NUMPAD_2: {
          temp = '2';
          break;
      } case Input.Keys.NUM_3:
        case Input.Keys.NUMPAD_3: {
          temp = '3';
          break;
      } case Input.Keys.NUM_4:
        case Input.Keys.NUMPAD_4: {
          temp = '4';
          break;
      } case Input.Keys.NUM_5:
        case Input.Keys.NUMPAD_5: {
          temp = '5';
          break;
      } case Input.Keys.NUM_6:
        case Input.Keys.NUMPAD_6: {
          temp = '6';
          break;
      } case Input.Keys.NUM_7:
        case Input.Keys.NUMPAD_7: {
          temp = '7';
          break;
      } case Input.Keys.NUM_8:
        case Input.Keys.NUMPAD_8: {
          temp = '8';
          break;
      } case Input.Keys.NUM_9:
        case Input.Keys.NUMPAD_9: {
          temp = '9';
          break;
      } case Input.Keys.A: {
          temp = 'A';
          break;
      } case Input.Keys.APOSTROPHE: {
          temp = '\'';
          break;
      } case Input.Keys.AT: {
          temp = '@';
          break;
      } case Input.Keys.B: {
          temp = 'B';
          break;
      } case Input.Keys.BACKSLASH: {
          temp = '\\';
          break;
      } case Input.Keys.C: {
          temp = 'C';
          break;
      } case Input.Keys.COMMA: {
          temp = ',';
          break;
      } case Input.Keys.D: {
          temp = 'D';
          break;
      } case Input.Keys.E: {
          temp = 'E';
          break;
      } case Input.Keys.EQUALS: {
          temp = '=';
          break;
      } case Input.Keys.F: {
          temp = 'F';
          break;
      } case Input.Keys.G: {
          temp = 'G';
          break;
      } case Input.Keys.GRAVE: {
          temp = '`';
          break;
      } case Input.Keys.H: {
          temp = 'H';
          break;
      } case Input.Keys.I: {
          temp = 'I';
          break;
      } case Input.Keys.J: {
          temp = 'J';
          break;
      } case Input.Keys.K: {
          temp = 'K';
          break;
      } case Input.Keys.L: {
          temp = 'L';
          break;
      } case Input.Keys.LEFT_BRACKET: {
          temp = '[';
          break;
      } case Input.Keys.M: {
          temp = 'M';
          break;
      } case Input.Keys.MINUS: {
          temp = '-';
          break;
      } case Input.Keys.N: {
          temp = 'N';
          break;
      } case Input.Keys.O: {
          temp = 'O';
          break;
      } case Input.Keys.P: {
          temp = 'P';
          break;
      } case Input.Keys.PERIOD: {
          temp = '.';
          break;
      } case Input.Keys.PLUS: {
          temp = '+';
          break;
      } case Input.Keys.POUND: {
          temp = '#';
          break;
      } case Input.Keys.Q: {
          temp = 'Q';
          break;
      } case Input.Keys.R: {
          temp = 'R';
          break;
      } case Input.Keys.RIGHT_BRACKET: {
          temp = ']';
          break;
      } case Input.Keys.S: {
          temp = 'S';
          break;
      } case Input.Keys.SEMICOLON: {
          temp = ';';
          break;
      } case Input.Keys.SLASH: {
          temp = '/';
          break;
      } case Input.Keys.SPACE: {
          temp = ' ';
          break;
      } case Input.Keys.STAR: {
          temp = '*';
          break;
      } case Input.Keys.T: {
          temp = 'T';
          break;
      } case Input.Keys.U: {
          temp = 'U';
          break;
      } case Input.Keys.V: {
          temp = 'V';
          break;
      } case Input.Keys.W: {
          temp = 'W';
          break;
      } case Input.Keys.X: {
          temp = 'X';
          break;
      } case Input.Keys.Y: {
          temp = 'Y';
          break;
      } case Input.Keys.Z: {
          temp = 'Z';
          break;
      } case Input.Keys.ANY_KEY: {
          throw new GdxRuntimeException ("Only key codes whose character would return true for " +
                  "Character.isISOControl() are allowed.\nThe reason why is that LibGDX already repeats printable " +
                  "characters for you automatically.\nOffending key code: " + gdxKeyCode + ", which represents the " +
                  "special LibGDX constant: com.badlogic.gdx.Input.Keys.ANY_KEY.");
      } default: {
          return;
      }
    }

    throw new GdxRuntimeException ("Only key codes whose character would return true for " +
            "Character.isISOControl() are allowed.\nThe reason why is that LibGDX already repeats printable " +
            "characters for you automatically.\nOffending key code: " + gdxKeyCode + ", which represents the " +
            "printable character: " + temp);
  }

  private static void checkRepeatRate(final int repeatRateMillis) throws GdxRuntimeException {
    if (repeatRateMillis <= 0) {
      throw new GdxRuntimeException("Key repeat rate must be greater than 0 milliseconds.");
    }
  }

  private static void checkRepeatRateStartDelay(final int repeatStartDelayMillis) throws GdxRuntimeException {
    if (repeatStartDelayMillis < 0) {
      throw new GdxRuntimeException("Key repeat start delay cannot be less than 0 milliseconds.");
    }
  }

  private static float millisToSeconds(int milliseconds) {
    return milliseconds / 1000.0f;
  }

  private static int secondsToMillis(float seconds) {
    return (int) (seconds * 1000);
  }
}
