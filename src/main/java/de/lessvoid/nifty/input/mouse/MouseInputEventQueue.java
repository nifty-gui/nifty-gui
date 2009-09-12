package de.lessvoid.nifty.input.mouse;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * MouseInputEventQueue.
 * @author void
 */
public class MouseInputEventQueue {
  private Queue < MouseInputEvent > queue = new LinkedList < MouseInputEvent >();
  private int lastMouseX = 0;
  private int lastMouseY = 0;
  private boolean lastMouseDown = false;
  private int displayHeight;

  public MouseInputEventQueue(final int displayHeightParam) {
    displayHeight = displayHeightParam;
  }

  public MouseInputEvent peek() {
    return queue.peek();
  }

  public MouseInputEvent poll() {
    return queue.poll();
  }

  public void reset() {
    queue.clear();
    lastMouseDown = false;
  }

  public void process(final List < MouseInputEvent > mouseEvents) {
    if (!mouseEvents.isEmpty()) {
      for (MouseInputEvent mouse : mouseEvents) {
        processInternal(mouse);
      }
    } else {
      if (lastMouseDown) {
        forwardEventToNifty(lastMouseX, lastMouseY, lastMouseDown);
      }
    }
  }

  void processInternal(final MouseInputEvent mouse) {
    int mouseX = mouse.getMouseX();
    int mouseY = displayHeight - mouse.getMouseY();
    boolean leftButton = mouse.isLeftButton();

    if (mouseX != lastMouseX || mouseY != lastMouseY || leftButton != lastMouseDown) {
      forwardEventToNifty(mouseX, mouseY, leftButton);
    }
  }

  void forwardEventToNifty(int mouseX, int mouseY, boolean leftButton) {
    lastMouseX = mouseX;
    lastMouseY = mouseY;
    lastMouseDown = leftButton;
    queue.add(new MouseInputEvent(mouseX, mouseY, leftButton));
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("MouseEvents: size: " + queue.size());
    for (MouseInputEvent e : queue) {
      buffer.append(" [" + e.getMouseX() + "," + e.getMouseY() + "," + e.isLeftButton() + "]");
    }
    return buffer.toString();
  }

  public int getLastMouseX() {
    return lastMouseX;
  }

  public int getLastMouseY() {
    return lastMouseY;
  }

  public boolean isLastMouseDown() {
    return lastMouseDown;
  }
}
