package de.lessvoid.nifty.controls.imageselect;

import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * ImageSelectInputMapping.
 *
 * @author void
 */
public class ImageSelectInputMapping implements NiftyInputMapping {

  @Nullable
  @Override
  public NiftyStandardInputEvent convert(@Nonnull final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      if (inputEvent.getKey() == KeyboardInputEvent.KEY_LEFT) {
        return NiftyStandardInputEvent.MoveCursorLeft;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_RIGHT) {
        return NiftyStandardInputEvent.MoveCursorRight;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_RETURN) {
        return NiftyStandardInputEvent.Activate;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_SPACE) {
        return NiftyStandardInputEvent.Activate;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_TAB) {
        if (inputEvent.isShiftDown()) {
          return NiftyStandardInputEvent.PrevInputElement;
        } else {
          return NiftyStandardInputEvent.NextInputElement;
        }
      }
    }
    return null;
  }
}
