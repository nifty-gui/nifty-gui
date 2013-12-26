package de.lessvoid.nifty.java2d.input;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import javax.annotation.Nonnull;
import java.awt.event.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputSystemAwtImpl implements InputSystem, MouseMotionListener,
    MouseListener, KeyListener {

  @Nonnull
  private final ConcurrentLinkedQueue<MouseEvent> mouseEvents = new ConcurrentLinkedQueue<MouseEvent>();

  @Nonnull
  private final ConcurrentLinkedQueue<KeyboardInputEvent> keyboardEvents = new
      ConcurrentLinkedQueue<KeyboardInputEvent>();

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader nifty) {
  }

  @Override
  public void forwardEvents(@Nonnull final NiftyInputConsumer inputEventConsumer) {
    MouseEvent mouseEvent = mouseEvents.poll();
    while (mouseEvent != null) {
      inputEventConsumer.processMouseEvent(mouseEvent.getX(), mouseEvent.getY(), 0, mouseEvent.getButton() - 1,
          mouseEvent.getButton() != MouseEvent.NOBUTTON);
      mouseEvent = mouseEvents.poll();
    }

    KeyboardInputEvent keyEvent = keyboardEvents.poll();
    while (keyEvent != null) {
      inputEventConsumer.processKeyboardEvent(keyEvent);
      keyEvent = keyboardEvents.poll();
    }
  }

  @Override
  public void mouseDragged(MouseEvent mouseEvent) {
    mouseEvents.add(mouseEvent);
  }

  @Override
  public void mouseMoved(MouseEvent mouseEvent) {
    mouseEvents.add(mouseEvent);
  }

  @Override
  public void mouseClicked(MouseEvent mouseEvent) {

  }

  @Override
  public void mouseEntered(MouseEvent mouseEvent) {

  }

  @Override
  public void mouseExited(MouseEvent mouseEvent) {

  }

  @Override
  public void mousePressed(@Nonnull MouseEvent mouseEvent) {
    // at the moment we only care about the BUTTON1
    if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
      mouseEvents.add(mouseEvent);
    }
  }

  @Override
  public void mouseReleased(@Nonnull MouseEvent mouseEvent) {
    // at the moment we only care about the BUTTON1
    if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
      mouseEvents.add(mouseEvent);
    }
  }

  @Override
  public void keyPressed(@Nonnull KeyEvent e) {
    handleKeyEvent(e, true);
  }

  @Override
  public void keyReleased(@Nonnull KeyEvent e) {
    handleKeyEvent(e, false);
  }

  private void handleKeyEvent(@Nonnull KeyEvent e, boolean isKeyDown) {
    int newKeyCode = keyCodeConverter.convertToNiftyKeyCode(e.getKeyCode(), e.getKeyLocation());
    keyboardEvents.add(new KeyboardInputEvent(newKeyCode, e
        .getKeyChar(), isKeyDown, e.isShiftDown(), e.isControlDown()));
  }

  final AwtToNiftyKeyCodeConverter keyCodeConverter = new AwtToNiftyKeyCodeConverter();

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void setMousePosition(int x, int y) {
    // TODO Auto-generated method stub
  }
}