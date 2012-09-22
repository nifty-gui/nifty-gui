package de.lessvoid.nifty.input;


/**
 * NiftyMouseInputEvent.
 *
 * Please Note: This object is pool managed which means it requires a default constructor and an initialize method
 * that resets all (!) member variables. Not doing this will lead to bad things happening while instances are reused!
 * 
 * @author void
 */
public class NiftyMouseInputEvent {
  private int mouseX;
  private int mouseY;
  private int mouseWheel;
  private boolean button0Down;
  private boolean button0InitialDown;
  private boolean button0Release;
  private boolean button1Down;
  private boolean button1InitialDown;
  private boolean button1Release;
  private boolean button2Down;
  private boolean button2InitialDown;
  private boolean button2Release;

  public void initialize(final int mouseX, final int mouseY, final int mouseWheel, final boolean button0Down, final boolean button1Down, final boolean button2Down) {
    this.mouseX = mouseX;
    this.mouseY = mouseY;
    this.mouseWheel = mouseWheel;
    this.button0Down = button0Down;
    this.button0InitialDown = false;
    this.button0Release = false;
    this.button1Down = button1Down;
    this.button1InitialDown = false;
    this.button1Release = false;
    this.button2Down = button2Down;
    this.button2InitialDown = false;
    this.button2Release = false;
  }

  public int getMouseX() {
    return mouseX;
  }

  public int getMouseY() {
    return mouseY;
  }

  public int getMouseWheel() {
    return mouseWheel;
  }

  public boolean isButton0Down() {
    return button0Down;
  }

  public boolean isButton1Down() {
    return button1Down;
  }

  public boolean isButton2Down() {
    return button2Down;
  }

  public boolean isButton0InitialDown() {
    return button0InitialDown;
  }

  public void setButton0InitialDown(final boolean button0InitialDown) {
    this.button0InitialDown = button0InitialDown;
  }

  public boolean isButton1InitialDown() {
    return button1InitialDown;
  }

  public void setButton1InitialDown(final boolean button1InitialDown) {
    this.button1InitialDown = button1InitialDown;
  }

  public boolean isButton2InitialDown() {
    return button2InitialDown;
  }

  public void setButton2InitialDown(final boolean button2InitialDown) {
    this.button2InitialDown = button2InitialDown;
  }

  public boolean isButton0Release() {
    return button0Release;
  }

  public void setButton0Release(final boolean button0Release) {
    this.button0Release = button0Release;
  }

  public boolean isButton1Release() {
    return button1Release;
  }

  public void setButton1Release(final boolean button1Release) {
    this.button1Release = button1Release;
  }

  public boolean isButton2Release() {
    return button2Release;
  }

  public void setButton2Release(final boolean button2Release) {
    this.button2Release = button2Release;
  }

  public String toString() {
    return
      "mouseX=" + mouseX + ", " +
      "mouseY=" + mouseY + ", " +
      "mouseWheel=" + mouseWheel + ", " +
      "b0Down=" + button0Down + ", " +
      "b1Down=" + button1Down + ", " +
      "b2Down=" + button2Down + ", " + 
      "b0InitialDown=" + button0InitialDown + ", " +
      "b1InitialDown=" + button1InitialDown + ", " +
      "b2InitialDown=" + button2InitialDown + ", " +
      "b0Release=" + button0Release + ", " +
      "b1Release=" + button1Release + ", " +
      "b2Release=" + button2Release; 
  }
}
