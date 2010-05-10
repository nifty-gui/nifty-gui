package de.lessvoid.nifty.java2d.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;

public class InputSystemAwtImpl implements InputSystem, MouseMotionListener,
		MouseListener {

	private ConcurrentLinkedQueue<MouseInputEvent> mouseEvents = new ConcurrentLinkedQueue<MouseInputEvent>();
	private ConcurrentLinkedQueue<KeyboardInputEvent> keyboardEvents = new ConcurrentLinkedQueue<KeyboardInputEvent>();

  @Override
  public void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
		MouseInputEvent mouseEvent = mouseEvents.poll();
		while (mouseEvent != null) {
		  inputEventConsumer.processMouseEvent(mouseEvent);
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

	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent) {
		mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent
				.getY(), mouseEvent.getButton() == MouseEvent.BUTTON1));
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
	public void mousePressed(MouseEvent mouseEvent) {

	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent
				.getY(), mouseEvent.getButton() == MouseEvent.BUTTON1));
	}
}