package de.lessvoid.nifty.nulldevice;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;

public class NullInputSystem implements InputSystem {
  
  public List<KeyboardInputEvent> getKeyboardEvents() {
    return new ArrayList<KeyboardInputEvent>();
  }
  
  public List<MouseInputEvent> getMouseEvents() {
    return new ArrayList<MouseInputEvent>();
  }  
}
