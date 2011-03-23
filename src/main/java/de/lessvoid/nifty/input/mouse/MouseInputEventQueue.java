package de.lessvoid.nifty.input.mouse;

import de.lessvoid.nifty.input.NiftyMouseInputEvent;


/**
 * MouseInputEventQueue.
 * @author void
 */
public class MouseInputEventQueue {
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

  public boolean canProcess(final NiftyMouseInputEvent mouseEvent) {
    hadAnyEvents = true;

    int mouseX = mouseEvent.getMouseX();
    int mouseY = mouseEvent.getMouseY();
    int mouseWheel = mouseEvent.getMouseWheel();
    boolean button0Down = mouseEvent.isButton0Down();
    boolean button1Down = mouseEvent.isButton1Down();
    boolean button2Down = mouseEvent.isButton2Down();

    if (mouseX != lastMouseX ||
        mouseY != lastMouseY ||
        mouseWheel != 0 ||
        button0Down != lastButtonDown0 ||
        button1Down != lastButtonDown1 ||
        button2Down != lastButtonDown2) {
      return true;
    }

    return false;
  }

  public void process(final NiftyMouseInputEvent mouse) {
    mouse.setButton0InitialDown(!lastButtonDown0 && mouse.isButton0Down());
    mouse.setButton1InitialDown(!lastButtonDown1 && mouse.isButton1Down());
    mouse.setButton2InitialDown(!lastButtonDown2 && mouse.isButton2Down());
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
