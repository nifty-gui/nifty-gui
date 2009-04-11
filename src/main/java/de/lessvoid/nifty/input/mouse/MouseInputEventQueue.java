package de.lessvoid.nifty.input.mouse;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * MouseInputEventQueue.
 * @author void
 */
public class MouseInputEventQueue {
  public Queue < MouseInputEvent > queue = new LinkedList < MouseInputEvent >();
  private int lastMouseX = 0;
  private int lastMouseY = 0;
  private boolean lastMouseDown = false;
  private int displayWidth;
  private int displayHeight;

  public MouseInputEventQueue(final int displayWidthParam, final int displayHeightParam) {
    displayWidth = displayWidthParam;
    displayHeight = displayHeightParam;
  }

  public MouseInputEvent peek() {
    return queue.peek();
  }

  public MouseInputEvent remove() {
    return queue.remove();
  }

  public void reset() {
    queue.clear();
  }

  public void process(final List < MouseInputEvent > mouseEvents) {
    for (MouseInputEvent mouse : mouseEvents) {
      processInternal(mouse);
    }
  }

  void processInternal(final MouseInputEvent mouse) {
    int mouseX = mouse.getMouseX();
    int mouseY = displayHeight - mouse.getMouseY();
    boolean leftButton = mouse.isLeftButton();

    if (mouseX != lastMouseX || mouseY != lastMouseY || leftButton != lastMouseDown) {
      lastMouseX = mouseX;
      lastMouseY = mouseY;
      lastMouseDown = leftButton;
      queue.add(new MouseInputEvent(mouseX, mouseY, leftButton));
    }
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("MouseEvents: size: " + queue.size());
    for (MouseInputEvent e : queue) {
      buffer.append(" [" + e.getMouseX() + "," + e.getMouseY() + "," + e.isLeftButton() + "]");
    }
    return buffer.toString();
  }
}
