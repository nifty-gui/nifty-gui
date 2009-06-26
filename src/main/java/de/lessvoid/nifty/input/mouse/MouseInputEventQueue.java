package de.lessvoid.nifty.input.mouse;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.lessvoid.nifty.tools.TimeProvider;

/**
 * MouseInputEventQueue.
 * @author void
 */
public class MouseInputEventQueue {
  private static final long REPEATED_CLICK_START_TIME = 150;
  private static final long REPEATED_CLICK_TIME = 50;

  private Queue < MouseInputEvent > queue = new LinkedList < MouseInputEvent >();
  private int lastMouseX = 0;
  private int lastMouseY = 0;
  private boolean lastMouseDown = false;
  private long lastMouseTime;
  private int displayWidth;
  private int displayHeight;
  private TimeProvider timeProvider;
  private long lastRepeatStartTime;

  public MouseInputEventQueue(final int displayWidthParam, final int displayHeightParam, final TimeProvider newTimeProvider) {
    displayWidth = displayWidthParam;
    displayHeight = displayHeightParam;
    timeProvider = newTimeProvider;
  }

  public MouseInputEvent peek() {
    return queue.peek();
  }

  public MouseInputEvent poll() {
    return queue.poll();
  }

  public void reset() {
    queue.clear();
  }

  public void process(final List < MouseInputEvent > mouseEvents) {
    if (!mouseEvents.isEmpty()) {
      for (MouseInputEvent mouse : mouseEvents) {
        processInternal(mouse);
      }
    } else {
      if (lastMouseDown) {
//        long now = timeProvider.getMsTime();
//        long deltaTime = now - lastMouseTime;
//        if (deltaTime > REPEATED_CLICK_START_TIME) {
//          long pastTime = deltaTime - REPEATED_CLICK_START_TIME;
//          long repeatTime = pastTime - lastRepeatStartTime;
//          if (repeatTime > REPEATED_CLICK_TIME) {
//            lastRepeatStartTime = pastTime;
//            System.out.println("repeat");
            forwardEventToNifty(lastMouseX, lastMouseY, lastMouseDown);
//          }
//        }
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
    lastMouseTime = timeProvider.getMsTime();
    //System.out.println(lastMouseX + ", " + lastMouseY + ", " + lastMouseDown);
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
}
