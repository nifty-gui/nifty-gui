package de.lessvoid.nifty.input.mouse;

import java.util.LinkedList;
import java.util.Queue;

/**
 * MouseInputEventQueue.
 * @author void
 */
public class MouseInputEventQueue {

  /**
   * queue.
   */
  public Queue < MouseInputEvent > queue = new LinkedList < MouseInputEvent >();

  /**
   * last mouse x.
   */
  private int lastMouseX = 0;

  /**
   * last mouse y.
   */
  private int lastMouseY = 0;

  /**
   * last mouse down.
   */
  private boolean lastMouseDown = false;

  /**
   * process the given mouse events.
   * @param mouseX mouse x
   * @param mouseY mouse y
   * @param mouseDown mouse down
   */
  public void process(
      final int mouseX,
      final int mouseY,
      final boolean mouseDown) {
    if (mouseX != lastMouseX || mouseY != lastMouseY || mouseDown != lastMouseDown) {
      lastMouseX = mouseX;
      lastMouseY = mouseY;
      lastMouseDown = mouseDown;

      MouseInputEvent event = new MouseInputEvent(mouseX, mouseY, mouseDown);
      queue.add(event);
    }
  }

  /**
   * peek from the queue.
   * @return MouseInputEvent
   */
  public MouseInputEvent peek() {
    return queue.peek();
  }

  /**
   * poll from the queue.
   * @return MouseInputEvent
   */
  public MouseInputEvent remove() {
    return queue.remove();
  }

  /**
   * output event queue for debugging purpose.
   * @return string representing this queue.
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("MouseEvents: size: " + queue.size());
    for (MouseInputEvent e : queue) {
      buffer.append(" [" + e.getMouseX() + "," + e.getMouseY() + "," + e.isLeftButton() + "]");
    }
    return buffer.toString();
  }

  /**
   * reset the queue.
   */
  public void reset() {
    queue.clear();
  }
}
