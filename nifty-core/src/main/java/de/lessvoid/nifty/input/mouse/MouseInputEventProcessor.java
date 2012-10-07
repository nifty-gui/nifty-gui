package de.lessvoid.nifty.input.mouse;

import de.lessvoid.nifty.input.NiftyMouseInputEvent;


/**
 * The MouseInputEventProcessor keeps track of mouse event state.
 * @author void
 */
public class MouseInputEventProcessor {
  private int lastMouseX = 0;
  private int lastMouseY = 0;
  private int lastMouseWheel = 0;
  private boolean lastButtonDown0 = false;
  private boolean lastButtonDown1 = false;
  private boolean lastButtonDown2 = false;
  private boolean hadAnyEvents = false;

  public void reset() {
    lastButtonDown0 = false;
    lastButtonDown1 = false;
    lastButtonDown2 = false;
  }

  public void begin() {
    hadAnyEvents = false;
  }

  public void process(final NiftyMouseInputEvent mouse) {
    hadAnyEvents = true;
    mouse.setButton0InitialDown(!lastButtonDown0 && mouse.isButton0Down());
    mouse.setButton0Release(lastButtonDown0 && !mouse.isButton0Down());
    mouse.setButton1InitialDown(!lastButtonDown1 && mouse.isButton1Down());
    mouse.setButton1Release(lastButtonDown1 && !mouse.isButton1Down());
    mouse.setButton2InitialDown(!lastButtonDown2 && mouse.isButton2Down());
    mouse.setButton2Release(lastButtonDown2 && !mouse.isButton2Down());
    lastMouseX = mouse.getMouseX();
    lastMouseY = mouse.getMouseY();
    lastMouseWheel = mouse.getMouseWheel();
    lastButtonDown0 = mouse.isButton0Down();
    lastButtonDown1 = mouse.isButton1Down();
    lastButtonDown2 = mouse.isButton2Down();
  }

  public boolean hasLastMouseDownEvent() {
    return !hadAnyEvents && (lastButtonDown0 || lastButtonDown1 || lastButtonDown2);
  }

  public NiftyMouseInputEvent getLastMouseDownEvent() {
    NiftyMouseInputEvent result = new NiftyMouseInputEvent();
    result.initialize(lastMouseX, lastMouseY, lastMouseWheel, lastButtonDown0, lastButtonDown1, lastButtonDown2);
    return result;
  }
}
