package de.lessvoid.nifty.java2d.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;

public class InputSystemAwt implements InputSystem, MouseMotionListener, MouseListener {

  private ArrayList<MouseInputEvent> mouseInputEvents = new ArrayList<MouseInputEvent>();

  @Override
  public List<KeyboardInputEvent> getKeyboardEvents() {
    return new ArrayList<KeyboardInputEvent>();
  }

  @Override
  public List<MouseInputEvent> getMouseEvents() {
    ArrayList<MouseInputEvent> newMouseInputEvents = new ArrayList<MouseInputEvent>(
        mouseInputEvents);
    mouseInputEvents.clear();
    return newMouseInputEvents;
  }

  @Override
  public void mouseDragged(MouseEvent mouseEvent) {

  }

  @Override
  public void mouseMoved(MouseEvent mouseEvent) {
    mouseInputEvents.add(new MouseInputEvent(mouseEvent.getX(),
        mouseEvent.getY(),
        mouseEvent.getButton() == MouseEvent.BUTTON1));
  }

  @Override
  public void mouseClicked(MouseEvent arg0) {

  }

  @Override
  public void mouseEntered(MouseEvent arg0) {

  }

  @Override
  public void mouseExited(MouseEvent arg0) {

  }

  @Override
  public void mousePressed(MouseEvent arg0) {

  }

  @Override
  public void mouseReleased(MouseEvent mouseEvent) {
    mouseInputEvents.add(new MouseInputEvent(mouseEvent.getX(),
        mouseEvent.getY(),
        mouseEvent.getButton() == MouseEvent.BUTTON1));
  }

}