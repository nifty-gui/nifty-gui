package de.lessvoid.nifty.input.mapping;

import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DefaultScreenMapping implements NiftyInputMapping {

  @Override
  @Nullable
  public NiftyStandardInputEvent convert(@Nonnull final KeyboardInputEvent inputEvent) {
    if (inputEvent.isKeyDown()) {
      if (inputEvent.getKey() == KeyboardInputEvent.KEY_ESCAPE) {
        return NiftyStandardInputEvent.Escape;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_LEFT) {
        return NiftyStandardInputEvent.MoveCursorLeft;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_RIGHT) {
        return NiftyStandardInputEvent.MoveCursorRight;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_UP) {
        return NiftyStandardInputEvent.MoveCursorUp;
      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_DOWN) {
        return NiftyStandardInputEvent.MoveCursorDown;
      }
    }
    return null;
  }
}
