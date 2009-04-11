package de.lessvoid.nifty.lwjglslick.input;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;

public class LwjglInputSystem implements InputSystem {
  private boolean lastLeftMouseDown = false;
  public void startup() throws Exception {
    Mouse.create();
  }

  public void shutdown() {
    Mouse.destroy();
  }

  public List < MouseInputEvent > getMouseEvents() {
    List < MouseInputEvent > events = new ArrayList < MouseInputEvent >();
    while (Mouse.next()) {
      int mouseX = Mouse.getEventX();
      int mouseY = Mouse.getEventY();
      if (Mouse.getEventButton() == 0) {
        boolean leftMouseButton = Mouse.getEventButtonState();
        if (leftMouseButton != lastLeftMouseDown) {
          lastLeftMouseDown = leftMouseButton;
        }
      }
      MouseInputEvent inputEvent = new MouseInputEvent(mouseX, mouseY, lastLeftMouseDown);
      events.add(inputEvent);
    }
    return events;
  }  
}
